package com.wmp;

import com.wmp.classtools.frame.MainWindow;

import java.io.IOException;

public class Main {

    public static String version = "1.5.0";

    public static void main(String[] args) throws IOException {
        System.out.println("Hello, World!");
        String path = System.getenv ("LOCALAPPDATA");

        StringBuilder sb = new StringBuilder();
        sb.append(path).append("\\ClassTools\\");

        //System.out.println(sb);


        new MainWindow(sb.toString());
    }
}