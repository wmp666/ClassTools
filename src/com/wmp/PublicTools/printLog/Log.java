package com.wmp.PublicTools.printLog;

import com.wmp.Main;
import com.wmp.PublicTools.OpenInExp;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.UITools.GetIcon;
import com.wmp.PublicTools.videoView.MediaPlayer;
import com.wmp.classTools.CTComponent.CTOptionPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

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
                        //System.out.printf("内容(%s): %s%n", i, logInfList.get(i)); // 添加换行
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

    public static InfoLogStyle info = new InfoLogStyle(LogStyle.INFO);
    public static PrintLogStyle warn = new PrintLogStyle(LogStyle.WARN);
    public static PrintLogStyle err = new PrintLogStyle(LogStyle.ERROR);

    static {
        thread.setDaemon(true);
        thread.start();  // 确保启动线程
    }

    public Log() {
    }

    public static void exit(int status) {

        if(status == -1){
            return;
        }
        //获取桌面大小
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        JWindow window = new JWindow();
        window.setAlwaysOnTop(true);
        window.setSize(screenSize);
        Container c = window.getContentPane();
        c.setLayout(new BorderLayout());
        c.setBackground(Color.BLACK);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(status);
            }
        });


        String[] exitStrList = {
                "愿此行，终抵群星",
                "我们终将重逢",
                "明天见",
                "聚散终有时，山水有相逢。",
                "樱花凋零时，方知一期一会。",
                "当你有机会做出选择的时候，\n不要让自己后悔。",
                "庸人只得学着独立行走，\n在跌倒爬起中度过碌碌一生。\n但失败的人生同样是人生，\n他们有权品尝至最后。",
                "每一次移动后发生的变化，\n每一个不经意间弹出的话语，\n都在呈现■■■■时的所见所感……\n虽不如■■■■，\n但创造出了足够的情感冲击。",
                "如果遇到了两难的情况，\n一方面是■■，\n一方面是■■，\n只能选一个……",
        };
        String exitStr = exitStrList[new Random().nextInt(exitStrList.length)];
        if (exitStr.contains("\n")) {
            exitStr = "<html>" + exitStr.replaceAll("\\n", "<br>") + "</html>";
        }
        //String result = "<html>" + exitStr.replaceAll("\\n", "<br>") + "</html>";
        JLabel label = new JLabel(exitStr);// 创建标签
        label.setForeground(Color.WHITE);
        label.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.MORE_BIG));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        c.add(label, BorderLayout.CENTER);
        window.setVisible(true);



        new Timer(10, new ActionListener() {
            private float alpha = 255; // 使用浮点保证平滑过渡
            private final float delta = 255 / (1000/10f); // 按1秒持续时间计算步长

            @Override
            public void actionPerformed(ActionEvent e) {
                if (alpha <= 0) {
                    ((Timer)e.getSource()).stop();
                    return;
                }

                alpha = Math.max(0, alpha - delta);
                label.setForeground(new Color(255, 255, 255, 255 - (int)alpha));
            }
        }).start();



        // 改为自动关闭窗口
        new Timer(3000, e -> {
            window.dispose();

            System.exit(status);

        }).start();

        saveLog(false);
    }

    public static void print(LogStyle style, String owner, String logInfo, Container c) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MM.dd HH:mm:ss");
        String dateStr = dateFormat.format(date);

        String info;
        switch (style) {
            case INFO -> {

                info = "[" + dateStr + "]" +
                        "[info]" +
                        "[" + owner + "]: " +
                        logInfo;
                System.out.println(info);
                logInfList.add(info);
            }

            case WARN -> {

                info = "[" + dateStr + "]" +
                        "[警告]" +
                        "[" + owner + "] :" +
                        logInfo;
                System.out.println(info);
                logInfList.add(info);
            }

            case ERROR -> {

                info = "[" + dateStr + "]" +
                        "[错误]" +
                        "[" + owner + "] :" +
                        logInfo;
                System.err.println(info);

                MediaPlayer.playMusic(MediaPlayer.MUSIC_STYLE_ERROR, true);


                String title;
                if (Main.isError) title = "骇客已入侵";
                else title = "世界拒绝了我";
                Icon icon = null;
                if (Main.isError) icon = GetIcon.getIcon(Log.class.getResource("/image/error/icon.png"), 100, 100);
                CTOptionPane.showMessageDialog(c, title, logInfo, icon, CTOptionPane.ERROR_MESSAGE, false);

                logInfList.add(info);
            }
        }
    }

    public static ArrayList<String> getLogInfList() {
        return logInfList;
    }

    public static void showLogDialog() {
        //dialog.removeAll();
        JDialog dialog = new JDialog((Frame) null, false);
        dialog.setTitle("日志");
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(textArea);
        //scrollPane.setPreferredSize(new Dimension(480, 550));
        dialog.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("关闭");
        closeButton.addActionListener(e -> dialog.dispose());


        JButton clearButton = new JButton("清空");
        clearButton.addActionListener(e -> {
            int i = Log.info.showChooseDialog(dialog, "日志-清空", "是否清空并保存?");
            if (i == JOptionPane.YES_OPTION){
                saveLog();
            }
            textArea.setText("");
            logInfList.clear();
            index = 0;

        });

        JButton openButton = new JButton("打开所在位置");
        openButton.addActionListener(e -> {
            if ( !Files.exists(Paths.get(Main.DATA_PATH + "Log\\"))){
                try {
                    Files.createDirectories(Paths.get(Main.DATA_PATH + "Log\\"));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            OpenInExp.open(Main.DATA_PATH + "Log\\");
        });

        JButton saveButton = new JButton("保存");
        saveButton.addActionListener(e -> {
            saveLog();
        });
        buttonPanel.add(closeButton);
        buttonPanel.add(openButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(saveButton);




        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);

    }

    private static void saveLog() {
        saveLog(true);
    }

    private static void saveLog(boolean showMessageDialog){
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        //将logInfList中的内容转化为byte数组
        StringBuilder sb = new StringBuilder();
        for (String s : logInfList) {
            sb.append(s).append("\n");
        }
        // 实现日志保存
        try {
            if (!Files.exists(Paths.get(Main.DATA_PATH + "Log\\"))){
                Files.createDirectories(Paths.get(Main.DATA_PATH + "Log\\"));
            }
            Files.writeString(Paths.get(Main.DATA_PATH + "Log\\Log_" + dateFormat.format(new Date()) + ".txt"),
                    sb.toString(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
            if (showMessageDialog)
                Log.info.message(null, "Log", "日志保存成功");
        } catch (IOException e) {
            if (showMessageDialog)
                Log.err.print("Log", "日志保存失败");
            throw new RuntimeException(e);
        }
    }
}


