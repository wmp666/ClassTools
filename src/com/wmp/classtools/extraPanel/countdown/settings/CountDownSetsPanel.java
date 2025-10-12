package com.wmp.classTools.extraPanel.countdown.settings;

import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.classTools.CTComponent.CTSetsPanel;
import com.wmp.classTools.CTComponent.CTTextField;
import com.wmp.classTools.extraPanel.countdown.CDInfoControl;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class CountDownSetsPanel extends CTSetsPanel {

    private final CTTextField titleField = new CTTextField();
    private final CTTextField targetTimeField = new CTTextField();

    public CountDownSetsPanel(String basicDataPath) {
        super(basicDataPath);

        this.setID("CountDownSetsPanel");
        this.setName("倒计时设置");
        this.setLayout(new GridBagLayout());


        initInfo();
        initUI();
    }

    private void initInfo() {
        CDInfoControl.CDInfo info = CDInfoControl.getCDInfo();
        titleField.setText(info.title());
        targetTimeField.setText(info.targetTime());
    }

    private void initUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        //gbc.insets = new Insets(5, 5, 5, 5);

        JPanel titlePanel = new JPanel(new GridLayout(0, 2));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("标题:");
        title.setForeground(CTColor.textColor);
        title.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));
        titlePanel.add(title);
        titlePanel.add(titleField);

        this.add(titlePanel, gbc);
        gbc.gridy++;

        JPanel targetTimePanel = new JPanel(new GridLayout(2, 0));
        targetTimePanel.setOpaque(false);

        JLabel targetTimeLabel = new JLabel("目标时间 (yyyy.MM.dd HH:mm:ss):");
        targetTimeLabel.setForeground(CTColor.textColor);
        targetTimeLabel.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));
        targetTimePanel.add(targetTimeLabel);
        targetTimePanel.add(targetTimeField);

        this.add(targetTimePanel, gbc);
        gbc.gridy++;
    }

    @Override
    public void save() throws Exception {
        CDInfoControl.setCDInfo(new CDInfoControl.CDInfo(titleField.getText(), targetTimeField.getText()));
    }

    @Override
    public void refresh() throws IOException {
        this.removeAll();

        initInfo();
        initUI();

        this.revalidate();
        this.repaint();
    }
}
