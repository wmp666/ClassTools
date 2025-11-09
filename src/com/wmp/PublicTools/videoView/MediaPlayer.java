package com.wmp.PublicTools.videoView;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.appFileControl.MusicControl;
import com.wmp.PublicTools.printLog.Log;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MediaPlayer {

    public static void playVideo(String filePath) throws IOException {
        File videoFile = new File(filePath);


        //newStyleToShowVideo(videoFile);
        try {
            Desktop.getDesktop().open(videoFile);
        } catch (IOException e) {
            // 处理打开视频文件失败的情况
            Log.err.print(MediaPlayer.class, "播放出现错误", e);
        }
    }

    public static void playLocalMusic(String filePath) throws IOException {
        File musicFile = new File(filePath);


        //newStyleToShowVideo(videoFile);
        try {
            Desktop.getDesktop().open(musicFile);
        } catch (IOException e) {
            // 处理打开视频文件失败的情况
            Log.err.print(MediaPlayer.class, "播放出现错误", e);
        }
    }

    public static void playOther(String filePath) throws IOException {
        File file = new File(filePath);


        //newStyleToShowVideo(videoFile);
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            // 处理打开视频文件失败的情况
            Log.err.print(MediaPlayer.class, "播放出现错误", e);
        }
    }

    public static void playMusic(String... keys) {
        //key = keys[0].key[1].key[2] ... key[n]
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.length; i++) {
            sb.append(keys[i]);
            if (i != keys.length - 1) {
                sb.append(".");
            }
        }
        String key = sb.toString();

        Log.info.print(MediaPlayer.class.getName(), "播放音频:" + key);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Player player = MusicControl.getPlayer(key);
                if (player != null) {
                    try {
                        player.play();
                    } catch (Exception e) {
                        Log.err.print(MediaPlayer.class, "播放音频失败", e);
                    }
                }else{
                    Log.warn.print(MediaPlayer.class.getName(), "音频不存在:" + key);
                }
                timer.cancel();
            }
        }, 0);
    }

}