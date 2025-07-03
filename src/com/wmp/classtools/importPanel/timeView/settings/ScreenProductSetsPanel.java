package com.wmp.classTools.importPanel.timeView.settings;

import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.io.GetPath;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTComboBox;
import com.wmp.classTools.CTComponent.CTSetsPanel;
import com.wmp.classTools.CTComponent.CTTextButton;
import com.wmp.classTools.frame.tools.screenProduct.SetsScrInfo;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class ScreenProductSetsPanel extends CTSetsPanel {

    private final File dataPath;


    private final CTComboBox mainColorComboBox = new CTComboBox();
    private final CTComboBox mainThemeComboBox = new CTComboBox();

    public ScreenProductSetsPanel(String basicDataPath) throws IOException {
        super(basicDataPath);
        dataPath = new File(basicDataPath + "\\ScreenProduct");

        if (!dataPath.exists()) {
            dataPath.mkdirs();
        }

        setName("屏保设置");

        try {
            initUI();
        } catch (IOException e) {
            Log.err.print("ScreenProductPanel-initUI", "初始化失败:" + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private void initUI() throws IOException {
        JPanel panel = new JPanel(new GridBagLayout());

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
        panel.add(comp, gbc);


        JPanel iconSetsPanel = new JPanel(new GridLayout(1, 2));
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
        iconSetsPanel.add(pathChoiceButton, gbc);

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
        iconSetsPanel.add(clearButton, gbc);

        gbc.gridy++;
        panel.add(iconSetsPanel, gbc);


        JPanel ColorPanel = new JPanel();
        ColorPanel.setLayout(new GridLayout(1, 2));
        ColorPanel.setBorder(BorderFactory.createTitledBorder("颜色设置"));
        //颜色设置
        {
            //主题色设置
            JPanel MainColorSets = new JPanel();
            {

                MainColorSets.setLayout(new FlowLayout(FlowLayout.LEFT));

                JLabel mainColorLabel = new JLabel("主题色:");
                mainColorLabel.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.SMALL));


                mainColorComboBox.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.SMALL));

                //添加颜色项目
                mainColorComboBox.removeAllItems();
                mainColorComboBox.addItems("蓝色", "白色", "黑色");

                MainColorSets.add(mainColorLabel);
                MainColorSets.add(mainColorComboBox);
            }

            //主题设置
            JPanel MainThemeSets = new JPanel();
            {

                MainThemeSets.setLayout(new FlowLayout(FlowLayout.LEFT));

                JLabel mainThemeLabel = new JLabel("主题:");
                mainThemeLabel.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.SMALL));


                mainThemeComboBox.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.SMALL));


                //添加主题项目
                mainThemeComboBox.removeAllItems();
                mainThemeComboBox.addItems("浅色", "深色");

                MainThemeSets.add(mainThemeLabel);
                MainThemeSets.add(mainThemeComboBox);
            }

            ColorPanel.add(MainColorSets);
            ColorPanel.add(MainThemeSets);

        }
        gbc.gridy++;
        panel.add(ColorPanel, gbc);

        //数据显示
        {
            SetsScrInfo setsScrInfo = new SetsScrInfo();
            String mainColor = setsScrInfo.getMainColor();
            String mainTheme = setsScrInfo.getMainTheme();
            //主题色设置
            if (mainColor != null) {
                switch (mainColor) {
                    case "black" -> mainColorComboBox.setSelectedItem("黑色");
                    case "blue" -> mainColorComboBox.setSelectedItem("蓝色");
                    default -> mainColorComboBox.setSelectedItem("白色");
                }
            }
            //主题设置
            if (mainTheme != null) {
                switch (mainTheme) {
                    case "light" -> mainThemeComboBox.setSelectedItem("浅色");
                    default -> mainThemeComboBox.setSelectedItem("黑色");
                }
            }
        }


        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        this.add(scrollPane);
    }


    @Override
    public void save() {

        JSONObject jsonObject;
        try {
            jsonObject = new SetsScrInfo().getJsonObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //设置主题色
        String tempMainColor = switch (Objects.requireNonNull(mainColorComboBox.getSelectedItem()).toString()) {
            case "黑色" -> "black";
            case "白色" -> "white";
            default -> "blue";
        };
        jsonObject.put("mainColor", tempMainColor);

        //设置主题
        String tempMainThemeColor = switch (Objects.requireNonNull(mainThemeComboBox.getSelectedItem()).toString()) {
            case "深色" -> "dark";
            default -> "light";
        };
        jsonObject.put("mainTheme", tempMainThemeColor);

        IOForInfo ioForInfo = new IOForInfo(dataPath + "\\background.json");
        try {
            ioForInfo.SetInfo(jsonObject.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void refresh() throws IOException {
        this.removeAll();
        initUI();
        this.revalidate();
        this.repaint();
    }
}
