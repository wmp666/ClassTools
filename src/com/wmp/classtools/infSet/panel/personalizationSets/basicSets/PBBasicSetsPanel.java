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

public class PBBasicSetsPanel extends CTBasicSetsPanel {
    //颜色 数据
    private final CTComboBox mainColorComboBox = new CTComboBox();
    private final CTComboBox mainThemeComboBox = new CTComboBox();
    private final CTComboBox FontNameComboBox = new CTComboBox();
    //字体 数据
    private final ArrayList<CTTextField> FontSizeList = new ArrayList<>();
    //隐藏按钮 数据
    private final TreeMap<String, CTCheckBox> disposeButton = new TreeMap<>();
    //其他数据
    private final CTCheckBox StartUpdate = new CTCheckBox("启动检查更新");
    private final CTCheckBox canExit = new CTCheckBox("防止被意外关闭");
    private final CTCheckBox startUp = new CTCheckBox("开机自启动");

    public PBBasicSetsPanel(String basicDataPath) {
        super(basicDataPath);

        setName("个性化设置");

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


        JPanel ColorPanel = new JPanel();
        ColorPanel.setOpaque(false);
        ColorPanel.setLayout(new GridLayout(1, 2));
        ColorPanel.setBorder(CTBorderFactory.createTitledBorder("颜色设置"));
        //颜色设置
        {
            //主题色设置
            JPanel MainColorSets = new JPanel();
            MainColorSets.setOpaque(false);
            {

                MainColorSets.setLayout(new FlowLayout(FlowLayout.LEFT));

                JLabel mainColorLabel = new JLabel("主题色:");
                mainColorLabel.setForeground(CTColor.textColor);
                mainColorLabel.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));


                mainColorComboBox.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));

                //添加颜色项目
                mainColorComboBox.removeAllItems();
                mainColorComboBox.addItems("蓝色", "红色", "绿色", "白色", "黑色");

                MainColorSets.add(mainColorLabel);
                MainColorSets.add(mainColorComboBox);
            }

            //主题设置
            JPanel MainThemeSets = new JPanel();
            MainThemeSets.setOpaque(false);
            {

                MainThemeSets.setLayout(new FlowLayout(FlowLayout.LEFT));

                JLabel mainThemeLabel = new JLabel("主题:");
                mainThemeLabel.setForeground(CTColor.textColor);
                mainThemeLabel.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));

                mainThemeComboBox.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));

                //添加主题项目
                mainThemeComboBox.removeAllItems();
                mainThemeComboBox.addItems("浅色", "深色");

                MainThemeSets.add(mainThemeLabel);
                MainThemeSets.add(mainThemeComboBox);
            }

            ColorPanel.add(MainColorSets);
            ColorPanel.add(MainThemeSets);

        }

        JPanel reSetFontPanel = new JPanel();
        reSetFontPanel.setOpaque(false);
        reSetFontPanel.setLayout(new GridBagLayout());
        reSetFontPanel.setBorder(CTBorderFactory.createTitledBorder("字体设置"));
        //字体设置
        {
            //字体设置
            JPanel setFontName = new JPanel();
            setFontName.setOpaque(false);
            {

                setFontName.setLayout(new FlowLayout(FlowLayout.LEFT));

                JLabel FontNameLabel = new JLabel("字体:");
                FontNameLabel.setForeground(CTColor.textColor);
                FontNameLabel.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));

                FontNameComboBox.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));

                //添加颜色项目
                FontNameComboBox.removeAllItems();
                FontNameComboBox.addItems(CTFont.getAllFontName());


                setFontName.add(FontNameLabel);
                setFontName.add(FontNameComboBox);
            }
            //字体大小设置
            JPanel setFontSize = new JPanel();
            setFontSize.setOpaque(false);
            {
                setFontSize.setLayout(new FlowLayout(FlowLayout.LEFT));
                JLabel FontSizeLabel = new JLabel("字体大小-大:");
                FontSizeLabel.setForeground(CTColor.textColor);
                FontSizeLabel.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));
                CTTextField FontSizeTextField = new CTTextField();
                FontSizeTextField.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));
            }

            JButton button = new JButton("改为默认");
            button.setFont(new Font("微软雅黑", Font.PLAIN, CTFont.getSize(CTFontSizeStyle.SMALL)));
            button.addActionListener(e -> {
                CTFont.setSize(100, 60, 24, 19, 15, 12);
                CTFont.setFontName("微软雅黑");
                FontNameComboBox.setSelectedItem("微软雅黑");
                FontSizeList.forEach(textField -> textField.setText(String.valueOf(CTFont.getBasicSize()[0])));
                MainWindow.allPanelList.forEach(ctPanel -> {
                    try {
                        ctPanel.refresh();
                    } catch (Exception ex) {
                        Log.err.print(getClass(), "刷新失败", ex);
                    }
                });
                save();
            });


            reSetFontPanel.add(setFontName);

            reSetFontPanel.add(button);

        }

        JPanel disButPanel = new JPanel();
        disButPanel.setOpaque(false);
        disButPanel.setLayout(new GridLayout(0, 2));
        //设置按钮隐藏
        disButPanel.setBorder(CTBorderFactory.createTitledBorder("隐藏部分按钮"));
        {

            FinalPanel.allButList.forEach(button -> {
                CTCheckBox checkBox = new CTCheckBox(button.getName());
                checkBox.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));
                checkBox.setBackground(CTColor.backColor);
                checkBox.setForeground(CTColor.textColor);
                disposeButton.put(button.getName(), checkBox);
            });

            disposeButton.forEach((key, value) -> {
                disButPanel.add(value);
            });
        }

        JPanel otherPanel = new JPanel();
        otherPanel.setOpaque(false);
        otherPanel.setLayout(new GridLayout(0, 2));
        otherPanel.setBorder(CTBorderFactory.createTitledBorder("其他设置"));
        //其他设置
        {

            startUp.setBackground(CTColor.backColor);
            startUp.setForeground(CTColor.textColor);
            startUp.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));

            canExit.setBackground(CTColor.backColor);
            canExit.setForeground(CTColor.textColor);
            canExit.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));

            StartUpdate.setBackground(CTColor.backColor);
            StartUpdate.setForeground(CTColor.textColor);
            StartUpdate.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));
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
        gbc.gridy++;
        SetsPanel.add(reSetFontPanel, gbc);
        gbc.gridy++;
        SetsPanel.add(disButPanel, gbc);
        gbc.gridy++;
        SetsPanel.add(otherPanel, gbc);

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

            //主题色设置
            if (jsonObject.has("mainColor")) {
                switch (jsonObject.getString("mainColor")) {
                    case "black" -> mainColorComboBox.setSelectedItem("黑色");
                    case "white" -> mainColorComboBox.setSelectedItem("白色");
                    case "green" -> mainColorComboBox.setSelectedItem("绿色");
                    case "red" -> mainColorComboBox.setSelectedItem("红色");
                    default -> mainColorComboBox.setSelectedItem("蓝色");
                }
            }
            //主题设置
            if (jsonObject.has("mainTheme")) {
                switch (jsonObject.getString("mainTheme")) {
                    case "dark" -> mainThemeComboBox.setSelectedItem("深色");
                    default -> mainThemeComboBox.setSelectedItem("浅色");
                }
            }
            //字体设置
            FontNameComboBox.setSelectedItem(CTFont.getFontName());
            //
            if (jsonObject.has("disposeButton")) {
                JSONArray JSONArrdisButton = jsonObject.getJSONArray("disposeButton");
                for (int i = 0; i < JSONArrdisButton.length(); i++) {
                    String s = JSONArrdisButton.getString(i);
                    if (disposeButton.containsKey(s)) {
                        disposeButton.get(s).setSelected(true);
                    }
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

        }
    }

    @Override
    public void save() {
        try {
            //保存数据-个性化
            {
                IOForInfo io = new IOForInfo(new File(CTInfo.DATA_PATH + "setUp.json"));
                JSONObject jsonObject = new JSONObject(io.getInfos());

                //设置主题色
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

                //设置字体
                String tempFontName = Objects.requireNonNull(FontNameComboBox.getSelectedItem(), "微软雅黑").toString();
                jsonObject.put("FontName", tempFontName);
                //设置隐藏按钮
                {
                    ArrayList<String> tempList = new ArrayList<>();
                    disposeButton.forEach((key, value) -> {
                        if (value.isSelected()) {
                            tempList.add(key);
                        }
                    });
                    jsonObject.put("disposeButton", tempList);
                }
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
