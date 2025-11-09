package com.wmp.classTools.frame;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.EasterEgg.EasterEgg;
import com.wmp.PublicTools.OpenInExp;
import com.wmp.PublicTools.UITools.*;
import com.wmp.PublicTools.io.GetPath;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.PublicTools.update.GetNewerVersion;
import com.wmp.classTools.CTComponent.CTButton.CTIconButton;
import com.wmp.classTools.CTComponent.CTButton.CTTextButton;
import com.wmp.classTools.CTComponent.Menu.CTMenu;
import com.wmp.classTools.CTComponent.Menu.CTMenuBar;
import com.wmp.classTools.CTComponent.Menu.CTMenuItem;
import com.wmp.classTools.CTComponent.Menu.CTPopupMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URI;

public class AboutDialog extends JDialog {

    private static final JPanel view = new JPanel();

    static {
        view.setBackground(CTColor.backColor);
        view.setLayout(new BorderLayout());
        view.setPreferredSize(new Dimension((int) (300 * CTInfo.dpi), (int) (100 * CTInfo.dpi)));

        GetNewerVersion.checkForUpdate(null, view, false, false);
    }

    public AboutDialog() throws MalformedURLException {

        this.setTitle("关于");
        this.setSize((int) (300 * CTInfo.dpi), (int) (400 * CTInfo.dpi));
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        this.setModal(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.getContentPane().setBackground(CTColor.backColor);


        JLabel icon = new JLabel(GetIcon.getIcon("图标", IconControl.COLOR_DEFAULT, 100, 100));


        JLabel info = new JLabel("<html>"
                + "程序名: " + CTInfo.appName + "<br><br>"
                + "作者: " + CTInfo.author + "<br><br>"
                + "版本: " + CTInfo.version
                + "</html>");
        info.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.SMALL));
        info.setForeground(CTColor.textColor);
        info.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int button = e.getButton();
                if (button == MouseEvent.BUTTON3) {
                    CTPopupMenu ETPopupMenu = new CTPopupMenu();

                    CTTextButton edit = new CTTextButton("编辑");
                    edit.setIcon(GetIcon.getIcon("编辑", IconControl.COLOR_DEFAULT, 20, 20));
                    edit.addActionListener(event -> {
                        EasterEgg.errorAction();
                    });

                    ETPopupMenu.add(edit);

                    ETPopupMenu.show(info, e.getX(), e.getY());
                }
            }
        });
        //view = new JPanel();

        JPanel infos = new JPanel();
        infos.setOpaque(false);
        infos.setLayout(new BorderLayout());
        infos.add(icon, BorderLayout.WEST);
        infos.add(info, BorderLayout.CENTER);

        CTIconButton getNew = new CTIconButton("检查更新",
                "更新", IconControl.COLOR_COLORFUL,
                () -> GetNewerVersion.checkForUpdate(this, view, true));
        getNew.setBackground(CTColor.backColor);

        JPanel update = new JPanel(new GridBagLayout());
        update.setOpaque(false);
        {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(5, 5, 5, 5);
            update.add(view, gbc);
            gbc.gridy++;
            update.add(getNew.toRoundTextButton(), gbc);


        }

        this.add(update, BorderLayout.SOUTH);
        this.add(infos, BorderLayout.CENTER);

        initMenuBar();


    }

    private void initMenuBar() {
        CTMenuBar menuBar = new CTMenuBar();
        this.setJMenuBar(menuBar);

        CTMenu menu = new CTMenu("转到");

        CTMenu chat = new CTMenu("社交");

        CTMenuItem weChat = new CTMenuItem("微信");
        weChat.setIcon(GetIcon.getIcon("微信", IconControl.COLOR_DEFAULT, 20, 20));
        weChat.addActionListener(e ->
                Log.info.message(this, "关于-个人信息", "微信: w13607088913")
        );

        CTMenuItem qq = new CTMenuItem("QQ");
        qq.setIcon(GetIcon.getIcon("QQ", 20, 20));
        qq.addActionListener(e ->
                Log.info.message(this, "关于-个人信息", "QQ: 2134868121"));

        CTMenuItem bilibili = new CTMenuItem("哔哩哔哩");
        bilibili.setIcon(GetIcon.getIcon("哔哩哔哩", 20, 20));
        bilibili.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://space.bilibili.com/1075810224/"));
            } catch (Exception ex) {
                Log.err.print(getClass(), "网页打开失败", ex);
            }
        });

        chat.add(weChat);
        chat.add(qq);
        chat.add(bilibili);

        CTMenu github = new CTMenu("Github");
        github.setIcon(GetIcon.getIcon("Github", 20, 20));

        CTMenuItem authorGithub = new CTMenuItem("作者");
        authorGithub.setIcon(GetIcon.getIcon("Github", 20, 20));
        authorGithub.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/wmp666"));
            } catch (Exception ex) {
                Log.err.print(getClass(), "网页打开失败", ex);
            }
        });

        CTMenuItem repo = new CTMenuItem("仓库");
        repo.setIcon(GetIcon.getIcon("Github", 20, 20));
        repo.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/wmp666/ClassTools"));
            } catch (Exception ex) {
                Log.err.print(getClass(), "网页打开失败", ex);
            }
        });

        github.add(authorGithub);
        github.add(repo);

        CTMenuItem appPath = new CTMenuItem("程序路径");
        appPath.setIcon(GetIcon.getIcon("文件夹", 20, 20));
        appPath.addActionListener(e -> {
            OpenInExp.open(GetPath.getAppPath(GetPath.APPLICATION_PATH));

        });

        CTMenuItem dataPath = new CTMenuItem("数据路径");
        dataPath.setIcon(GetIcon.getIcon("文件夹", 20, 20));
        dataPath.addActionListener(e -> {
            OpenInExp.open(CTInfo.DATA_PATH);
        });

        menu.add(chat);
        menu.add(github);
        menu.addSeparator();
        menu.add(appPath);
        menu.add(dataPath);

        menuBar.add(menu);

        // 在现有菜单中添加

        CTMenu downloadMenu = new CTMenu("下载");

        //获取源代码
        CTMenuItem getSource = new CTMenuItem("获取源代码");
        getSource.addActionListener(e -> GetNewerVersion.getSource(this, view));

        CTMenuItem checkUpdate = new CTMenuItem("检查更新");
        checkUpdate.setIcon(GetIcon.getIcon("更新", 20, 20));
        checkUpdate.addActionListener(e -> GetNewerVersion.checkForUpdate(this, view, true));

        downloadMenu.add(getSource);
        downloadMenu.add(checkUpdate);

        menuBar.add(downloadMenu);

        CTMenu helpMenu = new CTMenu("帮助");

        CTMenuItem helpDoc = new CTMenuItem("帮助文档");
        helpDoc.setIcon(GetIcon.getIcon("文档", 20, 20));
        helpDoc.addActionListener(e -> {
            try {
                new ShowHelpDoc();
            } catch (Exception ex) {
                Log.err.print(getClass(), "帮助打开失败", ex);
            }

        });

        CTMenuItem easterEgg = new CTMenuItem("■■");
        easterEgg.setIcon(GetIcon.getIcon("祈愿", 20, 20));
        easterEgg.addActionListener(e ->
                EasterEgg.getPin());

        helpMenu.add(helpDoc);
        helpMenu.add(easterEgg);

        menuBar.add(helpMenu);
    }


}
