package com.wmp.classTools.importPanel.eastereggtext;

import com.wmp.PublicTools.CTColor;
import com.wmp.PublicTools.EasterEgg.EETextStyle;
import com.wmp.PublicTools.EasterEgg.EasterEgg;
import com.wmp.classTools.CTComponent.CTPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

public class EEPanel extends CTPanel {
    public EEPanel(int lastPanelY){
        this.setLayout(new GridLayout(1,1));

        this.setNextPanelY(lastPanelY);

        {
            String text = EasterEgg.getText(EETextStyle.HTML);
            JLabel label = new JLabel(text);
            label.setForeground(CTColor.textColor);
            label.setBackground(CTColor.backColor);
            label.setFont(new Font("微软雅黑", Font.BOLD, 16));
            this.add(label);

            // 根据文字数量调整窗口大小
            String plainText = text.replaceAll("<html>|</html>", "").replaceAll("<br>", "\n"); // 去除HTML标签
            String[] lines = plainText.split("\n"); // 按换行符分割
            int lineCount = lines.length;
            // 计算最长行的长度 .mapToInt(String::length) 将每个字符串映射为其长度，然后使用 max() 方法找到最大值
            //.orElse(0) 如果数组为空，返回默认值 0
            int maxLength = Arrays.stream(lines).mapToInt(String::length).max().orElse(0);

            // 计算新的窗口尺寸（基础尺寸 + 动态调整）
            int baseWidth = 350;
            int baseHeight = 200;
            int newWidth = Math.max(baseWidth, maxLength * 20 + 200); // 每个字符约15像素宽度
            int newHeight = Math.max(baseHeight, lineCount * 20);  // 每多一行增加30像素高度

            // 设置窗口大小
            this.setSize(newWidth, newHeight);

            this.appendNextPanelY(newHeight);
        }


        new Thread(()->{
            while (true){
                this.removeAll();

                String text = EasterEgg.getText(EETextStyle.HTML);
                JLabel label = new JLabel(text);
                label.setForeground(CTColor.textColor);
                label.setBackground(CTColor.backColor);
                label.setFont(new Font("微软雅黑", Font.BOLD, 20));
                this.add(label, BorderLayout.CENTER);

                // 根据文字数量调整窗口大小
                String plainText = text.replaceAll("<html>|</html>", "").replaceAll("<br>", "\n"); // 去除HTML标签
                String[] lines = plainText.split("\n"); // 按换行符分割
                int lineCount = lines.length;
                // 计算最长行的长度 .mapToInt(String::length) 将每个字符串映射为其长度，然后使用 max() 方法找到最大值
                //.orElse(0) 如果数组为空，返回默认值 0
                int maxLength = Arrays.stream(lines).mapToInt(String::length).max().orElse(0);

                // 计算新的窗口尺寸（基础尺寸 + 动态调整）
                int newWidth = maxLength * 20; // 每个字符约15像素宽度
                int newHeight = lineCount * 30;  // 每多一行增加30像素高度

                // 设置窗口大小
                this.setSize(newWidth, newHeight);

                this.revalidate();
                this.repaint();

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public EEPanel() {


    }
    @Override
    public void refresh() throws IOException {

    }
}
