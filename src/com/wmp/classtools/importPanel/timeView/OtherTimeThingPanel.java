package com.wmp.classTools.importPanel.timeView;

import com.nlf.calendar.Lunar;
import com.wmp.PublicTools.DateTools;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTPanel.CTViewPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Date;

public class OtherTimeThingPanel extends CTViewPanel {
    private final JLabel other = new JLabel();

    private final Thread timeThread = new Thread(() -> {

        while (true) {

            Calendar calendar = Calendar.getInstance();
            String week = "周" + new String[]{"天", "一", "二", "三", "四", "五", "六"}[calendar.get(Calendar.DAY_OF_WEEK) - 1];

            Lunar lunar = Lunar.fromDate(new Date());

            //周六 八月廿七 乙巳[蛇]年
            // 大雪 节日
            StringBuilder jie = new StringBuilder();
            for (String festival : lunar.getFestivals()) {
                jie.append(festival).append( " ");
            }
            other.setText(String.format("<html>%s %s%s%s %s[%s]年<br>%s %s</html>",
                    week, lunar.getMonth()<0?"闰":"", DateTools.months[Math.abs(lunar.getMonth()) - 1], DateTools.days[lunar.getDay() - 1], lunar.getYearInGanZhi(), lunar.getYearShengXiao(),
                    lunar.getJieQi(), jie));
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Log.err.print(getClass(), "错误", e);
            }
            repaint();
        }
    });

    public OtherTimeThingPanel() throws IOException {

        this.setName("时间详情");
        this.setID("OtherTimeThingPanel");
        this.setLayout(new BorderLayout());
        initPanel();

        //时间刷新
        timeThread.setDaemon(true);
        timeThread.start();
    }

    private void initPanel() throws MalformedURLException {
        this.removeAll();

        Calendar calendar = Calendar.getInstance();

        other.setText(String.format("星期%s", calendar.get(Calendar.DAY_OF_WEEK)));
        other.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG));
        other.setForeground(CTColor.mainColor);
        this.add(other, BorderLayout.SOUTH);

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
