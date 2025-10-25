package com.wmp.classTools.importPanel.finalPanel;

import com.wmp.Main;
import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.EasterEgg.EasterEgg;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.PublicTools.update.GetNewerVersion;
import com.wmp.classTools.CTComponent.CTButton.CTIconButton;
import com.wmp.classTools.CTComponent.CTPanel.CTViewPanel;
import com.wmp.classTools.CTComponent.Menu.CTPopupMenu;
import com.wmp.classTools.frame.AboutDialog;
import com.wmp.classTools.frame.MainWindow;
import com.wmp.classTools.frame.ShowCookieDialog;
import com.wmp.classTools.infSet.InfSetDialog;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;


public class FinalPanel extends CTViewPanel {

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
        this.add(Box.createRigidArea(new Dimension(0, 0))); // 按钮间距
        this.add(Box.createHorizontalGlue()); // 右侧弹簧


    }

    private void initButton() throws MalformedURLException {

        CTPopupMenu moreMenu = new CTPopupMenu();
        moreMenu.setBackground(CTColor.backColor);


        CTIconButton moreButton = new CTIconButton("更多功能",
                "/image/%s/more.png", () -> {
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
                "/image/%s/settings_0.png", () -> {

            try {
                    new InfSetDialog();
            } catch (Exception e) {
                Log.err.print(getClass(), "设置打开失败", e);
            }

        });
        allButList.add(settings);

        CTIconButton cookie = new CTIconButton("快速启动页",
                "/image/%s/cookie_0.png", () -> {
            try {
                new ShowCookieDialog();
            } catch (IOException e) {
                Log.err.print(getClass(), "打开失败", e);
            }
        });
        allButList.add(cookie);


        CTIconButton about = new CTIconButton("软件信息",
                "/image/%s/about_0.png", () -> {
            try {
                new AboutDialog().setVisible(true);
            } catch (Exception e) {
                Log.err.print(getClass(), "打开失败", e);
            }
        });
        allButList.add(about);

        CTIconButton update = new CTIconButton("检查更新",
                "/image/%s/update_0.png", () -> GetNewerVersion.checkForUpdate(null, null, true));
        allButList.add(update);

        // 自定义刷新方法
        CTIconButton refresh = new CTIconButton("刷新",
                "/image/%s/refresh_0.png", MainWindow::refresh);
        allButList.add(refresh);

        CTIconButton holidayBlessings = new CTIconButton("查看祝词",
                "/image/wish.png", () -> EasterEgg.showHolidayBlessings(1));
        allButList.add(holidayBlessings);

        CTIconButton showLog = new CTIconButton("查看日志",
                "/image/%s/showLog_0.png", Log::showLogDialog);
        showLog.setPreferredSize(showLog.getSize());
        showLog.setMaximumSize(showLog.getSize());
        showLog.setMinimumSize(showLog.getSize());


        this.add(Box.createHorizontalStrut(2));
        this.add(moreButton);
        this.add(Box.createHorizontalStrut(2)); // 按钮后添加间距

        AtomicInteger length = new AtomicInteger();

        //按钮展示
        allButList.forEach(ctButton -> {
            if (CTInfo.disButList.contains(ctButton.getName())) {
                moreMenu.add(ctButton.toRoundTextButton());
                length.getAndIncrement();
            } else {
                CTIconButton temp = null;
                try {
                    temp = ctButton.copy();
                    temp.setPreferredSize(ctButton.getSize());
                    temp.setMaximumSize(ctButton.getSize());
                    temp.setMinimumSize(ctButton.getSize());
                    this.add(Box.createHorizontalStrut(2));
                    this.add(temp);
                    this.add(Box.createHorizontalStrut(2)); // 按钮后添加间距
                } catch (MalformedURLException e) {
                    Log.err.print(getClass(), "初始化按钮时出错", e);
                }
            }
        });


        this.add(Box.createHorizontalStrut(2));
        this.add(showLog);
        this.add(Box.createHorizontalStrut(2)); // 按钮后添加间距

        if (length.get() == 0) {
            this.remove(moreButton);
        }

        //设置关闭按钮
        if (!CTInfo.isError && CTInfo.canExit && !Main.allArgs.get("screenProduct:show").contains(Main.argsList)) {
            CTIconButton exit = new CTIconButton("关闭",
                    "/image/%s/exit_0.png", () -> {
                int i = Log.info.showChooseDialog(null, "CTViewPanel-按钮组", "确认退出?");
                if (i == JOptionPane.YES_OPTION) {
                    Log.exit(0);
                }

            });
            exit.setPreferredSize(exit.getSize());
            exit.setMaximumSize(exit.getSize());
            exit.setMinimumSize(exit.getSize());


            this.add(Box.createHorizontalStrut(5));
            this.add(exit);
            this.add(Box.createHorizontalStrut(5)); // 按钮后添加间距
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
