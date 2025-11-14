package com.wmp.classTools.CTComponent.CTButton;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.UITools.GetIcon;
import com.wmp.PublicTools.appFileControl.IconControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;
import java.net.MalformedURLException;

public class CTIconButton extends CTButton implements ActionListener {

    private final String text;
    private Runnable callback;


    public CTIconButton() {
        this("null", "null", IconControl.COLOR_DEFAULT, null);
    }

    //图标 正方形
    public CTIconButton(String name, int iconStyle, Runnable callback) {

        this(null, name, iconStyle, callback);

    }

    //文字 图标 正方形

    public CTIconButton(String text, String name, int iconStyle, Runnable callback) {
        this.text = text;
        this.setToolTipText(text);

        this.setIconName(name);
        this.setIconStyle(iconStyle);

        setName(text);

        //super(text);

        if (name != null) {

            this.setIcon(GetIcon.getIcon(name, iconStyle, 35, 35));

            this.setSize(getIcon().getIconWidth(), getIcon().getIconHeight());
        }


        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        this.setOpaque(false);

        this.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.MORE_SMALL));
        this.setBackground(CTColor.backColor);
        this.setForeground(CTColor.textColor);

        this.callback = callback;

        this.addActionListener(this);

        JButton button = this;
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                button.setBackground(new Color(218, 218, 218));
            }

            @Override
            public void focusLost(FocusEvent e) {
                button.setBackground(CTColor.backColor);
            }
        });

        this.addChangeListener(e -> {
            ButtonModel model = button.getModel();
            if (model.isPressed()) {//鼠标按下
                button.setBackground(new Color(179, 179, 179));
            } else if (model.isRollover()) {//鼠标移入
                button.setBackground(new Color(218, 218, 218));
            } else {
                button.setBackground(CTColor.backColor);
            }
        });
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;

    }

    public CTIconButton copy() throws MalformedURLException {
        CTIconButton ctIconButton = new CTIconButton();
        ctIconButton.setIcon(this.getIcon());
        ctIconButton.setToolTipText(this.text);
        ctIconButton.setCallback(this.callback);
        return ctIconButton;
    }

    public CTTextButton toTextButton(boolean showBorder) {
        CTTextButton button = new CTTextButton(this.text, this.getIconName(), this.getIconStyle(), showBorder);
        button.addActionListener(this);
        return button;
    }

    public CTRoundTextButton toRoundTextButton() {
        CTRoundTextButton button = new CTRoundTextButton(this.text, this.getIconName(), this.getIconStyle());
        button.addActionListener(this);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.isEnabled() && callback != null) {
            callback.run();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int tempX = (width - this.getIcon().getIconWidth()) / 2;
        int tempY = (height - this.getIcon().getIconHeight()) / 2;

        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Float(tempX, tempY, this.getIcon().getIconWidth(), this.getIcon().getIconHeight(), CTInfo.arcw, CTInfo.arch));

        // 绘制图标（如果存在）
        if (this.getIcon() != null) {


            g2.drawImage(((ImageIcon) this.getIcon()).getImage(), tempX, tempY, this.getIcon().getIconWidth(), this.getIcon().getIconHeight(), null);
        }

        // 绘制文本
        if (getText() != null && !getText().isEmpty()) {
            FontMetrics fontMetrics = g2.getFontMetrics();
            Rectangle stringBounds = fontMetrics.getStringBounds(this.getText(), g2).getBounds();
            int textX = (width - stringBounds.width) / 2;
            int textY = (height - stringBounds.height) / 2 + fontMetrics.getAscent();
            g2.setColor(getForeground());
            g2.setFont(getFont());
            g2.drawString(getText(), textX, textY);
        }

        g2.dispose();
    }
}
