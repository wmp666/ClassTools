package com.wmp.classTools.extraPanel.duty.settings;

import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.io.InfProcess;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTIconButton;
import com.wmp.classTools.CTComponent.CTSetsPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class DutyListSetsPanel extends CTSetsPanel {

    private final File DutyListPath;

    private final JTable DutyTable = new JTable();
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

        //this.setBackground(CTColor.backColor);
        this.setLayout(null);

        DefaultTableModel model = new DefaultTableModel(dutyList,
                new String[]{"扫地", "擦黑板"});
        //设置表格的两列不可以修改顺序
        DutyTable.getTableHeader().setReorderingAllowed(false);
        DutyTable.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.SMALL));
        DutyTable.setModel(model);

        JScrollPane scrollPane = new JScrollPane(DutyTable);
        scrollPane.setBounds(20, 30, 340, 300);
        //scrollPane.setBackground(CTColor.backColor);
        this.add(scrollPane);

        JPanel buttonPanel = new JPanel();
        //buttonPanel.setBackground(CTColor.backColor);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBounds(0, 340, 400, 45);

        //新建
        {

            CTIconButton newBtn = new CTIconButton("添加新的值日生记录",
                    "/image/%s/new_0.png",
                    "/image/%s/new_1.png", 30, () -> {
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
            //newBtn.setToolTipText("添加新的值日生记录");
            //newBtn.setLocation(255, 340);
            buttonPanel.add(newBtn);
        }

        // 删除
        {

            CTIconButton deleteBtn = new CTIconButton("删除选中的值日生记录",
                    "/image/%s/delete_0.png",
                    "/image/%s/delete_1.png", 35, () -> {

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
            //deleteBtn.setToolTipText("删除选中的值日生记录");
            //deleteBtn.setLocation(255, 380);
            buttonPanel.add(deleteBtn);
        }

        this.add(buttonPanel);

    }

    private String[][] getDutyList(File dutyPath) throws IOException {
        //获取inf
        IOForInfo ioForInfo = new IOForInfo(dutyPath);

        String[] inf = ioForInfo.GetInfo();

        //System.out.println(inf);
        if (inf[0].equals("err")) {
            //总会有的
            ioForInfo.SetInfo("[尽快,设置] [请]",
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
            new IOForInfo(DutyListPath).SetInfo(sb.toString());
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
