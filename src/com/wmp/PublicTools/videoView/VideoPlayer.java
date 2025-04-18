package com.wmp.PublicTools.videoView;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class VideoPlayer {



    public static void playVideo(String filePath) throws IOException {
        File videoFile = new File(filePath);


        //newStyleToShowVideo(videoFile);
        try {
            Desktop.getDesktop().open(videoFile);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "播放出现错误\n" + e.getMessage(), "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
    }


}