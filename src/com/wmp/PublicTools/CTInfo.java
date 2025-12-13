package com.wmp.PublicTools;

import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.appFileControl.IconControl;
import com.wmp.PublicTools.appFileControl.MusicControl;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.frame.MainWindow;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;

public class CTInfo {
    //禁用的组件和按钮
    public static final ArrayList<String> disPanelList = new ArrayList<>();
    public static final ArrayList<String> disButList = new ArrayList<>();
    //控件数据
    public static final int arch = 15;
    public static final int arcw = 15;
    public static double dpi = 1.0;
    public static boolean isButtonUseMainColor = false;
    //数据位置
    public static String DATA_PATH;
    public static String TEMP_PATH;
    public static String APP_INFO_PATH;
    //基础数据
    public static String appName = "班级工具";
    public static String author = "无名牌";
    public static String iconPath = "/image/icon/icon.png";
    //其他
    public static boolean isError = false;
    public static boolean canExit = true;
    public static boolean StartUpdate = true;
    /**
     * a.b.c.d.e 例:1.5.3.1.1<br>
     * a:主版本号<br>
     * b:功能更新版本号<br>
     * c:修订版本号/小功能更新<br>
     * d:只修复的问题,问题较少<br>
     * e:测试版本号
     */
    public static String version = "1.47.2.1";
    private static JSONObject jsonObject;

    static {
        init();
    }

    public static void init() {

        if (MainWindow.mainWindow != null){
            Taskbar taskbar = Taskbar.getTaskbar();

            if (taskbar.isSupported(Taskbar.Feature.USER_ATTENTION_WINDOW)) {
                taskbar.requestWindowUserAttention(MainWindow.mainWindow);
            }

            if (taskbar.isSupported(Taskbar.Feature.PROGRESS_STATE_WINDOW)) {
                taskbar.setWindowProgressState(MainWindow.mainWindow, Taskbar.State.INDETERMINATE);
            }
        }

        dpi = 1.0;
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

        initCTInfo();

        initCTBasicInfo();



        if (MainWindow.mainWindow != null){
            Taskbar taskbar = Taskbar.getTaskbar();

            if (taskbar.isSupported(Taskbar.Feature.PROGRESS_STATE_WINDOW)) {
                taskbar.setWindowProgressState(MainWindow.mainWindow, Taskbar.State.OFF);
            }
        }

    }

    private static void initCTBasicInfo() {
        IconControl.init();
        MusicControl.init();
        Log.initTrayIcon();
    }

    private static void initCTInfo() {
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
                CTColor.setMainColor(jsonObject.getString("mainColor"));
            }
            //设置主题
            if (jsonObject.has("mainTheme")) {
                CTColor.setMainTheme(jsonObject.getString("mainTheme"));
            }
            //设置按钮颜色
            if (jsonObject.has("buttonColor")) {
                isButtonUseMainColor = jsonObject.getBoolean("buttonColor");
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
