package com.wmp.classTools.test.weatherTest;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetPositionTest {
    public static void getCity(String cityCode) {
        try {

            String key = "bfaf25fc5c695452951e7edb57ddcd49";

            // 构造请求URL (使用一个公开的免费天气接口示例)
            String nowCloud = String.format("https://restapi.amap.com/v3/config/district?key=%s&keywords=%s", key, cityCode);
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
                if (jsonResponse.getInt("status") == 1) {
                    JSONObject data = jsonResponse.getJSONArray("districts").getJSONObject(0)
                            .getJSONArray("districts").getJSONObject(0);
                    String adcode = data.getString("adcode");
                    String city = data.getString("name");
                    System.out.println("城市: " + city);
                    System.out.println("城市编码: " + adcode);
                }

            } else {
                System.out.println("HTTP请求失败，状态码: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        getCity("360000");
    }
}
