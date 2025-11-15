package com.wmp.classTools.infSet.panel.personalizationSets.basicSets;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTBorderFactory;
import com.wmp.classTools.CTComponent.CTCheckBox;
import com.wmp.classTools.CTComponent.CTComboBox;
import com.wmp.classTools.CTComponent.CTPanel.setsPanel.CTBasicSetsPanel;
import com.wmp.classTools.CTComponent.CTTextField;
import com.wmp.classTools.frame.MainWindow;
import com.wmp.classTools.importPanel.finalPanel.FinalPanel;
import com.wmp.classTools.infSet.panel.personalizationSets.PersonalizationPanel;
import com.wmp.classTools.infSet.tools.SetStartUp;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.TreeMap;

public class PBPanelSetsPanel extends CTBasicSetsPanel {
    //隐藏面板 数据
    private final TreeMap<String, CTCheckBox> disposePanel = new TreeMap<>();
    //兼容数据
    private final CTTextField dpi = new CTTextField();

    public PBPanelSetsPanel(String basicDataPath) {
        super(basicDataPath);

        setName("组件设置");

        initUI();
    }

    private void initUI() {
        this.removeAll();


        JPanel SetsPanel = new JPanel();
        SetsPanel.setOpaque(false);
        JScrollPane mainPanelScroll = new JScrollPane(SetsPanel);
        mainPanelScroll.setOpaque(false);
        mainPanelScroll.getViewport().setOpaque(false);
        //调整滚动灵敏度
        mainPanelScroll.getVerticalScrollBar().setUnitIncrement(16);
        mainPanelScroll.setSize(400, 400);

        SetsPanel.setLayout(new GridBagLayout());//new GridLayout(0,1)
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel disPanPanel = new JPanel();
        disPanPanel.setOpaque(false);
        disPanPanel.setLayout(new GridLayout(0, 2));
        //设置组件隐藏
        disPanPanel.setBorder(CTBorderFactory.createTitledBorder("隐藏部分组件"));
        {

            MainWindow.allPanelList.forEach(panel -> {

                if (panel.getID().equals("TimeViewPanel") ||
                        //panel.getID().equals("NewsTextPanel") ||
                        panel.getID().equals("FinalPanel")) {
                    return;
                }

                CTCheckBox checkBox = new CTCheckBox(panel.getName());
                checkBox.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));
                checkBox.setBackground(CTColor.backColor);
                checkBox.setForeground(CTColor.textColor);
                disposePanel.put(panel.getID(), checkBox);
            });

            disposePanel.forEach((key, value) -> {
                disPanPanel.add(value);
            });
        }

        JPanel compatiblePanel = new JPanel();
        compatiblePanel.setOpaque(false);
        compatiblePanel.setLayout(new GridLayout(0, 2));
        compatiblePanel.setBorder(CTBorderFactory.createTitledBorder("其他设置"));
        //其他设置
        {
            JPanel dpiPanel = new JPanel();
            dpiPanel.setOpaque(false);
            dpiPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            {
                JLabel dpiLabel = new JLabel("组件放大倍率:");
                dpiLabel.setForeground(CTColor.textColor);
                dpiLabel.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));

                dpi.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));
                dpi.setText(String.valueOf(CTInfo.dpi));


                dpiPanel.add(dpiLabel);
                dpiPanel.add(dpi);
            }

            compatiblePanel.add(dpiPanel);


        }

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        SetsPanel.add(disPanPanel, gbc);
        gbc.gridy++;
        SetsPanel.add(compatiblePanel, gbc);

        this.setLayout(new BorderLayout());
        //this.setBackground(CTColor.backColor);
        this.add(mainPanelScroll, BorderLayout.CENTER);

        //显示数据
        {
            IOForInfo io = new IOForInfo(new File(CTInfo.DATA_PATH + "setUp.json"));
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(io.getInfos());
            } catch (Exception e) {
                Log.err.print(getClass(), "读取设置文件失败: " + e.getMessage());
                jsonObject = new JSONObject();
            }


            //
            if (jsonObject.has("disposePanel")) {
                JSONArray JSONArrdisPanel = jsonObject.getJSONArray("disposePanel");
                for (int i = 0; i < JSONArrdisPanel.length(); i++) {
                    String s = JSONArrdisPanel.getString(i);
                    if (disposePanel.containsKey(s)) {
                        disposePanel.get(s).setSelected(true);
                    }
                }
            }

            //放大倍率
            if (jsonObject.has("dpi"))
                dpi.setText(String.valueOf(jsonObject.getDouble("dpi")));

        }
    }

    @Override
    public void save() {
        try {
        //保存数据-个性化
        {
            IOForInfo io = new IOForInfo(new File(CTInfo.DATA_PATH + "setUp.json"));
            JSONObject jsonObject = null;

                jsonObject = new JSONObject(io.getInfos());


            //设置隐藏面板
            {
                ArrayList<String> tempList = new ArrayList<>();
                disposePanel.forEach((key, value) -> {
                    if (value.isSelected()) {
                        tempList.add(key);
                    }
                });
                jsonObject.put("disposePanel", tempList);
            }
            //设置DPI
            try {
                jsonObject.put("DPI", Double.valueOf(dpi.getText()));
            } catch (NumberFormatException e) {
                jsonObject.put("DPI", 1.0);
            }

            Log.info.print("InfSetDialog", "保存数据: " + jsonObject);
            try {
                io.setInfo(jsonObject.toString(2));

            } catch (Exception e) {
                Log.err.print(PersonalizationPanel.class, "保存数据失败", e);
            }

        }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void refresh() throws IOException {
        initUI();

        this.revalidate();
        this.repaint();
    }
}
