package com.wmp;

import com.wmp.PublicTools.StartupParameters;
import com.wmp.PublicTools.io.IOStreamForInf;
import com.wmp.PublicTools.update.GetNewerVersion;
import com.wmp.classTools.frame.EasterEgg;
import com.wmp.classTools.frame.LoadingWindow;
import com.wmp.classTools.frame.MainWindow;
import com.wmp.classTools.frame.tools.cookie.StartCookie;
import org.json.JSONObject;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class Main{

    public static String DataPath = "null";

    public static String TempPath = "null";

    /**a.b.c.d.e
    * a:主版本号
    * b:功能更新版本号
    * c:修订版本号/小功能更新
    * d:修复c版的问题(仅限)
    * e:测试版本号
     */
    public static String version = "1.13.4";

    public static ArrayList<String> list = new ArrayList<>();

    public static final TreeMap<String, StartupParameters> allArgs = new TreeMap<>();

    public static boolean canExit = true;

    private static boolean StartUpdate = true;

    static {
        //加载基础目录
        String path = System.getenv("LOCALAPPDATA");

        StringBuilder sb = new StringBuilder();
        DataPath = sb.append(path).append("\\ClassTools\\").toString();

        StringBuilder sb2 = new StringBuilder();

        TempPath = sb2.append(path).append("\\ClassToolsTemp\\").toString();

        DataPath = sb.toString();


        allArgs.put("TimeView:screen", StartupParameters.creative("-TimeView:screen", "/TimeView:screen"));
        allArgs.put("StartUpdate:false", StartupParameters.creative("-StartUpdate:false", "/StartUpdate:false"));
        allArgs.put("EasterEgg:", StartupParameters.creative("-EasterEgg:", "/EasterEgg:"));
        allArgs.put("screenProduct:show", StartupParameters.creative("/s", "-s"));
        allArgs.put("screenProduct:view", StartupParameters.creative("/p", "-p"));
        allArgs.put("Cookie:StartUp", StartupParameters.creative("-OpenCookie:", "/OpenCookie:"));
        //allArgs.put("", StartupParameters.creative("-EasterEgg-pin:nj02", "/EasterEgg-pin:nj02"));
    }
    public static void main(String[] args) throws IOException, URISyntaxException {

        initSetUp();

        try {
            //使用系统UI
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].replace("/", "-");
        }
        System.out.println("程序支持的启动参数:" + allArgs);

        if (args.length > 0) {
            list = new ArrayList<>(Arrays.asList(args));
            System.out.println(list);
        }

        show();

    }

    private static void initSetUp() throws IOException {
        boolean exists = new File(Main.DataPath + "setUp.json").exists();

        if (exists) {
            IOStreamForInf sets = new IOStreamForInf(new File(Main.DataPath + "setUp.json"));
            System.out.println(sets);
            JSONObject jsonObject = new JSONObject(sets.GetInf()[0]);
            if (jsonObject.has("mainColor")) {
                switch (jsonObject.getString("mainColor")) {
                    case "black" -> CTColor.setMainColorColor(CTColor.MAIN_COLOR_BLACK);
                    case "white" -> CTColor.setMainColorColor(CTColor.MAIN_COLOR_WHITE);
                    case "green" -> CTColor.setMainColorColor(CTColor.MAIN_COLOR_GREEN);
                    case "red" -> CTColor.setMainColorColor(CTColor.MAIN_COLOR_RED);
                    default -> CTColor.setMainColorColor(CTColor.MAIN_COLOR_BLUE);
                }
            }
            if (jsonObject.has("mainTheme")) {
                switch (jsonObject.getString("mainTheme")) {
                    case "dark" -> CTColor.setMainTheme(CTColor.STYLE_DARK);
                    default -> CTColor.setMainTheme(CTColor.STYLE_LIGHT);
                }
            }
            if (jsonObject.has("canExit")) {
                canExit = jsonObject.getBoolean("canExit");
            }
            if (jsonObject.has("StartUpdate")) {
                StartUpdate = jsonObject.getBoolean("StartUpdate");
            }
        }

        //加载颜色(CTColor)数据
        //判断当前时间是否是4月1日
        // 明确指定时区
        LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Shanghai"));
        boolean b = currentDate.getMonth() == Month.APRIL
                && currentDate.getDayOfMonth() == 1;
        if (b){
            CTColor.setAllColor(CTColor.MAIN_COLOR_GREEN, CTColor.STYLE_LIGHT);
        }

        System.out.println(new CTColor());
    }

    private static void show() throws URISyntaxException, IOException {
        System.out.println("Hello, World!");


        //System.out.println(sb);
        LoadingWindow loadingWindow = new LoadingWindow();

        loadingWindow.setVisible(true);

        if (StartUpdate &&
                !(allArgs.get("StartUpdate:false").contains(list) ||
                allArgs.get("screenProduct:show").contains(list) ||
                allArgs.get("screenProduct:view").contains(list))) {
            //执行你的代码
            GetNewerVersion.checkForUpdate(
                    loadingWindow, null, true);
            System.out.println("-StartUpdate:true");
        }else{
            System.out.println("-StartUpdate:false");
        }

        if (allArgs.get("EasterEgg:").contains(list)) {
            int i = list.indexOf("-EasterEgg:") + 1;
            System.out.println("-EasterEgg:" + list.get(i));
            //System.out.println();
            EasterEgg.showEasterEgg(list.get(i).split(";"));
        }
        if (allArgs.get("Cookie:StartUp").contains(list)) {
            int i = list.indexOf("-OpenCookie:") + 1;
            System.out.println("-OpenCookie:" + list.get(i));
            //System.out.println();
            StartCookie.showCookie(list.get(i).split(";"));
        }
        /*

        if (allArgs.get(5).contains(list)){
            System.out.println("-EasterEgg-pin:nj02");
            EasterEgg.show("nj02");
        }
*/


        new MainWindow(DataPath);
        loadingWindow.setVisible(false);
    }
}