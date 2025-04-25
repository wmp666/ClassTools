package com.wmp.PublicTools.printLog;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Log {
    private static final ArrayList<String> logInfList = new ArrayList<>();
    private static int index = 0;


    private static final JTextArea textArea = new JTextArea();

    private static final Thread thread = new Thread(() -> {
        while (true) {
            synchronized (logInfList) { // 恢复同步块
                int currentSize = logInfList.size();
                if (index < currentSize) {
                    //SwingUtilities.invokeLater(() -> {
                    for (int i = index; i < currentSize; i++) {
                        textArea.append(logInfList.get(i) + "\n");
                        System.out.printf("内容(%s): %s%n", i, logInfList.get(i)); // 添加换行
                    }
                    textArea.setCaretPosition(textArea.getDocument().getLength());
                    //});
                    index = currentSize; // 在同步块内更新索引


                }
            }
            try {
                Thread.sleep(1000);  // 缩短刷新间隔
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    });

    public static PrintLogStyle info = new PrintLogStyle(LogStyle.INFO);
    public static PrintLogStyle warn = new PrintLogStyle(LogStyle.WARN);
    public static PrintLogStyle error = new PrintLogStyle(LogStyle.ERROR);

    static{
        thread.setDaemon(true);
        thread.start();  // 确保启动线程
    }

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

    public static ArrayList<String> getLogInfList() {
        return logInfList;
    }
    public static void showLogDialog() {
        //dialog.removeAll();
        JDialog dialog = new JDialog((Frame) null,false);
        dialog.setTitle("日志");
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(textArea);
        //scrollPane.setPreferredSize(new Dimension(480, 550));
        dialog.add(scrollPane, BorderLayout.CENTER);

        dialog.setVisible(true);

    }
}


