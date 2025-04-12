package com.wmp.PublicTools.io;

import javax.swing.*;
import java.io.*;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipPack {

    private static JDialog dialog = new JDialog();
    private static JProgressBar progressBar = new JProgressBar(0,100);
    public static void unzip(String zipFilePath, String destDir) {
        //new File(destDir).delete();
        //生成一个弹窗显示解压进度

        new Thread(() ->{
            dialog.setTitle("正在解压...");
            dialog.setModal(true);
            dialog.setSize(300, 80);
            dialog.setLocationRelativeTo(null);

            progressBar.setIndeterminate(true);

            dialog.add(progressBar);
            dialog.setVisible(true);
        }).start();

        try {
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath));
            // 解压缩文件
            unzipFiles(zipInputStream, destDir);

            System.out.println("Files unzipped successfully!");
        } catch (IOException e) {
            dialog.setVisible(false);
            e.printStackTrace();
        }

        dialog.setVisible(false);

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

    public static boolean createZip(String outputPath, String dataPath, String zipName){

        System.out.println("数据位置:" + dataPath);
        new Thread(() ->{
            dialog.setTitle("正在压缩...");
            dialog.setModal(true);
            dialog.setSize(300, 100);
            dialog.setLocationRelativeTo(null);

            progressBar.setIndeterminate(true);

            dialog.add(progressBar);
            dialog.setVisible(true);
        }).start();

        String sourceFolder = dataPath;
        // String zipName = zipName;

        try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(outputPath + File.separator + zipName));
            addFolderToZip(new File(sourceFolder), "", zos);
            dialog.setVisible(false);
            return true;
        } catch (IOException e) {
            dialog.setVisible(false);
            e.printStackTrace();
            return false;
        }
        //dialog.setVisible(false);
        //return false;
    }

    // 优化的压缩方法
    private static void addFolderToZip(File folder, String parentPath, ZipOutputStream zos) throws IOException {
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            String entryName = parentPath + file.getName();

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
    private static void compressFolder(String sourceFolder, String folderName, ZipOutputStream zipOutputStream) throws IOException {
        File folder = new File(sourceFolder);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 压缩子文件夹
                    compressFolder(file.getAbsolutePath(), folderName + "/" + file.getName(), zipOutputStream);
                } else {
                    // 压缩文件
                    addToZipFile(folderName + "/" + file.getName(), file.getAbsolutePath(), zipOutputStream);
                }
            }
        }
    }

    private static void addToZipFile(String fileName, String fileAbsolutePath, ZipOutputStream zipOutputStream) throws IOException {
        // 创建ZipEntry对象并设置文件名
        ZipEntry entry = new ZipEntry(fileName);
        zipOutputStream.putNextEntry(entry);

        // 读取文件内容并写入Zip文件
        try (FileInputStream fileInputStream = new FileInputStream(fileAbsolutePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                zipOutputStream.write(buffer, 0, bytesRead);
            }
        }

        // 完成当前文件的压缩
        zipOutputStream.closeEntry();
    }
}
