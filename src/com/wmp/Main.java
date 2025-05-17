package com.wmp;

import com.wmp.PublicTools.StartupParameters;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.SwingRun;
import com.wmp.classTools.infSet.tools.GetSetsJSON;

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
    public static String version = "1.19.0";

    public static ArrayList<String> list = new ArrayList<>();

    public static final TreeMap<String, StartupParameters> allArgs = new TreeMap<>();

    public static final ArrayList<String> disButList = new ArrayList<>();

    public static boolean canExit = true;

    static {
        Log.exit(-1);
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
    public static void main(String[] args) throws IOException {


        Log.info.print("Main", "正在初始化...");
        boolean b = false;
        boolean startUpdate = false;
        try {
            GetSetsJSON setsJSON = new GetSetsJSON();

            b = isImportDay();

            startUpdate = setsJSON.isStartUpdate();
            canExit = setsJSON.isCanExit();
            disButList.addAll(setsJSON.getDisButList());


            for (int i = 0; i < args.length; i++) {
                args[i] = args[i].replace("/", "-");
            }
            Log.info.print("Main", "启动参数:" + Arrays.toString(args));

            if (args.length > 0) {
                list = new ArrayList<>(Arrays.asList(args));
                Log.info.print("Main", "使用的启动参数:" + Arrays.toString(args));
            }
        } catch (IOException e) {
            Log.error.print("Main", "初始化失败:" + e.getMessage());
            Log.showLogDialog();
            throw new RuntimeException(e);
        }
        try {
            SwingRun.show(b, allArgs, list, startUpdate);
        } catch (URISyntaxException e) {
            Log.error.print("Main", "窗口初始化失败:" + e.getMessage());
            Log.showLogDialog();
            throw new RuntimeException(e);
        }

        Log.info.print("Main", "初始化完毕");


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



}