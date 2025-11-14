package com.wmp.classTools.CTComponent.Menu;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.UITools.CTColor;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;

public class CTPopupMenu extends JPopupMenu {
    public CTPopupMenu() {
        this("");
    }

    public CTPopupMenu(String label) {
        super(label);

        this.setBackground(CTColor.backColor);

        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
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

    @Override
    public void show(Component invoker, int x, int y) {
        // 在显示前确保菜单项背景透明
        Component[] components = getComponents();
        for (Component comp : components) {
            if (comp instanceof JComponent) {
                ((JComponent) comp).setOpaque(false);
            }
        }
        super.show(invoker, x, y);

        // 使弹出菜单的窗口背景透明，确保圆角外部分不可见
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.setBackground(new Color(0, 0, 0, 0));
        }
    }
}