package com.wmp.classTools.importPanel.finalPanel;

import com.wmp.Main;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.GetIcon;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.PublicTools.update.GetNewerVersion;
import com.wmp.classTools.CTComponent.CTButton;
import com.wmp.classTools.CTComponent.CTPanel;
import com.wmp.classTools.frame.AboutDialog;
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

public class FinalPanel extends CTPanel {


    private final ArrayList<CTPanel> panelList;
    public static final ArrayList<CTButton> buttonList = new ArrayList<>();


    public FinalPanel(int nextPanelY, ArrayList<CTPanel> panelList) throws MalformedURLException {
        super(nextPanelY);


        this.panelList = panelList;


        initPanel();

        initButton(panelList);
    }

    private void initPanel() {
        this.setLayout(new GridLayout(1, 6));
        this.setBackground(CTColor.backColor);
        //this.setLayout(null);

        this.setSize(250, 39);
    }

    private void initButton(ArrayList<CTPanel> panelList) throws MalformedURLException {
        JDialog moreDialog = new JDialog();
        moreDialog.setTitle("已折叠的功能");
        moreDialog.setLayout(new FlowLayout(FlowLayout.CENTER));
        moreDialog.setSize(250, 300);
        moreDialog.setLocationRelativeTo(null);
        moreDialog.setModal(true);
        moreDialog.getContentPane().setBackground(CTColor.backColor);
        moreDialog.setIconImage(GetIcon.getImageIcon(getClass().getResource("/image/light/more.png"), 32, 32).getImage());

        CTButton moreButton = new CTButton("更多功能",
                "/image/%s/more.png",
                "/image/%s/more.png", 30, () -> moreDialog.setVisible(true));

        buttonList.clear();

        CTButton settings = new CTButton("设置",
                "/image/%s/settings_0.png",
                "/image/%s/settings_1.png", 30, () -> {

            try {
                new InfSetDialog(() -> {

                    refreshPanel(panelList);
                }).setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
        buttonList.add(settings);

        CTButton cookie = new CTButton("插件库",
                "/image/%s/cookie_0.png",
                "/image/%s/cookie_1.png", 30, () -> {
            try {
                new ShowCookieDialog();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        buttonList.add(cookie);


        CTButton about = new CTButton("软件信息",
                "/image/%s/about_0.png",
                "/image/%s/about_1.png", 30, () -> {
            try {
                new AboutDialog().setVisible(true);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        });
        buttonList.add(about);

        CTButton update = new CTButton("检查更新",
                "/image/%s/update_0.png",
                "/image/%s/update_1.png", 30, () -> GetNewerVersion.checkForUpdate(null, null, true));
        buttonList.add(update);

        CTButton refresh = new CTButton("刷新",
                "/image/%s/refresh_0.png",
                "/image/%s/refresh_1.png", 30, () -> {
            panelList.forEach(panel -> {
                refreshPanel(panelList);
            });// 自定义刷新方法
        });
        buttonList.add(refresh);

        CTButton showLog = new CTButton("查看日志",
                "/image/%s/showLog_0.png",
                "/image/%s/showLog_1.png", 30, Log::showLogDialog);



        this.add(moreButton);
        buttonList.forEach(this::add);
        this.add(showLog);

        AtomicInteger length = new AtomicInteger();

        buttonList.forEach(ctButton -> {
            if (disButList.contains(ctButton.getName())) {
                ctButton.setText(ctButton.getToolTipText());
                moreDialog.add(ctButton);
                length.getAndIncrement();
            }
        });

        if (length.get() == 0) {
            this.remove(moreButton);
        }




        //设置关闭按钮
        if (!Main.isError && Main.canExit) {
            CTButton exit = new CTButton("关闭",
                    "/image/%s/exit_0.png",
                    "/image/%s/exit_1.png", 30, () -> {
                int i = Log.info.inputInt(null, "CTPanel-按钮组", "确认退出?");
                if (i == JOptionPane.YES_OPTION) {
                    Log.exit(0);
                }

            });

            //exit.setLocation(210, 0);
            this.add(exit);
        }
    }

    private static void refreshPanel(ArrayList<CTPanel> panelList) {
        GetSetsJSON setsJSON;
        try {
            setsJSON = new GetSetsJSON();


            Main.canExit = setsJSON.isCanExit();
            disButList.clear();
            disButList.addAll(setsJSON.getDisButList());

            panelList.forEach(panel -> {
                try {
                    panel.refresh();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });// 自定义刷新方法
            //this.setBackground(CTColor.backColor);
            //refresh();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void refresh() throws IOException {
        this.removeAll();

        initPanel();
        initButton(panelList);

        revalidate();
        repaint();
    }
}
