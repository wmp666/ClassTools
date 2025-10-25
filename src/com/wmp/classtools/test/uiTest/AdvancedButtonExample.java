package com.wmp.classTools.test.uiTest;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class AdvancedButtonExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("高级按钮示例");
            frame.setSize(400, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel panel = new JPanel(new FlowLayout());

            // 创建自定义边框
            Border customBorder = BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(0, 100, 200), 3),        // 外边框
                    new EmptyBorder(10, 20, 10, 20)                   // 内边距
            );

            // 创建按钮
            JButton customButton = new JButton("专业按钮");

            // 关键配置
            customButton.setContentAreaFilled(false); // 禁用默认填充
            customButton.setBorder(customBorder);     // 设置自定义边框
            customButton.setOpaque(true);             // 允许背景绘制

            // 初始颜色
            Color normalColor = new Color(200, 220, 255);
            Color hoverColor = new Color(170, 200, 255);
            Color pressedColor = new Color(140, 180, 240);

            customButton.setBackground(normalColor);
            customButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
            customButton.setForeground(Color.DARK_GRAY);

            // 状态变化监听器
            customButton.addChangeListener(e -> {
                ButtonModel model = customButton.getModel();
                if (model.isPressed()) {
                    customButton.setBackground(pressedColor);
                } else if (model.isRollover()) {
                    customButton.setBackground(hoverColor);
                } else {
                    customButton.setBackground(normalColor);
                }
            });

            // 添加点击事件
            customButton.addActionListener(e ->
                    System.out.println("按钮被点击！"));

            panel.add(customButton);
            frame.add(panel);
            frame.setVisible(true);
        });
    }
}