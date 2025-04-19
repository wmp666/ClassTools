package com.wmp.PublicTools.web;

import com.wmp.PublicTools.update.SslUtils;
import org.jsoup.Jsoup;

public class GetWebInf {
    public static String getWebInf(String apiUrl) throws Exception {
        SslUtils.ignoreSsl();
        try {
            // 获取原始JSON响应
            String webInf = Jsoup.connect(apiUrl)
                    .header("Accept", "application/vnd.github.v3+json")
                    .timeout(10000)
                    .ignoreContentType(true)
                    .execute()
                    .body();

            //System.out.println("原始数据 - " + webInf);
            return webInf;
        } catch (Exception e) {

            throw new RuntimeException("信息解析失败", e);
        }
    }
}
