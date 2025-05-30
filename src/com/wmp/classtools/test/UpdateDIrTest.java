package com.wmp.classTools.test;

import com.wmp.PublicTools.io.GetPath;

import java.io.File;

public class UpdateDIrTest {
    public static void main(String[] args) {
        try {
            File programDir = new File(GetPath.getAppPath(0));
            System.out.println("程序所在目录: " + programDir.getAbsolutePath());
            // 在此处继续你的程序逻辑
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
