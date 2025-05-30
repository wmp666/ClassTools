package com.wmp.PublicTools.printLog;

import com.wmp.Main;
import com.wmp.PublicTools.UITools.GetIcon;

import javax.swing.*;
import java.awt.*;

public class InfoLogStyle extends PrintLogStyle {



    public InfoLogStyle(LogStyle style) {
        super(style);
    }

    public void message(Container c, String owner, String logInfo) {
        Log.print(getStyle(), owner, "弹窗信息->" + logInfo, c);
        String title = getTitle(owner);
        JOptionPane.showMessageDialog(c, logInfo, title, JOptionPane.INFORMATION_MESSAGE, getIcon());
    }

    private static String getTitle(String owner) {
        String title;
        if (Main.isError) title = "骇客已入侵";
        else title = owner;
        return title;
    }

    private static Icon getIcon() {
        if (Main.isError) return GetIcon.getIcon(Main.class.getResource("/image/error/icon.png"), 100, 100);
        return null;
    }

    public String input(Container c, String owner, String logInfo) {
        Log.print(getStyle(), owner, "弹窗信息->" + logInfo, c);
        String title = getTitle(owner);
        String s = JOptionPane.showInputDialog(c, logInfo, title, JOptionPane.QUESTION_MESSAGE, getIcon(),
                null, null).toString();
        Log.print(getStyle(), owner, "输入信息->" + s, c);
        return s;
    }

    public int inputInt(Container c, String owner, String logInfo) {
        Log.print(getStyle(), owner, "弹窗信息->" + logInfo, c);
        String title = getTitle(owner);
        int i = JOptionPane.showConfirmDialog(c, logInfo, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, getIcon());
        String s ;
        if (i == JOptionPane.YES_OPTION) {
            s = "是";
        } else if (i == JOptionPane.NO_OPTION) {
            s = "否";
        }else {
            s = "取消";
        }

        Log.print(getStyle(), owner, "输入信息->" + s, c);
        return i;
    }
}
