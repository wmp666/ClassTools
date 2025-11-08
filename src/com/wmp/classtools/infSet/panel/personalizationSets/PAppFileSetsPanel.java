package com.wmp.classTools.infSet.panel.personalizationSets;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.UITools.IconControl;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.PublicTools.web.GetWebInf;
import com.wmp.classTools.CTComponent.CTBorderFactory;
import com.wmp.classTools.CTComponent.CTButton.CTTextButton;
import com.wmp.classTools.CTComponent.CTPanel.setsPanel.CTBasicSetsPanel;
import com.wmp.classTools.frame.ShowHelpDoc;
import com.wmp.classTools.infSet.panel.tools.DataControlUnit;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.util.concurrent.atomic.AtomicReference;

public class PAppFileSetsPanel extends CTBasicSetsPanel {
    public PAppFileSetsPanel(String basicDataPath) {
        super(basicDataPath);
        setName("文件设置");

        initUI();
    }

    private void initUI(){
        this.setBackground(CTColor.backColor);
        this.setLayout(new GridLayout(0, 1, 5, 5));

        JPanel imageSetsPanel = new JPanel();
        imageSetsPanel.setOpaque(false);
        imageSetsPanel.setBorder(CTBorderFactory.createTitledBorder("图片设置"));
        imageSetsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        CTTextButton downloadButton = new CTTextButton("下载图片", "导入", IconControl.COLOR_COLORFUL);
        downloadButton.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG));
        downloadButton.addActionListener(e->{
            try {
                JSONObject jsonObject = new JSONObject(
                        GetWebInf.getWebInf("https://api.github.com/repos/wmp666/ClassTools_Image/releases/latest" , false));
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
        helpButton.addActionListener(e->{
            ShowHelpDoc.openWebHelpDoc("imageCreativeHelp");
        });
        imageSetsPanel.add(helpButton);

        this.add(imageSetsPanel);

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
