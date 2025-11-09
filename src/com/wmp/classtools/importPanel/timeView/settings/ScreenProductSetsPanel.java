package com.wmp.classTools.importPanel.timeView.settings;

import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.io.GetPath;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTBorderFactory;
import com.wmp.classTools.CTComponent.CTButton.CTTextButton;
import com.wmp.classTools.CTComponent.CTComboBox;
import com.wmp.classTools.CTComponent.CTPanel.setsPanel.CTSetsPanel;
import com.wmp.classTools.CTComponent.CTTextField;
import com.wmp.classTools.frame.tools.screenProduct.SetsScrInfo;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

public class ScreenProductSetsPanel extends CTSetsPanel {

    private static final CTComboBox mainColorComboBox = new CTComboBox();
    private static final CTComboBox mainThemeComboBox = new CTComboBox();
    private static final CTTextField repaintTimerTextField = new CTTextField();
    private final File dataPath;

    public ScreenProductSetsPanel(String basicDataPath) throws IOException {
        super(basicDataPath);
        dataPath = new File(basicDataPath + "\\ScreenProduct");

        if (!dataPath.exists()) {
            dataPath.mkdirs();
        }

        setName("屏保设置");
        this.setLayout(new BorderLayout());

        try {
            initUI();
        } catch (IOException e) {
            Log.err.print(getClass(), "初始化失败", e);
        }

    }

    private static void initViewPanel(JSONObject jsonObject, JLabel viewLabel) {

        viewLabel.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.NORMAL));
        viewLabel.setForeground(CTColor.textColor);
        viewLabel.setText("");

        try {
            if (jsonObject.has("path")) {
                String path = jsonObject.getString("path");
                if (new File(path).exists()) {

                    if (new File(path).isFile()) {
                        ImageIcon icon = new ImageIcon(path);
                        do {
                            icon.setImage(icon.getImage().getScaledInstance(icon.getIconWidth() / 2, icon.getIconHeight() / 2, Image.SCALE_SMOOTH));
                        } while (icon.getIconWidth() >= 400);

                        viewLabel.setIcon(icon);
                    } else if (new File(path).isDirectory()) {
                        viewLabel.setIcon(null);
                        viewLabel.setText("包含多张图片,不支持预览");
                    }
                } else if (path.startsWith("BingBG")) {
                    ImageIcon icon = new ImageIcon(new URL(new SetsScrInfo().getBGImagePath(0)));
                    do {
                        icon.setImage(icon.getImage().getScaledInstance(icon.getIconWidth() / 2, icon.getIconHeight() / 2, Image.SCALE_SMOOTH));
                    } while (icon.getIconWidth() >= 400);

                    viewLabel.setIcon(icon);
                } else {
                    viewLabel.setIcon(null);
                    viewLabel.setText("请选择图片");
                }


            }
        } catch (Exception e) {
            Log.err.print(ScreenProductSetsPanel.class, "初始化失败", e);
        }

        viewLabel.revalidate();
        viewLabel.repaint();
    }

    private void initUI() throws IOException {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.gridx = 0;
        gbc.gridy = 0;

        File BGPath = new File(dataPath + "\\background.json");
        if (!BGPath.exists()) {
            try {
                FileWriter fileWriter = new FileWriter(BGPath);
                fileWriter.write("{}");
                fileWriter.close();
            } catch (IOException e) {
                Log.err.print(getClass(), "错误", e);
            }
        }

        JSONObject jsonObject = new JSONObject(new IOForInfo(BGPath).getInfos());

        JPanel bgPanel = new JPanel(new BorderLayout());
        bgPanel.setOpaque(false);
        bgPanel.setBorder(CTBorderFactory.createTitledBorder("背景设置"));

        //预览
        JLabel viewLabel = new JLabel();
        initViewPanel(jsonObject, viewLabel);
        viewLabel.setBorder(new EmptyBorder(10, 10, 10, 10));// 设置边框
        viewLabel.setAlignmentX(CENTER_ALIGNMENT);// 设置居中


        JScrollPane comp = new JScrollPane(viewLabel);
        comp.setOpaque(false);
        comp.getViewport().setOpaque(false);
        comp.setPreferredSize(new Dimension(300, 300));
        //设置灵敏度
        comp.getVerticalScrollBar().setUnitIncrement(10);
        bgPanel.add(comp, BorderLayout.CENTER);


        JPanel iconSetsPanel = new JPanel(new GridLayout(1, 2));
        iconSetsPanel.setOpaque(false);
        CTTextButton pathChoiceButton = new CTTextButton("选择图片");
        pathChoiceButton.addActionListener(e -> {

            String choose = Log.info.showChooseDialog(this, "设置背景", "请选择背景样式(位置)", "图片", "文件夹", "Bing壁纸", "Bing壁纸(随机)");
            if (choose == null) {
                return;
            }
            if (choose.equals("文件夹")) {
                String path = GetPath.getDirectoryPath(this, "请选择文件夹");

                if (path != null && !path.isEmpty()) {

                    String target = dataPath + "\\background\\";
                    try {
                        //通过遍历将文件拷贝到目标文件夹
                        File sourceFile = new File(path);

                        File targetFile = new File(target);
                        if (targetFile.exists()) {
                            Files.walkFileTree(targetFile.toPath(), new SimpleFileVisitor<Path>() {
                                // 先去遍历删除文件
                                @Override
                                public FileVisitResult visitFile(Path file,
                                                                 BasicFileAttributes attrs) {
                                    try {
                                        Files.delete(file);
                                    } catch (IOException ex) {
                                        Log.err.print(getClass(), "文件: " + file + "\n删除失败", ex);
                                    }
                                    return FileVisitResult.CONTINUE;
                                }

                                // 再去遍历删除目录
                                @Override
                                public FileVisitResult postVisitDirectory(Path dir,
                                                                          IOException exc) {
                                    try {
                                        Files.delete(dir);
                                    } catch (IOException ex) {
                                        Log.err.print(getClass(), "文件夹: " + dir + "\n删除失败", ex);
                                    }
                                    return FileVisitResult.CONTINUE;
                                }
                            });

                        }
                        targetFile.mkdirs();
                        if (!sourceFile.exists()) {
                            Log.err.print(getClass(), "文件夹不存在:" + path);
                            return;
                        }
                        for (File file : Objects.requireNonNull(sourceFile.listFiles())) {
                            if (file.isFile()) {
                                // 创建目标文件夹 StandardCopyOption.REPLACE_EXISTING -
                                Files.copy(file.toPath(), Paths.get(target, file.getName()), StandardCopyOption.REPLACE_EXISTING);
                            }
                        }
                        //Files.copy(Paths.get(path), Paths.get(target), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException ex) {
                        Log.err.print(getClass(), "文件夹复制失败:" + ex.getMessage());
                        throw new RuntimeException(ex);
                    }
                    jsonObject.put("path", target);
                }

            } else if (choose.equals("图片")) {
                String path = GetPath.getFilePath(this, "请选择图片", ".png|.jpg|.jpeg", "PNG|JPG");

                if (path != null && !path.isEmpty()) {
                    String[] split = path.split("\\.");

                    String target = dataPath + "\\background." + split[split.length - 1];
                    try {
                        Files.copy(Paths.get(path), Paths.get(target), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException ex) {
                        Log.err.print(getClass(), "图片复制失败", ex);
                    }
                    jsonObject.put("path", target);
                }


            } else if (choose.equals("Bing壁纸")) {
                jsonObject.put("path", "BingBG");
            } else if (choose.equals("Bing壁纸(随机)")) {
                jsonObject.put("path", "BingBGRandom");
            }
            IOForInfo ioForInfo = new IOForInfo(BGPath);
            try {
                ioForInfo.setInfo(jsonObject.toString());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }


            Log.info.message(this, "InfSetDialog", "已保存数据: " + jsonObject);

            initViewPanel(jsonObject, viewLabel);


        });
        iconSetsPanel.add(pathChoiceButton, gbc);

        //背景设置
        CTTextButton bgSetsButton = new CTTextButton("设置");
        bgSetsButton.addActionListener(e -> {

            JDialog dialog = new JDialog();
            dialog.setModal(true);
            dialog.setAlwaysOnTop(true);
            dialog.setTitle("背景设置");
            dialog.getContentPane().setBackground(CTColor.backColor);
            dialog.setLayout(new GridBagLayout());

            GridBagConstraints BGGbc = new GridBagConstraints();
            BGGbc.fill = GridBagConstraints.HORIZONTAL;
            BGGbc.weightx = 10;
            BGGbc.weighty = 10;
            BGGbc.gridx = 0;

            //清空背景
            CTTextButton clearButton = new CTTextButton("清空背景");
            clearButton.addActionListener(e1 -> {

                viewLabel.setIcon(null);
                viewLabel.setText("请选择图片");
                viewLabel.revalidate();
                viewLabel.repaint();

                jsonObject.put("path", "");

                IOForInfo ioForInfo = new IOForInfo(BGPath);
                try {
                    ioForInfo.setInfo(jsonObject.toString());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            BGGbc.gridy++;
            dialog.add(clearButton, BGGbc);
            //刷新间隔
            {
                JPanel repaintTimerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                repaintTimerPanel.setOpaque(false);

                JLabel repaintTimerLabel = new JLabel("刷新间隔(秒):");


                repaintTimerTextField.setColumns(10);


                repaintTimerPanel.add(repaintTimerLabel);
                repaintTimerPanel.add(repaintTimerTextField);

                BGGbc.gridy++;
                dialog.add(repaintTimerPanel, BGGbc);
            }

            dialog.setLocationRelativeTo(null);
            dialog.pack();
            dialog.setVisible(true);

        });
        iconSetsPanel.add(bgSetsButton, gbc);


        bgPanel.add(iconSetsPanel, BorderLayout.SOUTH);

        panel.add(bgPanel, gbc);

        JPanel ColorPanel = new JPanel();
        ColorPanel.setOpaque(false);
        ColorPanel.setLayout(new GridLayout(1, 2));
        ColorPanel.setBorder(CTBorderFactory.createTitledBorder("颜色设置"));
        //颜色设置
        {
            //主题色设置
            JPanel MainColorSets = new JPanel();

            {
                MainColorSets.setOpaque(false);
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

                MainThemeSets.setOpaque(false);
                MainThemeSets.setLayout(new FlowLayout(FlowLayout.LEFT));

                JLabel mainThemeLabel = new JLabel("主题:");
                mainThemeLabel.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.SMALL));


                mainThemeComboBox.setFont(CTFont.getCTFont(-1, CTFontSizeStyle.SMALL));


                //添加主题项目
                mainThemeComboBox.removeAllItems();
                mainThemeComboBox.addItems("深色", "浅色");

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
            int repaintTimer = setsScrInfo.getRepaintTimer();
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
                    default -> mainThemeComboBox.setSelectedItem("深色");
                }
            }
            if (repaintTimer > 0)
                repaintTimerTextField.setText(Integer.toString(repaintTimer));

        }


        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        //scrollPane.setBorder(BorderFactory.createEmptyBorder());

        this.add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void save() {

        JSONObject jsonObject = new SetsScrInfo().getJsonObject();

        //设置主题色
        String tempMainColor = switch (Objects.requireNonNull(mainColorComboBox.getSelectedItem()).toString()) {
            case "黑色" -> "black";
            case "蓝色" -> "blue";
            default -> "white";
        };
        jsonObject.put("mainColor", tempMainColor);

        //设置主题
        String tempMainThemeColor = switch (Objects.requireNonNull(mainThemeComboBox.getSelectedItem()).toString()) {
            case "深色" -> "dark";
            default -> "light";
        };
        jsonObject.put("mainTheme", tempMainThemeColor);

        jsonObject.put("repaintTimer", Integer.parseInt(repaintTimerTextField.getText()));

        IOForInfo ioForInfo = new IOForInfo(dataPath + "\\background.json");
        try {
            ioForInfo.setInfo(jsonObject.toString(4));
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
