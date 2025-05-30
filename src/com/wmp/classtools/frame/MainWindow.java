package com.wmp.classTools.frame;

import com.wmp.Main;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.classTools.CTComponent.CTPanel;
import com.wmp.classTools.extraPanel.attendance.panel.ATPanel;
import com.wmp.classTools.extraPanel.duty.panel.DPanel;
import com.wmp.classTools.importPanel.eastereggtext.ETPanel;
import com.wmp.classTools.importPanel.finalPanel.FinalPanel;
import com.wmp.classTools.importPanel.timeView.TimeViewPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static com.wmp.Main.allArgs;
import static com.wmp.Main.argsList;

public class MainWindow extends JDialog {
    private final Container contentPane = this.getContentPane();

    public static final ArrayList<CTPanel> showPanelList = new ArrayList<>();

    public MainWindow(String path) throws IOException {

        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setTitle(Main.appName + "-V" + Main.version);
        //删除边框
        this.setUndecorated(true);

        contentPane.setBackground(CTColor.backColor);
        contentPane.setLayout(null);

        File DutyListPath = new File( path + "Duty\\DutyList.txt");
        File indexPath = new File(path + "Duty\\index.txt");
        File AllStuPath = new File(path + "Att\\AllStu.txt");
        File LeaveListPath = new File(path + "Att\\LeaveList.txt");


        //初始化
        new IOForInfo(DutyListPath);
        new IOForInfo(indexPath);
        new IOForInfo(AllStuPath);




        //添加组件
        TimeViewPanel timeViewPanel = new TimeViewPanel(0);
        showPanelList.add(timeViewPanel);


        if (allArgs.get("screenProduct:view").contains(argsList)) {
            JDialog view = new JDialog();
            view.setSize(timeViewPanel.getWidth() + 20, timeViewPanel.getHeight() + 40);
            view.setLocationRelativeTo(null);
            view.setLayout(null);

            view.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {

                }

                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }

                @Override
                public void windowClosed(WindowEvent e) {
                    System.exit(0);
                }

                @Override
                public void windowIconified(WindowEvent e) {

                }

                @Override
                public void windowDeiconified(WindowEvent e) {

                }

                @Override
                public void windowActivated(WindowEvent e) {

                }

                @Override
                public void windowDeactivated(WindowEvent e) {

                }
            });

            view.add(timeViewPanel);

            view.setVisible(true);

        }else {
            AtomicInteger mixY = new AtomicInteger();

            DPanel dPanel = new DPanel(mixY.get(),DutyListPath,indexPath);
            showPanelList.add(dPanel);

            ATPanel aTPanel = new ATPanel(mixY.get(),AllStuPath,LeaveListPath);
            showPanelList.add(aTPanel);

            ETPanel eEPanel = new ETPanel(mixY.get());
            showPanelList.add(eEPanel);

            FinalPanel finalPanel = new FinalPanel(mixY.get(), showPanelList);
            showPanelList.add(finalPanel);

            showPanelList.forEach(ctPanel -> {
                ctPanel.setLocation(0, mixY.get());
                ctPanel.setBackground(CTColor.backColor);
                mixY.set(ctPanel.getNextPanelY());
                contentPane.add(ctPanel);
            });

            contentPane.add(finalPanel);

            initFrame(mixY.get());

            this.setVisible(true);
            //刷新

            Thread repaint = new Thread(() -> {

                while (true) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    finalPanel.setSize(250, finalPanel.getHeight());
                    //刷新窗口大小
                    AtomicInteger temp = new AtomicInteger(0);
                    AtomicInteger finalMaxY = new AtomicInteger(mixY.get());
                    AtomicInteger finalMaxX = new AtomicInteger(250);

                    showPanelList.forEach(ctPanel -> {
                        temp.set(temp.get() + ctPanel.getHeight());

                    });
                    temp.set(temp.get() + finalPanel.getHeight());

                    if (temp.get() != finalMaxY.get()) {


                        showPanelList.forEach(ctPanel -> {
                            ctPanel.setLocation(0, finalMaxY.get());
                            ctPanel.setBackground(CTColor.backColor);

                            finalMaxY.set(finalMaxY.get() + ctPanel.getHeight());
                            finalMaxX.set(Math.max(finalMaxX.get(), ctPanel.getWidth()));
                        });
                        this.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width - finalMaxX.get(), 0, finalMaxX.get(), finalMaxY.get() + 5);

                        finalPanel.setSize(finalMaxX.get(), finalPanel.getHeight());
                    }

                    contentPane.setBackground(CTColor.backColor);

                    this.repaint();
                }
            });
            repaint.start();
        }
    }

    private void initFrame(int mixY) {
        //设置屏幕大小
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        this.setForeground(CTColor.backColor);
        this.setIconImage(new ImageIcon(getClass().getResource(Main.iconPath)).getImage());
        this.setSize(250, mixY + 5);
        this.setLocation(screenWidth - this.getWidth(), 0);


    }

}
