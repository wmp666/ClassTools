package com.wmp.classTools.importPanel.timeView.settings;

import com.wmp.PublicTools.io.GetPath;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTSetsPanel;
import com.wmp.classTools.CTComponent.CTTextButton;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ScreenProductSetsPanel extends CTSetsPanel {

    private final File dataPath;

    public ScreenProductSetsPanel(String basicDataPath) throws IOException {
        super(basicDataPath);
        dataPath = new File(basicDataPath + "\\ScreenProduct");

        if (!dataPath.exists()) {
            dataPath.mkdirs();
        }

        setName("屏保设置");

        this.setLayout(new GridBagLayout());
        initUI();

    }

    private void initUI() throws IOException {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 0;


        File BGPath = new File(dataPath + "\\background.json");
        if (!BGPath.exists()) {
            try {
                FileWriter fileWriter = new FileWriter(BGPath);
                fileWriter.write("{}");
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        URL file = new URL("file://" + BGPath.getPath());
        System.out.println(file);
        JSONObject jsonObject = new JSONObject(new IOForInfo(BGPath).GetInfos());

        //预览


        JLabel viewLabel = new JLabel();
        if (jsonObject.has("path")) {
            ImageIcon icon = new ImageIcon(jsonObject.getString("path"));
            do {
                icon.setImage(icon.getImage().getScaledInstance(icon.getIconWidth() / 2, icon.getIconHeight() / 2, Image.SCALE_SMOOTH));
            } while (icon.getIconWidth() >= 400);

            viewLabel.setIcon(icon);
        }
        viewLabel.setBorder(new EmptyBorder(10, 10, 10, 10));// 设置边框
        viewLabel.setAlignmentX(CENTER_ALIGNMENT);// 设置居中
        gbc.gridx = 0;
        gbc.gridy = 0;

        JScrollPane comp = new JScrollPane(viewLabel);
        comp.setPreferredSize(new Dimension(300, 300));
        //设置灵敏度
        comp.getVerticalScrollBar().setUnitIncrement(10);
        this.add(comp, gbc);

        CTTextButton pathChoiceButton = new CTTextButton("选择图片");
        pathChoiceButton.addActionListener(e -> {
            String path = GetPath.getFilePath(this, "请选择图片", ".png|.jpg|jpeg", "PNG|JPG");

            if (path != null && !path.isEmpty()) {
                String[] split = path.split("\\.");

                String target = dataPath + "\\background." + split[split.length - 1];
                try {
                    Files.copy(Paths.get(path), Paths.get(target), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    Log.err.print("ScreenProductPanel-pathChoiceButton", "图片复制失败:" + ex.getMessage());
                    throw new RuntimeException(ex);
                }
                jsonObject.put("path", target);
            }

            IOForInfo ioForInfo = new IOForInfo(BGPath);
            try {
                ioForInfo.SetInfo(jsonObject.toString());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }


            Log.info.message(this, "InfSetDialog", "已保存数据: " + jsonObject);

            ImageIcon icon = new ImageIcon(jsonObject.getString("path"));
            do {
                icon.setImage(icon.getImage().getScaledInstance(icon.getIconWidth() / 2, icon.getIconHeight() / 2, Image.SCALE_SMOOTH));
            } while (icon.getIconWidth() >= 400);

            viewLabel.setIcon(icon);
            viewLabel.setText("");
            viewLabel.revalidate();
            viewLabel.repaint();


        });
        gbc.gridy++;
        this.add(pathChoiceButton, gbc);

        //清空背景
        CTTextButton clearButton = new CTTextButton("清空背景");
        clearButton.addActionListener(e -> {

            viewLabel.setIcon(null);
            viewLabel.setText("请选择图片");
            viewLabel.revalidate();
            viewLabel.repaint();

            IOForInfo ioForInfo = new IOForInfo(BGPath);
            try {
                ioForInfo.SetInfo("{}");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        gbc.gridy++;
        this.add(clearButton, gbc);
    }


    @Override
    public void save() {

    }

    @Override
    public void refresh() throws IOException {
        this.removeAll();
        initUI();
        this.revalidate();
        this.repaint();
    }
}
