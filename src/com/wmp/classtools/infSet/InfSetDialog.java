package com.wmp.classTools.infSet;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.OpenInExp;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.UITools.GetIcon;
import com.wmp.PublicTools.io.GetPath;
import com.wmp.PublicTools.io.ZipPack;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTIconButton;
import com.wmp.classTools.CTComponent.CTSetsPanel;
import com.wmp.classTools.CTComponent.CTTextButton;
import com.wmp.classTools.frame.MainWindow;
import com.wmp.classTools.infSet.panel.ClearTempPanel;
import com.wmp.classTools.infSet.panel.PersonalizationPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class InfSetDialog extends JDialog {

    private final Container c;
    private final Runnable refreshCallback;

    private final ArrayList<CTSetsPanel> ctSetsPanelList = new ArrayList<>();


    private String openedPanel;

    public InfSetDialog(Runnable refreshCallback) throws IOException {
        this(refreshCallback, "迟到人员");
    }


    // 添加文件路径参数
    public InfSetDialog(Runnable refreshCallback, String showPanel) throws IOException {
        Log.info.systemPrint("设置", "正在初始化设置...");

        this.setBackground(CTColor.backColor);
        this.setIconImage(GetIcon.getImageIcon(getClass().getResource("/image/light/settings_0.png"), 32, 32).getImage());
        this.setTitle("设置");
        this.setSize(400, 550);
        this.setLocationRelativeTo(null);
        this.setModal(true);
        //this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.openedPanel = showPanel;

        this.c = this.getContentPane();
        this.refreshCallback = refreshCallback;

        Log.info.systemPrint("设置", "正在获取设置页面...");

        ctSetsPanelList.add(new PersonalizationPanel(CTInfo.DATA_PATH));

        MainWindow.allPanelList.forEach(ctPanel -> {
            java.util.List<CTSetsPanel> tempCTSetsPanelList = ctPanel.getCtSetsPanelList();
            if (tempCTSetsPanelList != null) {
                ctSetsPanelList.addAll(tempCTSetsPanelList);
            }
        });

        ctSetsPanelList.add(new ClearTempPanel(CTInfo.DATA_PATH));

        Log.info.systemPrint("设置", "正在完成后续工作...");
        initMenuBar();


        c.setLayout(new BorderLayout());// 设置布局为边界布局

        initSaveButton();
        ctSetsPanelList.forEach(ctSetsPanel -> {
            if (ctSetsPanel.getName().equals(this.openedPanel))
                c.add(ctSetsPanel, BorderLayout.CENTER);
        });
        c.add(initSetsPanelSwitchBar(), BorderLayout.NORTH);


        this.setVisible(true);
    }

    private JScrollPane initSetsPanelSwitchBar() {
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new GridBagLayout());


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 5, 15, 5); // 设置边距 10 5 20 5
        this.ctSetsPanelList.forEach(ctSetsPanel -> {
            CTTextButton button = new CTTextButton(ctSetsPanel.getName());
            if (openedPanel.equals(ctSetsPanel.getName())) {
                button.setForeground(new Color(0x0090FF));
            } else {
                button.setForeground(Color.BLACK);
            }
            button.setBorder(null);
            button.setBackground(Color.WHITE);
            button.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.SMALL));
            button.addActionListener(e -> {
                openedPanel = ctSetsPanel.getName();
                try {
                    this.repaintSetsPanel(ctSetsPanel);
                } catch (IOException ex) {
                    Log.err.print("InfSetDialog", "刷新设置页面失败");
                    throw new RuntimeException(ex);
                }
            });
            mainPanel.add(button, gbc);
        });

        JScrollPane mainPanelScroll = new JScrollPane(mainPanel);
        mainPanelScroll.setBorder(BorderFactory.createEmptyBorder());
        mainPanelScroll.getViewport().setBackground(Color.WHITE);
        mainPanelScroll.getVerticalScrollBar().setUnitIncrement(16);

        return mainPanelScroll;
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(CTColor.backColor);
        this.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("文件");
        //fileMenu.setIcon(getClass().getResource("/image/file.png"));

        JMenu openFile = new JMenu("打开文件");
        openFile.setIcon(GetIcon.getIcon(getClass().getResource("/image/openExp.png"), 16, 16));

        JMenuItem openAppList = new JMenuItem("软件位置");
        openAppList.setIcon(GetIcon.getIcon(getClass().getResource("/image/openExp.png"), 16, 16));
        openAppList.addActionListener(e -> {
            OpenInExp.open(System.getProperty("user.dir"));
        });

        JMenuItem openSetsList = new JMenuItem("数据位置");
        openSetsList.setIcon(GetIcon.getIcon(getClass().getResource("/image/openExp.png"), 16, 16));
        openSetsList.addActionListener(e -> {
            OpenInExp.open(CTInfo.DATA_PATH);
        });

        JMenu InfSets = new JMenu("数据设置");
        InfSets.setIcon(GetIcon.getIcon(getClass().getResource("/image/light/setting_0.png"), 16, 16));

        JMenu getInf = new JMenu("导入数据");
        getInf.setIcon(GetIcon.getIcon(getClass().getResource("/image/input.png"), 16, 16));

        JMenuItem getAllInf = new JMenuItem("导入所有数据(.ctdatas)");
        getAllInf.setIcon(GetIcon.getIcon(getClass().getResource("/image/input.png"), 16, 16));
        getAllInf.addActionListener(e -> {
            String filePath = GetPath.getFilePath(this, "请选择所有数据", ".ctdatas", "ClassTools");
            ZipPack.unzip(filePath, CTInfo.DATA_PATH);
            //刷新数据
            this.setVisible(false);
            refreshCallback.run();
        });

        JMenu inputInf = new JMenu("导出数据");
        inputInf.setIcon(GetIcon.getIcon(getClass().getResource("/image/light/update_0.png"), 16, 16));

        JMenuItem inputAllInf = new JMenuItem("导出所有数据(.ctdatas)");
        inputAllInf.setIcon(GetIcon.getIcon(getClass().getResource("/image/light/update_0.png"), 16, 16));
        inputAllInf.addActionListener(e -> {
            String path = GetPath.getDirectoryPath(this, "请选择导出目录");
            //将ClassTools文件夹中的文件打包为.zip
            ZipPack.createZip(path, CTInfo.DATA_PATH, "ClassTools.ctdatas");
        });


        JMenuItem exitMenuItem = new JMenuItem("退出");
        exitMenuItem.setIcon(GetIcon.getIcon(getClass().getResource("/image/light/exit_0.png"), 16, 16));
        exitMenuItem.addActionListener(e -> {
            save();
            this.setVisible(false);
        });

        openFile.add(openAppList);
        openFile.add(openSetsList);

        getInf.add(getAllInf);

        inputInf.add(inputAllInf);

        InfSets.add(getInf);
        InfSets.add(inputInf);

        fileMenu.add(openFile);
        fileMenu.add(InfSets);
        fileMenu.add(exitMenuItem);


        menuBar.add(fileMenu);

        //编辑
        JMenu editMenu = new JMenu("编辑");

        JMenu setMenu = new JMenu("设置界面");
        setMenu.setIcon(GetIcon.getIcon(getClass().getResource("/image/light/settings_0.png"), 16, 16));

        ctSetsPanelList.forEach(ctSetsPanel -> {
            JMenuItem tempMenuItem = new JMenuItem(ctSetsPanel.getName());
            tempMenuItem.addActionListener(e -> {
                try {
                    repaintSetsPanel(ctSetsPanel);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            setMenu.add(tempMenuItem);
        });

        JMenuItem saveMenuItem = new JMenuItem("保存");
        saveMenuItem.setIcon(GetIcon.getIcon(getClass().getResource("/image/light/save_0.png"), 16, 16));
        saveMenuItem.addActionListener(e -> {
            save();

        });


        editMenu.add(setMenu);
        editMenu.add(saveMenuItem);

        menuBar.add(editMenu);


    }

    private void initSaveButton() throws MalformedURLException {
        CTIconButton saveButton = new CTIconButton(CTIconButton.ButtonText, "保存数据",
                "/image/light/save_0.png",
                "/image/light/save_1.png", 200, 50, this::save);
        c.add(saveButton, BorderLayout.SOUTH);

    }

    private void repaintSetsPanel(CTSetsPanel panel) throws IOException {

        c.removeAll();
        c.add(initSetsPanelSwitchBar(), BorderLayout.NORTH);
        initMenuBar();
        initSaveButton();
        c.add(panel, BorderLayout.CENTER);
        panel.refresh();

        c.revalidate();
        c.repaint();

    }

    private void save() {
        int result = Log.info.showChooseDialog(this, "InfSetDialog-保存", "是否保存？");
        if (result == JOptionPane.YES_OPTION) {


            ctSetsPanelList.forEach(panel -> {
                try {
                    panel.save();
                } catch (Exception e) {
                    Log.err.print("InfSetDialog-保存", "保存失败：" + e);
                    throw new RuntimeException(e);
                }
            });

            // 保存成功后执行回调
            refreshCallback.run();
            //this.setVisible(false);

        }
    }

}
