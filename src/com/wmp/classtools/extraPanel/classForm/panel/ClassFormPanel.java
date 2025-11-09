package com.wmp.classTools.extraPanel.classForm.panel;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.UITools.PeoPanelProcess;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTPanel.CTViewPanel;
import com.wmp.classTools.extraPanel.classForm.CFInfoControl;
import com.wmp.classTools.extraPanel.classForm.settings.ClassFormSetsPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassFormPanel extends CTViewPanel {

    private final ArrayList<String> oldNowClassNameList = new ArrayList<>();

    private String oldNextClassName = "无";


    public ClassFormPanel() {
        this.setLayout(new GridBagLayout());
        this.setName("课程表");
        this.setID("ClassFormPanel");
        this.setOpaque(false);
        this.setCtSetsPanelList(java.util.List.of(new ClassFormSetsPanel(CTInfo.DATA_PATH)));

        this.setIgnoreState(true);
        this.setIndependentRefresh(true, 3 * 1000);
    }

    private void showClassForm(String[] nowClassesList, String nextClass) {

        if (List.of(nowClassesList).contains("无") && nextClass.equals("无")) {
            return;
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < nowClassesList.length; i++) {
            if (i != nowClassesList.length - 1)
                sb.append(nowClassesList[i]).append(", ");
            else sb.append(nowClassesList[i]);

        }

        String infoSB = "现在的课程:" +
                sb +
                "\n" +
                "下节课:" +
                nextClass;

        Log.info.adaptedMessage("课程提醒", infoSB, 60, 5);

    }

    @Override
    protected void Refresh() throws IOException {
        this.removeAll();


        //课程数据
        String[] nowClasses = CFInfoControl.getNowClasses();
        CFInfoControl.nextClassInfo nextClassInfo = CFInfoControl.getNextClass();
        String nextClass = nextClassInfo.className();
        try {

            if (nowClasses.length == 0) {
                nowClasses = new String[]{"无"};
            }
            if (nextClass == null || nextClass.isEmpty()) nextClass = "无";

            if (!oldNowClassNameList.equals(List.of(nowClasses)) ||
                    !oldNextClassName.equals(nextClass))
                showClassForm(nowClasses, nextClass.equals("无") ? "无" : String.format("%s(%s分钟)", nextClass, nextClassInfo.time()));

            //数据更新
            this.oldNowClassNameList.clear();
            this.oldNowClassNameList.addAll(List.of(nowClasses));

            oldNextClassName = nextClass;


        } catch (Exception e) {
            Log.err.print(getClass(), "获取课程表失败", e);
        }


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel titleLabel = new JLabel("<html>本节课:</html>");
        titleLabel.setForeground(CTColor.textColor);
        titleLabel.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.NORMAL));
        this.add(titleLabel, gbc);

        gbc.gridy++;
        this.add(PeoPanelProcess.getShowPeoPanel(List.of(nowClasses)), gbc);

        JLabel titleLabel2 = new JLabel("<html>下节课:</html>");
        titleLabel2.setForeground(CTColor.textColor);
        titleLabel2.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.NORMAL));
        gbc.gridy++;
        this.add(titleLabel2, gbc);

        gbc.gridy++;
        this.add(PeoPanelProcess.getShowPeoPanel(List.of(nextClass.equals("无") ? "无" : String.format("%s(%s分钟)", nextClass, nextClassInfo.time()))), gbc);


        this.revalidate();
        this.repaint();
    }
}
