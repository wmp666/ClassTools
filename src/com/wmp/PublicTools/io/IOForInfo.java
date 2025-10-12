package com.wmp.PublicTools.io;

import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTProgressBar;
import com.wmp.classTools.frame.tools.cookie.CookieSets;

import javax.swing.*;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;

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

            //判断内容是否为空
            if (infos.length == 0 || infos[0].isEmpty()) {
                Log.warn.print("IOForInfo-设置数据", file.getPath() + "文件内容为空");
            }


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

    public static void deleteDirectoryRecursively(Path path) {
        Log.info.print("删除文件", "删除");

        JDialog dialog = new JDialog();

        SwingUtilities.invokeLater(() -> {
            dialog.setSize(300, 80);
            dialog.setTitle("正在删除");
            dialog.setLocationRelativeTo(null);
            dialog.setModal(true);

            CTProgressBar progressBar = new CTProgressBar();
            progressBar.setIndeterminate(true);
            dialog.add(progressBar);

            dialog.setVisible(true);
        });

        File file = new File(path.toUri());

        new Thread(() -> {
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
                            Log.info.message(null, "删除文件", "删除成功");
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
                    dialog.setVisible(false);
                    dialog.dispose();
                }
            }.execute();
        }).start();


        /*if (Files.exists(path)) {
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
        }*/
        Log.info.message(null, "IOForInfo-删除文件", "删除文件/文件夹: " + path);
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
