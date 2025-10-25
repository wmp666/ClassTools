package com.wmp.classTools.frame;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTBorderFactory;
import com.wmp.classTools.CTComponent.CTPanel.CTViewPanel;
import com.wmp.classTools.extraPanel.attendance.panel.ATPanel;
import com.wmp.classTools.extraPanel.classForm.panel.ClassFormPanel;
import com.wmp.classTools.extraPanel.countdown.panel.CountDownPanel;
import com.wmp.classTools.extraPanel.duty.panel.DPanel;
import com.wmp.classTools.extraPanel.reminderBir.panel.BRPanel;
import com.wmp.classTools.importPanel.eastereggtext.ETPanel;
import com.wmp.classTools.importPanel.finalPanel.FinalPanel;
import com.wmp.classTools.importPanel.timeView.OtherTimeThingPanel;
import com.wmp.classTools.importPanel.timeView.TimeViewPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.TreeMap;

import static com.wmp.Main.allArgs;
import static com.wmp.Main.argsList;

public class MainWindow extends JDialog {
    //private final JPanel centerPane = new JPanel(); // 用于放置中间组件的面板

    public static final ArrayList<CTViewPanel> allPanelList = new ArrayList<>();
    //public static final ArrayList<CTViewPanel> showPanelList = new ArrayList<>();

    private static final TreeMap<String, String[]> panelLocationMap = new TreeMap<>();
    private static final TreeMap<String, CTViewPanel[]> panelMap = new TreeMap<>();

    private static JDialog mainWindow = new JDialog();

    public MainWindow(String path) throws IOException {

        mainWindow = this;

        panelLocationMap.put("上方", new String[]{"TimeViewPanel", "OtherTimeThingPanel"});
        panelLocationMap.put("下方", new String[]{"ETPanel", "FinalPanel"});


        File DutyListPath = new File(path + "Duty\\DutyList.txt");
        File indexPath = new File(path + "Duty\\index.txt");
        File AllStuPath = new File(path + "Att\\AllStu.txt");
        File LeaveListPath = new File(path + "Att\\LeaveList.txt");
        File birthdayPath = new File(path + "birthday.json");

        //添加组件
        TimeViewPanel timeViewPanel = new TimeViewPanel();
        OtherTimeThingPanel otherTimeThingPanel = new OtherTimeThingPanel();
        ETPanel eEPanel = new ETPanel();
        FinalPanel finalPanel = new FinalPanel();

        DPanel dPanel = new DPanel(DutyListPath, indexPath);
        allPanelList.add(dPanel);

        ATPanel aTPanel = new ATPanel(AllStuPath, LeaveListPath);
        allPanelList.add(aTPanel);

        CountDownPanel countDownPanel = new CountDownPanel();
        allPanelList.add(countDownPanel);

        ClassFormPanel classFormPanel = new ClassFormPanel();
        allPanelList.add(classFormPanel);

        BRPanel brPanel = new BRPanel(birthdayPath);
        allPanelList.add(brPanel);

        allPanelList.add(timeViewPanel); // 添加到列表中以便统一管理
        allPanelList.add(otherTimeThingPanel);
        allPanelList.add(eEPanel);
        allPanelList.add(finalPanel);

        if (allArgs.get("screenProduct:view").contains(argsList)) {
            JDialog view = new JDialog();
            view.setLocationRelativeTo(null);
            view.setLayout(new BorderLayout());
            view.setAlwaysOnTop(true);

            view.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });

            view.add(timeViewPanel, BorderLayout.CENTER);

            view.pack();
            view.setVisible(true);

        } else {
            initFrame();

            if (allArgs.get("screenProduct:show").contains(argsList)) {
                CTColor.setScreenProductColor();
                allPanelList.forEach(ctPanel -> {
                    try {
                        ctPanel.refresh();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

                new ScreenProduct();
            } else {
                refreshPanel();
                // 确保窗口可见
                this.setVisible(true);
                this.toFront();

                //刷新
                Timer repaint = new Timer(500, e -> {

                    allPanelList.forEach(ctPanel -> {
                        ctPanel.setOpaque(false);

                        ctPanel.revalidate();
                        ctPanel.repaint();
                    });


                    Dimension size = this.getPreferredSize();

                    if (size.height >= Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 4 / 5)
                        this.setSize(new Dimension( size.width, (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 4 / 5)));
                    else this.setSize(new Dimension( size.width, size.height));
                    this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width - size.width - 5, 5);

                    //this.setLocation(0,0);
                    this.getContentPane().setBackground(CTColor.backColor);
                    this.setShape(new RoundRectangle2D.Double(0, 0, this.getWidth(), this.getHeight(), 20, 20));

                    this.repaint();
                });
                repaint.start();

                //刷新数据
                Timer strongRepaint = new Timer(60 * 1000, e -> {
                    refreshPanel();
                });
                strongRepaint.start();
            }
        }
    }


    public static void refreshPanel() {
        try {
            CTInfo.init();

            //初始化边框
            CTBorderFactory.BASIC_LINE_BORDER = BorderFactory.createLineBorder(new Color(200, 200, 200), (int) (2 * CTInfo.dpi));
            CTBorderFactory.FOCUS_GAINTED_BORDER = BorderFactory.createLineBorder(new Color(112, 112, 112), (int) (2 * CTInfo.dpi));

            panelMap.clear();
            panelMap.put("上方", new CTViewPanel[]{new CTViewPanel() {
                @Override
                public void refresh() throws IOException {

                }
            }});
            panelMap.put("下方", new CTViewPanel[]{new CTViewPanel() {
                @Override
                public void refresh() throws IOException {

                }
            }});
            panelMap.put("中间", new CTViewPanel[]{new CTViewPanel() {
                @Override
                public void refresh() throws IOException {

                }
            }});

            allPanelList.forEach(panel -> {

                if (!CTInfo.disPanelList.contains(panel.getID())) {
                    //showPanelList.add(panel);

                    if (Arrays.asList(panelLocationMap.get("上方")).contains(panel.getID())) {
                        java.util.List<CTViewPanel> temp = new java.util.ArrayList<>(Arrays.asList(panelMap.get("上方")));
                        temp.add(panel);
                        panelMap.put("上方", temp.toArray(new CTViewPanel[0]));
                    } else if (Arrays.asList(panelLocationMap.get("下方")).contains(panel.getID())) {
                        java.util.List<CTViewPanel> temp = new java.util.ArrayList<>(Arrays.asList(panelMap.get("下方")));
                        temp.add(panel);
                        panelMap.put("下方", temp.toArray(new CTViewPanel[0]));
                    } else {
                        // 使用 new ArrayList 包装，创建可变列表
                        java.util.List<CTViewPanel> temp = new java.util.ArrayList<>(Arrays.asList(panelMap.get("中间")));
                        temp.add(panel);
                        panelMap.put("中间", temp.toArray(new CTViewPanel[0]));
                    }
                }
            });

            if (!allArgs.get("screenProduct:show").contains(argsList)) {//showPanelList.clear();


                JPanel northPanel = new JPanel();
                JPanel southPanel = new JPanel();
                JPanel centerPanel = new JPanel();

                northPanel.setLayout(new GridBagLayout());
                southPanel.setLayout(new GridBagLayout());
                centerPanel.setLayout(new GridBagLayout());

                northPanel.setOpaque(false);
                southPanel.setOpaque(false);
                centerPanel.setOpaque(false);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.anchor = GridBagConstraints.WEST;
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.weightx = 1; // 添加水平权重
                //gbc.fill = GridBagConstraints.HORIZONTAL; // 水平填充
                gbc.insets = new Insets(0, 0, 0, 0); // 确保没有额外的边距

                TreeMap<String, Integer> countMap = new TreeMap<>();

                //1.将要显示的组件添加到显示列表
                panelMap.forEach((key, value) -> {
                    for (CTViewPanel panel : value) {
                        countMap.put(key, countMap.getOrDefault(key, 0) + 1);
                        gbc.gridy = countMap.get(key);
                        if (key.equals("上方")) {
                            northPanel.add(panel, gbc);
                        } else if (key.equals("下方")) {
                            southPanel.add(panel, gbc);
                        } else {
                            centerPanel.add(panel, gbc);
                        }
                    }
                });

                JScrollPane scrollPane = new JScrollPane(centerPanel);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                scrollPane.getViewport().setOpaque(false);
                scrollPane.setOpaque(false);
                scrollPane.setBorder(BorderFactory.createEmptyBorder());


                // 在添加新组件前移除所有现有组件
                mainWindow.getContentPane().removeAll();
                mainWindow.add(northPanel, BorderLayout.NORTH);
                mainWindow.add(southPanel, BorderLayout.SOUTH);
                mainWindow.add(scrollPane, BorderLayout.CENTER);

                mainWindow.revalidate();
                mainWindow.repaint();

                // 确保窗口可见并置于前端
                mainWindow.setVisible(true);
                //mainWindow.toFront();}
            }

            allPanelList.forEach(panel -> {
                try {
                    panel.refresh();
                } catch (IOException e) {
                    Log.err.print(MainWindow.class, "刷新失败", e);
                }
            });
        } catch (Exception e) {
            Log.err.print(MainWindow.class, "刷新失败", e);
        }
    }

    private void initFrame() {
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setTitle(CTInfo.appName + "-V" + CTInfo.version);
        //删除边框
        this.setUndecorated(true);

        this.setLayout(new BorderLayout());
        //设置屏幕大小
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        this.getContentPane().setForeground(CTColor.backColor);
        try {
            this.setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource(CTInfo.iconPath))).getImage());
        } catch (Exception e) {
            Log.err.print(MainWindow.class, "图标加载失败", e);
        }
        
        // 确保窗口有最大尺寸
        //this.setMaximumSize(new Dimension(screenSize.width, screenSize.height * 4/5));
        this.pack();
    }
}