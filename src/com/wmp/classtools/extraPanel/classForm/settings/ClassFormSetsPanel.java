package com.wmp.classTools.extraPanel.classForm.settings;

import com.wmp.PublicTools.UITools.GetIcon;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTList;
import com.wmp.classTools.CTComponent.CTSetsPanel;
import com.wmp.classTools.CTComponent.CTTable;
import com.wmp.classTools.CTComponent.CTTextButton;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ClassFormSetsPanel extends CTSetsPanel {


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
        CTList chooseButtons = new CTList(new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"},
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

        this.add(chooseButtons, BorderLayout.NORTH);
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

    private void resetSetsPanel(int week) {
        Log.info.print("CFSetsPanel", "重置课程表设置面板");

        JPanel mainPanel = new JPanel();

        mainPanel.setLayout(new BorderLayout());

        CTTable ctTable = CFTableList.get(week - 1);
        DefaultTableModel model = (DefaultTableModel) ctTable.getModel();
        JScrollPane scrollPane = new JScrollPane(ctTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

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
                String s2 = "01-01";
                while (b) {
                    s1 = Log.info.showInputDialog(this, "CFSetsPanel-新建", "请输入课程");

                    if (s1 != null && !s1.trim().isEmpty()) {
                        b = false;
                    } else if (s1 == null) {
                        return;
                    }
                }

                b = true;
                while (b) {
                    s2 = Log.info.showInputDialog(this, "CFSetsPanel-新建", "请输入时间段(HH:mm-HH:mm)\n如:11:45-14:45");
                    if (s2 != null && !s2.trim().isEmpty()) {
                        b = false;
                    } else if (s2 == null) {
                        return;
                    }
                }

                model.addRow(new Object[]{s2, s1});

            });
            buttonPanel.add(newBtn);
        }

        // 删除
        {

            CTTextButton deleteBtn = new CTTextButton("删除", GetIcon.getIcon(getClass().getResource("/image/light/delete_0.png"), 30, 30));
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

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.add(mainPanel, BorderLayout.CENTER);
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
    public void save() throws Exception {
        Log.info.print("CFSetsPanel", "保存课程表设置");
        for (int i = 0; i < CFTableList.size(); i++) {
            CTTable table = CFTableList.get(i);
            JSONArray jsonArray = new JSONArray();
            for (int j = 0; j < table.getRowCount(); j++) {
                jsonArray.put(new JSONObject()
                        .put("class", table.getValueAt(j, 1))
                        .put("time", table.getValueAt(j, 0)));
            }
            new IOForInfo(new File(this.path + (i + 1) + ".json")).setInfo(jsonArray.toString());
        }
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
