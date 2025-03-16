package com.wmp.classtools.CTComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DuButton extends JButton implements MouseListener {
    private Runnable callback;
    private Icon defaultIcon ;
    private Icon rolloverIcon ;
    public DuButton(String text, int weight, int height, Runnable callback)
    {
        this(text, null, null, weight, height, callback);
    }

    public DuButton(Icon deaultIcon, Icon rolloverIcon, int a, Runnable callback)
    {
        this("", deaultIcon, rolloverIcon, a, a, callback);


    }

    public DuButton(String text, Icon deaultIcon, Icon rolloverIcon, int weight, int height, Runnable callback)
    {
        super(text, deaultIcon);
        this.setBorderPainted(false);
        Color color = new Color(236, 236, 237);
        this.setBackground(color);
        this.setSize(weight, height);
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
