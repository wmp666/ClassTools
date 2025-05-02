package com.wmp.classTools.infSet;

import com.wmp.CTColor;
import com.wmp.Main;
import com.wmp.PublicTools.GetIcon;
import com.wmp.PublicTools.InfProcess;
import com.wmp.PublicTools.OpenInExp;
import com.wmp.PublicTools.io.GetPath;
import com.wmp.PublicTools.io.IOStreamForInf;
import com.wmp.PublicTools.io.ZipPack;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTButton;
import com.wmp.classTools.infSet.DataStyle.AllStu;
import com.wmp.classTools.infSet.DataStyle.Duty;
import com.wmp.classTools.infSet.tools.GetExcelData;
import com.wmp.classTools.infSet.tools.SetStartUp;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InfSetDialog extends JDialog implements WindowListener {

    private final Container c;
    private final File AllStuPath;
    private final File leaveListPath;
    private final File DutyListPath;
    private final File indexPath;
    private final Runnable refreshCallback;

    private final ArrayList<JCheckBox> checkBoxList = new ArrayList<>();
    private final AtomicInteger index = new AtomicInteger();
    private final JTable DutyTable = new JTable();
    private final JTable allStuTable = new JTable();

    private final JComboBox<String> mainColorComboBox = new JComboBox<>();
    private final JComboBox<String> mainThemeComboBox = new JComboBox<>();
    private final TreeMap<String, JCheckBox> disposeButton = new TreeMap<>();
    private final JCheckBox StartUpdate = new JCheckBox("启动检查更新");
    private final JCheckBox canExit = new JCheckBox("防止被意外关闭");
    private final JCheckBox startUp = new JCheckBox("开机自启动");

    private final ArrayList<String> leaveList;
    private final ArrayList<String> studentList;
    private final String[][] dutyList;


    // 添加文件路径参数
    public InfSetDialog(File AllStuPath, File leaveListPath, File DutyListPath, File indexPath, Runnable refreshCallback) throws IOException {

        this.setBackground(CTColor.backColor);
        this.setIconImage(GetIcon.getImageIcon(getClass().getResource("/image/light/settings_0.png"), 32, 32).getImage());
        this.setTitle("设置");
        this.setSize(400, 500);
        this.setLocationRelativeTo(null);
        this.setModal(true);
        //this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.addWindowListener(this);
        c = this.getContentPane();
        this.AllStuPath = AllStuPath;
        this.leaveListPath = leaveListPath;
        this.DutyListPath = DutyListPath;
        this.indexPath = indexPath;
        this.refreshCallback = refreshCallback;

        initMenuBar();

        this.leaveList = getLeaveList();
        this.studentList = getStudentList();
        this.dutyList = getDutyList(DutyListPath);

        index.set(Integer.parseInt(new IOStreamForInf(indexPath).GetInf()[0]));
        c.setLayout(new BorderLayout());// 设置布局为边界布局

        initSaveButton();
        c.add(initATSet(this.studentList, this.leaveList), BorderLayout.CENTER);

        initDuSet(this.dutyList);

        initAllStuSet(this.studentList);

        initPersonalization();

    }

    private JPanel initPersonalization() throws IOException {
        JPanel SetsPanel = new JPanel();
        JScrollPane mainPanelScroll = new JScrollPane(SetsPanel);
        //调整滚动灵敏度
        mainPanelScroll.getVerticalScrollBar().setUnitIncrement(16);
        mainPanelScroll.setSize(400, 400);

        SetsPanel.setBackground(CTColor.backColor);
        SetsPanel.setLayout(new GridLayout(0,1));

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


        SetsPanel.add(ColorPanel);
        SetsPanel.add(disposePanel);
        SetsPanel.add(otherPanel);

        JPanel tempPanel = new JPanel();
        tempPanel.setLayout(new BorderLayout());
        tempPanel.setBackground(CTColor.backColor);
        tempPanel.add(mainPanelScroll, BorderLayout.CENTER);

        //显示数据
        {
            IOStreamForInf io = new IOStreamForInf(new File(Main.DATA_PATH + "setUp.json"));
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(io.GetInf()[0]);
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
    private JPanel initDuSet(String [][] dutyList) throws IOException {

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(CTColor.backColor);
        mainPanel.setLayout(null);

        DefaultTableModel model = new DefaultTableModel(dutyList,
                new String[]{"扫地", "擦黑板"});
        //设置表格的两列不可以修改顺序
        DutyTable.getTableHeader().setReorderingAllowed(false);
        DutyTable.setFont(new Font("微软雅黑", -1, 15));
        DutyTable.setModel(model);

        JScrollPane scrollPane = new JScrollPane(DutyTable);
        scrollPane.setBounds(20, 30, 340, 300);
        scrollPane.setBackground(CTColor.backColor);
        mainPanel.add(scrollPane);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(CTColor.backColor);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBounds(0, 340, 400, 45);

        //新建
        {

            CTButton newBtn = new CTButton("添加新的值日生记录",
                    "/image/%s/new_0.png",
                    "/image/%s/new_1.png", 30, () -> {
                //检测内容是否为空
                boolean b = true;
                String s1 = "null";
                String s2 = "null";
                while (b) {
                    s1 =  Log.info.input(c, "InfSetDialog-新建", "请输入擦黑板人员");

                    if (s1 != null && !s1.trim().isEmpty() && !(s1.indexOf('[') != -1 || s1.indexOf(']') != -1)) {
                        b = false;
                    } else if (s1 == null) {
                        return;
                    }
                }

                b = true;
                while (b) {
                    s2 = Log.info.input(c, "InfSetDialog-新建", "请输入扫地人员");
                    if (s2 != null && !s2.trim().isEmpty() && !(s2.indexOf('[') != -1 || s2.indexOf(']') != -1)) {
                        b = false;
                    } else if (s2 == null) {
                        return;
                    }
                }

                model.addRow(new Object[]{s2, s1});

            });
            //newBtn.setToolTipText("添加新的值日生记录");
            //newBtn.setLocation(255, 340);
            buttonPanel.add(newBtn);
        }

        // 删除
        {

            CTButton deleteBtn = new CTButton("删除选中的值日生记录",
                    "/image/%s/delete_0.png",
                    "/image/%s/delete_1.png", 35, () -> {

                int selectedRow = DutyTable.getSelectedRow();
                if (selectedRow != -1) {
                    model.removeRow(selectedRow);
                    if (selectedRow < index.get()) {
                        index.getAndDecrement();
                    }
                    if (DutyTable.getRowCount() == index.get()) {
                        index.set(0);
                    }
                }
            });
            //deleteBtn.setToolTipText("删除选中的值日生记录");
            //deleteBtn.setLocation(255, 380);
            buttonPanel.add(deleteBtn);
        }

        mainPanel.add(buttonPanel);

        return mainPanel;
    }

    private JPanel initAllStuSet(ArrayList<String> studentList) throws IOException {

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(CTColor.backColor);
        mainPanel.setLayout(null);

        String[][] studentListTemp = new String[studentList.size()][2];
        for (int i = 0; i < studentList.size(); i++) {
            studentListTemp[i][0] = String.valueOf(i + 1);
            studentListTemp[i][1] = studentList.get(i);
        }

        DefaultTableModel model = new DefaultTableModel(studentListTemp,
                new String[]{"序号", "姓名"});

        allStuTable.getTableHeader().setReorderingAllowed(false);
        allStuTable.setFont(new Font("微软雅黑", -1, 12));
        allStuTable.setModel(model);

        JScrollPane scrollPane = new JScrollPane(allStuTable);
        scrollPane.setBounds(20, 30, 340, 300);
        scrollPane.setBackground(CTColor.backColor);
        mainPanel.add(scrollPane);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(CTColor.backColor);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBounds(0, 340, 400, 45);


        //新建
        {

            CTButton newBtn = new CTButton("添加新的项",
                    "/image/%s/new_0.png",
                    "/image/%s/new_1.png", 30, () -> {
                //检测内容是否为空
                boolean b = true;
                String s1 = "null";
                while (b) {
                    s1 = Log.info.input(c, "InfSetDialog-新建", "请输入姓名");
                    if (s1 != null && !s1.trim().isEmpty()) {
                        b = false;
                    } else if (s1 == null) {
                        return;
                    }
                }

                model.addRow(new Object[]{String.valueOf(model.getRowCount() + 1), s1});

            });
            buttonPanel.add(newBtn);
        }

        // 删除
        {

            CTButton deleteBtn = new CTButton("删除选中的项",
                    "/image/%s/delete_0.png",
                    "/image/%s/delete_1.png", 35, () -> {


                try {
                    index.set(Integer.parseInt(new IOStreamForInf(indexPath).GetInf()[0]));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                int selectedRow = allStuTable.getSelectedRow();
                if (selectedRow != -1) {
                    model.removeRow(selectedRow);
                    if (selectedRow < index.get()) {
                        index.getAndDecrement();
                    }
                    if (allStuTable.getRowCount() == index.get()) {
                        index.set(0);
                    }
                }
            });
            //deleteBtn.setToolTipText("删除选中的值日生记录");
            //deleteBtn.setLocation(255, 380);
            buttonPanel.add(deleteBtn);
        }

        mainPanel.add(buttonPanel);

        return mainPanel;
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

        JMenuItem openStuList = new JMenuItem("人员名单");
        openStuList.setIcon(GetIcon.getIcon(getClass().getResource("/image/openExp.png"), 16, 16));
        openStuList.addActionListener(e -> {
            OpenInExp.open(AllStuPath.getParent());
        });

        JMenuItem openDutyList = new JMenuItem("值班人员");
        openDutyList.setIcon(GetIcon.getIcon(getClass().getResource("/image/openExp.png"), 16, 16));
        openDutyList.addActionListener(e -> {
            OpenInExp.open(DutyListPath.getParent());
        });

        JMenu InfSets = new JMenu("数据设置");
        InfSets.setIcon(GetIcon.getIcon(getClass().getResource("/image/light/setting_0.png"), 16, 16));

        JMenu getInf = new JMenu("导入数据");
        getInf.setIcon(GetIcon.getIcon(getClass().getResource("/image/input.png"), 16, 16));

        JMenuItem getDutyList = new JMenuItem("导入值日表(.xlsx)");
        getDutyList.setIcon(GetIcon.getIcon(getClass().getResource("/image/input.png"), 16, 16));
        getDutyList.addActionListener(e -> {
            String filePath = GetPath.getFilePath(this, "请选择值日表", ".xlsx", "Excel");
            new GetExcelData<Duty>().getExcelData(filePath, DutyListPath, Duty.class );
            Log.info.message(this, "InfSetDialog-导入表格", "导入成功");
            this.setVisible(false);
            refreshCallback.run();
        });

        JMenuItem getAllStuList = new JMenuItem("导入人员表(.xlsx)");
        getAllStuList.setIcon(GetIcon.getIcon(getClass().getResource("/image/input.png"), 16, 16));
        getAllStuList.addActionListener(e -> {
            String filePath = GetPath.getFilePath(this, "请选择人员表", ".xlsx", "Excel");
            new GetExcelData<AllStu>().getExcelData(filePath, AllStuPath, AllStu.class );
            Log.info.message(this, "InfSetDialog-导入表格", "导入成功");
            this.setVisible(false);
            refreshCallback.run();
        });

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
        openFile.add(openDutyList);
        openFile.add(openStuList);

        getInf.add(getDutyList);
        getInf.add(getAllStuList);
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


        JMenuItem DutyMenuItem = new JMenuItem("值日生");
        DutyMenuItem.addActionListener(e -> {
            try {
                repaintSetsPanel(initDuSet(this.dutyList));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        JMenuItem AttMenuItem = new JMenuItem("迟到人数");
        AttMenuItem.addActionListener(e -> {
            try {
                repaintSetsPanel(initATSet(this.studentList, this.leaveList));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        JMenuItem AllStuMenuItem = new JMenuItem("全体人员");
        AllStuMenuItem.addActionListener(e -> {
            try {
                repaintSetsPanel(initAllStuSet(this.studentList));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        JMenuItem saveMenuItem = new JMenuItem("保存");
        saveMenuItem.setIcon(GetIcon.getIcon(getClass().getResource("/image/light/save_0.png"), 16, 16));
        saveMenuItem.addActionListener(e -> {
            save();

        });


        setMenu.add(PersonalizationMenuItem);
        setMenu.add(DutyMenuItem);
        setMenu.add(AttMenuItem);
        setMenu.add(AllStuMenuItem);

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
            initMenuBar();
            initSaveButton();
            c.add(panel, BorderLayout.CENTER);

            c.revalidate();
            c.repaint();

    }

    private JPanel initATSet(ArrayList<String> studentList, ArrayList<String> leaveList) throws IOException {

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(CTColor.backColor);
        mainPanel.setLayout(null);

        // 请假人员设置组件
        JLabel leaveLabel = new JLabel("请假人员:");
        leaveLabel.setBounds(20, 0, 300, 25);
        leaveLabel.setForeground(CTColor.textColor);
        mainPanel.add(leaveLabel);

        JPanel leavePanel = new JPanel();
        leavePanel.setBounds(20, 0, 340, 300);
        leavePanel.setBackground(CTColor.backColor);
        leavePanel.setLayout(new GridLayout(studentList.size() / 4 + 1, 4, 10, 10));
        //leavePanel.setBackground(Color.WHITE);


        //JTextArea leaveArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(leavePanel);
        scrollPane.setBounds(20, 30, 340, 300);
        scrollPane.setBackground(CTColor.backColor);
        //修改滚轮的灵敏度
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        //scrollPane.setLayout(null);
        mainPanel.add(scrollPane);

        Log.info.print("数据设置界面-initATSet", "studentList:" + studentList);
        Log.info.print("数据设置界面-initATSet", "leaveList:" + leaveList);
        for (String student : studentList) {
            JCheckBox checkBox = new JCheckBox(student);
            checkBox.setBackground(CTColor.backColor);
            checkBox.setForeground(CTColor.textColor);


            if (leaveList.contains(student.trim())) {
                checkBox.setSelected(true);
            }
            checkBoxList.add(checkBox);
            leavePanel.add(checkBox);
        }

        // 保存按钮


        return mainPanel;
    }

    private ArrayList<String> getLeaveList() {
        ArrayList<String> leaveList = new ArrayList<>();
        // 初始化现有数据
        try {
            if (leaveListPath.exists()) {
                String[] content = new IOStreamForInf(leaveListPath).GetInf();
                leaveList.addAll(Arrays.asList(content));

                //leaveArea.setText(content.replace(",", "\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return leaveList;
    }

    private ArrayList<String> getStudentList() throws IOException {
        //ArrayList<JCheckBox> checkBoxList = new ArrayList<>();
        ArrayList<String> studentList = new ArrayList<>();

        //获取所有学生名单
        {
            IOStreamForInf ioStreamForInf = new IOStreamForInf(AllStuPath);

            String[] inf = ioStreamForInf.GetInf();

            //System.out.println(inf);
            if (!inf[0].equals("error")) {
                studentList.addAll(Arrays.asList(inf));
            }
        }
        return studentList;
    }

    private String[][] getDutyList(File dutyPath) throws IOException {
        //获取inf
        IOStreamForInf ioStreamForInf = new IOStreamForInf(dutyPath);

        String[] inf = ioStreamForInf.GetInf();

        //System.out.println(inf);
        if (inf[0].equals("error")) {
            //总会有的
            ioStreamForInf.SetInf("[尽快,设置] [请]",
                    "[尽快,设置,1] [请]");
            return new String[][]{{"error"}, {"null"}};
        }


        //处理inf

        //初级数据-[0]"[xxx][xxx,xxx]" [1]...
        String[] inftempList = inf;

        String[][] list = new String[inftempList.length][2];
        ArrayList<String[]> tempList = new ArrayList<>();

        int i = 0;
        for (String s : inftempList) {
            //二级数据- [0]"xxx" [1]"xxx,xxx"
            //三级数据- [0]{"xxx"} [1]{"xxx","xxx"}
            ArrayList<String> strings = InfProcess.NDExtractNames(s);

            if (strings.size() == 2) {
                list[i][0] = strings.get(0);
                list[i][1] = strings.get(1);
            }
            i++;
        }
        return list;
    }

    private void save() {
        int result = Log.info.inputInt(this, "InfSetDialog-保存", "是否保存？");
        if (result == JOptionPane.YES_OPTION) {
            try {

                //保存数据-dutyList
                {

                    //处理表格中的数据


                    // 遍历表格中的每一行，将每一行的数据添加到tempList中
                    //getRowCount()-行数
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < DutyTable.getRowCount(); i++) {

                        //getColumnCount()-列数
                        for (int j = 0; j < DutyTable.getColumnCount(); j++) {

                            sb.append("[").append(DutyTable.getValueAt(i, j)).append("]");
                        }
                        sb.append("\n");
                    }
                    //System.out.println("表格数据:" + sb);
                    //System.out.println("--index:" + index.get());

                    new IOStreamForInf(DutyListPath).SetInf(sb.toString());
                    new IOStreamForInf(indexPath).SetInf(String.valueOf(index.get()));

                }

                //保存数据-请假信息
                {
                    StringBuilder sb = new StringBuilder();
                    for (JCheckBox checkBox : checkBoxList) {
                        if (checkBox.isSelected()) {
                            sb.append(checkBox.getText()).append("\n");
                        }
                    }
                    String names = sb.toString();
                    new IOStreamForInf(leaveListPath).SetInf(names);


                }

                //保存数据-人员名单
                {
                    //处理表格中的数据


                    // 遍历表格中的每一行，将每一行的数据添加到tempList中
                    //getRowCount()-行数
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < allStuTable.getRowCount(); i++) {


                        sb.append(allStuTable.getValueAt(i, 1));
                        sb.append("\n");
                    }

                    new IOStreamForInf(AllStuPath).SetInf(sb.toString());


                }

                //保存数据-个性化
                {
                    IOStreamForInf io = new IOStreamForInf(new File(Main.DATA_PATH + "setUp.json"));

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
                    io.SetInf(jsonObject.toString());

                }

                // 保存成功后执行回调
                refreshCallback.run();
                //this.setVisible(false);

            } catch (IOException ex) {
                Log.error.print("InfSetDialog", "保存数据失败: " + ex.getMessage());
            }
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    //窗口准备关闭
    @Override
    public void windowClosing(WindowEvent e) {
        //save();
        this.setVisible(false);
    }

    //窗口关闭
    @Override
    public void windowClosed(WindowEvent e) {

    }

    //窗口最小化
    @Override
    public void windowIconified(WindowEvent e) {

    }

    //窗口还原
    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    //窗口获得焦点
    @Override
    public void windowActivated(WindowEvent e) {

    }

    //窗口失去焦点
    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
