package com.wmp.classTools.CTComponent;

import com.wmp.PublicTools.UITools.CTColor;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class CTPanel extends JPanel{

    private List<CTSetsPanel> ctSetsPanelList = new ArrayList<>();

    public CTPanel()
    {
        super();
        this.setBackground(CTColor.backColor);
    }

    public List<CTSetsPanel> getCtSetsPanelList() {
        return ctSetsPanelList;
    }

    public void setCtSetsPanelList(List<CTSetsPanel> ctSetsPanelList) {
        this.ctSetsPanelList = ctSetsPanelList;
    }

    public abstract void refresh() throws IOException;
}
