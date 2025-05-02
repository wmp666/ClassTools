package com.wmp.PublicTools.printLog;

import javax.swing.*;
import java.awt.*;

public class InfoLogStyle extends PrintLogStyle {



    public InfoLogStyle(LogStyle style) {
        super(style);
    }

    public void message(Container c, String owner, String logInfo) {
        Log.print(getStyle(), owner, "弹窗信息->" + logInfo, c);
        JOptionPane.showMessageDialog(c, logInfo, "提示", JOptionPane.INFORMATION_MESSAGE);
    }

    public String input(Container c, String owner, String logInfo) {
        Log.print(getStyle(), owner, "弹窗信息->" + logInfo, c);
        String s = JOptionPane.showInputDialog(c, logInfo, "输入", JOptionPane.QUESTION_MESSAGE);
        Log.print(getStyle(), owner, "输入信息->" + s, c);
        return s;
    }

    public int inputInt(Container c, String owner, String logInfo) {
        Log.print(getStyle(), owner, "弹窗信息->" + logInfo, c);
        int i = JOptionPane.showConfirmDialog(c, logInfo, "输入", JOptionPane.YES_NO_OPTION);
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
