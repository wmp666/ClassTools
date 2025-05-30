package com.wmp.classTools.extraPanel.attendance.panel;

import com.wmp.Main;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.PeoPanelProcess;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTPanel;
import com.wmp.classTools.CTComponent.CTSetsPanel;
import com.wmp.classTools.extraPanel.attendance.settings.AllStuSetsPanel;
import com.wmp.classTools.extraPanel.attendance.settings.LeaveListSetsPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ATPanel extends CTPanel {

    private final JLabel LateStuLabel = new JLabel();
    private final JLabel AttendStuLabel = new JLabel();
    private final JLabel AllStuLabel = new JLabel();
    private final JLabel personLabel = new JLabel();
    private final File AllStudentPath;
    private final File LeaveListPath;
    private int ATPanelMaxWidth = 250;
    private int ATPanelMixY; //面板的高度
    private int studentLength;// 应到人数
    private int studentLateLength; // 请假人数
    private final ArrayList<String> studentList = new ArrayList<>();
    private final ArrayList<String> leaveList = new ArrayList<>();//迟到人员

    public ATPanel(int nextPanelY, File AllStudentPath,File LeaveListPath) throws IOException {
        super(nextPanelY);

        this.AllStudentPath = AllStudentPath;
        this.LeaveListPath = LeaveListPath;

        ArrayList<CTSetsPanel> list = new ArrayList<>();
        list.add(new LeaveListSetsPanel(Main.DATA_PATH));
        list.add(new AllStuSetsPanel(Main.DATA_PATH));
        this.setCtSetsPanelList(list);

        setLayout(null);

        initStuList(AllStudentPath,LeaveListPath);


        //System.out.println(studentList);

        initContainer();


        this.setSize(ATPanelMaxWidth, ATPanelMixY + 5);
        appendNextPanelY( ATPanelMixY + 5);

    }

    private void initContainer() {
        //将CTColor.mainColor解析为16进制颜色

        String StrMainColor = String.format("#%06x", CTColor.mainColor.getRGB());
        StrMainColor = StrMainColor.substring(2, 9);
        String NumColor = "style='color: " + StrMainColor + ";'";

        String StrTextColor = String.format("#%06x", CTColor.textColor.getRGB());
        StrTextColor = StrTextColor.substring(2, 9);
        String TextColor = "style='color: " + StrTextColor + ";'";

        AllStuLabel.setText("<html><span " + TextColor + ">" + "应到：<span " + NumColor + ">" + studentLength + "人</html>");
        AllStuLabel.setFont(new Font("微软雅黑", Font.BOLD, 25));
        AllStuLabel.setBounds(5, 0, 250, 30);
        ATPanelMixY = AllStuLabel.getHeight();
        this.add(AllStuLabel);

        AttendStuLabel.setText("<html><span " + TextColor + ">" + "实到：<span " + NumColor + ">" + (studentLength - studentLateLength) + "人</html>");
        AttendStuLabel.setFont(new Font("微软雅黑", Font.BOLD, 25));
        AttendStuLabel.setBounds(5, ATPanelMixY, 250, 30);
        ATPanelMixY = ATPanelMixY + AttendStuLabel.getHeight();
        this.add(AttendStuLabel);

        LateStuLabel.setText("<html><span " + TextColor + ">" + "请假：<span style='color: red;'>" + studentLateLength + "人</html>");
        LateStuLabel.setFont(new Font("微软雅黑", Font.BOLD, 25));
        LateStuLabel.setBounds(5, ATPanelMixY, 250, 30);
        ATPanelMixY = ATPanelMixY + LateStuLabel.getHeight();
        this.add(LateStuLabel);

        initLateList();


    }

    private void initLateList() {


        if (leaveList.isEmpty()) {
            personLabel.setText("<html>无请假人员</html>");
            personLabel.setFont(new Font("微软雅黑", Font.BOLD, 23));
            personLabel.setForeground(CTColor.mainColor);
            personLabel.setBounds(5, ATPanelMixY, 250, 30);
            ATPanelMixY = ATPanelMixY + personLabel.getHeight();
            this.add(personLabel);
       }else {
            Object[] objects = PeoPanelProcess.getPeopleName(leaveList);

            personLabel.setText(String.valueOf(objects[0]));
            personLabel.setFont(new Font("微软雅黑", Font.BOLD, 23));
            personLabel.setForeground(CTColor.mainColor);
            personLabel.setBounds(5, ATPanelMixY,
                    Integer.parseInt(String.valueOf(objects[2])) * 23 + 8,
                    30 * Integer.parseInt(String.valueOf(objects[1])));
            ATPanelMixY = ATPanelMixY + personLabel.getHeight();
            ATPanelMaxWidth = Math.max(ATPanelMaxWidth, personLabel.getWidth());

            this.add(personLabel);
        }


    }

    @Override
    public void refresh() throws IOException {
        // 清空旧数据
        studentList.clear();
        leaveList.clear();

        // 重新加载数据
        initStuList(AllStudentPath, LeaveListPath);


        // 更新UI组件

        this.removeAll();
        ATPanelMaxWidth = 250;
        ATPanelMixY = 0;

        initContainer();

        setNextPanelY(ATPanelMixY);
        this.setSize(ATPanelMaxWidth, ATPanelMixY + 5);


        // 强制重绘
        revalidate();
        repaint();
    }

    private void initStuList(File AllStudentPath, File LeaveListPath) throws IOException {
        //获取所有学生名单
        {
            IOForInfo ioForInfo = new IOForInfo(AllStudentPath);

            String[] inf = ioForInfo.GetInfo();

            //System.out.println(inf);
            if (inf[0].equals("err")) {
                {
                    //将数据改为默认
                    // 使用常量数组管理姓名数据
                    final String[] DEFAULT_NAMES = {
                            "请", "尽快", "设置", "!!!"
                    };

                    // 通过数组传递完整数据
                    ioForInfo.SetInfo(DEFAULT_NAMES);
                }


            } else {
                studentList.addAll(Arrays.asList(inf));
                studentLength = studentList.size();
            }
        }

        //获取请假名单
        {
            IOForInfo ioForInfo = new IOForInfo(LeaveListPath);
            String[] inf = ioForInfo.GetInfo();

            //遍历数组
            for (String s : inf) {
                if (s.equals("")) {
                    continue;
                }
                Log.info.print("ATPanel-initStuList", "请假名单:" + s);
                //leaveList.add(s);
            }
            if (inf[0].equals("err")) {
                ioForInfo.SetInfo("");
                studentLateLength = 0;
            }else{
                //leaveList.clear();
                leaveList.addAll(List.of(inf));
                studentLateLength = leaveList.size();
            }
        }


    }


}
