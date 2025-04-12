package com.wmp.classTools.frame.tools.cookie;

import java.io.File;

public class Cookie {
    private String name;
    private String Style;
    private File path;

    public Cookie(String name, String style, File path) {
        this.name = name;
        Style = style;
        this.path = path;
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

    @Override
    public String toString() {
        return "Cookie{" +
                "name='" + name + '\'' +
                ", Style='" + Style + '\'' +
                ", path=" + path +
                '}';
    }
}
