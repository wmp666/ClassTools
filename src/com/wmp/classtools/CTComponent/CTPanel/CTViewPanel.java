package com.wmp.classTools.CTComponent.CTPanel;

import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTPanel.setsPanel.CTSetsPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public abstract class CTViewPanel extends CTPanel {

    private List<CTSetsPanel> ctSetsPanelList = new ArrayList<>();

    private boolean isScreenProductViewPanel = false;
    private boolean independentRefresh = false;
    /**
     * 忽略组件状态(是否显示)
     */
    private boolean ignoreState = false;

    private final Timer refreshTimer = new Timer(2 * 1000, e -> {
        try {
            Refresh();
        } catch (Exception ex) {
            Log.err.print(getClass(), "刷新失败", ex);
        }
    });

    public CTViewPanel() {
        super();
    }

    public List<CTSetsPanel> getCtSetsPanelList() {
        return ctSetsPanelList;
    }

    public void setCtSetsPanelList(List<CTSetsPanel> ctSetsPanelList) {
        this.ctSetsPanelList = ctSetsPanelList;
    }

    public void toScreenProductViewPanel() {
        isScreenProductViewPanel = true;
    }

    public boolean isScreenProductViewPanel() {
        return isScreenProductViewPanel;
    }

    public boolean isIndependentRefresh() {
        return independentRefresh;
    }

    /**
     * 设置是否使用独立的刷新方式
     *
     * @param independentRefresh 是否使用独立刷新方式
     * @param delay              刷新间隔(ms)
     */
    public void setIndependentRefresh(boolean independentRefresh, int delay) {
        this.independentRefresh = independentRefresh;

        if (independentRefresh) {
            refreshTimer.setDelay(delay);
            refreshTimer.start();
        }
    }

    public boolean isIgnoreState() {
        return ignoreState;
    }

    public void setIgnoreState(boolean ignoreState) {
        this.ignoreState = ignoreState;
    }

    public void setRefreshTimerDelay(int delay) {
        refreshTimer.setDelay(delay);
    }

    /**
     * 用于集中管理刷新
     */
    @Override
    public void refresh() throws Exception {
        if (isVisible() || isIgnoreState()) {

            Log.info.print(getID(), "开始刷新");
            Refresh();
            if (independentRefresh) {

                refreshTimer.restart();
            }
        } else {
            Log.info.print(getID(), "刷新被禁止");
            refreshTimer.stop();
        }
    }

    /**
     * 刷新时做什么
     */
    protected abstract void Refresh() throws Exception;
}
