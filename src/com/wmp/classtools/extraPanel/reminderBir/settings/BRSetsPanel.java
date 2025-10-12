package com.wmp.classTools.extraPanel.reminderBir.settings;

import com.wmp.PublicTools.UITools.GetIcon;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
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
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class BRSetsPanel extends CTSetsPanel {

    private final File birthdayPath;
    private final CTTable BRTable = new CTTable();
    private final AtomicInteger index = new AtomicInteger();

    public BRSetsPanel(String basicDataPath) {
        super(basicDataPath);
        setName("生日表");

        birthdayPath = new File(basicDataPath, "birthday.json");

        //获取数据{{姓名},{日期}}
        //绘制UI
        try {
            initTable(getArray());
        } catch (Exception e) {
            Log.err.print(getClass(), "获取生日数据失败", e);
        }
    }

    private void initTable(String[][] array) throws MalformedURLException {
        this.removeAll();

        this.setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel(array,
                new String[]{"姓名", "日期"});
        BRTable.setModel(model);

        JScrollPane scrollPane = new JScrollPane(BRTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        //新建
        {
            CTTextButton newBtn = new CTTextButton("添加",
                    GetIcon.getIcon(getClass().getResource("/image/light/new_0.png"), 30, 30));
            newBtn.addActionListener(e -> {
                //检测内容是否为空
                boolean b = true;
                String s1 = "null";
                String s2 = "01-01";
                while (b) {
                    s1 = Log.info.showInputDialog(this, "BRSetsPanel-新建", "请输入姓名");

                    if (s1 != null && !s1.trim().isEmpty()) {
                        b = false;
                    } else if (s1 == null) {
                        return;
                    }
                }

                b = true;
                while (b) {
                    String[] inputs = Log.info.showInputDialog(this, "BRSetsPanel-新建", "请输入日期(MM-dd)\n如:01-20\n农历日期可以不加\"0\"补空位", "公历", "农历");
                    s2 = (inputs[0].equals("农历") ? "lunar" : "") + inputs[1];
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
            CTTextButton deleteBtn = new CTTextButton("删除",
                    GetIcon.getIcon(getClass().getResource("/image/light/delete_0.png"), 30, 30));
            deleteBtn.addActionListener(e -> {
                int selectedRow = BRTable.getSelectedRow();
                if (selectedRow != -1) {
                    model.removeRow(selectedRow);
                    if (selectedRow < index.get()) {
                        index.getAndDecrement();
                    }
                    if (BRTable.getRowCount() == index.get()) {
                        index.set(0);
                    }
                }
            });


            buttonPanel.add(deleteBtn);
        }

        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    private String[][] getArray() {
        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<String> dateList = new ArrayList<>();
        if (!birthdayPath.exists()) return new String[][]{{}, {}};
        try {
            String infos = IOForInfo.getInfos(birthdayPath.toURI().toURL());
            JSONArray jsonArray = new JSONArray(infos);
            jsonArray.forEach(object -> {
                if (object instanceof JSONObject jsonObject) {
                    nameList.add(jsonObject.getString("name"));
                    dateList.add(jsonObject.getString("birthday"));
                }
            });
            String[][] temp = new String[nameList.size()][2];
            for (int i = 0; i < nameList.size(); i++) {
                temp[i][0] = nameList.get(i);
                temp[i][1] = dateList.get(i);
            }
            return temp;
        } catch (Exception e) {
            Log.err.print(getClass(), "获取生日数据失败", e);
        }
        return null;
    }

    @Override
    public void save() throws Exception {
        IOForInfo ioForInfo = new IOForInfo(birthdayPath);

        //将表格中的数组转化成JsonArray
        JSONArray jsonArray = new JSONArray();
        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<String> dateList = new ArrayList<>();
        for (int i = 0; i < BRTable.getRowCount(); i++) {

            //getColumnCount()-列数
            for (int j = 0; j < BRTable.getColumnCount(); j++) {
                if (j == 0) {
                    nameList.add(BRTable.getValueAt(i, j).toString());
                } else {
                    dateList.add(BRTable.getValueAt(i, j).toString());
                }
                //BRTable.getValueAt(i, j);
            }

        }
        nameList.forEach(s -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", s);
            jsonObject.put("birthday", dateList.get(nameList.indexOf(s)));
            jsonArray.put(jsonObject);
        });
        ioForInfo.setInfo(jsonArray.toString());
    }

    @Override
    public void refresh() throws IOException {
        try {
            initTable(getArray());
        } catch (Exception e) {
            Log.err.print(getClass(), "获取生日数据失败", e);
            throw new RuntimeException(e);
        }

        this.revalidate();
        this.repaint();
    }
}
