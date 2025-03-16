package com.wmp.classtools.infSet;

import com.wmp.Main;
import com.wmp.classtools.CTComponent.DuButton;
import com.wmp.io.IOStreamForInf;
import com.wmp.tools.InfProcess;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class InfSetDialog extends JDialog {

    private Container c;

    // 添加文件路径参数
    public InfSetDialog(Window owner, File AllStuPath, File leaveListPath, File DutyListPath, File indexPath, Runnable refreshCallback) throws IOException {

        this.setTitle("设置");
        this.setSize(400, 500);
        this.setLocationRelativeTo(null);
        this.setModal(true);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        c = this.getContentPane();


        initMenuBar(AllStuPath, leaveListPath, DutyListPath, indexPath, refreshCallback);

        initATSet(AllStuPath, leaveListPath, refreshCallback);


        //initDuSet(DutyListPath);

    }

    private void initDuSet(File DutyListPath, File indexPath, Runnable refreshCallback) throws IOException {

        AtomicInteger index = new AtomicInteger();

        DefaultTableModel model = new DefaultTableModel(getDutyList(DutyListPath),
                new String[]{"扫地","擦黑板"});
        JTable table = new JTable(model);
        //table.setEnabled(false);
        //table.setBounds(0, 0, 340, 300);
        //c.add(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 30, 340, 300);
        c.add(scrollPane);

        // 保存按钮
        {




            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/image/save_0.png"));
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
            ImageIcon imageIcon2 = new ImageIcon(getClass().getResource("/image/save_1.png"));
            imageIcon2.setImage(imageIcon2.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
            DuButton saveBtn = new DuButton("保存", imageIcon, imageIcon2, 95, 35, () -> {
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
        }

        // 资源管理器按钮
        {
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/image/openExp_0.png"));
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
            ImageIcon imageIcon2 = new ImageIcon(getClass().getResource("/image/openExp_1.png"));
            imageIcon2.setImage(imageIcon2.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
            DuButton explorerBtn = new DuButton("打开资源管理器", imageIcon, imageIcon2, 190, 35, () -> {});
            explorerBtn.setLocation(5, 380);
            explorerBtn.addActionListener(e -> {
                try {
                    Runtime.getRuntime().exec("explorer.exe " + DutyListPath.getParent());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            c.add(explorerBtn);
        }

        //新建
        {
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/image/new_0.png"));
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
            ImageIcon imageIcon2 = new ImageIcon(getClass().getResource("/image/new_1.png"));
            imageIcon2.setImage(imageIcon2.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
            DuButton newBtn = new DuButton("新建",imageIcon, imageIcon2,95,35, () -> {
                String s1 = JOptionPane.showInputDialog(this, "请输入擦黑板人员", "新建", JOptionPane.PLAIN_MESSAGE);
                String s2 = JOptionPane.showInputDialog(this, "请输入扫地人员", "新建", JOptionPane.PLAIN_MESSAGE);
                model.addRow(new Object[]{s2,s1});

            });
            newBtn.setLocation(255, 340);
            c.add(newBtn);
        }

        // 删除
        {
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/image/delete_0.png"));
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
            ImageIcon imageIcon2 = new ImageIcon(getClass().getResource("/image/delete_1.png"));
            imageIcon2.setImage(imageIcon2.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
            DuButton deleteBtn = new DuButton("删除", imageIcon, imageIcon2, 95, 35, () -> {


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
            c.add(deleteBtn);
        }
    }

    private void initMenuBar(File AllStuPath, File leaveListPath, File DutyListPath, File indexPath, Runnable refreshCallback) {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JMenu setMenu = new JMenu("设置界面");


        JMenuItem DutyMenuItem = new JMenuItem("值日生");
        DutyMenuItem.addActionListener(e -> {
            try {
                c.removeAll();
                initMenuBar(AllStuPath, leaveListPath, DutyListPath, indexPath, refreshCallback);
                initDuSet(DutyListPath,indexPath,refreshCallback);

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
                initMenuBar(AllStuPath, leaveListPath, DutyListPath, indexPath, refreshCallback);
                initATSet(AllStuPath, leaveListPath, refreshCallback);

                c.revalidate();
                c.repaint();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });


        setMenu.add(DutyMenuItem);
        setMenu.add(AttMenuItem);

        menuBar.add(setMenu);

        JMenu helpMenu = new JMenu("关于");

        JMenuItem appAbout = new JMenuItem("软件");
        appAbout.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "软件版本："+ Main.version +"\n开发者：WMP\n版权所有：WMP\n");
        });

        helpMenu.add(appAbout);

        menuBar.add(helpMenu);
    }

    private void initATSet(File AllStuPath, File leaveListPath, Runnable refreshCallback) throws IOException {
        // 请假人员设置组件
        JLabel leaveLabel = new JLabel("请假人员:");
        leaveLabel.setBounds(20, 0, 300, 25);
        c.add(leaveLabel);

        ArrayList<JCheckBox> checkBoxList = new ArrayList<>();
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
        c.add(scrollPane);


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


        { // 保存按钮
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/image/save_0.png"));
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
            ImageIcon imageIcon2 = new ImageIcon(getClass().getResource("/image/save_1.png"));
            imageIcon2.setImage(imageIcon2.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
            DuButton saveBtn = new DuButton("保存", imageIcon, imageIcon2, 95, 35, () -> {
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
        }

        {// 原有资源管理器按钮调整位置
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/image/openExp_0.png"));
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
            ImageIcon imageIcon2 = new ImageIcon(getClass().getResource("/image/openExp_1.png"));
            imageIcon2.setImage(imageIcon2.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
            DuButton explorerBtn = new DuButton("打开资源管理器", imageIcon, imageIcon2, 190, 35, () -> {});
            explorerBtn.setLocation(5, 380);
            explorerBtn.addActionListener(e -> {
                try {
                    Runtime.getRuntime().exec("explorer.exe " + leaveListPath.getParent());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            c.add(explorerBtn);
        }
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
}
