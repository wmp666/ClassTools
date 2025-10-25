package com.wmp.classTools.CTComponent.CTButton;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.classTools.CTComponent.CTBorderFactory;

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
        this.setForeground(CTColor.textColor);
        this.setFocusPainted(false);
        this.setOpaque(false);

        JButton button = this;
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                button.setOpaque(true);
                button.setBorder(CTBorderFactory.FOCUS_GAINTED_BORDER);
                button.setBackground(new Color(218, 218, 218));
                button.setForeground(Color.BLACK);
            }

            @Override
            public void focusLost(FocusEvent e) {
                button.setOpaque(false);
                button.setBorder(CTBorderFactory.BASIC_LINE_BORDER);
                button.setBackground(new Color(255, 255, 255));
                button.setForeground(CTColor.textColor);
            }
        });

        this.addChangeListener(e -> {
            ButtonModel model = button.getModel();
            if (model.isPressed()) {//鼠标按下
                button.setOpaque(true);
                button.setForeground(Color.BLACK);
                button.setBackground(new Color(179, 179, 179));
            } else if (model.isRollover()) {//鼠标移入
                button.setOpaque(true);
                button.setForeground(Color.BLACK);
                button.setBackground(new Color(218, 218, 218));
            } else {
                button.setOpaque(false);
                button.setForeground(CTColor.textColor);
                button.setBackground(new Color(255, 255, 255));
            }
        });
    }
}

