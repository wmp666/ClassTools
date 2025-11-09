package com.wmp.PublicTools.EasterEgg;

import com.wmp.Main;
import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.DateTools;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.GetIcon;
import com.wmp.PublicTools.appFileControl.IconControl;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.io.ResourceLocalizer;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.PublicTools.videoView.MediaPlayer;
import com.wmp.PublicTools.web.GetWebInf;
import com.wmp.classTools.CTComponent.CTOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class EasterEgg {

    public static final int STYLE_IMPORT_DAY = 1;
    public static final int STYLE_ERROR = 2;

    public static final int STYLE_EE_VIDEO = 0;
    public static final int STYLE_EE_MUSIC = 1;
    public static final int STYLE_EE_OTHER = 2;

    public static boolean getEasterEggItem(int style) {

        if (Main.allArgs.get("screenProduct:show").contains(Main.argsList)) return false;

        switch (style) {
            case STYLE_IMPORT_DAY -> {
                //加载颜色(CTColor)数据
                //判断当前时间是否是4月1日
                boolean b = DateTools.dayIsNow("04-01");
                if (b) {
                    CTColor.setAllColor(CTColor.MAIN_COLOR_GREEN, CTColor.STYLE_LIGHT);
                    return b;
                }

                if (DateTools.dayIsNow("09-18") ||
                        DateTools.dayIsNow("10-01") ||
                        DateTools.dayIsNow("05-01")) {
                    CTColor.setAllColor(CTColor.MAIN_COLOR_RED, CTColor.STYLE_LIGHT);
                }

                b = DateTools.dayIsNow("09-28") ||//原神周年庆
                        DateTools.dayIsNow("lunar9-17") ||//author birthday
                        DateTools.dayIsNow("09-03") ||//mc
                        DateTools.dayIsNow("04-25");//崩铁

                return b;
            }
            case STYLE_ERROR -> {
                // 明确指定时区
                LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Shanghai"));
                boolean b = DateTools.dayIsNow("04-07");
                if (!b) {
                    if (DateTools.dayIsNow("04-25")) {//崩铁
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


        String style = Log.info.showChooseDialog(null, "祈愿", "请输入选择彩蛋格式\n注:\"其他\"指不是常规格式(MP3, MP4)的文件", "视频", "音乐", "其他");

        new Thread(() -> {
            Log.info.print("彩蛋", "正在获取数据,稍安勿躁...");
        }).start();
        try {

            AtomicReference<JSONArray> info = new AtomicReference<>(new JSONArray());

            //name, key
            HashMap<String, String> keyMap = new HashMap<>();
            //name, URL
            HashMap<String, String> musicMap = new HashMap<>();
            HashMap<String, String> videoMap = new HashMap<>();
            HashMap<String, String> otherMap = new HashMap<>();

            //获取彩蛋列表
            String webInf = GetWebInf.getWebInf("https://api.github.com/repos/wmp666/ClassTools/releases/tags/0.0.1");
            JSONObject jsonObject = new JSONObject(webInf);

            JSONArray assets = jsonObject.getJSONArray("assets");
            assets.forEach(asset -> {
                if (asset instanceof JSONObject jsonObject1) {
                    String name = jsonObject1.getString("name");
                    String browser_download_url = jsonObject1.getString("browser_download_url");

                    keyMap.put(name, name);

                    if (name.endsWith(".mp3")) {
                        musicMap.put(name, browser_download_url);
                    } else if (name.endsWith(".mp4")) {
                        videoMap.put(name, browser_download_url);
                    } else if (name.equals("EasterEggInfo.json")) {
                        try {
                            String s = GetWebInf.getWebInf(browser_download_url);
                            info.set(new JSONArray(s));
                        } catch (Exception e) {
                            Log.err.print(EasterEgg.class, "数据获取失败", e);
                        }
                    } else {
                        otherMap.put(name, browser_download_url);
                    }
                }
            });

            //根据EasterEgg.json中的数据,修改彩蛋名
            info.get().forEach(object -> {
                if (object instanceof JSONObject jsonObject2) {
                    String key = jsonObject2.getString("key");
                    String name = jsonObject2.getString("name");

                    keyMap.remove(key);
                    keyMap.put(name, key);

                    if (key.endsWith(".mp3")) {
                        String s = musicMap.get(key);
                        musicMap.remove(key);
                        musicMap.put(name, s);
                    } else if (key.endsWith(".mp4")) {
                        String s = videoMap.get(key);
                        videoMap.remove(key);
                        videoMap.put(name, s);
                    } else {
                        String s = otherMap.get(key);
                        otherMap.remove(key);
                        otherMap.put(name, s);
                    }
                }
            });

            int styleInt = STYLE_EE_OTHER;
            String name = "";
            String url = "";
            switch (style) {
                case "视频" -> {
                    String[] names = videoMap.keySet().toArray(new String[0]);
                    String s = Log.info.showChooseDialog(null, "祈愿", "请选择彩蛋", names);
                    name = keyMap.get(s);
                    url = videoMap.get(s);
                    styleInt = STYLE_EE_VIDEO;
                }
                case "音乐" -> {
                    String[] names = musicMap.keySet().toArray(new String[0]);
                    String s = Log.info.showChooseDialog(null, "祈愿", "请选择彩蛋", names);
                    name = keyMap.get(s);
                    url = musicMap.get(s);
                    styleInt = STYLE_EE_MUSIC;
                }
                case "其他" -> {
                    String[] names = otherMap.keySet().toArray(new String[0]);
                    String s = Log.info.showChooseDialog(null, "祈愿", "请选择彩蛋", names);
                    name = keyMap.get(s);
                    url = otherMap.get(s);
                    styleInt = STYLE_EE_OTHER;
                }

            }
            if (CTInfo.isError) {
                name = "PV-yl.mp4";
                url = videoMap.get(name);
                styleInt = STYLE_EE_VIDEO;
            }
            showEasterEgg(styleInt, name, url);


        } catch (Exception e) {
            Log.err.print(null, EasterEgg.class, "获取彩蛋失败", e);
        }
    }

    public static void showEasterEgg(int style, String name, String url) {
        Log.info.print("EasterEgg-显示", "正在准备...");

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // 异步下载（在后台线程执行）
                Log.info.print("EasterEgg-下载", "正在下载...");

                Log.info.print("EasterEgg-下载", "下载链接: " + url);

                if (style == STYLE_EE_VIDEO)
                    ResourceLocalizer.copyWebFile(CTInfo.TEMP_PATH + "EasterEgg\\video\\", url, name);
                else if (style == STYLE_EE_MUSIC)
                    ResourceLocalizer.copyWebFile(CTInfo.TEMP_PATH + "EasterEgg\\music\\", url, name);
                else if (style == STYLE_EE_OTHER)
                    ResourceLocalizer.copyWebFile(CTInfo.TEMP_PATH + "EasterEgg\\other\\", url, name);
                return null;
            }

            @Override
            protected void done() {
                // 下载完成后在EDT线程执行
                try {

                    get(); // 获取执行结果（可捕获异常）


                    try {
                        if (style == STYLE_EE_MUSIC) {
                            String path = CTInfo.TEMP_PATH + "EasterEgg\\music\\" + name;
                            MediaPlayer.playLocalMusic(path);
                        } else if (style == STYLE_EE_VIDEO) {
                            String path = CTInfo.TEMP_PATH + "EasterEgg\\video\\" + name;
                            MediaPlayer.playVideo(path);
                        } else if (style == STYLE_EE_OTHER) {
                            String path = CTInfo.TEMP_PATH + "EasterEgg\\other\\" + name;
                            MediaPlayer.playOther(path);
                        }

                    } catch (IOException e) {
                        Log.err.print(null, EasterEgg.class, "播放失败", e);
                    }


                } catch (Exception e) {
                    Log.err.print(null, EasterEgg.class, "下载失败", e);
                }
            }
        }.execute();


    }


    public static String getText(EETextStyle style) {
        String[] easterEggList = getAllText();
        String s = easterEggList[new Random().nextInt(easterEggList.length)];
        switch (style) {
            case DEFAULT -> {
                return s;
            }
            case HTML -> {
                String s1 = "<html>" + s.replace("\\n", "<br>") + "</html>";
                Log.info.print("获取彩蛋文字", s1);
                return s1;
            }
        }
        return "err";
    }

    public static String[] getAllText() {
        if (CTInfo.isError) return new String[]{
                "骇客已入侵:\\n游戏就只是为了游戏\\n仅此而已！",
                "骇客已入侵:\\n重要的不是数值\\n是体验，是操作！",
                "骇客已入侵:\\n这次能让我玩得开心点么？"
        };

        return IOForInfo.getInfo(EasterEgg.class.getResource("EasterEgg.txt"));

    }

    public static void errorAction() {
        Log.info.print("EasterEgg", "你没有权限!!!");

        CTOptionPane.showMessageDialog(null, "警告", "你没有权限!!!", GetIcon.getIcon("刻律德菈", IconControl.COLOR_DEFAULT, 100, 100), CTOptionPane.ERROR_MESSAGE, true);
    }

    public static void showHolidayBlessings(int style) {

        Log.info.print("EasterEgg", "搜索今日是否需要祝福");

        AtomicBoolean b = new AtomicBoolean(false);
        try {
            //获取文件
            String jsonArrayStr = IOForInfo.getInfos(Objects.requireNonNull(EasterEgg.class.getResource("HBText.json")));
            JSONArray jsonArray = new JSONArray(jsonArrayStr);

            //获取时间
            /*DateFormat dateFormat = new SimpleDateFormat("MM-dd");
            String date = dateFormat.format(new Date());*/

            jsonArray.forEach(jsonObject -> {


                if (jsonObject instanceof JSONObject jsonObject1) {

                    String date1 = jsonObject1.getString("date");
                    if (DateTools.dayIsNow(date1)) {

                        b.set(true);
                        Main.argsList.add("-StartUpdate:false");

                        String text = jsonObject1.getString("text");
                        String title = jsonObject1.getString("title");

                        CTOptionPane.showFullScreenMessageDialog(title, text, 0, 5);

                    }

                } else {
                    Log.err.print(EasterEgg.class, "获取彩蛋文件数据异常: \n" + jsonObject);
                }
            });


        } catch (Exception e) {
            Log.err.print(EasterEgg.class, "获取彩蛋文字失败", e);
        }
        if (style == 1 && !b.get()) Log.info.message(null, "EasterEgg", "今日无彩蛋");
    }
}
