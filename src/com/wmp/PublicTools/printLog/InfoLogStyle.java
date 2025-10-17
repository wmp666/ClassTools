package com.wmp.PublicTools.printLog;

import com.wmp.Main;
import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.UITools.GetIcon;
import com.wmp.classTools.CTComponent.CTOptionPane;
import com.wmp.classTools.CTComponent.LoadingDialog;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class InfoLogStyle extends PrintLogStyle {

    public final LoadingDialog loadingDialog = new LoadingDialog();

    public InfoLogStyle(LogStyle style) {
        super(style);
    }

    public void systemPrint(String owner, String logInfo) {
        Log.systemPrint(LogStyle.INFO, owner, logInfo);
    }

    public void message(Container c, String owner, String logInfo) {
        Log.print(getStyle(), owner, "弹窗信息->" + logInfo, c);
        String title = getTitle(owner);
        CTOptionPane.showMessageDialog(c, title, logInfo, getIcon(), CTOptionPane.INFORMATION_MESSAGE, true);
    }

    private static String getTitle(String owner) {
        String title;
        if (CTInfo.isError) title = "骇客已入侵";
        else title = owner;
        return title;
    }

    private static Icon getIcon() {
        if (CTInfo.isError) return GetIcon.getIcon(Main.class.getResource("/image/error/icon.png"), 100, 100);
        return null;
    }

    public String showInputDialog(Container c, String owner, String logInfo) {
        Log.print(getStyle(), owner, "弹窗信息->" + logInfo, c);
        String title = getTitle(owner);
        String s = CTOptionPane.showInputDialog(c, title, logInfo, getIcon(),
                true);
        Log.print(getStyle(), owner, "输入信息->" + s, c);
        return s;
    }

    /**
     * 显示选择输入对话框
     *
     * @param owner   对话框的父组件
     * @param logInfo 显示的消息
     * @param choices 显示的选项
     * @return 0-选择的选项  1-用户输入的字符串
     */

    public String[] showInputDialog(Container c, String owner, String logInfo, String... choices) {
        Log.print(getStyle(), owner, "弹窗信息->" + logInfo, c);
        String title = getTitle(owner);
        String[] ss = CTOptionPane.showConfirmInputDialog(c, title, logInfo, getIcon(),
                true, choices);
        Log.print(getStyle(), owner, "输入信息->" + Arrays.toString(ss), c);
        return ss;
    }

    public int showChooseDialog(Container c, String owner, String logInfo) {
        Log.print(getStyle(), owner, "弹窗信息->" + logInfo, c);
        String title = getTitle(owner);
        int i = CTOptionPane.showConfirmDialog(c, title, logInfo, getIcon(), true);
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

    public String showChooseDialog(Container c, String owner, String logInfo, String... choices) {
        Log.print(getStyle(), owner, "弹窗信息->" + logInfo, c);
        String title = getTitle(owner);
        String s = CTOptionPane.showConfirmDialog(c, title, logInfo, getIcon(), true, choices);

        Log.print(getStyle(), owner, "输入信息->" + s, c);
        return s;
    }
}
