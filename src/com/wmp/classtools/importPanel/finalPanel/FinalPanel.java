package com.wmp.classTools.importPanel.finalPanel;

import com.wmp.Main;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.PublicTools.update.GetNewerVersion;
import com.wmp.classTools.CTComponent.CTIconButton;
import com.wmp.classTools.CTComponent.CTPanel;
import com.wmp.classTools.frame.AboutDialog;
import com.wmp.classTools.frame.MainWindow;
import com.wmp.classTools.frame.ShowCookieDialog;
import com.wmp.classTools.infSet.InfSetDialog;
import com.wmp.classTools.infSet.tools.GetSetsJSON;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static com.wmp.Main.disButList;
import static com.wmp.Main.disPanelList;

public class FinalPanel extends CTPanel {

    public static final ArrayList<CTIconButton> allButList = new ArrayList<>();


    public FinalPanel() throws MalformedURLException {
        super();


        this.setName("功能性按钮组");
        this.setID("FinalPanel");

        initPanel();

        initButton();

        Log.initTrayIcon();
    }

    private void initPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBackground(CTColor.backColor);
        // 添加弹性空间
        this.add(Box.createHorizontalGlue()); // 左侧弹簧
        this.add(Box.createRigidArea(new Dimension(5, 0))); // 按钮间距
        this.add(Box.createHorizontalGlue()); // 右侧弹簧

    }

    public static void refreshPanel() {
        GetSetsJSON setsJSON;
        try {
            setsJSON = new GetSetsJSON();


            Main.canExit = setsJSON.isCanExit();
            disButList.clear();
            disButList.addAll(setsJSON.getDisButList());
            disPanelList.clear();
            disPanelList.addAll(setsJSON.getDisPanelList());
            MainWindow.showPanelList.clear();

            //Log.err.print("FinalPanel", "已折叠的Panel:" + disPanelList);

            //1.判断需要显示的CTPanel,不需要的清除其中的内容
            MainWindow.allPanelList.forEach(panel -> {
                if (!disPanelList.contains(panel.getID())) {
                    MainWindow.showPanelList.add(panel);
                } else {
                    panel.removeAll();
                    panel.revalidate();
                    panel.repaint();
                }
            });

            //刷新要显示的CTPanel的内容
            MainWindow.showPanelList.forEach(panel -> {
                try {
                    panel.refresh();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });// 自定义刷新方法

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initButton() throws MalformedURLException {

        JPopupMenu moreMenu = new JPopupMenu();
        moreMenu.setBackground(CTColor.backColor);

        CTIconButton moreButton = new CTIconButton("更多功能",
                "/image/%s/more.png",
                "/image/%s/more.png", 30, () -> {
            //moreDialog.setVisible(true);
        });
        moreButton.setCallback(() -> {
            moreMenu.show(moreButton, 0, moreButton.getHeight());
        });
        moreButton.setPreferredSize(moreButton.getSize());
        moreButton.setMaximumSize(moreButton.getSize());
        moreButton.setMinimumSize(moreButton.getSize());
        allButList.clear();

        CTIconButton settings = new CTIconButton("设置",
                "/image/%s/settings_0.png",
                "/image/%s/settings_1.png", 30, () -> {

            try {
                new InfSetDialog(() -> {

                    refreshPanel();
                });
            } catch (IOException e) {
                Log.err.print("FinalPanel", "设置打开失败");
                throw new RuntimeException(e);
            }

        });
        allButList.add(settings);

        CTIconButton cookie = new CTIconButton("插件库",
                "/image/%s/cookie_0.png",
                "/image/%s/cookie_1.png", 30, () -> {
            try {
                new ShowCookieDialog();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        allButList.add(cookie);


        CTIconButton about = new CTIconButton("软件信息",
                "/image/%s/about_0.png",
                "/image/%s/about_1.png", 30, () -> {
            try {
                new AboutDialog().setVisible(true);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        });
        allButList.add(about);

        CTIconButton update = new CTIconButton("检查更新",
                "/image/%s/update_0.png",
                "/image/%s/update_1.png", 30, () -> GetNewerVersion.checkForUpdate(null, null, true));
        allButList.add(update);

        CTIconButton refresh = new CTIconButton("刷新",
                "/image/%s/refresh_0.png",
                "/image/%s/refresh_1.png", 30, () -> {
            refreshPanel();// 自定义刷新方法
        });
        allButList.add(refresh);

        CTIconButton showLog = new CTIconButton("查看日志",
                "/image/%s/showLog_0.png",
                "/image/%s/showLog_1.png", 30, Log::showLogDialog);
        showLog.setPreferredSize(showLog.getSize());
        showLog.setMaximumSize(showLog.getSize());
        showLog.setMinimumSize(showLog.getSize());

        this.add(moreButton);
        AtomicInteger length = new AtomicInteger();

        //按钮展示
        allButList.forEach(ctButton -> {
            if (disButList.contains(ctButton.getName())) {
                ctButton.setText(ctButton.getToolTipText());
                //moreDialog.add(ctButton);
                moreMenu.add(ctButton);
                length.getAndIncrement();
            } else {
                CTIconButton temp = null;
                try {
                    temp = ctButton.copy();
                    temp.setPreferredSize(ctButton.getSize());
                    temp.setMaximumSize(ctButton.getSize());
                    temp.setMinimumSize(ctButton.getSize());
                    this.add(temp);
                } catch (MalformedURLException e) {
                    Log.err.print("FinalPanel", "初始化按钮时出错\n" + e);
                    throw new RuntimeException(e);
                }
            }
        });

        this.add(showLog);

        if (length.get() == 0) {
            this.remove(moreButton);
        }

        //设置关闭按钮
        if (!Main.isError && Main.canExit && !Main.allArgs.get("screenProduct:show").contains(Main.argsList)) {
            CTIconButton exit = new CTIconButton("关闭",
                    "/image/%s/exit_0.png",
                    "/image/%s/exit_1.png", 30, () -> {
                int i = Log.info.showChooseDialog(null, "CTPanel-按钮组", "确认退出?");
                if (i == JOptionPane.YES_OPTION) {
                    Log.exit(0);
                }

            });
            exit.setPreferredSize(exit.getSize());
            exit.setMaximumSize(exit.getSize());
            exit.setMinimumSize(exit.getSize());

            //exit.setLocation(210, 0);
            this.add(exit);
        }
    }

    @Override
    public void refresh() throws IOException {
        this.removeAll();

        initPanel();
        initButton();

        revalidate();
        repaint();
    }
}
