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

        buttonPanel.add(getButton("清除临时文件", Main.TEMP_PATH));

        buttonPanel.add(getButton("清除日志", Main.DATA_PATH + "Log\\"));

        buttonPanel.add(getButton("清理彩蛋文件", Main.TEMP_PATH + "EasterEgg\\"));
        buttonPanel.add(getButton("清理网络文件下载缓存", Main.TEMP_PATH + "WebTemp\\"));
        buttonPanel.add(getButton("清理帮助文档缓存", Main.TEMP_PATH + "help\\"));

        this.add(buttonPanel);
    }

    private static CTButton getButton(String name, String DATA_PATH) throws MalformedURLException {
        return new CTButton(CTButton.ButtonText, name,
                "/image/%s/delete_0.png",
                "/image/%s/delete_1.png", 35, 150, () -> {
            deleteDir(DATA_PATH);
        });
    }

    private static void deleteDir(String DATA_PATH) {
        try {
            IOForInfo.deleteDirectoryRecursively(Paths.get(DATA_PATH));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
