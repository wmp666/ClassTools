package com.wmp.classtools.frame;

import com.wmp.CTColor;
import com.wmp.Main;
import com.wmp.classtools.CTComponent.CTButton;
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

    private JPanel view = new JPanel();


    public AboutDialog() {

        view.setLayout(null);

        this.setTitle("关于");
        this.setSize(300, 400);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setModal(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.getContentPane().setBackground(CTColor.backColor);

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

        this.view = new JPanel();
        view.setBounds(0, 120, 300, 120);
        view.setBackground(CTColor.backColor);
        view.setLayout(null);

        CTButton getNew = new CTButton(CTButton.ButtonText, "检查更新",
                getClass().getResource("/image/update_0.png"),
                getClass().getResource("/image/update_1.png"), 150, 35,
                () -> GetNewerVersion.checkForUpdate(this, view));
        getNew.setBackground(CTColor.backColor);
        getNew.setLocation(60, 240);

        this.add(this.view);
        this.add(getNew);
        this.add(icon);
        this.add(title);
        this.add(version);
        this.add(author);

        initMenuBar();

        GetNewerVersion.checkForUpdate(this, this.view);

    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JMenu menu = new JMenu("转到");

        JMenu chat = new JMenu("社交");

        JMenuItem weChat = new JMenuItem("微信");
        weChat.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "微信: w13607088913"));

        JMenuItem qq = new JMenuItem("QQ");
        qq.addActionListener(e ->
            JOptionPane.showMessageDialog(this, "QQ: 2134868121"));

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

        JMenu downloadMenu = new JMenu("下载");

        //获取源代码
        JMenuItem getSource = new JMenuItem("获取源代码");
        getSource.addActionListener(e -> GetNewerVersion.getSource(this, this.view));

        JMenuItem checkUpdate = new JMenuItem("检查更新");
        checkUpdate.addActionListener(e -> GetNewerVersion.checkForUpdate(this, this.view));

        downloadMenu.add(getSource);
        downloadMenu.add(checkUpdate);

        menuBar.add(downloadMenu);

        JMenu helpMenu = new JMenu("帮助");

        JMenuItem helpDoc = new JMenuItem("帮助文档");
        helpDoc.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "正在加急制作...", "帮助文档(前面的区域，以后再来探索吧)", JOptionPane.INFORMATION_MESSAGE);
        });

        JMenuItem easterEgg = new JMenuItem("■■");
        easterEgg.addActionListener(e ->
            EasterEgg.getPin());

        helpMenu.add(helpDoc);
        helpMenu.add(easterEgg);

        menuBar.add(helpMenu);
    }


}
