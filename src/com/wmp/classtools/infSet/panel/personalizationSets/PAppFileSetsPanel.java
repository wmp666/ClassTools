package com.wmp.classTools.infSet.panel.personalizationSets;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.appFileControl.IconControl;
import com.wmp.PublicTools.appFileControl.MusicControl;
import com.wmp.PublicTools.io.GetPath;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.PublicTools.web.GetWebInf;
import com.wmp.classTools.CTComponent.CTBorderFactory;
import com.wmp.classTools.CTComponent.CTButton.CTTextButton;
import com.wmp.classTools.CTComponent.CTPanel.setsPanel.CTBasicSetsPanel;
import com.wmp.classTools.frame.ShowHelpDoc;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicReference;

public class PAppFileSetsPanel extends CTBasicSetsPanel {
    public PAppFileSetsPanel(String basicDataPath) {
        super(basicDataPath);
        setName("文件设置");

        initUI();
    }

    private void initUI() {
        this.setBackground(CTColor.backColor);
        this.setLayout(new GridLayout(0, 1, 5, 5));

        initImageSetsPanel();

        initMusicSetsPanel();

        initParentDataPathSetsPanel();
    }

    private void initParentDataPathSetsPanel() {
        JPanel dataPathSetsPanel = new JPanel();
        dataPathSetsPanel.setOpaque(false);
        dataPathSetsPanel.setBorder(CTBorderFactory.createTitledBorder("数据父路径设置"));
        dataPathSetsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel dataPathLabel = new JLabel("数据父路径：");
        {
            String path = System.getenv("LOCALAPPDATA");
            if (path != null && !path.isEmpty()){
                File file = new File(path, "\\ClassTools\\basicDataPath.txt");
                if (file.exists() && file.isFile()) {
                    try {
                        path = new File(Files.readString(file.toPath(), StandardCharsets.UTF_8)).getAbsolutePath();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            dataPathLabel.setText("<html>数据父路径：<br>" + path + "</html>");
        }
        dataPathLabel.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG));
        dataPathLabel.setForeground(CTColor.textColor);
        dataPathSetsPanel.add(dataPathLabel);

        CTTextButton dataPathButton = new CTTextButton("修改");
        dataPathButton.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG));
        dataPathButton.addActionListener(e -> {

            String path = GetPath.getDirectoryPath(this, "选择数据父路径");
            //加载基础目录
            String localPath = System.getenv("LOCALAPPDATA");
            if (localPath != null && !localPath.isEmpty()){
                File file = new File(localPath, "\\ClassTools\\basicDataPath.txt");
                if (path != null) {
                    try {
                        Files.writeString(file.toPath(), path, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                        dataPathLabel.setText("<html>数据父路径：<br>" + localPath + "</html>");
                    } catch (IOException ex) {
                        Log.err.print(getClass(), "数据父路径保存失败", ex);
                        throw new RuntimeException(ex);
                    }
                }
            }

        });
        dataPathSetsPanel.add(dataPathButton);

        this.add(dataPathSetsPanel);
    }

    private void initImageSetsPanel() {
        JPanel imageSetsPanel = new JPanel();
        imageSetsPanel.setOpaque(false);
        imageSetsPanel.setBorder(CTBorderFactory.createTitledBorder("图片设置"));
        imageSetsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        CTTextButton downloadButton = new CTTextButton("下载音频");
        downloadButton.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG));
        downloadButton.setIcon("导入", IconControl.COLOR_COLORFUL,
                downloadButton.getFont().getSize(), downloadButton.getFont().getSize());
        downloadButton.addActionListener(e -> {
            try {
                JSONObject jsonObject = new JSONObject(
                        GetWebInf.getWebInf("https://api.github.com/repos/wmp666/ClassTools_Image/releases/latest", false));
                AtomicReference<String> downloadURL = new AtomicReference<>("");
                AtomicReference<String> version = new AtomicReference<>("");
                //判断是否存在
                version.set(jsonObject.getString("tag_name"));
                jsonObject.getJSONArray("assets").forEach(object -> {
                    JSONObject asset = (JSONObject) object;
                    if (asset.getString("name").equals("image.zip")) {
                        downloadURL.set(asset.getString("browser_download_url"));
                    }
                });

                IconControl.downloadFile(downloadURL, version);
            } catch (Exception ex) {
                Log.err.print(getClass(), "图片下载失败", ex);
            }
        });
        imageSetsPanel.add(downloadButton);

        CTTextButton helpButton = new CTTextButton("使用帮助");
        helpButton.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG));
        helpButton.addActionListener(e -> {
            ShowHelpDoc.openWebHelpDoc("imageCreativeHelp");
        });
        imageSetsPanel.add(helpButton);

        this.add(imageSetsPanel);
    }

    private void initMusicSetsPanel() {
        JPanel musicSetsPanel = new JPanel();
        musicSetsPanel.setOpaque(false);
        musicSetsPanel.setBorder(CTBorderFactory.createTitledBorder("音频设置"));
        musicSetsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        CTTextButton downloadButton = new CTTextButton("下载音频");
        downloadButton.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG));
        downloadButton.setIcon("导入", IconControl.COLOR_COLORFUL,
                downloadButton.getFont().getSize(), downloadButton.getFont().getSize());
        downloadButton.addActionListener(e -> {
            try {
                JSONObject jsonObject = new JSONObject(
                        GetWebInf.getWebInf("https://api.github.com/repos/wmp666/ClassTools_Music/releases/latest", false));
                AtomicReference<String> downloadURL = new AtomicReference<>("");
                AtomicReference<String> version = new AtomicReference<>("");
                //判断是否存在
                version.set(jsonObject.getString("tag_name"));
                jsonObject.getJSONArray("assets").forEach(object -> {
                    JSONObject asset = (JSONObject) object;
                    if (asset.getString("name").equals("music.zip")) {
                        downloadURL.set(asset.getString("browser_download_url"));
                    }
                });

                MusicControl.downloadFile(downloadURL, version);
            } catch (Exception ex) {
                Log.err.print(getClass(), "音频下载失败", ex);
            }
        });
        musicSetsPanel.add(downloadButton);

        CTTextButton helpButton = new CTTextButton("使用帮助");
        helpButton.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG));
        helpButton.addActionListener(e -> {
            ShowHelpDoc.openWebHelpDoc("musicCreativeHelp");
        });
        musicSetsPanel.add(helpButton);

        this.add(musicSetsPanel);
    }

    @Override
    public void save() throws Exception {

    }

    @Override
    public void refresh() throws Exception {
        this.removeAll();
        initUI();
        this.revalidate();
        this.repaint();
    }
}
