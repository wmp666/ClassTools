package com.wmp.PublicTools.web;

import com.wmp.PublicTools.printLog.Log;
import org.jsoup.Jsoup;

import javax.swing.*;
import java.util.Random;

public class GetWebInf {

    public static String getWebInf(String apiUrl) throws Exception {
        return getWebInf(apiUrl, true);
    }
    public static String getWebInf(String apiUrl, boolean showDialog) throws Exception {
        int id = new Random().nextInt();
        SslUtils.ignoreSsl();
        if (showDialog) {
            Log.info.loading.showDialog("网络信息获取" + id, "正在获取Web信息...");
        }
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
            if (showDialog) {
                Log.info.loading.closeDialog("网络信息获取" + id);
            }
            return webInf;
        } catch (Exception e) {
            Log.warn.print("GetWebInf", "获取Web信息失败\n" + e);
        }
        if (showDialog) {
            Log.info.loading.closeDialog("网络信息获取" + id);
        }
        return "";
    }
}
