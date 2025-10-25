package com.wmp.classTools.test.weatherTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class SimpleWeatherService {

    public static void getWeatherByCity(String cityCode) {
        try {

            String key = "bfaf25fc5c695452951e7edb57ddcd49";

            // 构造请求URL (使用一个公开的免费天气接口示例)
            String nowCloud = String.format("https://restapi.amap.com/v3/weather/weatherInfo?key=%s&city=%s", key, cityCode);
            URL url = new URL(nowCloud);

            // 建立连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // 获取响应
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // 解析JSON响应
                JSONObject jsonResponse = new JSONObject(response.toString());
                System.out.println(jsonResponse);
                if (jsonResponse.getInt("status") == 1) { // 假设1000为成功状态码
                    JSONObject data = jsonResponse.getJSONArray("lives").getJSONObject(0);
                    String weather = data.getString("weather");
                    String temperature = data.getString("temperature");

                    System.out.println("天气: " + weather);
                    System.out.println("温度: " + temperature + "℃");
                } else {
                    System.err.println("获取天气失败: " + jsonResponse);
                }
            } else {
                System.out.println("HTTP请求失败，状态码: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        getWeatherByCity("110000");
    }
}