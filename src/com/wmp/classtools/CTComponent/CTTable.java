package com.wmp.classTools.CTComponent;

import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;

import javax.swing.*;
import java.awt.*;

public class CTTable extends JTable {

    public CTTable() {
        this(new Object[][]{}, new Object[]{});
    }

    public CTTable(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
        this.getTableHeader().setReorderingAllowed(false);// 列不允许拖动
        this.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));
        this.setForeground(CTColor.textColor);
        this.setBackground(CTColor.backColor);
        this.setRowHeight(CTFont.getSize(CTFontSizeStyle.SMALL));
        this.getTableHeader().setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));

        this.setCellEditor(new DefaultCellEditor(new CTTextField()));
    }

    public Object[][] getData() {
        int rowCount = this.getRowCount();
        int columnCount = this.getColumnCount();
        Object[][] data = new Object[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                data[i][j] = this.getValueAt(i, j);
            }
        }
        return data;
    }

}
