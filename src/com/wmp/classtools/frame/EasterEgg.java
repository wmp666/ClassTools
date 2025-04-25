package com.wmp.classTools.frame;

import com.wmp.Main;
import com.wmp.PublicTools.io.ResourceLocalizer;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.PublicTools.videoView.VideoPlayer;
import com.wmp.PublicTools.web.GetWebInf;
import com.wmp.classTools.frame.tools.cookie.StartCookie;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

public class EasterEgg {

    public static void getPin() {
        String s = JOptionPane.showInputDialog(null, "请输入■■", "祈愿", JOptionPane.PLAIN_MESSAGE);


        if (s != null) {
            try {

                String[] split = s.split(":");
                if (split.length == 2) {
                    if (split[0].equalsIgnoreCase("EasterEgg")) {
                        showEasterEgg(split[1].split(";"));
                    } else if (split[0].equalsIgnoreCase("cookie")) {
                        StartCookie.showCookie(split[1].split(";"));
                    } else{
                        JOptionPane.showMessageDialog(null, "请输入正确的格式", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (split.length == 1) {
                    showEasterEgg(s.split(";"));
                }else{
                    JOptionPane.showMessageDialog(null, "请输入正确的格式", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
                }


            } catch (URISyntaxException | IOException e) {
                throw new RuntimeException(e);
            }
        }


    }

    public static void showEasterEgg(String... pins) throws URISyntaxException, IOException {
        for (String pin : pins) {
            showEasterEgg(pin);
        }
    }
    public static void showEasterEgg(String pin){

        Log.info.print("EasterEgg-显示", "正在准备...");

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // 异步下载（在后台线程执行）
                Log.info.print("EasterEgg-下载", "正在下载...");
                String downloadUrl = "";
                try {
                    String temp = GetWebInf.getWebInf("https://api.github.com/repos/wmp666/ClassTools/releases");


                    //以数组的形式加载[n,d,v]
                    JSONArray jsonArray = new JSONArray(temp);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.getString("tag_name").equalsIgnoreCase("0.0.1")
                                && jsonObject.getString("name").equalsIgnoreCase("视频专用")){

                            JSONArray assets = jsonObject.getJSONArray("assets");
                            for (int j = 0; j < assets.length(); j++) {
                                JSONObject asset = assets.getJSONObject(j);
                                if (asset.getString("name").equals(pin + ".mp4")) {
                                    downloadUrl = asset.getString("browser_download_url");
                                    break;
                                }
                            }
                        }
                    }


                    //webInf = GetWebInf.getWebInf("https://api.github.com/repos/wmp666/ClassTools/contents/video/" + pin + ".mp4");
                } catch (Exception e) {
                    downloadUrl = "{\"message\":\"Not Found\"}";
                    throw new RuntimeException(e);
                }
                Log.info.print("EasterEgg-下载", "下载链接: " + downloadUrl);

                ResourceLocalizer.copyWebVideo(Main.TEMP_PATH + "video\\", downloadUrl, pin + ".mp4");
                return null;
            }

            @Override
            protected void done() {
                // 下载完成后在EDT线程执行
                try {

                    get(); // 获取执行结果（可捕获异常）

                    new SwingWorker<Void, Void>() {

                        @Override
                        protected void done() {
                            String videoPath = buildVideoPath(pin);
                            try {
                                VideoPlayer.playVideo(videoPath);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        @Override
                        protected Void doInBackground() throws Exception {
                            ImageIcon imageIcon = new ImageIcon(
                                Objects.requireNonNull(Main.class.getResource("/image/openEasterEgg.gif")));
                            int iconHeight = imageIcon.getIconHeight();
                            int iconWidth = imageIcon.getIconWidth();
                            Icon icon = new ImageIcon(
                                    imageIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_DEFAULT));
                            JLabel label = new JLabel(icon);
                            JWindow window = new JWindow();
                            window.setSize(iconWidth, iconHeight);
                            window.setLocationRelativeTo(null);
                            window.setAlwaysOnTop(true);
                            window.add(label);

                            window.setVisible(true);
                            try {
                                Thread.sleep(6850);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            window.dispose();

                            return null;
                        }
                    }.execute();



                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }.execute();

        //VideoLocalizer.copyEmbeddedVideo(Main.TEMP_PATH, pin + ".mp4");
        /*String videoPath = "null";
        switch (pin) {
            case "nj01":
                videoPath = Main.TEMP_PATH + "video\\nj01.mp4";
                break;
            case "nj02":
                videoPath = Main.TEMP_PATH + "video\\nj02.mp4";
                break;
            case "nj03":
                videoPath = Main.TEMP_PATH + "video\\nj03.mp4";
                break;
            case "nj04":
                videoPath = Main.TEMP_PATH + "video\\nj04.mp4";
                break;
            default:
                JOptionPane.showMessageDialog(null, "请输入正确的格式", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
                break;

        }
        VideoPlayer.playVideo(videoPath);*/

    }

    private static String buildVideoPath(String pin) {
        return Main.TEMP_PATH + "video\\" + pin + ".mp4";
    }
}
