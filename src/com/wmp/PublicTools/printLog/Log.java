package com.wmp.PublicTools.printLog;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Log {
    private static final ArrayList<String> logInfList = new ArrayList<>();

    public static PrintLogStyle info = new PrintLogStyle(LogStyle.INFO);
    public static PrintLogStyle warn = new PrintLogStyle(LogStyle.WARN);
    public static PrintLogStyle error = new PrintLogStyle(LogStyle.ERROR);
    public static void print(LogStyle style, String owner, String logInfo){
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MM.dd HH:mm:ss");
        String dateStr = dateFormat.format(date);

        String info;
        switch (style){
            case INFO -> {

                info = "[" + dateStr + "]" +
                        "[info]" +
                        "[" + owner + "]: " +
                        logInfo;
                System.out.println(info);
                logInfList.add(info);
            }

            case WARN->{

                info = "[" + dateStr + "]" +
                        "[警告]" +
                        "[" + owner + "] :" +
                        logInfo;
                System.out.println(info);
                logInfList.add(info);
            }

            case ERROR->{

                info = "[" + dateStr + "]" +
                        "[错误]" +
                        "[" + owner + "] :" +
                        logInfo;
                System.err.println(info);

                JOptionPane.showMessageDialog(null, logInfo, "世界拒绝了我", JOptionPane.ERROR_MESSAGE);

                logInfList.add(info);
            }
        }
    }
}


