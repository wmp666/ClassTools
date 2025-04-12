package com.wmp.classTools.frame;

import com.wmp.Main;
import com.wmp.PublicTools.videoView.VideoLocalizer;
import com.wmp.PublicTools.videoView.VideoPlayer;
import com.wmp.classTools.frame.tools.cookie.StartCookie;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class EasterEgg {

    public static void getPin() {
        String s = JOptionPane.showInputDialog(null, "请输入■■", "祈愿", JOptionPane.PLAIN_MESSAGE);


        if (s != null) {
            try {

                String[] split = s.split(":");
                if (split.length == 2) {
                    if (split[0].equalsIgnoreCase("EasterEgg")) {
                        showEasterEgg(split[1]);
                    } else if (split[0].equalsIgnoreCase("cookie")) {
                        StartCookie.showCookie(split[1]);
                    } else{
                        JOptionPane.showMessageDialog(null, "请输入正确的格式", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (split.length == 1) {
                    showEasterEgg(s);
                }else{
                    JOptionPane.showMessageDialog(null, "请输入正确的格式", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
                }


            } catch (URISyntaxException | IOException e) {
                throw new RuntimeException(e);
            }
        }


    }

    public static void showEasterEgg(String pins) throws URISyntaxException, IOException {
        ArrayList<String> pin = new ArrayList<>(List.of(pins.split(";")));
        if (pin.contains("nj01")) {

            //String localDataPath = System.getenv("LOCALAPPDATA") + "\\ClassTools\\";
            String videoPath = Main.TempPath + "video\\01.mp4";

            File file = new File(videoPath);
            if (!file.exists()) {
                VideoLocalizer.copyEmbeddedVideo(Main.TempPath, "01.mp4");
            }

            VideoPlayer.playVideo(videoPath);
        }
        if (pin.contains("nj02")) {
            //String localDataPath = System.getenv("LOCALAPPDATA") + "\\ClassTools\\";
            String videoPath = Main.TempPath + "video\\02.mp4";
            File file = new File(videoPath);
            if (!file.exists()) {
                VideoLocalizer.copyEmbeddedVideo(Main.TempPath, "02.mp4");
            }

            VideoPlayer.playVideo(videoPath);
        }
        if (pin.contains("nj03")) {
            //String localDataPath = System.getenv("LOCALAPPDATA") + "\\ClassTools\\";
            String videoPath = Main.TempPath + "video\\03.mp4";
            File file = new File(videoPath);
            if (!file.exists()) {
                VideoLocalizer.copyEmbeddedVideo(Main.TempPath, "03.mp4");
            }

            VideoPlayer.playVideo(videoPath);
        }
    }
}
