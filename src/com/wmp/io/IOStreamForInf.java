package com.wmp.io;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class IOStreamForInf {

    private final File file;

    public IOStreamForInf(File file) {
        this.file = file;
    }

    public String GetInf() throws IOException {
        if (!file.exists()) {
            if (!creativeFile(file)) {
                JOptionPane.showMessageDialog(null, file.getPath() + "文件无法创建", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
                return "error";
            }
        }



        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file), StandardCharsets.UTF_8))) { // 明确指定编码
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            String s = "";
            if (content.length()!=0){
                //去除文字中的空格
                 s = content.deleteCharAt(content.length()-1).toString().trim();
            }


            return !s.isEmpty() ? s : "null";
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, file.getPath() + "文件读取失败", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
            return "error";
        }
    }


    public boolean SetInf(String inf) throws IOException {
        if (!file.exists()) {
            if (!creativeFile(file)) {
                JOptionPane.showMessageDialog(null, file.getPath() + "文件无法创建", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        try (Writer writer = new OutputStreamWriter(
                new FileOutputStream(file), StandardCharsets.UTF_8)) { // 明确指定编码
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
                return content.toString().equals(inf);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "文件写入失败", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean creativeFile(File file) throws IOException {
        file.getParentFile().mkdirs();
        return file.createNewFile();
    }
}
