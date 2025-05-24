package com.wmp.PublicTools.videoView;

import com.wmp.PublicTools.printLog.Log;

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
            // 处理打开视频文件失败的情况
            Log.err.print("VideoPlayer", "播放出现错误\n" + e.getMessage());
            throw new RuntimeException(e);
        }
    }


}