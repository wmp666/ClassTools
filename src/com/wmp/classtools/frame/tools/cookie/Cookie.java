package com.wmp.classTools.frame.tools.cookie;

import javax.swing.*;
import java.io.File;

public class Cookie {
    private String name;
    private String Style;
    private File path;
    private Icon icon;

    public Cookie(String name, String style, Icon icon, File path) {
        this.name = name;
        this.Style = style;
        this.path = path;
        this.icon = icon;
    }

    public Cookie() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStyle() {
        return Style;
    }

    public void setStyle(String style) {
        Style = style;
    }

    public File getPath() {
        return path;
    }

    public void setPath(File path) {
        this.path = path;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "Cookie{" +
                "name='" + name + '\'' +
                ", Style='" + Style + '\'' +
                ", icon=" + icon + '\'' +
                ", path=" + path +
                '}';
    }
}
