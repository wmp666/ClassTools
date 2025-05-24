package com.wmp.classTools.infSet.panel;

import com.wmp.Main;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.classTools.CTComponent.CTButton;
import com.wmp.classTools.CTComponent.CTSetsPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;

public class ClearTempPanel extends CTSetsPanel {

    public ClearTempPanel(String basicDataPath) {
        super(basicDataPath);

        setName("清理临时文件");

        try {
            initUI();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }

    private void initUI() throws MalformedURLException {
        this.setBackground(Color.WHITE);
        this.setLayout(new GridLayout(0, 1, 5, 5));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBounds(0, 0, 400, 45);

        CTButton clearTemp = new CTButton(CTButton.ButtonText, "清除临时文件",
                "/image/%s/delete_0.png",
                "/image/%s/delete_1.png", 35, 150, () -> {
            try {
                IOForInfo.deleteDirectoryRecursively(Paths.get(Main.TEMP_PATH));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        buttonPanel.add(clearTemp);

        CTButton clearLog = new CTButton(CTButton.ButtonText, "清除日志",
                "/image/%s/delete_0.png",
                "/image/%s/delete_1.png", 35, 150, () -> {
            try {
                IOForInfo.deleteDirectoryRecursively(Paths.get(Main.DATA_PATH + "Log\\"));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        buttonPanel.add(clearLog);

        this.add(buttonPanel);
    }

    @Override
    public void save() {

    }

    @Override
    public void refresh() throws IOException {
        this.removeAll();
        initUI();
        this.revalidate();
        this.repaint();
    }
}
