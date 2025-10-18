package com.wmp.classTools.frame;

import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTIconButton;
import com.wmp.classTools.CTComponent.CTPanel;
import com.wmp.classTools.frame.tools.screenProduct.SetsScrInfo;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class ScreenProduct extends JDialog {

    private final JLabel timeView = new JLabel();
    private final JLabel otherLabel = new JLabel();

    private final JLabel viewLabel = new JLabel();

    private final Container c = this.getContentPane();

    private int index = 0;

    public ScreenProduct() throws IOException {

        initTimePanel();

        initWindow();

        this.getLayeredPane().add(viewLabel, Integer.valueOf(Integer.MIN_VALUE));


        SetsScrInfo setsScrInfo = new SetsScrInfo();

        if (setsScrInfo.getBGImagesLength() > 1)
            initBackground(new Random().nextInt(setsScrInfo.getBGImagesLength() - 1));
        initColor();


        //背景更新
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


        //让时间在组件中央显示
        timeView.setHorizontalAlignment(JLabel.CENTER);
        timeView.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG_BIG));
        timeView.setForeground(Color.WHITE);
        c.setBackground(Color.BLACK);
        c.add(timeView, BorderLayout.CENTER);

        //添加退出按钮 - 左侧
        CTIconButton exitButton = new CTIconButton(
                "/image/%s/exit_0.png", () -> {
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
        for (CTPanel ctPanel : MainWindow.allPanelList) {

            String name = Objects.isNull(ctPanel.getID()) ? "CTPanel" : ctPanel.getID();

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

        //刷新
        MainWindow.refreshPanel();

        this.setVisible(true);

        //时间刷新
        //格式化 11.22 23:05
        //让时间在组件显示
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

        c.setLayout(new BorderLayout());
    }

    private static Image resizeImage(ImageIcon icon, Dimension screenSize) {
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        int imageWidth = icon.getIconWidth();
        int imageHeight = icon.getIconHeight();

        double scale = Math.max((double) screenWidth / imageWidth, (double) screenHeight / imageHeight);

        int scaledWidth = (int) (imageWidth * scale);
        int scaledHeight = (int) (imageHeight * scale);

        Log.info.print("ScreenProduct", String.format("缩放比例：%s|宽:%s|高:%s\n原图大小:%s|%s\n", scale, scaledWidth, scaledHeight, imageWidth, imageHeight));

        return icon.getImage().getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
    }

    private void initBackground(int index) throws IOException {
        JPanel panel = (JPanel) this.getContentPane();


        SetsScrInfo setsScrInfo = new SetsScrInfo();


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        //背景
        {

            viewLabel.setBounds(0, 0, screenSize.width, screenSize.height);
            viewLabel.setBackground(CTColor.backColor);
            panel.setOpaque(true);

            String bgImagePath = setsScrInfo.getBGImagePath(index);
            if (bgImagePath != null) {
                // 使用ImageIO避免缓存并支持更多格式
                File imageFile = new File(bgImagePath);
                if (!imageFile.exists()) {
                    Log.warn.print("背景图片加载", "背景图片不存在: " + bgImagePath);
                    throw new IOException("背景图片不存在: " + bgImagePath);
                }

                Image image = ImageIO.read(imageFile);
                if (image == null) {
                    Log.warn.print("背景图片加载", "无法读取图片格式: " + bgImagePath);
                    throw new IOException("无法读取图片格式: " + bgImagePath);
                }

                ImageIcon icon = new ImageIcon(image);

                icon.setImage(resizeImage(icon, screenSize));

                panel.setOpaque(false);
                viewLabel.setIcon(icon);


            }
            viewLabel.revalidate();
            viewLabel.repaint();

        }
    }

    private void initColor() throws IOException {
        CTColor.setScreenProductColor();
        this.getContentPane().setBackground(CTColor.backColor);
    }

    private void initTimePanel() {

        timeView.setText("初始化...");
        timeView.setForeground(CTColor.mainColor);

        otherLabel.setText("初始化...");
        otherLabel.setForeground(CTColor.mainColor);

    }
}
