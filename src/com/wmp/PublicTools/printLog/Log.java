package com.wmp.PublicTools.printLog;

import com.sun.jdi.IncompatibleThreadStateException;
import com.wmp.Main;
import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.OpenInExp;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.UITools.GetIcon;
import com.wmp.PublicTools.appFileControl.IconControl;
import com.wmp.PublicTools.io.GetPath;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.videoView.MediaPlayer;
import com.wmp.classTools.CTComponent.CTButton.CTRoundTextButton;
import com.wmp.classTools.CTComponent.CTButton.CTTextButton;
import com.wmp.classTools.CTComponent.CTOptionPane;
import com.wmp.classTools.CTComponent.Menu.CTPopupMenu;
import com.wmp.classTools.frame.MainWindow;
import com.wmp.classTools.importPanel.finalPanel.FinalPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

public class Log {
    public static final TrayIcon trayIcon = new TrayIcon(GetIcon.getImageIcon(Log.class.getResource("/image/icon/icon.png"), 16, 16).getImage(), "ClassTools");
    private static final LinkedList<String> logInfList = new LinkedList<>();
    private static final JTextArea textArea = new JTextArea();

    public static InfoLogStyle info = new InfoLogStyle(LogStyle.INFO);
    public static WarnLogStyle warn = new WarnLogStyle(LogStyle.WARN);
    public static ErrorLogStyle err = new ErrorLogStyle(LogStyle.ERROR);

    private static boolean isSaveLog = true;

    public static String logFilePath;
    private static final Thread thread = new Thread(() -> {
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        logFilePath = CTInfo.DATA_PATH + "Log\\log_" + dateFormat.format(new Date()) + ".log";
        File logFile = new File(logFilePath);
        if (!logFile.exists()) {
            logFile.getParentFile().mkdirs();
        }
        try {

                BufferedWriter writer = new BufferedWriter(Files.newBufferedWriter(Paths.get(logFilePath), StandardOpenOption.APPEND, StandardOpenOption.CREATE_NEW));
                while (true) {
                    if (!isSaveLog) continue;
                    synchronized (logInfList) { // 恢复同步块
                        try {
                            int currentSize = logInfList.size();

                            for (int i = 0; i < currentSize; i++) {
                                String logInfo = logInfList.removeFirst();
                                writer.write(logInfo + "\n");
                            }
                            Thread.sleep(34);  // 刷新间隔
                        } catch (InterruptedException e) {
                            Log.err.systemPrint(Log.class, "日志保存异常", e);
                        }
                    }
                }
        } catch (IOException e) {
            Log.err.print("系统操作", "日志路径获取异常");
            throw new RuntimeException(e);
        }

    });

    static {
        SystemTray systemTray = SystemTray.getSystemTray();
        try {
            systemTray.add(trayIcon);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

        thread.setDaemon(true);
        thread.start();  // 确保启动线程
    }

    public Log() {
    }

    private static MouseAdapter mouseAdapter;
    private static ActionListener actionListener;
    public static void initTrayIcon() {
        CTPopupMenu popupMenu = new CTPopupMenu();

        CTRoundTextButton refresh = new CTRoundTextButton("刷新");
        refresh.setIcon(GetIcon.getImageIcon("刷新", IconControl.COLOR_COLORFUL, 20, 20));
        refresh.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.NORMAL));
        refresh.addActionListener(e -> MainWindow.refreshPanel());
        popupMenu.add(refresh);

        CTRoundTextButton more = new CTRoundTextButton("更多");
        more.setIcon(GetIcon.getImageIcon("更多", IconControl.COLOR_COLORFUL, 20, 20));
        more.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.NORMAL));
        more.addActionListener(e -> {
            CTPopupMenu moreMenu = new CTPopupMenu();
            FinalPanel.allButList.forEach(but -> {
                moreMenu.add(but.toRoundTextButton());
            });

            moreMenu.show(more,more.getWidth(), 0);
        });

        popupMenu.add(more);

        CTRoundTextButton exit = new CTRoundTextButton("关闭");
        exit.setIcon(GetIcon.getImageIcon("关闭", IconControl.COLOR_COLORFUL, 20, 20));
        exit.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.NORMAL));
        exit.addActionListener(e -> {
            Log.exit(0);
        });
        popupMenu.add(exit);

        CTRoundTextButton hide = new CTRoundTextButton("隐藏");
        hide.setIcon(GetIcon.getImageIcon("删除", IconControl.COLOR_COLORFUL, 20, 20));
        hide.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.NORMAL));
        hide.addActionListener(e -> {
            popupMenu.setVisible(false);
        });
        popupMenu.add(hide);

        trayIcon.removeMouseListener(mouseAdapter);
        trayIcon.removeActionListener(actionListener);

        mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                popupMenu.show(null, e.getXOnScreen() - popupMenu.getWidth(), e.getYOnScreen() - popupMenu.getHeight());
            }
        };
        actionListener = e -> {
            popupMenu.show(null, 0, 0);
        };
        trayIcon.addMouseListener(mouseAdapter);
        trayIcon.addActionListener(actionListener);
    }

    public static void exit(int status) {

        if (!Main.allArgs.get("screenProduct:show").contains(Main.argsList) && (status == -1 || !CTInfo.canExit)) {
            Log.err.print("系统操作", "错误行为");
            return;
        }
        //获取桌面大小
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        JFrame window = new JFrame();
        window.setAlwaysOnTop(true);
        window.setSize(screenSize);
        window.setUndecorated(true);
        Container c = window.getContentPane();


        c.setLayout(new BorderLayout());
        initBG(c, window, screenSize);

        window.setVisible(true);


        new Timer(3000, e -> {
                window.dispose();

                System.exit(status);
        }).start();

    }

    private static void initBG(Container c, JFrame window, Dimension screenSize) {
        //c.setBackground(Color.BLACK);
        ((JPanel) c).setOpaque(false);


        String[] exitStrList = {
                "愿此行，终抵群星",
                "我们终将重逢",
                "明天见",
                "为了与你重逢愿倾尽所有",
                "生命从夜中醒来\n却在触碰到光明的瞬间坠入永眠",
                "一起走向明天，我们不曾分离"
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
        label.setOpaque(false);

        c.add(label, BorderLayout.CENTER);

        JLabel viewLabel = new JLabel();
        //viewLabel.setBackground(Color.BLACK);
        {


            //背景
            {

                viewLabel.setBounds(0, 0, screenSize.width, screenSize.height);

                viewLabel.setIcon(GetIcon.getIcon(exitStr, IconControl.COLOR_DEFAULT, screenSize.width, screenSize.height, false));


            }
        }
        window.getLayeredPane().add(viewLabel, Integer.valueOf(Integer.MIN_VALUE));

        window.getLayeredPane().repaint();
        viewLabel.revalidate();
        viewLabel.repaint();
    }

    public static void systemPrint(LogStyle style, String owner, String logInfo) {

        if (SystemTray.isSupported() && Objects.requireNonNull(style) == LogStyle.INFO) {
            trayIcon.displayMessage(owner, logInfo, TrayIcon.MessageType.INFO);
        }
        Log.print(style, owner, logInfo, null, false);
    }

    public static void print(LogStyle style, String owner, Object logInfo, Container c) {
        print(style, owner, logInfo, c, true);
    }

    public static void print(LogStyle style, String owner, Object logInfo, Container c, boolean showMessageDialog) {
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
                logInfList.addLast(info);
            }

            case WARN -> {

                info = "[" + dateStr + "]" +
                        "[warn]" +
                        "[" + owner + "] :" +
                        logInfo;
                trayIcon.displayMessage(owner, logInfo.toString(), TrayIcon.MessageType.WARNING);
                System.err.println(info);


                logInfList.addLast(info);
            }

            case ERROR -> {

                info = "[" + dateStr + "]" +
                        "[error]" +
                        "[" + owner + "] :" +
                        logInfo;
                trayIcon.displayMessage(owner, logInfo.toString(), TrayIcon.MessageType.ERROR);
                System.err.println(info);
                logInfList.addLast(info);

                MediaPlayer.playMusic("系统", "错误");

                if (showMessageDialog) {
                    Icon icon = null;
                    if (CTInfo.isError) icon = GetIcon.getIcon("图标", IconControl.COLOR_DEFAULT, 100, 100);
                    CTOptionPane.showMessageDialog(c, owner, logInfo.toString(), icon, CTOptionPane.ERROR_MESSAGE, true);
                }


            }
        }
    }

    public static LinkedList<String> getLogInfList() {
        return logInfList;
    }

    public static void showLogDialog() {
        showLogDialog(false);
    }

    public static void showLogDialog(boolean happenSystemErr) {
        Log.info.loading.showDialog("log", "正在读取日志文件");

        textArea.setText("");
        try {
            BufferedReader br = new BufferedReader(Files.newBufferedReader(Path.of(logFilePath), StandardCharsets.UTF_8));
            while (br.ready()) {
                textArea.append(br.readLine() + "\n");
            }
            br.close();
        } catch (IOException e) {
            textArea.setText("日志文件不存在");
            Log.info.loading.closeDialog("log");
            Log.err.print(Log.class, "读取日志文件失败", e);
        }
        Log.info.loading.closeDialog("log");

        //dialog.removeAll();
        JDialog dialog = new JDialog((Frame) null, false);
        dialog.setTitle("日志");
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(CTColor.backColor);

        textArea.setFont(CTFont.getDefaultFont(Font.PLAIN, CTFontSizeStyle.SMALL));
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        dialog.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        CTTextButton closeButton = new CTTextButton("关闭");
        closeButton.addActionListener(e -> {
            dialog.dispose();

            if (happenSystemErr) {
                saveLog(false);

                System.exit(-1);
            }
        });


        CTTextButton clearButton = new CTTextButton("清空");
        clearButton.addActionListener(e -> {
            int i = Log.info.showChooseDialog(dialog, "日志-清空", "是否清空并保存?");
            if (i == JOptionPane.YES_OPTION) {
                saveLog();
            }
            textArea.setText("");
            logInfList.clear();

        });

        CTTextButton openButton = new CTTextButton("打开所在位置");
        openButton.addActionListener(e -> {
            if (!Files.exists(Paths.get(CTInfo.DATA_PATH + "Log\\"))) {
                try {
                    Files.createDirectories(Paths.get(CTInfo.DATA_PATH + "Log\\"));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            OpenInExp.open(CTInfo.DATA_PATH + "Log\\");
        });

        CTTextButton saveButton = new CTTextButton("保存至");
        saveButton.addActionListener(e -> {
            saveLog();
        });
        buttonPanel.add(closeButton);
        buttonPanel.add(openButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(saveButton);


        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);

    }

    private static void saveLog() {
        saveLog(GetPath.getDirectoryPath(null, "保存日志"), true);
    }

    private static void saveLog(String path) {
        saveLog(path, true);
    }

    private static void saveLog(boolean showMessageDialog) {
        saveLog(GetPath.getDirectoryPath(null, "保存日志"), showMessageDialog);
    }

    private static void saveLog(String path, boolean showMessageDialog) {

        synchronized (logInfList) {
            try {
                IOForInfo.copyFile(Paths.get(logFilePath), Paths.get(path, "Log.txt"));

                if (showMessageDialog)
                    Log.info.message(null, "Log", "日志保存成功");
            } catch (Exception e) {
                Log.err.print(Log.class, "日志保存失败", e);
                throw new RuntimeException(e);
            }
        }
    }

    public static void isSaveLog(boolean b) {
        isSaveLog = b;
    }
}


