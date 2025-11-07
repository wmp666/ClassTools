package com.wmp.PublicTools.UITools;

import com.wmp.PublicTools.CTInfo;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class GetIcon {
    public static Icon getIcon(URL path, int width, int height, boolean useDPI) {
        if (path == null) {
            return null;
        }

        if (useDPI) {
            width = (int) (width * CTInfo.dpi);
            height = (int) (height * CTInfo.dpi);
        }

        ImageIcon icon = new ImageIcon(path);
        // 保留对非GIF图像的缩放处理，GIF应由组件尺寸控制显示大小
        if (!path.getPath().toLowerCase().endsWith(".gif")) {
            icon.setImage(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        }
        return icon;
    }

    public static ImageIcon getImageIcon(URL path, int width, int height, boolean useDPI) {
        return (ImageIcon) getIcon(path, width, height, useDPI);
    }

    public static Icon getIcon(URL path, int width, int height) {

        return getIcon(path, width, height, true);
    }

    public static ImageIcon getImageIcon(URL path, int width, int height) {
        return (ImageIcon) getIcon(path, width, height, true);
    }






    public static Icon getIcon(String name, int colorStyle, int width, int height, boolean useDPI) {
        if (name == null) {
            return null;
        }

        if (useDPI) {
            width = (int) (width * CTInfo.dpi);
            height = (int) (height * CTInfo.dpi);
        }

        ImageIcon icon = new ImageIcon();
        // 保留对非GIF图像的缩放处理，GIF应由组件尺寸控制显示大小
        if (!IconControl.getIconStyle(name).startsWith("gif")) {
            icon.setImage(IconControl.getIcon(name, colorStyle).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        }else{
            icon.setImage(IconControl.getIcon(name, colorStyle).getImage());
        }
        return icon;
    }

    public static ImageIcon getImageIcon(String name, int colorStyle, int width, int height) {
        return (ImageIcon) getIcon(name, colorStyle, width, height, true);
    }

    public static Icon getIcon(String name, int colorStyle, int width, int height) {
        return getIcon(name, colorStyle, width, height, true);
    }

    public static ImageIcon getImageIcon(String name, int width, int height) {
        return (ImageIcon) getIcon(name, IconControl.COLOR_COLORFUL, width, height, true);
    }
    public static Icon getIcon(String name, int width, int height) {
        return getIcon(name, IconControl.COLOR_COLORFUL, width, height, true);
    }
}
