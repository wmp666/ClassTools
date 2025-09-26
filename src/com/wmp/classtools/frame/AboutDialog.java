package com.wmp.classTools.frame;

import com.wmp.Main;
import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.EasterEgg.EasterEgg;
import com.wmp.PublicTools.OpenInExp;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.UITools.GetIcon;
import com.wmp.PublicTools.io.GetPath;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.PublicTools.update.GetNewerVersion;
import com.wmp.classTools.CTComponent.CTIconButton;
import com.wmp.classTools.CTComponent.CTTextButton;
import com.wmp.classTools.frame.tools.help.ShowHelpDoc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class AboutDialog extends JDialog {

    private static final JPanel view = new JPanel();

    static {
        view.setBounds(0, 120, 300, 120);
        view.setBackground(CTColor.backColor);
        view.setLayout(null);
    }

    public AboutDialog() throws MalformedURLException {

        this.setTitle("关于");
        this.setSize(300, 400);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setModal(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.getContentPane().setBackground(CTColor.backColor);


        JLabel icon = new JLabel(GetIcon.getIcon(Main.class.getResource(CTInfo.iconPath), 100, 100));
        icon.setBounds(10, 10, 100, 100);

        JLabel title = new JLabel("程序名: " + CTInfo.appName);
        title.setBounds(120, 10, 200, 20);
        title.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.SMALL));
        title.setForeground(CTColor.textColor);

        //将图标显示在文字上方
        JLabel version = new JLabel("版本: " + CTInfo.version);
        version.setBounds(120, 40, 200, 20);
        version.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.SMALL));
        version.setForeground(CTColor.textColor);

        JLabel author = new JLabel("作者: " + CTInfo.author);
        author.setBounds(120, 70, 200, 20);
        author.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.SMALL));
        author.setForeground(CTColor.textColor);

        JLabel info = new JLabel("<html>"
                + "程序名: " + CTInfo.appName + "<br><br>"
                + "作者: " + CTInfo.author + "<br><br>"
                + "版本: " + CTInfo.version
                + "</html>");
        info.setBounds(120, 10, 200, 100);
        info.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.SMALL));
        info.setForeground(CTColor.textColor);
        info.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int button = e.getButton();
                if (button == MouseEvent.BUTTON3) {
                    JPopupMenu ETPopupMenu = new JPopupMenu();

                    CTTextButton edit = new CTTextButton("编辑");
                    edit.setIcon(GetIcon.getIcon(getClass().getResource("/image/edit.png"), 20, 20));
                    edit.addActionListener(event -> {
                        EasterEgg.errorAction();
                    });

                    ETPopupMenu.add(edit);

                    ETPopupMenu.show(info, e.getX(), e.getY());
                }
            }
        });
        //view = new JPanel();


        CTIconButton getNew = new CTIconButton(CTIconButton.ButtonText, "检查更新",
                "/image/%s/update_0.png",
                "/image/%s/update_1.png", 150, 35,
                () -> GetNewerVersion.checkForUpdate(this, view, true));
        getNew.setBackground(CTColor.backColor);
        getNew.setLocation(60, 240);

        this.add(view);
        this.add(getNew);
        this.add(icon);
        //this.add(title);
        //this.add(version);
        //this.add(author);
        this.add(info);

        initMenuBar();

        GetNewerVersion.checkForUpdate(this, view, false, false);

    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JMenu menu = new JMenu("转到");

        JMenu chat = new JMenu("社交");

        JMenuItem weChat = new JMenuItem("微信");
        weChat.setIcon(GetIcon.getIcon(getClass().getResource("/image/wechat.png"), 20, 20));
        weChat.addActionListener(e ->
                Log.info.message(this, "关于-个人信息", "微信: w13607088913")
        );

        JMenuItem qq = new JMenuItem("QQ");
        qq.setIcon(GetIcon.getIcon(getClass().getResource("/image/qq.png"), 20, 20));
        qq.addActionListener(e ->
                Log.info.message(this, "关于-个人信息", "QQ: 2134868121"));

        JMenuItem bilibili = new JMenuItem("哔哩哔哩");
        bilibili.setIcon(GetIcon.getIcon(getClass().getResource("/image/bilibili.png"), 20, 20));
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
        github.setIcon(GetIcon.getIcon(getClass().getResource("/image/github.png"), 20, 20));

        JMenuItem authorGithub = new JMenuItem("作者");
        authorGithub.setIcon(GetIcon.getIcon(getClass().getResource("/image/github.png"), 20, 20));
        authorGithub.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/wmp666"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        JMenuItem repo = new JMenuItem("仓库");
        repo.setIcon(GetIcon.getIcon(getClass().getResource("/image/github.png"), 20, 20));
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
        appPath.setIcon(GetIcon.getIcon(getClass().getResource("/image/openExp.png"), 20, 20));
        appPath.addActionListener(e -> {
            OpenInExp.open(GetPath.getAppPath(GetPath.APPLICATION_PATH));

        });

        JMenuItem dataPath = new JMenuItem("数据路径");
        dataPath.setIcon(GetIcon.getIcon(getClass().getResource("/image/openExp.png"), 20, 20));
        dataPath.addActionListener(e -> {
            OpenInExp.open(CTInfo.DATA_PATH);
        });

        menu.add(chat);
        menu.add(github);
        menu.add(appPath);
        menu.add(dataPath);

        menuBar.add(menu);

        // 在现有菜单中添加

        JMenu downloadMenu = new JMenu("下载");

        //获取源代码
        JMenuItem getSource = new JMenuItem("获取源代码");
        getSource.addActionListener(e -> GetNewerVersion.getSource(this, view));

        JMenuItem checkUpdate = new JMenuItem("检查更新");
        checkUpdate.setIcon(GetIcon.getIcon(getClass().getResource("/image/light/update_0.png"), 20, 20));
        checkUpdate.addActionListener(e -> GetNewerVersion.checkForUpdate(this, view, true));

        downloadMenu.add(getSource);
        downloadMenu.add(checkUpdate);

        menuBar.add(downloadMenu);

        JMenu helpMenu = new JMenu("帮助");

        JMenuItem helpDoc = new JMenuItem("帮助文档");
        helpDoc.setIcon(GetIcon.getIcon(getClass().getResource("/image/doc.png"), 20, 20));
        helpDoc.addActionListener(e -> {
            try {
                new ShowHelpDoc();
            } catch (URISyntaxException | IOException ex) {
                throw new RuntimeException(ex);
            }

        });

        JMenuItem easterEgg = new JMenuItem("■■");
        easterEgg.setIcon(GetIcon.getIcon(getClass().getResource("/image/wish.png"), 20, 20));
        easterEgg.addActionListener(e ->
            EasterEgg.getPin());

        helpMenu.add(helpDoc);
        helpMenu.add(easterEgg);

        menuBar.add(helpMenu);
    }


}
