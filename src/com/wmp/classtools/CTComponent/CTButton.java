package com.wmp.classtools.CTComponent;

import javax.swing.*;
import java.awt.*;

public class CTButton extends JButton {
    public CTButton(String text,int a,Runnable callback)
    {
        super(text);

        this.setBackground(Color.WHITE);
        this.setSize(a,a);
        this.addActionListener(e -> callback.run());
    }

    public CTButton(Icon icon,int a,Runnable callback)
    {
        super(icon);
        this.setBackground(Color.WHITE);
        this.setSize(a,a);
        this.addActionListener(e -> callback.run());
    }
}
