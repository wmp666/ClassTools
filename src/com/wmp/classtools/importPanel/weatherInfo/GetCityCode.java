package com.wmp.classTools.importPanel.weatherInfo;

import com.wmp.PublicTools.printLog.Log;
import com.wmp.PublicTools.web.GetWebInf;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetCityCode {
    public static String getCityCode(){
        try {
            Map<String, String> map = getCityCodes("100000");
            Map<String, String> oldMap = new HashMap<>();

            String s = Log.info.showChooseDialog(null, "请选择城市", "请选择城市", map.keySet().toArray(new String[0]));
            if (s.equals("NULL")) return null;
            while (true) {




                String oldCity = s;
                String oldCityCode = map.get(s);

                String info = String.format("当前选择:%s 对应编号:%s \n如果展示的内容为你需要的,就可以按下\"否\"/\"取消\"退出", oldCity, oldCityCode);

                map = getCityCodes(map.get(s));
                if (oldMap.equals(map) || map.isEmpty()) return oldMap.get(s);

                s = Log.info.showChooseDialog(null, "请选择城市", info, map.keySet().toArray(new String[0]));

                if (s.equals("NULL")) return oldCityCode;

                oldMap = map;
            }

        } catch (Exception e) {
            Log.err.print(GetCityCode.class, "获取城市编码失败",  e);
            throw new RuntimeException(e);
        }
    }

    private static Map<String, String> getCityCodes(String cityCode) throws Exception {
        Map<String, String> map = new HashMap<>();


        String nowCloud = String.format("https://restapi.amap.com/v3/config/district?key=%s&keywords=%s", WeatherInfoControl.getKey(), cityCode);

        // 解析JSON响应
        JSONObject jsonResponse = new JSONObject(GetWebInf.getWebInf(nowCloud));
        Log.info.print("GetCityCode", "城市数据:" + jsonResponse);
        if (jsonResponse.getInt("status") == 1) {
            JSONArray data = jsonResponse.getJSONArray("districts").getJSONObject(0)
                    .getJSONArray("districts");
            for (Object datum : data) {
                if (datum instanceof JSONObject jsonObject) {
                    map.put(jsonObject.getString("name"), jsonObject.getString("adcode"));
                }
            }
        }
        return map;
    }
}
