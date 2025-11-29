package com.wmp.PublicTools;

import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.appFileControl.IconControl;
import com.wmp.PublicTools.appFileControl.MusicControl;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;

public class CTInfo {
    public static final ArrayList<String> disPanelList = new ArrayList<>();
    public static final ArrayList<String> disButList = new ArrayList<>();
    public static final int arch = 15;
    public static final int arcw = 15;
    public static String DATA_PATH;
    public static String TEMP_PATH;
    public static String APP_INFO_PATH;
    public static String appName = "班级工具";
    public static String author = "无名牌";
    public static String iconPath = "/image/icon/icon.png";
    public static double dpi = 1;
    public static boolean isError = false;
    public static boolean canExit = true;
    public static boolean StartUpdate = true;
    /**
     * a.b.c.d.e
     * a:主版本号
     * b:功能更新版本号
     * c:修订版本号/小功能更新
     * d:只修复的问题,问题较少
     * e:测试版本号
     */
    public static String version = "1.45.0";
    private static JSONObject jsonObject;

    static {
        init();
    }

    public static void init() {

        disButList.clear();
        disPanelList.clear();

        //加载基础目录
        String path = System.getenv("LOCALAPPDATA");
        if (path != null && !path.isEmpty()){
            File file = new File(path, "\\ClassTools\\basicDataPath.txt");
            if (file.exists() && file.isFile()) {
                try {
                    path = new File(Files.readString(file.toPath(), StandardCharsets.UTF_8)).getAbsolutePath();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        APP_INFO_PATH = path + "\\ClassToolsAppFile\\";
        DATA_PATH = path + "\\ClassTools\\";
        TEMP_PATH = path + "\\ClassToolsTemp\\";

        if (!isError) {
            if (version.split("\\.").length < 5) iconPath = "/image/icon/icon.png";
            else iconPath = "/image/icon/icon_bate.png";
        } else iconPath = "/image/err/icon.png";

        IconControl.init();
        MusicControl.init();
        Log.initTrayIcon();

        boolean exists = new File(CTInfo.DATA_PATH + "setUp.json").exists();
        if (exists) {
            IOForInfo sets = new IOForInfo(new File(CTInfo.DATA_PATH + "setUp.json"));

            try {
                jsonObject = new JSONObject(sets.getInfos());
            } catch (Exception e) {
                jsonObject = new JSONObject();
                Log.err.print(CTFont.class, "数据获取发生错误", e);
            }

            //设置颜色
            if (jsonObject.has("mainColor")) {
                switch (jsonObject.getString("mainColor")) {
                    case "black" -> CTColor.setMainColor(CTColor.MAIN_COLOR_BLACK);
                    case "white" -> CTColor.setMainColor(CTColor.MAIN_COLOR_WHITE);
                    case "green" -> CTColor.setMainColor(CTColor.MAIN_COLOR_GREEN);
                    case "red" -> CTColor.setMainColor(CTColor.MAIN_COLOR_RED);
                    default -> CTColor.setMainColor(CTColor.MAIN_COLOR_BLUE);
                }
            }
            //设置主题
            if (jsonObject.has("mainTheme")) {
                if (jsonObject.getString("mainTheme").equals("dark")) {
                    CTColor.setMainTheme(CTColor.STYLE_DARK);
                } else {
                    CTColor.setMainTheme(CTColor.STYLE_LIGHT);
                }
            }
            //设置字体
            if (jsonObject.has("FontName")) {
                CTFont.setFontName(jsonObject.getString("FontName"));
            }
            //设置隐藏内容
            if (jsonObject.has("disposeButton")) {
                JSONArray disButtonList = jsonObject.getJSONArray("disposeButton");
                disButtonList.forEach(object -> {
                    disButList.add(object.toString());
                });
            }
            if (jsonObject.has("disposePanel")) {
                JSONArray disButtonList = jsonObject.getJSONArray("disposePanel");
                disButtonList.forEach(object -> {
                    disPanelList.add(object.toString());
                });
            }
            //设置是否可以退出
            if (jsonObject.has("canExit")) {
                canExit = jsonObject.getBoolean("canExit");
            }
            //设置是否可以更新
            if (jsonObject.has("StartUpdate")) {
                StartUpdate = jsonObject.getBoolean("StartUpdate");
            }
            //设置DPI
            if (jsonObject.has("DPI")) {
                dpi = jsonObject.getDouble("DPI");
            }
            //设置日志
            if (jsonObject.has("isSaveLog")) {
                boolean b = jsonObject.getBoolean("isSaveLog");
                Log.isSaveLog(b);
            }
        }

    }
}
