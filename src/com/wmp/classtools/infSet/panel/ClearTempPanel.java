package com.wmp.classTools.infSet.panel;

import com.wmp.Main;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTSetsPanel;
import com.wmp.classTools.infSet.panel.tools.DataControlUnit;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;

public class ClearTempPanel extends CTSetsPanel {

    public ClearTempPanel(String basicDataPath) {
        super(basicDataPath);

        setName("软件数据文件管理");

        try {
            initUI();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }

    private void initUI() throws MalformedURLException {
        this.setBackground(Color.WHITE);
        this.setLayout(new GridLayout(0, 1, 5, 5));

        /*JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new GridLayout(0,1));
        buttonPanel.setBounds(0, 0, 400, 45);*/

        this.add(getControlUnit("临时文件", Main.TEMP_PATH, true,
                new DataControlUnit("彩蛋文件", Main.TEMP_PATH + "EasterEgg", true,
                        new DataControlUnit("视频文件", Main.TEMP_PATH + "EasterEgg\\video", true),
                        new DataControlUnit("音频文件", Main.TEMP_PATH + "EasterEgg\\music", true)),
                new DataControlUnit("网络文件下载缓存", Main.TEMP_PATH + "WebTemp", true),
                new DataControlUnit("帮助文档缓存", Main.TEMP_PATH + "help", true)));
        this.add(getControlUnit("数据文件", Main.DATA_PATH, false,
                new DataControlUnit("日志", Main.DATA_PATH + "Log", true),
                new DataControlUnit("屏保数据", Main.DATA_PATH + "ScreenProduct", false),
                new DataControlUnit("值日数据", Main.DATA_PATH + "Duty", false),
                new DataControlUnit("迟到数据", Main.DATA_PATH + "Att", false),
                new DataControlUnit("插件", Main.DATA_PATH + "Cookie", false),
                new DataControlUnit("个性化文件", Main.DATA_PATH + "setUp.json", false)));

        //this.add(getControlUnit("日志", Main.DATA_PATH + "Log\\"));

        //this.add(getControlUnit("彩蛋文件", Main.TEMP_PATH + "EasterEgg\\"));
        //this.add(getControlUnit("网络文件下载缓存", Main.TEMP_PATH + "WebTemp\\"));
        //this.add(getControlUnit("帮助文档缓存", Main.TEMP_PATH + "help\\"));

        //this.add(buttonPanel);
    }

    private DataControlUnit getControlUnit(String name, String DATA_PATH, boolean canDelete, DataControlUnit... childUnits) throws MalformedURLException {
        /*CTTextButton button = new CTTextButton(name*//*, GetIcon.getIcon(Main.class.getResource("/image/light/delete_0.png"), 30, 30)*//*);
        button.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));
        button.addActionListener(e -> {
            deleteDir(DATA_PATH);
        });
        return button;*/

        return new DataControlUnit(name, DATA_PATH, canDelete, childUnits);

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
