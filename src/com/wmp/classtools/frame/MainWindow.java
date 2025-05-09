package com.wmp.classTools.frame;

import com.wmp.PublicTools.CTColor;
import com.wmp.Main;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.classTools.CTComponent.CTPanel;
import com.wmp.classTools.importPanel.eastereggtext.EEPanel;
import com.wmp.classTools.importPanel.finalPanel.FinalPanel;
import com.wmp.classTools.importPanel.timeView.TimeViewPanel;
import com.wmp.classTools.extraPanel.attendance.panel.ATPanel;
import com.wmp.classTools.extraPanel.duty.panel.DPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static com.wmp.Main.allArgs;
import static com.wmp.Main.list;

public class MainWindow extends JDialog {
    private final Container contentPane = this.getContentPane();

    public MainWindow(String path) throws IOException {

        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setTitle("ClassTools-V" + Main.version);
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

        AtomicInteger mixY = new AtomicInteger();

        ArrayList<CTPanel> showPanelList = new ArrayList<>();
        //添加组件
        TimeViewPanel timeViewPanel = new TimeViewPanel(mixY.get());
        showPanelList.add(timeViewPanel);



        if (allArgs.get("screenProduct:view").contains(list)){
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
            EEPanel eEPanel = new EEPanel(mixY.get());
            showPanelList.add(eEPanel);

            DPanel dPanel = new DPanel(mixY.get(),DutyListPath,indexPath);
            showPanelList.add(dPanel);

            ATPanel aTPanel = new ATPanel(mixY.get(),AllStuPath,LeaveListPath);
            showPanelList.add(aTPanel);

            FinalPanel finalPanel = new FinalPanel(mixY.get(), AllStuPath, LeaveListPath, DutyListPath, indexPath,
                    showPanelList);
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

                    //刷新窗口大小
                    AtomicInteger temp = new AtomicInteger(0);
                    AtomicInteger finalMixY = new AtomicInteger(mixY.get());
                    AtomicInteger finalMixX = new AtomicInteger(250);

                    showPanelList.forEach(ctPanel -> {
                        temp.set(temp.get() + ctPanel.getHeight());

                    });
                    temp.set(temp.get() + finalPanel.getHeight());

                    if (temp.get() != finalMixY.get()) {


                        showPanelList.forEach(ctPanel -> {
                            ctPanel.setLocation(0, finalMixY.get());
                            ctPanel.setBackground(CTColor.backColor);

                            finalMixY.set(finalMixY.get() + ctPanel.getHeight());
                            finalMixX.set(Math.max(finalMixX.get(), ctPanel.getWidth()));
                        });
                        this.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width - finalMixX.get(), 0, finalMixX.get(), finalMixY.get() + 5);

                    }

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


        this.setIconImage(new ImageIcon(getClass().getResource("/image/icon.png")).getImage());
        this.setSize(250, mixY + 5);
        this.setLocation(screenWidth - this.getWidth(), 0);


    }

}
