package com.wmp.classTools.CTComponent.Menu;

import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;

import javax.swing.*;
import java.awt.*;

public class CTMenuBar extends JMenuBar {
    public CTMenuBar() {
        super();

        this.setOpaque(true);
        this.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));
        this.setBackground(CTColor.backColor);
        this.setForeground(CTColor.textColor);
    }
}
