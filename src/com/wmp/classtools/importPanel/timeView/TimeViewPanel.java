package com.wmp.classTools.importPanel.timeView;

import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTButton;
import com.wmp.classTools.CTComponent.CTPanel;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.wmp.Main.allArgs;
import static com.wmp.Main.argsList;

public class TimeViewPanel extends CTPanel {

    private final JLabel timeView = new JLabel();

    private final Thread timeThread = new Thread(() -> {

        while (true) {
            //获取时间
            Date date02 = new Date();
            //格式化 11.22 23:05
            DateFormat dateFormat02 = new SimpleDateFormat("MM.dd HH:mm:ss");
            //让时间在组件左侧显示
            timeView.setHorizontalAlignment(JLabel.CENTER);
            timeView.setText(dateFormat02.format(date02));
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            repaint();
        }
    });

    public TimeViewPanel() throws MalformedURLException {

        this.setName("TimeViewPanel");
        this.setLayout(new BorderLayout());
        initPanel();

        //时间刷新

        timeThread.setDaemon(true);
        timeThread.start();
    }

    private void initPanel() throws MalformedURLException {
        //获取时间
        Date date = new Date();
        //格式化 11.22 23:05
        DateFormat dateFormat = new SimpleDateFormat("MM.dd HH:mm:ss");


        timeView.setText(dateFormat.format(date));
        timeView.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG));
        //timeView.setBackground(new Color(0x0ECECED, true));
        timeView.setForeground(CTColor.mainColor);
        this.add(timeView, BorderLayout.CENTER);

        CTButton viewTimeButton = new CTButton("全屏显示时间",
                "/image/%s/view_0.png",
                "/image/%s/view_1.png", 30, () -> {
            try {
                viewTimeInDeskTop();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        });

        this.add(viewTimeButton, BorderLayout.EAST);

        if (allArgs.get("TimeView:screen").contains(argsList)) {
            //执行你的代码
            viewTimeInDeskTop();
            Log.info.print("TimeView", "全屏显示时间");
        }

    }

    private void viewTimeInDeskTop() throws MalformedURLException {
        //设置屏幕大小
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        JDialog window = new JDialog();
        window.setIconImage(new ImageIcon(getClass().getResource("/image/icon.png")).getImage());
        window.setSize(screenWidth, screenHeight);
        window.setLocation(0, 0);
        window.setAlwaysOnTop(true);
        window.setUndecorated(true);

        Container c = window.getContentPane();
        c.setLayout(new BorderLayout());

        //让时间在组件中央显示
        timeView.setHorizontalAlignment(JLabel.CENTER);
        timeView.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG_BIG));
        c.setBackground(CTColor.backColor);

        c.add(timeView, BorderLayout.CENTER);


        CTButton exitButton = new CTButton(
                "/image/%s/exit_0.png",
                "/image/%s/exit_1.png", 1, () -> {
                window.setVisible(false);

            timeView.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG));
            this.add(timeView, BorderLayout.CENTER);

        });
        c.add(exitButton, BorderLayout.WEST);

        window.setVisible(true);
    }

    @Override
    public void refresh() {
        this.removeAll();

        try {
            initPanel();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }

}
