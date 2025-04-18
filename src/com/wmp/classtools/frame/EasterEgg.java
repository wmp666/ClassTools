package com.wmp.classTools.frame;

import com.wmp.Main;
import com.wmp.PublicTools.io.ResourceLocalizer;
import com.wmp.PublicTools.videoView.VideoPlayer;
import com.wmp.PublicTools.web.GetWebInf;
import com.wmp.classTools.frame.tools.cookie.StartCookie;
import org.json.JSONObject;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class EasterEgg {

    public static void getPin() {
        String s = JOptionPane.showInputDialog(null, "请输入■■", "祈愿", JOptionPane.PLAIN_MESSAGE);


        if (s != null) {
            try {

                String[] split = s.split(":");
                if (split.length == 2) {
                    if (split[0].equalsIgnoreCase("EasterEgg")) {
                        showEasterEgg(split[1]);
                    } else if (split[0].equalsIgnoreCase("cookie")) {
                        StartCookie.showCookie(split[1]);
                    } else{
                        JOptionPane.showMessageDialog(null, "请输入正确的格式", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (split.length == 1) {
                    showEasterEgg(s);
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
    public static void showEasterEgg(String pin) throws URISyntaxException, IOException {

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // 异步下载（在后台线程执行）
                String webInf;
                try {
                    webInf = GetWebInf.getWebInf("https://api.github.com/repos/wmp666/ClassTools/contents/video/" + pin + ".mp4");
                } catch (Exception e) {
                    webInf = "{\"message\":\"Not Found\"}";
                    throw new RuntimeException(e);
                }
                //处理数据
                String downloadUrl = "";
                JSONObject jsonObject = new JSONObject(webInf);
                if (jsonObject.has("download_url")){
                    downloadUrl = jsonObject.getString("download_url");
                }

                ResourceLocalizer.copyWebVideo(Main.TEMP_PATH + "video\\", downloadUrl, pin + ".mp4");
                return null;
            }

            @Override
            protected void done() {
                // 下载完成后在EDT线程执行
                try {
                    get(); // 获取执行结果（可捕获异常）
                    String videoPath = buildVideoPath(pin);
                    VideoPlayer.playVideo(videoPath);
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
