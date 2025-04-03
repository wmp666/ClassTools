package com.wmp.classTools.frame;

import javax.swing.*;
import java.awt.*;

public class LoadingWindow extends JWindow {
    public LoadingWindow() {
        this.setSize(350, 200);
        this.setLocationRelativeTo(null);
        //this.setAlwaysOnTop(true);

        ImageIcon defaultIcon = new ImageIcon( getClass().getResource("/image/icon.png"));
        defaultIcon.setImage(defaultIcon.getImage().getScaledInstance(180, 180, Image.SCALE_DEFAULT));



        JLabel label = new JLabel("正在加载...",defaultIcon, SwingConstants.CENTER);
        label.setFont(new Font("微软雅黑", Font.BOLD, 20));

        this.add(label);
    }
}
