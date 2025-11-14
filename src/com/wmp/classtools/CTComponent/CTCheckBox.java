package com.wmp.classTools.CTComponent;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.printLog.Log;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;

public class CTCheckBox extends JCheckBox implements MouseListener, ChangeListener {
    public CTCheckBox() {
        this("");
    }

    public CTCheckBox(String text) {
        this(text, null, false);
    }

    public CTCheckBox(String text, boolean selected) {
        this(text, null, selected);
    }

    public CTCheckBox(String text, Icon icon, boolean selected) {
        super(text, icon, selected);

        if (getIcon() != null){
            Log.warn.print("CTCheckBox", "图标无法显示");
        }

        this.setOpaque(false);
        this.addMouseListener(this);
        this.addChangeListener(this);
    }

    public CTCheckBox(Icon icon, boolean selected) {
        this(null, icon, selected);
    }

    public CTCheckBox(String text, Icon icon) {
        this(text, icon, false);
    }

    public CTCheckBox(Icon icon) {
        this(null, icon, false);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.setBackground(new Color(179, 179, 179));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.setBackground(CTColor.backColor);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

        this.setBackground(new Color(218, 218, 218));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.setBackground(CTColor.backColor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        int textX = 0, textY = 0;

        // 绘制按钮
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Float(0, (getHeight() - getFont().getSize())/2, getFont().getSize()*2, getFont().getSize(), getFont().getSize(), getFont().getSize()));
        textX = getFont().getSize()*2 + getIconTextGap();

        int roundX = 0;
        if (isSelected()){
            roundX = getFont().getSize();
        }

        g2.setColor(getForeground());
        g2.fill(new RoundRectangle2D.Float(roundX, (getHeight() - getFont().getSize())/2, getFont().getSize(), getFont().getSize(), getFont().getSize(), getFont().getSize()));

        // 绘制文本
        if (getText() != null && !getText().isEmpty()) {
            FontMetrics fm = g2.getFontMetrics();
            // 计算文本的Y坐标，使其垂直居中
            textY = (height + fm.getAscent() - fm.getDescent()) / 2;
            g2.setColor(getForeground());
            g2.setFont(getFont());
            g2.drawString(getText(), textX, textY);
        }

        g2.dispose();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (this.isSelected()){
            this.setForeground(CTColor.mainColor);
        }else{
            this.setForeground(CTColor.textColor);
        }
    }

    @Override
    public void setSelected(boolean b) {
        super.setSelected(b);

        if (b){
            this.setForeground(CTColor.mainColor);
        }else{
            this.setForeground(CTColor.textColor);
        }
    }
}
