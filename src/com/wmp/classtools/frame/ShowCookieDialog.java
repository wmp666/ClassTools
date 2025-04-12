package com.wmp.classTools.frame;

import com.wmp.Main;
import com.wmp.PublicTools.GetIcon;
import com.wmp.PublicTools.OpenInExp;
import com.wmp.PublicTools.io.GetPath;
import com.wmp.PublicTools.io.ZipPack;
import com.wmp.classTools.CTComponent.CTButton;
import com.wmp.classTools.frame.tools.about.ShowHelpDoc;
import com.wmp.classTools.frame.tools.cookie.GetCookie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.TreeSet;

public class ShowCookieDialog extends JDialog {

    private String[] s = {"null", "null"};

    private final TreeSet<JLabel> cookiePanelMap = new TreeSet<>(Comparator.comparing(JLabel::getText));
    private final TreeMap<String, File> cookieMap = new TreeMap<>();

    public ShowCookieDialog() throws IOException {
        initDialog();

        Container c = this.getContentPane();
        c.setLayout(new BorderLayout());


        initShowCookies(c);

        initMenuBar();

        this.setVisible(true);
    }

    private void initShowCookies(Container c) throws IOException {
        // 显示插件库
        JPanel cookiesPanel = new JPanel();
        cookiesPanel.setBackground(Color.WHITE);
        cookiesPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20,10));

        GetCookie getCookie = new GetCookie();


        getCookie.getCookieMap().forEach((key, value) -> {
            JLabel label = new JLabel(getCookie.getName(key));
            //显示边框
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            label.setFont(new Font("微软雅黑", Font.BOLD, 20));
            label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));// 设置鼠标样式 - 箭头
            label.addMouseListener(new MouseListener() {

                // 鼠标点击事件
                @Override
                public void mouseClicked(MouseEvent evt) {
                    System.out.println("点击了" + label.getText());
                    s[0] = key;
                    s[1] = label.getText();
                    label.setBorder(BorderFactory.createLineBorder(new Color(0x0090FF), 1));
                    label.setForeground(new Color(0x0090FF));
                    for (JLabel label : cookiePanelMap) {
                        if (!label.getText().equals(s[1])) {
                            label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                            label.setForeground(Color.BLACK);
                        }
                    }
                    label.repaint();
                    cookiesPanel.repaint();
                    c.repaint();
                }

                // 鼠标按下事件
                @Override
                public void mousePressed(MouseEvent evt) {

                }

                // 鼠标释放事件
                @Override
                public void mouseReleased(MouseEvent e) {

                }

                // 鼠标移入事件
                @Override
                public void mouseEntered(MouseEvent evt) {

                }

                // 鼠标移出事件
                @Override
                public void mouseExited(MouseEvent evt) {

                }


            });
            cookiePanelMap.add(label);
            cookieMap.put(key, value);
            cookiesPanel.add(label);
        });

        JScrollPane scrollPane = new JScrollPane(cookiesPanel);
        c.add(scrollPane, BorderLayout.CENTER);

        // 控制面板
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

        CTButton outputBtn = new CTButton(CTButton.ButtonText, "导出插件",
                "/image/light/update_0.png",
                "/image/light/update_1.png", 30, 100, () -> {
            String path = GetPath.getDirectoryPath(this, "请选择导出目录");
            //将选中的插件文件夹打包为.zip
            boolean b = ZipPack.createZip(path, cookieMap.get(s[0]).getParent(), s[0] + ".zip");
            if (!b){
                JOptionPane.showMessageDialog(this, "数据导出异常", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
            }
        });
        controlPanel.add(outputBtn);

        c.add(controlPanel, BorderLayout.SOUTH);
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("文件");
        fileMenu.setMnemonic('F');

        JMenuItem inputCookie = new JMenuItem("导入插件(.zip)");
        inputCookie.setIcon( GetIcon.getIcon(getClass().getResource("/image/input.png"),16,16));
        inputCookie.addActionListener(e -> {
            String filePath = GetPath.getFilePath(this, "导入插件", ".zip");
            //将zip文件解压到Cookie
            ZipPack.unzip(filePath, Main.DataPath + "\\Cookie\\");

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
            boolean b = ZipPack.createZip(path, cookieMap.get(s[0]).getParent(), s[0] + ".zip");
            if (!b){
                JOptionPane.showMessageDialog(this, "数据导出异常", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
            }
        });

        JMenuItem openInExp = new JMenuItem("打开插件所在目录");
        openInExp.setIcon( GetIcon.getIcon(getClass().getResource("/image/openExp.png"),16,16));
        openInExp.addActionListener(e -> {
            OpenInExp.open(Main.DataPath + "\\Cookie\\");
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
        this.setSize(400, 300);
        this.setIconImage(GetIcon.getImageIcon(getClass().getResource("/image/icon.png"),
                        32, 32).getImage());
        this.setLocationRelativeTo(null);
        this.setModal(true);
    }
}
