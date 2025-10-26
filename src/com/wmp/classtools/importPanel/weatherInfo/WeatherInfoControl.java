package com.wmp.classTools.importPanel.weatherInfo;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;

import java.io.IOException;

public class WeatherInfoControl {

    public static final String key = "bfaf25fc5c695452951e7edb57ddcd49";
    public static void setWeatherInfo(String cityCode) {
        IOForInfo io = new IOForInfo(CTInfo.DATA_PATH + "WeatherInfo\\cityCode.txt");
        try {
            io.setInfo(cityCode);
        } catch (IOException e) {
            Log.err.print(WeatherInfoControl.class, "天气信息保存失败", e);
        }
    }

    public static String getWeatherInfo() {
        IOForInfo io = new IOForInfo(CTInfo.DATA_PATH + "WeatherInfo\\cityCode.txt");
        try {
            String infos = io.getInfos();
            if (infos == null || infos.equals("err")) {
                io.setInfo("360100");
                return "360100";
            }
            return infos;
        } catch (IOException e) {
            Log.err.print(WeatherInfoControl.class, "天气信息获取失败", e);
        }
        return null;
    }

    /**
     * 天气信息
     * @param date 日期(yyyy-MM-dd)(2025-10-26)
     * @param daypower 白天-天气
     * @param daywind 白天-风向
     * @param daytemp 白天-温度
     * @param daypower 白天-风力
     * @param nightweather 夜间-天气
     * @param nightwind 夜间-风向
     * @param nighttemp 夜间-温度
     * @param nightpower 夜间-风力
     */
    public record ForecastsWeatherInfo(String date, String dayweather, String daywind, String daytemp, String daypower,
                                       String nightweather, String nightwind, String nighttemp, String nightpower) {
    }
}
