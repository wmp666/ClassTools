package com.wmp.classTools.frame.tools.cookie;

import com.wmp.Main;
import com.wmp.classTools.printLog.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.TreeMap;

public class GetCookie {

    private final String CookiePath = Main.DATA_PATH + "\\Cookie\\";
    // pin -> name
    //private final TreeMap<String, String> nameList = new TreeMap<>();
    // pin -> 文件位置
    private final TreeMap<String, Cookie> cookieMap = new TreeMap<>();
    private final TreeMap<String, Cookie> initCookieMap = new TreeMap<>();



    public GetCookie() throws IOException {
        File basic = new File(CookiePath);
        if (!basic.exists()) {
            basic.mkdirs();
        }

        File[] cookies = basic.listFiles();
        if (cookies != null) {
            //循环判断每一个文件夹
            for (File cookieSetsFile : cookies) {
                if (cookieSetsFile.isDirectory()) {
                    //获取相关数据
                    File cookieSets = new File(cookieSetsFile + "\\setUp.json");
                    if (cookieSets.exists()) {
                        //读取文件
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(
                                        new FileInputStream(cookieSets), StandardCharsets.UTF_8));
                        StringBuilder s = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            s.append(line);
                        }
                        reader.close();
                        JSONObject JSONCookieSets;
                        try {
                            JSONCookieSets = new JSONObject(s.toString());
                        } catch (JSONException e) {
                            Log.error.print(null, "获取插件数据", cookieSets.getParent() + "setUp.json数据出错\n" + e.getMessage());
                            continue;
                        }


                        if (JSONCookieSets.has("pin") && JSONCookieSets.has("run")) {

                            String exec = JSONCookieSets.getString("run");

                            Cookie cookie = new Cookie("null", "other", null, exec, cookieSetsFile);


                            if (JSONCookieSets.has("style")){
                                cookie.setStyle(JSONCookieSets.getString("style"));
                            }
                            if (JSONCookieSets.has("parameters")) {
                                cookie.setParameters(JSONCookieSets.getJSONArray("parameters").toList());
                            }
                            if (JSONCookieSets.has("name")){
                                cookie.setName(JSONCookieSets.getString("name"));
                            }
                            if (JSONCookieSets.has("icon")){


                                cookie.setIcon(JSONCookieSets.getString("icon"));


                            }

                            cookieMap.put(JSONCookieSets.getString("pin"), cookie);
                        }


                    }

                }
            }
            Log.info.print("GetCookie", "Cookie加载完毕");
            Log.info.print("GetCookie", "CookieMap:" + cookieMap);
            //System.out.println(cookieMap);
        }

    }

    public String getName(String key) {
        return cookieMap.get(key).getName();
    }

    public TreeMap<String, Cookie> getCookieMap() {
        return cookieMap;
    }

}
