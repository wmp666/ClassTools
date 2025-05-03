package com.wmp.classTools.importPanel.timeView;

import com.wmp.CTColor;
import com.wmp.classTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTButton;
import com.wmp.classTools.CTComponent.CTPanel;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.wmp.Main.allArgs;
import static com.wmp.Main.list;

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

    public TimeViewPanel(int nextPanelY) throws MalformedURLException {


        setNextPanelY(nextPanelY);

        initPanel();

        //时间刷新


        timeThread.start();
    }

    private void initPanel() throws MalformedURLException {
        //获取时间
        Date date = new Date();
        //格式化 11.22 23:05
        DateFormat dateFormat = new SimpleDateFormat("MM.dd HH:mm:ss");


        timeView.setText(dateFormat.format(date));
        timeView.setFont(new Font("微软雅黑", Font.BOLD, 23));
        //timeView.setBackground(new Color(0x0ECECED, true));
        timeView.setForeground(CTColor.mainColor);
        timeView.setBounds(5,3,180,32);
        this.add(timeView);
        setNextPanelY(32);

        CTButton viewTimeButton = new CTButton("全屏显示时间",
                "/image/%s/view_0.png",
                "/image/%s/view_1.png", 30, () -> {
            try {
                viewTimeInDeskTop(0);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        });

        viewTimeButton.setLocation(210, 5 );
        this.add(viewTimeButton);

        if (allArgs.get("TimeView:screen").contains(list)) {
            //执行你的代码
            viewTimeInDeskTop(0);
            Log.info.print("TimeView", "全屏显示时间");
        }else if(allArgs.get("screenProduct:show").contains(list)){

            Log.info.print("TimeView", "屏保模式");
            viewTimeInDeskTop(1);
        }

        this.setLayout(null);
        this.setSize(250, 37);
    }

    private void viewTimeInDeskTop(int i) throws MalformedURLException {
        //设置屏幕大小
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        JWindow window = new JWindow();
        window.setIconImage(new ImageIcon(getClass().getResource("/image/icon.png")).getImage());
        window.setSize(screenWidth, screenHeight);
        window.setLocation(0, 0);
        window.setAlwaysOnTop(true);

        Container c = window.getContentPane();
        c.setLayout(new BorderLayout());

        //让时间在组件中央显示
        timeView.setHorizontalAlignment(JLabel.CENTER);
        timeView.setFont(new Font("微软雅黑", Font.BOLD, 100));
        c.setBackground(CTColor.backColor);

        c.add(timeView, BorderLayout.CENTER);

         if (i == 1) {
            timeView.setForeground(Color.WHITE);
            c.setBackground(Color.BLACK);

        }


        CTButton exitButton = new CTButton(
                "/image/%s/exit_0.png",
                "/image/%s/exit_1.png", 1, () -> {
            if (i == 0) {
                window.setVisible(false);

                timeView.setFont(new Font("微软雅黑", Font.BOLD, 23));
                timeView.setBounds(5,3,180,32);
                this.add(timeView);
            } else if (i == 1) {
                Log.exit(0);
                //System.exit(0);
            }
        });

         if (i == 1){
             exitButton.setBackground(Color.BLACK);
         }

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
