package com.wmp.PublicTools.io;

import com.wmp.PublicTools.printLog.Log;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipPack {

    private static JDialog dialog = new JDialog();
    private static JProgressBar progressBar = new JProgressBar(0,100);
    public static void unzip(String zipFilePath, String destDir) {
        unzip(zipFilePath, destDir, () -> {
            // 运行其他操作
        });
    }

    public static void unzip(String zipFilePath, String destDir, Runnable runnable) {
        //new File(destDir).delete();
        //生成一个弹窗显示解压进度
        Log.info.print("ZipPack-解压", "正在解压...");

        try {
            if (zipFilePath == null || !new File(zipFilePath).exists()) {
                Log.error.print("ZipPack-解压", "找不到压缩包!");
                return;
            }
        }catch (Exception e) {
            Log.error.print("ZipPack-解压", "找不到压缩包!");

            return;
        }

        //生成弹窗
        SwingUtilities.invokeLater(() -> {
            dialog.setTitle("正在解压...");
            dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            dialog.setModal(true);
            dialog.setSize(300, 80);
            dialog.setLocationRelativeTo(null);
            progressBar.setIndeterminate(true);  // 确保在EDT设置进度条
            dialog.add(progressBar);
            dialog.setVisible(true);
        });


        new Thread(() -> {
            try {
                ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath));
                // 解压缩文件
                unzipFiles(zipInputStream, destDir);

                Log.info.print("ZipPack-解压", "解压完成!");
                dialog.setVisible(false);

            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> {
                    dialog.setVisible(false);
                });
                Log.error.print("ZipPack-解压", "解压失败！\n" + e.getMessage());
                throw new RuntimeException(e);
            }
            try {
                runnable.run();
            }catch (Exception e) {
                Log.error.print("ZipPack-解压", "运行失败！\n" + e.getMessage());
                throw new RuntimeException(e);
            }
        }).start();



        //return true;
    }

    private static void unzipFiles(ZipInputStream zipInputStream, String outputFolder) throws IOException {
        byte[] buffer = new byte[1024];
        ZipEntry entry;

        // 遍历压缩文件中的每个文件
        while ((entry = zipInputStream.getNextEntry()) != null) {
            // 处理文件
            String fileName = entry.getName();
            File outputFile = new File(outputFolder + "/" + fileName);

            Log.info.print("ZipPack-解压", "解压文件: " + outputFile);
            // 创建文件夹
            if (entry.isDirectory()) {
                outputFile.mkdirs();
            } else {
                // 创建文件并写入内容
                new File(outputFile.getParent()).mkdirs();
                try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {

                    int bytesRead;
                    while ((bytesRead = zipInputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }
                }
            }

            zipInputStream.closeEntry();
        }
    }

    public static void createZip(String outputPath, String dataPath, String zipName, String... ZipFiles){

        Log.info.print("ZipPack-压缩", "开始压缩...");
        Log.info.print("ZipPack-压缩", "压缩:" + dataPath + "->" + outputPath);
        if (ZipFiles.length != 0){
            Log.info.print("ZipPack-压缩", "要打包的文件:" + Arrays.toString(ZipFiles));

        }else Log.info.print("ZipPack-压缩", "要打包的文件:全部");

        SwingUtilities.invokeLater(() -> {
            dialog.setTitle("正在压缩...");
            dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            dialog.setModal(true);
            dialog.setSize(300, 80);
            dialog.setLocationRelativeTo(null);
            progressBar.setIndeterminate(true);  // 确保在EDT设置进度条
            dialog.add(progressBar);
            dialog.setVisible(true);
        });



        String sourceFolder = dataPath;
        // String zipName = zipName;

        new Thread(() -> {  // 在后台线程执行压缩操作
            try (ZipOutputStream zos = new ZipOutputStream(
                    new FileOutputStream(outputPath + File.separator + zipName))) {

                addFolderToZip(new File(sourceFolder), "", zos, ZipFiles);

                // 压缩完成后更新UI
                Log.info.print("ZipPack-压缩", "压缩完成!");
                dialog.setVisible(false);
            } catch (IOException e) {
                Log.error.print("ZipPack-压缩", "压缩失败！\n" + e.getMessage());
                dialog.setVisible(false);
            }
        }).start();

    }

    // 优化的压缩方法
    private static void addFolderToZip(File folder, String parentPath, ZipOutputStream zos, String... ZipFiles) throws IOException {
        for (File file : Objects.requireNonNull(folder.listFiles())) {

            if (ZipFiles.length != 0){
                boolean b = Arrays.asList(ZipFiles).contains(file.getName());
                if (!b){
                    // 跳过不压缩的文件
                    continue;
                }
            }

            String entryName = parentPath + file.getName();

            Log.info.print("ZipPack-压缩", "压缩文件: " + entryName);
            if (file.isDirectory()) {
                // 处理目录时添加"/"后缀
                zos.putNextEntry(new ZipEntry(entryName + "/"));
                zos.closeEntry();
                addFolderToZip(file, entryName + "/", zos);
            } else {
                try (FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry entry = new ZipEntry(entryName);
                    zos.putNextEntry(entry);

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        zos.write(buffer, 0, bytesRead);
                    }
                    zos.closeEntry();
                }
            }
        }

    }

}
