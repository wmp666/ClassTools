package com.wmp.classTools.importPanel.eastereggtext;

import com.wmp.Main;
import com.wmp.PublicTools.EasterEgg.EETextStyle;
import com.wmp.PublicTools.EasterEgg.EasterEgg;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.UITools.GetMaxSize;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

public class ETPanel extends CTPanel {

    private JLabel label;

    public ETPanel() {
        this.setLayout(new BorderLayout());
        this.setName("ETPanel");
        this.setID("ETPanel");
        this.setOpaque(false);

        {
            String text = EasterEgg.getText(EETextStyle.HTML);
            label = new JLabel(text);
            label.setForeground(CTColor.textColor);
            label.setBackground(CTColor.backColor);
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

        }


        //刷新
        new Thread(() -> {
            while (true) {
                this.removeAll();

                if (Main.disPanelList.contains(getID())) {
                    continue;
                }

                String text = EasterEgg.getText(EETextStyle.HTML);
                label.setText(text);
                label.setForeground(CTColor.mainColor);
                label.setBackground(CTColor.backColor);
                label.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.NORMAL));


                int[] maxSize = GetMaxSize.getMaxSize(text, GetMaxSize.STYLE_HTML);
                // 根据文字数量调整窗口大小
                int lineCount = maxSize[1];
                int maxLength = maxSize[0];

                // 计算新的窗口尺寸（基础尺寸 + 动态调整）
                int newWidth = maxLength * label.getFont().getSize(); // 每个字符约20像素宽度
                int newHeight = lineCount * label.getFont().getSize() + 5;  // 每多一行增加30像素高度

                int maxShowHeight = 4 * label.getFont().getSize() + 5;
                int maxShowWidth = 16 * label.getFont().getSize();
                int minShowWidth = 13 * label.getFont().getSize();

                long waitTime = Math.max(10000, text.replaceAll("<html>|</html>|<br>", "").length() * 100L);
                // 设置窗口大小

                if (newWidth >= maxShowWidth) {
                    newWidth = maxShowWidth;
                } else if (newWidth < minShowWidth) {
                    newWidth = minShowWidth;
                }
                if (newHeight >= maxShowHeight) {
                    newHeight = maxShowHeight;
                }
                JScrollPane scrollPane = new JScrollPane(label);
                scrollPane.getViewport().setOpaque(false);
                scrollPane.setOpaque(false);
                scrollPane.setBorder(BorderFactory.createEmptyBorder());

                this.add(scrollPane, BorderLayout.CENTER);
                System.out.printf("大小:%s|%s\n", newWidth, newHeight + 20);
                scrollPane.setPreferredSize(new Dimension(newWidth, newHeight + 20));


                this.revalidate();
                this.repaint();

                Log.info.print("ETPanel", String.format("内容: %s | 等待时间: %s 毫秒", text, waitTime));

                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }).start();
    }


    @Override
    public void refresh() throws IOException {
        label.setForeground(CTColor.mainColor);

        this.revalidate();
        this.repaint();
    }
}
