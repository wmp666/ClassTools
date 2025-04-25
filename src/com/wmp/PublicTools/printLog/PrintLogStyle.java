package com.wmp.PublicTools.printLog;

public class PrintLogStyle {
    private LogStyle style;

    public PrintLogStyle(LogStyle style) {
        this.style = style;
    }

    public LogStyle getStyle() {
        return style;
    }

    public void setStyle(LogStyle style) {
        this.style = style;
    }

    public void print(String owner, String logInfo) {
        Log.print(style, owner, logInfo);
    }
}
