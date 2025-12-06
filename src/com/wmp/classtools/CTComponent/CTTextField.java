package com.wmp.classTools.CTComponent;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.UITools.CTColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class CTTextField extends JTextField {
    public CTTextField() {
        super();
    }

    public CTTextField(String text) {
        super(text);
    }

    @Override
    public void setForeground(Color fg) {}

    @Override
    public void setBackground(Color bg) {}
}
