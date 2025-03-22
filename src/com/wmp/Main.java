package com.wmp;

import com.wmp.classtools.frame.LoadingWindow;
import com.wmp.classtools.frame.MainWindow;

import java.io.IOException;

public class Main {

    public static String version = "1.5.2";

    public static void main(String[] args) throws IOException {
        System.out.println("Hello, World!");
        String path = System.getenv ("LOCALAPPDATA");

        StringBuilder sb = new StringBuilder();
        sb.append(path).append("\\ClassTools\\");

        //System.out.println(sb);
        LoadingWindow loadingWindow = new LoadingWindow();

        loadingWindow.setVisible(true);
        new MainWindow(sb.toString());
        loadingWindow.setVisible(false);
    }
}