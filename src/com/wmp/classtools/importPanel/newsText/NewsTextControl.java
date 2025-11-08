package com.wmp.classTools.importPanel.newsText;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;

import java.io.IOException;

public class NewsTextControl {
    public static void setKey(String key) {
        IOForInfo io = new IOForInfo(CTInfo.DATA_PATH + "NewsText\\key.txt");
        try {
            io.setInfo(key);
        } catch (IOException e) {
            Log.err.print(NewsTextControl.class, "新闻获取密钥保存失败", e);
        }
    }

    public static String getKey() {
        IOForInfo io = new IOForInfo(CTInfo.DATA_PATH + "NewsText\\key.txt");
        try {
            String infos = io.getInfos();
            if (infos == null || infos.equals("err")) {
                return "";
            }
            return infos;
        } catch (IOException e) {
            Log.err.print(NewsTextControl.class, "新闻文本获取密钥获取失败", e);
        }
        return null;
    }
}
