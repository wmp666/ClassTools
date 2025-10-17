package com.wmp.classTools.importPanel.timeView;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.UITools.GetIcon;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.*;
import com.wmp.classTools.CTComponent.Menu.CTPopupMenu;
import com.wmp.classTools.frame.MainWindow;
import com.wmp.classTools.importPanel.timeView.settings.ScreenProductSetsPanel;
import com.wmp.classTools.infSet.InfSetDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TimeViewPanel extends CTPanel {

    private final JLabel timeView = new JLabel();

    private final Thread timeThread = new Thread(() -> {

        while (true) {
            //获取时间
            Date date02 = new Date();
            //格式化 11.22 23:05
            DateFormat dateFormat02 = new SimpleDateFormat("MM.dd HH:mm:ss");
            //让时间在组件左侧显示
            timeView.setHorizontalAlignment(JLabel.CENTER);
            timeView.setText(dateFormat02.format(date02));
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Log.err.print(getClass(), "错误", e);
            }
            repaint();
        }
    });

    private final CTPopupMenu ETPopupMenu = new CTPopupMenu();

    public TimeViewPanel() throws IOException {

        this.setName("时间显示组件");
        this.setID("TimeViewPanel");
        this.setLayout(new BorderLayout());
        this.setCtSetsPanelList(List.of(new ScreenProductSetsPanel(CTInfo.DATA_PATH)));
        initPanel();

        CTTextButton edit = new CTTextButton("编辑");
        edit.setIcon(GetIcon.getIcon(getClass().getResource("/image/edit.png"), 20, 20));
        edit.addActionListener(e -> {
            try {
                new InfSetDialog(MainWindow::refreshPanel, "屏保设置");
            } catch (Exception ex) {
                Log.err.print(getClass(), "设置打开失败", ex);
                throw new RuntimeException(ex);
            }
        });

        ETPopupMenu.add(edit);

        timeView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    ETPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                }

            }
        });

        //时间刷新
        timeThread.setDaemon(true);
        timeThread.start();
    }

    private void initPanel() throws MalformedURLException {
        //获取时间
        Date date = new Date();
        //格式化 11.22 23:05
        DateFormat dateFormat = new SimpleDateFormat("MM.dd HH:mm:ss");


        timeView.setText(dateFormat.format(date));
        timeView.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG));
        //timeView.setBackground(new Color(0x0ECECED, true));
        timeView.setForeground(CTColor.mainColor);
        this.add(timeView, BorderLayout.CENTER);

        CTIconButton viewTimeButton = new CTIconButton("全屏显示时间",
                "/image/%s/view_0.png", () -> {
            try {
                int i = Log.info.showChooseDialog(null, "doge", "若当成了关闭按钮,\n请按\"是\",想要显示时间请按\"否\"");
                if (i == JOptionPane.YES_OPTION) return;
                else {
                    if (Log.info.showChooseDialog(null, "doge", "请再次确认是不是当成了关闭键?") == JOptionPane.YES_OPTION) {
                        CTOptionPane.showFullScreenMessageDialog("你不乘哦~", "不要总是乱选", 5);
                        return;
                    } else {
                        if (Log.info.showChooseDialog(null, "doge", "最后一次确认,你是不是想关闭某程序,\n而不是想要全屏显示时间?") == JOptionPane.YES_OPTION) {
                            CTOptionPane.showFullScreenMessageDialog("你不乘哦~", "不要总是乱选", 5);
                            return;
                        }
                    }
                }
                viewTimeInDeskTop();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        });

        this.add(viewTimeButton, BorderLayout.EAST);

    }

    private void viewTimeInDeskTop() throws MalformedURLException {
        //设置屏幕大小
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        JDialog window = new JDialog();
        window.setIconImage(new ImageIcon(getClass().getResource("/image/icon.png")).getImage());
        window.setSize(screenWidth, screenHeight);
        window.setLocation(0, 0);
        window.setAlwaysOnTop(true);
        window.setUndecorated(true);

        Container c = window.getContentPane();
        c.setLayout(new BorderLayout());

        //让时间在组件中央显示
        timeView.setHorizontalAlignment(JLabel.CENTER);
        timeView.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG_BIG));
        c.setBackground(CTColor.backColor);

        c.add(timeView, BorderLayout.CENTER);


        CTIconButton exitButton = new CTIconButton(
                "/image/%s/exit_0.png", () -> {
                window.setVisible(false);

            timeView.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG));
            this.add(timeView, BorderLayout.CENTER);

        });
        c.add(exitButton, BorderLayout.WEST);

        window.setVisible(true);
    }

    @Override
    public void refresh() {
        this.removeAll();

        try {
            initPanel();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }

}
