package com.wmp.classTools.CTComponent;

import com.wmp.PublicTools.UITools.CTColor;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public abstract class CTPanel extends JPanel{

    private int nextPanelY;
    private ArrayList<CTSetsPanel> ctSetsPanelList = new ArrayList<>();

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

    public ArrayList<CTSetsPanel> getCtSetsPanelList() {
        return ctSetsPanelList;
    }

    public void setCtSetsPanelList(ArrayList<CTSetsPanel> ctSetsPanelList) {
        this.ctSetsPanelList = ctSetsPanelList;
    }

    public abstract void refresh() throws IOException;
}
