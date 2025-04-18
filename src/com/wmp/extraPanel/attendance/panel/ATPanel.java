package com.wmp.extraPanel.attendance.panel;

import com.wmp.CTColor;
import com.wmp.classTools.CTComponent.CTPanel;
import com.wmp.PublicTools.PeoPanelProcess;
import com.wmp.PublicTools.io.IOStreamForInf;

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
    private int ATPanelMixY;
    private int studentLength;
    private int studentLateLength;
    private final ArrayList<String> studentList = new ArrayList<>();
    private final ArrayList<String> leaveList = new ArrayList<>();//迟到人员

    public ATPanel(int nextPanelY, File AllStudentPath,File LeaveListPath) throws IOException {
        super(nextPanelY);

        this.AllStudentPath = AllStudentPath;
        this.LeaveListPath = LeaveListPath;

        setLayout(null);

        initStuList(AllStudentPath,LeaveListPath);


        //System.out.println(studentList);

        initContainer();



        this.setSize(250, ATPanelMixY + 5);
        setNextPanelY( ATPanelMixY + 5);

    }

    private void initContainer() {
        //将CTColor.mainColor解析为16进制颜色

        String StrMainColor = String.format("#%06x", CTColor.mainColor.getRGB());
        StrMainColor = StrMainColor.substring(2, 9);
        System.out.println("MainColor:" + StrMainColor);
        String NumColor = "style='color: " + StrMainColor + ";'";

        String StrTextColor = String.format("#%06x", CTColor.textColor.getRGB());
        StrTextColor = StrTextColor.substring(2, 9);
        System.out.println("TextColor:" + StrTextColor);
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
            personLabel.setBounds(5, ATPanelMixY, 250, 30 * Integer.parseInt(String.valueOf(objects[1])));
            ATPanelMixY = ATPanelMixY + personLabel.getHeight();

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
        ATPanelMixY = 0;

        initContainer();

        nextPanelY(ATPanelMixY);
        this.setSize(250, ATPanelMixY + 5);


        // 强制重绘
        revalidate();
        repaint();
    }

    private void initStuList(File AllStudentPath, File LeaveListPath) throws IOException {
        //获取所有学生名单
        {
            IOStreamForInf ioStreamForInf = new IOStreamForInf(AllStudentPath);

            String[] inf = ioStreamForInf.GetInf();

            //System.out.println(inf);
            if (inf[0].equals("error")) {
                {
                    //将数据改为默认
                    // 使用常量数组管理姓名数据
                    final String[] DEFAULT_NAMES = {
                            "曾世通", "陈昌焱", "陈权浩", "陈思源", "程政", "程睿智",
                            "范祖轩", "贾梓瑄", "姜宇航", "赖奕鑫", "李宇涵", "林宇轩",
                            "刘弘祥", "刘诗怡", "刘欣妍", "刘雨涵", "芦其松", "罗伟钦",
                            "骆钦阳", "毛佳欣", "毛嘉锐", "彭靖波", "唐志涛", "万涵予",
                            "万思遥", "万文卿", "万延康", "吴鹤轩", "吴宇恒", "吴梓豪",
                            "伍子阳", "熊天晴", "徐浩", "晏文杰", "杨沛妍", "姚嘉鑫",
                            "姚焱竣", "易家树", "俞皓天", "袁立志", "章智峰", "赵银涛",
                            "钟世豪", "朱彦轩", "祝思哲", "邹子煦"
                    };

                    // 通过数组传递完整数据
                    ioStreamForInf.SetInf(DEFAULT_NAMES);
                }


            } else {
                studentList.addAll(Arrays.asList(inf));
                studentLength = studentList.size();
            }
        }

        //获取请假名单
        {
            IOStreamForInf ioStreamForInf = new IOStreamForInf(LeaveListPath);
            String[] inf = ioStreamForInf.GetInf();

            //遍历数组
            for (String s : inf) {
                if (s.equals("")) {
                    continue;
                }
                System.out.print(s + "-");
                //leaveList.add(s);
            }
            System.out.println();
            if (inf[0].equals("error")) {
                ioStreamForInf.SetInf("");
                studentLateLength = 0;
            }else{
                //leaveList.clear();
                leaveList.addAll(List.of(inf));
                studentLateLength = leaveList.size();
                System.out.println("leaveList.size():" + leaveList.size());
            }
        }


    }


}
