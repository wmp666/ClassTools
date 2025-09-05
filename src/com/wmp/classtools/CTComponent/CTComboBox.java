package com.wmp.classTools.CTComponent;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class CTComboBox extends JComboBox<String> {
    public CTComboBox() {
        super();


        this.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {//创建按钮 - 箭头按钮

                CTTextButton button = new CTTextButton("v");
                button.setBorderPainted(false);

                return button;
            }

        });


        this.setBorder(CTOptionPane.BASIC_LINE_BORDER);//设置按钮的边框 - 5px 实线
        this.setBackground(new Color(255, 255, 255));
        this.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
        JComboBox<String> comboBox = this;
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                comboBox.setBorder(CTOptionPane.FOCUS_GAINTED_BORDER);
            }

            @Override
            public void focusLost(FocusEvent e) {
                comboBox.setBorder(CTOptionPane.BASIC_LINE_BORDER);
            }
        });
    }

    public void addItems(String... items) {
        for (String item : items) {
            this.addItem(item);
        }
    }
}
