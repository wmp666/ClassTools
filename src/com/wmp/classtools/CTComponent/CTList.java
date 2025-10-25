package com.wmp.classTools.CTComponent;

import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.classTools.CTComponent.CTButton.CTTextButton;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CTList extends JPanel {

    private SelectListener listener;
    private int direct = 0;
    private String[] choices;

    public CTList() {
        this(new String[]{}, 0, null);
    }

    /**
     * 创建一个列表
     *
     * @param choices  列表选项
     * @param direct   列表方向 0-垂直 1-水平
     * @param listener 选项点击监听器
     */
    public CTList(String[] choices, int direct, SelectListener listener) {

        this.listener = listener;
        this.direct = direct;
        this.choices = choices;

        this.setBackground(CTColor.backColor);
        this.setLayout(new GridBagLayout());

        initUI();
    }

    private void initUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(5, 0, 5, 0);

        List.of(this.choices).forEach(choice -> {
            CTTextButton button = new CTTextButton(choice, false);

            button.setHorizontalAlignment(SwingConstants.LEFT);
            button.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.NORMAL));
            button.addActionListener(e -> {
                if (this.listener == null) return;
                this.listener.select(e, choice);
            });
            this.add(button, gbc);
            if (this.direct == 0) {
                gbc.gridy++;
                gbc.gridx = 0;
            } else {
                gbc.gridx++;
                gbc.gridy = 0;
            }
        });

        if (this.direct == 0) {
            // 添加一个空的组件占据剩余空间，将按钮推到顶部
            gbc.weighty = 1.0;
            JPanel filler = new JPanel();
            filler.setOpaque(false);
            this.add(filler, gbc);
        }
    }

    public SelectListener getListener() {
        return listener;
    }

    public void setListener(SelectListener listener) {
        this.listener = listener;
    }

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;

        this.removeAll();
        initUI();
        this.revalidate();
        this.repaint();
    }

    public void resetChoices(String[] choices) {
        this.choices = choices;

        this.removeAll();
        initUI();
        this.revalidate();
        this.repaint();
    }
}

