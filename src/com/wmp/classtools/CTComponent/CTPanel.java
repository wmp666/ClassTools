package com.wmp.classTools.CTComponent;

import com.wmp.PublicTools.UITools.CTColor;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public abstract class CTPanel extends JPanel{

    private ArrayList<CTSetsPanel> ctSetsPanelList = new ArrayList<>();

    public CTPanel()
    {
        super();
        this.setBackground(CTColor.backColor);
    }

    public ArrayList<CTSetsPanel> getCtSetsPanelList() {
        return ctSetsPanelList;
    }

    public void setCtSetsPanelList(ArrayList<CTSetsPanel> ctSetsPanelList) {
        this.ctSetsPanelList = ctSetsPanelList;
    }

    public abstract void refresh() throws IOException;
}
