package com.wmp.classTools.extraPanel.attendance.settings;

import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTIconButton;
import com.wmp.classTools.CTComponent.CTSetsPanel;
import com.wmp.classTools.CTComponent.CTTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class AllStuSetsPanel extends CTSetsPanel {

    private final File AllStuPath;

    private final CTTable allStuTable = new CTTable();

    public AllStuSetsPanel(String basicDataPath) throws IOException {
        super(basicDataPath);

        File dataPath = new File(basicDataPath, "Att");
        this.AllStuPath = new File(dataPath, "AllStu.txt");

        setName("学生名单");

        ArrayList<String> studentList = getStudentList();
        initAllStuSet(studentList);
    }


    private void initAllStuSet(ArrayList<String> studentList) throws IOException {

        this.removeAll();

        this.setLayout(new BorderLayout());

        String[][] studentListTemp = new String[studentList.size()][2];
        for (int i = 0; i < studentList.size(); i++) {
            studentListTemp[i][0] = String.valueOf(i + 1);
            studentListTemp[i][1] = studentList.get(i);
        }

        DefaultTableModel model = new DefaultTableModel(studentListTemp,
                new String[]{"序号", "姓名"});

        //allStuTable.getTableHeader().setReorderingAllowed(false);// 列不允许拖动

        allStuTable.setModel(model);

        JScrollPane scrollPane = new JScrollPane(allStuTable);
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));


        //新建
        {

            CTIconButton newBtn = new CTIconButton("添加新的项",
                    "/image/%s/new_0.png",
                    "/image/%s/new_1.png", 30, () -> {
                //检测内容是否为空
                boolean b = true;
                String s1 = "null";
                while (b) {
                    s1 = Log.info.showInputDialog(this, "InfSetDialog-新建", "请输入姓名");
                    if (s1 != null && !s1.trim().isEmpty()) {
                        b = false;
                    } else if (s1 == null) {
                        return;
                    }
                }

                model.addRow(new Object[]{String.valueOf(model.getRowCount() + 1), s1});

            });
            buttonPanel.add(newBtn);
        }

        // 删除
        {

            CTIconButton deleteBtn = new CTIconButton("删除选中的项",
                    "/image/%s/delete_0.png",
                    "/image/%s/delete_1.png", 35, () -> {


                int selectedRow = allStuTable.getSelectedRow();
                if (selectedRow != -1) {
                    model.removeRow(selectedRow);

                }
            });
            //deleteBtn.setToolTipText("删除选中的值日生记录");
            //deleteBtn.setLocation(255, 380);
            buttonPanel.add(deleteBtn);
        }

        this.add(buttonPanel, BorderLayout.SOUTH);

    }

    private ArrayList<String> getStudentList() {
        //ArrayList<JCheckBox> checkBoxList = new ArrayList<>();
        ArrayList<String> studentList = new ArrayList<>();

        //获取所有学生名单
        {
            IOForInfo ioForInfo = new IOForInfo(AllStuPath);

            String[] inf = null;
            try {
                inf = ioForInfo.GetInfo();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            //System.out.println(inf);
            if (!inf[0].equals("err")) {
                studentList.addAll(Arrays.asList(inf));
            }
        }
        return studentList;
    }

    @Override
    public void save() {
        //保存数据-人员名单
        //处理表格中的数据


        // 遍历表格中的每一行，将每一行的数据添加到tempList中
        //getRowCount()-行数
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < allStuTable.getRowCount(); i++) {


            sb.append(allStuTable.getValueAt(i, 1));
            sb.append("\n");
        }

        try {
            new IOForInfo(AllStuPath).SetInfo(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void refresh() throws IOException {
        this.removeAll();

        ArrayList<String> studentList = getStudentList();
        initAllStuSet(studentList);

        this.revalidate();
        this.repaint();
    }
}
