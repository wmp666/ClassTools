package com.wmp.classtools.CTComponent;

import com.wmp.CTColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;

public class CTButton extends JButton implements MouseListener {

    public static int ToolTipText = 0;
    public static int ButtonText = 1;

    private Runnable callback;
    private Icon defaultIcon ;
    private Icon rolloverIcon ;

    public CTButton() {
    }

    //文字
    public CTButton(String text, int weight, int height, Runnable callback) throws MalformedURLException {
        this(ButtonText, text, null, null, weight, height, callback);
    }

    //图标 正方形
    public CTButton(URL defaultIconPath, URL rolloverIconPath, int a, Runnable callback)
    {

        this(ToolTipText, null, defaultIconPath, rolloverIconPath, a, a, callback);

    }

    //文字 图标 正方形
    public CTButton(String text, URL defaultIconPath, URL rolloverIconPath, int a, Runnable callback)
    {

        this(ToolTipText, text, defaultIconPath, rolloverIconPath, a, a, callback);

    }

    public CTButton(int textType, String text, URL defaultIconPath, URL rolloverIconPath, int weight, int height, Runnable callback)
    {
        if (textType == ToolTipText) {
            this.setToolTipText(text);
        }
        else if (textType == ButtonText) {
            this.setText(text);
        }

        //super(text);

        if (defaultIconPath != null){
            ImageIcon defaultIcon = new ImageIcon(defaultIconPath);
            defaultIcon.setImage(defaultIcon.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
            ImageIcon rolloverIcon = new ImageIcon(rolloverIconPath);
            rolloverIcon.setImage(rolloverIcon.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));

            this.setIcon(defaultIcon);
            this.defaultIcon = defaultIcon;
            this.rolloverIcon = rolloverIcon;
        }

        // 设置按钮边框为透明
        this.setFocusPainted(false);
        this.setBorderPainted(false);

        this.setBackground(CTColor.backColor);
        this.setSize(weight, height);
        this.callback = callback;

        this.addMouseListener(this);
    }

    public void setCallback(Runnable callback) {
        this.callback = callback;

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        callback.run();
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
        if(rolloverIcon != null)
        {
            this.setIcon(rolloverIcon);
        }
        this.repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if(defaultIcon != null)
        {
            this.setIcon(defaultIcon);
        }
        this.repaint();
    }
}
