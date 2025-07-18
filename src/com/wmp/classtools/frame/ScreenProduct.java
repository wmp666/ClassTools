package com.wmp.classTools.frame;

import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTIconButton;
import com.wmp.classTools.CTComponent.CTPanel;
import com.wmp.classTools.frame.tools.screenProduct.SetsScrInfo;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ScreenProduct extends JDialog {

    private final JLabel timeView = new JLabel();

    private final JLabel viewLabel = new JLabel();

    private int index = 0;

    public ScreenProduct() throws IOException {

        initTimePanel();

        initWindow();

        this.getLayeredPane().add(viewLabel, Integer.valueOf(Integer.MIN_VALUE));

        initBackground(0);
        initColor();

        SetsScrInfo setsScrInfo = new SetsScrInfo();
        Timer updateBG = new Timer(setsScrInfo.getRepaintTimer() * 1000, e -> {

            try {
                initBackground(index);
                initColor();
                if (index < setsScrInfo.getBGImagesLength() - 1) index++;
                else index = 0;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        updateBG.setRepeats(true);
        updateBG.start();


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
        Timer timer = new Timer(300, e -> {
            //获取时间
            Date date = new Date();
            //格式化 11.22 23:05
            DateFormat dateFormat = new SimpleDateFormat("MM.dd HH:mm:ss");
            timeView.setText(dateFormat.format(date));
            timeView.setForeground(CTColor.mainColor);
            repaint();
        });
        timer.start();
        timer.setRepeats(true);//循环
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

    private void initBackground(int index) throws IOException {
        JPanel panel = (JPanel) this.getContentPane();
        panel.setOpaque(false);

        SetsScrInfo setsScrInfo = new SetsScrInfo();


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        //背景
        {

            viewLabel.setBounds(0, 0, screenSize.width, screenSize.height);

            String bgImagePath = setsScrInfo.getBGImagePath(index);
            if (bgImagePath != null) {
                ImageIcon icon = new ImageIcon(bgImagePath);

                icon.setImage(icon.getImage().getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_SMOOTH));

                viewLabel.setIcon(icon);

                viewLabel.revalidate();
                viewLabel.repaint();
            } else {
                viewLabel.setBackground(CTColor.backColor);
                panel.setOpaque(true);
            }

        }
    }

    private void initColor() throws IOException {
        CTColor.setScreenProductColor();
        this.getContentPane().setBackground(CTColor.backColor);
    }

    private void initTimePanel() {

        timeView.setText("初始化...");
        //timeView.setFont(new Font("微软雅黑", Font.BOLD, 23));
        //timeView.setBackground(new Color(0x0ECECED, true));
        timeView.setForeground(CTColor.mainColor);
        timeView.setBounds(5, 3, 180, 32);

    }
}
