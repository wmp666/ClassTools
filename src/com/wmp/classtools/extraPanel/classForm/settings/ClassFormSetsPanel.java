package com.wmp.classTools.extraPanel.classForm.settings;

import com.wmp.classTools.CTComponent.CTSetsPanel;

import java.awt.*;
import java.io.IOException;

public class ClassFormSetsPanel extends CTSetsPanel {
    public ClassFormSetsPanel(String basicDataPath) {
        super(basicDataPath);

        this.setID("ClassFormSetsPanel");
        this.setName("课程表设置");
        this.setLayout(new GridBagLayout());
    }

    @Override
    public void save() throws Exception {

    }

    @Override
    public void refresh() throws IOException {

    }
}
