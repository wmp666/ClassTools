package com.wmp.PublicTools.io;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.net.URISyntaxException;

public class GetPath {

    public static final int APPLICATION_PATH = 1;
    public static final int SOURCE_FILE_PATH = 0;


    /**
     * 获取应用程序路径
     *
     * @param type 1: 应用程序路径 0: 源文件路径
     * @return
     */
    public static String getAppPath(int type) {
        try {
            if (type == SOURCE_FILE_PATH) return getProgramDirectory().getPath();
            else return getProgramDirectory().getParent();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取程序真实所在目录（兼容管理员权限模式）
     */
    private static File getProgramDirectory() throws URISyntaxException {
        // 通过类加载器获取代码源位置
        File jarFile = new File(
                GetPath.class
                        .getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .toURI()
        );

        // 如果是JAR文件，返回所在目录；如果是IDE运行，返回class文件目录
        if (jarFile.isFile()) {
            return jarFile.getParentFile();
        } else {
            // 开发环境中返回项目编译输出目录
            return new File(jarFile.getPath());
        }
    }

    public static String getFilePath(Component c, String title, String fileType, String fileName) throws RuntimeException {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(title);

        //将文件过滤器设置为只显示.xlsx 或 文件夹
        chooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    String fileName = f.getName();
                    String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
                    return fileSuffix.equals(fileType);
                }
            }

            @Override
            public String getDescription() {
                return fileName + "文件(*" + fileType + ")";
            }
        });

        //获取文件路径
        if (chooser.showOpenDialog(c) == JFileChooser.APPROVE_OPTION) {
            try {
                String filePath = chooser.getSelectedFile().getAbsolutePath();
                //获取文件名
                String chooseFileName = chooser.getSelectedFile().getName();
                //获取文件名后缀
                String fileSuffix = chooseFileName.substring(chooseFileName.lastIndexOf("."));
                //获取文件名前缀
                String filePrefix = chooseFileName.substring(0, chooseFileName.lastIndexOf("."));

                System.out.println("文件路径：" + filePath + "|文件名: " + chooseFileName + "|文件后缀: " + fileSuffix + "|文件前缀: " + filePrefix);
                return filePath;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return null;
    }

    public static String getDirectoryPath(Component c, String title) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(title);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //返回文件路径
        if (chooser.showOpenDialog(c) == JFileChooser.APPROVE_OPTION) {// 如果点击了"确定"按钮
            try {
                String filePath = chooser.getSelectedFile().getAbsolutePath();
                System.out.println("文件路径：" + filePath);
                return filePath;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return null;
    }
}
