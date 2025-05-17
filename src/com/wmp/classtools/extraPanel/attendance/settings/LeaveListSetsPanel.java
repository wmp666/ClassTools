package com.wmp.classTools.extraPanel.attendance.settings;

import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTSetsPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class LeaveListSetsPanel extends CTSetsPanel {

    private final File AllStuPath;
    private final File leaveListPath;


    private final ArrayList<JCheckBox> checkBoxList = new ArrayList<>();

    public LeaveListSetsPanel(String basicDataPath) throws IOException {
        super(basicDataPath);
        File dataPath = new File(basicDataPath, "Att");

        this.AllStuPath = new File(dataPath, "AllStu.txt");
        this.leaveListPath = new File(dataPath, "LeaveList.txt");

        setName("迟到人员");

        ArrayList<String> leaveList = getLeaveList();
        ArrayList<String> studentList = getStudentList();
        initATSet(studentList, leaveList);
    }

    private void initATSet(ArrayList<String> studentList, ArrayList<String> leaveList) throws IOException {

        this.setBackground(CTColor.backColor);
        this.setLayout(null);

        // 请假人员设置组件
        JLabel leaveLabel = new JLabel("请假人员:");
        leaveLabel.setBounds(20, 0, 300, 25);
        leaveLabel.setForeground(CTColor.textColor);
        this.add(leaveLabel);

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
        this.add(scrollPane);

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

    }

    private ArrayList<String> getLeaveList() {
        ArrayList<String> leaveList = new ArrayList<>();
        // 初始化现有数据
        try {
            if (leaveListPath.exists()) {
                String[] content = new IOForInfo(leaveListPath).GetInfo();
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
            IOForInfo ioForInfo = new IOForInfo(AllStuPath);

            String[] inf = ioForInfo.GetInfo();

            //System.out.println(inf);
            if (!inf[0].equals("error")) {
                studentList.addAll(Arrays.asList(inf));
            }
        }
        return studentList;
    }

    @Override
    public void save() {
        //保存数据-请假信息
        {
            StringBuilder sb = new StringBuilder();
            for (JCheckBox checkBox : checkBoxList) {
                if (checkBox.isSelected()) {
                    sb.append(checkBox.getText()).append("\n");
                }
            }
            String names = sb.toString();
            try {
                new IOForInfo(leaveListPath).SetInfo(names);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }


    }
}
