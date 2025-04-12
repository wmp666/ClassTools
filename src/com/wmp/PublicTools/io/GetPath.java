package com.wmp.PublicTools.io;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

public class GetPath {


    public static String getFilePath(Component c, String title , String fileType) {
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
                return "Excel文件(*"+ fileType +")";
            }
        });

        //获取文件路径
        if (chooser.showOpenDialog(c) == JFileChooser.APPROVE_OPTION) {
            try {
                String filePath = chooser.getSelectedFile().getAbsolutePath();
                //获取文件名
                String fileName = chooser.getSelectedFile().getName();
                //获取文件名后缀
                String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
                //获取文件名前缀
                String filePrefix = fileName.substring(0, fileName.lastIndexOf("."));

                System.out.println("文件路径：" + filePath + "|文件名: " + fileName + "|文件后缀: " + fileSuffix + "|文件前缀: " + filePrefix);
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
        if(chooser.showOpenDialog(c) == JFileChooser.APPROVE_OPTION){// 如果点击了"确定"按钮
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
