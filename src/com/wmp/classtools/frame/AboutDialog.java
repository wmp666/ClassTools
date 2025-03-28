package com.wmp.classtools.frame;

import com.wmp.Main;
import com.wmp.tools.GetNewerVersion;
import com.wmp.tools.SslUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AboutDialog extends JDialog {

    public AboutDialog() {

        GetNewerVersion.checkForUpdate(this);

        this.setTitle("关于");
        this.setSize(300, 400);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        //this.setAlwaysOnTop(true);
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

        initMenuBar();

    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JMenu menu = new JMenu("转到");

        JMenu chat = new JMenu("社交");

        JMenuItem weChat = new JMenuItem("微信");
        weChat.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "微信: w13607088913");
        });

        JMenuItem qq = new JMenuItem("QQ");
        qq.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "QQ: 2134868121");
        });

        JMenuItem bilibili = new JMenuItem("哔哩哔哩");
        bilibili.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://space.bilibili.com/1075810224/"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        chat.add(weChat);
        chat.add(qq);
        chat.add(bilibili);

        JMenu github = new JMenu("Github");

        JMenuItem authorGithub = new JMenuItem("作者");
        authorGithub.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/wmp666"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        JMenuItem repo = new JMenuItem("仓库");
        repo.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/wmp666/ClassTools"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        github.add(authorGithub);
        github.add(repo);

        JMenuItem appPath = new JMenuItem("程序路径");
        appPath.addActionListener(e -> {
            try {
                //打开程序路径
                Desktop.getDesktop().open(new File(System.getProperty("user.dir")));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        menu.add(chat);
        menu.add(github);
        menu.add(appPath);

        menuBar.add(menu);

        // 在现有菜单中添加

        JMenu updateMenu = new JMenu("更新");

        JMenuItem checkUpdate = new JMenuItem("检查更新");
        checkUpdate.addActionListener(e -> GetNewerVersion.checkForUpdate(this));

        updateMenu.add(checkUpdate);

        menuBar.add(updateMenu);
    }


}
