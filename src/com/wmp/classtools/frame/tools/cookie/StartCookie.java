package com.wmp.classTools.frame.tools.cookie;

import com.wmp.PublicTools.OpenInExp;
import com.wmp.PublicTools.videoView.VideoPlayer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class StartCookie {

    public static void showCookie(String... pins){
        for (String pin : pins) {
            showCookie(pin);
        }

    }
    public static void showCookie(String pin){
        if (pin == null){
            JOptionPane.showMessageDialog(null, "pin为空", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            GetCookie getCookie = new GetCookie();
            TreeMap<String, Cookie> cookieMap = getCookie.getCookieMap();
            if (cookieMap.containsKey(pin)) {
                File RunFile = cookieMap.get(pin).getRunPath();
                String style = cookieMap.get(pin).getStyle();
                switch (style){
                    case "image", "music", "other" ->{
                        Desktop.getDesktop().open(RunFile);
                    }
                    case "video"->{
                        VideoPlayer.playVideo(RunFile.getPath());
                    }
                    case "exe" -> {

                        if (!Desktop.isDesktopSupported()) {
                            JOptionPane.showMessageDialog(null, "不支持运行exe文件", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        ArrayList<String> parameters = cookieMap.get(pin).getParameters();
                        System.out.println("启动参数:" + parameters);

                        ArrayList<String> temp = new ArrayList<>();
                        temp.add(RunFile.getPath());

                        if (!parameters.isEmpty()){
                            temp.addAll(parameters);
                        }
                        String[] cmdArray = temp.toArray(new String[0]);
                        Runtime runtime = Runtime.getRuntime();
                        runtime.exec(cmdArray, null, RunFile.getParentFile());
                    }case "directory", "file" -> {
                        OpenInExp.open(RunFile.getPath());
                    }
                    default -> {
                        JOptionPane.showMessageDialog(null, "未知的cookie类型", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
                    }
                }
                new Thread(() -> {
                    JOptionPane.showMessageDialog(null, "已通知运行:" + getCookie.getCookieMap().get(pin).getName(), "通知", JOptionPane.INFORMATION_MESSAGE);
                }).start();

                //Runtime.getRuntime().exec(cookieFile.getRunPath());
            } else if (pin.equals("null")) {
                return;
            } else {
                JOptionPane.showMessageDialog(null, "错误的pin", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
