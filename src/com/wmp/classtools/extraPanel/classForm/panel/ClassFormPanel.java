package com.wmp.classTools.extraPanel.classForm.panel;

import com.wmp.Main;
import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.UITools.PeoPanelProcess;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTOptionPane;
import com.wmp.classTools.CTComponent.CTPanel;
import com.wmp.classTools.extraPanel.classForm.CFInfoControl;
import com.wmp.classTools.extraPanel.classForm.settings.ClassFormSetsPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassFormPanel extends CTPanel {

    private ArrayList<String> oldNowClassNameList = new ArrayList<>();

    private String oldNextClassName = "无";


    public ClassFormPanel() {
        this.setLayout(new GridBagLayout());
        this.setName("课程表");
        this.setID("ClassFormPanel");
        this.setOpaque(false);
        this.setCtSetsPanelList(java.util.List.of(new ClassFormSetsPanel(CTInfo.DATA_PATH)));

        //刷新
        new Thread(() -> {
            while (true) {
                this.removeAll();

                if (CTInfo.disPanelList.contains(getID())) {
                    continue;
                }

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.gridx = 0;
                gbc.gridy = 0;


                JLabel titleLabel = new JLabel("<html>本节课:</html>");
                titleLabel.setForeground(CTColor.textColor);
                titleLabel.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.NORMAL));
                this.add(titleLabel, gbc);


                //现在的课程
                try {
                    String[] nowClasses = CFInfoControl.getNowClasses();
                    if (nowClasses.length == 0) {
                        nowClasses = new String[]{"无"};
                    }
                    if (!oldNowClassNameList.equals(List.of(nowClasses))) showNowClasses(nowClasses);


                    this.oldNowClassNameList.clear();
                    this.oldNowClassNameList.addAll(List.of(nowClasses));


                    gbc.gridy++;
                    this.add(PeoPanelProcess.getShowPeoPanel(List.of(nowClasses)), gbc);
                } catch (Exception e) {
                    Log.err.print("ClassFormPanel", "获取课程列表失败: \n" + e.getMessage());
                    throw new RuntimeException(e);
                }

                JLabel titleLabel2 = new JLabel("<html>下节课:</html>");
                titleLabel2.setForeground(CTColor.textColor);
                titleLabel2.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.NORMAL));
                gbc.gridy++;
                this.add(titleLabel2, gbc);

                try {
                    String nextClass = CFInfoControl.getNextClass();
                    if (nextClass == null || nextClass.isEmpty()) nextClass = "无";
                    if (!oldNextClassName.equals(nextClass)) showNextClasses(nextClass);


                    this.oldNowClassNameList.clear();
                    this.oldNowClassNameList.add(nextClass);


                    gbc.gridy++;
                    this.add(PeoPanelProcess.getShowPeoPanel(List.of(nextClass)), gbc);
                } catch (Exception e) {
                    Log.err.print("ClassFormPanel", "获取课程表失败: \n" + e.getMessage());
                    throw new RuntimeException(e);
                }

                this.revalidate();
                this.repaint();

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }).start();
    }

    private void showNowClasses(String[] list) {

        if (!List.of(list).contains("无")) {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < list.length; i++) {
                if (i != list.length - 1)
                    sb.append(list[i]).append(", ");
                else sb.append(list[i]);

            }
            if (Main.allArgs.get("screenProduct:show").contains(Main.argsList))
                CTOptionPane.showFullScreenMessageDialog("课程提醒", "现在的课程:" + sb, 60);
            else
                Log.info.systemPrint("课程提醒", "现在的课程:" + sb);
        }


    }

    private void showNextClasses(String className) {

        if (!className.equals("无")) {

            if (Main.allArgs.get("screenProduct:show").contains(Main.argsList))
                CTOptionPane.showFullScreenMessageDialog("课程提醒", "下节课程:" + className, 60);
            else
                Log.info.systemPrint("课程提醒", "下节课程:" + className);
        }


    }

    @Override
    public void refresh() throws IOException {

    }
}
