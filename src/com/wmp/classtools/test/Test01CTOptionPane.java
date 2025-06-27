package com.wmp.classTools.test;

import com.wmp.Main;
import com.wmp.PublicTools.UITools.GetIcon;
import com.wmp.classTools.CTComponent.CTOptionPane;

import javax.swing.*;
import java.util.Arrays;

public class Test01CTOptionPane {
    public static void main(String[] args) {
        try {
            //使用系统UI
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        CTOptionPane.showMessageDialog(null, "测试", "测试文本——酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷为如此llllllllllllllllllllllllllllllllll二万人次人文传统测温额外v他认为而维持特认为测", null, CTOptionPane.INFORMATION_MESSAGE, true);

        System.out.println("窗口1关闭");

        int i = CTOptionPane.showConfirmDialog(null, "测试", "测试文本", GetIcon.getIcon(Main.class.getResource("/image/error/icon.png"), 50, 50), true);
        if (i == CTOptionPane.YES_OPTION) {
            System.out.println("是");
        } else if (i == CTOptionPane.NO_OPTION) {
            System.out.println("否");
        } else {
            System.out.println("取消");
        }
        System.out.println("窗口2关闭");

        String s = CTOptionPane.showInputDialog(null, "测试", "测试文本", null, true);
        System.out.println("输入信息->" + s);
        System.out.println("窗口3关闭");

        String[] ss = CTOptionPane.showConfirmInputDialog(null, "测试", "测试文本", null, true, "1", "2", "3");
        System.out.println("输入信息->" + Arrays.toString(ss));
        System.out.println("窗口4关闭");

        String s1 = CTOptionPane.showConfirmDialog(null, "测试", "测试文本", null, true, "1", "2", "3");
        System.out.println("输入信息->" + s1);
        System.out.println("窗口5关闭");
    }
}
