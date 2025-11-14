package com.wmp.classTools.CTComponent.CTButton;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.UITools.GetIcon;
import com.wmp.PublicTools.appFileControl.IconControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;

public class CTRoundTextButton extends CTButton {

    public CTRoundTextButton(String text) {
        this(text, null, IconControl.COLOR_DEFAULT);
    }

    public CTRoundTextButton(String text, String iconKey, int iconStyle) {
        this(text, iconKey, iconStyle, 35, 35);
    }

    public CTRoundTextButton(String text, String iconKey, int iconStyle, int width, int height) {
        super();

        this.setText(text);
        setName(text);


        if (iconKey != null) {
            this.setIcon(GetIcon.getIcon(iconKey, iconStyle, width, height));
        }


        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        this.setOpaque(false);

        this.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));
        this.setBackground(CTColor.backColor);
        this.setForeground(CTColor.textColor);

        JButton button = this;
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                button.setBackground(new Color(218, 218, 218));
                button.setForeground(Color.BLACK);
            }

            @Override
            public void focusLost(FocusEvent e) {
                button.setBackground(CTColor.backColor);
                button.setForeground(CTColor.textColor);
            }
        });

        this.addChangeListener(e -> {
            ButtonModel model = button.getModel();
            if (model.isPressed()) {//鼠标按下
                button.setForeground(Color.BLACK);
                button.setBackground(new Color(179, 179, 179));
            } else if (model.isRollover()) {//鼠标移入
                button.setForeground(Color.BLACK);
                button.setBackground(new Color(218, 218, 218));
            } else {
                button.setForeground(CTColor.textColor);
                button.setBackground(CTColor.backColor);
            }
        });

    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // 绘制背景
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Float(0, 0, width, height, CTInfo.arcw, CTInfo.arch));

        int iconX = 0, iconY = 0, textX = 0, textY = 0;
        int horizontalSpacing = 5; // 图标与文字之间的间距

        // 水平左右对齐布局
        if (this.getIcon() != null && getText() != null && !getText().isEmpty()) {
            // 同时有图标和文字时，水平排列
            int totalWidth = this.getIcon().getIconWidth() + horizontalSpacing + getTextWidth(g2, getText());
            int startX = (width - totalWidth) / 2; // 整体居中

            iconX = startX;
            iconY = (height - this.getIcon().getIconHeight()) / 2;

            textX = startX + this.getIcon().getIconWidth() + horizontalSpacing;
            textY = (height + g2.getFontMetrics().getAscent() - g2.getFontMetrics().getDescent()) / 2;
        } else if (this.getIcon() != null) {
            // 只有图标时居中
            iconX = (width - this.getIcon().getIconWidth()) / 2;
            iconY = (height - this.getIcon().getIconHeight()) / 2;
        } else if (getText() != null && !getText().isEmpty()) {
            // 只有文字时居中
            FontMetrics fm = g2.getFontMetrics();
            textX = (width - fm.stringWidth(getText())) / 2;
            textY = (height + fm.getAscent() - fm.getDescent()) / 2;
        }

        // 绘制图标
        if (this.getIcon() != null) {
            g2.drawImage(((ImageIcon) this.getIcon()).getImage(), iconX, iconY, this.getIcon().getIconWidth(), this.getIcon().getIconHeight(), null);
        }

        // 绘制文本
        if (getText() != null && !getText().isEmpty()) {
            g2.setColor(getForeground());
            g2.setFont(getFont());
            g2.drawString(getText(), textX, textY);
        }

        g2.dispose();
    }

    // 辅助方法：获取文本宽度
    private int getTextWidth(Graphics2D g2, String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        return g2.getFontMetrics().stringWidth(text);
    }


}
