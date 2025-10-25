package com.wmp.classTools.frame;

import com.wmp.Main;
import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.OpenInExp;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.UITools.GetIcon;
import com.wmp.PublicTools.io.GetPath;
import com.wmp.PublicTools.io.ZipPack;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTButton.CTTextButton;
import com.wmp.classTools.CTComponent.Menu.CTMenu;
import com.wmp.classTools.CTComponent.Menu.CTMenuItem;
import com.wmp.classTools.frame.tools.cookie.*;
import org.json.JSONException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.TreeSet;

public class ShowCookieDialog extends JDialog{

    private String[] s = {"null", "null"};

    private final TreeSet<JButton> cookiePanelList = new TreeSet<>(Comparator.comparing(JButton::getText));
    private final TreeMap<String, File> cookieMap = new TreeMap<>();

    private long lastModifyTime = 0;
    private File cookieDir = new File(CTInfo.DATA_PATH + "\\Cookie\\");


    public ShowCookieDialog() throws IOException {
        Log.info.systemPrint("ShowCookieDialog", "正在初始化快速启动单元展示页...");

        Log.info.loadingDialog.showDialog("ShowCookieDialog", "正在初始化快速启动单元展示页...");

        initDialog();

        initShowCookies(this.getContentPane());

        initMenuBar();

        Log.info.loadingDialog.closeDialog("ShowCookieDialog");
        this.setVisible(true);

    }

    private void initShowCookies(Container c) throws IOException {

        //快速启动单元设置面板
        initCookieSetsPanel result1 = initCookieSets(c);

        // 控制面板
        initControlPanel result2 = getInitControlPanel(c);

        // 显示快速启动单元库
        initCookieShowPanel(c, result1, result2);


    }

    private initCookieSetsPanel initCookieSets(Container c) throws MalformedURLException {
        JPanel cookieSettingPanel = new JPanel();
        cookieSettingPanel.setOpaque(false);
        cookieSettingPanel.setLayout(new GridLayout(6, 1, 20, 5));

        CTTextButton removeCookie = new CTTextButton("修改快速启动单元", GetIcon.getIcon(getClass().getResource("/image/light/settings_0.png"), 30, 30));
        removeCookie.addActionListener(e -> {

                    String cookiePin = s[0];
                    try {
                        CookieSets.CookieSetsDialog(cookieMap.get(cookiePin));
                    } catch (IOException ex) {
                        Log.err.print(c, getClass(), "快速启动单元设置文件打开失败", ex);
                        throw new RuntimeException(ex);

                    } catch (JSONException ex) {
                        Log.err.print(c, getClass(), "快速启动单元设置文件格式错误", ex);
                        throw new RuntimeException(ex);
                    }
                }
        );
        removeCookie.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.NORMAL));
        removeCookie.setEnabled(false);
        cookieSettingPanel.add(removeCookie);

        CTTextButton deleteCookie = new CTTextButton("删除启动单元", GetIcon.getIcon(getClass().getResource("/image/light/delete_0.png"), 30, 30));
        deleteCookie.addActionListener(e -> {
            String cookiePin = s[0];
            CookieSets.deleteCookie(cookieMap.get(cookiePin));
        });
        deleteCookie.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.NORMAL));
        deleteCookie.setEnabled(false);
        cookieSettingPanel.add(deleteCookie);

        c.add(cookieSettingPanel, BorderLayout.EAST);

        return new initCookieSetsPanel(removeCookie, deleteCookie);
    }

    private void initCookieShowPanel(Container c, initCookieSetsPanel result1, initControlPanel result2) throws IOException {
        cookiePanelList.clear();
        cookieMap.clear();

        JPanel cookiesPanel = new JPanel();

        cookiesPanel.removeAll();

        cookiesPanel.setOpaque(false);
        cookiesPanel.setLayout(new GridLayout(0, 1, 20, 10));

        GetCookie getCookie = new GetCookie();

        getCookie.getCookieMap().forEach((key, value) -> {
            CTTextButton cookieButton = new CTTextButton(getCookie.getName(key));
            if (getCookie.getCookieMap().get(key).getIcon() != null){
                cookieButton.setIcon(getCookie.getCookieMap().get(key).getIcon());
            }
            cookieButton.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.NORMAL));
            cookieButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));// 设置鼠标样式 - 箭头
            cookieButton.addActionListener(e->{

                Log.info.print("快速启动单元管理页", "点击了" + cookieButton.getText());
                s[0] = key;
                s[1] = cookieButton.getText();

                //cookieButton.setBorder(BorderFactory.createLineBorder(new Color(0x0090FF), 1));
                cookieButton.setForeground(new Color(0x0090FF));

                result1.removeCookie().setEnabled(true);
                result1.deleteCookie().setEnabled(true);

                result2.openInExp().setEnabled(true);
                result2.runCookie().setEnabled(true);
                result2.outputBtn().setEnabled(true);
                for (JButton label : cookiePanelList) {
                    if (!label.getText().equals(s[1])) {
                        //label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                        label.setForeground(CTColor.textColor);
                    }
                }
                cookieButton.repaint();
                cookiesPanel.repaint();
                c.repaint();
            });// 添加事件
            cookiePanelList.add(cookieButton);
            cookieMap.put(key, value.getCookiePath());
            cookiesPanel.add(cookieButton);



        });

        //添加文件
        FileDragDropLabel addCookie = new FileDragDropLabel();
        addCookie.setOpaque(false);
        addCookie.setIcon(GetIcon.getIcon(getClass().getResource("/image/light/new_0.png"),
                30, 30));
        addCookie.setHorizontalAlignment(JLabel.CENTER);
        //addCookie.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));//显示边框
        addCookie.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.NORMAL));
        addCookie.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 添加文件
                try {
                    CookieSets.CookieSetsDialog();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (JSONException ex) {
                    Log.err.print(c, getClass(), "快速启动单元设置文件格式错误", ex);
                    throw new RuntimeException(ex);
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
        });
        addCookie.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));// 设置鼠标样式 - 箭头
        cookiesPanel.add(addCookie);

        cookiesPanel.setMaximumSize(cookiesPanel.getPreferredSize());
        JScrollPane scrollPane = new JScrollPane(cookiesPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);// 设置滚动速度
        //让滚动面板无法水平滚动
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);// 禁止水平滚动
        c.add(scrollPane, BorderLayout.CENTER);

        cookiesPanel.revalidate();
        cookiesPanel.repaint();
    }

    private initControlPanel getInitControlPanel(Container c) throws MalformedURLException {
        JPanel controlPanel = new JPanel();
        controlPanel.setOpaque(false);
        controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 15));

        CTTextButton openInExp = new CTTextButton("打开所在目录", GetIcon.getIcon(Main.class.getResource("/image/openExp.png"), 30, 30));
        openInExp.addActionListener(e -> {
            OpenInExp.open(cookieMap.get(s[0]).getPath());
        });
        openInExp.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.NORMAL));
        openInExp.setEnabled(false);
        controlPanel.add(openInExp);

        CTTextButton outputBtn = new CTTextButton("导出", GetIcon.getIcon(Main.class.getResource("/image/light/update_0.png"), 30, 30));
        outputBtn.addActionListener(e -> {
            String path = GetPath.getDirectoryPath(this, "请选择导出目录");
            //将选中的快速启动单元文件夹打包为.zip
            ZipPack.createZip(path, cookieMap.get(s[0]).getParent(), s[0] + ".zip", cookieMap.get(s[0]).getName());

        });
        outputBtn.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.NORMAL));
        outputBtn.setEnabled(false);
        controlPanel.add(outputBtn);

        CTTextButton runCookie = new CTTextButton("运行", GetIcon.getIcon(Main.class.getResource("/image/wish.png"), 30, 30));
        runCookie.addActionListener(e -> {
            StartCookie.showCookie(s[0]);
        });
        runCookie.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.NORMAL));
        runCookie.setEnabled(false);
        controlPanel.add(runCookie);

        JScrollPane controlScrollPane = new JScrollPane(controlPanel);
        controlScrollPane.setOpaque(false);
        controlScrollPane.getViewport().setOpaque(false);

        c.add(controlScrollPane, BorderLayout.SOUTH);
        return new initControlPanel(openInExp, outputBtn, runCookie);
    }

    private record initControlPanel(CTTextButton openInExp, CTTextButton outputBtn, CTTextButton runCookie) {
    }

    private record initCookieSetsPanel(CTTextButton removeCookie, CTTextButton deleteCookie) {
    }
    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        CTMenu fileMenu = new CTMenu("文件");
        fileMenu.setMnemonic('F');

        CTMenuItem inputCookie = new CTMenuItem("导入启动单元(.zip)");
        inputCookie.setIcon( GetIcon.getIcon(getClass().getResource("/image/input.png"),16,16));
        inputCookie.addActionListener(e -> {
            String filePath = GetPath.getFilePath(this, "导入启动单元", ".zip", "ClassTools快速启动单元");

            if (filePath != null) {
                CookieSets.addCookie(new File(filePath));
            }

            try {
                this.removeAll();
                initShowCookies(this.getContentPane());
                this.getContentPane().repaint();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        CTMenuItem outputMenuItem = new CTMenuItem("导出启动单元(.zip)");
        outputMenuItem.setIcon( GetIcon.getIcon(getClass().getResource("/image/light/update_0.png"),16,16));
        outputMenuItem.addActionListener(e -> {
            String path = GetPath.getDirectoryPath(this, "请选择导出目录");
            //将选中的快速启动单元文件夹打包为.zip
            ZipPack.createZip(path, cookieMap.get(s[0]).getPath(), s[0] + ".zip", cookieMap.get(s[0]).getName());

        });

        CTMenuItem openInExp = new CTMenuItem("打开启动单元所在目录");
        openInExp.setIcon( GetIcon.getIcon(getClass().getResource("/image/openExp.png"),16,16));
        openInExp.addActionListener(e -> {
            OpenInExp.open(CTInfo.DATA_PATH + "\\Cookie\\");
        });


        CTMenuItem exit = new CTMenuItem("退出");
        exit.setIcon( GetIcon.getIcon(getClass().getResource("/image/light/exit_0.png"),16,16));
        exit.addActionListener(e -> {
            this.setVisible(false);
        });

        fileMenu.add(inputCookie);
        fileMenu.add(outputMenuItem);
        fileMenu.add(openInExp);
        fileMenu.add(exit);

        menuBar.add(fileMenu);

        CTMenu editMenu = new CTMenu("编辑");
        editMenu.setMnemonic('E');

        CTMenuItem cookieDownload = new CTMenuItem("下载启动单元");
        cookieDownload.setIcon(GetIcon.getIcon(getClass().getResource("/image/input.png"), 16, 16));
        cookieDownload.addActionListener(e -> {
            try {
                new CookieDownload();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        CTMenuItem cookieSets = new CTMenuItem("修改启动单元");
        cookieSets.setIcon( GetIcon.getIcon(getClass().getResource("/image/light/settings_0.png"),16,16));
        cookieSets.addActionListener(e -> {
            try {
                CookieSets.CookieSetsDialog(cookieMap.get(s[0]));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (JSONException ex){
                Log.err.print(this, getClass(), "快速启动单元设置文件格式错误", ex);
                throw new RuntimeException(ex);
            }
        });

        CTMenuItem deleteCookie = new CTMenuItem("删除启动单元");
        deleteCookie.setIcon( GetIcon.getIcon(getClass().getResource("/image/light/delete_0.png"),16,16));
        deleteCookie.addActionListener(e -> {
            CookieSets.deleteCookie(cookieMap.get(s[0]));
        });

        editMenu.add(cookieDownload);
        editMenu.add(deleteCookie);
        editMenu.add(cookieSets);

        menuBar.add(editMenu);

        CTMenu helpMenu = new CTMenu("帮助");
        helpMenu.setMnemonic('H');

        CTMenuItem helpDoc = new CTMenuItem("帮助文档");
        helpDoc.setIcon( GetIcon.getIcon(getClass().getResource("/image/doc.png"),16,16));
        helpDoc.addActionListener(e -> {
            try {
                new ShowHelpDoc(ShowHelpDoc.CONFIG_PLUGIN);
            } catch (URISyntaxException | IOException ex) {
                throw new RuntimeException(ex);
            }

        });
        helpMenu.add(helpDoc);

        menuBar.add(helpMenu);
    }

    private void initDialog() {
        this.setTitle("快速启动页");
        this.setSize((int) (500 * CTInfo.dpi), (int) (400 * CTInfo.dpi));
        this.setIconImage(GetIcon.getImageIcon(getClass().getResource("/image/light/cookie_0.png"),
                        32, 32).getImage());
        this.setLocationRelativeTo(null);
        this.setModal(true);

        Container c = this.getContentPane();

        c.setLayout(new BorderLayout());
        c.setBackground(CTColor.backColor);
    }

    public void refreshCookiePanel() throws IOException {
        Container c = this.getContentPane();
        c.removeAll();
        initShowCookies(c);
        initMenuBar();
        c.revalidate();
        c.repaint();
    }
}
