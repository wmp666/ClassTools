package com.wmp.PublicTools.EasterEgg;

import com.wmp.Main;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.io.ResourceLocalizer;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.PublicTools.videoView.MediaPlayer;
import com.wmp.PublicTools.web.GetWebInf;
import com.wmp.classTools.frame.tools.cookie.StartCookie;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;

public class EasterEgg {


    public static void getPin() {
        String s = Log.info.input(null, "祈愿", "请输入■■");



        if (s != null) {
            try {

                String[] split = s.split(":");
                if (split.length == 2) {
                    if (split[0].equalsIgnoreCase("EasterEgg")) {
                        showEasterEgg(split[1].split(";"));
                    } else if (split[0].equalsIgnoreCase("cookie")) {
                        StartCookie.showCookie(split[1].split(";"));
                    } else{
                        Log.err.print(null, "祈愿", "请输入正确的格式");

                    }
                } else if (split.length == 1) {
                    showEasterEgg(s.split(";"));
                }else{
                    Log.err.print(null, "祈愿", "请输入正确的格式");
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
        String videoName;


        Log.info.print("EasterEgg-显示", "正在准备...");

        if (Main.isError) videoName = "PV-yl";
        else {
            videoName = pin;
        }

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
                                if (asset.getString("name").equals(videoName + ".mp4")) {
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

                ResourceLocalizer.copyWebVideo(Main.TEMP_PATH + "EasterEgg\\video\\", downloadUrl, videoName + ".mp4");
                return null;
            }

            @Override
            protected void done() {
                // 下载完成后在EDT线程执行
                try {

                    get(); // 获取执行结果（可捕获异常）


                    String videoPath = buildVideoPath(videoName);
                            try {
                                MediaPlayer.playVideo(videoPath);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }



                } catch (Exception e) {
                    Log.err.print(null, "EasterEgg-下载", "下载失败: " + e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        }.execute();



    }

    private static String buildVideoPath(String pin) {
        return Main.TEMP_PATH + "EasterEgg\\video\\" + pin + ".mp4";
    }

    public static String getText(EETextStyle style){
        String[] easterEggList = getAllText();
        String s = easterEggList[new Random().nextInt(easterEggList.length)];
        switch (style){
            case DEFAULT -> {
                return s;
            }
            case HTML -> {
                String s1 = "<html>" + s.replace("\\n", "<br>") + "</html>";
                Log.info.print("获取彩蛋文字", s1);
                return s1 ;
            }
        }
        return "err";
    }

    public static String[] getAllText(){
        if (Main.isError) return new String[]{
                "骇客已入侵:\\n游戏就只是为了游戏\\n仅此而已！",
                "骇客已入侵:\\n重要的不是数值\\n是体验，是操作！",
                "骇客已入侵:\\n这次能让我玩得开心点么？"
        };

        return IOForInfo.getInfo(EasterEgg.class.getResource("EasterEgg.txt"));

    }
}
