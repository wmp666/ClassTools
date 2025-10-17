package com.wmp.PublicTools.web;

import com.wmp.PublicTools.printLog.Log;
import org.jsoup.Jsoup;

import javax.swing.*;

public class GetWebInf {
    public static String getWebInf(String apiUrl) throws Exception {
        SslUtils.ignoreSsl();
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Log.info.loadingDialog.showDialog("网络信息获取", "正在获取Web信息...");
                return null;
            }
        }.execute();
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
            Log.info.loadingDialog.closeDialog("网络信息获取");
            return webInf;
        } catch (Exception e) {
            Log.err.print(GetWebInf.class, "获取Web信息失败", e);
        }
        Log.info.loadingDialog.closeDialog("网络信息获取");
        return "";
    }
}
