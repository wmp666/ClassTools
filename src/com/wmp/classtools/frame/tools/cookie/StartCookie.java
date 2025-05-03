package com.wmp.classTools.frame.tools.cookie;

import com.wmp.PublicTools.OpenInExp;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.PublicTools.videoView.VideoPlayer;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
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
            Log.error.print("StartCookie", "pin为空");
            return;
        }
        try {
            GetCookie getCookie = new GetCookie();
            TreeMap<String, Cookie> cookieMap = getCookie.getCookieMap();
            if (cookieMap.containsKey(pin)) {
                String run = cookieMap.get(pin).getRunPath();
                File RunFile = null;
                try {
                    RunFile = new File(run);
                } catch (Exception e) {
                    Log.warn.print("StartCookie", "run不是文件\n" + e.getMessage());
                    throw new RuntimeException(e);
                }
                String style = cookieMap.get(pin).getStyle();
                try {
                    switch (style){
                        case "image", "music", "other" ->{
                            Desktop.getDesktop().browse(URI.create(run));
                            //Desktop.getDesktop().open(RunFile);
                        }
                        case "video"->{
                            VideoPlayer.playVideo(RunFile.getPath());
                        }
                        case "exe" -> {

                            if (!Desktop.isDesktopSupported()) {
                                Log.error.print("StartCookie", "不支持运行此exe文件");
                                return;
                            }

                            ArrayList<String> parameters = cookieMap.get(pin).getParameters();

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
                            Log.error.print("StartCookie", "未知的cookie类型");
                        }
                    }
                } catch (Exception e) {
                    Log.error.print("StartCookie", "运行失败！\n" + e.getMessage());
                    throw new RuntimeException(e);
                }
                new Thread(() -> {
                    Log.info.message(null, "StartCookie", "已通知运行:" + getCookie.getCookieMap().get(pin).getName());
                }).start();

                //Runtime.getRuntime().exec(cookieFile.getRunPath());
            }else {
                Log.error.print("StartCookie", "错误的pin");
            }
        } catch (IOException e) {
            Log.error.print("StartCookie", "运行失败！\n" + e.getMessage());
        }

    }
}
