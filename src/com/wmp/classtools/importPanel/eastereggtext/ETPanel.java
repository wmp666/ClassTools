package com.wmp.classTools.importPanel.eastereggtext;

import com.wmp.PublicTools.EasterEgg.EETextStyle;
import com.wmp.PublicTools.EasterEgg.EasterEgg;
import com.wmp.PublicTools.UITools.*;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTPanel.CTViewPanel;
import com.wmp.classTools.CTComponent.Menu.CTPopupMenu;
import com.wmp.classTools.CTComponent.CTButton.CTTextButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class ETPanel extends CTViewPanel implements MouseListener {

    private final JLabel label;


    public ETPanel() {
        this.setLayout(new BorderLayout());
        this.setName("ETPanel");
        this.setID("ETPanel");
        this.setOpaque(false);

        label = new JLabel();


        label.addMouseListener(this);

        //刷新
        new Thread(() -> {
            while (true) {
                this.removeAll();

                /*if (CTInfo.disPanelList.contains(getID())) {
                    continue;
                }*/

                String text = EasterEgg.getText(EETextStyle.HTML);
                label.setText(text);
                label.setForeground(CTColor.mainColor);
                label.setBackground(CTColor.backColor);
                label.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.NORMAL));

                FontMetrics fm = label.getFontMetrics(label.getFont());

                // 根据文字数量调整窗口大小
                int lineCount = GetMaxSize.getLine(label.getText(), GetMaxSize.STYLE_HTML);// 行数


                // 计算新的窗口尺寸（基础尺寸 + 动态调整）
                int newWidth = GetMaxSize.getHTMLToTextMaxLength(label.getText(), fm); // 根据最大字符宽度计算总宽度
                int newHeight = lineCount * label.getFont().getSize() + 5;  // 每多一行增加30像素高度

                int maxShowHeight = (this.isScreenProductViewPanel()?6:4) * label.getFont().getSize(); // 最大显示高度

                // 设置窗口大小
                if (newHeight >= maxShowHeight) {
                    newHeight = maxShowHeight;
                }

                JScrollPane scrollPane = new JScrollPane(label);
                scrollPane.getViewport().setOpaque(false);
                scrollPane.setOpaque(false);
                scrollPane.setBorder(BorderFactory.createEmptyBorder());

                this.add(scrollPane, BorderLayout.CENTER);
                scrollPane.setPreferredSize(new Dimension(this.isScreenProductViewPanel()?Toolkit.getDefaultToolkit().getScreenSize().width:newWidth, newHeight + 20));


                this.revalidate();
                this.repaint();

                try {
                    Thread.sleep(Math.max(10000, text.replaceAll("<html>|</html>|<br>", "").length() * 100L));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }).start();
    }


    @Override
    public void refresh() throws IOException {
        label.setForeground(CTColor.mainColor);

        this.revalidate();
        this.repaint();
    }

    @Override//鼠标点击
    public void mouseClicked(MouseEvent e) {
        int button = e.getButton();
        if (button == MouseEvent.BUTTON3) {
            CTPopupMenu ETPopupMenu = new CTPopupMenu();

            CTTextButton edit = new CTTextButton("编辑");
            edit.setIcon(GetIcon.getIcon(getClass().getResource("/image/edit.png"), 20, 20));
            edit.addActionListener(event -> {
                EasterEgg.errorAction();
            });

            ETPopupMenu.add(edit);

            ETPopupMenu.show(label, e.getX(), e.getY());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
