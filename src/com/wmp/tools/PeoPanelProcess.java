package com.wmp.tools;

import java.util.ArrayList;

public class PeoPanelProcess {
    public static Object[] getPeopleName(ArrayList<String> array)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");

        int size = array.size();
        int index = (size + 2) / 3;  // 修正组数计算逻辑

        for (int i = 0; i < index; i++) {
            int base = i * 3;
            sb.append(array.get(base));

            if (base + 1 < size) {
                sb.append(", ").append(array.get(base + 1));
            }
            if (base + 2 < size) {
                sb.append(", ").append(array.get(base + 2));
            }

            if (i < index - 1) {  // 仅在非最后一组后添加换行
                sb.append("<br>");
            }
        }

        sb.append("</html>");
        return new Object[]{sb.toString(), index};
    }
}
