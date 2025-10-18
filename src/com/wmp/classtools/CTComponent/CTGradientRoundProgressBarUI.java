package com.wmp.classTools.CTComponent;

import com.wmp.PublicTools.UITools.CTColor;

import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class CTGradientRoundProgressBarUI extends BasicProgressBarUI {

    private final Timer timer;
    private float phase = 0.0f;

    public CTGradientRoundProgressBarUI() {
        timer = new Timer(30, e -> {
            phase += 0.025f;
            if (phase > 1.0f) {
                phase = 0.0f;
            }
            progressBar.repaint();
        });
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        timer.start();
    }

    @Override
    public void uninstallUI(JComponent c) {
        timer.stop();
        super.uninstallUI(c);
    }

    @Override
    protected void paintDeterminate(Graphics g, JComponent c) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 进度条内部区域
        int insets = 1; // 内边距，避免边框被裁剪
        int width = progressBar.getWidth() - insets * 2;
        int height = progressBar.getHeight() - insets * 2;
        int arc = height; // 圆角弧度，设置为高度可实现半圆形端角

        // 绘制背景
        g2d.setColor(CTColor.backColor);
        g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, arc, arc));

        // 计算进度条前景长度
        int progressLength = (int) (width * getProgressFraction());
        if (progressLength > 0) {
            // 创建进度条前景（圆角）
            Shape foreground = new RoundRectangle2D.Double(insets, insets, progressLength, height, arc, arc);
            /*// 设置渐变画笔
            Point2D start = new Point2D.Float(insets, insets);
            Point2D end = new Point2D.Float(progressLength, height);
            float[] fractions = {0.0f, 1.0f};
            Color[] colors = {Color.CYAN, Color.BLUE}; // 渐变
            LinearGradientPaint gradient = new LinearGradientPaint(start, end, fractions, colors);
            g2d.setPaint(gradient);*/
            g2d.setColor(CTColor.mainColor);
            g2d.fill(foreground);
        }


        g2d.dispose();
    }

    // 辅助方法：获取当前进度比例
    private double getProgressFraction() {
        double min = progressBar.getMinimum();
        double max = progressBar.getMaximum();
        double value = progressBar.getValue();
        return (value - min) / (max - min);// 进度比例
    }

    @Override
    protected void paintIndeterminate(Graphics g, JComponent c) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 进度条内部区域
        int width = progressBar.getWidth();
        int height = progressBar.getHeight();
        int arc = height; // 圆角弧度，设置为高度可实现半圆形端角

        // 绘制背景
        g2d.setColor(CTColor.backColor);
        g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, arc, arc));

        //绘制进度条
        if (phase < 0.5f) {
            double progressLength = (phase * width);
            Shape foreground = new RoundRectangle2D.Double(progressLength/2, 0, progressLength, height, arc, arc);
            g2d.setColor(CTColor.mainColor);
            g2d.fill(foreground);
        } else {
            double progressLength = ((1 - phase) * width);
            Shape foreground = new RoundRectangle2D.Double( width*((phase- 0.5f)*3/2 + 0.25f), 0,  progressLength, height, arc, arc);
            g2d.setColor(CTColor.mainColor);
            g2d.fill(foreground);
        }


        g2d.dispose();
    }
}
