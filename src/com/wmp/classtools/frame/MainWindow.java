package com.wmp.classtools.frame;

import com.wmp.panel.attendance.panel.ATPanel;
import com.wmp.panel.duty.panel.DPanel;
import com.wmp.classtools.infSet.InfSetDialog;
import com.wmp.panel.timeView.panel.TimeViewPanel;
import com.wmp.io.IOStreamForInf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class MainWindow extends JWindow {
    private final Container contentPane = this.getContentPane();

    public MainWindow(String path) throws IOException {

        contentPane.setLayout(null);

        File DutyListPath = new File( path + "Duty\\DutyList.txt");
        File indexPath = new File(path + "Duty\\index.txt");
        File AllStuPath = new File(path + "Att\\AllStu.txt");
        File LeaveListPath = new File(path + "Att\\LeaveList.txt");


        //初始化
        new IOStreamForInf(DutyListPath);
        new IOStreamForInf(indexPath);
        new IOStreamForInf(AllStuPath);

        int mixY = 0;

        //添加组件
        TimeViewPanel timeViewPanel = new TimeViewPanel(mixY);
        timeViewPanel.setLocation(0,mixY);
        contentPane.add(timeViewPanel);
        mixY = timeViewPanel.getMixY();

        //时间刷新
        new Thread(() -> {

            while (true) {
                //获取时间
                Date date = new Date();
                //格式化 11.22 23:05
                DateFormat dateFormat = new SimpleDateFormat("MM.dd HH:mm:ss");
                timeViewPanel.getTimeView().setText(dateFormat.format(date));
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                timeViewPanel.repaint();
            }
        }).start();


        //System.out.println("mixY = " + mixY);

        DPanel dPanel = new DPanel(mixY,DutyListPath,indexPath);
        dPanel.setLocation(0,mixY);
        mixY = dPanel.getMixY();
        contentPane.add(dPanel);

        ATPanel aTPanel = new ATPanel(mixY,AllStuPath,LeaveListPath);
        aTPanel.setLocation(0,mixY + 5);
        mixY = aTPanel.getMixY();
        contentPane.add(aTPanel);



        //添加至系统托盘
        initTray();

        initFrame(mixY);

        //刷新
        AtomicInteger finalMixY = new AtomicInteger(mixY);
        new Thread(() -> {

            while (true) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //刷新窗口大小
                int temp = timeViewPanel.getHeight() + dPanel.getHeight() + aTPanel.getHeight() ;
                if (temp != finalMixY.get()){
                    this.setSize(250, temp + 5);
                    timeViewPanel.setLocation(0, 0);
                    dPanel.setLocation(0, timeViewPanel.getHeight());
                    aTPanel.setLocation(0, timeViewPanel.getHeight() + dPanel.getHeight());

                    finalMixY.set(temp);
                }

                this.repaint();
            }
        }).start();
    }

    private void initFrame(int mixY) {
        //设置屏幕大小
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();


        this.setIconImage(new ImageIcon(getClass().getResource("/image/icon.png")).getImage());
        this.setSize(250, mixY + 5);
        this.setLocation(screenWidth - this.getWidth(), 0);
        this.setVisible(true);
    }

    // 在MainWindow构造函数中添加以下代码（建议放在initFrame之后）
    private void initTray() {
    if (!SystemTray.isSupported()) return;

    // 创建托盘图标
    Image trayImage = new ImageIcon(getClass().getResource("/image/icon.png")).getImage();
    TrayIcon trayIcon = new TrayIcon(trayImage, "ClassTools");
    trayIcon.setImageAutoSize(true);

        JWindow window = new JWindow();

    JButton button = new JButton("关闭");
    button.setSize(80, 30);
    button.setLocation(0, 0);
    button.setFont(new Font("微软雅黑", Font.BOLD, 20));
    button.setBackground(Color.WHITE);

    button.addActionListener(e1 ->{
            window.setVisible(false);

            int result = JOptionPane.showConfirmDialog(
                    null,
                    "确定要退出程序吗？",
                    "退出确认",
                    JOptionPane.YES_NO_OPTION
            );
            if (result == JOptionPane.YES_OPTION) {
                SystemTray.getSystemTray().remove(trayIcon);
                System.exit(0);
            }
        });

    trayIcon.addMouseListener(new MouseListener() {
        // 鼠标点击
        @Override
        public void mouseClicked(MouseEvent e) {
            //System.out.println("鼠标点击");



            window.getContentPane().add(button);
            //获取鼠标坐标
            int x = e.getX();
            int y = e.getY();

            window.setLayout(null);

            window.setAlwaysOnTop(true);
            //将大小设置为popup的大小
            window.setSize(80 , 30);
            window.setLocation(x - window.getWidth(), y - window.getHeight());
            window.setVisible(true);



        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        // 鼠标进入
        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    });

    try {
        SystemTray.getSystemTray().add(trayIcon);
    } catch (AWTException e) {
        System.err.println("无法添加系统托盘图标");
    }
}



}
