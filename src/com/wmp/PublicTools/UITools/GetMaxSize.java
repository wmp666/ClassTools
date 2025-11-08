package com.wmp.PublicTools.UITools;

import com.wmp.PublicTools.CTInfo;

import java.awt.*;
import java.util.Arrays;

public class GetMaxSize {
    public static int STYLE_HTML = 0;
    public static int STYLE_PLAIN = 1;

    /**
     * 获取文本的最大行数和最大长度
     *
     * @param s     文本
     * @param style 文本样式 0:HTML 1:PLAIN
     * @return 每行中最长的字数, 行数
     */
    public static int[] getMaxSize(String s, int style) {

        return new int[]{getMaxLength(s, style), getLine(s, style)};
    }

    public static int getLine(String s, int style) {
        String temp;
        if (style == STYLE_HTML) {
            temp = s.replaceAll("<html>|</html>", "").replaceAll("<br>", "\n"); // 去除HTML标签
        } else {
            temp = s;
        }
        return temp.split("\n").length;
    }

    public static int getMaxLength(String s, int style) {
        String temp;
        if (style == STYLE_HTML) {
            temp = s.replaceAll("<html>|</html>", "").replaceAll("<br>", "\n"); // 去除HTML标签
        } else {
            temp = s;
        }

        // 计算最长行的长度.mapToInt(String::length) 将每个字符串映射为其长度，然后使用 max() 方法找到最大值
        //.orElse(0) 如果数组为空，返回默认值 0
        String[] lines = temp.split("\n|\\\\n");
        return Arrays.stream(lines).mapToInt(String::length).max().orElse(0);
    }

    public static int getHTMLToTextMaxLength(String s, FontMetrics fm) {
        String[] strings = s.replaceAll("<html>|</html>", "").replaceAll("<br>", "\n").split("\n");// 去除HTML标签
        int maxLength = 12 *  fm.getFont().getSize();
        for (String string : strings) {
            maxLength = Math.min(fm.stringWidth(string), maxLength);
        }
        return maxLength;

    }
}
