package com.wmp.classTools.infSet.panel.tools;

import com.wmp.PublicTools.OpenInExp;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTBorderFactory;
import com.wmp.classTools.CTComponent.CTTextButton;
import com.wmp.classTools.CTComponent.CTTextField;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class DataControlUnit extends JPanel {

    private String name = "name";
    private String path = "";
    private boolean canDelete = false;
    private List<DataControlUnit> specialChildPathList = new ArrayList<>();

    /**
     * 构造函数
     *
     * @param name              名字
     * @param path              路径
     * @param canDelete         是否可以删除
     * @param specialChildPaths 特殊子路径列表(有传递性),不省略父路径
     */
    public DataControlUnit(String name, String path, boolean canDelete, DataControlUnit... specialChildPaths) {

        this.name = name;
        this.path = path;
        this.canDelete = canDelete;
        this.specialChildPathList.addAll(List.of(specialChildPaths));

        this.setBorder(CTBorderFactory.createTitledBorder(name));
        this.setLayout(new BorderLayout(5, 5));

        //路径显示
        {
            JPanel pathPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridy = 0;
            gbc.gridx = 0;

            JLabel pathLabel = new JLabel("路径: ");
            CTTextField pathTextField = new CTTextField(path);
            pathTextField.setColumns(20);
            pathTextField.setEditable(false);
            CTTextButton openInExp = new CTTextButton("打开");
            openInExp.addActionListener(e -> {
                OpenInExp.open(path);
            });
            pathPanel.add(pathLabel, gbc);
            gbc.gridx++;
            pathPanel.add(pathTextField, gbc);
            gbc.gridx++;
            pathPanel.add(openInExp, gbc);

            this.add(pathPanel, BorderLayout.CENTER);
        }

        // 管理按钮
        {
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(1, 0, 10, 10));
            CTTextButton deleteButton = new CTTextButton("删除");
            deleteButton.addActionListener(e -> {
                try {
                    IOForInfo.deleteDirectoryRecursively(Path.of(path));
                } catch (IOException ex) {
                    Log.err.print("文件管理", "删除失败:\n" + ex.getMessage());
                    throw new RuntimeException(ex);
                }
            });
            deleteButton.setEnabled(canDelete);
            deleteButton.setForeground(Color.RED);

            CTTextButton childPathControlButton = new CTTextButton("子目录管理");
            File file = new File(path);
            if (file.exists() && file.isDirectory()) {
                childPathControlButton.addActionListener(e -> {
                    JDialog dialog = new JDialog();
                    dialog.setTitle("子目录管理");
                    dialog.setModal(true);
                    dialog.setLayout(new BorderLayout());


                    JPanel panel = new JPanel(new GridLayout(0, 1));

                    File[] files = file.listFiles();
                    TreeMap<String, DataControlUnit> unitMap = new TreeMap<>();
                    specialChildPathList.forEach(dataControlUnit -> {
                        unitMap.put(dataControlUnit.getPath(), dataControlUnit);
                    });
                    Set<String> keySet = unitMap.keySet();
                    for (File file1 : files) {
                        DataControlUnit dataControlUnit = new DataControlUnit(file1.getName(), file1.getAbsolutePath(), this.canDelete);
                        if (keySet.contains(file1.getAbsolutePath())) {
                            dataControlUnit = unitMap.get(file1.getAbsolutePath());
                            //System.err.println(file1.getAbsolutePath());
                        }

                        panel.add(dataControlUnit);
                    }

                    JScrollPane scrollPane = new JScrollPane(panel);
                    //设置滚轮灵活度
                    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
                    dialog.add(scrollPane, BorderLayout.CENTER);

                    dialog.pack();
                    dialog.setLocationRelativeTo(this);
                    dialog.setVisible(true);
                });
            } else childPathControlButton.setEnabled(false);

            buttonPanel.add(deleteButton);
            buttonPanel.add(childPathControlButton);

            this.add(buttonPanel, BorderLayout.SOUTH);
        }


    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public List<DataControlUnit> getSpecialChildPathList() {
        return specialChildPathList;
    }

    public void setSpecialChildPathList(List<DataControlUnit> specialChildPathList) {
        this.specialChildPathList = specialChildPathList;
    }
}


