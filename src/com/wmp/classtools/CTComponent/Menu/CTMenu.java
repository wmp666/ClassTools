package com.wmp.classTools.CTComponent.Menu;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.classTools.CTComponent.CTButton.CTRoundTextButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CTMenu extends JMenu{

    public CTMenu() {
        this("");
    }

    public CTMenu(String text) {
        super(text);
        // 延迟初始化：在真正需要时才创建CTPopupMenu
        this.setOpaque(true);
        this.setBackground(CTColor.backColor);
        this.setForeground(CTColor.textColor);
/*
        for (Component menuComponent : this.getMenuComponents()) {
            menuComponent.setBackground(CTColor.backColor);
            menuComponent.setForeground(CTColor.textColor);
        }*/
    }

}