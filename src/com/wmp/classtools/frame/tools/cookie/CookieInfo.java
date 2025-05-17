package com.wmp.classTools.frame.tools.cookie;

public class CookieInfo {
    private String name;
    private String function;
    private String downloadUrl = "NULL";

    public CookieInfo() {
    }

    public CookieInfo(String name, String function) {
        this.name = name;
        this.function = function;
    }

    public CookieInfo(String name, String function, String downloadUrl) {
        this.name = name;
        this.function = function;
        if (!downloadUrl.isEmpty())
            this.downloadUrl = downloadUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        if (!downloadUrl.isEmpty())
            this.downloadUrl = downloadUrl;
    }

    @Override
    public String toString() {
        return "CookieInfo{" +
                "name='" + name + '\'' +
                ", function='" + function + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }
}
