package com.wmp.classtools.frame;

import com.wmp.Main;
import com.wmp.tools.VideoLocalizer;
import com.wmp.tools.VideoPlayer;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

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
    public static void show(String pin) throws URISyntaxException, IOException {
        if (pin.equals("nj01")) {

            String localDataPath = System.getenv("LOCALAPPDATA") + "\\ClassTools\\";
            String videoPath = localDataPath + "video\\01.mp4";

            File file = new File(videoPath);
            if (!file.exists()){
                VideoLocalizer.copyEmbeddedVideo(localDataPath, "01.mp4");
            }

            VideoPlayer.playVideo(videoPath);
        }
    }
}
