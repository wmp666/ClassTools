package com.wmp;

import com.wmp.classtools.frame.EasterEgg;
import com.wmp.classtools.frame.LoadingWindow;
import com.wmp.classtools.frame.MainWindow;
import com.wmp.tools.GetNewerVersion;
import com.wmp.tools.StartupParameters;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static String version = "1.8.3";

    public static ArrayList<String> list = new ArrayList<>();

    public static final ArrayList<StartupParameters> allArgs = new ArrayList<>();

    static {

        allArgs.add(StartupParameters.creative("-TimeView:screen", "/TimeView:screen"));
        allArgs.add(StartupParameters.creative("-StartUpdate:false", "/StartUpdate:false"));
        allArgs.add(StartupParameters.creative("-EasterEgg-pin:nj01", "/EasterEgg-pin:nj01"));
        allArgs.add(StartupParameters.creative("/s", "-s"));
        allArgs.add(StartupParameters.creative("/p", "-p"));
    }
    public static void main(String[] args) throws IOException, URISyntaxException {

        java.lang.System.out.println("程序支持的启动参数:" + allArgs);

        if (args.length > 0) {
            list = new ArrayList<>(Arrays.asList(args));
            java.lang.System.out.println(list);
        }

        java.lang.System.out.println("Hello, World!");
        String path = java.lang.System.getenv ("LOCALAPPDATA");

        StringBuilder sb = new StringBuilder();
        sb.append(path).append("\\ClassTools\\");

        //System.out.println(sb);
        LoadingWindow loadingWindow = new LoadingWindow();

        loadingWindow.setVisible(true);

        if (!(allArgs.get(1).contains(list) ||
                allArgs.get(3).contains(list) ||
                allArgs.get(4).contains(list))) {
            //执行你的代码
            GetNewerVersion.checkForUpdate(
                           loadingWindow, null);
            java.lang.System.out.println("-StartUpdate:true");
        }

        if (allArgs.get(2).contains(list)) {
            System.out.println("-EasterEgg-pin:nj01");
            //System.out.println();
            EasterEgg.show("nj01");
        }


        new MainWindow(sb.toString());
        loadingWindow.setVisible(false);
    }


}