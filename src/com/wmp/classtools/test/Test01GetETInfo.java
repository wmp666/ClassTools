package com.wmp.classTools.test;

import com.wmp.PublicTools.EasterEgg.EasterEgg;

public class Test01GetETInfo {
    public static void main(String[] args) {
        int max = 0;
        int line = 0;
        String[] text = EasterEgg.getAllText();
        for (int i = 0; i < text.length; i++) {
            //统计text[i]中\n的数量

            int enterLength = text[i].split("\\\\n").length;
            System.out.printf("行数:%s | 换行数量:%s | 内容:%s\n", i + 1, enterLength, text[i]);
            if (max < enterLength) {
                line = i + 1;
                max = enterLength;
            }
        }
        System.out.printf("对应行数:%s|最大行数:%s", line, max);
    }
}
