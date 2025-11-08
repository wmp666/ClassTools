package com.wmp.classTools.extraPanel.classForm.settings;

import com.wmp.PublicTools.DateTools;
import com.wmp.PublicTools.UITools.IconControl;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.*;
import com.wmp.classTools.CTComponent.CTButton.CTTextButton;
import com.wmp.classTools.CTComponent.CTPanel.setsPanel.CTBasicSetsPanel;
import com.wmp.classTools.CTComponent.CTPanel.setsPanel.CTListSetsPanel;
import com.wmp.classTools.CTComponent.CTPanel.setsPanel.CTSetsPanel;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ClassFormSetsPanel extends CTListSetsPanel {


    private final String path;

    private final ArrayList<CTTable> CFTableList = new ArrayList<>();

    public ClassFormSetsPanel(String basicDataPath) {
        super(basicDataPath);

        this.path = basicDataPath + "ClassForm\\";
        this.setID("ClassFormSetsPanel");
        this.setName("课程表设置");
        this.setLayout(new BorderLayout());
        initSetsPanel();

        initChooseButtons();
    }

    private void initChooseButtons() {
        Log.info.print("CFSetsPanel", "初始化页面切换");
        this.clearCTList();
        for (int i = 0; i < 7; i++) {
            this.add(resetSetsPanel(i + 1));
        }
        /*CTList chooseButtons = new CTList(new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"},
                1, (e, choice) -> {
            int week = 0;
            switch (choice) {
                case "周一" -> week = 1;
                case "周二" -> week = 2;
                case "周三" -> week = 3;
                case "周四" -> week = 4;
                case "周五" -> week = 5;
                case "周六" -> week = 6;
                case "周日" -> week = 7;
            }
            this.removeAll();

            initChooseButtons();
            resetSetsPanel(week);

            this.revalidate();
            this.repaint();
        });

        JScrollPane mainPanelScroll = new JScrollPane(chooseButtons);
        mainPanelScroll.setBorder(BorderFactory.createEmptyBorder());
        mainPanelScroll.getViewport().setBackground(Color.WHITE);
        mainPanelScroll.getVerticalScrollBar().setUnitIncrement(16);

        this.add(chooseButtons, BorderLayout.NORTH);*/



        // 强制重新布局和重绘
        this.revalidate();
        this.repaint();
    }

    private void initSetsPanel() {
        Log.info.print("CFSetsPanel", "初始化课程表设置面板");
        CFTableList.clear();
        for (int i = 1; i <= 7; i++) {
            DefaultTableModel model = new DefaultTableModel(getClassFormData(i),
                    new String[]{"时间(周" + i + ")", "课程"});
            CTTable table = new CTTable();
            table.setModel(model);
            CFTableList.add(table);
        }
    }

    private CTBasicSetsPanel resetSetsPanel(int week) {
        Log.info.print("CFSetsPanel", "重置课程表设置面板:" + week);

        CTBasicSetsPanel mainPanel = new CTBasicSetsPanel(getBasicDataPath()){

            @Override
            public void refresh() throws Exception {

                this.removeAll();

                CTTable ctTable = CFTableList.get(week - 1);
                DefaultTableModel model = (DefaultTableModel) ctTable.getModel();
                JScrollPane scrollPane = new JScrollPane(ctTable);
                scrollPane.setOpaque(false);
                scrollPane.getViewport().setOpaque(false);
                this.add(scrollPane, BorderLayout.CENTER);

                JPanel buttonPanel = new JPanel();
                buttonPanel.setOpaque(false);
                buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

                //新建
                {

                    CTTextButton newBtn = new CTTextButton("添加");
                    newBtn.setIcon("添加", IconControl.COLOR_COLORFUL, 30, 30);
                    newBtn.addActionListener(e -> {
                        //检测内容是否为空
                        boolean b = true;
                        String s1 = "null";
                        String s2 = "01-01";
                        while (b) {
                            s1 = Log.info.showInputDialog(this, "CFSetsPanel-新建", "请输入课程");

                            if (s1 != null && !s1.trim().isEmpty()) {
                                b = false;
                            } else if (s1 == null) {
                                return;
                            }
                        }

                        int[] beginTime = Log.info.showTimeChooseDialog(this, "CFSetsPanel-新建", "请选择开始时间", CTOptionPane.HOURS_MINUTES);
                        int[] afterTime = Log.info.showTimeChooseDialog(this, "CFSetsPanel-新建", "请选择结束时间", CTOptionPane.HOURS_MINUTES);
                        if (beginTime.length == 1 || afterTime.length == 1) return;
                        s2 = DateTools.getTimeStr(beginTime, CTOptionPane.HOURS_MINUTES, ':') + "-" + DateTools.getTimeStr(afterTime, CTOptionPane.HOURS_MINUTES, ':');


                        model.addRow(new Object[]{s2, s1});

                    });
                    buttonPanel.add(newBtn);
                }

                // 删除
                {

                    CTTextButton deleteBtn = new CTTextButton("删除");
                    deleteBtn.setIcon("删除", IconControl.COLOR_COLORFUL, 30, 30);
                    deleteBtn.addActionListener(e -> {

                        int selectedRow = ctTable.getSelectedRow();
                        if (selectedRow != -1) {
                            model.removeRow(selectedRow);
                        }
                    });
                    //deleteBtn.setToolTipText("删除选中的值日生记录");
                    //deleteBtn.setLocation(255, 380);
                    buttonPanel.add(deleteBtn);
                }

                this.add(buttonPanel, BorderLayout.SOUTH);
                /*this.removeAll();

                CTTable ctTable = CFTableList.get(week - 1);
                DefaultTableModel model = (DefaultTableModel) ctTable.getModel();
                JScrollPane scrollPane = new JScrollPane(ctTable);
                scrollPane.setOpaque(false);
                scrollPane.getViewport().setOpaque(false);
                this.add(scrollPane, BorderLayout.CENTER);

                JPanel buttonPanel = new JPanel();
                buttonPanel.setOpaque(false);
                buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

                //新建
                {

                    CTTextButton newBtn = new CTTextButton("添加");
                    newBtn.setIcon("添加", IconControl.COLOR_COLORFUL, 30, 30);
                    newBtn.addActionListener(e -> {
                        //检测内容是否为空
                        boolean b = true;
                        String s1 = "null";
                        String s2 = "01-01";
                        while (b) {
                            s1 = Log.info.showInputDialog(this, "CFSetsPanel-新建", "请输入课程");

                            if (s1 != null && !s1.trim().isEmpty()) {
                                b = false;
                            } else if (s1 == null) {
                                return;
                            }
                        }

                        int[] beginTime = Log.info.showTimeChooseDialog(this, "CFSetsPanel-新建", "请选择开始时间", CTOptionPane.HOURS_MINUTES);
                        int[] afterTime = Log.info.showTimeChooseDialog(this, "CFSetsPanel-新建", "请选择结束时间", CTOptionPane.HOURS_MINUTES);
                        if (beginTime.length == 1 || afterTime.length == 1) return;
                        s2 = DateTools.getTimeStr(beginTime, CTOptionPane.HOURS_MINUTES, ':') + "-" + DateTools.getTimeStr(afterTime, CTOptionPane.HOURS_MINUTES, ':');


                        model.addRow(new Object[]{s2, s1});

                    });
                    buttonPanel.add(newBtn);
                }

                // 删除
                {

                    CTTextButton deleteBtn = new CTTextButton("删除");
                    deleteBtn.setIcon("删除", IconControl.COLOR_COLORFUL, 30, 30);
                    deleteBtn.addActionListener(e -> {

                        int selectedRow = ctTable.getSelectedRow();
                        if (selectedRow != -1) {
                            model.removeRow(selectedRow);
                        }
                    });
                    //deleteBtn.setToolTipText("删除选中的值日生记录");
                    //deleteBtn.setLocation(255, 380);
                    buttonPanel.add(deleteBtn);
                }

                this.add(buttonPanel, BorderLayout.SOUTH);

                this.revalidate();
                this.repaint();*/
            }

            @Override
            public void save() throws Exception {
                Log.info.print("CFSetsPanel", "保存课程表设置" + "周" + week);
                CTTable table = CFTableList.get(week - 1);
                JSONArray jsonArray = new JSONArray();
                for (int j = 0; j < table.getRowCount(); j++) {
                    jsonArray.put(new JSONObject()
                            .put("class", table.getValueAt(j, 1))
                            .put("time", table.getValueAt(j, 0)));
                }
                new IOForInfo(new File(ClassFormSetsPanel.this.path + week + ".json")).setInfo(jsonArray.toString(4));
            }
        };
        String name = switch (week) {
            case 1 -> "周一";
            case 2 -> "周二";
            case 3 -> "周三";
            case 4 -> "周四";
            case 5 -> "周五";
            case 6 -> "周六";
            case 7 -> "周日";
            default -> "未知";
        };
        mainPanel.setName(name);
        mainPanel.setOpaque(false);

        mainPanel.setLayout(new BorderLayout());



        return mainPanel;
    }

    private String[][] getClassFormData(int week) {
        Log.info.print("CFSetsPanel", "获取星期" + week + "课程表数据");

        File path = new File(this.path + week + ".json");

        ArrayList<String> CFList = new ArrayList<>();
        ArrayList<String> dateList = new ArrayList<>();
        try {
            String infos = new IOForInfo(path).getInfos();
            if (infos.equals("err")) return new String[][]{{}, {}};

            JSONArray jsonArray = new JSONArray(infos);
            jsonArray.forEach(object -> {
                if (object instanceof JSONObject jsonObject) {
                    if (!(jsonObject.has("class") && jsonObject.has("time"))) return;
                    CFList.add(jsonObject.getString("class"));
                    dateList.add(jsonObject.getString("time"));
                }
            });
            String[][] temp = new String[CFList.size()][2];
            for (int i = 0; i < CFList.size(); i++) {
                temp[i][1] = CFList.get(i);
                temp[i][0] = dateList.get(i);
            }
            return temp;
        } catch (Exception e) {
            Log.err.print(getClass(), "获取数据失败", e);
        }
        return null;
    }

    @Override
    public void refresh() throws IOException {
        this.removeAll();

        initSetsPanel();
        initChooseButtons();

        this.revalidate();
        this.repaint();
    }
}
