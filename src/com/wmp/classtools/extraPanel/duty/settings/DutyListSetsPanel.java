package com.wmp.classTools.extraPanel.duty.settings;

import com.wmp.PublicTools.UITools.GetIcon;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.io.InfProcess;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTSetsPanel;
import com.wmp.classTools.CTComponent.CTTable;
import com.wmp.classTools.CTComponent.CTTextButton;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class DutyListSetsPanel extends CTSetsPanel {

    private final File DutyListPath;

    private final CTTable DutyTable = new CTTable();
    private final AtomicInteger index = new AtomicInteger();

    public DutyListSetsPanel(String basicDataPath) {
        super(basicDataPath);
        setName("值日生");

        File dataPath = new File(basicDataPath, "Duty");

        this.DutyListPath = new File(dataPath, "DutyList.txt");

        //初始化
        try {
            String[][] dutyList = getDutyList(this.DutyListPath);
            initDuSet(dutyList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void initDuSet(String[][] dutyList) throws IOException {

        this.removeAll();

        this.setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel(dutyList,
                new String[]{"扫地", "擦黑板"});
        DutyTable.setModel(model);

        JScrollPane scrollPane = new JScrollPane(DutyTable);
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        //新建
        {

            CTTextButton newBtn = new CTTextButton("添加", GetIcon.getIcon(getClass().getResource("/image/light/new_0.png"), 30, 30));
            newBtn.addActionListener(e -> {
                //检测内容是否为空
                boolean b = true;
                String s1 = "null";
                String s2 = "null";
                while (b) {
                    s1 = Log.info.showInputDialog(this, "InfSetDialog-新建", "请输入擦黑板人员");

                    if (s1 != null && !s1.trim().isEmpty() && !(s1.indexOf('[') != -1 || s1.indexOf(']') != -1)) {
                        b = false;
                    } else if (s1 == null) {
                        return;
                    }
                }

                b = true;
                while (b) {
                    s2 = Log.info.showInputDialog(this, "InfSetDialog-新建", "请输入扫地人员");
                    if (s2 != null && !s2.trim().isEmpty() && !(s2.indexOf('[') != -1 || s2.indexOf(']') != -1)) {
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
                int selectedRow = DutyTable.getSelectedRow();
                if (selectedRow != -1) {
                    model.removeRow(selectedRow);
                    if (selectedRow < index.get()) {
                        index.getAndDecrement();
                    }
                    if (DutyTable.getRowCount() == index.get()) {
                        index.set(0);
                    }
                }
            });
            buttonPanel.add(deleteBtn);
        }

        this.add(buttonPanel, BorderLayout.SOUTH);

    }

    private String[][] getDutyList(File dutyPath) throws IOException {
        //获取inf
        IOForInfo ioForInfo = new IOForInfo(dutyPath);

        String[] inf = ioForInfo.getInfo();

        //System.out.println(inf);
        if (inf[0].equals("err")) {
            //总会有的
            ioForInfo.setInfo("[尽快,设置] [请]",
                    "[尽快,设置,1] [请]");
            return new String[][]{{"err"}, {"null"}};
        }


        //处理inf

        //初级数据-[0]"[xxx][xxx,xxx]" [1]...
        String[] inftempList = inf;

        String[][] list = new String[inftempList.length][2];
        ArrayList<String[]> tempList = new ArrayList<>();

        int i = 0;
        for (String s : inftempList) {
            //二级数据- [0]"xxx" [1]"xxx,xxx"
            //三级数据- [0]{"xxx"} [1]{"xxx","xxx"}
            ArrayList<String> strings = InfProcess.NDExtractNames(s);

            if (strings.size() == 2) {
                list[i][0] = strings.get(0);
                list[i][1] = strings.get(1);
            }
            i++;
        }
        return list;
    }

    @Override
    public void save() {
        //保存数据-dutyList


        //处理表格中的数据


        // 遍历表格中的每一行，将每一行的数据添加到tempList中
        //getRowCount()-行数
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < DutyTable.getRowCount(); i++) {

            //getColumnCount()-列数
            for (int j = 0; j < DutyTable.getColumnCount(); j++) {

                sb.append("[").append(DutyTable.getValueAt(i, j)).append("]");
            }
            sb.append("\n");
        }
        //System.out.println("表格数据:" + sb);
        //System.out.println("--index:" + index.get());

        try {
            new IOForInfo(DutyListPath).setInfo(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void refresh() throws IOException {

        this.removeAll();
        //初始化
        try {
            String[][] dutyList = getDutyList(this.DutyListPath);
            initDuSet(dutyList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.revalidate();
        this.repaint();
    }
}
