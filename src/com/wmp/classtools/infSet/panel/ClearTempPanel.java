package com.wmp.classTools.infSet.panel;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTSetsPanel;
import com.wmp.classTools.infSet.panel.tools.DataControlUnit;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;

public class ClearTempPanel extends CTSetsPanel {

    public ClearTempPanel(String basicDataPath) {
        super(basicDataPath);

        setName("软件数据管理");

        try {
            initUI();
        } catch (Exception e) {
            Log.err.print(getClass(), "UI初始化失败", e);
        }

    }

    private void initUI() throws MalformedURLException {
        this.setBackground(Color.WHITE);
        this.setLayout(new GridLayout(0, 1, 5, 5));

        /*JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new GridLayout(0,1));
        buttonPanel.setBounds(0, 0, 400, 45);*/

        this.add(getControlUnit("临时文件", CTInfo.TEMP_PATH, true,
                new DataControlUnit("彩蛋文件", CTInfo.TEMP_PATH + "EasterEgg", true,
                        new DataControlUnit("视频文件", CTInfo.TEMP_PATH + "EasterEgg\\video", true),
                        new DataControlUnit("音频文件", CTInfo.TEMP_PATH + "EasterEgg\\music", true)),
                new DataControlUnit("网络文件下载缓存", CTInfo.TEMP_PATH + "WebTemp", true),
                new DataControlUnit("帮助文档缓存", CTInfo.TEMP_PATH + "help", true)));
        this.add(getControlUnit("数据文件", CTInfo.DATA_PATH, false,
                new DataControlUnit("日志", CTInfo.DATA_PATH + "Log", true),
                new DataControlUnit("屏保数据", CTInfo.DATA_PATH + "ScreenProduct", false),
                new DataControlUnit("值日数据", CTInfo.DATA_PATH + "Duty", false),
                new DataControlUnit("迟到数据", CTInfo.DATA_PATH + "Att", false),
                new DataControlUnit("插件", CTInfo.DATA_PATH + "Cookie", false),
                new DataControlUnit("个性化文件", CTInfo.DATA_PATH + "setUp.json", false)));

    }

    private DataControlUnit getControlUnit(String name, String DATA_PATH, boolean canDelete, DataControlUnit... childUnits) throws MalformedURLException {

        return new DataControlUnit(name, DATA_PATH, canDelete, childUnits);

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
