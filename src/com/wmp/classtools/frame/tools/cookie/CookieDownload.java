package com.wmp.classTools.frame.tools.cookie;

import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.UITools.GetIcon;
import com.wmp.PublicTools.io.DownloadURLFile;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.PublicTools.web.GetWebInf;
import com.wmp.classTools.CTComponent.CTTextButton;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;

public class CookieDownload {

    /**
     * Cookie信息<br>
     * String key<br>
     * CookieInfo 快速启动单元信息
     */
    private final TreeMap<String, CookieInfo> cookieInfoMap = new TreeMap<>();

    private static final String downloadUrl = "https://github.com/wmp666/ClassTools/releases/tag/0.0.2";

    private static final String apiUrl = "https://api.github.com/repos/wmp666/ClassTools/releases/tags/0.0.2";

    /**
     * 下载官方快速启动单元
     */
    public CookieDownload() throws Exception {
        Log.info.systemPrint("CookieDownload", "正在初始化快速启动单元下载页");

        Log.info.systemPrint("CookieDownload", "正在获取数据");
        getInfo();

        Log.info.systemPrint("CookieDownload", "正在初始化UI");
        JDialog dialog = new JDialog();
        initDialog(dialog);

        initUI(dialog);

        Log.info.systemPrint("CookieDownload", "完成");
        //dialog.pack();
        dialog.setVisible(true);
    }

    private void initUI(JDialog dialog) {
        var ref = new Object() {
            String openedButtonKey = "";
            String openedButtonName = "";
            final ArrayList<JButton> cookieButtonList = new ArrayList<>();
        };

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints showCookieGbc = new GridBagConstraints();
        showCookieGbc.fill = GridBagConstraints.BOTH;//水平填充
        showCookieGbc.gridx = 0;//组件在网格中的x坐标
        showCookieGbc.gridy = 0;//组件在网格中的y坐标
        showCookieGbc.insets = new Insets(5, 5, 5, 5);//组件之间的间距
        showCookieGbc.weighty = 0;
        //展示已有快速启动单元
        cookieInfoMap.forEach((key, value) -> {
            CTTextButton button = new CTTextButton(value.getName());
            button.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.NORMAL));
            button.addActionListener(e -> {
                button.setForeground(new Color(0x0090FF));
                ref.openedButtonKey = key;
                ref.openedButtonName = button.getText();
                ref.cookieButtonList.forEach(button1 -> {
                    if (!button1.getText().equals(ref.openedButtonName))
                        button1.setForeground(Color.BLACK);
                });

            });


            panel.add(button, showCookieGbc);
            ref.cookieButtonList.add(button);
            showCookieGbc.gridy++;
        });

        JScrollPane scrollPane = new JScrollPane(panel);
        dialog.add(scrollPane, BorderLayout.CENTER);

        //展示按钮组
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));

        CTTextButton downloadButton = new CTTextButton("下载");
        downloadButton.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.SMALL));
        downloadButton.addActionListener(e -> {
            if (ref.openedButtonKey.isEmpty()) {
                Log.err.print(getClass(), "请选择一个快速启动单元");
            } else {
                if (cookieInfoMap.get(ref.openedButtonKey).getDownloadUrl().isEmpty()) {
                    Log.err.print(getClass(), "该快速启动单元暂无下载地址");
                } else {

                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("选择下载目录");
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    fileChooser.setFileHidingEnabled(false); // 显示隐藏文件
                    fileChooser.setMultiSelectionEnabled(false); // 只允许选择一个文件
                    fileChooser.showOpenDialog(dialog);
                    File selectedFile = fileChooser.getSelectedFile();// 选择文件
                    new SwingWorker<>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            DownloadURLFile.downloadWebFile(dialog, null,
                                    cookieInfoMap.get(ref.openedButtonKey).getDownloadUrl(), selectedFile.getPath());
                            return null;
                        }
                    }.execute();
                }
            }
        });
        buttonPanel.add(downloadButton);

        CTTextButton showInfoButton = new CTTextButton("详细信息");
        showInfoButton.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.SMALL));
        showInfoButton.addActionListener(e -> {
            if (ref.openedButtonKey.isEmpty()) {
                Log.err.print(getClass(), "请选择一个快速启动单元");
            } else {
                JDialog infoDialog = new JDialog();
                infoDialog.setIconImage(GetIcon.getImageIcon(getClass().getResource("/image/light/about_0.png"), 300, 300).getImage());
                infoDialog.setSize(450, 300);
                infoDialog.setLocationRelativeTo(null);
                infoDialog.setModal(true);
                infoDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                infoDialog.setTitle("快速启动单元详细信息");

                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new GridBagLayout());
                JLabel nameLabel = new JLabel("快速启动单元名称:" + cookieInfoMap.get(ref.openedButtonKey).getName());
                nameLabel.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.SMALL));
                JLabel functionLabel = new JLabel("<html>快速启动单元功能:" + cookieInfoMap.get(ref.openedButtonKey).getFunction() + "</html>");
                functionLabel.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.SMALL));
                JLabel downloadUrlLabel = new JLabel("<html>快速启动单元下载地址:<br>" + cookieInfoMap.get(ref.openedButtonKey).getDownloadUrl() + "</html>");
                downloadUrlLabel.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.SMALL));

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.WEST;// 左对齐
                infoPanel.add(nameLabel, gbc);
                gbc.gridy = 1;
                infoPanel.add(functionLabel, gbc);
                gbc.gridy = 2;
                infoPanel.add(downloadUrlLabel, gbc);

                infoDialog.add(infoPanel);


                infoDialog.pack();
                infoDialog.setVisible(true);
            }
        });
        buttonPanel.add(showInfoButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initDialog(JDialog dialog) {
        dialog.setIconImage(GetIcon.getImageIcon(getClass().getResource("/image/input.png"), 300, 300).getImage());
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(null);
        //dialog.setResizable(false);
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setTitle("快速启动单元下载");
    }

    private void getInfo() throws Exception {

        Log.info.print("CookieDownload", "正在获取快速启动单元数据...");

        JSONObject jsonObject = new JSONObject(GetWebInf.getWebInf(apiUrl));

        //从仓库直接获取数据
        jsonObject.getJSONArray("assets").forEach(asset -> {
            JSONObject assetJson = (JSONObject) asset;

            String key = assetJson.getString("name");
            String browser_download_url = assetJson.getString("browser_download_url");
            /*if (cookieInfoMap.containsKey(key))
                cookieInfoMap.get(key).setDownloadUrl(browser_download_url);*/
            if (!key.equals("CookieInfo.json"))
                cookieInfoMap.put(key, new CookieInfo(key, "无", browser_download_url));
        });

        //从CookieInfo.json获取数据
        //1.获取文件信息
        AtomicReference<String> StrInfo = new AtomicReference<>();
        jsonObject.getJSONArray("assets").forEach(asset -> {
            JSONObject info = (JSONObject) asset;
            if (info.getString("name").equals("CookieInfo.json")) {
                try {
                    StrInfo.set(GetWebInf.getWebInf(info.getString("browser_download_url")));
                } catch (Exception e) {
                    Log.err.print(getClass(), "信息获取失败", e);
                }
            }

        });
        //2.获取数据
        JSONArray cookieInfo = new JSONArray(StrInfo.get());
        cookieInfo.forEach(info -> {
            JSONObject infoJson = (JSONObject) info;
            String key = infoJson.getString("key");
            String name = infoJson.getString("name");
            String function = infoJson.getString("function");
            if (cookieInfoMap.containsKey(key)) {//匹配正确的Cookie Key
                cookieInfoMap.get(key).setName(name);
                cookieInfoMap.get(key).setFunction(function);
            }
        });


        Log.info.print("CookieDownload", "快速启动单元数据获取完成");
        Log.info.print("CookieDownload", "快速启动单元数据:" + cookieInfoMap);
    }
}
