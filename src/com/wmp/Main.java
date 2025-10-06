package com.wmp;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.EasterEgg.EasterEgg;
import com.wmp.PublicTools.StartupParameters;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.SwingRun;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class Main{

    public static ArrayList<String> argsList = new ArrayList<>();
    public static final TreeMap<String, StartupParameters> allArgs = new TreeMap<>();

    static {
        //加载基础目录

        allArgs.put("StartUpdate:false", StartupParameters.creative("-StartUpdate:false", "/StartUpdate:false"));
        allArgs.put("screenProduct:show", StartupParameters.creative("/s", "-s"));
        allArgs.put("screenProduct:view", StartupParameters.creative("/p", "-p"));
    }

    public static void main(String[] args) throws IOException {

        CTInfo.init();

        Log.info.systemPrint("Main", "正在初始化...");
        boolean b;
        boolean startUpdate;
        try {
            //GetSetsJSON setsJSON = new GetSetsJSON();

            b = EasterEgg.getEasterEggItem(EasterEgg.STYLE_IMPORT_DAY);

            startUpdate = CTInfo.StartUpdate;

            for (int i = 0; i < args.length; i++) {
                args[i] = args[i].replace("/", "-");
            }
            Log.info.print("Main", "启动参数:" + Arrays.toString(args));

            if (args.length > 0) {
                argsList = new ArrayList<>(Arrays.asList(args));
                Log.info.print("Main", "使用的启动参数:" + Arrays.toString(args));
            }
        } catch (Exception e) {
            Log.err.print(Main.class, "初始化失败:" + e.getMessage());
            Log.showLogDialog();
            throw new RuntimeException(e);
        }

        CTInfo.isError = EasterEgg.getEasterEggItem(EasterEgg.STYLE_ERROR);
        //Log.info.print("[Main]", "是否被骇客入侵:" + isError);
        if (CTInfo.isError) {

            Log.info.message(null, "Main", "这次能让我玩得开心点吗？");

            CTInfo.version = "999.999.999";//错误版本号(无法更新)
            CTInfo.appName = "班级病毒";
            CTInfo.author = "银狼";
            CTInfo.iconPath = "/image/error/icon.png";
            b = false;
            CTColor.setErrorColor();//修改颜色
        }


        try {
            SwingRun.show(b, startUpdate);
        } catch (Exception e) {
            Log.err.print(Main.class, "窗口初始化失败:" + e.getMessage());
            Log.showLogDialog();
            throw new RuntimeException(e);
        }

        Log.info.print("Main", "初始化完毕");


    }
}