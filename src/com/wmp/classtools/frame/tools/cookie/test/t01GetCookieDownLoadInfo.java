package com.wmp.classTools.frame.tools.cookie.test;

import com.wmp.PublicTools.web.GetWebInf;
import org.json.JSONArray;
import org.json.JSONObject;

public class t01GetCookieDownLoadInfo {
    public static void main(String[] args) throws Exception {
        String webInf = GetWebInf.getWebInf("https://api.github.com/repos/wmp666/ClassTools/releases/tags/0.0.2");

        JSONObject jsonObject = new JSONObject(webInf);
        JSONArray assets = jsonObject.getJSONArray("assets");
        assets.forEach(asset -> {
            JSONObject assetObject = (JSONObject) asset;
            if (assetObject.getString("name").equals("CookieInfo.json")) {
                System.out.println(assetObject.getString("browser_download_url"));
                try {
                    System.out.println(GetWebInf.getWebInf(assetObject.getString("browser_download_url")));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        });
    }
}
