package com.wmp.classtools.CTComponent;

import javax.swing.*;

public abstract class CTPanel extends JPanel{

    private int mixY;

    public CTPanel(int mixY)
    {
        super();
        this.mixY = mixY;
    }

    public CTPanel()
    {
        super();
    }

    public int getMixY() {
        return mixY;
    }

    public void setMixY(int mixY) {
        this.mixY = mixY + this.mixY;
    }

    public void resetMixY(int mixY) {
        this.mixY = mixY;
    }
}
