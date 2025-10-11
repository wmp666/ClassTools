package com.wmp.classTools.extraPanel.reminderBir.panel;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.DayIsNow;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.UITools.PeoPanelProcess;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTOptionPane;
import com.wmp.classTools.CTComponent.CTPanel;
import com.wmp.classTools.extraPanel.reminderBir.settings.BRSetsPanel;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class BRPanel extends CTPanel {

    private final JLabel label = new JLabel();

    private File birthdayPath;

    private ArrayList<String> oldBRNameList = new ArrayList<>();

    private ArrayList<String> oldWBNameList = new ArrayList<>();

    public BRPanel(File birthdayPath) throws IOException {

        this.birthdayPath = birthdayPath;

        this.setLayout(new GridBagLayout());
        this.setName("生日提醒页");
        this.setID("BRPanel");
        this.setOpaque(false);
        this.setCtSetsPanelList(List.of(new BRSetsPanel(CTInfo.DATA_PATH)));

        //刷新
        new Thread(() -> {
            while (true) {
                this.removeAll();

                if (CTInfo.disPanelList.contains(getID())) {
                    continue;
                }

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.gridx = 0;
                gbc.gridy = 0;


                JLabel titleLabel = new JLabel("<html>今日过生日:</html>");
                titleLabel.setForeground(CTColor.textColor);
                titleLabel.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.NORMAL));
                titleLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            showBR();
                        } catch (Exception ex) {
                            Log.err.print(getClass(), "显示生日列表失败", ex);
                        }
                    }
                });
                this.add(titleLabel, gbc);


                //EasterEgg.getText(EETextStyle.HTML)
                try {
                    List<String> temp = getBRList();
                    if (!oldBRNameList.equals(temp)) showBR();


                    this.oldBRNameList.clear();
                    this.oldBRNameList.addAll(temp);
                    gbc.gridy++;
                    this.add(PeoPanelProcess.getShowPeoPanel(oldBRNameList), gbc);
                } catch (Exception e) {
                    Log.err.print(getClass(), "获取生日列表失败", e);
                }

                JLabel titleLabel2 = new JLabel("<html>即将过生日:</html>");
                titleLabel2.setForeground(CTColor.textColor);
                titleLabel2.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.NORMAL));
                titleLabel2.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            showWB();
                        } catch (Exception ex) {
                            Log.err.print(getClass(), "显示生日列表失败", ex);
                        }
                    }
                });
                gbc.gridy++;
                this.add(titleLabel2, gbc);

                try {
                    List<String> temp = getWBList();
                    if (!oldWBNameList.equals(temp)) showWB();


                    this.oldWBNameList.clear();
                    this.oldWBNameList.addAll(temp);
                    gbc.gridy++;
                    this.add(PeoPanelProcess.getShowPeoPanel(oldWBNameList), gbc);
                } catch (Exception e) {
                    Log.err.print(getClass(), "获取生日列表失败", e);
                }

                this.revalidate();
                this.repaint();

                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    Log.err.print(getClass(), "错误", e);
                }

            }
        }).start();
    }

    private void showBR() throws IOException {
        List<String> temp = getBRList();
        if (!temp.contains("无人生日") && !temp.contains("没有相关数据")) {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < temp.size(); i++) {
                if (i != temp.size() - 1)
                    sb.append(temp.get(i)).append(", ");
                else sb.append(temp.get(i));

            }
            CTOptionPane.showFullScreenMessageDialog("生日祝福", "让我们祝->" + sb + "<-生日快乐", 60);
        }
    }

    private void showWB() throws IOException {
        List<String> temp = getWBList();
        if (!temp.contains("无人即将生日") && !temp.contains("没有相关数据")) {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < temp.size(); i++) {
                if (i != temp.size() - 1)
                    sb.append(temp.get(i)).append(", ");
                else sb.append(temp.get(i));

            }
            CTOptionPane.showFullScreenMessageDialog("即将生日提醒", "这些人->" + sb + "<-即将生日", 60);
        }
    }

    private java.util.List<String> getBRList() throws IOException {

        if (!birthdayPath.exists()) {
            return List.of("没有相关数据");
        }
        ArrayList<String> nameList = new ArrayList<>();
        //Files.readString(Paths.get(birthdayPath.getPath()), StandardCharsets.UTF_8);
        String info = IOForInfo.getInfos(birthdayPath.toURI().toURL());
        JSONArray infoArray = new JSONArray(info);
        infoArray.forEach(item -> {
            if (item instanceof JSONObject infoObject) {
                String name = infoObject.getString("name");
                String birthday = infoObject.getString("birthday");
                if (DayIsNow.dayIsNow(birthday)) {
                    nameList.add(name);
                }
            }
        });
        if (!nameList.isEmpty()) {
            return nameList;
        }

        return List.of("无人生日");
    }

    private java.util.List<String> getWBList() throws IOException {

        if (!birthdayPath.exists()) {
            return List.of("没有相关数据");
        }
        ArrayList<String> nameList = new ArrayList<>();
        //Files.readString(Paths.get(birthdayPath.getPath()), StandardCharsets.UTF_8);
        String info = IOForInfo.getInfos(birthdayPath.toURI().toURL());
        JSONArray infoArray = new JSONArray(info);
        infoArray.forEach(item -> {
            if (item instanceof JSONObject infoObject) {
                String name = infoObject.getString("name");
                String birthday = infoObject.getString("birthday");
                int remainderDay = DayIsNow.getRemainderDay(birthday);
                if (remainderDay < 11 && remainderDay > 0) {
                    nameList.add(name);
                }
            }
        });
        if (!nameList.isEmpty()) {
            return nameList;
        }

        return List.of("无人即将生日");
    }

    @Override
    public void refresh() throws IOException {
        label.setForeground(CTColor.mainColor);

        this.revalidate();
        this.repaint();
    }
}
