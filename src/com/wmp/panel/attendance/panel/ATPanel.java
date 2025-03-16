package com.wmp.panel.attendance.panel;

import com.wmp.classtools.CTComponent.CTButton;
import com.wmp.classtools.CTComponent.CTPanel;
import com.wmp.classtools.infSet.InfSetDialog;
import com.wmp.io.IOStreamForInf;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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

    public ATPanel(int mixY, File AllStudentPath,File LeaveListPath) throws IOException {
        super(mixY);

        this.AllStudentPath = AllStudentPath;
        this.LeaveListPath = LeaveListPath;

        setLayout(null);

        initStuList(AllStudentPath,LeaveListPath);


        System.out.println(studentList);

        initContainer();



        this.setSize(250, ATPanelMixY + 5);
        setMixY( ATPanelMixY + 5);

    }

    private void initContainer() {
        String NumColor = "style='color: #0090FF;'";

        AllStuLabel.setText("<html>应到：<span " + NumColor + ">" + studentLength + "人</html>");
        AllStuLabel.setFont(new Font("微软雅黑", Font.BOLD, 25));
        AllStuLabel.setBounds(5, 0, 250, 30);
        ATPanelMixY = AllStuLabel.getHeight();
        this.add(AllStuLabel);

        AttendStuLabel.setText("<html>实到：<span " + NumColor + ">" + (studentLength - studentLateLength) + "人</html>");
        AttendStuLabel.setFont(new Font("微软雅黑", Font.BOLD, 25));
        AttendStuLabel.setBounds(5, ATPanelMixY, 250, 30);
        ATPanelMixY = ATPanelMixY + AttendStuLabel.getHeight();
        this.add(AttendStuLabel);

        LateStuLabel.setText("<html>迟到：<span style='color: red;'>" + studentLateLength + "人</html>");
        LateStuLabel.setFont(new Font("微软雅黑", Font.BOLD, 25));
        LateStuLabel.setBounds(5, ATPanelMixY, 250, 30);
        ATPanelMixY = ATPanelMixY + LateStuLabel.getHeight();
        this.add(LateStuLabel);

        initLateList();

        /*ImageIcon imageIcon = new ImageIcon(getClass().getResource("/image/settings.png"));
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
        CTButton settings = new CTButton(imageIcon,30,() -> {


            new InfSetDialog(this, LeaveListPath, () -> {
                try {
                    refreshAttendanceData(); // 自定义刷新方法
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }).setVisible(true);
        });

        settings.setLocation(200, 0);

        this.add(settings);*/
    }

    private void initLateList() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");

        int size = leaveList.size();
        int index = (size + 2) / 3;  // 修正组数计算逻辑

        for (int i = 0; i < index; i++) {
            int base = i * 3;
            sb.append(leaveList.get(base));

            if (base + 1 < size) {
                sb.append(",").append(leaveList.get(base + 1));
            }
            if (base + 2 < size) {
                sb.append(",").append(leaveList.get(base + 2));
            }

            if (i < index - 1) {  // 仅在非最后一组后添加换行
                sb.append("<br>");
            }
        }

        sb.append("</html>");


        personLabel.setText(sb.toString());
        personLabel.setFont(new Font("微软雅黑", Font.BOLD, 23));
        personLabel.setForeground(new Color(0x0090FF));
        personLabel.setBounds(5, ATPanelMixY, 250, 30 * index);
        ATPanelMixY = ATPanelMixY + personLabel.getHeight();

        this.add(personLabel);
    }

    public void refreshAttendanceData() throws IOException {
        // 清空旧数据
        studentList.clear();
        leaveList.clear();

        // 重新加载数据
        initStuList(AllStudentPath, LeaveListPath);
        System.out.println( studentList);
        System.out.println("leave:" + leaveList);

        // 更新UI组件

        this.removeAll();
        ATPanelMixY = 0;
        /*String NumColor = "style='color: #0090FF;'";

        AllStuLabel.setText("<html>应到：<span " + NumColor + ">" + studentLength + "人</html>");
        AttendStuLabel.setText("<html>实到：<span " + NumColor + ">" + (studentLength - studentLateLength) + "人</html>");
        LateStuLabel.setText("<html>迟到：<span style='color: red;'>" + studentLateLength + "人</html>");

        initLateList();*/
        initContainer();

        resetMixY(ATPanelMixY);
        this.setSize(250, ATPanelMixY + 5);



        // 强制重绘
        revalidate();
        repaint();
    }

    private void initStuList(File AllStudentPath, File LeaveListPath) throws IOException {
        //获取所有学生名单
        {
            IOStreamForInf ioStreamForInf = new IOStreamForInf(AllStudentPath);

            String inf = ioStreamForInf.GetInf();

            //System.out.println(inf);
            if (inf.isEmpty() || inf.equals("error")) {
                {
                    //将数据改为默认
                    ioStreamForInf.SetInf("""
                            曾世通
                            陈昌焱
                            陈权浩
                            陈思源
                            程政
                            程睿智
                            范祖轩
                            贾梓瑾
                            姜宇航
                            赖奕鑫
                            李宇涵
                            林宇轩
                            刘弘祥
                            刘诗怡
                            刘欣妍
                            刘雨涵
                            芦其松
                            罗伟钦
                            骆钦阳
                            毛佳欣
                            毛嘉锐
                            彭靖波
                            唐志涛
                            万涵予
                            万思遥
                            万文卿
                            万延康
                            吴鹤轩
                            吴宇恒
                            吴梓豪
                            伍子阳
                            熊天晴
                            徐浩
                            晏文杰
                            杨沛妍
                            姚嘉鑫
                            姚焱竣
                            易家树
                            俞皓天
                            袁立志
                            章智峰
                            赵银涛
                            钟世豪
                            朱彦轩
                            祝思哲
                            邹子煦
                            """);
                }


            } else {
                studentList.addAll(Arrays.asList(inf.split("\n")));
                studentLength = studentList.size();
            }
        }

        //获取请假名单
        {
            IOStreamForInf ioStreamForInf = new IOStreamForInf(LeaveListPath);
            String inf = ioStreamForInf.GetInf();
            if (inf.isEmpty() || inf.equals("error")) {

            }else{
                leaveList.addAll(Arrays.asList(inf.split(",")));
                studentLateLength = leaveList.size();
            }
        }


    }
}
