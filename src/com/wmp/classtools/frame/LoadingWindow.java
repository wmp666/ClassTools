package com.wmp.classTools.frame;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class LoadingWindow extends JWindow {

    public static final int STYLE_NORMAL = 0;
    public static final int STYLE_SCREEN = 1;

    public LoadingWindow() {
        this(LoadingWindow.class.getResource("/image/icon.png"),
                180, 180, "正在加载...", false, 0);
    }

    public LoadingWindow(URL url, int width, int height, String text){
        this(url, width, height, text, false, 0, 0);
    }

    public LoadingWindow(URL url, int width, int height, String text, boolean mustWait, long time){
        this(url, width, height, text, mustWait, time, 0);
    }

    public LoadingWindow(URL url, int width, int height, String text, boolean mustWait, long time, int windowStyle){
        this.setSize(350, 200);

        if (windowStyle == STYLE_SCREEN){
            this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        }

        this.setLocationRelativeTo(null);
        //this.setAlwaysOnTop(true);

        ImageIcon defaultIcon = new ImageIcon(url);
        defaultIcon.setImage(defaultIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));



        JLabel label = new JLabel(text,defaultIcon, SwingConstants.CENTER);
        label.setBackground(Color.WHITE);
        label.setFont(new Font("微软雅黑", Font.BOLD, 20));

        this.getContentPane().setBackground(Color.WHITE);
        this.add(label);

        this.setVisible(true);
        if (mustWait){
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
