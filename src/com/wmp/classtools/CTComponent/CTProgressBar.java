package com.wmp.classTools.CTComponent;

import javax.swing.*;

public class CTProgressBar extends JProgressBar {

    public CTProgressBar() {
        this(0, 100);
    }

    public CTProgressBar(int min, int max) {
        super(min, max);
        this.setOpaque(false);
        this.setBorderPainted(false);
        this.setUI(new CTGradientRoundProgressBarUI());


    }
}
