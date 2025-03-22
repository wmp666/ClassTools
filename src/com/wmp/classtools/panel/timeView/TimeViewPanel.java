package com.wmp.classtools.panel.timeView;

import com.wmp.CTColor;
import com.wmp.classtools.CTComponent.CTPanel;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeViewPanel extends CTPanel {

    private final JLabel timeView = new JLabel();

    public TimeViewPanel(int mixY) {


        setNextPanelY(mixY);



        //获取时间
        Date date = new Date();
        //格式化 11.22 23:05
        DateFormat dateFormat = new SimpleDateFormat("MM.dd HH:mm:ss");


        timeView.setText(dateFormat.format(date));
        timeView.setFont(new Font("微软雅黑", Font.BOLD, 23));
        //timeView.setBackground(new Color(0x0ECECED, true));
        timeView.setForeground(CTColor.mainColor);
        timeView.setBounds(5,3,250,25);
        this.add(timeView);
        setNextPanelY(25);

        this.setLayout(null);
        this.setSize(250, getNextPanelY() + 5);
    }

    public JLabel getTimeView() {
        return timeView;
    }
}
