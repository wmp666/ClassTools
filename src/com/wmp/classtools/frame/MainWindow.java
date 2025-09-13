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
    private final Container contentPane = this.getContentPane();

    public static final ArrayList<CTPanel> allPanelList = new ArrayList<>();
    public static final ArrayList<CTPanel> showPanelList = new ArrayList<>();

    public MainWindow(String path) throws IOException {

        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setTitle(Main.appName + "-V" + Main.version);
        //删除边框
        this.setUndecorated(true);

        contentPane.setBackground(CTColor.backColor);
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;

        File DutyListPath = new File( path + "Duty\\DutyList.txt");
        File indexPath = new File(path + "Duty\\index.txt");
        File AllStuPath = new File(path + "Att\\AllStu.txt");
        File LeaveListPath = new File(path + "Att\\LeaveList.txt");
        File birthdayPath = new File(path + "birthday.json");



        //添加组件
        TimeViewPanel timeViewPanel = new TimeViewPanel();
        allPanelList.add(timeViewPanel);

        DPanel dPanel = new DPanel(DutyListPath, indexPath);
        allPanelList.add(dPanel);

        ATPanel aTPanel = new ATPanel(AllStuPath, LeaveListPath);
        allPanelList.add(aTPanel);

        BRPanel brPanel = new BRPanel(birthdayPath);
        allPanelList.add(brPanel);

        ETPanel eEPanel = new ETPanel();
        allPanelList.add(eEPanel);

        FinalPanel finalPanel = new FinalPanel();
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


            allPanelList.forEach(ctPanel -> {
                ctPanel.setBackground(CTColor.backColor);

                gbc.gridy++;
                contentPane.add(ctPanel, gbc);

                if (disPanelList.contains(ctPanel.getID())) {
                    ctPanel.removeAll();
                    ctPanel.revalidate();
                    ctPanel.repaint();
                }
            });

            showPanelList.forEach(ctPanel -> {
                ctPanel.setBackground(CTColor.backColor);

                gbc.gridy++;
                contentPane.add(ctPanel, gbc);
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
                        //this.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width - finalMaxX.get(), 0, finalMaxX.get(), finalMaxY.get() + 5);
                        this.pack();
                        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth(), 0);


                        contentPane.setBackground(CTColor.backColor);

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
