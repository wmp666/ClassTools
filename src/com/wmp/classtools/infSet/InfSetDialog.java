package com.wmp.classtools.infSet;

import com.wmp.io.IOStreamForInf;

import javax.print.DocFlavor;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class InfSetDialog extends JDialog {

    // 添加文件路径参数
    public InfSetDialog(Object owner,File AllStuPath , File leaveListPath, Runnable refreshCallback) throws IOException {
        this.setTitle("设置");
        this.setSize(400, 500); // 扩大窗口尺寸
        this.setLocationRelativeTo(null);
        this.setModal(true);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);



        initATSet(AllStuPath, leaveListPath, refreshCallback);

    }

    private void initATSet(File AllStuPath, File leaveListPath, Runnable refreshCallback) throws IOException {
        // 请假人员设置组件
        JLabel leaveLabel = new JLabel("请假人员:");
        leaveLabel.setBounds(20, 20, 300, 25);
        this.add(leaveLabel);

        ArrayList<JCheckBox> checkBoxList = new ArrayList<>();
        ArrayList<String> studentList = new ArrayList<>();

        //获取所有学生名单
        {
            IOStreamForInf ioStreamForInf = new IOStreamForInf(AllStuPath);

            String inf = ioStreamForInf.GetInf();

            //System.out.println(inf);
            if (!(inf.isEmpty() || inf.equals("error"))){
                studentList.addAll(Arrays.asList(inf.split("\n")));
            }
        }

        JPanel leavePanel = new JPanel();
        leavePanel.setBounds(0, 0, 340, 300);
        leavePanel.setLayout(new GridLayout(studentList.size()/4 + 1, 4, 10, 10));
        //leavePanel.setBackground(Color.WHITE);



        //JTextArea leaveArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(leavePanel);
        scrollPane.setBounds(20, 50, 340, 300);
        //修改滚轮的灵敏度
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        //scrollPane.setLayout(null);
        this.add(scrollPane);


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
        JButton saveBtn = new JButton("保存请假名单");
        saveBtn.setBounds(120, 370, 150, 30);
        saveBtn.addActionListener(e -> {
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
        this.add(saveBtn);

        // 原有资源管理器按钮调整位置
        JButton explorerBtn = new JButton("打开资源管理器");
        explorerBtn.setBounds(120, 420, 150, 30);
        explorerBtn.addActionListener(e -> {
            try {
                Runtime.getRuntime().exec("explorer.exe " + leaveListPath.getParent());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        this.add(explorerBtn);
    }


}
