package com.wmp.classTools.frame;

import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTIconButton;
import com.wmp.classTools.CTComponent.CTPanel;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ScreenProduct extends JDialog {

    private final JLabel timeView = new JLabel();

    public ScreenProduct() throws MalformedURLException {

        initTimePanel();

        initWindow();

        Container c = this.getContentPane();
        c.setLayout(new BorderLayout());

        //让时间在组件中央显示
        timeView.setHorizontalAlignment(JLabel.CENTER);
        timeView.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG_BIG));
        timeView.setForeground(Color.WHITE);
        c.setBackground(Color.BLACK);
        c.add(timeView, BorderLayout.CENTER);

        //添加退出按钮 - 左侧
        CTIconButton exitButton = new CTIconButton(
                "/image/%s/exit_0.png",
                "/image/%s/exit_1.png", 1, () -> {
            this.setVisible(false);
            Log.exit(0);
        });
        exitButton.setBackground(Color.BLACK);
        c.add(exitButton, BorderLayout.WEST);


        //添加CTPanel - 右侧
        JScrollPane scrollPane = new JScrollPane();

        JPanel tempPanel = new JPanel(new GridBagLayout());
        tempPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.WEST;// 左对齐
        for (CTPanel ctPanel : MainWindow.showPanelList) {

            String name = Objects.isNull(ctPanel.getName()) ? "CTPanel" : ctPanel.getName();

            if (name.equals("TimeViewPanel")) continue;

            //添加ETPanel - 下方
            if (name.equals("ETPanel")) c.add(ctPanel, BorderLayout.SOUTH);
            else {
                gbc.gridy++;
                ctPanel.setOpaque(false);
                tempPanel.add(ctPanel, gbc);
            }

        }
        //隐藏边框
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setViewportView(tempPanel);
        c.add(scrollPane, BorderLayout.EAST);

        this.setVisible(true);

        //时间刷新
        //获取时间
        //格式化 11.22 23:05
        //让时间在组件左侧显示
        Thread timeThread = new Thread(() -> {

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
        timeThread.setDaemon(true);
        timeThread.start();
    }

    private void initWindow() {
        //设置屏幕大小
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        this.setUndecorated(true);
        this.setIconImage(new ImageIcon(getClass().getResource("/image/icon.png")).getImage());
        this.setSize(screenWidth, screenHeight);
        this.setLocation(0, 0);
    }


    private void initTimePanel() {

        timeView.setText("初始化...");
        //timeView.setFont(new Font("微软雅黑", Font.BOLD, 23));
        //timeView.setBackground(new Color(0x0ECECED, true));
        timeView.setForeground(CTColor.mainColor);
        timeView.setBounds(5, 3, 180, 32);

    }
}
