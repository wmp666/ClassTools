package com.wmp.PublicTools.printLog;

import com.wmp.PublicTools.ExceptionStringConverter;

import java.awt.*;

public class ErrorLogStyle extends PrintLogStyle {
    public ErrorLogStyle(LogStyle style) {
        super(style);
    }


    public void systemPrint(Class<?> owner, Object logInfo, Exception e) {

        Log.systemPrint(LogStyle.ERROR, owner.getName(), logInfo + "\n"
                + ExceptionStringConverter.convertToString(e, true));
    }

    public void print(Class<?> owner, Object logInfo) {
        super.print(owner.toString(), logInfo);
    }

    public void print(Container c, Class<?> owner, Object logInfo) {
        super.print(c, owner.toString(), logInfo);
    }

    public void print(Class<?> owner, Object logInfo, Exception e) {

        super.print(owner.toString(), logInfo + "\n" +
                ExceptionStringConverter.convertToString(e, true));
    }

    public void print(Container c, Class<?> owner, String logInfo, Exception e) {
        super.print(c, owner.toString(), logInfo + "\n" +
                ExceptionStringConverter.convertToString(e, true));
    }
}
