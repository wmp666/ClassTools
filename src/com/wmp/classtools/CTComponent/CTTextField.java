package com.wmp.classTools.CTComponent;

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

        this.setBorder(CTOptionPane.BASIC_LINE_BORDER);//设置按钮的边框 - 5px 实线
        this.setBackground(new Color(255, 255, 255));
        this.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
        JTextField textField = this;
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(CTOptionPane.FOCUS_GAINTED_BORDER);
            }

            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(CTOptionPane.BASIC_LINE_BORDER);
            }
        });
    }
}
