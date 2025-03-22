package com.wmp.classtools.infSet;

import com.wmp.CTColor;
import com.wmp.Main;
import com.wmp.classtools.CTComponent.CTButton;
import com.wmp.io.IOStreamForInf;
import com.wmp.tools.InfProcess;

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
    private final JTable table = new JTable();


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

        c.add(initATSet());

        initDuSet();

    }

    private JPanel initDuSet() throws IOException {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);

        DefaultTableModel model = new DefaultTableModel(getDutyList(DutyListPath),
                new String[]{"扫地","擦黑板"});

        table.setModel(model);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 30, 340, 300);
        mainPanel.add(scrollPane);

        // 保存按钮
        /*{





            CTButton saveBtn = new CTButton("保存", getClass().getResource("/image/save_0.png"),
                    getClass().getResource("/image/save_1.png"), 95, 35, () -> {
                try {

                    //处理表格中的数据


                    // 遍历表格中的每一行，将每一行的数据添加到tempList中
                    //getRowCount()-行数
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < table.getRowCount(); i++) {

                        //getColumnCount()-列数
                        for (int j = 0; j < table.getColumnCount(); j++) {

                            sb.append("[").append(table.getValueAt(i, j)).append("]");
                        }
                        sb.append("\n");
                    }
                    System.out.println("表格数据:" + sb);
                    System.out.println("--index:" + index.get());

                    new IOStreamForInf(DutyListPath).SetInf(sb.toString());
                    new  IOStreamForInf(indexPath).SetInf(String.valueOf(index.get()));

                    System.out.println(new IOStreamForInf(indexPath).GetInf());

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

        /*// 资源管理器按钮
        {

            CTButton explorerBtn = new CTButton("打开资源管理器", getClass().getResource("/image/openExp_0.png"),
                    getClass().getResource("/image/openExp_1.png"), 190, 35, () -> {});
            explorerBtn.setLocation(5, 380);
            explorerBtn.addActionListener(e -> {
                try {
                    Runtime.getRuntime().exec("explorer.exe " + DutyListPath.getParent());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            c.add(explorerBtn);
        }*/

        //新建
        {

            CTButton newBtn = new CTButton("新建",getClass().getResource("/image/new_0.png"),
                    getClass().getResource("/image/new_1.png"),95,35, () -> {
                String s1 = JOptionPane.showInputDialog(this, "请输入擦黑板人员", "新建", JOptionPane.PLAIN_MESSAGE);
                String s2 = JOptionPane.showInputDialog(this, "请输入扫地人员", "新建", JOptionPane.PLAIN_MESSAGE);
                model.addRow(new Object[]{s2,s1});

            });
            newBtn.setLocation(255, 340);
            mainPanel.add(newBtn);
        }

        // 删除
        {

            CTButton deleteBtn = new CTButton("删除",getClass().getResource("/image/delete_0.png"),
                    getClass().getResource("/image/delete_1.png"), 95, 35, () -> {


                try {
                    index.set(Integer.parseInt(new IOStreamForInf(indexPath).GetInf()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    model.removeRow(selectedRow);
                    if (selectedRow < index.get()){
                        index.getAndDecrement();
                    }
                    if (table.getRowCount() == index.get()) {
                        index.set(0);
                    }
                }
            });
            deleteBtn.setLocation(255, 380);
            mainPanel.add(deleteBtn);
        }

        return mainPanel;
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("文件");

        JMenu openFile = new JMenu("打开文件");

        JMenuItem openStuList = new JMenuItem("人员名单");
        openStuList.addActionListener(e -> {
            try {
                Runtime.getRuntime().exec("explorer.exe " + leaveListPath.getParent());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        JMenuItem openDutyList = new JMenuItem("值班人员");
        openDutyList.addActionListener(e -> {
            try {
                Runtime.getRuntime().exec("explorer.exe " + DutyListPath.getParent());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        JMenuItem exitMenuItem = new JMenuItem("退出");
        exitMenuItem.addActionListener(e -> {
            save();
            this.setVisible(false);
        });


        openFile.add(openStuList);
        openFile.add(openDutyList);

        fileMenu.add(openFile);
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        //编辑
        JMenu editMenu = new JMenu("编辑");

        JMenu setMenu = new JMenu("设置界面");


        JMenuItem DutyMenuItem = new JMenuItem("值日生");
        DutyMenuItem.addActionListener(e -> {
            try {
                c.removeAll();
                initMenuBar();
                c.add(initDuSet());

                c.revalidate();
                c.repaint();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        JMenuItem AttMenuItem = new JMenuItem("迟到人数");

        AttMenuItem.addActionListener(e -> {
            try {
                c.removeAll();
                initMenuBar();
                c.add(initATSet());

                c.revalidate();
                c.repaint();
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

        editMenu.add(setMenu);
        editMenu.add(saveMenuItem);

        menuBar.add(editMenu);

        JMenu helpMenu = new JMenu("关于");

        JMenuItem appAbout = new JMenuItem("软件");
        appAbout.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "软件版本："+ Main.version +"\n开发者：WMP\n版权所有：WMP\n");
        });

        helpMenu.add(appAbout);

        menuBar.add(helpMenu);


    }

    private JPanel initATSet() throws IOException {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);

        // 请假人员设置组件
        JLabel leaveLabel = new JLabel("请假人员:");
        leaveLabel.setBounds(20, 0, 300, 25);
        mainPanel.add(leaveLabel);

        //ArrayList<JCheckBox> checkBoxList = new ArrayList<>();
        ArrayList<String> studentList = new ArrayList<>();

        //获取所有学生名单
        {
            IOStreamForInf ioStreamForInf = new IOStreamForInf(AllStuPath);

            String inf = ioStreamForInf.GetInf();

            //System.out.println(inf);
            if (!(inf.isEmpty() || inf.equals("error"))) {
                studentList.addAll(Arrays.asList(inf.split("\n")));
            }
        }

        JPanel leavePanel = new JPanel();
        leavePanel.setBounds(20, 0, 340, 300);
        leavePanel.setLayout(new GridLayout(studentList.size() / 4 + 1, 4, 10, 10));
        //leavePanel.setBackground(Color.WHITE);


        //JTextArea leaveArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(leavePanel);
        scrollPane.setBounds(20, 30, 340, 300);
        //修改滚轮的灵敏度
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        //scrollPane.setLayout(null);
        mainPanel.add(scrollPane);


        ArrayList<String> leaveList = new ArrayList<>();
        // 初始化现有数据
        try {
            if (leaveListPath.exists()) {
                String content = new IOStreamForInf(leaveListPath).GetInf();
                leaveList.addAll(Arrays.asList(content.split(",")));

                //leaveArea.setText(content.replace(",", "\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("studentList:" + studentList);
        System.out.println("leaveList:" + leaveList);
        for (String student : studentList) {
            JCheckBox checkBox = new JCheckBox(student);
            //checkBox.setBackground(Color.WHITE);


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

        /*{// 原有资源管理器按钮调整位置
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/image/openExp_0.png"));
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
            ImageIcon imageIcon2 = new ImageIcon(getClass().getResource("/image/openExp_1.png"));
            imageIcon2.setImage(imageIcon2.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
            CTButton explorerBtn = new CTButton("打开资源管理器", imageIcon, imageIcon2, 190, 35, () -> {});
            explorerBtn.setLocation(5, 380);
            explorerBtn.addActionListener(e -> {

            });
            c.add(explorerBtn);
        }*/

        return mainPanel;
    }

    private String[][] getDutyList(File dutyPath) throws IOException {
        //获取inf
        IOStreamForInf ioStreamForInf = new IOStreamForInf(dutyPath);

        String inf = ioStreamForInf.GetInf();

        //System.out.println(inf);
        if(inf.equals("null")){
            //将数据改为默认-空,需要用户自行输入数据

            ioStreamForInf.SetInf("""
        [请] [尽快,设置]
        [请] [尽快,设置]
        """);
        } else if (inf.equals("error")) {
            //总会有的
            return new String[][]{{"error"},{"null"}};
        }



        //处理inf

        //初级数据-[0]"[xxx][xxx,xxx]" [1]...
        String[] inftempList = inf.split("\n");

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

    private void save(){
        int result = JOptionPane.showConfirmDialog(this, "是否保存？", "提示", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION){
            try {

                //保存数据
                {

                    //处理表格中的数据


                    // 遍历表格中的每一行，将每一行的数据添加到tempList中
                    //getRowCount()-行数
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < table.getRowCount(); i++) {

                        //getColumnCount()-列数
                        for (int j = 0; j < table.getColumnCount(); j++) {

                            sb.append("[").append(table.getValueAt(i, j)).append("]");
                        }
                        sb.append("\n");
                    }
                    //System.out.println("表格数据:" + sb);
                    //System.out.println("--index:" + index.get());

                    new IOStreamForInf(DutyListPath).SetInf(sb.toString());
                    new  IOStreamForInf(indexPath).SetInf(String.valueOf(index.get()));

                    //System.out.println(new IOStreamForInf(indexPath).GetInf());

                    //JOptionPane.showMessageDialog(this, "保存成功");

                }

                //保存数据
                {
                    StringBuilder sb = new StringBuilder();
                    for (JCheckBox checkBox : checkBoxList) {
                        if (checkBox.isSelected()) {
                            sb.append(checkBox.getText()).append(",");
                        }
                    }
                    String names = sb.toString();
                    new IOStreamForInf(leaveListPath).SetInf(names);
                    //JOptionPane.showMessageDialog(this, "保存成功");


                }

                // 保存成功后执行回调
                refreshCallback.run();
                //this.setVisible(false);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "保存失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    //
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
