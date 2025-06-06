package com.wmp.classTools.test;

import com.wmp.Main;
import com.wmp.classTools.extraPanel.attendance.panel.ATPanel;
import com.wmp.classTools.extraPanel.duty.panel.DPanel;
import com.wmp.classTools.frame.MainWindow;
import com.wmp.classTools.importPanel.eastereggtext.ETPanel;
import com.wmp.classTools.importPanel.finalPanel.FinalPanel;
import com.wmp.classTools.importPanel.timeView.TimeViewPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CTPanelTest {
    public static void main(String[] args) throws IOException {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("""
                    请输入测试窗口的序号:
                    0.exit
                    1.ATPanel
                    2.DPanel
                    3.ETPanel
                    4.TimeViewPanel
                    5.FinalPanel""");
            switch (scanner.nextInt()) {
                case 1 -> {
                    new ATPanelTest();
                }
                case 2 -> {
                    new DPanelTest();
                }
                case 3 -> {
                    new ETPanelTest();
                }
                case 4 -> {
                    new TimeViewPanelTest();
                }
                case 5 -> {
                    new FinalPanelTest();
                }
                case 0 -> {
                    return;
                }
            }
        }
    }

    static class ATPanelTest extends JFrame {

        public ATPanelTest() throws IOException {
            File AllStuPath = new File(Main.DATA_PATH + "Att\\AllStu.txt");
            File LeaveListPath = new File(Main.DATA_PATH + "Att\\LeaveList.txt");

            ATPanel atPanel = new ATPanel(AllStuPath, LeaveListPath);

            this.add(atPanel);
            this.pack();
            this.setVisible(true);


        }

    }

    static class DPanelTest extends JFrame {

        public DPanelTest() throws IOException {
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setLocationRelativeTo(null);

            File DutyListPath = new File(Main.DATA_PATH + "Duty\\DutyList.txt");
            File indexPath = new File(Main.DATA_PATH + "Duty\\index.txt");

            DPanel dPanel = new DPanel(DutyListPath, indexPath);
            this.add(dPanel);
            this.pack();
            this.setVisible(true);

            new Thread(() -> {
                while (true) {
                    dPanel.repaint();
                    this.pack();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }

    }

    static class ETPanelTest extends JFrame {
        public ETPanelTest() throws HeadlessException {
            ETPanel etPanel = new ETPanel();

            this.add(etPanel);
            this.pack();
            this.setVisible(true);

            new Thread(() -> {
                while (true) {
                    etPanel.repaint();
                    this.pack();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }
    }

    static class TimeViewPanelTest extends JFrame {
        public TimeViewPanelTest() throws IOException {
            TimeViewPanel timeViewPanel = new TimeViewPanel();

            this.add(timeViewPanel);
            this.pack();
            this.setVisible(true);


        }
    }

    static class FinalPanelTest extends JFrame {
        public FinalPanelTest() throws IOException {
            new MainWindow(Main.DATA_PATH);

            FinalPanel finalPanel = new FinalPanel(MainWindow.showPanelList);

            this.add(finalPanel);
            this.pack();
            this.setVisible(true);

            new Thread(() -> {
                while (true) {
                    finalPanel.repaint();
                    this.pack();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }
    }
}
