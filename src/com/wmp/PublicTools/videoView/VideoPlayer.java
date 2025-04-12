package com.wmp.PublicTools.videoView;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class VideoPlayer {



    public static void playVideo(String filePath) throws IOException {
        File videoFile = new File(filePath);


        //newStyleToShowVideo(videoFile);
        Desktop.getDesktop().open(videoFile);
    }


}