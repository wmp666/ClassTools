package com.wmp;

import com.wmp.PublicTools.StartupParameters;
import com.wmp.PublicTools.update.GetNewerVersion;
import com.wmp.classTools.frame.EasterEgg;
import com.wmp.classTools.frame.LoadingWindow;
import com.wmp.classTools.frame.MainWindow;
import com.wmp.classTools.frame.tools.cookie.StartCookie;
import com.wmp.classTools.infSet.tools.GetSetsJSON;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class Main{

    public static String DATA_PATH = "null";

    public static String TEMP_PATH = "null";

    /**a.b.c.d.e
    * a:主版本号
    * b:功能更新版本号
    * c:修订版本号/小功能更新
    * d:修复c版的问题(仅限)
    * e:测试版本号
     */
    public static String version = "1.14.0";

    public static ArrayList<String> list = new ArrayList<>();

    public static final TreeMap<String, StartupParameters> allArgs = new TreeMap<>();

    public static final ArrayList<String> disButList = new ArrayList<>();

    public static boolean canExit = true;

    private static boolean StartUpdate = true;

    static {
        //加载基础目录
        String path = System.getenv("LOCALAPPDATA");

        StringBuilder sb = new StringBuilder();
        DATA_PATH = sb.append(path).append("\\ClassTools\\").toString();

        StringBuilder sb2 = new StringBuilder();

        TEMP_PATH = sb2.append(path).append("\\ClassToolsTemp\\").toString();

        DATA_PATH = sb.toString();


        allArgs.put("TimeView:screen", StartupParameters.creative("-TimeView:screen", "/TimeView:screen"));
        allArgs.put("StartUpdate:false", StartupParameters.creative("-StartUpdate:false", "/StartUpdate:false"));
        allArgs.put("EasterEgg:", StartupParameters.creative("-EasterEgg:", "/EasterEgg:"));
        allArgs.put("screenProduct:show", StartupParameters.creative("/s", "-s"));
        allArgs.put("screenProduct:view", StartupParameters.creative("/p", "-p"));
        allArgs.put("Cookie:StartUp", StartupParameters.creative("-OpenCookie:", "/OpenCookie:"));
        //allArgs.put("", StartupParameters.creative("-EasterEgg-pin:nj02", "/EasterEgg-pin:nj02"));
    }
    public static void main(String[] args) throws IOException, URISyntaxException {

        GetSetsJSON setsJSON = new GetSetsJSON();

        StartUpdate = setsJSON.isStartUpdate();
        canExit = setsJSON.isCanExit();
        disButList.addAll(setsJSON.getDisButList());

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


        new MainWindow(DATA_PATH);
        loadingWindow.setVisible(false);
    }
}