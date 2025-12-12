package com.wmp.PublicTools.UITools;

import com.wmp.PublicTools.printLog.Log;

import java.awt.Color;

public class SystemColor {

    public static Color accentColor;
    public static boolean isDarkMode;

    static {
        // 在类加载时自动获取系统主题设置
        accentColor = getAccentColor();
        isDarkMode = checkDarkMode();
    }

    public static Color getAccentColor() {
        try {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                // Windows系统
                Process process = Runtime.getRuntime().exec(
                        "reg query \"HKCU\\Software\\Microsoft\\Windows\\DWM\" /v AccentColor");

                java.io.BufferedReader reader = new java.io.BufferedReader(
                        new java.io.InputStreamReader(process.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("AccentColor")) {
                        String[] parts = line.trim().split("\\s+");
                        String hex = parts[parts.length - 1];

                        if (hex.startsWith("0x")) hex = hex.substring(2);
                        long value = Long.parseLong(hex, 16);

                        // 正确提取ARGB各通道值
                        int alpha = (int)((value >> 24) & 0xFF);
                        int red = (int)(value & 0xFF);
                        int green = (int)((value >> 8) & 0xFF);
                        int blue = (int)((value >> 16) & 0xFF);

                        // 确保所有值都在有效范围内
                        red = Math.max(0, Math.min(255, red));
                        green = Math.max(0, Math.min(255, green));
                        blue = Math.max(0, Math.min(255, blue));


                        return new Color(red, green, blue);
                    }
                }
            }
        } catch (Exception e) {
            Log.err.systemPrint(SystemColor.class, "获取系统主题设置失败", e);
        }
        return new Color(0, 120, 215); // Windows默认蓝色
    }

    public static boolean checkDarkMode() {
        try {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                Process process = Runtime.getRuntime().exec(
                        "reg query \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize\" /v AppsUseLightTheme");

                java.io.BufferedReader reader = new java.io.BufferedReader(
                        new java.io.InputStreamReader(process.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("AppsUseLightTheme")) {
                        String[] parts = line.trim().split("\\s+");
                        String value = parts[parts.length - 1];
                        return "0x0".equals(value) || "0".equals(value);
                    }
                }
            }
        } catch (Exception e) {
            Log.err.systemPrint(SystemColor.class, "获取系统主题设置失败", e);
        }
        return false;
    }

    // 获取颜色的十六进制表示
    public static String getAccentColorHex() {
        return String.format("#%02X%02X%02X",
                accentColor.getRed(),
                accentColor.getGreen(),
                accentColor.getBlue());
    }


}
