package com.wmp.PublicTools.io;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.UITools.GetIcon;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTProgressBar;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;

public class DownloadURLFile {
    public static void downloadWebFile(Window parent, JPanel panel, String downloadUrl, String dataPath) {
        downloadWebFile(parent, panel, downloadUrl, dataPath, null);
    }
    
    public static void downloadWebFile(Window parent, JPanel panel, String downloadUrl, String dataPath, String expectedSHA256) {
        int id = new Random().nextInt();

        Log.info.print("DownloadURLFile-下载", "开始下载");

        //JDialog progressDialog = new JDialog();
        JLabel label = new JLabel("正在下载文件，请稍候...");
        label.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.MORE_SMALL));
        CTProgressBar progressBar = new CTProgressBar(0, 100);

        if (panel == null) {
            Log.info.loadingDialog.showDialog("文件下载" + id, "正在下载...");
        }else{
            // 设置进度对话框
            progressBar.setForeground(CTColor.mainColor);
            // 进度条自适应 作用: 进度条自动滚动
            progressBar.setAutoscrolls(true);
        }




        if (panel != null) {
            panel.add(label, BorderLayout.NORTH);
            panel.add(progressBar, BorderLayout.CENTER);
            panel.setVisible(true);
        }


        try {
            Log.info.print("DownloadURLFile-下载", "正在创建目标目录，请稍候...");
            label.setText("正在创建目标目录，请稍候...");
            //设置为不确定
            progressBar.setIndeterminate(true);
            if (panel != null) {
                panel.repaint();
            }


            // 创建缓存目录
            File appDir = new File(CTInfo.TEMP_PATH + "WebTemp\\");
            if (!appDir.exists()) appDir.mkdirs();

            // 替换原有的页面解析逻辑为直接获取最新JAR
            //String fileUrl = downloadUrl.replace("github", "kkgithub");

            Log.info.print("DownloadURLFile-下载", "正在初始化数据，请稍候...");
            if (panel != null) {
                label.setText("正在初始化数据，请稍候...");
                panel.repaint();
            }else{
                Log.info.loadingDialog.updateDialog("文件下载" + id, "正在初始化数据，请稍候...");
            }

            // 开始下载
            URL url = new URL(downloadUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();


            conn.setRequestProperty("Accept", "application/octet-stream"); // 获取二进制文件
            conn.setInstanceFollowRedirects(true); // 启用自动重定向

            // 添加超时设置
            conn.setConnectTimeout(30000);// 设置连接超时时间为30秒
            conn.setReadTimeout(120000);// 设置读取超时时间为120秒

            String fileNameFromUrl = getFileNameFromUrl(downloadUrl);
            try (InputStream in = conn.getInputStream();
                 FileOutputStream out = new FileOutputStream(appDir.getAbsolutePath() + "/" + fileNameFromUrl)) {
                byte[] buffer = new byte[1024 * 512];
                int read;
                long total = 0;
                long fileSize = conn.getContentLength();


                Log.info.print("DownloadURLFile-下载", "正在下载文件 0KB/0KB");

                if (panel != null) {
                    label.setText("正在下载文件 0KB/0KB");
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(0);
                    panel.repaint();
                } else {
                    Log.info.loadingDialog.updateDialog("文件下载" + id, "正在下载文件 0KB/0KB");

                }

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
                    if (fileSize > 1024 * 1024) {
                        if (total > 1024 * 1024) {
                            v = String.format("%.2fMB/%.2fMB 速度: %.2fKB/s",
                                    total / 1024.0 / 1024.0,
                                    fileSize / 1024.0 / 1024.0,
                                    speed);
                        } else {
                            v = String.format("%.2fKB/%.2fMB 速度: %.2fKB/s",
                                    total / 1024.0,
                                    fileSize / 1024.0 / 1024.0,
                                    speed);
                        }

                    } else {
                        v = String.format("%.2fKB/%.2fKB 速度: %.2fKB/s",
                                total / 1024.0,
                                fileSize / 1024.0,
                                speed);
                    }


                    Log.info.print("DownloadURLFile-下载", "下载文件 " + v);
                    label.setText("下载文件 " + v);
                    if (panel == null) {
                        Log.info.loadingDialog.updateDialog("文件下载" + id, v,  progress);

                    }else{
                        SwingUtilities.invokeLater(() -> progressBar.setValue(progress));
                    }
                }

                in.close();

                if (panel == null) {
                    Log.info.loadingDialog.updateDialog("文件下载" + id, "正在拷贝...");
                }else{
                    label.setText("正在拷贝，请稍候...");
                    progressBar.setValue(0);
                    panel.repaint();
                }

                //将文件移至app
                try {
                    File sourceFile = new File(appDir.getPath() + "/" + fileNameFromUrl);
                    FileInputStream sourceIn = new FileInputStream(sourceFile);


                    // 文件完整性校验
                    if (expectedSHA256 != null) {
                        label.setText("正在校验文件完整性...");
                        if (panel == null) {
                            Log.info.loadingDialog.updateDialog("文件下载" + id, "正在校验文件完整性...");
                        }

                        //boolean md5Verified = expectedMD5 == null || FileChecksum.verifyMD5(targetFile, expectedMD5);
                        boolean sha256Verified = expectedSHA256 == null || FileChecksum.verifySHA256(sourceFile, expectedSHA256);

                        if (!sha256Verified) {
                            Log.err.print(parent, "DownloadURLFile-下载", "文件完整性校验失败，请重新下载！");
                            sourceFile.delete(); // 删除损坏的文件
                            return;
                        } else {
                            Log.info.print("DownloadURLFile-下载", "文件完整性校验通过");
                        }
                    }

                    File targetFile = new File(dataPath + "/" + fileNameFromUrl);
                    if (!targetFile.exists()) {
                        targetFile.getParentFile().mkdirs();
                        targetFile.createNewFile();
                    }

                    FileOutputStream targetOut = new FileOutputStream(targetFile);

                    byte[] temp = new byte[1024 * 10];
                    int total2 = 0;

                    while (true) {
                        int i = sourceIn.read(temp);
                        if (i == -1) break;
                        targetOut.write(temp, 0, i);
                        total2 += i;

                        Log.info.print("DownloadURLFile-下载", "拷贝进度: " + ((total2 * 100L) / fileSize));
                        // 更新进度条
                        int finalTotal = (int) (total2 * 100 / fileSize);
                        if (panel == null) {

                            Log.info.loadingDialog.updateDialog("文件下载" + id,  finalTotal);
                        }else{
                            SwingUtilities.invokeLater(() -> progressBar.setValue(finalTotal));
                        }
                    }






                    


                } catch (IOException e) {
                    //判断错误是否为拒绝访问
                    Log.err.print(DownloadURLFile.class, "下载失败", e);
                    if (e.getMessage().contains("拒绝访问")) {
                        Log.info.message(parent, "DownloadURLFile-下载", "下载失败，请以管理员身份运行");

                    }
                }
                Log.info.message(parent, "DownloadURLFile-下载", "下载完成！");

            }
        } catch (Exception ex) {
            Log.err.print(DownloadURLFile.class, "下载失败", ex);
        }
        if (panel != null) {
            panel.removeAll();
        } else {
            Log.info.loadingDialog.closeDialog("文件下载" + id);
        }
        //}).start();
    }

    private static String getFileNameFromUrl(String urlString) {
        try {
            URI uri = new URI(urlString);
            String path = uri.getPath();
            if (path == null || path.isEmpty()) return "";

            // 处理编码字符
            String decodedPath = URLDecoder.decode(path, StandardCharsets.UTF_8);

            // 分割路径并过滤空段
            String[] segments = decodedPath.split("/");
            for (int i = segments.length - 1; i >= 0; i--) {
                if (!segments[i].isEmpty()) {
                    String fileName = segments[i];
                    // 过滤查询参数（虽然URI.getRunPath()已处理，但二次确认）
                    int queryIndex = fileName.indexOf('?');
                    return queryIndex == -1 ? fileName : fileName.substring(0, queryIndex);
                }
            }
            return "";
        } catch (URISyntaxException e) {
            return ""; // 或根据业务需求处理异常
        }
    }
}