package com.wmp.classTools.importPanel.timeView;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.appFileControl.IconControl;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTButton.CTIconButton;
import com.wmp.classTools.CTComponent.CTOptionPane;
import com.wmp.classTools.CTComponent.CTPanel.CTViewPanel;
import com.wmp.classTools.importPanel.timeView.settings.ScreenProductSetsPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TimeViewPanel extends CTViewPanel {

    private final JLabel timeView = new JLabel();

    public TimeViewPanel() throws IOException {

        this.setName("时间显示组件");
        this.setID("TimeViewPanel");
        this.setLayout(new BorderLayout());
        this.setCtSetsPanelList(List.of(new ScreenProductSetsPanel(CTInfo.DATA_PATH)));

        this.setIndependentRefresh(true, 34);

    }

    @Override
    protected void Refresh() throws Exception {
        this.removeAll();

        //获取时间
        Date date = new Date();
        //格式化 11.22 23:05
        DateFormat dateFormat = new SimpleDateFormat("MM.dd HH:mm:ss");


        timeView.setHorizontalAlignment(JLabel.CENTER);
        timeView.setText(dateFormat.format(date));
        timeView.setFont(CTFont.getCTFont(Font.BOLD, isScreenProductViewPanel() ? CTFontSizeStyle.BIG_BIG : CTFontSizeStyle.BIG));
        //timeView.setBackground(new Color(0x0ECECED, true));
        timeView.setForeground(CTColor.mainColor);
        this.add(timeView, BorderLayout.CENTER);

        this.revalidate();
        this.repaint();

    }

}
