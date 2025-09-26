package com.wmp.PublicTools.UITools;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class GetIcon {
    public static Icon getIcon(URL path, int width, int height) {
        if (path == null) {
            return null;
        }

        ImageIcon icon = new ImageIcon(path);
        // 保留对非GIF图像的缩放处理，GIF应由组件尺寸控制显示大小
        if (!path.getPath().toLowerCase().endsWith(".gif")) {
            icon.setImage(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        }
        return icon;
    }

    public static ImageIcon getImageIcon(URL path, int width, int height){
        return (ImageIcon) getIcon(path, width, height);
    }
}
