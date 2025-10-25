package com.wmp.classTools.extraPanel.countdown.settings;

import com.wmp.PublicTools.DateTools;
import com.wmp.PublicTools.UITools.GetIcon;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.*;
import com.wmp.classTools.CTComponent.CTButton.CTTextButton;
import com.wmp.classTools.CTComponent.CTPanel.CTSetsPanel;
import com.wmp.classTools.extraPanel.countdown.CDInfoControl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;

public class CountDownSetsPanel extends CTSetsPanel {

    private final CTTable CDTable = new CTTable();


    public CountDownSetsPanel(String basicDataPath) {
        super(basicDataPath);

        this.setID("CountDownSetsPanel");
        this.setName("倒计时设置");
        this.setLayout(new BorderLayout());

        initTable();
    }

    private Object[][] getInfo() {
        CDInfoControl.CDInfo[] cdInfos = CDInfoControl.getCDInfos();
        Object[][] data = new Object[cdInfos.length][2];

        for (int i = 0; i < cdInfos.length; i++) {
            data[i][0] = cdInfos[i].title();
            data[i][1] = cdInfos[i].targetTime();
        }
        if (cdInfos.length == 0) {
            data = new Object[][]{{"null", "9999.12.30 23:59:59"}};
        }

        return data;
    }



    private void initTable() {
        Log.info.print("CDSetsPanel", "倒计时设置面板");

        DefaultTableModel model = new DefaultTableModel(getInfo(), new String[]{"标题", "目标时间"});

        CDTable.setModel(model);

        JScrollPane scrollPane = new JScrollPane(CDTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        //新建
        {

            CTTextButton newBtn = new CTTextButton("添加", GetIcon.getIcon(getClass().getResource("/image/light/new_0.png"), 30, 30));
            newBtn.addActionListener(e -> {
                //检测内容是否为空
                boolean b = true;
                String s1 = "null";
                String s2 = "9999.12.30 23:59:59";
                while (b) {
                    s1 = Log.info.showInputDialog(this, "CDSetsPanel-新建", "请输入标题");

                    if (s1 != null && !s1.trim().isEmpty()) {
                        b = false;
                    } else if (s1 == null) {
                        return;
                    }
                }

                b = true;
                while (b) {
                    int[] date = Log.info.showTimeChooseDialog(this, "CDSetsPanel-新建", "请选择日期", CTOptionPane.YEAR_MONTH_DAY);
                    int[] time = Log.info.showTimeChooseDialog(this, "CDSetsPanel-新建", "请选择时间", CTOptionPane.HOURS_MINUTES_SECOND);
                    
                    // 检查用户是否取消了操作
                    if (date.length == 0 || time.length == 0) {
                        return;
                    }
                    
                    // 将数组中的数据转换为字符串, 格式化为yyyy.MM.dd HH:mm:ss
                    s2 = DateTools.getDateStr(date, CTOptionPane.YEAR_MONTH_DAY, '.') + " " + DateTools.getTimeStr(time, CTOptionPane.HOURS_MINUTES_SECOND, ':');

                    if (!s2.trim().isEmpty()) {
                        b = false;
                    }
                }

                model.addRow(new Object[]{s1, s2});

            });
            buttonPanel.add(newBtn);
        }

        // 删除
        {

            CTTextButton deleteBtn = new CTTextButton("删除", GetIcon.getIcon(getClass().getResource("/image/light/delete_0.png"), 30, 30));
            deleteBtn.addActionListener(e -> {

                int selectedRow = CDTable.getSelectedRow();
                if (selectedRow != -1) {
                    model.removeRow(selectedRow);
                }
            });
            buttonPanel.add(deleteBtn);
        }

        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void save() throws Exception {
        Object[][] data = CDTable.getData();
        CDInfoControl.CDInfo[] cdInfo = new CDInfoControl.CDInfo[data.length];
        for (int i = 0; i < data.length; i++) {
            cdInfo[i] = new CDInfoControl.CDInfo(data[i][0].toString(), data[i][1].toString());
        }
        CDInfoControl.setCDInfo(cdInfo);
    }

    @Override
    public void refresh() throws IOException {
        this.removeAll();

        initTable();

        this.revalidate();
        this.repaint();
    }
}
