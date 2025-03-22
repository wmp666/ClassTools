package com.wmp.classtools.panel.finalPanel;

import com.wmp.classtools.CTComponent.CTButton;
import com.wmp.classtools.CTComponent.CTPanel;
import com.wmp.classtools.infSet.InfSetDialog;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;

public class FinalPanel extends CTPanel {

    private int FLPanelMixY;
    private CTButton settings = new CTButton();

    public FinalPanel(int nextPanelY) throws MalformedURLException {
        super(nextPanelY);

        // 设置布局-
        this.setLayout(new GridLayout( 1, 1, 0, 0));

        CTButton settings = new CTButton(getClass().getResource("/image/settings_0.png"),
                getClass().getResource("/image/settings_1.png"),30,() -> {

        });



        settings.setLocation(0, FLPanelMixY);

        this.setSize(300, 34);
        FLPanelMixY = this.getHeight() + FLPanelMixY;
        this.add(settings);
    }

    public CTButton getSettings() {
        return settings;
    }

    public void setSettings(CTButton settings) {
        this.settings = settings;

    }
}
