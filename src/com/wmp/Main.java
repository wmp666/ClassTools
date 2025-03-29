package com.wmp;

import com.wmp.classtools.frame.EasterEgg;
import com.wmp.classtools.frame.LoadingWindow;
import com.wmp.classtools.frame.MainWindow;
import com.wmp.tools.GetNewerVersion;
import com.wmp.tools.VideoPlayer;
import org.jsoup.select.Evaluator;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static String version = "1.8.1";

    public static ArrayList<String> list = new ArrayList<>();

    public static final ArrayList<String> allArgs = new ArrayList<>();

    static {
        allArgs.add("-TimeView:screen");
        allArgs.add("-StartUpdate:false");
        allArgs.add("-EasterEgg-pin:nj01");
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

        if (!list.contains(allArgs.get(1))) {
            //执行你的代码
            GetNewerVersion.checkForUpdate(
                           loadingWindow, null);
            java.lang.System.out.println("-StartUpdate:true");
        }

        if (list.contains(allArgs.get(2))) {
            System.out.println("-EasterEgg-pin:nj01");
            //System.out.println();
            EasterEgg.show("nj01");
        }


        new MainWindow(sb.toString());
        loadingWindow.setVisible(false);
    }


}