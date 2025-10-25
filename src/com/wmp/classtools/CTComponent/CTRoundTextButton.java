package com.wmp.classTools.CTComponent;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;

public class CTRoundTextButton extends JButton {


    private Icon defaultIcon;
    private Image defaultIconImage;

    public CTRoundTextButton(String text) {
        this(text, null);
    }

    public CTRoundTextButton(String text, Icon icon) {
        super(text, icon);

        setName(text);


        if (icon != null) {
            this.defaultIcon = icon;
            this.defaultIconImage = ((ImageIcon) icon).getImage();

            this.setIcon(defaultIcon);
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
                button.setOpaque(true);
                button.setBackground(new Color(218, 218, 218));
                button.setForeground(Color.BLACK);
            }

            @Override
            public void focusLost(FocusEvent e) {
                button.setOpaque(false);
                button.setBackground(CTColor.backColor);
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
        if (defaultIconImage != null && getText() != null && !getText().isEmpty()) {
            // 同时有图标和文字时，水平排列
            int totalWidth = defaultIcon.getIconWidth() + horizontalSpacing + getTextWidth(g2, getText());
            int startX = (width - totalWidth) / 2; // 整体居中

            iconX = startX;
            iconY = (height - defaultIcon.getIconHeight()) / 2;

            textX = startX + defaultIcon.getIconWidth() + horizontalSpacing;
            textY = (height + g2.getFontMetrics().getAscent() - g2.getFontMetrics().getDescent()) / 2;
        } else if (defaultIconImage != null) {
            // 只有图标时居中
            iconX = (width - defaultIcon.getIconWidth()) / 2;
            iconY = (height - defaultIcon.getIconHeight()) / 2;
        } else if (getText() != null && !getText().isEmpty()) {
            // 只有文字时居中
            FontMetrics fm = g2.getFontMetrics();
            textX = (width - fm.stringWidth(getText())) / 2;
            textY = (height + fm.getAscent() - fm.getDescent()) / 2;
        }

        // 绘制图标
        if (defaultIconImage != null) {
            g2.drawImage(defaultIconImage, iconX, iconY, defaultIcon.getIconWidth(), defaultIcon.getIconHeight(), null);
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
