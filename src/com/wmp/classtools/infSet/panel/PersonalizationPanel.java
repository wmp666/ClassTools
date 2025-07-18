package com.wmp.classTools.infSet.panel;

import com.wmp.Main;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTComboBox;
import com.wmp.classTools.CTComponent.CTSetsPanel;
import com.wmp.classTools.CTComponent.CTTextField;
import com.wmp.classTools.frame.MainWindow;
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

    private final CTComboBox mainColorComboBox = new CTComboBox();
    private final CTComboBox mainThemeComboBox = new CTComboBox();
    private final CTComboBox FontNameComboBox = new CTComboBox();
    private final ArrayList<CTTextField> FontSizeList = new ArrayList<>();
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

        SetsPanel.setLayout(new GridBagLayout());//new GridLayout(0,1)
        GridBagConstraints gbc = new GridBagConstraints();


        JPanel ColorPanel = new JPanel();
        ColorPanel.setLayout(new GridLayout(1, 2));
        ColorPanel.setBorder(BorderFactory.createTitledBorder("颜色设置"));
        //颜色设置
        {
            //主题色设置
            JPanel MainColorSets = new JPanel();
            {

                MainColorSets.setLayout(new FlowLayout(FlowLayout.LEFT));

                JLabel mainColorLabel = new JLabel("主题色:");
                mainColorLabel.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.SMALL));


                mainColorComboBox.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.SMALL));

                //添加颜色项目
                mainColorComboBox.removeAllItems();
                mainColorComboBox.addItems("蓝色", "红色", "绿色", "白色", "黑色");

                MainColorSets.add(mainColorLabel);
                MainColorSets.add(mainColorComboBox);
            }

            //主题设置
            JPanel MainThemeSets = new JPanel();
            {

                MainThemeSets.setLayout(new FlowLayout(FlowLayout.LEFT));
                //MainThemeSets.setBackground(CTColor.backColor);

                JLabel mainThemeLabel = new JLabel("主题:");
                mainThemeLabel.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.SMALL));
                //mainThemeLabel.setForeground(CTColor.textColor);
                //mainThemeLabel.setSize(50, 30);

                mainThemeComboBox.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.SMALL));
                //mainThemeComboBox.setForeground(CTColor.textColor);
                //mainThemeComboBox.setBackground(CTColor.backColor);

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
        //reSetFontPanel.setBackground(CTColor.backColor);
        reSetFontPanel.setLayout(new GridBagLayout());
        reSetFontPanel.setBorder(BorderFactory.createTitledBorder("字体设置"));
        //字体设置
        {
            //字体设置
            JPanel setFontName = new JPanel();
            {

                setFontName.setLayout(new FlowLayout(FlowLayout.LEFT));
                //setFontName.setBackground(CTColor.backColor);

                JLabel FontNameLabel = new JLabel("字体:");
                FontNameLabel.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.SMALL));


                FontNameComboBox.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.SMALL));


                //添加颜色项目
                FontNameComboBox.removeAllItems();
                FontNameComboBox.addItems(CTFont.getAllFontName());


                setFontName.add(FontNameLabel);
                setFontName.add(FontNameComboBox);
            }
            //字体大小设置
            JPanel setFontSize = new JPanel();
            {
                setFontSize.setLayout(new FlowLayout(FlowLayout.LEFT));
                //setFontSize.setBackground(CTColor.backColor);
                JLabel FontSizeLabel = new JLabel("字体大小-大:");
                FontSizeLabel.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.SMALL));
                //FontSizeLabel.setForeground(CTColor.textColor);
                CTTextField FontSizeTextField = new CTTextField();
                FontSizeTextField.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.SMALL));
                //FontSizeTextField.setForeground(CTColor.textColor);
            }

            JButton button = new JButton("改为默认");
            //button.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
            button.addActionListener(e -> {
                CTFont.setSize(100, 60, 24, 19, 15, 12);
                CTFont.setFontName("微软雅黑");
                FontNameComboBox.setSelectedItem("微软雅黑");
                FontSizeList.forEach(textField -> textField.setText(String.valueOf(CTFont.getSize()[0])));
                MainWindow.showPanelList.forEach(ctPanel -> {
                    try {
                        ctPanel.refresh();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                save();
            });



            reSetFontPanel.add(setFontName);

            reSetFontPanel.add(button);

        }

        JPanel disposePanel = new JPanel();
        //disposePanel.setBackground(CTColor.backColor);
        disposePanel.setLayout(new GridLayout(0, 2));
        //设置按钮隐藏
        disposePanel.setBorder(BorderFactory.createTitledBorder("隐藏部分按钮"));
        {

            FinalPanel.allButList.forEach(button -> {
                JCheckBox checkBox = new JCheckBox(button.getName());
                checkBox.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.SMALL));
                //checkBox.setBackground(CTColor.backColor);
                //checkBox.setForeground(CTColor.textColor);
                disposeButton.put(button.getName(), checkBox);
            });

            disposeButton.forEach((key, value) -> {
                disposePanel.add(value);
            });
        }

        JPanel otherPanel = new JPanel();
        //otherPanel.setBackground(CTColor.backColor);
        otherPanel.setLayout(new GridLayout(0, 2));
        otherPanel.setBorder(BorderFactory.createTitledBorder("其他设置"));
        //其他设置
        {
            startUp.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.SMALL));

            canExit.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.SMALL));

            StartUpdate.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.SMALL));
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
        SetsPanel.add(disposePanel, gbc);
        gbc.gridy++;
        SetsPanel.add(otherPanel, gbc);

        this.setLayout(new BorderLayout());
        //this.setBackground(CTColor.backColor);
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
        //保存数据-个性化
        {
            IOForInfo io = new IOForInfo(new File(Main.DATA_PATH + "setUp.json"));
            JSONObject jsonObject = new JSONObject();

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
