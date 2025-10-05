package com.wmp.classTools.CTComponent;

import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;

import javax.swing.*;
import java.awt.*;

public class CTTable extends JTable {
    public CTTable() {
        super();
        this.getTableHeader().setReorderingAllowed(false);// 列不允许拖动
        this.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));
        this.setRowHeight(CTFont.getSize(CTFontSizeStyle.SMALL));
        this.getTableHeader().setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));

        this.setCellEditor(new DefaultCellEditor(new CTTextField()));
    }


}
