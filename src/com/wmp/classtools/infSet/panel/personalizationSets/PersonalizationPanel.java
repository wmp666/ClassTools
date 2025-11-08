package com.wmp.classTools.infSet.panel.personalizationSets;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTBorderFactory;
import com.wmp.classTools.CTComponent.CTComboBox;
import com.wmp.classTools.CTComponent.CTPanel.setsPanel.CTListSetsPanel;
import com.wmp.classTools.CTComponent.CTPanel.setsPanel.CTSetsPanel;
import com.wmp.classTools.CTComponent.CTTextField;
import com.wmp.classTools.frame.MainWindow;
import com.wmp.classTools.importPanel.finalPanel.FinalPanel;
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

public class PersonalizationPanel extends CTListSetsPanel {


    public PersonalizationPanel(String basicDataPath) {
        super(basicDataPath);

        this.setName("个性化");

        this.add(new PBasicSetsPanel(basicDataPath));
        this.add(new PAppFileSetsPanel(basicDataPath));
    }
}
