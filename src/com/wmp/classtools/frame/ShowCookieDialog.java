package com.wmp.classTools.frame;

import com.wmp.Main;
import com.wmp.PublicTools.OpenInExp;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.UITools.GetIcon;
import com.wmp.PublicTools.io.GetPath;
import com.wmp.PublicTools.io.ZipPack;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTProButton;
import com.wmp.classTools.frame.tools.cookie.*;
import com.wmp.classTools.frame.tools.help.ShowHelpDoc;
import org.json.JSONException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.TreeSet;

public class ShowCookieDialog extends JDialog implements WindowListener {

    private String[] s = {"null", "null"};

    private final TreeSet<JButton> cookiePanelList = new TreeSet<>(Comparator.comparing(JButton::getText));
    private final TreeMap<String, File> cookieMap = new TreeMap<>();

    private final Thread repaintCookie = new Thread(() -> {
        long lastModifyTime = 0;
        File cookieDir = new File(Main.DATA_PATH + "\\Cookie\\");

        // 监听文件修改
        while (!Thread.interrupted()) {
            try {
                long currentModifyTime = cookieDir.lastModified();

                //if (currentModifyTime != lastModifyTime) {
                    lastModifyTime = currentModifyTime;

                    SwingUtilities.invokeLater(() -> {
                        try {
                            refreshCookiePanel();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });
                //}
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    });

    public ShowCookieDialog() throws IOException {
        initDialog();

        Container c = this.getContentPane();

        c.setLayout(new BorderLayout());


        initShowCookies(c);

        initMenuBar();

        this.addWindowListener(this);
        this.setVisible(true);


        repaintCookie.start();
    }

    private void initShowCookies(Container c) throws IOException {

        //插件设置面板
        initCookieSetsPanel result1 = initCookieSets(c);

        // 控制面板
        initControlPanel result2 = getInitControlPanel(c);

        // 显示插件库
        initCookieShowPanel(c, result1, result2);


    }

    private initCookieSetsPanel initCookieSets(Container c) throws MalformedURLException {
        JPanel cookieSettingPanel = new JPanel();
        cookieSettingPanel.setBackground(Color.WHITE);
        cookieSettingPanel.setLayout(new GridLayout(6, 1, 20, 5));

        CTProButton removeCookie = new CTProButton("修改插件", GetIcon.getIcon(getClass().getResource("/image/light/settings_0.png"), 30, 30));
        removeCookie.addActionListener(e -> {

                    String cookiePin = s[0];
                    try {
                        CookieSets.CookieSetsDialog(cookieMap.get(cookiePin));
                    } catch (IOException ex) {
                        Log.err.print(c, "插件管理页", "插件设置文件打开失败");
                        throw new RuntimeException(ex);

                    } catch (JSONException ex) {
                        Log.err.print(c, "插件管理页", "插件设置文件格式错误");
                        throw new RuntimeException(ex);
                    }
                }
        );
        removeCookie.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.NORMAL));
        removeCookie.setEnabled(false);
        cookieSettingPanel.add(removeCookie);

        CTProButton deleteCookie = new CTProButton("删除插件", GetIcon.getIcon(getClass().getResource("/image/light/delete_0.png"), 30, 30));
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

        cookiesPanel.setBackground(Color.WHITE);
        cookiesPanel.setLayout(new GridLayout(0, 1, 20, 10));

        GetCookie getCookie = new GetCookie();

        getCookie.getCookieMap().forEach((key, value) -> {
            CTProButton cookieButton = new CTProButton(getCookie.getName(key));
            if (getCookie.getCookieMap().get(key).getIcon() != null){
                cookieButton.setIcon(getCookie.getCookieMap().get(key).getIcon());
            }
            cookieButton.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.NORMAL));
            cookieButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));// 设置鼠标样式 - 箭头
            cookieButton.addActionListener(e->{

                Log.info.print("插件管理页", "点击了" + cookieButton.getText());
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
                        label.setForeground(Color.BLACK);
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
        addCookie.setBackground(Color.WHITE);
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
                    Log.err.print(c, "插件管理页", "插件设置文件格式错误");
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
        //在接触滚动面板最右侧时,让cookiesPanel不能向右添加

        scrollPane.getVerticalScrollBar().setUnitIncrement(16);// 设置滚动速度
        //让滚动面板无法水平滚动
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);// 禁止水平滚动
        c.add(scrollPane, BorderLayout.CENTER);

        cookiesPanel.revalidate();
        cookiesPanel.repaint();
    }

    private initControlPanel getInitControlPanel(Container c) throws MalformedURLException {
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 15));

        CTProButton openInExp = new CTProButton("打开所在目录", GetIcon.getIcon(Main.class.getResource("/image/openExp.png"), 30, 30));
        openInExp.addActionListener(e -> {
            OpenInExp.open(cookieMap.get(s[0]).getPath());
        });
        openInExp.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.NORMAL));
        openInExp.setEnabled(false);
        controlPanel.add(openInExp);

        CTProButton outputBtn = new CTProButton("导出", GetIcon.getIcon(Main.class.getResource("/image/light/update_0.png"), 30, 30));
        outputBtn.addActionListener(e -> {
            String path = GetPath.getDirectoryPath(this, "请选择导出目录");
            //将选中的插件文件夹打包为.zip
            ZipPack.createZip(path, cookieMap.get(s[0]).getParent(), s[0] + ".zip", cookieMap.get(s[0]).getName());

        });
        outputBtn.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.NORMAL));
        outputBtn.setEnabled(false);
        controlPanel.add(outputBtn);

        CTProButton runCookie = new CTProButton("运行", GetIcon.getIcon(Main.class.getResource("/image/wish.png"), 30, 30));
        runCookie.addActionListener(e -> {
            StartCookie.showCookie(s[0]);
        });
        runCookie.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.NORMAL));
        runCookie.setEnabled(false);
        controlPanel.add(runCookie);

        JScrollPane controlScrollPane = new JScrollPane(controlPanel);

        c.add(controlScrollPane, BorderLayout.SOUTH);
        return new initControlPanel(openInExp, outputBtn, runCookie);
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        repaintCookie.interrupt();
        this.setVisible(false);
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

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

    private record initControlPanel(CTProButton openInExp, CTProButton outputBtn, CTProButton runCookie) {
    }

    private record initCookieSetsPanel(CTProButton removeCookie, CTProButton deleteCookie) {
    }
    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("文件");
        fileMenu.setMnemonic('F');

        JMenuItem inputCookie = new JMenuItem("导入插件(.zip)");
        inputCookie.setIcon( GetIcon.getIcon(getClass().getResource("/image/input.png"),16,16));
        inputCookie.addActionListener(e -> {
            String filePath = GetPath.getFilePath(this, "导入插件", ".zip", "ClassTools插件");

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

        JMenuItem outputMenuItem = new JMenuItem("导出插件(.zip)");
        outputMenuItem.setIcon( GetIcon.getIcon(getClass().getResource("/image/light/update_0.png"),16,16));
        outputMenuItem.addActionListener(e -> {
            String path = GetPath.getDirectoryPath(this, "请选择导出目录");
            //将选中的插件文件夹打包为.zip
            ZipPack.createZip(path, cookieMap.get(s[0]).getPath(), s[0] + ".zip", cookieMap.get(s[0]).getName());

        });

        JMenuItem openInExp = new JMenuItem("打开插件所在目录");
        openInExp.setIcon( GetIcon.getIcon(getClass().getResource("/image/openExp.png"),16,16));
        openInExp.addActionListener(e -> {
            OpenInExp.open(Main.DATA_PATH + "\\Cookie\\");
        });


        JMenuItem exit = new JMenuItem("退出");
        exit.setIcon( GetIcon.getIcon(getClass().getResource("/image/light/exit_0.png"),16,16));
        exit.addActionListener(e -> {
            this.setVisible(false);
        });

        fileMenu.add(inputCookie);
        fileMenu.add(outputMenuItem);
        fileMenu.add(openInExp);
        fileMenu.add(exit);

        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu("编辑");
        editMenu.setMnemonic('E');

        JMenuItem cookieDownload = new JMenuItem("下载插件");
        cookieDownload.setIcon(GetIcon.getIcon(getClass().getResource("/image/input.png"), 16, 16));
        cookieDownload.addActionListener(e -> {
            try {
                new CookieDownload();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        JMenuItem cookieSets = new JMenuItem("修改插件");
        cookieSets.setIcon( GetIcon.getIcon(getClass().getResource("/image/light/settings_0.png"),16,16));
        cookieSets.addActionListener(e -> {
            try {
                CookieSets.CookieSetsDialog(cookieMap.get(s[0]));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (JSONException ex){
                Log.err.print(this, "插件管理页", "插件设置文件格式错误");
                throw new RuntimeException(ex);
            }
        });

        JMenuItem deleteCookie = new JMenuItem("删除插件");
        deleteCookie.setIcon( GetIcon.getIcon(getClass().getResource("/image/light/delete_0.png"),16,16));
        deleteCookie.addActionListener(e -> {
            CookieSets.deleteCookie(cookieMap.get(s[0]));
        });

        editMenu.add(cookieDownload);
        editMenu.add(deleteCookie);
        editMenu.add(cookieSets);

        menuBar.add(editMenu);

        JMenu helpMenu = new JMenu("帮助");
        helpMenu.setMnemonic('H');

        JMenuItem helpDoc = new JMenuItem("帮助文档");
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
        this.setTitle("插件库");
        this.setSize(500, 400);
        this.setIconImage(GetIcon.getImageIcon(getClass().getResource("/image/light/cookie_0.png"),
                        32, 32).getImage());
        this.setLocationRelativeTo(null);
        this.setModal(true);
        //this.setMaximumSize(new Dimension(600, 400));
        this.setResizable(false);//禁止改变大小
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
