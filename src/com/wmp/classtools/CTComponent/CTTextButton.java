package com.wmp.classTools.CTComponent;

import com.wmp.PublicTools.CTInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class CTTextButton extends JButton {


    public CTTextButton(String text) {
        this(text, null);
    }

    public CTTextButton(String text, Icon icon) {
        this(text, icon, true);
    }

    public CTTextButton(String text, boolean showBorder) {
        this(text, null, showBorder);
    }

    public CTTextButton(String text, Icon icon, boolean showBorder) {
        this.setContentAreaFilled(false);
        this.setBorderPainted(showBorder);
        this.setBorder(CTBorderFactory.BASIC_LINE_BORDER);

        if (icon != null) {
            this.setIcon(icon);
        }
        this.setText(text);
        this.setBackground(new Color(255, 255, 255));
        this.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, (int) (20 * CTInfo.dpi)));
        this.setFocusPainted(false);
        this.setOpaque(true);

        JButton button = this;
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                button.setBorder(CTBorderFactory.FOCUS_GAINTED_BORDER);
                button.setBackground(new Color(218, 218, 218));
            }

            @Override
            public void focusLost(FocusEvent e) {
                button.setBorder(CTBorderFactory.BASIC_LINE_BORDER);
                button.setBackground(new Color(255, 255, 255));
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

