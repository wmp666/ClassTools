package com.wmp;

import com.wmp.PublicTools.StartupParameters;
import com.wmp.classTools.printLog.Log;
import com.wmp.PublicTools.update.GetNewerVersion;
import com.wmp.classTools.frame.EasterEgg;
import com.wmp.classTools.frame.LoadingWindow;
import com.wmp.classTools.frame.MainWindow;
import com.wmp.classTools.frame.tools.cookie.StartCookie;
import com.wmp.classTools.infSet.tools.GetSetsJSON;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
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
    * d:只修复的问题,问题较少
    * e:测试版本号
     */
    public static String version = "1.17.0.1";

    public static ArrayList<String> list = new ArrayList<>();

    public static final TreeMap<String, StartupParameters> allArgs = new TreeMap<>();

    public static final ArrayList<String> disButList = new ArrayList<>();

    public static boolean canExit = true;

    private static boolean StartUpdate = true;

    static {
        Log log = new Log();
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

        boolean b = isImportDay();

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
        Log.info.print("Main", "启动参数:" + Arrays.toString(args));

        if (args.length > 0) {
            list = new ArrayList<>(Arrays.asList(args));
            Log.info.print("Main", "使用的启动参数:" + Arrays.toString(args));
        }

        show(b);

    }

    private static boolean isImportDay() {
        //加载颜色(CTColor)数据
        //判断当前时间是否是4月1日
        // 明确指定时区
        LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Shanghai"));
        boolean b = currentDate.getMonth() == Month.APRIL
                && currentDate.getDayOfMonth() == 1;
        if (b){
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


        //System.out.println(new CTColor());

    }


    private static void show(boolean b) throws URISyntaxException, IOException {

        LoadingWindow loadingWindow;
        if (b){
            if (allArgs.get("screenProduct:show").contains(list)){
                loadingWindow = new LoadingWindow(Main.class.getResource("/image/start.gif"),
                        692, 491, "", true, 1300, LoadingWindow.STYLE_SCREEN);
            }else{
                loadingWindow = new LoadingWindow(Main.class.getResource("/image/loading.gif"),
                        200, 200, "加载中...", true, 2300);
            }

        }else{
            loadingWindow = new LoadingWindow();
        }
        //System.out.println(sb);


        //loadingWindow.setVisible(true);

        if (StartUpdate &&
                !(allArgs.get("StartUpdate:false").contains(list) ||
                allArgs.get("screenProduct:show").contains(list) ||
                allArgs.get("screenProduct:view").contains(list))) {
            Log.info.print("Main", "开始启动自动检查更新");
            GetNewerVersion.checkForUpdate(
                    loadingWindow, null, true);

        }

        if (allArgs.get("EasterEgg:").contains(list)) {
            int i = list.indexOf("-EasterEgg:") + 1;
            Log.info.print("Main", "-EasterEgg:" + list.get(i));
            //System.out.println();
            EasterEgg.showEasterEgg(list.get(i).split(";"));
        }
        if (allArgs.get("Cookie:StartUp").contains(list)) {
            int i = list.indexOf("-OpenCookie:") + 1;
            Log.info.print("Main", "-OpenCookie:" + list.get(i));
            //System.out.println();
            StartCookie.showCookie(list.get(i).split(";"));
        }

        new MainWindow(DATA_PATH);
        loadingWindow.setVisible(false);
    }
}