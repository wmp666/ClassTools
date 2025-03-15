package com.wmp.panel.timeView.panel;

import com.wmp.classtools.CTComponent.CTPanel;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class TimeViewPanel extends CTPanel {

    private final JLabel timeView = new JLabel();

    public TimeViewPanel(int mixY) {

        setMixY(mixY);



        //获取时间
        Date date = new Date();
        //格式化 11.22 23:05
        DateFormat dateFormat = new SimpleDateFormat("MM.dd HH:mm:ss");


        timeView.setText(dateFormat.format(date));
        timeView.setFont(new Font("微软雅黑", Font.BOLD, 23));
        timeView.setForeground(new Color(0x0090FF));
        timeView.setBounds(5,3,250,25);
        this.add(timeView);
        setMixY(25);

        this.setLayout(null);
        this.setSize(250,getMixY() + 5);
    }

    public JLabel getTimeView() {
        return timeView;
    }
}
