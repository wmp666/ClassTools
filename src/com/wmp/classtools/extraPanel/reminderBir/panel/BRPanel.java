package com.wmp.classTools.extraPanel.reminderBir.panel;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.DateTools;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.UITools.PeoPanelProcess;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTOptionPane;
import com.wmp.classTools.CTComponent.CTPanel.CTViewPanel;
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


public class BRPanel extends CTViewPanel {

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

                /*if (CTInfo.disPanelList.contains(getID())) {
                    continue;
                }*/

                try {

                        List<String> BRTempList = getBRList();
                        List<String> WBTempList = getWBList();

                        if (!oldBRNameList.equals(BRTempList) || !oldWBNameList.equals(WBTempList)) showBRAndWB();

                        this.oldBRNameList.clear();
                        this.oldBRNameList.addAll(BRTempList);

                        this.oldWBNameList.clear();
                        this.oldWBNameList.addAll(WBTempList);


                } catch (Exception e) {
                    Log.err.print(getClass(), "获取生日列表失败", e);
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
                            showBRAndWB();
                        } catch (Exception ex) {
                            Log.err.print(getClass(), "显示生日列表失败", ex);
                        }
                    }
                });
                this.add(titleLabel, gbc);

                gbc.gridy++;
                this.add(PeoPanelProcess.getShowPeoPanel(oldBRNameList), gbc);

                //EasterEgg.getText(EETextStyle.HTML)


                JLabel titleLabel2 = new JLabel("<html>即将过生日:</html>");
                titleLabel2.setForeground(CTColor.textColor);
                titleLabel2.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.NORMAL));
                titleLabel2.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            showBRAndWB();
                        } catch (Exception ex) {
                            Log.err.print(getClass(), "显示生日列表失败", ex);
                        }
                    }
                });
                gbc.gridy++;
                this.add(titleLabel2, gbc);

                gbc.gridy++;
                this.add(PeoPanelProcess.getShowPeoPanel(oldWBNameList), gbc);


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

    private void showBRAndWB() throws IOException {
        List<String> BRTempList = getBRList();
        StringBuilder BRsb = new StringBuilder();

        List<String> WBTempList = getWBList();
        StringBuilder WBsb = new StringBuilder();

        if ((!WBTempList.contains("无人即将生日") && !WBTempList.contains("没有相关数据")) ||
                !BRTempList.contains("无人生日") && !BRTempList.contains("没有相关数据")) {
            for (int i = 0; i < BRTempList.size(); i++) {
                if (i != BRTempList.size() - 1)
                    BRsb.append(BRTempList.get(i)).append(", ");
                else BRsb.append(BRTempList.get(i));

            }

            for (int i = 0; i < WBTempList.size(); i++) {
                if (i != WBTempList.size() - 1)
                    WBsb.append(WBTempList.get(i)).append(", ");
                else WBsb.append(WBTempList.get(i));

            }
            CTOptionPane.showFullScreenMessageDialog("生日祝福", String.format("今日生日:%s\n即将生日:%s", BRsb, WBsb), 60, 8);
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
                if (DateTools.dayIsNow(birthday)) {
                    nameList.add(name);
                }
            }
        });
        if (!nameList.isEmpty()) {
            return nameList;
        }

        return List.of("无人生日");
    }

    /**
     * 获取即将生日列表
     *
     * @return 即将生日列表
     */
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
                int remainderDay = DateTools.getRemainderDay(birthday);
                if (remainderDay < 11 && remainderDay > 0) {
                    nameList.add(String.format("%s(差%d天)", name, remainderDay));
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
