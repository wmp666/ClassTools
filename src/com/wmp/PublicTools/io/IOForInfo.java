package com.wmp.PublicTools.io;

import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.frame.tools.cookie.CookieSets;

import javax.swing.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class IOForInfo {

    private final File file;

    public IOForInfo(File file) {
        this.file = file;
    }

    public IOForInfo(String file) {
        this.file = new File(file);
    }

    public IOForInfo(URI file) {
        this.file = new File(file);
    }

    public static String[] getInfo(String file) {
        String infos = getInfos(file);
        if (infos.equals("err")) {
            return new String[]{"err"};
        }
        return infos.split("\n");
    }
    public static String[] getInfo(URL file) {
        String infos = getInfos(file);
        if (infos.equals("err")) {
            return new String[]{"err"};
        }
        return infos.split("\n");
    }

    public static String getInfos(String file){
        try {
            File file1 = new File(file);
            if (!file1.exists()) return "err";
            return getInfos(file1.toURI().toURL());
        } catch (MalformedURLException e) {
            Log.err.print(IOForInfo.class, file + "文件读取失败", e);
            return "err";
        }
    }
    public static String getInfos(URL file) {
        try { // 明确指定编码
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            file.openStream(), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (String line; (line = reader.readLine()) != null; ) {
                if (line.startsWith("#")){
                    continue;
                }
                if (line.isEmpty()){
                    continue;
                }
                sb.append(line).append("\n");
            }
            Log.info.print("IOForInfo-获取数据", "数据内容: " + sb);
            return sb.toString();// 读取第一行
        } catch (IOException e) {
            Log.err.print(IOForInfo.class, file.getPath() + "文件读取失败", e);
        }
        return null;
    }

    public String[] getInfo() throws IOException {
        String s = getInfos();
        if (s.equals("err")) {
            return new String[]{"err"};
        }
        return s.split("\n");
    }

    public String getInfos() throws IOException {
        if (!file.exists()) {
            if (!creativeFile(file)) {
                Log.err.print(IOForInfo.class, file.getPath() + "文件无法创建");
                return "err";
            }
        }


        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file), StandardCharsets.UTF_8))) { // 明确指定编码

            Log.info.print("IOForInfo-获取数据", file.getPath() + "文件开始读取");
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            String s = "";
            if (!content.isEmpty()) {
                //去除文字中的空格
                s = content.deleteCharAt(content.length() - 1).toString().trim();
            }

            String replace = s.replace("\n", "\\n");

            Log.info.print("IOForInfo-获取数据", "数据内容: " + replace);
            Log.info.print("IOForInfo-获取数据", file.getPath() + "文件读取完成");
            return !s.isEmpty() ? s : "err";
        } catch (IOException e) {
            Log.err.print(IOForInfo.class, file.getPath() + "文件读取失败", e);
            return "err";
        }
    }

    public void setInfo(String... infos) throws IOException {

        if (!file.exists()) {
            if (!creativeFile(file)) {
                Log.err.print(IOForInfo.class, file.getPath() + "文件无法创建");
                return;
            }
        }

        try (Writer writer = new OutputStreamWriter(
                new FileOutputStream(file), StandardCharsets.UTF_8)) {// 明确指定编码
            Log.info.print("IOForInfo-设置数据", file.getPath() + "文件开始写入");


            String inf = String.join("\n", infos);
            Log.info.print("IOForInfo-设置数据", "数据内容: " + inf);
            writer.write(inf);
            writer.flush();

            // 验证部分也需要使用UTF-8读取
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(file), StandardCharsets.UTF_8))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
            }
        } catch (IOException e) {
            Log.err.print(IOForInfo.class, file.getPath() + "文件写入失败", e);
        }
    }

    private boolean creativeFile(File file) throws IOException {
        Log.info.print("IOForInfo-创建文件", file.getPath() + "文件创建");
        file.getParentFile().mkdirs();
        return file.createNewFile();
    }

    public static Thread deleteDirectoryRecursively(Path path) {
        return deleteDirectoryRecursively(path, null);
    }
    public static Thread deleteDirectoryRecursively(Path path, Runnable callback) {
        Log.info.print("删除文件", "删除");

        Log.info.loading.showDialog("文件删除", "正在删除文件...");

        File file = new File(path.toUri());

        Thread thread = new Thread(() -> {
            new SwingWorker<Void, Void>() {
                //  执行耗时操作
                @Override
                protected Void doInBackground() throws Exception {
                    try {
                        if (file == null || !file.exists()) {
                            Log.err.print(CookieSets.class, "目标不存在");
                            return null;
                        }

                        if (file.isDirectory()) {
                            Files.walk(file.toPath())
                                    .sorted(Comparator.reverseOrder())
                                    .map(Path::toFile)
                                    .forEach(File::delete);
                        }

                        if (file.delete() || !file.exists()) {

                            Log.info.message(null, "IOForInfo-删除文件", "删除文件/文件夹: " + path);
                        } else {
                            String errorType = file.canWrite() ? "文件被占用" : "权限不足";
                            Log.err.print(IOForInfo.class, "删除失败" + errorType);
                        }
                    } catch (Exception e) {
                        Log.err.print(CookieSets.class, "删除失败", e);
                    }
                    return null;
                }

                @Override
                protected void done() {
                    Log.info.loading.closeDialog("文件删除");
                    if (callback != null) {
                        callback.run();
                    }
                }
            }.execute();
        });
        thread.start();

        return thread;
    }

    public static void copyFile(Path source, Path target) throws IOException {

        int id = new Random().nextInt();
        Log.info.loading.showDialog("更新文件" + id, "正在复制文件...");

        File sourceFile =source.toFile();
        File targetFile = target.toFile();

        if(!sourceFile.exists()) {
            Log.info.loading.closeDialog("更新文件" + id);
            Log.err.print(IOForInfo.class, "源文件不存在");
            return;
        }


        if (!targetFile.exists()) {
            targetFile.getParentFile().mkdirs();
            targetFile.createNewFile();
        }


        FileOutputStream targetOut = new FileOutputStream(targetFile);
        FileInputStream sourceIn = new FileInputStream(sourceFile);


        byte[] temp = new byte[1024 * 10];
        int total2 = 0;

        while (true) {
            int i = sourceIn.read(temp);
            if (i == -1) break;
            targetOut.write(temp, 0, i);
            total2 += i;

            Log.info.print("DownloadURLFile-下载", "拷贝进度: " + ((total2 * 100L) / sourceFile.length()));
            // 更新进度条
            int finalTotal = (int) (total2 * 100 / sourceFile.length());
            Log.info.loading.updateDialog("更新文件" + id,  finalTotal);
        }

        Log.info.loading.closeDialog("更新文件" + id);
    }

    @Override
    public String toString() {

        try {
            return "IOForInfo{" +
                    "file=" + file +
                    " Inf=" + Arrays.toString(getInfo()) +
                    '}';
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
