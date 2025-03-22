package com.wmp.classtools.CTComponent;

import com.wmp.CTColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;

public class CTButton extends JButton implements MouseListener {
    private Runnable callback;
    private Icon defaultIcon ;
    private Icon rolloverIcon ;

    public CTButton() {
    }

    public CTButton(String text, int weight, int height, Runnable callback) throws MalformedURLException {
        this(text, null, null, weight, height, callback);
    }

    public CTButton(URL defaultIconPath, URL rolloverIconPath, int a, Runnable callback)
    {



        this(null, defaultIconPath, rolloverIconPath, a, a, callback);


    }

    public CTButton(String text, URL defaultIconPath, URL rolloverIconPath, int weight, int height, Runnable callback)
    {
        super(text);

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
