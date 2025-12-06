package com.wmp.classTools.CTComponent;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.classTools.CTComponent.CTButton.CTTextButton;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class CTComboBox extends JComboBox<String> {
    public CTComboBox() {
        super();


    }

    public void addItems(String... items) {
        for (String item : items) {
            this.addItem(item);
        }
    }
}
