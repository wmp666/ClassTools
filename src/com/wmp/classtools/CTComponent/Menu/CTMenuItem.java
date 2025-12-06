package com.wmp.classTools.CTComponent.Menu;


import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;

import javax.swing.*;
import java.awt.*;

public class CTMenuItem extends JMenuItem {
    public CTMenuItem() {
        this("");
    }

    public CTMenuItem(String text) {
        this(text, null);
    }

    public CTMenuItem(String text, Icon icon) {
        super(text, icon);

    }
}