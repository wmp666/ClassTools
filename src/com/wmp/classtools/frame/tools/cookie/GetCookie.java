package com.wmp.classTools.frame.tools.cookie;

import com.wmp.Main;
import com.wmp.PublicTools.GetIcon;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetCookie {

    private final String CookiePath = Main.DATA_PATH + "\\Cookie\\";
    // pin -> name
    //private final TreeMap<String, String> nameList = new TreeMap<>();
    // pin -> 文件位置
    private final TreeMap<String, Cookie> cookieMap = new TreeMap<>();

    private static final TreeMap<String, String> fixedPathMap = new TreeMap<>();

    static {
        fixedPathMap.put("%DataPath", Main.DATA_PATH);
        fixedPathMap.put("%TempPath", Main.TEMP_PATH);
        fixedPathMap.put("%AppDirPath", System.getProperty("user.dir"));
    }

    public GetCookie() throws IOException {
        File basic = new File(CookiePath);
        if (!basic.exists()) {
            basic.mkdirs();
        }

        File[] cookies = basic.listFiles();
        if (cookies != null) {
            //循环判断每一个文件夹
            for (File cookieFile : cookies) {
                if (cookieFile.isDirectory()) {
                    //获取相关数据
                    File cookieSets = new File(cookieFile + "\\setUp.json");
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
                            JOptionPane.showMessageDialog(null,  cookieSets.getParent() + "setUp.json数据出错\n" + e.getMessage(), "JSON格式错误", JOptionPane.ERROR_MESSAGE);
                            System.err.println(e.getMessage());
                            continue;
                        }


                        if (JSONCookieSets.has("pin") && JSONCookieSets.has("run")) {

                            String exec = JSONCookieSets.getString("run");
                            exec = exec.replace("%CookiePath", cookieFile.getPath());
                            Pattern pattern = Pattern.compile("(%[^\\\\]+)");
                            Matcher matcher = pattern.matcher(exec);

                            while (matcher.find()){
                                String group = matcher.group(1);
                                if (fixedPathMap.containsKey(group)){
                                    exec = exec.replace(group, fixedPathMap.get(group)).replace("\\", "/");
                                }
                            }

                            Cookie cookie = new Cookie("null", "other", null, new File(exec), cookieFile);

                            StringBuilder iconPath = new StringBuilder();
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
                                StringBuilder temp = new StringBuilder();
                                temp.append("file:///").append(JSONCookieSets.getString("icon"));

                                {
                                    String tempStr = temp.toString();
                                    tempStr = tempStr.replace("%CookiePath", cookieFile.getPath());

                                    Pattern pattern2 = Pattern.compile("(%[^\\\\]+)");
                                    Matcher matcher2 = pattern2.matcher(tempStr);

                                    while (matcher2.find()){
                                        String group = matcher2.group(1);
                                        if (fixedPathMap.containsKey(group)){
                                            tempStr = tempStr.replace(group, fixedPathMap.get(group));
                                        }
                                    }

                                    iconPath.append(tempStr.replace("\\", "/"));
                                }

                                try {
                                    if (!iconPath.toString().isEmpty()) {
                                        cookie.setIcon(GetIcon.getImageIcon(new URL(iconPath.toString()), 40, 40));
                                    }
                                }catch ( Exception e){
                                    System.err.println("iconSetError:" + e);
                                    cookie.setIcon(null);
                                }

                            }

                            cookieMap.put(JSONCookieSets.getString("pin"), cookie);
                        }


                    }

                }
            }
            System.out.println(cookieMap);
        }

    }

    public String getName(String key) {
        return cookieMap.get(key).getName();
    }

    public TreeMap<String, Cookie> getCookieMap() {
        return cookieMap;
    }

}
