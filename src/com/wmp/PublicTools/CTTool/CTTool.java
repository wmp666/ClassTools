package com.wmp.PublicTools.CTTool;

import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTPanel.setsPanel.CTSetsPanel;

import javax.swing.*;
import java.util.Random;

public abstract class CTTool {
    private CTSetsPanel[] ctSetsPanelList = null;
    private String name = "CTTool";

    public CTTool() {
    }

    public CTTool(String name) {
        this.name = name;
    }

    public void showTool() {
        int id = new Random().nextInt();
        Log.info.loading.showDialog("工具" + name + id, "正在打开" + name + "工具");

        JDialog dialog = getDialog();
        dialog.setAlwaysOnTop(true);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setModal(true);

        Log.info.loading.closeDialog("工具" + name + id);
        dialog.setVisible(true);
    }



    public CTSetsPanel[] getCtSetsPanelList() {
        return ctSetsPanelList;
    }

    public void setCtSetsPanelList(CTSetsPanel... ctSetsPanelList) {
        this.ctSetsPanelList = ctSetsPanelList;
    }


    public abstract JDialog getDialog();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
