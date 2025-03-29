package com.wmp;

import com.wmp.classtools.frame.LoadingWindow;
import com.wmp.classtools.frame.MainWindow;
import com.wmp.tools.GetNewerVersion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static String version = "1.8.0";

    public static ArrayList<String> list = new ArrayList<>();

    public static final ArrayList<String> allArgs = new ArrayList<>();

    static {
        allArgs.add("-TimeView:screen");
        allArgs.add("-StartUpdate:false");
    }
    public static void main(String[] args) throws IOException {

        System.out.println("程序支持的启动参数:" + allArgs);

        if (args.length > 0) {
            list = new ArrayList<>(Arrays.asList(args));
            System.out.println(list);
        }

        System.out.println("Hello, World!");
        String path = System.getenv ("LOCALAPPDATA");

        StringBuilder sb = new StringBuilder();
        sb.append(path).append("\\ClassTools\\");

        //System.out.println(sb);
        LoadingWindow loadingWindow = new LoadingWindow();

        loadingWindow.setVisible(true);

        if (!list.contains(allArgs.get(1))) {
            //执行你的代码
            GetNewerVersion.checkForUpdate(
                           loadingWindow, null);
            System.out.println("!-StartUpdate:false");
        }


        new MainWindow(sb.toString());
        loadingWindow.setVisible(false);
    }
}