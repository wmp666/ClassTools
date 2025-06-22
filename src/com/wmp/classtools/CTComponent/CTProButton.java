package com.wmp.classTools.CTComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class CTProButton extends JButton {


    public CTProButton(String text) {
        this(text, null);
    }

    public CTProButton(String text, Icon icon) {
        this.setContentAreaFilled(false);
        this.setBorder(CTOptionPane.BASIC_LINE_BORDER);

        if (icon != null) {
            this.setIcon(icon);
        }
        this.setText(text);
        this.setBackground(new Color(255, 255, 255));
        this.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
        this.setFocusPainted(false);
        this.setOpaque(true);

        JButton button = this;
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                button.setBorder(CTOptionPane.FOCUS_GAINTED_BORDER);
            }

            @Override
            public void focusLost(FocusEvent e) {
                button.setBorder(CTOptionPane.BASIC_LINE_BORDER);
            }
        });

        this.addChangeListener(e -> {
            ButtonModel model = button.getModel();
            if (model.isPressed()) {//鼠标按下
                button.setBackground(new Color(179, 179, 179));
            } else if (model.isRollover()) {//鼠标移入
                button.setBackground(new Color(218, 218, 218));
            } else {
                button.setBackground(new Color(255, 255, 255));
            }
        });
    }
}
