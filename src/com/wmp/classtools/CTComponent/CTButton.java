package com.wmp.classtools.CTComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CTButton extends JButton implements MouseListener {
    private Runnable callback;
    private Icon defaultIcon ;
    private Icon rolloverIcon ;
    public CTButton(String text,int a,Runnable callback)
    {
        super(text);
        this.setBorderPainted(false);
        this.setBackground(Color.WHITE);
        this.setSize(a,a);
        this.addActionListener(e -> callback.run());
    }

    public CTButton(Icon deaultIcon, Icon rolloverIcon,int a,Runnable callback)
    {
        super(deaultIcon);
        this.setBorderPainted(false);
        Color color = new Color(236, 236, 237);
        this.setBackground(color);
        this.setSize(a,a);
        this.callback = callback;
        this.defaultIcon = deaultIcon;
        this.rolloverIcon = rolloverIcon;
        this.addMouseListener(this);
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
        if(rolloverIcon != null)
        {
            this.setIcon(rolloverIcon);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if(defaultIcon != null)
        {
            this.setIcon(defaultIcon);
        }
    }
}
