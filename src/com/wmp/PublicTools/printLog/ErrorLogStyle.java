package com.wmp.PublicTools.printLog;

import java.awt.*;

public class ErrorLogStyle extends PrintLogStyle {
    public ErrorLogStyle(LogStyle style) {
        super(style);
    }

    public void print(Class<?> owner, String logInfo) {
        super.print(owner.toString(), logInfo);
    }

    public void print(Container c, Class<?> owner, String logInfo) {
        super.print(c, owner.toString(), logInfo);
    }
}
