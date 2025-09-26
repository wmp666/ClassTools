package com.wmp.PublicTools.videoView;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.printLog.Log;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class MediaPlayer {

    public static final int MUSIC_STYLE_ERROR = 0;


    public static void playVideo(String filePath) throws IOException {
        File videoFile = new File(filePath);


        //newStyleToShowVideo(videoFile);
        try {
            Desktop.getDesktop().open(videoFile);
        } catch (IOException e) {
            // 处理打开视频文件失败的情况
            Log.err.print("MediaPlayer", "播放出现错误\n" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void playMusic(String filePath) throws IOException {
        File musicFile = new File(filePath);


        //newStyleToShowVideo(videoFile);
        try {
            Desktop.getDesktop().open(musicFile);
        } catch (IOException e) {
            // 处理打开视频文件失败的情况
            Log.err.print("MediaPlayer", "播放出现错误\n" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void playMusic(int style, boolean inThread) {
        InputStream inputStream = null;

        switch (style) {
            case MUSIC_STYLE_ERROR -> {

                // 1/10概率播放
                {
                    Random r = new Random();
                    if (!(r.nextInt(10) == 0)) return;
                }

                Random r = new Random();

                if (CTInfo.isError) {
                    inputStream = Log.class.getResourceAsStream("/music/error-yl.mp3");
                } else {
                    int i = r.nextInt(3);
                    switch (i) {
                        case 0 -> inputStream = Log.class.getResourceAsStream("/music/error-kong.mp3");
                        case 1 -> inputStream = Log.class.getResourceAsStream("/music/error-yin.mp3");
                        case 2 -> inputStream = Log.class.getResourceAsStream("/music/error-oll.mp3");

                    }
                }

            }

            default -> inputStream = Log.class.getResourceAsStream("/music/error-kong.mp3");
        }


        if (inputStream != null) {
            if (inThread) {
                InputStream finalInputStream = inputStream;
                new Thread(() -> {


                    try {
                        Player player = new Player(finalInputStream);
                        player.play();
                    } catch (JavaLayerException e) {
                        throw new RuntimeException(e);
                    }

                }, "PlayerErrorMp3").start();
            } else {
                try {
                    Player player = new Player(inputStream);
                    player.play();
                } catch (JavaLayerException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}