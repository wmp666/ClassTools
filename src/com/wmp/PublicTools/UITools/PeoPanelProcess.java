package com.wmp.PublicTools.UITools;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PeoPanelProcess {
    /**
     * 用于获取值日人员的姓名
     *
     * @param array 人员姓名数组
     * @return 人员姓名, 行数, 最大长度
     */
    private static Object[] getPeopleName(List<String> array) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");

        int size = array.size();
        int index = (size + 2) / 3;  // 修正组数计算逻辑

        for (int i = 0; i < index; i++) {
            int base = i * 3;
            sb.append(array.get(base));

            if (base + 1 < size) {
                sb.append("，").append(array.get(base + 1));
            }
            if (base + 2 < size) {
                sb.append("，").append(array.get(base + 2));
            }

            if (i < index - 1) {  // 仅在非最后一组后添加换行
                sb.append("<br>");
            }
        }

        sb.append("</html>");

        int maxLength = GetMaxSize.getMaxLength(sb.toString(), GetMaxSize.STYLE_HTML);

        return new Object[]{sb.toString(), index, maxLength};
    }

    public static JScrollPane getShowPeoPanel(List<String> peo) {

        JLabel personLabel = new JLabel();

        Object[] objects = getPeopleName(peo);

        personLabel.setText(String.valueOf(objects[0]));
        personLabel.setFont(new Font("微软雅黑", Font.BOLD, 23));
        personLabel.setForeground(CTColor.mainColor);

        JScrollPane scrollPane = new JScrollPane(personLabel);

        int[] maxSize = GetMaxSize.getMaxSize(objects[0].toString(), GetMaxSize.STYLE_HTML);
        // 根据文字数量调整窗口大小
        int lineCount = maxSize[1];// 行数
        int maxLength = maxSize[0];// 最大长度

        // 计算新的窗口尺寸（基础尺寸 + 动态调整）
        int newWidth = maxLength * 23; // 每个字符约23像素宽度
        int newHeight = lineCount * 30;  // 每多一行增加30像素高度

        int maxShowHeight = 4 * 23; // 最大显示高度

        // 设置窗口大小
        if (newHeight >= maxShowHeight) {
            newHeight = maxShowHeight;
        }
        scrollPane.getViewport().setBackground(CTColor.backColor);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        scrollPane.setPreferredSize(new Dimension(newWidth + 18, newHeight + 18));

        return scrollPane;
    }
}
