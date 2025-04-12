package com.wmp.classTools.frame.tools.cookie;

import com.wmp.Main;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.TreeMap;

public class GetCookie {

    private static final String CookiePath = Main.DataPath + "\\Cookie\\";
    private TreeMap<String, String> nameList = new TreeMap<>();
    // 插件名 -> 文件位置
    private static TreeMap<String, File> cookieMap = new TreeMap<>();

    public GetCookie() throws IOException {
        File basic = new File(CookiePath);
        if (!basic.exists()) {
            basic.mkdirs();
        }

        File[] cookies = basic.listFiles();
        if (cookies != null) {
            //循环判断每一个文件夹
            for (File cookie : cookies) {
                System.out.println(cookie);
                if (cookie.isDirectory()) {
                    //获取相关数据
                    File cookieSets = new File(cookie + "\\setUp.json");
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
                        JSONObject JSONCookieSets = new JSONObject(s.toString());

                        if (JSONCookieSets.has("pin") && JSONCookieSets.has("run")) {

                            String exec = JSONCookieSets.getString("run");
                            exec = exec.replace("%CookiePath", cookie.getPath());

                            nameList.put(JSONCookieSets.getString("pin"), JSONCookieSets.getString("pin"));
                            cookieMap.put(JSONCookieSets.getString("pin"), new File(exec));
                        }
                        if (JSONCookieSets.has("name"))
                            //已UTF-8编码加载
                            nameList.put(JSONCookieSets.getString("pin"), JSONCookieSets.getString("name"));

                    }

                }
            }
            System.out.println(cookieMap);
        }
    }

    public String getName(String key) {
        return nameList.get(key);
    }

    public TreeMap<String, File> getCookieMap() {
        return cookieMap;
    }

}
