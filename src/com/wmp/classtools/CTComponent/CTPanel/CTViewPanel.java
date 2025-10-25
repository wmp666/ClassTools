package com.wmp.classTools.CTComponent.CTPanel;

import com.wmp.PublicTools.UITools.CTColor;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class CTViewPanel extends CTPanel{

    private List<CTSetsPanel> ctSetsPanelList = new ArrayList<>();


    public CTViewPanel()
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

    public CTViewPanel getScreenProductViewPanel(){
        return this;
    }
}
