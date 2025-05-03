package com.wmp.classTools.printLog;

import java.awt.*;

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
        Log.print(style, owner, logInfo, null);
    }

    public void print(Container c, String owner, String logInfo) {
        Log.print(style, owner, logInfo, c);
    }
}

