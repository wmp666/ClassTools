package com.wmp.classTools.infSet;

import com.wmp.Main;
import com.wmp.PublicTools.OpenInExp;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.GetIcon;
import com.wmp.PublicTools.io.GetPath;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.io.ZipPack;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTButton;
import com.wmp.classTools.CTComponent.CTPanel;
import com.wmp.classTools.CTComponent.CTSetsPanel;
import com.wmp.classTools.frame.MainWindow;
import com.wmp.classTools.infSet.tools.SetStartUp;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.TreeMap;

public class InfSetDialog extends JDialog {

    private final Container c;
    private final Runnable refreshCallback;

    private final ArrayList<CTSetsPanel> ctSetsPanelList = new ArrayList<>();

    private final JComboBox<String> mainColorComboBox = new JComboBox<>();
    private final JComboBox<String> mainThemeComboBox = new JComboBox<>();
    private final TreeMap<String, JCheckBox> disposeButton = new TreeMap<>();
    private final JCheckBox StartUpdate = new JCheckBox("启动检查更新");
    private final JCheckBox canExit = new JCheckBox("防止被意外关闭");
    private final JCheckBox startUp = new JCheckBox("开机自启动");

    private String openedPanel = "迟到人员";
    // 添加文件路径参数
    public InfSetDialog(Runnable refreshCallback) throws IOException {

        this.setBackground(CTColor.backColor);
        this.setIconImage(GetIcon.getImageIcon(getClass().getResource("/image/light/settings_0.png"), 32, 32).getImage());
        this.setTitle("设置");
        this.setSize(400, 550);
        this.setLocationRelativeTo(null);
        this.setModal(true);
        //this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        this.c = this.getContentPane();
        this.refreshCallback = refreshCallback;

        ArrayList<CTPanel> showPanelList = MainWindow.showPanelList;
        showPanelList.forEach(ctPanel -> {
            ArrayList<CTSetsPanel> tempCTSetsPanelList = ctPanel.getCtSetsPanelList();
            if (tempCTSetsPanelList != null) {
                ctSetsPanelList.addAll(tempCTSetsPanelList);
            }
        });

        initMenuBar();


        c.setLayout(new BorderLayout());// 设置布局为边界布局

        initSaveButton();
        ctSetsPanelList.forEach(ctSetsPanel -> {
            if (ctSetsPanel.getName().equals("迟到人员"))
                c.add(ctSetsPanel, BorderLayout.CENTER);
        });
        c.add(initSetsPanelSwitchBar(), BorderLayout.NORTH);


        initPersonalization();

    }

    private JPanel initPersonalization() throws IOException {
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
        ColorPanel.setLayout(new GridLayout(1,2));
        ColorPanel.setBorder(BorderFactory.createTitledBorder("颜色设置"));
        //颜色设置
        {
            //主题色设置
            JPanel MainColorSets = new JPanel();
            {

                MainColorSets.setLayout(new FlowLayout(FlowLayout.LEFT));
                MainColorSets.setBackground(CTColor.backColor);

                JLabel mainColorLabel = new JLabel("主题色:");
                mainColorLabel.setFont(new Font("微软雅黑", -1, 15));
                mainColorLabel.setForeground(CTColor.textColor);
                //mainColorLabel.setSize(50, 30);


                mainColorComboBox.setFont(new Font("微软雅黑", -1, 15));
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
                mainThemeLabel.setFont(new Font("微软雅黑", -1, 15));
                mainThemeLabel.setForeground(CTColor.textColor);
                //mainThemeLabel.setSize(50, 30);

                mainThemeComboBox.setFont(new Font("微软雅黑", -1, 15));
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
            JCheckBox cookie = new JCheckBox("插件管理页");
            cookie.setFont(new Font("微软雅黑", -1, 15));
            cookie.setBackground(CTColor.backColor);
            cookie.setForeground(CTColor.textColor);
            disposeButton.put("cookie", cookie);

            JCheckBox about = new JCheckBox("软件信息");
            about.setFont(new Font("微软雅黑", -1, 15));
            about.setBackground(CTColor.backColor);
            about.setForeground(CTColor.textColor);
            disposeButton.put("about", about);

            JCheckBox update = new JCheckBox("更新");
            update.setFont(new Font("微软雅黑", -1, 15));
            update.setBackground(CTColor.backColor);
            update.setForeground(CTColor.textColor);
            disposeButton.put("update", update);

            JCheckBox settings = new JCheckBox("设置");
            settings.setFont(new Font("微软雅黑", -1, 15));
            settings.setBackground(CTColor.backColor);
            settings.setForeground(CTColor.textColor);
            disposeButton.put("settings", settings);

            disposeButton.forEach((key, value) -> {
                disposePanel.add(value);
            });
        }

        JPanel otherPanel = new JPanel();
        otherPanel.setBackground(CTColor.backColor);
        otherPanel.setLayout(new GridLayout(0,2));
        otherPanel.setBorder(BorderFactory.createTitledBorder("其他设置"));
        //其他设置
        {
            startUp.setFont(new Font("微软雅黑", -1, 15));
            startUp.setForeground(CTColor.textColor);
            startUp.setBackground(CTColor.backColor);

            canExit.setFont(new Font("微软雅黑", -1, 15));
            canExit.setForeground(CTColor.textColor);
            canExit.setBackground(CTColor.backColor);

            StartUpdate.setFont(new Font("微软雅黑", -1, 15));
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

        JPanel tempPanel = new JPanel();
        tempPanel.setLayout(new BorderLayout());
        tempPanel.setBackground(CTColor.backColor);
        tempPanel.add(mainPanelScroll, BorderLayout.CENTER);

        //显示数据
        {
            IOForInfo io = new IOForInfo(new File(Main.DATA_PATH + "setUp.json"));
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(io.GetInfo()[0]);
            }catch (Exception e){
                Log.error.print("InfSetDialog", "读取设置文件失败: " + e.getMessage());
                return tempPanel;

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

            if (jsonObject.has("disposeButton")){
                JSONArray JSONArrdisButton = jsonObject.getJSONArray("disposeButton");
                for (int i = 0; i < JSONArrdisButton.length(); i++) {
                    String s = JSONArrdisButton.getString(i);
                    if (disposeButton.containsKey(s)){
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

        return tempPanel;
    }


    private JPanel initClearTemp() throws IOException {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new GridLayout( 0, 1, 5,5));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBounds(0, 0, 400, 45);

        CTButton clearTemp = new CTButton(CTButton.ButtonText, "清除临时文件",
                "/image/%s/delete_0.png",
                "/image/%s/delete_1.png", 35, 150, () -> {
            try {
                IOForInfo.deleteDirectoryRecursively(Paths.get(Main.TEMP_PATH));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        buttonPanel.add(clearTemp);

        CTButton clearLog = new CTButton(CTButton.ButtonText, "清除日志",
                "/image/%s/delete_0.png",
                "/image/%s/delete_1.png", 35, 150, () -> {
            try {
                IOForInfo.deleteDirectoryRecursively(Paths.get(Main.DATA_PATH + "Log\\"));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        buttonPanel.add(clearLog);

        mainPanel.add(buttonPanel);

        return mainPanel;
    }

    private JPanel initSetsPanelSwitchBar() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new GridBagLayout());

        {
            JButton button = new JButton("个性化");
            if (openedPanel.equals("个性化")) {
                button.setForeground(new Color(0x0090FF));
            } else {
                button.setForeground(Color.BLACK);
            }
            button.setBackground(Color.WHITE);
            button.setFont(new Font("微软雅黑", Font.BOLD, 12));
            button.setBorderPainted(false);
            button.setFocusPainted(false);// 去除按钮的焦点边框
            button.addActionListener(e -> {
                openedPanel = "个性化";
                try {
                    this.repaintSetsPanel(initPersonalization());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            mainPanel.add(button);
        }


        this.ctSetsPanelList.forEach(ctSetsPanel -> {
            JButton button = new JButton(ctSetsPanel.getName());
            if (openedPanel.equals(ctSetsPanel.getName())) {
                button.setForeground(new Color(0x0090FF));
            } else {
                button.setForeground(Color.BLACK);
            }
            button.setBackground(Color.WHITE);
            button.setFont(new Font("微软雅黑", Font.BOLD, 12));
            button.setBorderPainted(false);
            button.setFocusPainted(false);// 去除按钮的焦点边框
            button.addActionListener(e -> {
                openedPanel = ctSetsPanel.getName();
                try {
                    this.repaintSetsPanel(ctSetsPanel);
                } catch (MalformedURLException ex) {
                    throw new RuntimeException(ex);
                }
            });
            mainPanel.add(button);
        });
        {
            JButton button = new JButton("清除临时文件");
            if (openedPanel.equals("清除临时文件")) {
                button.setForeground(new Color(0xFF0000));
            } else {
                button.setForeground(Color.BLACK);
            }
            button.setBackground(Color.WHITE);
            button.setFont(new Font("微软雅黑", Font.BOLD, 12));
            button.setBorderPainted(false);
            button.setFocusPainted(false);// 去除按钮的焦点边框
            button.addActionListener(e -> {
                openedPanel = "清除临时文件";
                try {
                    this.repaintSetsPanel(initClearTemp());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            mainPanel.add(button);
        }
        JScrollPane mainPanelScroll = new JScrollPane(mainPanel);
        mainPanelScroll.setBorder(BorderFactory.createEmptyBorder());
        mainPanelScroll.getViewport().setBackground(Color.WHITE);
        mainPanelScroll.getVerticalScrollBar().setUnitIncrement(16);
        JPanel tempPanel = new JPanel();
        tempPanel.setBackground(Color.WHITE);
        tempPanel.add(mainPanelScroll);
        return tempPanel;
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(CTColor.backColor);
        this.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("文件");
        //fileMenu.setIcon(getClass().getResource("/image/file.png"));

        JMenu openFile = new JMenu("打开文件");
        openFile.setIcon(GetIcon.getIcon(getClass().getResource("/image/openExp.png"), 16, 16));

        JMenuItem openAppList = new JMenuItem("软件位置");
        openAppList.setIcon(GetIcon.getIcon(getClass().getResource("/image/openExp.png"), 16, 16));
        openAppList.addActionListener(e -> {
            OpenInExp.open(System.getProperty("user.dir"));
        });

        JMenuItem openSetsList = new JMenuItem("数据位置");
        openSetsList.setIcon(GetIcon.getIcon(getClass().getResource("/image/openExp.png"), 16, 16));
        openSetsList.addActionListener(e -> {
            OpenInExp.open(Main.DATA_PATH);
        });

        JMenu InfSets = new JMenu("数据设置");
        InfSets.setIcon(GetIcon.getIcon(getClass().getResource("/image/light/setting_0.png"), 16, 16));

        JMenu getInf = new JMenu("导入数据");
        getInf.setIcon(GetIcon.getIcon(getClass().getResource("/image/input.png"), 16, 16));

        JMenuItem getAllInf = new JMenuItem("导入所有数据(.ctdatas)");
        getAllInf.setIcon(GetIcon.getIcon(getClass().getResource("/image/input.png"), 16, 16));
        getAllInf.addActionListener(e -> {
            String filePath = GetPath.getFilePath(this, "请选择所有数据", ".ctdatas", "ClassTools");
            ZipPack.unzip(filePath, Main.DATA_PATH);
            //刷新数据
            this.setVisible(false);
            refreshCallback.run();
        });

        JMenu inputInf = new JMenu("导出数据");
        inputInf.setIcon(GetIcon.getIcon(getClass().getResource("/image/light/update_0.png"), 16, 16));

        JMenuItem inputAllInf = new JMenuItem("导出所有数据(.ctdatas)");
        inputAllInf.setIcon(GetIcon.getIcon(getClass().getResource("/image/light/update_0.png"), 16, 16));
        inputAllInf.addActionListener(e -> {
            String path = GetPath.getDirectoryPath(this, "请选择导出目录");
            //将ClassTools文件夹中的文件打包为.zip
            ZipPack.createZip(path, Main.DATA_PATH, "ClassTools.ctdatas");
        });


        JMenuItem exitMenuItem = new JMenuItem("退出");
        exitMenuItem.setIcon(GetIcon.getIcon(getClass().getResource("/image/light/exit_0.png"), 16, 16));
        exitMenuItem.addActionListener(e -> {
            save();
            this.setVisible(false);
        });

        openFile.add(openAppList);
        openFile.add(openSetsList);

        getInf.add(getAllInf);

        inputInf.add(inputAllInf);

        InfSets.add(getInf);
        InfSets.add(inputInf);

        fileMenu.add(openFile);
        fileMenu.add(InfSets);
        fileMenu.add(exitMenuItem);


        menuBar.add(fileMenu);

        //编辑
        JMenu editMenu = new JMenu("编辑");

        JMenu setMenu = new JMenu("设置界面");
        setMenu.setIcon(GetIcon.getIcon(getClass().getResource("/image/light/settings_0.png"), 16, 16));

        JMenuItem PersonalizationMenuItem = new JMenuItem("个性化");
        PersonalizationMenuItem.addActionListener(e -> {
            try {
                repaintSetsPanel(initPersonalization());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        JMenuItem ClearTempMenuItem = new JMenuItem("清除临时文件");
        ClearTempMenuItem.addActionListener(e -> {
            try {
                repaintSetsPanel(initClearTemp());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });


        ctSetsPanelList.forEach(ctSetsPanel -> {
            JMenuItem tempMenuItem = new JMenuItem(ctSetsPanel.getName());
            tempMenuItem.addActionListener(e -> {
                try {
                    repaintSetsPanel(ctSetsPanel);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            setMenu.add(tempMenuItem);
        });

        JMenuItem saveMenuItem = new JMenuItem("保存");
        saveMenuItem.setIcon(GetIcon.getIcon(getClass().getResource("/image/light/save_0.png"), 16, 16));
        saveMenuItem.addActionListener(e -> {
            save();

        });


        setMenu.add(PersonalizationMenuItem);
        setMenu.add(ClearTempMenuItem);

        editMenu.add(setMenu);
        editMenu.add(saveMenuItem);

        menuBar.add(editMenu);


    }

    private void initSaveButton() throws MalformedURLException {
        CTButton saveButton = new CTButton(CTButton.ButtonText, "保存数据",
                "/image/%s/save_0.png",
                "/image/%s/save_1.png", 200, 50, this::save);
        c.add(saveButton, BorderLayout.SOUTH);

    }

    private void repaintSetsPanel(JPanel panel) throws MalformedURLException {

            c.removeAll();
        c.add(initSetsPanelSwitchBar(), BorderLayout.NORTH);
            initMenuBar();
            initSaveButton();
            c.add(panel, BorderLayout.CENTER);

            c.revalidate();
            c.repaint();

    }

    private void save() {
        int result = Log.info.inputInt(this, "InfSetDialog-保存", "是否保存？");
        if (result == JOptionPane.YES_OPTION) {
            try {


                ctSetsPanelList.forEach(CTSetsPanel::save);

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
                        if (Path != null){
                            if (Path.endsWith(".jar")){
                                SetStartUp.enableAutoStart("javaw -jar " + Path ); // 使用javaw避免黑窗口
                            } else if (Path.endsWith(".exe")) {
                                SetStartUp.enableAutoStart(Path);
                            }
                        }

                    }

                    Log.info.print("InfSetDialog", "保存数据: " + jsonObject.toString());
                    io.SetInfo(jsonObject.toString());

                }

                // 保存成功后执行回调
                refreshCallback.run();
                //this.setVisible(false);

            } catch (IOException ex) {
                Log.error.print("InfSetDialog", "保存数据失败: " + ex.getMessage());
            }
        }
    }

}
