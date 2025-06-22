package com.wmp.classTools.infSet.panel;

import com.wmp.Main;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTProButton;
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

    private CTProButton getButton(String name, String DATA_PATH) throws MalformedURLException {
        CTProButton button = new CTProButton(name/*, GetIcon.getIcon(Main.class.getResource("/image/light/delete_0.png"), 30, 30)*/);
        button.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));
        button.addActionListener(e -> {
            deleteDir(DATA_PATH);
        });
        return button;

    }

    private void deleteDir(String DATA_PATH) {

        int i = Log.info.showChooseDialog(this, "清理临时文件", "是否清理?");
        if (i == JOptionPane.NO_OPTION) {
            return;
        }

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
