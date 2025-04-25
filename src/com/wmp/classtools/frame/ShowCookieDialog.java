package com.wmp.classTools.frame;

import com.wmp.Main;
import com.wmp.PublicTools.GetIcon;
import com.wmp.PublicTools.OpenInExp;
import com.wmp.PublicTools.io.GetPath;
import com.wmp.PublicTools.io.ZipPack;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTButton;
import com.wmp.classTools.frame.tools.cookie.CookieSets;
import com.wmp.classTools.frame.tools.cookie.FileDragDropLabel;
import com.wmp.classTools.frame.tools.cookie.GetCookie;
import com.wmp.classTools.frame.tools.cookie.StartCookie;
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

        CTButton removeCookie = new CTButton(CTButton.ButtonText, "修改插件",
                "/image/light/settings_0.png", "/image/light/settings_1.png", 30,100,
                () -> {
                    String cookiePin = s[0];
                    try {
                        CookieSets.CookieSetsDialog(cookieMap.get(cookiePin));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }catch (JSONException e){
                        JOptionPane.showMessageDialog(this, "插件设置文件格式错误", "错误", JOptionPane.ERROR_MESSAGE);
                        throw new RuntimeException(e);
                    }
                }
        );
        removeCookie.setBorderPainted(true);
        removeCookie.setBackground(Color.WHITE);
        removeCookie.setForeground(Color.BLACK);
        removeCookie.setEnabled(false);
        cookieSettingPanel.add(removeCookie);

        CTButton deleteCookie = new CTButton(CTButton.ButtonText, "删除插件",
                "/image/light/delete_0.png", "/image/light/delete_1.png", 30,100,
                () -> {
                    String cookiePin = s[0];
                    CookieSets.deleteCookie(cookieMap.get(cookiePin));
                }
        );
        deleteCookie.setBorderPainted(true);
        deleteCookie.setBackground(Color.WHITE);
        deleteCookie.setForeground(Color.BLACK);
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
            JButton cookieButton = new JButton(getCookie.getName(key));
            cookieButton.setBackground(Color.WHITE);
            if (getCookie.getCookieMap().get(key).getIcon() != null){
                cookieButton.setIcon(getCookie.getCookieMap().get(key).getIcon());
            }
            cookieButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));//显示边框
            cookieButton.setFont(new Font("微软雅黑", Font.BOLD, 18));
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
        addCookie.setFont(new Font("微软雅黑", Font.BOLD, 18));
        addCookie.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 添加文件
                try {
                    CookieSets.CookieSetsDialog();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (JSONException ex) {
                    JOptionPane.showMessageDialog(c, "插件设置文件格式错误", "错误", JOptionPane.ERROR_MESSAGE);
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

        CTButton openInExp = new CTButton(CTButton.ButtonText, "打开所在目录",
                "/image/openExp.png",
                "/image/openExp.png", 30, 100, () -> {
            OpenInExp.open(cookieMap.get(s[0]).getPath());
        });
        openInExp.setBorderPainted(true);
        openInExp.setForeground(Color.BLACK);
        openInExp.setBackground(Color.WHITE);
        openInExp.setEnabled(false);
        controlPanel.add(openInExp);

        CTButton outputBtn = new CTButton(CTButton.ButtonText, "导出",
                "/image/light/update_0.png",
                "/image/light/update_0.png", 30, 100, () -> {
            String path = GetPath.getDirectoryPath(this, "请选择导出目录");
            //将选中的插件文件夹打包为.zip
            ZipPack.createZip(path, cookieMap.get(s[0]).getParent(), s[0] + ".zip", cookieMap.get(s[0]).getName());

        });
        outputBtn.setBorderPainted(true);
        outputBtn.setForeground(Color.BLACK);
        outputBtn.setBackground(Color.WHITE);
        outputBtn.setEnabled(false);
        controlPanel.add(outputBtn);

        CTButton runCookie = new CTButton(CTButton.ButtonText, "运行",
                "/image/wish.png",
                "/image/wish.png",30,100, () -> {
            StartCookie.showCookie(s[0]);
        });
        runCookie.setBorderPainted(true);
        runCookie.setForeground(Color.BLACK);
        runCookie.setBackground(Color.WHITE);
        runCookie.setEnabled(false);
        controlPanel.add(runCookie);

        JScrollPane controlScrollPane = new JScrollPane(controlPanel);

        c.add(controlScrollPane, BorderLayout.SOUTH);
        initControlPanel result = new initControlPanel(openInExp, outputBtn, runCookie);
        return result;
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

    private record initControlPanel(CTButton openInExp, CTButton outputBtn, CTButton runCookie) {
    }

    private record initCookieSetsPanel(CTButton removeCookie, CTButton deleteCookie) {
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

        JMenuItem cookieSets = new JMenuItem("修改插件");
        cookieSets.setIcon( GetIcon.getIcon(getClass().getResource("/image/light/settings_0.png"),16,16));
        cookieSets.addActionListener(e -> {
            try {
                CookieSets.CookieSetsDialog(cookieMap.get(s[0]));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (JSONException ex){
                JOptionPane.showMessageDialog(this, "插件设置文件格式错误", "错误", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException(ex);
            }
        });

        JMenuItem deleteCookie = new JMenuItem("删除插件");
        deleteCookie.setIcon( GetIcon.getIcon(getClass().getResource("/image/light/delete_0.png"),16,16));
        deleteCookie.addActionListener(e -> {
            CookieSets.deleteCookie(cookieMap.get(s[0]));
        });

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
            //JOptionPane.showMessageDialog(this, "正在加急制作...", "帮助文档(前面的区域，以后再来探索吧)", JOptionPane.INFORMATION_MESSAGE);
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
