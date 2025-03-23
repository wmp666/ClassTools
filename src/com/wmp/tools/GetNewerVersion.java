package com.wmp.tools;

import com.wmp.Main;
import com.wmp.classtools.frame.AboutDialog;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
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

            // 3. 提取所有链接
            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String absUrl = link.attr("abs:href");
                System.out.println("发现链接: " + absUrl);
            }

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
}
