package com.wmp.classtools.frame;

import com.wmp.Main;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class AboutDialog extends JDialog {

    public AboutDialog() {
        this.setTitle("关于");
        this.setSize(300, 400);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setAlwaysOnTop(true);
        this.setModal(true);
        this.setResizable(false);
        //this.setUndecorated(true);// 去掉边框
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        ImageIcon defaultIcon = new ImageIcon( getClass().getResource("/image/icon.png"));
        defaultIcon.setImage(defaultIcon.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));


        JLabel icon = new JLabel(defaultIcon);
        icon.setBounds(10, 10, 100, 100);

        JLabel title = new JLabel("程序名: ClassTools");
        title.setBounds(120, 10, 200, 20);

        //将图标显示在文字上方
        JLabel version = new JLabel("版本: " + Main.version);
        version.setBounds(120, 40, 200, 20);

        JLabel author = new JLabel("作者: WMP");
        author.setBounds(120, 70, 200, 20);

        this.add(icon);
        this.add(title);
        this.add(version);
        this.add(author);

        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JMenu menu = new JMenu("转到");

        JMenuItem github = new JMenuItem("Github");
        github.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new java.net.URI("https://github.com/wmp666/ClassTools"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        JMenuItem appPath = new JMenuItem("程序路径");
        appPath.addActionListener(e -> {
            try {
                Desktop.getDesktop().open(new File(System.getProperty("user.dir")));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        menu.add(appPath);
        menu.add(github);

        menuBar.add(menu);
        //this.setVisible(true);
    }
}
