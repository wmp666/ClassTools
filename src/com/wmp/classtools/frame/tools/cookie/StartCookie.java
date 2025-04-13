package com.wmp.classTools.frame.tools.cookie;

import com.wmp.PublicTools.OpenInExp;
import com.wmp.PublicTools.videoView.VideoPlayer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
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
                File cookieFile = cookieMap.get(pin).getPath();
                String style = cookieMap.get(pin).getStyle();
                switch (style){
                    case "image", "music", "other" ->{
                        Desktop.getDesktop().open(cookieFile);
                    }
                    case "video"->{
                        VideoPlayer.playVideo(cookieFile.getPath());
                    }
                    case "exe" -> {
                        Runtime runtime = Runtime.getRuntime();
                        runtime.exec(cookieFile.getPath(), null, cookieFile.getParentFile());
                    }case "directory", "file" -> {
                        OpenInExp.open(cookieFile.getPath());
                    }
                    default -> {
                        JOptionPane.showMessageDialog(null, "未知的cookie类型", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
                    }
                }
                new Thread(() -> {
                    JOptionPane.showMessageDialog(null, "已通知运行:" + getCookie.getCookieMap().get(pin).getName(), "通知", JOptionPane.INFORMATION_MESSAGE);
                }).start();

                //Runtime.getRuntime().exec(cookieFile.getPath());
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
