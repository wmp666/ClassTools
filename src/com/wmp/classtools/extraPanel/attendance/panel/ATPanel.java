package com.wmp.classTools.extraPanel.attendance.panel;

import com.wmp.Main;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
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
    private int studentLength;// 应到人数
    private int studentLateLength; // 请假人数
    private final ArrayList<String> studentList = new ArrayList<>();
    private final ArrayList<String> leaveList = new ArrayList<>();//迟到人员

    public ATPanel(File AllStudentPath, File LeaveListPath) throws IOException {
        super();

        this.AllStudentPath = AllStudentPath;
        this.LeaveListPath = LeaveListPath;

        ArrayList<CTSetsPanel> list = new ArrayList<>();
        list.add(new LeaveListSetsPanel(Main.DATA_PATH));
        list.add(new AllStuSetsPanel(Main.DATA_PATH));
        this.setCtSetsPanelList(list);
        this.setName("ATPanel");
        this.setLayout(new GridBagLayout());

        initStuList(AllStudentPath,LeaveListPath);


        //System.out.println(studentList);

        initContainer();


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
        AllStuLabel.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG));


        AttendStuLabel.setText("<html><span " + TextColor + ">" + "实到：<span " + NumColor + ">" + (studentLength - studentLateLength) + "人</html>");
        AttendStuLabel.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG));


        LateStuLabel.setText("<html><span " + TextColor + ">" + "请假：<span style='color: red;'>" + studentLateLength + "人</html>");
        LateStuLabel.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;// 列
        gbc.gridy = 0;// 行
        gbc.fill = GridBagConstraints.BOTH;// 填充方式

        this.add(AllStuLabel, gbc);
        gbc.gridy = 1;// 列
        this.add(AttendStuLabel, gbc);
        gbc.gridy = 2;// 列
        this.add(LateStuLabel, gbc);

        if (leaveList.isEmpty()) {

            ArrayList<String> temp = new ArrayList<>();
            temp.add("无请假人员");
            JScrollPane showPeoPanel = PeoPanelProcess.getShowPeoPanel(temp);
            gbc.gridy++;// 列
            this.add(showPeoPanel, gbc);
        } else {
            JScrollPane showPeoPanel = PeoPanelProcess.getShowPeoPanel(leaveList);
            gbc.gridy++;// 列
            this.add(showPeoPanel, gbc);
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

        initContainer();



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
