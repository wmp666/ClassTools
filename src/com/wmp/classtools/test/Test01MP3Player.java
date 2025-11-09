package com.wmp.classTools.test;

import com.wmp.PublicTools.printLog.Log;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.awt.*;
import java.io.InputStream;
import java.util.Random;

public class Test01MP3Player {
    public static void main(String[] args) {
        Toolkit.getDefaultToolkit().beep(); // 测试系统声音是否正常
        System.out.println("音频格式支持状态：" + Player.class.getProtectionDomain().getCodeSource().getLocation());


        InputStream inputStream;

        Random r = new Random();
        boolean b = r.nextBoolean();
        if (b) {
            inputStream = Log.class.getResourceAsStream("/music/err/kong.mp3");
        } else {
            inputStream = Log.class.getResourceAsStream("/music/err/yin.mp3");
        }
        System.out.println("播放:" + (b ? "空" : "荧"));

        Thread playThread = new Thread(() -> {

            try {
                if (inputStream != null) {
                    Player player = new Player(inputStream);
                    System.out.println("开始播放...");
                    player.play();

                } else {
                    System.err.println("错误：音乐文件加载失败，请检查资源路径");
                }
            } catch (JavaLayerException e) {
                System.err.println("播放器初始化失败：");
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("未知错误：");
                e.printStackTrace();
            }
        }, "PlayerErrorMp3");
        playThread.start();


        try {
            playThread.join(); // 等待播放线程完成
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("播放完成");
    }
}
