package com.wmp.PublicTools.printLog;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.UITools.GetIcon;
import com.wmp.PublicTools.appFileControl.IconControl;
import com.wmp.classTools.CTComponent.CTOptionPane;
import com.wmp.classTools.CTComponent.LoadingDialog;

import javax.swing.*;
import java.awt.*;

public class WarnLogStyle extends PrintLogStyle {

    public final LoadingDialog loading = new LoadingDialog();

    public WarnLogStyle(LogStyle style) {
        super(style);
    }

    private static String getTitle(String owner) {
        String title;
        if (CTInfo.isError) title = "骇客已入侵";
        else title = owner;
        return title;
    }

    private static Icon getIcon() {
        if (CTInfo.isError) return GetIcon.getIcon("图标", IconControl.COLOR_DEFAULT, 100, 100);
        return null;
    }

    public void systemPrint(String owner, String logInfo) {
        Log.systemPrint(LogStyle.WARN, owner, logInfo);
    }

    public void message(Container c, String owner, String logInfo) {
        Log.print(getStyle(), owner, "弹窗信息->" + logInfo, c);
        String title = getTitle(owner);
        CTOptionPane.showMessageDialog(c, title, logInfo, getIcon(), CTOptionPane.WARNING_MESSAGE, true);
    }

    public int showChooseDialog(Container c, String owner, String logInfo) {
        Log.print(getStyle(), owner, "弹窗信息->" + logInfo, c);
        String title = getTitle(owner);
        int i = CTOptionPane.showConfirmDialog(c, title, logInfo, getIcon(), CTOptionPane.WARNING_MESSAGE, true);
        String s;
        if (i == CTOptionPane.YES_OPTION) {
            s = "是";
        } else if (i == CTOptionPane.NO_OPTION) {
            s = "否";
        } else {
            s = "取消";
        }

        Log.print(getStyle(), owner, "输入信息->" + s, c);
        return i;
    }

    public String showChooseDialog(Container c, String owner, String logInfo, String... choices) {
        Log.print(getStyle(), owner, "弹窗信息->" + logInfo, c);
        String title = getTitle(owner);
        String s = CTOptionPane.showConfirmDialog(c, title, logInfo, getIcon(), CTOptionPane.WARNING_MESSAGE, true, choices);

        Log.print(getStyle(), owner, "输入信息->" + s, c);
        return s;
    }

}
