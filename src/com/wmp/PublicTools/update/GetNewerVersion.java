package com.wmp.PublicTools.update;

import com.wmp.CTColor;
import com.wmp.Main;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class GetNewerVersion {

    private static int newerVersion =  1;
    private static int importUpdate = 2;
    private static JPanel view;

    //private static JDialog dialog;
    private static String versionContent = "";
    private static String apiUrl = "https://api.github.com/repos/wmp666/ClassTools/releases/latest";
    private static String downloadUrl = "null";
    //https://github.com/wmp666/ClassTools/releases/download/1.6.4/ClassTools.jar

    public static void getSource(Window dialog, JPanel panel){
        if (panel != null) {
            view = panel;
            view.removeAll();
        }


        new SwingWorker<Void, Void>() {
            String sourceURL;

            protected Void doInBackground() throws Exception {
                sourceURL = getSourceURL();
                return null;
            }

            protected void done() {
                if (sourceURL == null) {
                    JOptionPane.showMessageDialog(dialog,
                            "无法获取下载地址", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                    int result = JOptionPane.showConfirmDialog(dialog,
                            "检测到源代码文件，是否下载？",
                            "发现更新", JOptionPane.YES_NO_OPTION);

                    if (result == JOptionPane.YES_OPTION) {
                        try {
                            downloadSource(sourceURL);
                        } catch (URISyntaxException e) {
                            JOptionPane.showMessageDialog(dialog, "下载失败", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        //openGithubRelease();
                    }
            }
        }.execute();
    }

    public static String getLatestVersion() throws Exception {
        SslUtils.ignoreSsl();
        try {
            // 获取原始JSON响应
            String json = Jsoup.connect(apiUrl)
                    .header("Accept", "application/vnd.github.v3+json")
                    .timeout(10000)
                    .ignoreContentType(true)
                    .execute()
                    .body();

            // 使用JSONObject解析
            JSONObject release = new JSONObject(json);
            String version = release.getString("tag_name");
            versionContent = release.getString("body").replace("\\r\\n", "\n");

            // 获取准确下载地址
            JSONArray assets = release.getJSONArray("assets");
            for (int i = 0; i < assets.length(); i++) {
                JSONObject asset = assets.getJSONObject(i);
                if (asset.getString("name").endsWith(".jar")) {
                    downloadUrl = asset.getString("browser_download_url");
                    break;
                }
            }

            return version;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("版本信息解析失败", e);
        }
    }

    public static String getSourceURL() throws Exception {
        SslUtils.ignoreSsl();
        try {
            // 获取原始JSON响应
            String json = Jsoup.connect(apiUrl)
                    .header("Accept", "application/vnd.github.v3+json")
                    .timeout(10000)
                    .ignoreContentType(true)
                    .execute()
                    .body();

            // 使用JSONObject解析
            JSONObject release = new JSONObject(json);

            System.out.println("原始数据" + json);
            // 获取准确下载地址
            String sourceURL = release.getString("zipball_url");


            return sourceURL;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("信息解析失败", e);
        }
    }

    public static void checkForUpdate(Window dialog, JPanel panel) {

        if (panel != null) {
            view = panel;
            view.removeAll();
        }


        new SwingWorker<Void, Void>() {
            String latestVersion;

            protected Void doInBackground() throws Exception {
                latestVersion = getLatestVersion();
                return null;
            }

            protected void done() {
                if (latestVersion == null) {
                    JOptionPane.showMessageDialog(dialog,
                            "无法检查更新", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int i = isNewerVersion(latestVersion, Main.version);
                if (i == 1) {
                    int result = JOptionPane.showConfirmDialog(dialog,
                            "发现新版本 " + latestVersion + "，是否下载？\n" + versionContent,
                            "发现更新", JOptionPane.YES_NO_OPTION);

                    if (result == JOptionPane.YES_OPTION) {
                        downloadUpdate(dialog, view, downloadUrl);
                        //openGithubRelease();
                    }
                } else if (i == 2) {
                    JOptionPane.showMessageDialog(dialog,
                            "发现新版本 " + latestVersion + "!\n" + versionContent,
                            "发现更新", JOptionPane.INFORMATION_MESSAGE);

                    downloadUpdate(dialog, view, downloadUrl);
                } else {
                    JOptionPane.showMessageDialog(dialog,
                            "当前已是最新版本", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }.execute();
    }

    private static int isNewerVersion(String remote, String local) {
        // 实现版本号比较逻辑（需根据你的版本号格式调整）
        String[] remoteParts = remote.split("\\.");
        String[] localParts = local.split("\\.");
        for (int i = 0; i < Math.min(remoteParts.length, localParts.length); i++) {
            int remotePart = Integer.parseInt(remoteParts[i]);
            int localPart = Integer.parseInt(localParts[i]);
            if (remotePart > localPart) {
                if (i == 0 || i == 1){
                    return importUpdate;
                }else{
                    return newerVersion;
                }

            } else if (remotePart < localPart) {
                return 0;
            }
        }
        // 如果版本号相同，则比较长度
        if (remoteParts.length > localParts.length) {
            return newerVersion;
        }else {
            return 0;
        }
    }


    private static void openGithubRelease() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/wmp666/ClassTools/releases/latest"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String getFileNameFromUrl(String urlString) {
        try {
            URI uri = new URI(urlString);
            String path = uri.getPath();
            if (path == null || path.isEmpty()) return "";

            // 处理编码字符
            String decodedPath = URLDecoder.decode(path, StandardCharsets.UTF_8.name());

            // 分割路径并过滤空段
            String[] segments = decodedPath.split("/");
            for (int i = segments.length - 1; i >= 0; i--) {
                if (!segments[i].isEmpty()) {
                    String fileName = segments[i];
                    // 过滤查询参数（虽然URI.getPath()已处理，但二次确认）
                    int queryIndex = fileName.indexOf('?');
                    return queryIndex == -1 ? fileName : fileName.substring(0, queryIndex);
                }
            }
            return "";
        } catch (URISyntaxException | UnsupportedEncodingException e) {
            return ""; // 或根据业务需求处理异常
        }
    }

    public static void downloadUpdate(Window parent, JPanel panel, String downloadUrl) {
        new Thread(() -> {

            JDialog progressDialog = new JDialog();
            JLabel label = new JLabel("正在下载更新，请稍候...");
            JProgressBar progressBar = new JProgressBar(0, 100);

            if (panel == null) {
                progressDialog.setSize(300, 175);
                progressDialog.setTitle("下载中...");
                progressDialog.setLocationRelativeTo(parent);
                progressDialog.setModal(true);
                progressDialog.setLayout(null);
                progressDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            }


            label.setBounds(10, 10, 300, 20);


            // 设置进度对话框
            progressBar.setForeground(CTColor.mainColor);
            progressBar.setBounds(10, 35, 260, 30);
            // 进度条自适应 作用: 进度条自动滚动
            progressBar.setAutoscrolls(true);


            if (panel == null) {
                progressDialog.add(label);
                progressDialog.add(progressBar);
                new Thread(() -> progressDialog.setVisible(true)).start();
            } else {
                panel.add(label);
                panel.add(progressBar);
                panel.setVisible(true);
            }


            try {
                label.setText("正在创建目标目录，请稍候...");
                //设置为不确定
                progressBar.setIndeterminate(true);
                Objects.requireNonNullElse(panel, progressDialog).repaint();


                // 创建目标目录
                File appDir = new File(Main.TempPath);
                if (!appDir.exists()) appDir.mkdirs();

                /*// 通过API获取准确下载链接（推荐）：
                String apiUrl = "https://api.github.com/repos/wmp666/ClassTools/releases/latest";
                Document apiDoc = Jsoup.connect(apiUrl)
                        .ignoreContentType(true)
                        .get();

                Element jarAsset = apiDoc.selectFirst("a[href$=.jar]");
                String fileUrl = jarAsset.attr("abs:href");*/

                // 替换原有的页面解析逻辑为直接获取最新JAR
                String fileUrl = downloadUrl;

                label.setText("正在初始化更新数据，请稍候...");
                Objects.requireNonNullElse(panel, progressDialog).repaint();

                // 开始下载
                URL url = new URL(fileUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();


                conn.setRequestProperty("Accept", "application/octet-stream"); // 获取二进制文件
                conn.setInstanceFollowRedirects(true); // 启用自动重定向

                // 添加超时设置
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(30000);

                String fileNameFromUrl = getFileNameFromUrl(fileUrl);
                try (InputStream in = conn.getInputStream();
                    FileOutputStream out = new FileOutputStream(appDir.getAbsolutePath() + "/" + fileNameFromUrl)) {
                    System.out.println(out);
                    byte[] buffer = new byte[1024 * 512];
                    int read;
                    long total = 0;
                    long fileSize = conn.getContentLength();


                    label.setText("正在下载更新文件 0KB/0KB");
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(0);
                    Objects.requireNonNullElse(panel, progressDialog).repaint();

                    long startTime = System.currentTimeMillis();
                    while ((read = in.read(buffer)) > 0) {
                        out.write(buffer, 0, read);
                        total += read;

                        // 计算已用时间（秒）
                        long elapsedTime = System.currentTimeMillis() - startTime;
                        double speed = (total / 1024.0) / (elapsedTime / 1000.0); // KB/s

                        int progress = (int) ((total / (double) fileSize) * 100);
                        // 格式化显示
                        String v;
                        //判断total fileSize 是否大于1024KB
                        if (fileSize > 1024*1024) {
                            if (total > 1024*1024){
                                v = String.format("%.2fMB/%.2fMB 速度: %.2fKB/s",
                                        total/1024.0/1024.0,
                                        fileSize/1024.0/1024.0,
                                        speed);
                            }else{
                                v = String.format("%.2fKB/%.2fMB 速度: %.2fKB/s",
                                        total/1024.0,
                                        fileSize/1024.0/1024.0,
                                        speed);
                            }

                        }else {
                            v = String.format("%.2fKB/%.2fKB 速度: %.2fKB/s",
                                    total/1024.0,
                                    fileSize/1024.0,
                                    speed);
                        }


                        label.setText("下载文件 " + v);
                        SwingUtilities.invokeLater(() -> progressBar.setValue(progress));
                    }

                    in.close();

                    label.setText("正在更新，请稍候...");
                    progressBar.setValue(0);
                    Objects.requireNonNullElse(panel, progressDialog).repaint();

                    //将文件移至app
                    try {
                        File sourceFile = new File(appDir.getPath() + "/" + fileNameFromUrl);

                        File targetFile = new File("app/ClassTools.jar");
                        if (!targetFile.exists()){
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
                            // 更新进度条
                            int finalTotal = (int)(total2 * 100 / fileSize);
                            SwingUtilities.invokeLater(() -> progressBar.setValue(finalTotal));
                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    if (panel != null){
                        panel.removeAll();
                    } else if (progressDialog != null) {
                        progressDialog.setVisible(false);
                    }
                    JOptionPane.showMessageDialog(parent, "下载完成！请重启应用");
                    //System.exit(0);
                    SwingUtilities.invokeLater(() -> {

                    });
                }
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    progressDialog.dispose();
                    JOptionPane.showMessageDialog(parent, "下载失败: " + ex.getMessage(), "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    public static void downloadSource(String downloadUrl) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI(downloadUrl));
    }


}
