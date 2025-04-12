package com.wmp.classTools.infSet;

import com.wmp.CTColor;
import com.wmp.Main;
import com.wmp.PublicTools.GetIcon;
import com.wmp.PublicTools.InfProcess;
import com.wmp.PublicTools.OpenInExp;
import com.wmp.PublicTools.io.IOStreamForInf;
import com.wmp.PublicTools.io.ZipPack;
import com.wmp.classTools.CTComponent.CTButton;
import com.wmp.classTools.infSet.DataStyle.AllStu;
import com.wmp.classTools.infSet.DataStyle.Duty;
import com.wmp.classTools.infSet.tools.GetExcelData;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
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
    private final JCheckBox canExit = new JCheckBox("防止被意外关闭");
    private final JCheckBox startUp = new JCheckBox("开机自启动");

    private final ArrayList<String> leaveList;
    private final ArrayList<String> studentList;
    private final String[][] dutyList;


    // 添加文件路径参数
    public InfSetDialog(Window owner, File AllStuPath, File leaveListPath, File DutyListPath, File indexPath, Runnable refreshCallback) throws IOException {

        this.setBackground(CTColor.backColor);
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
        mainPanelScroll.setSize(400, 400);

        SetsPanel.setBackground(CTColor.backColor);
        SetsPanel.setLayout(new GridLayout(2,1));

        JPanel ColorPanel = new JPanel();
        ColorPanel.setBackground(CTColor.backColor);
        ColorPanel.setLayout(new GridLayout(2,1));
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

        JPanel otherPanel = new JPanel();
        otherPanel.setBackground(CTColor.backColor);

        //其他设置
        {
            startUp.setFont(new Font("微软雅黑", -1, 15));
            startUp.setForeground(CTColor.textColor);
            startUp.setBackground(CTColor.backColor);

            canExit.setFont(new Font("微软雅黑", -1, 15));
            canExit.setForeground(CTColor.textColor);
            canExit.setBackground(CTColor.backColor);


            otherPanel.add(startUp);
            otherPanel.add(canExit);


        }
        otherPanel.setLayout(new GridLayout(3,2));

        SetsPanel.add(ColorPanel);
        SetsPanel.add(otherPanel);

        JPanel tempPanel = new JPanel();
        tempPanel.setLayout(new BorderLayout());
        tempPanel.setBackground(CTColor.backColor);
        tempPanel.add(mainPanelScroll, BorderLayout.NORTH);

        //显示数据
        {
            IOStreamForInf io = new IOStreamForInf(new File(Main.DataPath + "setUp.json"));
            JSONObject jsonObject = new JSONObject(io.GetInf()[0]);
            if (jsonObject.has("mainColor")) {
                switch (jsonObject.getString("mainColor")) {
                    case "black" -> mainColorComboBox.setSelectedIndex(4);
                    case "white" -> mainColorComboBox.setSelectedIndex(3);
                    case "green" -> mainColorComboBox.setSelectedIndex(2);
                    case "red" -> mainColorComboBox.setSelectedIndex(1);
                    default -> mainColorComboBox.setSelectedIndex(0);
                }
            }
            if (jsonObject.has("mainTheme")) {
                switch (jsonObject.getString("mainTheme")) {
                    case "dark" -> mainThemeComboBox.setSelectedIndex(1);
                    default -> mainThemeComboBox.setSelectedIndex(0);
                }
            }
            if (jsonObject.has("canExit")) {
                canExit.setSelected(!jsonObject.getBoolean("canExit"));

            }
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
                    s1 = JOptionPane.showInputDialog(this, "请输入擦黑板人员", "新建", JOptionPane.PLAIN_MESSAGE);
                    if (s1 != null && !s1.trim().isEmpty() && !(s1.indexOf('[') != -1 || s1.indexOf(']') != -1)) {
                        b = false;
                    } else if (s1 == null) {
                        return;
                    }
                }

                b = true;
                while (b) {
                    s2 = JOptionPane.showInputDialog(this, "请输入扫地人员", "新建", JOptionPane.PLAIN_MESSAGE);
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
                    s1 = JOptionPane.showInputDialog(this, "请输入姓名", "新建", JOptionPane.PLAIN_MESSAGE);
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

        JMenuItem openSetsList = new JMenuItem("个性化文件");
        openSetsList.addActionListener(e -> {
            OpenInExp.open(Main.DataPath);
        });

        JMenuItem openStuList = new JMenuItem("人员名单");
        openStuList.addActionListener(e -> {
            OpenInExp.open(AllStuPath.getParent());
        });

        JMenuItem openDutyList = new JMenuItem("值班人员");
        openDutyList.addActionListener(e -> {
            OpenInExp.open(DutyListPath.getParent());
        });

        JMenu InfSets = new JMenu("数据设置");

        JMenu getInf = new JMenu("导入数据");
        getInf.setIcon(GetIcon.getIcon(getClass().getResource("/image/input.png"), 16, 16));

        JMenuItem getDutyList = new JMenuItem("导入值日表(.xlsx)");
        getDutyList.setIcon(GetIcon.getIcon(getClass().getResource("/image/input.png"), 16, 16));
        getDutyList.addActionListener(e -> {
            String filePath = getFilePath("请选择值日表", ".xlsx");
            new GetExcelData<Duty>().getExcelData(filePath, DutyListPath, Duty.class );
        });

        JMenuItem getAllStuList = new JMenuItem("导入人员表(.xlsx)");
        getAllStuList.setIcon(GetIcon.getIcon(getClass().getResource("/image/input.png"), 16, 16));
        getAllStuList.addActionListener(e -> {
            String filePath = getFilePath("请选择人员表", ".xlsx");
            new GetExcelData<AllStu>().getExcelData(filePath, AllStuPath, AllStu.class );
            JOptionPane.showConfirmDialog(this, "导入成功", "", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
            this.setVisible(false);
            refreshCallback.run();
        });

        JMenuItem getAllInf = new JMenuItem("导入所有数据(.ctdatas)");
        getAllInf.setIcon(GetIcon.getIcon(getClass().getResource("/image/input.png"), 16, 16));
        getAllInf.addActionListener(e -> {
            String filePath = getFilePath("请选择所有数据", ".ctdatas");
            ZipPack.unzip(filePath, Main.DataPath);
            //刷新数据
            this.setVisible(false);
            refreshCallback.run();
        });

        JMenu inputInf = new JMenu("导出数据");
        inputInf.setIcon(GetIcon.getIcon(getClass().getResource("/image/light/update_0.png"), 16, 16));

        JMenuItem inputAllInf = new JMenuItem("导出所有数据(.ctdatas)");
        inputAllInf.setIcon(GetIcon.getIcon(getClass().getResource("/image/light/update_0.png"), 16, 16));
        inputAllInf.addActionListener(e -> {
            String path = getDirectoryPath("请选择导出目录");
            //将ClassTools文件夹中的文件打包为.zip
            boolean b = ZipPack.createZip(path, Main.DataPath, "ClassTools.ctdatas");
            if (!b){
                JOptionPane.showMessageDialog(this, "数据导出异常", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
            }
            //JOptionPane.showMessageDialog(this, "加急制作中...", "导出所有数据", JOptionPane.INFORMATION_MESSAGE);
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
            //JOptionPane.showMessageDialog(this, "保存成功");
        });


        setMenu.add(PersonalizationMenuItem);
        setMenu.add(DutyMenuItem);
        setMenu.add(AttMenuItem);
        setMenu.add(AllStuMenuItem);

        editMenu.add(setMenu);
        editMenu.add(saveMenuItem);

        menuBar.add(editMenu);


    }



    private String getFilePath(String title , String fileType) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(title);

        //将文件过滤器设置为只显示.xlsx 或 文件夹
        chooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    String fileName = f.getName();
                    String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
                    return fileSuffix.equals(fileType);
                }
            }
            @Override
            public String getDescription() {
                return "Excel文件(*"+ fileType +")";
            }
        });

        //获取文件路径
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                String filePath = chooser.getSelectedFile().getAbsolutePath();
                //获取文件名
                String fileName = chooser.getSelectedFile().getName();
                //获取文件名后缀
                String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
                //获取文件名前缀
                String filePrefix = fileName.substring(0, fileName.lastIndexOf("."));

                System.out.println("文件路径：" + filePath + "|文件名: " + fileName + "|文件后缀: " + fileSuffix + "|文件前缀: " + filePrefix);
                return filePath;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return null;
    }

    private String getDirectoryPath(String title) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(title);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //返回文件路径
        if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){// 如果点击了"确定"按钮
            try {
                String filePath = chooser.getSelectedFile().getAbsolutePath();
                System.out.println("文件路径：" + filePath);
                return filePath;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return null;
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

        System.out.println("studentList:" + studentList);
        System.out.println("leaveList:" + leaveList);
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
        /*{

            CTButton saveBtn = new CTButton("保存", getClass().getResource("/image/save_0.png")
                    , getClass().getResource("/image/save_1.png"), 95, 35, () -> {
                try {
                    StringBuilder sb = new StringBuilder();
                    for (JCheckBox checkBox : checkBoxList) {
                        if (checkBox.isSelected()) {
                            sb.append(checkBox.getText()).append(",");
                        }
                    }
                    String names = sb.toString();
                    new IOStreamForInf(leaveListPath).SetInf(names);
                    JOptionPane.showMessageDialog(this, "保存成功");

                    refreshCallback.run(); // 保存成功后执行回调

                    this.setVisible(false);

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "保存失败", "错误", JOptionPane.ERROR_MESSAGE);
                }
            });
            //JButton saveBtn = new JButton("保存");
            saveBtn.setLocation(22, 340);

            c.add(saveBtn);
        }*/

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
        int result = JOptionPane.showConfirmDialog(this, "是否保存？", "提示", JOptionPane.YES_NO_OPTION);
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

                    //System.out.println(new IOStreamForInf(indexPath).GetInf());

                    //JOptionPane.showMessageDialog(this, "保存成功");

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
                    //JOptionPane.showMessageDialog(this, "保存成功");


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
                    IOStreamForInf io = new IOStreamForInf(new File(Main.DataPath + "setUp.json"));

                    JSONObject jsonObject = new JSONObject();
                    String tempMainColor = switch (Objects.requireNonNull(mainColorComboBox.getSelectedItem()).toString()) {
                        case "黑色" -> "black";
                        case "白色" -> "white";
                        case "绿色" -> "green";
                        case "红色" -> "red";
                        default -> "blue";
                    };
                    jsonObject.put("mainColor", tempMainColor);

                    String tempMainThemeColor = switch (Objects.requireNonNull(mainThemeComboBox.getSelectedItem()).toString()) {
                        case "深色" -> "dark";
                        default -> "light";
                    };
                    jsonObject.put("mainTheme", tempMainThemeColor);

                    jsonObject.put("canExit", !canExit.isSelected());

                    System.out.println(jsonObject);
                    io.SetInf(jsonObject.toString());
                }

                // 保存成功后执行回调
                refreshCallback.run();
                //this.setVisible(false);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "保存失败", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    //窗口准备关闭
    @Override
    public void windowClosing(WindowEvent e) {
        save();
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
