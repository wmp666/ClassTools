package com.wmp.classTools.CTComponent;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.UITools.CTColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class CTTextField extends JTextField {


    public CTTextField() {
        this("");
    }


    public CTTextField(String text) {

        if (text != null) {
            this.setText(text);
        }
        this.setCaretColor(CTColor.textColor);

        this.setBorder(CTBorderFactory.BASIC_LINE_BORDER);//设置按钮的边框 - 5px 实线
        this.setBackground(CTColor.backColor);
        this.setForeground(CTColor.textColor);
        this.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, (int) (15 * CTInfo.dpi)));
        JTextField textField = this;
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(CTBorderFactory.FOCUS_GAINTED_BORDER);
            }

            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(CTBorderFactory.BASIC_LINE_BORDER);
            }
        });
    }
}
