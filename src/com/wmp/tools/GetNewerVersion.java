package com.wmp.tools;

import com.wmp.CTColor;
import com.wmp.Main;
import com.wmp.classtools.frame.AboutDialog;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetNewerVersion {

    //private static JDialog dialog;

    public static String getLatestVersion() throws Exception {
        SslUtils.ignoreSsl();
        String url = "https://api.github.com/repos/wmp666/ClassTools/releases/latest";

        try {
            // 1. 获取网页内容
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0") // 设置UA
                    .timeout(3000)  // 超时设置
                    .ignoreContentType(true) // 忽略内容类型
                    .get();

            // 2. 解析内容
            String title = doc.title();
            System.out.println("网页标题: " + title);

            /*// 3. 提取所有链接
            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String absUrl = link.attr("abs:href");
                System.out.println("发现链接: " + absUrl);
            }
*/
            Element body = doc.body();

            // 正则表达式匹配大括号内的内容
            //"tag_name":"1.5.2"
            Pattern pattern = Pattern.compile("\"tag_name\":\"([^\"]+)");
            Matcher matcher = pattern.matcher(body.html());

            String result = "";
            while (matcher.find()) {
                result = matcher.group(1);
                //System.out.println("find");
            }
            System.out.println(result);

            System.out.println(body);
            //System.out.println(doc.body());

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void checkForUpdate(JDialog dialog) {
        new SwingWorker<Void, Void>() {
            String latestVersion;

            protected Void doInBackground() throws Exception {
                latestVersion = getLatestVersion();
                return null;
            }

            protected void done() {
                if (latestVersion == null) {
                    JOptionPane.showMessageDialog(dialog,
                            "无法检查更新", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (isNewerVersion(latestVersion, Main.version)) {
                    int result = JOptionPane.showConfirmDialog(dialog,
                            "发现新版本 " + latestVersion + "，是否前往下载？",
                            "发现更新", JOptionPane.YES_NO_OPTION);

                    if (result == JOptionPane.YES_OPTION) {
                        downloadUpdate(dialog, "https://github.com/wmp666/ClassTools/releases/latest");
                        openGithubRelease();
                    }
                } else {
                    JOptionPane.showMessageDialog(dialog,
                            "当前已是最新版本", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }.execute();
    }

    private static boolean isNewerVersion(String remote, String local) {
        // 实现版本号比较逻辑（需根据你的版本号格式调整）
        String[] remoteParts = remote.split("\\.");
        String[] localParts = local.split("\\.");
        for (int i = 0; i < Math.min(remoteParts.length, localParts.length); i++) {
            int remotePart = Integer.parseInt(remoteParts[i]);
            int localPart = Integer.parseInt(localParts[i]);
            if (remotePart > localPart) {
                return true;
            } else if (remotePart < localPart) {
                return false;
            }
        }
        // 如果版本号相同，则比较长度
        return remote.compareTo(local) > 0;
    }

    private static void openGithubRelease() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/wmp666/ClassTools/releases/latest"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void downloadUpdate(JDialog parent, String downloadUrl) {
        new Thread(() -> {

            JDialog progressDialog = new JDialog(parent, "下载中...", true);

            try {
                // 创建目标目录
                File appDir = new File("update");
                if (!appDir.exists()) appDir.mkdirs();

                // 设置进度对话框
                JProgressBar progressBar = new JProgressBar(0, 100);
                progressBar.setBackground(CTColor.mainColor);
                progressDialog.add(progressBar);
                progressDialog.setSize(300, 75);
                progressDialog.setLocationRelativeTo(parent);

                /*// 通过API获取准确下载链接（推荐）：
                String apiUrl = "https://api.github.com/repos/wmp666/ClassTools/releases/latest";
                Document apiDoc = Jsoup.connect(apiUrl)
                        .ignoreContentType(true)
                        .get();

                Element jarAsset = apiDoc.selectFirst("a[href$=.jar]");
                String fileUrl = jarAsset.attr("abs:href");*/

                new Thread(() -> {
                    progressDialog.setVisible(true);
                }).start();

                // 替换原有的页面解析逻辑为直接获取最新JAR
                String fileUrl = "https://github.com/wmp666/ClassTools/releases/latest/download/ClassTools.jar";

                // 开始下载
                URL url = new URL(fileUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestProperty("Accept", "application/octet-stream"); // 获取二进制文件
                conn.setInstanceFollowRedirects(true); // 启用自动重定向

                // 添加超时设置
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(30000);


                try (InputStream in = conn.getInputStream();
                     FileOutputStream out = new FileOutputStream("app/ClassTools.jar")) {

                    byte[] buffer = new byte[8192];
                    int read;
                    long total = 0;
                    long fileSize = conn.getContentLength();

                    while ((read = in.read(buffer)) > 0) {
                        //System.out.println("write" + Arrays.toString(buffer));
                        out.write(buffer, 0, read);
                        total += read;
                        int progress = (int) (total * 100 / fileSize);
                        SwingUtilities.invokeLater(() -> progressBar.setValue(progress));
                    }

                    SwingUtilities.invokeLater(() -> {
                        progressDialog.dispose();
                        JOptionPane.showMessageDialog(parent, "下载完成！请重启应用\n并将应用目录下update\\中的ClassTools.jar移至app\\");
                    });
                }
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    progressDialog.dispose();
                    JOptionPane.showMessageDialog(parent, "下载失败: " + ex.getMessage());
                });
            }
        }).start();
    }
}
