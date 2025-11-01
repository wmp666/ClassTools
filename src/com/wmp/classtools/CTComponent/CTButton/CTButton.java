package com.wmp.classTools.CTComponent.CTButton;

import com.wmp.PublicTools.UITools.GetIcon;

import javax.swing.*;

public class CTButton extends JButton {

    private String iconName;
    private int iconStyle;

    public CTButton() {
        super();
    }

    public void setIcon(String name, int colorStyle, int width, int height){
        this.setIcon(GetIcon.getIcon(name, colorStyle, width, height));
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public int getIconStyle() {
        return iconStyle;
    }

    public void setIconStyle(int iconStyle) {
        this.iconStyle = iconStyle;
    }
}
