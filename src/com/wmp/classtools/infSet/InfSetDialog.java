package com.wmp.classTools.infSet;

import com.wmp.Main;
import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.OpenInExp;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.GetIcon;
import com.wmp.PublicTools.appFileControl.IconControl;
import com.wmp.PublicTools.io.GetPath;
import com.wmp.PublicTools.io.ZipPack;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTButton.CTTextButton;
import com.wmp.classTools.CTComponent.CTList;
import com.wmp.classTools.CTComponent.CTOptionPane;
import com.wmp.classTools.CTComponent.CTPanel.setsPanel.CTSetsPanel;
import com.wmp.classTools.CTComponent.Menu.CTMenu;
import com.wmp.classTools.CTComponent.Menu.CTMenuBar;
import com.wmp.classTools.CTComponent.Menu.CTMenuItem;
import com.wmp.classTools.frame.CTTools;
import com.wmp.classTools.frame.MainWindow;
import com.wmp.classTools.infSet.panel.ClearTempPanel;
import com.wmp.classTools.infSet.panel.personalizationSets.PersonalizationPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class InfSetDialog extends JFrame implements WindowListener {

    private final Container c;

    private final ArrayList<CTSetsPanel> ctSetsPanelList = new ArrayList<>();


    private String openedPanel;

    private CTList switchPanel;

    public InfSetDialog() throws Exception {
        this(Main.allArgs.get("screenProduct:show").contains(Main.argsList) ? "屏保设置" : "个性化");
    }


    // 添加文件路径参数
    public InfSetDialog(String showPanel) throws Exception {
        Log.info.systemPrint("设置", "正在初始化设置...");
        Log.info.loading.showDialog("设置", "正在初始化设置...");

        this.openedPanel = showPanel;

        this.c = this.getContentPane();

        c.setBackground(CTColor.backColor);
        this.setIconImage(GetIcon.getImageIcon("设置", IconControl.COLOR_DEFAULT, 32, 32).getImage());
        this.setTitle("设置");
        this.setSize(400, 550);
        this.setLocationRelativeTo(null);
        this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.addWindowListener( this);

        Log.info.systemPrint("设置", "正在获取设置页面...");

        ctSetsPanelList.add(new PersonalizationPanel(CTInfo.DATA_PATH));

        MainWindow.allPanelList.forEach(ctPanel -> {
            java.util.List<CTSetsPanel> tempCTSetsPanelList = ctPanel.getCtSetsPanelList();
            if (tempCTSetsPanelList != null) {
                ctSetsPanelList.addAll(tempCTSetsPanelList);
            }
        });

        ctSetsPanelList.add(CTTools.CTToolsSetsPanel);

        ctSetsPanelList.add(new ClearTempPanel(CTInfo.DATA_PATH));

        Log.info.systemPrint("设置", "正在完成后续工作...");
        initMenuBar();


        c.setLayout(new BorderLayout());// 设置布局为边界布局

        initSaveButton();
        ctSetsPanelList.forEach(ctSetsPanel -> {
            String name = ctSetsPanel.getName();
            if (name == null) return;
            if (name.equals(this.openedPanel))
                c.add(ctSetsPanel, BorderLayout.CENTER);
        });
        c.add(initSetsPanelSwitchBar(), BorderLayout.WEST);

        this.pack();

        Log.info.loading.closeDialog("设置");
        this.setVisible(true);
    }

    private JScrollPane initSetsPanelSwitchBar() {

        String[] list = new String[this.ctSetsPanelList.size()];
        TreeMap<String, CTSetsPanel> map = new TreeMap<>();
        for (int i = 0; i < list.length; i++) {
            String name = this.ctSetsPanelList.get(i).getName();
            if (name == null) {
                name = "未命名";
            }
            list[i] = name;
            map.put(list[i], this.ctSetsPanelList.get(i));
        }

        this.switchPanel = new CTList(list, 0, (e, choice) -> {
            try {
                this.repaintSetsPanel(map.get(choice));
            } catch (Exception ex) {
                Log.err.print(getClass(), "刷新设置页面失败", ex);
            }
        });

        JScrollPane mainPanelScroll = new JScrollPane(this.switchPanel);
        mainPanelScroll.setBorder(BorderFactory.createEmptyBorder());
        mainPanelScroll.getViewport().setBackground(Color.WHITE);
        mainPanelScroll.getVerticalScrollBar().setUnitIncrement(16);

        return mainPanelScroll;
    }

    private void initMenuBar() {
        CTMenuBar menuBar = new CTMenuBar();
        menuBar.setBackground(CTColor.backColor);
        this.setJMenuBar(menuBar);

        CTMenu fileMenu = new CTMenu("文件");
        //fileMenu.setIcon(getClass().getResource("/image/file.png"));

        CTMenu openFile = new CTMenu("打开文件");
        openFile.setIcon(GetIcon.getIcon("文件夹", 16, 16));

        CTMenuItem openAppList = new CTMenuItem("软件位置");
        openAppList.setIcon(GetIcon.getIcon("文件夹", 16, 16));
        openAppList.addActionListener(e -> {
            OpenInExp.open(System.getProperty("user.dir"));
        });

        CTMenuItem openSetsList = new CTMenuItem("数据位置");
        openSetsList.setIcon(GetIcon.getIcon("文件夹", 16, 16));
        openSetsList.addActionListener(e -> {
            OpenInExp.open(CTInfo.DATA_PATH);
        });

        CTMenu InfSets = new CTMenu("数据设置");
        InfSets.setIcon(GetIcon.getIcon("设置", 16, 16));

        CTMenu getInf = new CTMenu("导入数据");
        getInf.setIcon(GetIcon.getIcon("导入", 16, 16));

        CTMenuItem getAllInf = new CTMenuItem("导入所有数据(.ctdatas)");
        getAllInf.setIcon(GetIcon.getIcon("导入", 16, 16));
        getAllInf.addActionListener(e -> {
            String filePath = GetPath.getFilePath(this, "请选择所有数据", ".ctdatas", "ClassTools");
            Thread thread = ZipPack.unzip(filePath, CTInfo.DATA_PATH);
            try {
                thread.join();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            //刷新数据
            this.setVisible(false);
            MainWindow.refreshPanel();
        });

        CTMenu inputInf = new CTMenu("导出数据");
        inputInf.setIcon(GetIcon.getIcon("更新", 16, 16));

        CTMenuItem inputAllInf = new CTMenuItem("导出所有数据(.ctdatas)");
        inputAllInf.setIcon(GetIcon.getIcon("更新", 16, 16));
        inputAllInf.addActionListener(e -> {
            File file = new File(CTInfo.DATA_PATH);
            if (!file.exists() || !file.isDirectory()) {
                Log.err.print(getClass(), "数据文件夹不存在/异常");
                return;
            }


            String path = GetPath.getDirectoryPath(this, "请选择导出目录");
            String s = Log.info.showChooseDialog(this, "请选择数据", "请选择数据", "全部数据", "部分数据");
            if (s.equals("全部数据")) {
                //将ClassTools文件夹中的文件打包为.zip
                ZipPack.createZip(path, CTInfo.DATA_PATH, "ClassTools.ctdatas");
            } else if (s.equals("部分数据")) {

                File[] files = file.listFiles();

                if (files != null) {
                    String[] names = new String[files.length];
                    for (int i = 0; i < files.length; i++) {
                        names[i] = files[i].getName();
                    }
                    String[] zipFiles = CTOptionPane.showChoicesDialog(this, "请选择数据", "请选择数据", null, CTOptionPane.INFORMATION_MESSAGE, true, names);
                    ZipPack.createZip(path, CTInfo.DATA_PATH, "ClassTools.ctdatas", zipFiles);
                }

            }


        });


        CTMenuItem exitMenuItem = new CTMenuItem("退出");
        exitMenuItem.setIcon(GetIcon.getIcon("关闭", 16, 16));
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
        CTMenu editMenu = new CTMenu("编辑");

        CTMenu setMenu = new CTMenu("设置界面");
        setMenu.setIcon(GetIcon.getIcon("设置", 16, 16));

        ctSetsPanelList.forEach(ctSetsPanel -> {
            CTMenuItem tempMenuItem = new CTMenuItem(ctSetsPanel.getName());
            tempMenuItem.addActionListener(e -> {
                try {
                    repaintSetsPanel(ctSetsPanel);
                } catch (Exception ex) {
                    Log.err.print(getClass(), "刷新设置页面失败", ex);
                }
            });
            setMenu.add(tempMenuItem);
        });

        CTMenuItem saveMenuItem = new CTMenuItem("保存");
        saveMenuItem.setIcon(GetIcon.getIcon("保存", 16, 16));
        saveMenuItem.addActionListener(e -> {
            save();

        });


        editMenu.add(setMenu);
        editMenu.add(saveMenuItem);

        menuBar.add(editMenu);


    }

    private void initSaveButton() {

        CTTextButton saveButton = new CTTextButton("保存数据", false);
        saveButton.setIcon("保存", IconControl.COLOR_COLORFUL, 35, 35);
        saveButton.addActionListener(e -> save());


        c.add(saveButton, BorderLayout.SOUTH);

    }

    private void repaintSetsPanel(CTSetsPanel panel) throws Exception {

        c.removeAll();

        this.openedPanel = panel.getName();


        JScrollPane mainPanelScroll = new JScrollPane(this.switchPanel);
        mainPanelScroll.setBorder(BorderFactory.createEmptyBorder());
        mainPanelScroll.getViewport().setBackground(Color.WHITE);
        mainPanelScroll.getVerticalScrollBar().setUnitIncrement(16);
        c.add(mainPanelScroll, BorderLayout.WEST);
        initMenuBar();
        initSaveButton();
        panel.setBackground(CTColor.backColor);
        c.add(panel, BorderLayout.CENTER);
        panel.refresh();

        c.revalidate();
        c.repaint();

    }

    private void save() {
        int result = Log.info.showChooseDialog(this, "InfSetDialog-保存", "是否保存？");
        if (result == JOptionPane.YES_OPTION) {


            ctSetsPanelList.forEach(panel -> {
                //Log.err.print("panel", panel.getName() + "\n" + this.switchPanel.getName() );
                if (panel.getName().equals(this.openedPanel)) {

                    try {
                        panel.save();
                    } catch (Exception e) {
                        Log.err.print(getClass(), "保存失败", e);
                    }
                }


            });
            MainWindow.refresh();

            //this.setVisible(false);

        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {
        //将窗口从最小化中重新显示
        this.setExtendedState(JFrame.NORMAL);
        this.setVisible(true);
    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
