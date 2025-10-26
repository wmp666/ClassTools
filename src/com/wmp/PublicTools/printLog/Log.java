package com.wmp.PublicTools.printLog;

import com.wmp.Main;
import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.EasterEgg.EasterEgg;
import com.wmp.PublicTools.OpenInExp;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.UITools.GetIcon;
import com.wmp.PublicTools.videoView.MediaPlayer;
import com.wmp.classTools.CTComponent.CTOptionPane;
import com.wmp.classTools.frame.MainWindow;
import com.wmp.classTools.importPanel.finalPanel.FinalPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class Log {
    private static final ArrayList<String> logInfList = new ArrayList<>();
    private static int index = 0;


    public static final TrayIcon trayIcon = new TrayIcon(GetIcon.getImageIcon(Main.class.getResource(CTInfo.iconPath), 16, 16).getImage(), "ClassTools");

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
    public static ErrorLogStyle err = new ErrorLogStyle(LogStyle.ERROR);

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

    public static void initTrayIcon() {
        PopupMenu popupMenu = new PopupMenu();
        MenuItem exit = new MenuItem("exit");
        exit.addActionListener(e -> {
            Log.exit(0);
        });
        popupMenu.add(exit);

        MenuItem refresh = new MenuItem("refresh");
        refresh.addActionListener(e -> MainWindow.refreshPanel());
        popupMenu.add(refresh);

        MenuItem more = new MenuItem("more");
        more.addActionListener(e -> {
            JDialog moreDialog = new JDialog();
            moreDialog.setTitle("更多");
            moreDialog.setLayout(new FlowLayout(FlowLayout.CENTER));
            moreDialog.setSize((int) (250 * CTInfo.dpi), (int) (300 * CTInfo.dpi));
            moreDialog.setLocationRelativeTo(null);
            moreDialog.setModal(true);
            moreDialog.getContentPane().setBackground(CTColor.backColor);
            moreDialog.setIconImage(GetIcon.getImageIcon(Log.class.getResource("/image/light/more.png"), 32, 32).getImage());

            FinalPanel.allButList.forEach(but -> {
                    moreDialog.add(but.toRoundTextButton());
            });

            moreDialog.setVisible(true);
        });

        popupMenu.add(more);

        trayIcon.setPopupMenu(popupMenu);
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


        // 改为自动关闭窗口
        new Timer(3000, e -> {
            window.dispose();

            System.exit(status);

        }).start();

        saveLog(false);
    }

    private static void initBG(Container c, JFrame window, Dimension screenSize) {
        //c.setBackground(Color.BLACK);
        ((JPanel) c).setOpaque(false);




        String[] exitStrList = {
                "愿此行，终抵群星",
                "我们终将重逢",
                "明天见",
                "为了与你重逢愿倾尽所有",
                "生命从夜中醒来\n却在触碰到光明的瞬间坠入永眠"
        };
        String exitStr = exitStrList[new Random().nextInt(exitStrList.length)];
        String imageStr = switch (exitStr) {
            case "我们终将重逢" -> "wmzjcf.jpg";
            case "明天见" -> "mtj.jpg";
            case "愿此行，终抵群星" -> "ycxzdqx.jpg";
            case "为了与你重逢愿倾尽所有" -> "wlyncfwyqjsy.jpg";
            case "生命从夜中醒来\n却在触碰到光明的瞬间坠入永眠" -> "smcyzxl.jpg";
            default -> "";
        };
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


                if (!imageStr.isEmpty()) {
                    String name = "/image/exitBG/" + imageStr;
                    System.err.println(name);
                    ImageIcon icon = new ImageIcon(Main.class.getResource(name));//

                    icon.setImage(icon.getImage().getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_SMOOTH));

                    viewLabel.setIcon(icon);


                } else {
                    viewLabel.setBackground(Color.BLACK);
                    ((JPanel) c).setOpaque(true);
                }

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
        Log.print(style, owner, logInfo, null);
    }
    public static void print(LogStyle style, String owner, Object logInfo, Container c) {
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
                trayIcon.displayMessage(owner, logInfo.toString(), TrayIcon.MessageType.WARNING);
                System.err.println(info);
                logInfList.add(info);
            }

            case ERROR -> {

                info = "[" + dateStr + "]" +
                        "[错误]" +
                        "[" + owner + "] :" +
                        logInfo;
                trayIcon.displayMessage(owner, logInfo.toString(), TrayIcon.MessageType.ERROR);
                System.err.println(info);

                MediaPlayer.playMusic(MediaPlayer.MUSIC_STYLE_ERROR, true);


                String title;
                if (CTInfo.isError) title = "骇客已入侵";
                else title = "世界拒绝了我";
                Icon icon = null;
                if (CTInfo.isError) icon = GetIcon.getIcon(Log.class.getResource("/image/error/icon.png"), 100, 100);
                CTOptionPane.showMessageDialog(c, title, logInfo.toString(), icon, CTOptionPane.ERROR_MESSAGE, true);

                logInfList.add(info);
            }
        }
    }

    public static ArrayList<String> getLogInfList() {
        return logInfList;
    }

    public static void showLogDialog() {
        showLogDialog(false);
    }

    public static void showLogDialog(boolean happenSystemErr) {
        //dialog.removeAll();
        JDialog dialog = new JDialog((Frame) null, false);
        dialog.setTitle("日志");
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());

        textArea.setEditable(false);
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                EasterEgg.errorAction();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                EasterEgg.errorAction();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                EasterEgg.errorAction();
            }
        });

        JScrollPane scrollPane = new JScrollPane(textArea);
        dialog.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("关闭");
        closeButton.addActionListener(e -> {
            dialog.dispose();

            if (happenSystemErr) {
                saveLog(false);

                System.exit(-1);
            }
        });


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
            if (!Files.exists(Paths.get(CTInfo.DATA_PATH + "Log\\"))) {
                try {
                    Files.createDirectories(Paths.get(CTInfo.DATA_PATH + "Log\\"));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            OpenInExp.open(CTInfo.DATA_PATH + "Log\\");
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
            if (!Files.exists(Paths.get(CTInfo.DATA_PATH + "Log\\"))) {
                Files.createDirectories(Paths.get(CTInfo.DATA_PATH + "Log\\"));
            }
            Files.writeString(Paths.get(CTInfo.DATA_PATH + "Log\\Log_" + dateFormat.format(new Date()) + ".txt"),
                    sb.toString(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
            if (showMessageDialog)
                Log.info.message(null, "Log", "日志保存成功");
        } catch (IOException e) {
            Log.err.print(Log.class, "日志保存失败", e);
        }
    }
}


