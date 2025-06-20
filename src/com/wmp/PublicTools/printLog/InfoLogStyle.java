package com.wmp.PublicTools.printLog;

import com.wmp.Main;
import com.wmp.PublicTools.UITools.GetIcon;
import com.wmp.classTools.CTComponent.CTOptionPane;

import javax.swing.*;
import java.awt.*;

public class InfoLogStyle extends PrintLogStyle {



    public InfoLogStyle(LogStyle style) {
        super(style);
    }

    public void message(Container c, String owner, String logInfo) {
        Log.print(getStyle(), owner, "弹窗信息->" + logInfo, c);
        String title = getTitle(owner);
        CTOptionPane.showMessageDialog(c, title, logInfo, getIcon(), CTOptionPane.INFORMATION_MESSAGE, true);
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

    public String showInputDialog(Container c, String owner, String logInfo) {
        Log.print(getStyle(), owner, "弹窗信息->" + logInfo, c);
        String title = getTitle(owner);
        String s = CTOptionPane.showInputDialog(c, title, logInfo, getIcon(),
                false);
        Log.print(getStyle(), owner, "输入信息->" + s, c);
        return s;
    }

    public int showChooseDialog(Container c, String owner, String logInfo) {
        Log.print(getStyle(), owner, "弹窗信息->" + logInfo, c);
        String title = getTitle(owner);
        int i = CTOptionPane.showConfirmDialog(c, title, logInfo, getIcon(), false);
        String s ;
        if (i == CTOptionPane.YES_OPTION) {
            s = "是";
        } else if (i == CTOptionPane.NO_OPTION) {
            s = "否";
        }else {
            s = "取消";
        }

        Log.print(getStyle(), owner, "输入信息->" + s, c);
        return i;
    }
}
