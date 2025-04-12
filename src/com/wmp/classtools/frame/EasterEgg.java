package com.wmp.classTools.frame;

import com.wmp.Main;
import com.wmp.PublicTools.io.ResourceLocalizer;
import com.wmp.PublicTools.videoView.VideoPlayer;
import com.wmp.classTools.frame.tools.cookie.StartCookie;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;

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

    public static void showEasterEgg(String... pins) throws URISyntaxException, IOException {
        for (String pin : pins) {
            showEasterEgg(pin);
        }
    }
    public static void showEasterEgg(String pin) throws URISyntaxException, IOException {

        ResourceLocalizer.copyEmbeddedVideo(Main.TempPath + "video\\", "/video/", pin +".mp4");
        //VideoLocalizer.copyEmbeddedVideo(Main.TempPath, pin + ".mp4");
        String videoPath = "null";
        switch (pin) {
            case "nj01":
                videoPath = Main.TempPath + "video\\nj01.mp4";
                break;
            case "nj02":
                videoPath = Main.TempPath + "video\\nj02.mp4";
                break;
            case "nj03":
                videoPath = Main.TempPath + "video\\nj03.mp4";
                break;
            case "nj04":
                videoPath = Main.TempPath + "video\\nj04.mp4";
                break;
            default:
                JOptionPane.showMessageDialog(null, "请输入正确的格式", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
                break;

        }
        VideoPlayer.playVideo(videoPath);

    }
}
