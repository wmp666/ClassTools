package com.wmp.PublicTools;

import com.wmp.PublicTools.printLog.Log;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class OpenInExp {
    public static void open(String path) {
        try {
            // 获取可靠的项目工作目录
            File targetDir = new File(path);

            // 校验父目录有效性
            if (targetDir == null || !targetDir.exists()) {
                Log.error.print("OpenInExp", "不存在的位置");
                throw new IOException("Parent directory does not exist");
            }
            // 使用跨平台方式打开文件管理器
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(targetDir);
            } else {
                // 兼容回退方案
                Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", targetDir.getAbsolutePath()});
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
