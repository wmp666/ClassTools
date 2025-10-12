package com.wmp.classTools.CTComponent;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.UITools.GetIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;
import java.net.MalformedURLException;
import java.net.URL;

public class CTIconButton extends JButton implements ActionListener {

    private Runnable callback;
    private Icon defaultIcon;
    private Image defaultIconImage;
    private String defaultIconPath;
    private String text;

    public CTIconButton() {
    }

    //图标 正方形
    public CTIconButton(String defaultIconPath, Runnable callback) throws MalformedURLException {

        this(null, defaultIconPath, callback);

    }

    //文字 图标 正方形

    public CTIconButton(String text, String defaultIconPath, Runnable callback) throws MalformedURLException {
        this.text = text;
        this.setToolTipText(text);

        setName(text);

        //super(text);

        if (defaultIconPath != null) {

            //将defaultIconPath中的%s替换为CTColor.mainColor

            String modifiedPath = defaultIconPath.replace("%s", CTColor.style);

            this.defaultIconPath = defaultIconPath;

            URL tempPath = getClass().getResource(modifiedPath);
            if (tempPath != null) {
                this.defaultIcon = GetIcon.getIcon(getClass().getResource(modifiedPath), 35, 35);
                this.defaultIconImage = GetIcon.getImageIcon(getClass().getResource(modifiedPath), 35, 35).getImage();
            }


            this.setIcon(defaultIcon);
        }


        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        this.setOpaque(false);

        this.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.MORE_SMALL));
        this.setBackground(CTColor.backColor);
        this.setForeground(CTColor.textColor);
        this.setSize(defaultIcon.getIconWidth(), defaultIcon.getIconHeight());
        this.callback = callback;

        this.addActionListener(this);

        JButton button = this;
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                button.setOpaque(true);
                button.setBackground(new Color(218, 218, 218));
            }

            @Override
            public void focusLost(FocusEvent e) {
                button.setOpaque(false);
                button.setBackground(CTColor.backColor);
            }
        });

        this.addChangeListener(e -> {
            ButtonModel model = button.getModel();
            if (model.isPressed()) {//鼠标按下
                button.setOpaque(true);
                button.setBackground(new Color(179, 179, 179));
            } else if (model.isRollover()) {//鼠标移入
                button.setOpaque(true);
                button.setBackground(new Color(218, 218, 218));
            } else {
                button.setOpaque(false);
                button.setBackground(CTColor.backColor);
            }
        });
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;

    }

    public CTIconButton copy() throws MalformedURLException {
        return new CTIconButton(this.text, this.defaultIconPath, this.callback);
    }

    public CTTextButton toTextButton(boolean showBorder) {
        CTTextButton button = new CTTextButton(this.text, this.defaultIcon, showBorder);
        button.addActionListener(this);
        return button;
    }

    public CTRoundTextButton toRoundTextButton() {
        CTRoundTextButton button = new CTRoundTextButton(this.text, this.defaultIcon);
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
        int tempX = (width - this.defaultIcon.getIconWidth()) / 2;
        int tempY = (height - this.defaultIcon.getIconHeight()) / 2;

        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Float(tempX, tempY, this.defaultIcon.getIconWidth(), this.defaultIcon.getIconHeight(), CTInfo.arcw, CTInfo.arch));

        // 绘制图标（如果存在）
        if (this.defaultIconImage != null) {


            g2.drawImage(this.defaultIconImage, tempX, tempY, this.defaultIcon.getIconWidth(), this.defaultIcon.getIconHeight(), null);
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
