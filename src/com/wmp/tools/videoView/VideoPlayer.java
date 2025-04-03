package com.wmp.tools.videoView;

import java.awt.Desktop;
import java.io.File;

public class VideoPlayer {
    public static void playVideo(String filePath) {
        try {
            System.out.println("播放视频: " + filePath);
            File videoFile = new File(filePath);

            if (videoFile.exists()) {
                Desktop.getDesktop().open(videoFile);
            } else {
                System.err.println("文件不存在: " + filePath);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
