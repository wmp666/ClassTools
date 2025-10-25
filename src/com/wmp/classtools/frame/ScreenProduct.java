package com.wmp.classTools.frame;

import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTButton.CTIconButton;
import com.wmp.classTools.CTComponent.CTPanel.CTViewPanel;
import com.wmp.classTools.frame.tools.screenProduct.SetsScrInfo;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class ScreenProduct extends JDialog {

    /*private final JLabel timeView = new JLabel();
    private final JLabel otherLabel = new JLabel();*/
    private static ScreenProduct screenProduct;

    private final JLabel imageViewLabel = new JLabel();

    private final Container c = this.getContentPane();

    private int index = 0;

    private static final HashMap<String, String[]> panelLocationMap = new HashMap<>();
    public ScreenProduct() throws IOException {
        screenProduct = this;
        panelLocationMap.put("北方", new String[]{"OtherTimeThingPanel", "WeatherInfoPanel"});
        panelLocationMap.put("南方", new String[]{"ETPanel"});
        panelLocationMap.put("中间", new String[]{"TimeViewPanel"});

        initWindow();

        this.getLayeredPane().add(imageViewLabel, Integer.valueOf(Integer.MIN_VALUE));

        //获取屏保设置
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



        //刷新组件内容
        /*Timer repaint = new Timer(34, e ->
                MainWindow.allPanelList.forEach(ctViewPanel ->{
                    ctViewPanel.toScreenProductViewPanel();
                    try {
                        ctViewPanel.repaint();
                    } catch (Exception ex) {
                        Log.err.print(ScreenProduct.class, "刷新失败", ex);
                    }
                }));
        repaint.setRepeats(true);
        repaint.start();*/

        //强刷新
        Timer strongRepaint = new Timer(60*1000, e -> {

            refreshScreenProductPanel();
        });
        strongRepaint.setRepeats(true);
        strongRepaint.start();

        refreshScreenProductPanel();
        this.setVisible(true);
    }

    public static void refreshScreenProductPanel() {
        //刷新要显示的组件
        MainWindow.refreshPanel();

        TreeMap<String, CTViewPanel[]> panelMap = new TreeMap<>();

        MainWindow.panelMap.forEach((key, value) -> {
            for (CTViewPanel ctViewPanel : value) {

                if (Arrays.asList(panelLocationMap.get("北方")).contains(ctViewPanel.getID())){
                    java.util.List<CTViewPanel> temp = new java.util.ArrayList<>(Arrays.asList(panelMap.getOrDefault("北方", new CTViewPanel[0])));
                    temp.add(ctViewPanel);
                    panelMap.put("北方", temp.toArray(new CTViewPanel[0]));
                } else if ( Arrays.asList(panelLocationMap.get("南方")).contains(ctViewPanel.getID())) {
                    java.util.List<CTViewPanel> temp = new java.util.ArrayList<>(Arrays.asList(panelMap.getOrDefault("南方", new CTViewPanel[0])));
                    temp.add(ctViewPanel);
                    panelMap.put("南方", temp.toArray(new CTViewPanel[0]));
                } else if (Arrays.asList(panelLocationMap.get("中间")).contains(ctViewPanel.getID())) {
                    java.util.List<CTViewPanel> temp = new java.util.ArrayList<>(Arrays.asList(panelMap.getOrDefault("中间", new CTViewPanel[0])));
                    temp.add(ctViewPanel);
                    panelMap.put("中间", temp.toArray(new CTViewPanel[0]));
                } else {
                    java.util.List<CTViewPanel> temp = new java.util.ArrayList<>(Arrays.asList(panelMap.getOrDefault("东方", new CTViewPanel[0])));
                    temp.add(ctViewPanel);
                    panelMap.put("东方", temp.toArray(new CTViewPanel[0]));
                }
/*
                switch (ctViewPanel.getID()) {
                    case "TimeViewPanel" -> panelMap.put("中间", List.of(ctViewPanel).toArray(new CTViewPanel[0]));
                    case "ETPanel" -> panelMap.put("南方", List.of(ctViewPanel).toArray(new CTViewPanel[0]));
                    case "OtherTimeThingPanel" ->
                            panelMap.put("北方", List.of(ctViewPanel).toArray(new CTViewPanel[0]));
                    default -> {
                        if (panelMap.containsKey("东方")) {
                            CTViewPanel[] tempPanels = panelMap.get("东方");
                            tempPanels = Arrays.copyOf(tempPanels, tempPanels.length + 1);
                            tempPanels[tempPanels.length - 1] = ctViewPanel;
                            panelMap.put("东方", tempPanels);
                        } else panelMap.put("东方", List.of(ctViewPanel).toArray(new CTViewPanel[0]));
                    }
                }*/
            }
        });

        JPanel northPanel = new JPanel();
        JPanel southPanel = new JPanel();
        JPanel centerPanel = new JPanel();
        JPanel eastPanel = new JPanel();
        JPanel otherPanel = new JPanel();

        northPanel.setLayout(new GridBagLayout());
        southPanel.setLayout(new GridBagLayout());
        centerPanel.setLayout(new BorderLayout());
        otherPanel.setLayout(new GridBagLayout());
        eastPanel.setLayout(new BorderLayout());

        northPanel.setOpaque(false);
        southPanel.setOpaque(false);
        centerPanel.setOpaque(false);
        otherPanel.setOpaque(false);
        eastPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1; // 添加水平权重
        //gbc.fill = GridBagConstraints.HORIZONTAL; // 水平填充
        gbc.insets = new Insets(0, 0, 0, 0); // 确保没有额外的边距

        TreeMap<String, Integer> countMap = new TreeMap<>();

        //将要显示的组件添加到显示列表
        panelMap.forEach((key, value) -> {
            for (CTViewPanel panel : value) {
                countMap.put(key, countMap.getOrDefault(key, 0) + 1);
                gbc.gridy = countMap.get(key);
                switch (key) {
                    case "北方" -> northPanel.add(panel, gbc);
                    case "南方" -> southPanel.add(panel, gbc);
                    case "中间" -> centerPanel.add(panel, BorderLayout.CENTER);
                    default -> {
                        if (panel.getID().equals("FinalPanel"))
                            eastPanel.add(panel, BorderLayout.SOUTH);
                        else otherPanel.add(panel, gbc);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(otherPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        eastPanel.add(scrollPane, BorderLayout.CENTER);



        // 在添加新组件前移除所有现有组件
        screenProduct.getContentPane().removeAll();
        screenProduct.getContentPane().add(northPanel, BorderLayout.NORTH);
        screenProduct.getContentPane().add(southPanel, BorderLayout.SOUTH);
        screenProduct.getContentPane().add(centerPanel, BorderLayout.CENTER);
        screenProduct.getContentPane().add(eastPanel, BorderLayout.EAST);

        //添加退出按钮 - 左侧
        CTIconButton exitButton = new CTIconButton(
                "/image/%s/exit_0.png", () -> {
            screenProduct.setVisible(false);
            Log.exit(0);
        });
        exitButton.setBackground(Color.BLACK);
        screenProduct.getContentPane().add(exitButton, BorderLayout.WEST);
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

            imageViewLabel.setBounds(0, 0, screenSize.width, screenSize.height);
            imageViewLabel.setBackground(CTColor.backColor);
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
                imageViewLabel.setIcon(icon);


            }
            imageViewLabel.revalidate();
            imageViewLabel.repaint();

        }
    }

    private void initColor() throws IOException {
        CTColor.setScreenProductColor();
        this.getContentPane().setBackground(CTColor.backColor);
    }
/*
    private void initTimePanel() {

        timeView.setText("初始化...");
        timeView.setForeground(CTColor.mainColor);

        otherLabel.setText("初始化...");
        otherLabel.setForeground(CTColor.mainColor);

    }*/
}
