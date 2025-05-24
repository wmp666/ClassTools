package com.wmp.PublicTools.io;

import com.wmp.PublicTools.printLog.Log;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

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

    public static String[] getInfo(URL file) {
        String infos = getInfos(file);
        if (infos.equals("err")) {
            return new String[]{"err"};
        }
        return infos.split("\n");
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

            return sb.toString();// 读取第一行
        } catch (IOException e) {
            Log.err.print("IOForInfo-获取数据", file.getPath() + "文件读取失败");
            throw new RuntimeException(e);
        }
    }

    public String[] GetInfo() throws IOException {
        String s = GetInfos();
        if (s.equals("err")) {
            return new String[]{"err"};
        }
        return s.split("\n");
    }

    public String GetInfos() throws IOException {
        if (!file.exists()) {
            if (!creativeFile(file)) {
                Log.err.print("IOForInfo-获取数据", file.getPath() + "文件无法创建");
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
            Log.err.print("IOForInfo-获取数据", file.getPath() + "文件读取失败");
            return "err";
        }
    }
    public void SetInfo(String... infos) throws IOException {

        if (!file.exists()) {
            if (!creativeFile(file)) {
                Log.err.print("IOForInfo-设置数据", file.getPath() + "文件无法创建");
                return;
            }
        }

        try (Writer writer = new OutputStreamWriter(
                new FileOutputStream(file), StandardCharsets.UTF_8)) {// 明确指定编码
            Log.info.print("IOForInfo-设置数据", file.getPath() + "文件开始写入");

            //判断内容是否为空
            if (infos.length == 0 || infos[0].isEmpty()) {
                Log.warn.print("IOForInfo-设置数据", file.getPath() + "文件内容为空");
                return;
            }

            String inf = String.join("\n", infos);
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
            Log.err.print("IOForInfo-设置数据", file.getPath() + "文件写入失败");
        }
    }

    private boolean creativeFile(File file) throws IOException {
        Log.info.print("IOForInfo-创建文件", file.getPath() + "文件创建");
        file.getParentFile().mkdirs();
        return file.createNewFile();
    }

    public static void deleteDirectoryRecursively(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        Log.info.message(null, "IOForInfo-删除文件", "删除文件/文件夹: " + path);
    }

    @Override
    public String toString() {

        try {
            return "IOForInfo{" +
                    "file=" + file +
                    " Inf=" + Arrays.toString(GetInfo()) +
                    '}';
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
