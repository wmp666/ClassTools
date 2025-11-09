package com.wmp.classTools.importPanel.weatherInfo.panel;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.CTComponent.CTBorderFactory;
import com.wmp.classTools.CTComponent.CTPanel.CTViewPanel;
import com.wmp.classTools.CTComponent.CTTable;
import com.wmp.classTools.frame.ShowHelpDoc;
import com.wmp.classTools.importPanel.weatherInfo.GetWeatherInfo;
import com.wmp.classTools.importPanel.weatherInfo.WeatherInfoControl;
import com.wmp.classTools.importPanel.weatherInfo.settings.WeatherInfoSetsPanel;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class WeatherInfoPanel extends CTViewPanel {

    private final JLabel weather = new JLabel();
    private String format = "<html>%s %s, %s℃<br>%s %s℃-%s℃</html>";
    private String cityCode = WeatherInfoControl.getWeatherInfo();

    public WeatherInfoPanel() {
        super();
        this.setID("WeatherInfoPanel");
        this.setName("天气详情");
        this.setLayout(new BorderLayout());
        this.setCtSetsPanelList(java.util.List.of(new WeatherInfoSetsPanel(CTInfo.DATA_PATH)));

        initPanel();
        resetPanel(CTFontSizeStyle.BIG);

    }

    private void initWeather() {
        try {

            cityCode = WeatherInfoControl.getWeatherInfo();

            JSONObject nowWeather = GetWeatherInfo.getNowWeather(cityCode);
            JSONArray weatherForecasts = GetWeatherInfo.getWeatherForecasts(cityCode);
            JSONObject todayOtherWeather = weatherForecasts.getJSONObject(0);
            if (nowWeather == null || todayOtherWeather == null) {
                weather.setText(String.format("<html>获取天气数据失败<br>%s<br>点击查看详情</html>", GetWeatherInfo.errCode));
                return;
            }
            weather.setText(String.format(format,
                    nowWeather.getString("city"),
                    nowWeather.getString("weather"),
                    nowWeather.getString("temperature"),
                    (todayOtherWeather.getString("dayweather").contains("雨") ||
                            todayOtherWeather.getString("nightweather").contains("雨")) ? "有雨" : "无雨",
                    todayOtherWeather.getString("nighttemp"),
                    todayOtherWeather.getString("daytemp")
            ));
            weather.setCursor(new Cursor(Cursor.HAND_CURSOR));
            StringBuilder sb = new StringBuilder();
            sb.append("今日天气: ")
                    .append(nowWeather.getString("weather")).append(" ")
                    .append(nowWeather.getString("temperature")).append("℃")
                    .append("\n");
            for (Object weatherForecast : weatherForecasts) {
                if (weatherForecast instanceof JSONObject weatherForecastObject) {
                    WeatherInfoControl.ForecastsWeatherInfo info = new WeatherInfoControl.ForecastsWeatherInfo(
                            weatherForecastObject.getString("date"),
                            weatherForecastObject.getString("dayweather"),
                            weatherForecastObject.getString("daywind"),
                            weatherForecastObject.getString("daytemp"),
                            weatherForecastObject.getString("daypower"),
                            weatherForecastObject.getString("nightweather"),
                            weatherForecastObject.getString("nightwind"),
                            weatherForecastObject.getString("nighttemp"),
                            weatherForecastObject.getString("nightpower")
                    );
                    sb.append(info.date().substring(5)).append(" ")
                            .append(" ").append(info.dayweather())
                            .append("->").append(info.nightweather())
                            .append(" ").append(info.nighttemp() + "-"
                                    + info.daytemp() + "℃").append("\n");

                }
            }
            Log.info.adaptedMessage(nowWeather.getString("city") + " 天气数据", sb.toString(), 60, 5);
        } catch (Exception ex) {
            Log.warn.print(getClass().toString(), "天气数据获取出错" + ex);
            throw new RuntimeException(ex);
        }
        weather.repaint();
    }

    private void resetPanel(CTFontSizeStyle size) {
        this.removeAll();

        if (this.isScreenProductViewPanel()) {
            format = "<html>%S 天气: %s, %s℃ %s %s℃-%s℃</html>";
        } else {
            format = "<html>%S 天气: %s, %s℃<br>%s %s℃-%s℃</html>";
        }

        weather.setFont(CTFont.getCTFont(Font.BOLD, size));
        weather.setForeground(CTColor.mainColor);
        this.add(weather, BorderLayout.SOUTH);

    }

    private void initPanel() {
        weather.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    ArrayList<WeatherInfoControl.ForecastsWeatherInfo> forecastsWeatherInfoList = new ArrayList<>();

                    JSONArray weatherForecasts = GetWeatherInfo.getWeatherForecasts(WeatherInfoPanel.this.cityCode);
                    if (weatherForecasts == null) {
                        ShowHelpDoc.openWebHelpDoc("WIErrCode.md");
                        return;
                    }
                    weatherForecasts.forEach(weatherForecast -> {
                        if (weatherForecast instanceof JSONObject weatherForecastObject) {
                            WeatherInfoControl.ForecastsWeatherInfo info = new WeatherInfoControl.ForecastsWeatherInfo(
                                    weatherForecastObject.getString("date"),
                                    weatherForecastObject.getString("dayweather"),
                                    weatherForecastObject.getString("daywind"),
                                    weatherForecastObject.getString("daytemp"),
                                    weatherForecastObject.getString("daypower"),
                                    weatherForecastObject.getString("nightweather"),
                                    weatherForecastObject.getString("nightwind"),
                                    weatherForecastObject.getString("nighttemp"),
                                    weatherForecastObject.getString("nightpower")
                            );
                            forecastsWeatherInfoList.add(info);

                        }
                    });

                    JDialog dialog = new JDialog();
                    dialog.setTitle("天气详情");
                    dialog.setModal(true);
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setLayout(new BorderLayout());
                    dialog.setAlwaysOnTop(true);

                    JLabel titleLabel = new JLabel("天气详情");
                    titleLabel.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.BIG));
                    dialog.add(titleLabel, BorderLayout.NORTH);

                    //生成天气表(二维数组)
                    String[][] dayWeatherTable = new String[forecastsWeatherInfoList.size()][4];
                    for (int i = 0; i < forecastsWeatherInfoList.size(); i++) {
                        dayWeatherTable[i][0] = forecastsWeatherInfoList.get(i).date().substring(5);
                        dayWeatherTable[i][1] = forecastsWeatherInfoList.get(i).dayweather();
                        dayWeatherTable[i][2] = forecastsWeatherInfoList.get(i).daywind();
                        dayWeatherTable[i][3] = forecastsWeatherInfoList.get(i).daypower();
                    }
                    String[][] nightWeatherTable = new String[forecastsWeatherInfoList.size()][5];
                    for (int i = 0; i < forecastsWeatherInfoList.size(); i++) {
                        nightWeatherTable[i][0] = forecastsWeatherInfoList.get(i).date().substring(5);
                        nightWeatherTable[i][1] = forecastsWeatherInfoList.get(i).nightweather();
                        nightWeatherTable[i][2] = forecastsWeatherInfoList.get(i).nightwind();
                        nightWeatherTable[i][3] = forecastsWeatherInfoList.get(i).nightpower();
                        nightWeatherTable[i][4] = forecastsWeatherInfoList.get(i).nighttemp() + "-"
                                + forecastsWeatherInfoList.get(i).daytemp() + "℃";

                    }

                    CTTable dayTable = new CTTable(dayWeatherTable, new String[]{"日期", "天气", "风向", "风力"}) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    };
                    dayTable.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.NORMAL));
                    dayTable.setRowHeight(CTFont.getSize(CTFontSizeStyle.NORMAL));
                    dayTable.getTableHeader().setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.BIG));

                    JScrollPane dayScrollPane = new JScrollPane(dayTable);
                    dayScrollPane.setOpaque(false);
                    dayScrollPane.getViewport().setOpaque(false);
                    dayScrollPane.setBorder(CTBorderFactory.createTitledBorder("白天"));

                    CTTable nightTable = new CTTable(nightWeatherTable, new String[]{"日期", "天气", "风向", "风力", "温差"}) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    };
                    nightTable.setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.NORMAL));
                    nightTable.setRowHeight(CTFont.getSize(CTFontSizeStyle.NORMAL));
                    nightTable.getTableHeader().setFont(CTFont.getCTFont(Font.PLAIN, CTFontSizeStyle.BIG));

                    JScrollPane nightScrollPane = new JScrollPane(nightTable);
                    nightScrollPane.setOpaque(false);
                    nightScrollPane.getViewport().setOpaque(false);
                    nightScrollPane.setBorder(CTBorderFactory.createTitledBorder("晚上"));

                    JPanel panel = new JPanel();
                    panel.setOpaque(false);
                    panel.setLayout(new GridLayout(0, 1));
                    panel.add(dayScrollPane);
                    panel.add(nightScrollPane);

                    dialog.add(panel, BorderLayout.CENTER);

                    dialog.setSize(dialog.getPreferredSize().width, (int) (500 * CTInfo.dpi));
                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);
                } catch (Exception ex) {
                    Log.err.print(getClass(), "天气数据获取出错", ex);
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    @Override
    protected void Refresh() throws Exception {
        if (!this.isScreenProductViewPanel()) {
            resetPanel(CTFontSizeStyle.BIG);
        } else {
            resetPanel(CTFontSizeStyle.MORE_BIG);
        }
        initWeather();
        this.repaint();
    }
}
