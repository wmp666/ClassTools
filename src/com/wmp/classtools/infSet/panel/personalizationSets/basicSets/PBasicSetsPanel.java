package com.wmp.classTools.infSet.panel.personalizationSets.basicSets;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.*;
import com.wmp.classTools.CTComponent.CTPanel.setsPanel.CTBasicSetsPanel;
import com.wmp.classTools.CTComponent.CTPanel.setsPanel.CTListSetsPanel;
import com.wmp.classTools.frame.MainWindow;
import com.wmp.classTools.importPanel.finalPanel.FinalPanel;
import com.wmp.classTools.infSet.panel.personalizationSets.PersonalizationPanel;
import com.wmp.classTools.infSet.tools.SetStartUp;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.TreeMap;

public class PBasicSetsPanel extends CTListSetsPanel {

    public PBasicSetsPanel(String basicDataPath) {
        super(basicDataPath);

        setName("基础设置");

        this.clearCTList();
        this.add(new PBBasicSetsPanel(CTInfo.DATA_PATH));
        this.add(new PBPanelSetsPanel(CTInfo.DATA_PATH));
    }
}
