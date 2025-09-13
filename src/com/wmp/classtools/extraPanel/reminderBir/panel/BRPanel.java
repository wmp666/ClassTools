package com.wmp.classTools.extraPanel.reminderBir.panel;

import com.wmp.Main;
import com.wmp.PublicTools.TodayIsNow;
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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class BRPanel extends CTPanel {

    private final JLabel label = new JLabel();

    private File birthdayPath;

    private ArrayList<String> oldNameList = new ArrayList<>();

    public BRPanel(File birthdayPath) throws IOException {

        this.birthdayPath = birthdayPath;

        this.setLayout(new BorderLayout());
        this.setName("生日提醒页");
        this.setID("BRPanel");
        this.setOpaque(false);
        this.setCtSetsPanelList(List.of(new BRSetsPanel(Main.DATA_PATH)));


        //刷新
        new Thread(() -> {
            while (true) {
                this.removeAll();

                if (Main.disPanelList.contains(getID())) {
                    continue;
                }

                JLabel titleLabel = new JLabel("今日过生日:");
                titleLabel.setForeground(CTColor.textColor);
                titleLabel.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.NORMAL));
                this.add(titleLabel, BorderLayout.NORTH);


                //EasterEgg.getText(EETextStyle.HTML)
                try {
                    List<String> temp = getBRList();
                    if (!oldNameList.equals(temp) && !temp.contains("无人生日") && !temp.contains("没有相关数据")) {
                        StringBuilder sb = new StringBuilder();

                        for (int i = 0; i < temp.size(); i++) {
                            if (i != temp.size() - 1)
                                sb.append(temp.get(i)).append(", ");
                            else sb.append(temp.get(i));

                        }
                        CTOptionPane.showFullScreenMessageDialog("生日祝福", "让我们祝->" + sb + "<-生日快乐");
                    }


                    this.oldNameList.clear();
                    this.oldNameList.addAll(temp);
                    this.add(PeoPanelProcess.getShowPeoPanel(oldNameList), BorderLayout.CENTER);
                } catch (IOException e) {
                    Log.err.print("BRPanel", "获取生日列表失败: \n" + e.getMessage());
                    throw new RuntimeException(e);
                }


                this.revalidate();
                this.repaint();

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }).start();
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
                if (TodayIsNow.todayIsNow(birthday)) {
                    nameList.add(name);
                }
            }
        });
        if (!nameList.isEmpty()) {
            return nameList;
        }

        return List.of("无人生日");
    }

    @Override
    public void refresh() throws IOException {
        label.setForeground(CTColor.mainColor);

        this.revalidate();
        this.repaint();
    }
}
