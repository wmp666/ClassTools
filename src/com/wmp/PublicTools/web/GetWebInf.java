package com.wmp.PublicTools.web;

import com.wmp.PublicTools.printLog.Log;
import org.jsoup.Jsoup;

public class GetWebInf {
    public static String getWebInf(String apiUrl) throws Exception {
        SslUtils.ignoreSsl();
        try {
            // 获取原始JSON响应
            String webInf = Jsoup.connect(apiUrl)
                    .header("Accept", "application/vnd.github.v3+json")
                    //.timeout(60000)
                    .ignoreContentType(true)
                    .execute()
                    .body();

            Log.info.print("GetWebInf", "获取Web信息成功");
            Log.info.print("GetWebInf", "信息: " + webInf);
            return webInf;
        } catch (Exception e) {
            Log.err.print(GetWebInf.class, "获取Web信息失败", e);
        }
        return "";
    }
}
