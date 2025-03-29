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
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetNewerVersion {

    private static JPanel view;

    //private static JDialog dialog;
    private static String versionContent = "";
    private static String apiUrl = "https://api.kkgithub.com/repos/wmp666/ClassTools/releases/latest";
    private static String downloadUrl = "null";
    //https://kkgithub.com/wmp666/ClassTools/releases/download/1.6.4/ClassTools.jar

    public static String getLatestVersion() throws Exception {
        SslUtils.ignoreSsl();
        String url = apiUrl;

        try {
            // 1. 获取网页内容
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0") // 设置UA
                    .timeout(10000)  // 超时设置
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
            String result = getTest(doc, "tag_name");

            downloadUrl = "https://kkgithub.com/wmp666/ClassTools/releases/download/"+ result +"/ClassTools.jar";

            versionContent = getTest(doc, "body").replace("\\r\\n", "\n");
            //System.out.println(doc.body());

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getTest(Document doc, String test) {
        Element body = doc.body();

        // 正则表达式匹配大括号内的内容
        //"tag_name":"1.5.2"
        Pattern pattern = Pattern.compile("\"" + test + "\":\"([^\"]+)");
        Matcher matcher = pattern.matcher(body.html());

        String result = "";
        while (matcher.find()) {
            result = matcher.group(1);
            //System.out.println("find");
        }
        System.out.println(result);

        System.out.println(body);
        return result;
    }

    public static void checkForUpdate(Window dialog, JPanel panel) {

        if (panel != null){
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

                if (isNewerVersion(latestVersion, Main.version)) {
                    int result = JOptionPane.showConfirmDialog(dialog,
                            "发现新版本 " + latestVersion + "，是否下载？\n" + versionContent ,
                            "发现更新", JOptionPane.YES_NO_OPTION);

                    if (result == JOptionPane.YES_OPTION) {
                        downloadUpdate(dialog, view, downloadUrl);
                        //openGithubRelease();
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

    public static void downloadUpdate(Window parent, JPanel panel, String downloadUrl) {
        new Thread(() -> {

            JDialog progressDialog = new JDialog();
            JLabel label = new JLabel("正在下载更新，请稍候...");
            JProgressBar progressBar = new JProgressBar(0, 100);

            if (panel == null){
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


            if (panel == null){
                progressDialog.add(label);
                progressDialog.add(progressBar);
                new Thread(() -> progressDialog.setVisible(true)).start();
            }else{
                panel.add(label);
                panel.add(progressBar);
                panel.setVisible(true);
            }


            try {
                label.setText("正在创建目标目录，请稍候...");
                //设置为不确定
                Objects.requireNonNullElse(panel, progressDialog).repaint();


                // 创建目标目录
                File appDir = new File(System.getenv ("LOCALAPPDATA") + "/ClassTools/UpdateTemp");
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

                progressBar.setValue(25);

                conn.setRequestProperty("Accept", "application/octet-stream"); // 获取二进制文件
                conn.setInstanceFollowRedirects(true); // 启用自动重定向

                progressBar.setValue(50);

                // 添加超时设置
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(30000);

                progressBar.setValue(75);


                try (InputStream in = conn.getInputStream();
                     FileOutputStream out = new FileOutputStream( appDir.getAbsolutePath() + "/ClassTools.jar")) {
                    System.out.println(out);
                    byte[] buffer = new byte[1024 * 512];
                    int read;
                    long total = 0;
                    long fileSize = conn.getContentLength();

                    progressBar.setValue(100);

                    label.setText("正在下载更新文件 0KB/0KB");
                    progressBar.setValue(0);
                    Objects.requireNonNullElse(panel, progressDialog).repaint();

                    while ((read = in.read(buffer)) > 0) {
                        //System.out.println("write" + Arrays.toString(buffer));
                        out.write(buffer, 0, read);
                        total += read;
                        int progress = (int) (total * 100 / fileSize);
                        //显示下载速度
                        String v = (total / 1024) + "KB/" + (fileSize / 1024) + "KB" + " 速度:" + (read / 1024) + "KB";
                        System.out.println(v + " " + buffer.length / 1024);
                        label.setText("正在下载更新文件 " + v);

                        SwingUtilities.invokeLater(() -> progressBar.setValue(progress));
                    }

                    in.close();

                    SwingUtilities.invokeLater(() -> {

                        label.setText("正在更新，请稍候...");
                        progressBar.setValue(0);
                        Objects.requireNonNullElse(panel, progressDialog).repaint();
                        //将文件移至app
                        try {
                            File sourceFile = new File(appDir.getAbsolutePath() + "/ClassTools.jar");

                            File targetFile = new File("app/ClassTools.jar");
                            targetFile.mkdirs();

                            FileOutputStream targetOut = new FileOutputStream(targetFile);
                            FileInputStream sourceIn = new FileInputStream(sourceFile);

                            byte[] temp = new byte[1024 * 10];
                            int total2 = 0;

                            while (true) {
                                int i = sourceIn.read(temp);
                                if (i == -1) break;
                                targetOut.write(temp,0 , i);
                                total2 += i;
                                // 更新进度条
                                int finalTotal = total2;
                                SwingUtilities.invokeLater(() -> progressBar.setValue(finalTotal));
                            }

                            //Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                            //progressBar.setValue(100);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        Objects.requireNonNullElse( panel, progressDialog).setVisible(false);
                        JOptionPane.showMessageDialog(parent, "下载完成！请重启应用");
                        //System.exit(0);
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
