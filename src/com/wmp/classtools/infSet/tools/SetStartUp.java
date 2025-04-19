package com.wmp.classTools.infSet.tools;

import java.io.*;

public class SetStartUp {

    // 注册表项路径（当前用户）
    private static final String REGISTRY_PATH =
            "HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Run";
    // 自启动项名称
    private static final String ENTRY_NAME = "ClassTools";

    /**
     * 添加程序到开机自启动
     * @param programPath 程序的完整路径（需用双引号包裹路径）
     */
    public static void enableAutoStart(String programPath) {
        System.out.println("programPath" + programPath);
        try {
            String command = String.format("reg add \"%s\" /v \"%s\" /t REG_SZ /d \"%s\" /f",
                    REGISTRY_PATH, ENTRY_NAME, programPath);
            executeCommand(command);
            System.out.println("已添加开机自启动！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 移除开机自启动项
     */
    public static void disableAutoStart() {
        try {
            String command = String.format("reg delete \"%s\" /v \"%s\" /f",
                    REGISTRY_PATH, ENTRY_NAME);
            executeCommand(command);
            System.out.println("已移除开机自启动！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查自启动项是否存在
     */
    public static boolean isAutoStartEnabled() {
        try {
            String command = String.format("reg query \"%s\" /v \"%s\"",
                    REGISTRY_PATH, ENTRY_NAME);
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(ENTRY_NAME)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 执行命令并等待完成
     */
    private static void executeCommand(String command) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        while (reader.readLine() != null) {
            // 输出命令执行结果
            System.out.println(reader.readLine());
        }
    }

    /**
     * 获取当前JAR文件的路径（需在可执行JAR中运行）
     */
    public static String getFilePath() {
        String path = System.getProperty("user.dir");
        /*String path = Test03.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getRunPath();
          // 处理路径中的空格和特殊字符
        return "\"" + path.replace("file:/", "").replace("/", "\\") + "\"";      */
        File appPath = new File(path);
        File[] files = appPath.listFiles();
        for (File file : files) {
            if (file.getName().equals("ClassTools.exe")) {
                return file.getPath();
            }
            if (file.getName().equals("ClassTools.jar")) {
                return file.getPath();
            }
        }
        return null;
    }

    /*public static void main(String[] args) {
        String jarPath = getJarFilePath();
        if (isAutoStartEnabled()) {
            disableAutoStart();// 移除自启动
        } else {
            //enableAutoStart("javaw -jar " + jarPath); // 使用javaw避免黑窗口
            enableAutoStart("\"" + jarPath + "\"");
        }
    }*/
}
