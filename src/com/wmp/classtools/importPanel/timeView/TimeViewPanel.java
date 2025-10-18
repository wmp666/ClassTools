package com.wmp.classTools.importPanel.timeView;

import com.nlf.calendar.Lunar;
import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.DayIsNow;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeViewPanel extends CTPanel {

    private final JLabel timeView = new JLabel();
    private final JLabel other = new JLabel();

    private final Thread timeThread = new Thread(() -> {

        while (true) {
            //获取时间
            Date date02 = new Date();
            //格式化 11.22 23:05
            DateFormat dateFormat02 = new SimpleDateFormat("MM.dd HH:mm:ss");
            //让时间在组件左侧显示
            timeView.setHorizontalAlignment(JLabel.CENTER);
            timeView.setText(dateFormat02.format(date02));

            Calendar calendar = Calendar.getInstance();
            String week = "周" + new String[]{"天", "一", "二", "三", "四", "五", "六"}[calendar.get(Calendar.DAY_OF_WEEK) - 1];

            Lunar lunar = Lunar.fromDate(new Date());


            StringBuilder sb = new StringBuilder();

            if (lunar.getMonth() < 0) {
                sb.append("闰");
            }
            //周六 八月廿七 乙巳[蛇]年 大雪
            sb.append(week)
                    .append(" ")
                    .append(DayIsNow.months[lunar.getMonth() - 1])//月
                    .append(DayIsNow.days[lunar.getDay() - 1])//日
                    .append(" ")
                    .append(lunar.getYearInGanZhi())
                    .append("[")
                    .append(lunar.getYearShengXiao())
                    .append("]年 ")
                    .append(lunar.getJieQi());

            other.setText(sb.toString());
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Log.err.print(getClass(), "错误", e);
            }
            repaint();
        }
    });

    public TimeViewPanel() throws IOException {

        this.setName("时间显示组件");
        this.setID("TimeViewPanel");
        this.setLayout(new BorderLayout());
        this.setCtSetsPanelList(List.of(new ScreenProductSetsPanel(CTInfo.DATA_PATH)));
        initPanel();

        //时间刷新
        timeThread.setDaemon(true);
        timeThread.start();
    }

    private void initPanel() throws MalformedURLException {
        this.removeAll();

        //获取时间
        Date date = new Date();
        //格式化 11.22 23:05
        DateFormat dateFormat = new SimpleDateFormat("MM.dd HH:mm:ss");


        timeView.setText(dateFormat.format(date));
        timeView.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG));
        //timeView.setBackground(new Color(0x0ECECED, true));
        timeView.setForeground(CTColor.mainColor);
        this.add(timeView, BorderLayout.CENTER);

        Calendar calendar = Calendar.getInstance();

        other.setText(String.format("星期%s", calendar.get(Calendar.DAY_OF_WEEK)));
        other.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG));
        other.setForeground(CTColor.mainColor);
        this.add(other, BorderLayout.SOUTH);

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
