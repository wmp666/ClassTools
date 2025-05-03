package com.wmp.PublicTools.io;

import com.wmp.classTools.printLog.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

public class IOStreamForInf {

    private static final Logger log = LoggerFactory.getLogger(IOStreamForInf.class);
    private final File file;

    public IOStreamForInf(File file) {
        this.file = file;
    }

    public String[] GetInf() throws IOException {
        if (!file.exists()) {
            if (!creativeFile(file)) {
                Log.error.print("IOStreamForInf-获取数据", file.getPath() + "文件无法创建");
                return new String[]{"error"};
            }
        }


        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file), StandardCharsets.UTF_8))) { // 明确指定编码

            Log.info.print("IOStreamForInf-获取数据", file.getPath() + "文件开始读取");
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

            Log.info.print("IOStreamForInf-获取数据", "数据内容: " + replace);
            Log.info.print("IOStreamForInf-获取数据", file.getPath() + "文件读取完成");
            return !s.isEmpty() ? s.split("\n") : new String[]{"error"};
        } catch (IOException e) {
            Log.error.print("IOStreamForInf-获取数据", file.getPath() + "文件读取失败");
            return new String[]{"error"};
        }
    }


    public void SetInf(String... infos) throws IOException {

        if (!file.exists()) {
            if (!creativeFile(file)) {
                Log.error.print("IOStreamForInf-设置数据", file.getPath() + "文件无法创建");
                return;
            }
        }

        try (Writer writer = new OutputStreamWriter(
                new FileOutputStream(file), StandardCharsets.UTF_8)) {// 明确指定编码
            Log.info.print("IOStreamForInf-设置数据", file.getPath() + "文件开始写入");

            //判断内容是否为空
            if (infos.length == 0 || infos[0].isEmpty()) {
                Log.warn.print("IOStreamForInf-设置数据", file.getPath() + "文件内容为空");
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
            Log.error.print("IOStreamForInf-设置数据", file.getPath() + "文件写入失败");
        }
    }

    private boolean creativeFile(File file) throws IOException {
        Log.info.print("IOStreamForInf-创建文件", file.getPath() + "文件创建");
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
        Log.info.message(null, "IOStreamForInf-删除文件", "删除文件/文件夹: " + path);
    }

    @Override
    public String toString() {

        try {
            return "IOStreamForInf{" +
                    "file=" + file +
                    " Inf=" + Arrays.toString(GetInf()) +
                    '}';
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
