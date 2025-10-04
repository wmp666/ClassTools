package com.wmp.classTools.CTComponent;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class CTBorderFactory {

    public static Border BASIC_LINE_BORDER = BorderFactory.createLineBorder(new Color(200, 200, 200), (int) (2 * CTInfo.dpi));
    public static Border FOCUS_GAINTED_BORDER = BorderFactory.createLineBorder(new Color(112, 112, 112), (int) (2 * CTInfo.dpi));

    public static Border createTitledBorder(String title) {
        return createTitledBorder(title, TitledBorder.LEFT, TitledBorder.TOP);
    }

    /**
     * 创建带标题的边框
     *
     * @param title              标题
     * @param titleJustification 对齐方式 -- 以下之一:
     *                           <ul>
     *                           <li><code>TitledBorder.LEFT</code>
     *                           <li><code>TitledBorder.CENTER</code>
     *                           <li><code>TitledBorder.RIGHT</code>
     *                           <li><code>TitledBorder.LEADING</code>
     *                           <li><code>TitledBorder.TRAILING</code>
     *                           <li><code>TitledBorder.DEFAULT_JUSTIFICATION</code> (leading)
     *                           </ul>
     *                            @param titlePosition    对其位置    -- 以下之一:
     *                           <ul>
     *                           <li><code> TitledBorder.ABOVE_TOP</code>
     *                           <li><code>TitledBorder.TOP</code> (坐在顶线)
     *                           <li><code>TitledBorder.BELOW_TOP</code>
     *                           <li><code>TitledBorder.ABOVE_BOTTOM</code>
     *                           <li><code>TitledBorder.BOTTOM</code> (坐在底线)
     *                           <li><code>TitledBorder.BELOW_BOTTOM</code>
     *                           <li><code>TitledBorder.DEFAULT_POSITION</code> (标题位置
     *                           由当前外观决定)
     *                           </ul>
     */
    public static Border createTitledBorder(String title, int titleJustification, int titlePosition) {
        return BorderFactory.createTitledBorder(BASIC_LINE_BORDER, title, titleJustification, titlePosition, CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));
    }
}
