package com.wmp.classTools.frame.tools.cookie;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Cookie {
    private String name;
    private String style;
    private ArrayList<String> parameters = new ArrayList<>();
    private File RunPath;
    private File cookiePath;
    private Icon icon;

    public Cookie(String name, String style, ArrayList<String> parameters, File RunPath, File cookiePath, Icon icon) {
        this.name = name;
        this.style = style;
        this.parameters = parameters;
        this.RunPath = RunPath;
        this.cookiePath = cookiePath;
        this.icon = icon;
    }

    public Cookie(String name, String style, Icon icon, File RunPath, File cookiePath) {
        this.name = name;
        this.style = style;
        this.RunPath = RunPath;
        this.cookiePath = cookiePath;
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

    public File getRunPath() {
        return RunPath;
    }

    public void setRunPath(File runPath) {
        this.RunPath = runPath;
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

    public void setParameters(ArrayList<String> parameters) {
        this.parameters = parameters;
    }

    public File getCookiePath() {
        return cookiePath;
    }

    public void setCookiePath(File cookiePath) {
        this.cookiePath = cookiePath;
    }

    @Override
    public String toString() {
        return "Cookie{" +
                "name='" + name + '\'' +
                ", style='" + style + '\'' +
                ", parameters=" + parameters +
                ", RunPath=" + RunPath +
                ", cookiePath=" + cookiePath +
                ", icon=" + icon +
                '}';
    }
}
