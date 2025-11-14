package com.wmp.classTools.importPanel.weatherInfo.settings;

import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTButton.CTTextButton;
import com.wmp.classTools.CTComponent.CTPanel.setsPanel.CTSetsPanel;
import com.wmp.classTools.CTComponent.CTTextField;
import com.wmp.classTools.frame.ShowHelpDoc;
import com.wmp.classTools.importPanel.weatherInfo.GetCityCode;
import com.wmp.classTools.importPanel.weatherInfo.WeatherInfoControl;

import javax.swing.*;
import java.awt.*;

public class WeatherInfoSetsPanel extends CTSetsPanel {

    private final CTTextField keyTextField = new CTTextField();
    private String cityCode = "360000";

    public WeatherInfoSetsPanel(String basicDataPath) {
        super(basicDataPath);
        setName("天气信息设置");

        this.setLayout(new BorderLayout());

        cityCode = WeatherInfoControl.getWeatherInfo();

        initUI();
    }

    private void initUI() {
        this.removeAll();

        JLabel title = new JLabel("天气信息设置");
        title.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG));
        title.setForeground(CTColor.textColor);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(title, BorderLayout.NORTH);

        JPanel centerSetsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        centerSetsPanel.setOpaque(false);

        {
            JLabel cityCodeLabel = new JLabel("城市编码: " + cityCode);
            cityCodeLabel.setForeground(CTColor.textColor);
            cityCodeLabel.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.NORMAL));


            CTTextButton cityCodeButton = new CTTextButton("按下选择城市");
            cityCodeButton.addActionListener(e -> {
                String tempStr = GetCityCode.getCityCode();
                if (tempStr == null) {
                    return;
                }
                this.cityCode = tempStr;
                cityCodeLabel.setText("城市编码: " + cityCode);
            });

            JPanel cityCodePanel = new JPanel();
            cityCodePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
            cityCodePanel.setOpaque(false);

            cityCodePanel.add(cityCodeLabel);
            cityCodePanel.add(cityCodeButton);

            centerSetsPanel.add(cityCodePanel);
        }

        {
            JLabel keyLabel = new JLabel("天气接口密钥: ");
            keyLabel.setForeground(CTColor.textColor);
            keyLabel.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.NORMAL));

            keyTextField.setText(WeatherInfoControl.getKey());
            keyTextField.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.NORMAL));
            keyTextField.setForeground(CTColor.textColor);

            CTTextButton helpButton = new CTTextButton("?", false);
            helpButton.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.NORMAL));
            helpButton.addActionListener(e -> {
                ShowHelpDoc.openWebHelpDoc("WTKeyCodeGetHelp.docx");
            });

            JPanel keyPanel = new JPanel();
            keyPanel.setLayout(new BorderLayout());
            keyPanel.setOpaque(false);
            keyPanel.add(keyLabel, BorderLayout.WEST);
            keyPanel.add(keyTextField, BorderLayout.CENTER);
            keyPanel.add(helpButton, BorderLayout.EAST);

            centerSetsPanel.add(keyPanel);
        }

        this.add(centerSetsPanel, BorderLayout.CENTER);
    }

    @Override
    public void save() throws Exception {
        Log.info.print("WISetsPanel", "保存天气信息设置");
        WeatherInfoControl.setWeatherInfo(this.cityCode, this.keyTextField.getText());
    }

    @Override
    public void refresh() throws Exception {
        cityCode = WeatherInfoControl.getWeatherInfo();
        initUI();
    }
}
