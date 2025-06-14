package com.wmp.classTools.infSet.panel;

import com.wmp.Main;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTSetsPanel;
import com.wmp.classTools.importPanel.finalPanel.FinalPanel;
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

public class PersonalizationPanel extends CTSetsPanel {

    private final JComboBox<String> mainColorComboBox = new JComboBox<>();
    private final JComboBox<String> mainThemeComboBox = new JComboBox<>();
    private final TreeMap<String, JCheckBox> disposeButton = new TreeMap<>();
    private final JCheckBox StartUpdate = new JCheckBox("启动检查更新");
    private final JCheckBox canExit = new JCheckBox("防止被意外关闭");
    private final JCheckBox startUp = new JCheckBox("开机自启动");


    public PersonalizationPanel(String basicDataPath) {
        super(basicDataPath);

        setName("个性化");

        initUI();
    }

    private void initUI() {
        this.removeAll();


        JPanel SetsPanel = new JPanel();
        JScrollPane mainPanelScroll = new JScrollPane(SetsPanel);
        //调整滚动灵敏度
        mainPanelScroll.getVerticalScrollBar().setUnitIncrement(16);
        mainPanelScroll.setSize(400, 400);

        SetsPanel.setBackground(CTColor.backColor);
        SetsPanel.setLayout(new GridBagLayout());//new GridLayout(0,1)
        GridBagConstraints gbc = new GridBagConstraints();


        JPanel ColorPanel = new JPanel();
        ColorPanel.setBackground(CTColor.backColor);
        ColorPanel.setLayout(new GridLayout(1, 2));
        ColorPanel.setBorder(BorderFactory.createTitledBorder("颜色设置"));
        //颜色设置
        {
            //主题色设置
            JPanel MainColorSets = new JPanel();
            {

                MainColorSets.setLayout(new FlowLayout(FlowLayout.LEFT));
                MainColorSets.setBackground(CTColor.backColor);

                JLabel mainColorLabel = new JLabel("主题色:");
                mainColorLabel.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.SMALL));
                mainColorLabel.setForeground(CTColor.textColor);
                //mainColorLabel.setSize(50, 30);


                mainColorComboBox.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.SMALL));
                mainColorComboBox.setForeground(CTColor.textColor);
                mainColorComboBox.setBackground(CTColor.backColor);

                //添加颜色项目
                mainColorComboBox.removeAllItems();
                mainColorComboBox.addItem("蓝色");
                mainColorComboBox.addItem("红色");
                mainColorComboBox.addItem("绿色");
                mainColorComboBox.addItem("白色");
                mainColorComboBox.addItem("黑色");

                MainColorSets.add(mainColorLabel);
                MainColorSets.add(mainColorComboBox);
            }

            //主题设置
            JPanel MainThemeSets = new JPanel();
            {

                MainThemeSets.setLayout(new FlowLayout(FlowLayout.LEFT));
                MainThemeSets.setBackground(CTColor.backColor);

                JLabel mainThemeLabel = new JLabel("主题:");
                mainThemeLabel.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.SMALL));
                mainThemeLabel.setForeground(CTColor.textColor);
                //mainThemeLabel.setSize(50, 30);

                mainThemeComboBox.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.SMALL));
                mainThemeComboBox.setForeground(CTColor.textColor);
                mainThemeComboBox.setBackground(CTColor.backColor);

                //添加主题项目
                mainThemeComboBox.removeAllItems();
                mainThemeComboBox.addItem("浅色");
                mainThemeComboBox.addItem("深色");

                MainThemeSets.add(mainThemeLabel);
                MainThemeSets.add(mainThemeComboBox);
            }

            ColorPanel.add(MainColorSets);
            ColorPanel.add(MainThemeSets);

        }

        JPanel disposePanel = new JPanel();
        disposePanel.setBackground(CTColor.backColor);
        disposePanel.setLayout(new GridLayout(0, 2));
        //设置边框
        disposePanel.setBorder(BorderFactory.createTitledBorder("隐藏部分按钮"));
        {

            FinalPanel.allButList.forEach(button -> {
                JCheckBox checkBox = new JCheckBox(button.getName());
                checkBox.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.SMALL));
                checkBox.setBackground(CTColor.backColor);
                checkBox.setForeground(CTColor.textColor);
                disposeButton.put(button.getName(), checkBox);
            });

            disposeButton.forEach((key, value) -> {
                disposePanel.add(value);
            });
        }

        JPanel otherPanel = new JPanel();
        otherPanel.setBackground(CTColor.backColor);
        otherPanel.setLayout(new GridLayout(0, 2));
        otherPanel.setBorder(BorderFactory.createTitledBorder("其他设置"));
        //其他设置
        {
            startUp.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.SMALL));
            startUp.setForeground(CTColor.textColor);
            startUp.setBackground(CTColor.backColor);

            canExit.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.SMALL));
            canExit.setForeground(CTColor.textColor);
            canExit.setBackground(CTColor.backColor);

            StartUpdate.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.SMALL));
            StartUpdate.setForeground(CTColor.textColor);
            StartUpdate.setBackground(CTColor.backColor);
            StartUpdate.setSelected(true);

            otherPanel.add(startUp);
            otherPanel.add(canExit);
            otherPanel.add(StartUpdate);


        }

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        SetsPanel.add(ColorPanel, gbc);
        gbc.gridy = 1;
        SetsPanel.add(disposePanel, gbc);
        gbc.gridy = 2;
        SetsPanel.add(otherPanel, gbc);

        this.setLayout(new BorderLayout());
        this.setBackground(CTColor.backColor);
        this.add(mainPanelScroll, BorderLayout.CENTER);

        //显示数据
        {
            IOForInfo io = new IOForInfo(new File(Main.DATA_PATH + "setUp.json"));
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(io.GetInfos());
            } catch (Exception e) {
                Log.err.print("InfSetDialog", "读取设置文件失败: " + e.getMessage());
                jsonObject = new JSONObject();
            }

            //主题色设置
            if (jsonObject.has("mainColor")) {
                switch (jsonObject.getString("mainColor")) {
                    case "black" -> mainColorComboBox.setSelectedIndex(4);
                    case "white" -> mainColorComboBox.setSelectedIndex(3);
                    case "green" -> mainColorComboBox.setSelectedIndex(2);
                    case "red" -> mainColorComboBox.setSelectedIndex(1);
                    default -> mainColorComboBox.setSelectedIndex(0);
                }
            }
            //主题设置
            if (jsonObject.has("mainTheme")) {
                switch (jsonObject.getString("mainTheme")) {
                    case "dark" -> mainThemeComboBox.setSelectedIndex(1);
                    default -> mainThemeComboBox.setSelectedIndex(0);
                }
            }

            if (jsonObject.has("disposeButton")) {
                JSONArray JSONArrdisButton = jsonObject.getJSONArray("disposeButton");
                for (int i = 0; i < JSONArrdisButton.length(); i++) {
                    String s = JSONArrdisButton.getString(i);
                    if (disposeButton.containsKey(s)) {
                        disposeButton.get(s).setSelected(true);
                    }
                    /*switch (JSONArrdisButton.getString(i)) {
                        case "cookie" -> disposeButton.get("cookie").setSelected(true);
                        case "about" -> disposeButton.get("about").setSelected(true);
                        case "update" -> disposeButton.get("update").setSelected(true);
                        case "settings" -> disposeButton.get("settings").setSelected(true);
                    }*/
                }
            }
            //是否可关闭
            if (jsonObject.has("canExit")) {
                canExit.setSelected(!jsonObject.getBoolean("canExit"));

            }
            //是否自动更新
            if (jsonObject.has("StartUpdate")) {
                StartUpdate.setSelected(jsonObject.getBoolean("StartUpdate"));
            }
            //是否自动启动
            startUp.setSelected(SetStartUp.isAutoStartEnabled());
            /*if (jsonObject.has("isAutoStart")){
                startUp.setSelected(jsonObject.getBoolean("isAutoStart"));
            }*/
        }
    }

    @Override
    public void save() {
        //保存数据-个性化
        {
            IOForInfo io = new IOForInfo(new File(Main.DATA_PATH + "setUp.json"));

            //设置主题色
            JSONObject jsonObject = new JSONObject();
            String tempMainColor = switch (Objects.requireNonNull(mainColorComboBox.getSelectedItem()).toString()) {
                case "黑色" -> "black";
                case "白色" -> "white";
                case "绿色" -> "green";
                case "红色" -> "red";
                default -> "blue";
            };
            jsonObject.put("mainColor", tempMainColor);

            //设置主题
            String tempMainThemeColor = switch (Objects.requireNonNull(mainThemeComboBox.getSelectedItem()).toString()) {
                case "深色" -> "dark";
                default -> "light";
            };
            jsonObject.put("mainTheme", tempMainThemeColor);

            ArrayList<String> tempList = new ArrayList<>();
            disposeButton.forEach((key, value) -> {
                if (value.isSelected()) {
                    tempList.add(key);
                }
            });
            jsonObject.put("disposeButton", tempList);
            //设置是否可退出
            jsonObject.put("canExit", !canExit.isSelected());

            //设置启动时是否更新
            jsonObject.put("StartUpdate", StartUpdate.isSelected());

            //设置是否自动启动
            jsonObject.put("isAutoStart", startUp.isSelected());
            String Path = SetStartUp.getFilePath();
            if (!startUp.isSelected()) {
                SetStartUp.disableAutoStart();// 移除自启动
            } else {
                if (Path != null) {
                    if (Path.endsWith(".jar")) {
                        SetStartUp.enableAutoStart("javaw -jar " + Path); // 使用javaw避免黑窗口
                    } else if (Path.endsWith(".exe")) {
                        SetStartUp.enableAutoStart(Path);
                    }
                }

            }

            Log.info.print("InfSetDialog", "保存数据: " + jsonObject.toString());
            try {
                io.SetInfo(jsonObject.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @Override
    public void refresh() throws IOException {
        initUI();

        this.revalidate();
        this.repaint();
    }
}
