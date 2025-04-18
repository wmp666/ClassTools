package com.wmp.classTools.frame.tools.cookie;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Cookie {
    private String name;
    private String style;
    private ArrayList<String> parameters = new ArrayList<>();
    private File path;
    private Icon icon;

    public Cookie(String name, String style, ArrayList<String> parameters, File path, Icon icon) {
        this.name = name;
        this.style = style;
        this.parameters = parameters;
        this.path = path;
        this.icon = icon;
    }

    public Cookie(String name, String style, Icon icon, File path) {
        this.name = name;
        this.style = style;
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
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
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

    public ArrayList<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<Object> parameters) {
        ArrayList<String> tempList= new ArrayList<>();
        for (Object o : parameters) {
            if (o instanceof String) {
                tempList.add((String) o);
            }
        }
        this.parameters = tempList;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "Cookie{" +
                "name='" + name + '\'' +
                ", style='" + style + '\'' +
                ", parameters='" + parameters + '\'' +
                ", path=" + path +
                ", icon=" + icon +
                '}';
    }
}
