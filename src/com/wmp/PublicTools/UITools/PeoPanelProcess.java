package com.wmp.PublicTools.UITools;

import java.util.ArrayList;

public class PeoPanelProcess {
    /**
     * 用于获取值日人员的姓名
     *
     * @param array 人员姓名数组
     * @return 人员姓名, 行数, 最大长度
     */
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
}
