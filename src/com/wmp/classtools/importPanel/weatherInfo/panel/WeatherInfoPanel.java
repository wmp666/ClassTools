package com.wmp.classTools.importPanel.weatherInfo.panel;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.importPanel.weatherInfo.GetWeatherInfo;
import com.wmp.classTools.CTComponent.CTPanel.CTViewPanel;
import com.wmp.classTools.importPanel.weatherInfo.WeatherInfoControl;
import com.wmp.classTools.importPanel.weatherInfo.settings.WeatherInfoSetsPanel;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

public class WeatherInfoPanel extends CTViewPanel {

    private String format = "<html>%S 天气: %s, %s℃<br>%s %s℃-%s℃</html>";

    private final JLabel weather = new JLabel();

    private final Timer time = new Timer(60*1000, e -> {
        initWeather();
    });

    private void initWeather() {
        try {

            String cityCode = WeatherInfoControl.getWeatherInfo();

            JSONObject nowWeather = GetWeatherInfo.getNowWeather(cityCode);
            JSONObject todayOtherWeather = GetWeatherInfo.getTodayOtherWeather(cityCode);
            if (nowWeather == null || todayOtherWeather == null) {
                return;
            }
            weather.setText(String.format(format,
                    nowWeather.getString("city"),
                    nowWeather.getString("weather"),
                    nowWeather.getString("temperature"),
                    (todayOtherWeather.getString("dayweather").contains("雨") ||
                            todayOtherWeather.getString("nightweather").contains("雨"))?"有雨" : "无雨",
                    todayOtherWeather.getString("nighttemp"),
                    todayOtherWeather.getString("daytemp")
                    ));
        } catch (Exception ex) {
            Log.err.print(getClass(), "天气数据获取出错", ex);
        }
        weather.repaint();
    }

    private void initPanel(CTFontSizeStyle size){
        this.removeAll();

        if (this.isScreenProductViewPanel()) {
            format = "<html>%S 天气: %s, %s℃ %s %s℃-%s℃</html>";
        }else {
            format = "<html>%S 天气: %s, %s℃<br>%s %s℃-%s℃</html>";
        }

        weather.setFont(CTFont.getCTFont(Font.BOLD, size));
        weather.setForeground(CTColor.mainColor);
        this.add(weather, BorderLayout.SOUTH);

    }

    public WeatherInfoPanel(){
        super();
        this.setID("WeatherInfoPanel");
        this.setName("天气详情");
        this.setLayout(new BorderLayout());
        this.setCtSetsPanelList(java.util.List.of(new WeatherInfoSetsPanel(CTInfo.DATA_PATH)));

        initPanel(CTFontSizeStyle.BIG);

        initWeather();

        time.setRepeats(true);
        time.start();

    }

    @Override
    public void refresh() throws Exception {
        if (!this.isScreenProductViewPanel()) {
            initPanel(CTFontSizeStyle.BIG);
        }else{
            initPanel(CTFontSizeStyle.MORE_BIG);
        }
        initWeather();
        this.repaint();
    }
}
