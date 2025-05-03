package com.wmp.classTools.frame.tools.cookie;

import com.wmp.Main;
import com.wmp.PublicTools.GetIcon;
import com.wmp.classTools.printLog.Log;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cookie {
    private String name;
    private String style;
    private ArrayList<String> parameters = new ArrayList<>();
    private String RunPath;
    private File cookiePath;
    private String icon;


    private static final TreeMap<String, String> fixedPathMap = new TreeMap<>();

    static {
        fixedPathMap.put("%DataPath", Main.DATA_PATH);
        fixedPathMap.put("%TempPath", Main.TEMP_PATH);
        fixedPathMap.put("%AppDirPath", System.getProperty("user.dir"));
    }

    public Cookie(String name, String style, ArrayList<String> parameters, String icon, String RunPath, File cookiePath) {
        this.name = name;
        this.style = style;
        this.parameters = parameters;
        this.RunPath = RunPath;
        this.cookiePath = cookiePath;
        this.icon = icon;
    }

    public Cookie(String name, String style, String icon, String RunPath, File cookiePath) {
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

    public ArrayList<String> getParameters() {
        return parameters;
    }

    public String getRunPath() {
        String exec = RunPath;
        exec = exec.replace("%CookiePath", cookiePath.getPath());
        Pattern pattern = Pattern.compile("(%[^\\\\]+)");
        Matcher matcher = pattern.matcher(exec);

        while (matcher.find()){
            String group = matcher.group(1);
            if (fixedPathMap.containsKey(group)){
                exec = exec.replace(group, fixedPathMap.get(group)).replace("\\", "/");
            }
        }


        try {
            return exec;
        } catch (Exception e) {
            Log.error.print("Cookie", "路径设置出错:" + e);
            throw new RuntimeException(e);
        }
    }

    public void setRunPath(String runPath) {
        RunPath = runPath;
    }

    public void setCookiePath(File cookiePath) {
        this.cookiePath = cookiePath;
    }

    public Icon getIcon() {
        StringBuilder iconPath = new StringBuilder();

        StringBuilder temp = new StringBuilder();
        temp.append("file:///").append(icon);

        {
            String tempStr = temp.toString();
            tempStr = tempStr.replace("%CookiePath", cookiePath.getPath());

            Pattern pattern2 = Pattern.compile("(%[^\\\\]+)");
            Matcher matcher2 = pattern2.matcher(tempStr);

            while (matcher2.find()){
                String group = matcher2.group(1);
                if (fixedPathMap.containsKey(group)){
                    tempStr = tempStr.replace(group, fixedPathMap.get(group));
                }
            }

            iconPath.append(tempStr.replace("\\", "/"));
        }

        try {
            if (!iconPath.toString().isEmpty()) {
                return GetIcon.getImageIcon(new URL(iconPath.toString()), 40, 40);
            }
        }catch ( Exception e){
            Log.error.print("Cookie", "图标路径设置出错:" + e.getMessage());
        }
        return null;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public void setParameters(ArrayList<String> parameters) {
        this.parameters = parameters;
    }

    public File getCookiePath() {


        try {

            if (cookiePath != null && !cookiePath.exists()) {
                cookiePath = new File(Main.DATA_PATH + "Cookie\\" + name + "\\");
                cookiePath.mkdirs();
            }
            return cookiePath;
        } catch (Exception e) {
            Log.error.print("Cookie", "cookiePath路径设置出错:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public TreeMap<String, Object> getPriData() {
        TreeMap<String, Object> tempMap = new TreeMap<>();
        tempMap.put("name", name);
        tempMap.put("style", style);
        tempMap.put("RunPath", RunPath);
        tempMap.put("cookiePath", cookiePath);
        tempMap.put("icon", icon);
        tempMap.put("parameters", parameters);
        return tempMap;
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
