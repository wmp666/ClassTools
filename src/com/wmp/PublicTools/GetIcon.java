package com.wmp.PublicTools;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class GetIcon {
    public static Icon getIcon(URL path, int width, int height){
        ImageIcon icon = new ImageIcon(path);
        icon.setImage(icon.getImage().getScaledInstance( width, height, Image.SCALE_DEFAULT));
        return icon;

    }
}
