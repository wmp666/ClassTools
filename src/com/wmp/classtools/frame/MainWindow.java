package com.wmp.classtools.frame;

import com.wmp.CTColor;
import com.wmp.Main;
import com.wmp.classtools.CTComponent.CTButton;
import com.wmp.panel.attendance.panel.ATPanel;
import com.wmp.panel.duty.panel.DPanel;
import com.wmp.classtools.infSet.InfSetDialog;
import com.wmp.classtools.panel.timeView.TimeViewPanel;
import com.wmp.io.IOStreamForInf;
import com.wmp.tools.GetNewerVersion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class MainWindow extends JDialog {
    private final Container contentPane = this.getContentPane();

    public MainWindow(String path) throws IOException {

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
        new IOStreamForInf(DutyListPath);
        new IOStreamForInf(indexPath);
        new IOStreamForInf(AllStuPath);

        int mixY = 0;

        //添加组件
        TimeViewPanel timeViewPanel = new TimeViewPanel(mixY);
        timeViewPanel.setLocation(0,mixY);
        timeViewPanel.setBackground(CTColor.backColor);
        contentPane.add(timeViewPanel);
        mixY = timeViewPanel.getNextPanelY();

        //时间刷新
        /*Thread timeThread = new Thread(() -> {

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
        });
        timeThread.start();*/


        //System.out.println("mixY = " + mixY);

        DPanel dPanel = new DPanel(mixY,DutyListPath,indexPath);
        dPanel.setLocation(0,mixY);
        dPanel.setBackground( CTColor.backColor);
        mixY = dPanel.getNextPanelY();
        contentPane.add(dPanel);

        ATPanel aTPanel = new ATPanel(mixY,AllStuPath,LeaveListPath);
        aTPanel.setLocation(0,mixY);
        aTPanel.setBackground(CTColor.backColor);
        mixY = aTPanel.getNextPanelY();
        contentPane.add(aTPanel);

        JPanel finalPanel = new JPanel();
        finalPanel.setLayout(new GridLayout(1, 6));
        finalPanel.setBackground(CTColor.backColor);
        //finalPanel.setLayout(null);
        finalPanel.setLocation(0, mixY);
        finalPanel.setSize(250, 39);

        CTButton settings = new CTButton("设置数据", getClass().getResource("/image/settings_0.png"),
                getClass().getResource("/image/settings_1.png"),30,() -> {

            try {
                new InfSetDialog(this, AllStuPath, LeaveListPath, DutyListPath, indexPath, () -> {
                    try {
                        dPanel.refresh();
                        aTPanel.refresh(); // 自定义刷新方法
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }).setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        //settings.setLocation(210, 0);
        finalPanel.add(settings);

        CTButton about = new CTButton("软件信息", getClass().getResource("/image/about_0.png"),
                getClass().getResource("/image/about_1.png"),30,() -> {
            new AboutDialog().setVisible(true);
        });

        //about.setLocation(210, 0);
        about.setToolTipText("版本：" + Main.version);
        finalPanel.add(about);

        CTButton update = new CTButton("检查更新", getClass().getResource("/image/update_0.png"),
                getClass().getResource("/image/update_1.png"),30,() -> {
            GetNewerVersion.checkForUpdate(null, null);

        });
        update.setToolTipText("获取更新");
        finalPanel.add(update);

        CTButton exit = new CTButton("关闭", getClass().getResource("/image/exit_0.png"),
                getClass().getResource("/image/exit_1.png"),30,() -> {
            int i = JOptionPane.showConfirmDialog(null, "确认退出?", "询问", JOptionPane.YES_NO_OPTION);
            if (i == JOptionPane.YES_OPTION){
                System.exit(0);
            }

        });

        //exit.setLocation(210, 0);
        finalPanel.add(exit);


        mixY = finalPanel.getHeight() + mixY;
        contentPane.add(finalPanel);

        //添加至系统托盘
        //initTray();

        initFrame(mixY);

        //刷新
        AtomicInteger finalMixY = new AtomicInteger(mixY);
        Thread repaint = new Thread(() -> {

            while (true) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //刷新窗口大小
                int temp = timeViewPanel.getHeight() + dPanel.getHeight() + aTPanel.getHeight() + finalPanel.getHeight();
                if (temp != finalMixY.get()) {
                    this.setSize(250, temp + 5);
                    timeViewPanel.setLocation(0, 0);
                    dPanel.setLocation(0, timeViewPanel.getHeight());
                    aTPanel.setLocation(0, timeViewPanel.getHeight() + dPanel.getHeight());
                    finalPanel.setLocation(0, temp - finalPanel.getHeight());
                    finalMixY.set(temp);
                }

                this.repaint();
            }
        });
        repaint.start();
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

    private void initTray() {
    if (!SystemTray.isSupported()) return;

    // 创建托盘图标
    Image trayImage = new ImageIcon(getClass().getResource("/image/icon.png")).getImage();
    TrayIcon trayIcon = new TrayIcon(trayImage, "ClassTools");
    trayIcon.setImageAutoSize(true);

        JWindow window = new JWindow();

    JButton exitButton = new JButton("关闭");
    exitButton.setSize(80, 30);
    exitButton.setLocation(0, 0);
    exitButton.setFont(new Font("微软雅黑", Font.BOLD, 20));
    exitButton.setBackground(Color.WHITE);
    exitButton.setBorderPainted(false);

    exitButton.addActionListener(e1 ->{
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


            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/image/hide_0.png"));
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
            JButton hideButton = new JButton(imageIcon);

            hideButton.setBorderPainted(false);
            hideButton.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    window.setVisible(false);
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    ImageIcon imageIcon = new ImageIcon(getClass().getResource("/image/hide_1.png"));
                    imageIcon.setImage(imageIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));

                    hideButton.setIcon(imageIcon);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    ImageIcon imageIcon = new ImageIcon(getClass().getResource("/image/hide_0.png"));
                    imageIcon.setImage(imageIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));

                    hideButton.setIcon(imageIcon);
                }
            });
            hideButton.setBackground(Color.WHITE);
            hideButton.setBounds(80 - 25, 55, 20, 20);
            window.getContentPane().add(hideButton);

            window.getContentPane().add(exitButton);
            //获取鼠标坐标
            int x = e.getX();
            int y = e.getY();

            window.setLayout(null);
            window.getContentPane().setBackground(Color.WHITE);
            window.setAlwaysOnTop(true);
            //将大小设置为popup的大小
            window.setSize(80 , 80);
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
            System.out.println("鼠标移入");
        }

        @Override
        public void mouseExited(MouseEvent e) {
            System.out.println("鼠标移出");
        }
    });

    try {
        SystemTray.getSystemTray().add(trayIcon);
    } catch (AWTException e) {
        System.err.println("无法添加系统托盘图标");
    }
}



}
