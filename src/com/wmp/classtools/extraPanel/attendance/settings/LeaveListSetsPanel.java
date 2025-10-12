package com.wmp.classTools.extraPanel.attendance.settings;

import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
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

        this.removeAll();
        checkBoxList.clear();

        //this.setBackground(CTColor.backColor);
        this.setLayout(new BorderLayout());

        // 请假人员设置组件
        JLabel leaveLabel = new JLabel("请假人员:");
        leaveLabel.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.NORMAL));
        leaveLabel.setForeground(CTColor.textColor);
        this.add(leaveLabel, BorderLayout.NORTH);

        JPanel leavePanel = new JPanel();
        leavePanel.setBounds(20, 0, 340, 300);
        leavePanel.setBackground(CTColor.backColor);
        leavePanel.setLayout(new GridLayout(studentList.size() / 4 + 1, 4, 10, 10));
        //leavePanel.setBackground(Color.WHITE);


        JScrollPane scrollPane = new JScrollPane(leavePanel);
        //修改滚轮的灵敏度
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        this.add(scrollPane, BorderLayout.CENTER);

        Log.info.print("数据设置界面-initATSet", "studentList:" + studentList);
        Log.info.print("数据设置界面-initATSet", "leaveList:" + leaveList);
        for (String student : studentList) {
            JCheckBox checkBox = new JCheckBox(student);
            checkBox.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));
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
                String[] content = new IOForInfo(leaveListPath).getInfo();
                leaveList.addAll(Arrays.asList(content));

                //leaveArea.setText(content.replace(",", "\n"));
            }
        } catch (IOException e) {
            Log.err.print(getClass(), "获取失败", e);
        }
        return leaveList;
    }

    private ArrayList<String> getStudentList() throws IOException {
        //ArrayList<JCheckBox> checkBoxList = new ArrayList<>();
        ArrayList<String> studentList = new ArrayList<>();

        //获取所有学生名单
        {
            IOForInfo ioForInfo = new IOForInfo(AllStuPath);

            String[] inf = ioForInfo.getInfo();

            //System.out.println(inf);
            if (!inf[0].equals("err")) {
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
                new IOForInfo(leaveListPath).setInfo(names);
            } catch (IOException e) {
                Log.err.print(getClass(), "保存失败", e);
            }


        }


    }

    @Override
    public void refresh() throws IOException {
        ArrayList<String> leaveList = getLeaveList();
        ArrayList<String> studentList = getStudentList();
        initATSet(studentList, leaveList);
    }
}
