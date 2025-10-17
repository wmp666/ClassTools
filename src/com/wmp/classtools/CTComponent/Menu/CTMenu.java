package com.wmp.classTools.CTComponent.Menu;

import com.wmp.PublicTools.CTInfo;

import javax.swing.*;
import java.awt.*;

public class CTMenu extends JMenu {
    public CTMenu() {
    }

    public CTMenu(String s) {
        super(s);


        this.setBorderPainted(false);

        this.setLayout(new GridLayout(0, 1));

    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        // 绘制圆角矩形背景
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), CTInfo.arcw, CTInfo.arch);

        // 绘制圆角矩形边框
        g2.setColor(Color.GRAY);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CTInfo.arcw, CTInfo.arch);
        g2.dispose();
    }

    @Override
    public Insets getInsets() {
        // 返回适当的边距，确保内容不会紧贴边框
        int margin = 5;
        return new Insets(margin, margin, margin, margin);
    }
}
