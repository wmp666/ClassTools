package com.wmp.classtools.infSet;

import com.wmp.io.IOStreamForInf;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class InfSetDialog extends JDialog {

    // 添加文件路径参数
    public InfSetDialog(Object owner, File leaveListPath, Runnable refreshCallback) {
        this.setTitle("设置");
        this.setSize(400, 500); // 扩大窗口尺寸
        this.setLocationRelativeTo(null);
        this.setModal(true);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 请假人员设置组件
        JLabel leaveLabel = new JLabel("请假人员（每行一个姓名）:");
        leaveLabel.setBounds(20, 20, 300, 25);
        this.add(leaveLabel);

        JTextArea leaveArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(leaveArea);
        scrollPane.setBounds(20, 50, 340, 300);
        this.add(scrollPane);

        // 初始化现有数据
        try {
            if (leaveListPath.exists()) {
                String content = new IOStreamForInf(leaveListPath).GetInf();
                leaveArea.setText(content.replace(",", "\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 保存按钮
        JButton saveBtn = new JButton("保存请假名单");
        saveBtn.setBounds(120, 370, 150, 30);
        saveBtn.addActionListener(e -> {
            try {
                String names = leaveArea.getText().replace("\n", ",");
                new IOStreamForInf(leaveListPath).SetInf(names);
                JOptionPane.showMessageDialog(this, "保存成功");

                refreshCallback.run(); // 保存成功后执行回调

                this.setVisible(false);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "保存失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        this.add(saveBtn);

        // 原有资源管理器按钮调整位置
        JButton explorerBtn = new JButton("打开资源管理器");
        explorerBtn.setBounds(120, 420, 150, 30);
        explorerBtn.addActionListener(e -> {
            try {
                Runtime.getRuntime().exec("explorer.exe " + leaveListPath.getPath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        this.add(explorerBtn);

    }


    private void extracted(String path) {
        JButton button = new JButton("打开资源管理器");
        button.addActionListener(e -> {
            try {
                Runtime.getRuntime().exec("explorer.exe " + path);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        button.setBounds(100, 100, 100, 30);
        this.add(button);
    }
}
