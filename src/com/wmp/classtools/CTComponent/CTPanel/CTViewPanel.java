package com.wmp.classTools.CTComponent.CTPanel;

import com.wmp.PublicTools.UITools.CTColor;

import java.util.ArrayList;
import java.util.List;

public abstract class CTViewPanel extends CTPanel{

    private List<CTSetsPanel> ctSetsPanelList = new ArrayList<>();

    private boolean isScreenProductViewPanel = false;

    public CTViewPanel()
    {
        super();
    }

    public List<CTSetsPanel> getCtSetsPanelList() {
        return ctSetsPanelList;
    }

    public void setCtSetsPanelList(List<CTSetsPanel> ctSetsPanelList) {
        this.ctSetsPanelList = ctSetsPanelList;
    }

    public void toScreenProductViewPanel(){
        isScreenProductViewPanel = true;
    }

    public boolean isScreenProductViewPanel() {
        return isScreenProductViewPanel;
    }
}
