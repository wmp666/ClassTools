package com.wmp.classTools.importPanel.finalPanel;

import com.wmp.CTColor;
import com.wmp.Main;
import com.wmp.PublicTools.GetIcon;
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
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import static com.wmp.Main.disButList;

public class FinalPanel extends CTPanel {

    private final File AllStuPath;
    private final File LeaveListPath;
    private final File DutyListPath;
    private final File indexPath;

    private final ArrayList<CTPanel> panelList;


    public FinalPanel(int nextPanelY, File AllStuPath, File LeaveListPath, File DutyListPath, File indexPath, ArrayList<CTPanel> panelList) throws MalformedURLException {
        super(nextPanelY);

        this.AllStuPath = AllStuPath;
        this.LeaveListPath = LeaveListPath;
        this.DutyListPath = DutyListPath;
        this.indexPath = indexPath;
        this.panelList = panelList;


        initPanel();

        initButton(AllStuPath, LeaveListPath, DutyListPath, indexPath,
                panelList);
    }

    private void initPanel() {
        this.setLayout(new GridLayout(1, 6));
        this.setBackground(CTColor.backColor);
        //this.setLayout(null);

        this.setSize(250, 39);
    }

    private void initButton(File AllStuPath, File LeaveListPath, File DutyListPath, File indexPath,
                            ArrayList<CTPanel> panelList) throws MalformedURLException {
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

        CTButton settings = new CTButton("设置数据",
                "/image/%s/settings_0.png",
                "/image/%s/settings_1.png", 30, () -> {


            try {
                new InfSetDialog(AllStuPath, LeaveListPath, DutyListPath, indexPath, () -> {

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
                        this.setBackground(CTColor.backColor);
                        refresh();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        //settings.setLocation(210, 0);


        CTButton cookie = new CTButton("启用插件",
                "/image/%s/cookie_0.png",
                "/image/%s/cookie_1.png", 30, () -> {
            try {
                new ShowCookieDialog();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


        CTButton about = new CTButton("软件信息",
                "/image/%s/about_0.png",
                "/image/%s/about_1.png", 30, () -> {
            try {
                new AboutDialog().setVisible(true);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        });


        CTButton update = new CTButton("检查更新",
                "/image/%s/update_0.png",
                "/image/%s/update_1.png", 30, () -> GetNewerVersion.checkForUpdate(null, null, true));

        CTButton showLog = new CTButton("查看日志",
                "/image/%s/showLog_0.png",
                "/image/%s/showLog_1.png", 30, Log::showLogDialog);



        this.add(moreButton);
        this.add(settings);
        this.add(cookie);
        this.add(about);
        this.add(showLog);
        this.add(update);

        int length = 0;

        for (String s : disButList) {
            if (s.equals("cookie")) {
                cookie.setText(cookie.getToolTipText());
                moreDialog.add(cookie);
                length++;
            }
            if (s.equals("settings")) {
                settings.setText(settings.getToolTipText());
                moreDialog.add(settings);
                length++;
            }
            if (s.equals("update")) {
                update.setText(update.getToolTipText());
                moreDialog.add(update);
                length++;
            }
            if (s.equals("about")) {
                about.setText(about.getToolTipText());
                moreDialog.add(about);
                length++;
            }
        }
        if (length == 0) {
            this.remove(moreButton);
        }




        //设置关闭按钮
        if (Main.canExit) {
            CTButton exit = new CTButton("关闭",
                    "/image/%s/exit_0.png",
                    "/image/%s/exit_1.png", 30, () -> {
                int i = JOptionPane.showConfirmDialog(null, "确认退出?", "询问", JOptionPane.YES_NO_OPTION);
                if (i == JOptionPane.YES_OPTION) {
                    Log.exit(0);
                }

            });

            //exit.setLocation(210, 0);
            this.add(exit);
        }
    }

    @Override
    public void refresh() throws IOException {
        this.removeAll();

        initPanel();
        initButton(AllStuPath, LeaveListPath, DutyListPath, indexPath, panelList);

        revalidate();
        repaint();
    }
}
