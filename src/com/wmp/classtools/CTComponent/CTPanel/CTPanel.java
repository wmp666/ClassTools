package com.wmp.classTools.CTComponent.CTPanel;

import com.wmp.PublicTools.UITools.CTColor;

import javax.swing.*;
import java.io.IOException;

public abstract class CTPanel extends JPanel {
    private String ID = "CTPanel";

    public CTPanel() {
        super();
        this.setOpaque(false);
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public abstract void refresh() throws Exception;


}
