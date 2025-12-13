package com.wmp;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.EasterEgg.EasterEgg;
import com.wmp.PublicTools.StartupParameters;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTOptionPane;
import com.wmp.classTools.SwingRun;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class Main {

    public static final TreeMap<String, StartupParameters> allArgs = new TreeMap<>();
    public static ArrayList<String> argsList = new ArrayList<>();

    static {
        //加载基础目录

        allArgs.put("StartUpdate:false", StartupParameters.creative("-StartUpdate:false", "/StartUpdate:false"));
        allArgs.put("screenProduct:show", StartupParameters.creative("/s", "-s"));
        allArgs.put("screenProduct:view", StartupParameters.creative("/p", "-p"));

        allArgs.put("CTInfo:isError", StartupParameters.creative("/CTInfo:error", "-CTInfo:error"));
    }

    public static void main(String[] args) throws IOException {
        JDialog wait = new JDialog();
        wait.setUndecorated(true);
        wait.setAlwaysOnTop(true);

        JLabel label = new JLabel("<html><body><font color='#29A5E3'>正在加载<br>班级工具运行所需的基础数据</font></body></html>");
        label.setFont(new Font(null, Font.PLAIN, 40));
        label.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        wait.add(label);

        wait.pack();
        wait.setLocationRelativeTo(null);
        wait.setVisible(true);

        new CTInfo();

        try {
            Log.info.loading.showDialog("程序加载", "正在启动数据...");
        } catch (Exception ignored) {

        }
        wait.setVisible(false);




        boolean b = false;
        boolean startUpdate = true;
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
            Log.info.loading.closeDialog("程序加载");
            Log.err.print(Main.class, "初始化失败", e);
            Log.showLogDialog(true);
        }

        CTInfo.isError = EasterEgg.getEasterEggItem(EasterEgg.STYLE_ERROR);
        if (CTInfo.isError) {

            CTInfo.version = "999.999.999";//错误版本号(无法更新)
            CTInfo.appName = "班级病毒";
            CTInfo.author = "银狼";
            CTInfo.iconPath = "/image/err/icon.png";
            b = false;
            CTColor.setErrorColor();//修改颜色
        }


        boolean finalStartUpdate = startUpdate;
        boolean finalB = b;

        Log.info.loading.closeDialog("程序加载");
        try {
            SwingRun.show(finalB, finalStartUpdate);
        } catch (Exception e) {
            Log.err.print(Main.class, "窗口初始化失败", e);
            Log.showLogDialog(true);
        }


        Log.info.print("Main", "初始化完毕");


    }
}