package com.wmp.classTools.frame;

import com.wmp.Main;
import com.wmp.tools.videoView.VideoLocalizer;
import com.wmp.tools.videoView.VideoPlayer;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class EasterEgg {

    public static void getPin(){
        String s = JOptionPane.showInputDialog(null, "请输入■■", "祈愿", JOptionPane.PLAIN_MESSAGE);

                try {
                    show(s);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

    }
    public static void show(String pins) throws URISyntaxException, IOException {
        ArrayList<String> pin = new ArrayList<>(List.of(pins.split(";")));
        if (pin.contains("nj01")) {

            //String localDataPath = System.getenv("LOCALAPPDATA") + "\\ClassTools\\";
            String videoPath = Main.TempPath + "video\\01.mp4";

            File file = new File(videoPath);
            if (!file.exists()){
                VideoLocalizer.copyEmbeddedVideo(Main.TempPath, "01.mp4");
            }

            VideoPlayer.playVideo(videoPath);
        }
        if (pin.contains("nj02")){
            //String localDataPath = System.getenv("LOCALAPPDATA") + "\\ClassTools\\";
            String videoPath = Main.TempPath + "video\\02.mp4";
            File file = new File(videoPath);
            if (!file.exists()){
                VideoLocalizer.copyEmbeddedVideo(Main.TempPath, "02.mp4");
            }

            VideoPlayer.playVideo(videoPath);
        }
        if (pin.contains("nj03")){
            //String localDataPath = System.getenv("LOCALAPPDATA") + "\\ClassTools\\";
            String videoPath = Main.TempPath + "video\\03.mp4";
            File file = new File(videoPath);
            if (!file.exists()){
                VideoLocalizer.copyEmbeddedVideo(Main.TempPath, "03.mp4");
            }

            VideoPlayer.playVideo(videoPath);
        }
    }
}
