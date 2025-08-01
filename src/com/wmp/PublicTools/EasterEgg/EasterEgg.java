package com.wmp.PublicTools.EasterEgg;

import com.wmp.Main;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.io.ResourceLocalizer;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.PublicTools.videoView.MediaPlayer;
import com.wmp.PublicTools.web.GetWebInf;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Random;

public class EasterEgg {

    public static final int STYLE_IMPORT_DAY = 1;
    public static final int STYLE_ERROR = 2;

    public static final int STYLE_EE_VIDEO = 0;
    public static final int STYLE_EE_MUSIC = 1;
    public static boolean getEasterEggItem(int style) {

        if (Main.allArgs.get("screenProduct:show").contains(Main.argsList)) return false;

        switch (style) {
            case STYLE_IMPORT_DAY -> {
                //加载颜色(CTColor)数据
                //判断当前时间是否是4月1日
                // 明确指定时区
                LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Shanghai"));
                boolean b = currentDate.getMonth() == Month.APRIL
                        && currentDate.getDayOfMonth() == 1;
                if (b) {
                    CTColor.setAllColor(CTColor.MAIN_COLOR_GREEN, CTColor.STYLE_LIGHT);
                    return b;
                }

                b = (currentDate.getMonth() == Month.SEPTEMBER //原神周年庆
                        && currentDate.getDayOfMonth() == 28) ||
                        (currentDate.getMonth() == Month.NOVEMBER //author birthday
                                && currentDate.getDayOfMonth() == 3) ||
                        (currentDate.getMonth() == Month.SEPTEMBER //mc
                                && currentDate.getDayOfMonth() == 3) ||
                        (currentDate.getMonth() == Month.APRIL //崩铁
                                && currentDate.getDayOfMonth() == 25);

                return b;
            }
            case STYLE_ERROR -> {
                // 明确指定时区
                LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Shanghai"));
                boolean b = currentDate.getMonth() == Month.APRIL
                        && currentDate.getDayOfMonth() == 7;
                if (!b) {
                    if (currentDate.getMonth() == Month.APRIL //崩铁
                            && currentDate.getDayOfMonth() == 25) {
                        Random r = new Random();
                        int i = r.nextInt(5);
                        System.out.println("崩铁:" + i);
                        return i == 0;
                    }
                    Random r = new Random();
                    int i = r.nextInt(20);
                    System.out.println("普通:" + i);
                    return i == 0;
                }


                return true;
            }
        }
        return false;
    }

    public static void getPin() {
        String[] ss = Log.info.showInputDialog(null, "祈愿", "请输入■■", "视频", "音乐");

        String s = ss[1];

        int style = 0;
        String s1 = ss[0];
        if (s1.equals("视频")) {
            style = STYLE_EE_VIDEO;
        } else if (s1.equals("音乐")) {
            style = STYLE_EE_MUSIC;
        } else {
            Log.err.print(null, "祈愿", "请输入正确的格式");
            return;
        }

        if (s != null) {
                String[] split = s.split(":");
                if (split.length == 2) {
                    String s2 = split[0];
                    if (s2.equalsIgnoreCase("EasterEgg")) {
                        showEasterEgg(style, split[1]);
                    } else{
                        Log.err.print(null, "祈愿", "请输入正确的格式");
                    }
                } else if (split.length == 1) {
                    showEasterEgg(style, s);
                }else{
                    Log.err.print(null, "祈愿", "请输入正确的格式");
                }
        }


    }

    public static void showEasterEgg(int style, String pin) {
        String mediaName;


        Log.info.print("EasterEgg-显示", "正在准备...");

        if (Main.isError) mediaName = "PV-yl";
        else {
            mediaName = pin;
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
                        if (jsonObject.getString("tag_name").equalsIgnoreCase("0.0.1")) {

                            JSONArray assets = jsonObject.getJSONArray("assets");
                            for (int j = 0; j < assets.length(); j++) {
                                JSONObject asset = assets.getJSONObject(j);
                                if (style == STYLE_EE_VIDEO) {
                                    if (asset.getString("name").equals(mediaName + ".mp4")) {
                                        downloadUrl = asset.getString("browser_download_url");
                                        break;
                                    }
                                } else if (style == STYLE_EE_MUSIC) {
                                    if (asset.getString("name").equals(mediaName + ".mp3")) {
                                        downloadUrl = asset.getString("browser_download_url");
                                        break;
                                    }
                                }

                            }
                        }
                    }


                    //webInf = GetWebInf.getWebInf("https://api.github.com/repos/wmp666/ClassTools/contents/video/" + pin + ".mp4");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                Log.info.print("EasterEgg-下载", "下载链接: " + downloadUrl);

                if (style == STYLE_EE_VIDEO)
                    ResourceLocalizer.copyWebFile(Main.TEMP_PATH + "EasterEgg\\video\\", downloadUrl, mediaName + ".mp4");
                else if (style == STYLE_EE_MUSIC)
                    ResourceLocalizer.copyWebFile(Main.TEMP_PATH + "EasterEgg\\music\\", downloadUrl, mediaName + ".mp3");
                return null;
            }

            @Override
            protected void done() {
                // 下载完成后在EDT线程执行
                try {

                    get(); // 获取执行结果（可捕获异常）


                    try {
                        if (style == STYLE_EE_MUSIC) {
                            String path = Main.TEMP_PATH + "EasterEgg\\music\\" + mediaName + ".mp3";
                            MediaPlayer.playMusic(path);
                        } else if (style == STYLE_EE_VIDEO) {
                            String path = Main.TEMP_PATH + "EasterEgg\\video\\" + mediaName + ".mp4";
                            MediaPlayer.playVideo(path);
                        }

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
