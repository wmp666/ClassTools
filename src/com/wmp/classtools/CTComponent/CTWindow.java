package com.wmp.classTools.CTComponent;

import com.wmp.PublicTools.CTInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.awt.geom.RoundRectangle2D;

public class CTWindow extends JFrame implements WindowStateListener {
    public CTWindow() throws HeadlessException {
        this.setUndecorated(true);
        this.setShape(new RoundRectangle2D.Double(0, 0, this.getWidth(), this.getHeight(), CTInfo.arcw, CTInfo.arch));

        this.addWindowStateListener(this);
    }

    @Override
    public void setSize(Dimension d) {
        super.setSize(d);

        this.setShape(new RoundRectangle2D.Double(0, 0, this.getWidth(), this.getHeight(), CTInfo.arcw, CTInfo.arch));

    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        this.setShape(new RoundRectangle2D.Double(0, 0, this.getWidth(), this.getHeight(), CTInfo.arcw, CTInfo.arch));
    }
    @Override
    public void windowStateChanged(WindowEvent e) {
        if (e.getNewState() == JFrame.ICONIFIED) {
            this.setState(JFrame.NORMAL);
        }
    }
}
