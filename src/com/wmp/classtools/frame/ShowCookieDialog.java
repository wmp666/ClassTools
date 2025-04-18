package com.wmp.classTools.frame;

import com.wmp.Main;
import com.wmp.PublicTools.GetIcon;
import com.wmp.PublicTools.OpenInExp;
import com.wmp.PublicTools.io.GetPath;
import com.wmp.PublicTools.io.ZipPack;
import com.wmp.classTools.CTComponent.CTButton;
import com.wmp.classTools.frame.tools.about.ShowHelpDoc;
import com.wmp.classTools.frame.tools.cookie.GetCookie;
import com.wmp.classTools.frame.tools.cookie.StartCookie;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.TreeSet;

public class ShowCookieDialog extends JDialog {

    private String[] s = {"null", "null"};

    private final TreeSet<JButton> cookiePanelMap = new TreeSet<>(Comparator.comparing(JButton::getText));
    private final TreeMap<String, File> cookieMap = new TreeMap<>();

    public ShowCookieDialog() throws IOException {
        initDialog();

        Container c = this.getContentPane();
        BorderLayout borderLayout = new BorderLayout();

        c.setLayout(new BorderLayout());


        initShowCookies(c);

        initMenuBar();

        this.setVisible(true);
    }

    private void initShowCookies(Container c) throws IOException {

        // 控制面板
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 15));

        CTButton openInExp = new CTButton(CTButton.ButtonText, "打开所在目录",
                "/image/openExp.png",
                "/image/openExp.png", 30, 100, () -> {
            OpenInExp.open(cookieMap.get(s[0]).getParent());
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
            ZipPack.createZip(path, cookieMap.get(s[0]).getParentFile().getParent(), s[0] + ".zip", cookieMap.get(s[0]).getParentFile().getName());

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

        // 显示插件库
        JPanel cookiesPanel = new JPanel();
        cookiesPanel.setBackground(Color.WHITE);
        cookiesPanel.setLayout(new GridLayout(0, 1, 20, 10));

        GetCookie getCookie = new GetCookie();


        getCookie.getCookieMap().forEach((key, value) -> {
            JButton cookieButton = new JButton(getCookie.getName(key));
            cookieButton.setBackground(Color.WHITE);
            if (getCookie.getCookieMap().get(key).getIcon() != null){
                cookieButton.setIcon(getCookie.getCookieMap().get(key).getIcon());
            }
            cookieButton.setHorizontalAlignment(JLabel.CENTER);
            //显示边框
            cookieButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            cookieButton.setFont(new Font("微软雅黑", Font.BOLD, 18));
            cookieButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));// 设置鼠标样式 - 箭头
            cookieButton.addActionListener(e->{
                if (s[0].equals(key)){
                    System.out.println("重复点击" + cookieButton.getText());
                    cookieButton.setForeground(Color.RED);

                    StartCookie.showCookie(s[0]);
                    s[0] = "null";
                    return;
                }else{
                    System.out.println("点击了" + cookieButton.getText());
                    s[0] = key;
                    s[1] = cookieButton.getText();
                }

                //cookieButton.setBorder(BorderFactory.createLineBorder(new Color(0x0090FF), 1));
                cookieButton.setForeground(new Color(0x0090FF));

                openInExp.setEnabled(true);
                runCookie.setEnabled(true);
                outputBtn.setEnabled(true);
                for (JButton label : cookiePanelMap) {
                    if (!label.getText().equals(s[1])) {
                        //label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                        label.setForeground(Color.BLACK);
                    }
                }
                cookieButton.repaint();
                cookiesPanel.repaint();
                c.repaint();
            });
            cookiePanelMap.add(cookieButton);
            cookieMap.put(key, value.getPath());
            cookiesPanel.add(cookieButton);
        });

        cookiesPanel.setMaximumSize(cookiesPanel.getPreferredSize());
        JScrollPane scrollPane = new JScrollPane(cookiesPanel);
        //在接触滚动面板最右侧时,让cookiesPanel不能向右添加

        scrollPane.getVerticalScrollBar().setUnitIncrement(16);// 设置滚动速度
        //让滚动面板无法水平滚动
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);// 禁止水平滚动
        c.add(scrollPane, BorderLayout.CENTER);


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
            //将zip文件解压到Cookie
            ZipPack.unzip(filePath, Main.DATA_PATH + "\\Cookie\\");

            try {
                new GetCookie();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        JMenuItem outputMenuItem = new JMenuItem("导出插件(.zip)");
        outputMenuItem.setIcon( GetIcon.getIcon(getClass().getResource("/image/light/update_0.png"),16,16));
        outputMenuItem.addActionListener(e -> {
            String path = GetPath.getDirectoryPath(this, "请选择导出目录");
            //将选中的插件文件夹打包为.zip
            ZipPack.createZip(path, cookieMap.get(s[0]).getAbsolutePath(), s[0] + ".zip", cookieMap.get(s[0]).getParentFile().getName());

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
        this.setSize(400, 500);
        this.setIconImage(GetIcon.getImageIcon(getClass().getResource("/image/light/cookie_0.png"),
                        32, 32).getImage());
        this.setLocationRelativeTo(null);
        this.setModal(true);
        //this.setMaximumSize(new Dimension(600, 400));
        this.setResizable(false);//禁止改变大小
    }
}
