// InfSetDialog.java
package com.wmp.classtools.CTComponent;

import com.wmp.io.IOStreamForInf;
import com.wmp.classtools.duty.type.DutyDay;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class InfSetDialog extends JDialog {
    private final int index;
    private final File configFile;
    private final Runnable refreshCallback;
    private DutyDay currentDuty;

    // 界面组件
    private JTextArea blackboardArea;
    private JTextArea floorArea;

    public InfSetDialog(Frame owner, File configFile, DutyDay currentData, int index,Runnable callback) {
        super(owner, "值日设置", true);
        this.index = index;
        this.configFile = configFile;
        this.currentDuty = currentData;
        this.refreshCallback = callback;
        initUI();
        setSize(400, 300);
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 双列编辑区域
        JPanel editPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        editPanel.add(createEditSection("擦黑板人员", currentDuty.getClBlackBroad()));
        editPanel.add(createEditSection("扫地人员", currentDuty.getClFloor()));




        // 操作按钮
        JButton confirmBtn = new JButton("确定");
        confirmBtn.addActionListener(this::saveData);
        JButton cancelBtn = new JButton("取消");
        cancelBtn.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(cancelBtn);
        buttonPanel.add(confirmBtn);

        // 新增操作按钮
        JButton addDayBtn = new JButton("添加新的一天");
        addDayBtn.addActionListener(this::addNewDay);
        JButton removeDayBtn = new JButton("删除这一天");
        removeDayBtn.addActionListener(this::removeCurrentDay);

        // 创建顶部按钮面板
        JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topButtonPanel.add(addDayBtn);
        topButtonPanel.add(removeDayBtn);

        mainPanel.add(topButtonPanel, BorderLayout.NORTH);
        mainPanel.add(editPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private JPanel createEditSection(String title, ArrayList<String> names) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));

        JTextArea area = new JTextArea(String.join("\n", names));
        area.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        if(title.contains("擦黑板")) blackboardArea = area;
        else floorArea = area;

        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        return panel;
    }

    private void saveData(ActionEvent e) {
        // 数据转换与验证
        ArrayList<String> blackboardList = parseInput(blackboardArea.getText());
        ArrayList<String> floorList = parseInput(floorArea.getText());

        if(blackboardList.isEmpty() || floorList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "人员列表不能为空", "输入错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 更新数据对象
        currentDuty.setClBlackBroad(blackboardList);
        currentDuty.setClFloor(floorList);

        // 异步保存
        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                saveToFile();
                return null;
            }

            @Override
            protected void done() {
                refreshCallback.run();
                dispose();
            }
        }.execute();
    }

    //将输入内容分割成数组
    private ArrayList<String> parseInput(String text) {
        ArrayList<String> list = new ArrayList<>();
        for(String line : text.split("\n")) {
            String trimmed = line.trim();
            if(!trimmed.isEmpty()) list.add(trimmed);
        }
        return list;
    }

    private void saveToFile() {
        try {

            // 读取已有数据
            List<String> existingData = Files.readAllLines(configFile.toPath());

            int index;
            {
                IOStreamForInf ioStreamForInf = new IOStreamForInf(new File("index.txt"));

                String inf = ioStreamForInf.GetInf();

                //System.out.println(inf);
                if(inf.equals("error")){
                    //将数据改为默认-空,需要用户自行输入数据
                    index = 0;
                    ioStreamForInf.SetInf("0");
                }else{
                    index = Integer.parseInt(inf);
                }
                System.out.println("index:" + index);
            }

            // 替换当前索引的数据（需传递索引参数）
            String newLine = serializeDutyData();
            if(index < existingData.size()) {
                existingData.set(index, newLine);
            } else {
                existingData.add(newLine);
            }

            // 写入全部数据
            boolean b = new IOStreamForInf(configFile).SetInf(String.join("\n", existingData));


            String data = serializeDutyData();
            boolean b1 = new IOStreamForInf(configFile).SetInf(data);
            System.out.println(data);
            System.out.println(b1);
            if(b) {
                JOptionPane.showMessageDialog(this, "配置保存成功", "成功", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "配置保存失败", "失败", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "配置保存失败", "IO错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String serializeDutyData() {
        return String.format("{%s}-{%s}",
            String.join(",", currentDuty.getClBlackBroad()),
            String.join(",", currentDuty.getClFloor()));
    }

    // 添加新的一天
    private void addNewDay(ActionEvent e) {
        currentDuty = new DutyDay(); // 创建新值班日对象
        blackboardArea.setText("");  // 清空输入区域
        floorArea.setText("");
    }

    // 删除当前天
    private void removeCurrentDay(ActionEvent e) {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "确认要删除当前天的配置吗？",
                "删除确认",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean deleted = configFile.delete();
                if (deleted) {
                    refreshCallback.run();
                    dispose();
                }
            } catch (SecurityException ex) {
                JOptionPane.showMessageDialog(this, "删除失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
