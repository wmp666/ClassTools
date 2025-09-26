package com.wmp.classTools.frame;

import com.wmp.Main;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.classTools.CTComponent.CTPanel;
import com.wmp.classTools.extraPanel.attendance.panel.ATPanel;
import com.wmp.classTools.extraPanel.duty.panel.DPanel;
import com.wmp.classTools.extraPanel.reminderBir.panel.BRPanel;
import com.wmp.classTools.importPanel.eastereggtext.ETPanel;
import com.wmp.classTools.importPanel.finalPanel.FinalPanel;
import com.wmp.classTools.importPanel.timeView.TimeViewPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.wmp.Main.*;

public class MainWindow extends JDialog {
    private final JPanel centerPane = new JPanel(); // 用于放置中间组件的面板

    public static final ArrayList<CTPanel> allPanelList = new ArrayList<>();
    public static final ArrayList<CTPanel> showPanelList = new ArrayList<>();

    public MainWindow(String path) throws IOException {

        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setTitle(Main.appName + "-V" + Main.version);
        //删除边框
        this.setUndecorated(true);

        this.setLayout(new BorderLayout());

        // 创建中心区域的滚动面板
        centerPane.setBackground(CTColor.backColor);
        centerPane.setLayout(new GridBagLayout());

        JScrollPane centerScrollPane = new JScrollPane(centerPane);
        centerScrollPane.setBorder(null);
        centerScrollPane.getViewport().setOpaque(false);
        // 设置滚动面板的水平滚动条策略
        centerScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        File DutyListPath = new File( path + "Duty\\DutyList.txt");
        File indexPath = new File(path + "Duty\\index.txt");
        File AllStuPath = new File(path + "Att\\AllStu.txt");
        File LeaveListPath = new File(path + "Att\\LeaveList.txt");
        File birthdayPath = new File(path + "birthday.json");

        //添加组件
        TimeViewPanel timeViewPanel = new TimeViewPanel();
        ETPanel eEPanel = new ETPanel();
        FinalPanel finalPanel = new FinalPanel();

        // 创建南方面板用于放置ETPanel和FinalPanel
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(CTColor.backColor);
        southPanel.add(eEPanel, BorderLayout.CENTER);
        southPanel.add(finalPanel, BorderLayout.SOUTH);

        DPanel dPanel = new DPanel(DutyListPath, indexPath);
        allPanelList.add(dPanel);

        ATPanel aTPanel = new ATPanel(AllStuPath, LeaveListPath);
        allPanelList.add(aTPanel);

        BRPanel brPanel = new BRPanel(birthdayPath);
        allPanelList.add(brPanel);

        allPanelList.add(timeViewPanel); // 添加到列表中以便统一管理
        allPanelList.add(eEPanel);
        allPanelList.add(finalPanel);

        if (allArgs.get("screenProduct:view").contains(argsList)) {
            JDialog view = new JDialog();
            view.setLocationRelativeTo(null);
            view.setLayout(null);
            view.setAlwaysOnTop(true);

            view.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });

            view.add(timeViewPanel);

            view.pack();
            view.setVisible(true);

        } else {
            // 使用BorderLayout重新组织组件
            this.add(timeViewPanel, BorderLayout.NORTH);
            this.add(centerScrollPane, BorderLayout.CENTER);
            this.add(southPanel, BorderLayout.SOUTH);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.WEST;
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1; // 添加水平权重
            //gbc.fill = GridBagConstraints.HORIZONTAL; // 水平填充
            gbc.insets = new Insets(0, 0, 0, 0); // 确保没有额外的边距

            allPanelList.forEach(ctPanel -> {
                ctPanel.setBackground(CTColor.backColor);

                // 只将不在南北区域的面板添加到中心区域
                if (ctPanel != timeViewPanel && ctPanel != eEPanel && ctPanel != finalPanel) {
                    centerPane.add(ctPanel, gbc);
                    gbc.gridy++;
                }

                if (disPanelList.contains(ctPanel.getID())) {
                    ctPanel.removeAll();
                    ctPanel.revalidate();
                    ctPanel.repaint();
                }
            });

            showPanelList.forEach(ctPanel -> {
                ctPanel.setBackground(CTColor.backColor);
                centerPane.add(ctPanel, gbc);
                gbc.gridy++;
            });

            initFrame();

            if (allArgs.get("screenProduct:show").contains(argsList)) {
                CTColor.setScreenProductColor();
                showPanelList.forEach(ctPanel -> {
                    try {
                        ctPanel.refresh();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

                new ScreenProduct();
            } else {
                this.setVisible(true);
                //刷新
                Thread repaint = new Thread(() -> {

                    while (true) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //刷新窗口大小

                        showPanelList.forEach(ctPanel -> {
                            ctPanel.setBackground(CTColor.backColor);
                        });

                        // 重新验证中心面板以更新布局
                        centerPane.revalidate();
                        
                        this.pack();
                        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth(), 0);
                        this.repaint();
                    }
                });
                repaint.start();
            }
        }
    }

    private void initFrame() {
        //设置屏幕大小
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        this.setForeground(CTColor.backColor);
        this.setIconImage(new ImageIcon(getClass().getResource(Main.iconPath)).getImage());
        this.pack();
    }
}