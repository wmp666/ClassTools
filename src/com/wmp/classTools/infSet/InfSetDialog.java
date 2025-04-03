package com.wmp.classTools.infSet;

import com.wmp.CTColor;
import com.wmp.classTools.CTComponent.CTButton;
import com.wmp.tools.io.IOStreamForInf;
import com.wmp.tools.InfProcess;
import com.wmp.tools.OpenInExp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    }

    private JPanel initDuSet(String [][] dutyList) throws IOException {

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(CTColor.backColor);
        mainPanel.setLayout(null);

        DefaultTableModel model = new DefaultTableModel(dutyList,
                new String[]{"扫地", "擦黑板"});
        //设置表格的两列不可以修改顺序
        DutyTable.setColumnSelectionAllowed(false);
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

            CTButton newBtn = new CTButton("添加新的值日生记录", getClass().getResource("/image/new_0.png"),
                    getClass().getResource("/image/new_1.png"), 30, () -> {
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

            CTButton deleteBtn = new CTButton("删除选中的值日生记录", getClass().getResource("/image/delete_0.png"),
                    getClass().getResource("/image/delete_1.png"), 35, () -> {

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

            CTButton newBtn = new CTButton("添加新的项", getClass().getResource("/image/new_0.png"),
                    getClass().getResource("/image/new_1.png"), 30, () -> {
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

            CTButton deleteBtn = new CTButton("删除选中的项", getClass().getResource("/image/delete_0.png"),
                    getClass().getResource("/image/delete_1.png"), 35, () -> {


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

        JMenu openFile = new JMenu("打开文件");

        JMenuItem openAppList = new JMenuItem("软件位置");
        openAppList.addActionListener(e -> {
            OpenInExp.open(System.getProperty("user.dir"));
        });


        JMenuItem openStuList = new JMenuItem("人员名单");
        openStuList.addActionListener(e -> {
            OpenInExp.open(AllStuPath.getParent());
        });

        JMenuItem openDutyList = new JMenuItem("值班人员");
        openDutyList.addActionListener(e -> {
            OpenInExp.open(DutyListPath.getParent());
        });

        JMenuItem exitMenuItem = new JMenuItem("退出");
        exitMenuItem.addActionListener(e -> {
            save();
            this.setVisible(false);
        });


        openFile.add(openDutyList);
        openFile.add(openAppList);
        openFile.add(openStuList);

        fileMenu.add(openFile);
        fileMenu.add(exitMenuItem);


        menuBar.add(fileMenu);

        //编辑
        JMenu editMenu = new JMenu("编辑");

        JMenu setMenu = new JMenu("设置界面");


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
        saveMenuItem.addActionListener(e -> {
            save();
            JOptionPane.showMessageDialog(this, "保存成功");
        });


        setMenu.add(DutyMenuItem);
        setMenu.add(AttMenuItem);
        setMenu.add(AllStuMenuItem);

        editMenu.add(setMenu);
        editMenu.add(saveMenuItem);

        menuBar.add(editMenu);


    }

    private void initSaveButton() {
        CTButton saveButton = new CTButton(CTButton.ButtonText, "保存数据", getClass().getResource("/image/save_0.png"),
                getClass().getResource("/image/save_1.png"), 200, 50, () -> {
            save();
            JOptionPane.showMessageDialog(this, "保存成功");
        });
        c.add(saveButton, BorderLayout.SOUTH);

    }

    private void repaintSetsPanel(JPanel panel) {

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
