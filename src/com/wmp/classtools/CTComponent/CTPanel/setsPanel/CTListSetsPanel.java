package com.wmp.classTools.CTComponent.CTPanel.setsPanel;

import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTList;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CTListSetsPanel extends CTSetsPanel{

    private final List<CTSetsPanel> ctSetsPanelList = new ArrayList<>();

    public CTListSetsPanel(String basicDataPath) {
        super(basicDataPath);
        this.setName("CTSetsPanel");
        this.setLayout(new BorderLayout());


    }

    public List<CTSetsPanel> getCtSetsPanelList() {
        return ctSetsPanelList;
    }

    public void add(CTSetsPanel ctSetsPanel) {
        ctSetsPanelList.add(ctSetsPanel);
        init();
    }

    public void clearCTList() {
        ctSetsPanelList.clear();
    }

    private void init() {
    this.removeAll();

    String[] choices = new String[ctSetsPanelList.size()];
    for (int i = 0; i < choices.length; i++) {
        choices[i] = ctSetsPanelList.get(i).getName();
    }

    // 当前显示的面板引用
    AtomicReference<CTSetsPanel> currentPanel = new AtomicReference<>();

    // 默认显示第一个面板（如果存在）
    if (!ctSetsPanelList.isEmpty()) {
        currentPanel.set(ctSetsPanelList.get(0));
    } else {
        // 如果没有面板，创建一个空面板
        currentPanel.set(new CTSetsPanel("") {
            @Override
            public void save() throws Exception {}
            @Override
            public void refresh() throws Exception {}
        });
    }

    // 创建包含当前面板的滚动面板
    JScrollPane contentScrollPane = new JScrollPane(currentPanel.get());
    contentScrollPane.setBorder(null);
    contentScrollPane.setOpaque(false);
    contentScrollPane.getViewport().setOpaque(false);
    this.add(contentScrollPane, BorderLayout.CENTER);

    // 创建选择列表
    CTList ctList = new CTList();
    ctList.resetChoices(choices);
    ctList.setDirect(1);
    ctList.setListener((e, choice) -> {
        Log.info.print(getName(), "选择:" + choice);

        // 移除旧的内容面板
        this.removeAll();

        // 查找并设置新面板
        for (CTSetsPanel ctSetsPanel : ctSetsPanelList) {
            if (ctSetsPanel.getName().equals(choice)) {
                Log.info.print(getName(), "显示:" + ctSetsPanel.getName());
                try {
                    ctSetsPanel.refresh();
                } catch (Exception ex) {
                    Log.err.print(this, getClass(), "刷新失败", ex);
                }
                currentPanel.set(ctSetsPanel);
                break;
            }
        }

        // 添加新的内容面板
        JScrollPane newScrollPane = new JScrollPane(currentPanel.get());
        newScrollPane.setBorder(null);
        newScrollPane.setOpaque(false);
        newScrollPane.getViewport().setOpaque(false);
        this.add(newScrollPane, BorderLayout.CENTER);

        // 添加选择列表到北部
        JScrollPane listScrollPane = new JScrollPane(ctList);
        listScrollPane.setBorder(null);
        listScrollPane.setOpaque(false);
        listScrollPane.getViewport().setOpaque(false);
        this.add(listScrollPane, BorderLayout.NORTH);

        // 重新布局
        this.revalidate();
        this.repaint();
    });

    // 添加选择列表到北部
    JScrollPane listScrollPane = new JScrollPane(ctList);
    listScrollPane.setBorder(null);
    listScrollPane.setOpaque(false);
    listScrollPane.getViewport().setOpaque(false);
    this.add(listScrollPane, BorderLayout.NORTH);
}


    @Override
    public void save() throws Exception {
        ctSetsPanelList.forEach(ctSetsPanel -> {
            try {
                ctSetsPanel.save();
            } catch (Exception e) {
                Log.err.print(this, getClass(), "保存失败", e);
            }
        });
    }

    @Override
    public void refresh() throws Exception {
        this.removeAll();

        init();

        this.revalidate();
        this.repaint();
    }
}
