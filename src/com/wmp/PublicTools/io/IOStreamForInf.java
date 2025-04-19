package com.wmp.PublicTools.io;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class IOStreamForInf {

    private final File file;

    public IOStreamForInf(File file) {
        this.file = file;
    }

    public String[] GetInf() throws IOException {
        if (!file.exists()) {
            if (!creativeFile(file)) {
                JOptionPane.showMessageDialog(null, file.getPath() + "文件无法创建", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
                return new String[]{"error"};
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
            if (!content.isEmpty()){
                //去除文字中的空格
                 s = content.deleteCharAt(content.length()-1).toString().trim();
            }

            //System.out.println(file.getRunPath()+ ":"  + s);
            //System.out.println(s.isEmpty());

            return !s.isEmpty() ? s.split("\n") : new String[]{"error"};
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, file.getPath() + "文件读取失败", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
            return new String[]{"error"};
        }
    }


    public boolean SetInf(String... infs) throws IOException {
        if (!file.exists()) {
            if (!creativeFile(file)) {
                JOptionPane.showMessageDialog(null, file.getPath() + "文件无法创建", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        try (Writer writer = new OutputStreamWriter(
                new FileOutputStream(file), StandardCharsets.UTF_8)) {// 明确指定编码
            String inf = String.join("\n", infs);
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
                return false;
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
