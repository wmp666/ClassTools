package com.wmp.classTools.CTComponent;

import com.wmp.PublicTools.CTColor;

import javax.swing.*;
import java.io.IOException;

public abstract class CTPanel extends JPanel{

    private int nextPanelY;

    public CTPanel(int nextPanelY)
    {
        super();
        this.nextPanelY = nextPanelY;
        this.setBackground(CTColor.backColor);
    }

    public CTPanel()
    {
        super();
    }

    public int getNextPanelY() {
        return nextPanelY;
    }

    public void appendNextPanelY(int nextPanelY) {
        this.nextPanelY = nextPanelY + this.nextPanelY;
    }

    public void setNextPanelY(int nextPanelY) {
        this.nextPanelY = nextPanelY;
    }

    public abstract void refresh() throws IOException;
}
