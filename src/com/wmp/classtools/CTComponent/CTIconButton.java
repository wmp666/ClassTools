package com.wmp.classTools.CTComponent;

import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.UITools.GetIcon;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;

public class CTIconButton extends JButton implements MouseListener, ActionListener {

    public static int ToolTipText = 0;
    public static int ButtonText = 1;

    private Runnable callback;
    private Icon defaultIcon;
    private String defaultIconPath;
    private Icon rolloverIcon;
    private String rolloverIconPath;
    private int textType = ToolTipText;
    private String text;

    public CTIconButton() {
    }

    //文字
    public CTIconButton(String text, int weight, int height, Runnable callback) throws MalformedURLException {
        this(ButtonText, text, null, null, weight, height, callback);
    }

    //图标 正方形
    public CTIconButton(String defaultIconPath, String rolloverIconPath, int a, Runnable callback) throws MalformedURLException {

        this(ToolTipText, null, defaultIconPath, rolloverIconPath, a, a, callback);

    }

    //文字 图标 正方形
    public CTIconButton(String text, String defaultIconPath, String rolloverIconPath, int a, Runnable callback) throws MalformedURLException {

        this(ToolTipText, text, defaultIconPath, rolloverIconPath, a, a, callback);

    }

    public CTIconButton(int textType, String text, String defaultIconPath, String rolloverIconPath, int weight, int height, Runnable callback) throws MalformedURLException {
        this.textType = textType;
        this.text = text;
        if (textType == ToolTipText) {
            this.setToolTipText(text);
        } else if (textType == ButtonText) {
            this.setText(text);
        }

        setName(text);

        //super(text);

        if (defaultIconPath != null) {

            //将defaultIconPath中的%s替换为CTColor.mainColor

            String modifiedPath = defaultIconPath.replace("%s", CTColor.style);
            String rolloverPath = rolloverIconPath.replace("%s", CTColor.style);

            this.defaultIconPath = defaultIconPath;
            this.rolloverIconPath = rolloverIconPath;

            URL tempPath = getClass().getResource(modifiedPath);
            URL tempPath2 = getClass().getResource(rolloverPath);
            if (tempPath != null)
                this.defaultIcon = GetIcon.getIcon(getClass().getResource(modifiedPath), 30, 30);
            if (tempPath2 != null)
                this.rolloverIcon = GetIcon.getIcon(getClass().getResource(rolloverPath), 30, 30);


            this.setIcon(defaultIcon);
        }

        // 设置按钮边框为透明
        this.setFocusPainted(false);
        this.setBorderPainted(false);


        this.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.MORE_SMALL));
        this.setBackground(CTColor.backColor);
        this.setForeground(CTColor.textColor);
        this.setSize(weight, height);
        this.callback = callback;

        this.addMouseListener(this);
        this.addActionListener(this);
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;

    }

    public CTIconButton copy() throws MalformedURLException {
        return new CTIconButton(this.textType, this.text, this.defaultIconPath, this.rolloverIconPath, this.getWidth(), this.getHeight(), this.callback);
    }

    @Override
    public void mouseClicked(MouseEvent e) {


    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //System.out.println("鼠标移入");
        if (rolloverIcon != null) {
            this.setIcon(rolloverIcon);
        }
        this.repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (defaultIcon != null) {
            this.setIcon(defaultIcon);
        }
        this.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.isEnabled() && callback != null) {
            callback.run();
        }
    }
}
