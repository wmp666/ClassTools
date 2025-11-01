package com.wmp.classTools.extraPanel.countdown.panel;

import com.wmp.Main;
import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.DateTools;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTOptionPane;
import com.wmp.classTools.CTComponent.CTPanel.CTViewPanel;
import com.wmp.classTools.extraPanel.countdown.CDInfoControl;
import com.wmp.classTools.extraPanel.countdown.settings.CountDownSetsPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CountDownPanel extends CTViewPanel {

    private static CDInfoControl.CDInfo info = CDInfoControl.getCDInfo();
    private final JLabel titleLabel = new JLabel();
    private final JLabel timeLabel = new JLabel();

    private AtomicBoolean b = new AtomicBoolean(false);

    public CountDownPanel() {
        this.setID("CountDownPanel");
        this.setName("倒计时界面");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        this.setCtSetsPanelList(List.of(new CountDownSetsPanel(CTInfo.DATA_PATH)));

        initInfo();
        initUI();

        this.add(titleLabel, BorderLayout.NORTH);
        this.add(timeLabel, BorderLayout.CENTER);

        this.setIndependentRefresh(true, 34);

        /*Timer timer = new Timer(34, e -> {
            String targetTime = info.targetTime();
            long time = 0;
            try {
                // 获取时间, 并计算时间差
                time = DateTools.getRemainderTime(targetTime, "yyyy.MM.dd HH:mm:ss");
            } catch (Exception ex) {
                Log.err.print(getClass(), "时间数据化异常", ex);
            }
            //Log.info.print("时间显示","时间差:" + time);
            if (time < -60*1000) {
                CDInfoControl.CDInfo old = info;
                initInfo();
                if (!old.title().equals(info.title()) && info.title().equals("数据出错")) {
                    Log.info.systemPrint("时间显示", "已切换倒计时");
                }
            }

            if (time <= 0) {

                timeLabel.setText("已结束");
                if (!b.get()) {
                    if (Main.allArgs.get("screenProduct:show").contains(Main.argsList))
                        CTOptionPane.showFullScreenMessageDialog(info.title() + "倒计时", "已结束", 60, 3);
                    else
                        Log.info.systemPrint(info.title() + "倒计时", "已结束");
                }
                b.set(true);
                return;
            }

            b.set(false);
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%02d", time / (24L * 60 * 60 * 1000)))
                    .append("天");
            time %= 24L * 60 * 60 * 1000;// 去除n天(n * 24h)的时间,只留下余数
            sb.append(String.format("%02d时%02d分%02d秒", time / 3600000, time / 60000 % 60, time / 1000 % 60));

            timeLabel.setText(sb.toString());

            //otherTimeLabel.setText(String.format("%02d时%02d分%02d秒", time / 3600000, time / 60000 % 60, time / 1000 % 60));
        });
        timer.setRepeats(true);
        timer.start();*/
    }

    private void initUI() {


        titleLabel.setText("距" + info.title() + "还剩:");
        titleLabel.setForeground(CTColor.textColor);
        titleLabel.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.NORMAL));

        timeLabel.setForeground(CTColor.mainColor);
        timeLabel.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG));
    }

    private static void initInfo() {
        info = CDInfoControl.getCDInfo();
    }

    @Override
    protected void Refresh() throws IOException {
        initInfo();
        initUI();

        String targetTime = info.targetTime();
        long time = 0;
        try {
            // 获取时间, 并计算时间差
            time = DateTools.getRemainderTime(targetTime, "yyyy.MM.dd HH:mm:ss");
        } catch (Exception ex) {
            Log.err.print(getClass(), "时间数据化异常", ex);
        }
        //Log.info.print("时间显示","时间差:" + time);
        if (time < -60*1000) {
            CDInfoControl.CDInfo old = info;
            initInfo();
            if (!old.title().equals(info.title()) && info.title().equals("数据出错")) {
                Log.info.systemPrint("时间显示", "已切换倒计时");
            }
        }

        if (time <= 0) {

            timeLabel.setText("已结束");
            if (!b.get()) {
                if (Main.allArgs.get("screenProduct:show").contains(Main.argsList))
                    CTOptionPane.showFullScreenMessageDialog(info.title() + "倒计时", "已结束", 60, 3);
                else
                    Log.info.systemPrint(info.title() + "倒计时", "已结束");
            }
            b.set(true);
            return;
        }

        b.set(false);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%02d", time / (24L * 60 * 60 * 1000)))
                .append("天");
        time %= 24L * 60 * 60 * 1000;// 去除n天(n * 24h)的时间,只留下余数
        sb.append(String.format("%02d时%02d分%02d秒", time / 3600000, time / 60000 % 60, time / 1000 % 60));

        timeLabel.setText(sb.toString());

        this.revalidate();
        this.repaint();
    }
}
