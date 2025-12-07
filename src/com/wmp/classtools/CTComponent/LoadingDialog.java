package com.wmp.classTools.CTComponent;

import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.printLog.Log;

import javax.swing.*;
import java.awt.*;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadingDialog extends JFrame {

    private final TreeMap<String, JPanel> PanelList = new TreeMap<>();

    private final TreeMap<String, CTProgressBar> progressBarPanelList = new TreeMap<>();
    private final TreeMap<String, JLabel> textPanelList = new TreeMap<>();


    public LoadingDialog() {
        //生成弹窗
        this.setTitle("进度显示");
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        //this.setModal(true);
        this.setLocationRelativeTo(null);
        this.setLayout(new GridLayout(0, 1));
        this.setAlwaysOnTop(true);

        this.getContentPane().setBackground(CTColor.backColor);
    }

    private void resetDialog() {
        AtomicBoolean isIndeterminate = new AtomicBoolean(false);
        AtomicInteger value = new AtomicInteger(0);
        progressBarPanelList.values().forEach(progressBar -> {
            if (progressBar.isIndeterminate()) {
                isIndeterminate.set(true);
            }else{
                value.addAndGet(progressBar.getValue());
            }
        });
        Taskbar taskbar = Taskbar.getTaskbar();
        if (isIndeterminate.get()) {
            taskbar.setWindowProgressState(this, Taskbar.State.INDETERMINATE);
        }else{
            taskbar.setWindowProgressState(this, Taskbar.State.NORMAL);
            taskbar.setWindowProgressValue(this, !progressBarPanelList.isEmpty() ?value.get()/progressBarPanelList.size():0);
        }

        this.getContentPane().setBackground(CTColor.backColor);
        this.revalidate();
        this.repaint();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(!PanelList.isEmpty());
    }

    public void showDialog(String id, String text) {
        showDialog(id, text, 0, true);
    }

    public void showDialog(String id, String text, int value) {
        showDialog(id, text, value, false);
    }

    public void showDialog(String id, String text, int value, boolean isIndeterminate) {
        Log.info.print("LoadingDialog", "显示进度条" + id);

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.SMALL));
        textLabel.setForeground(CTColor.textColor);

        CTProgressBar progressBar = new CTProgressBar();
        progressBar.setIndeterminate(isIndeterminate);

        if (!isIndeterminate) {
            progressBar.setValue(value);
        }

        JPanel panel = new JPanel();
        panel.setBorder(CTBorderFactory.createTitledBorder(id));
        panel.setLayout(new BorderLayout(10, 0));
        panel.add(textLabel, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);
        panel.setOpaque(false);

        PanelList.put(id, panel);
        textPanelList.put(id, textLabel);
        progressBarPanelList.put(id, progressBar);

        this.add(panel);

        resetDialog();
    }

    public void updateDialog(String id, int value) {
        updateDialog(id, null, value);
    }

    public void updateDialog(String id, String text) {
        updateDialog(id, text, -2);
    }

    /**
     * 更新进度条
     *
     * @param id    进度条id
     * @param text  进度条文本
     * @param value 进度条值(为-1时,更改为不确定模式)
     */
    public void updateDialog(String id, String text, int value) {
        Log.info.print("LoadingDialog", "更新进度条" + id);

        if (!PanelList.containsKey(id)) {
            Log.warn.print("LoadingDialog", "未找到id为" + id + "的进度条");
            return;
        }

        if (text != null) {
            JLabel textLabel = textPanelList.get(id);
            textLabel.setText(text);
            textLabel.repaint();
        }

        CTProgressBar progressBar = progressBarPanelList.get(id);
        if (value >= 0) {
            progressBar.setIndeterminate(false);
            progressBar.setValue(value);
        } else if (value == -1) {
            progressBar.setIndeterminate(true);
        }


        resetDialog();
    }

    public void closeDialog(String id) {
        Log.info.print("LoadingDialog", "关闭进度条" + id);

        if (!PanelList.containsKey(id)) {
            Log.warn.print("LoadingDialog", "未找到id为" + id + "的进度条");
            return;
        }

        this.remove(PanelList.get(id));
        //this.remove(PanelList.get(id));
        PanelList.remove(id);
        textPanelList.remove(id);
        progressBarPanelList.remove(id);

        resetDialog();
    }
}
