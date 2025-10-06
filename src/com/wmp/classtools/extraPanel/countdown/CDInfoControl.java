package com.wmp.classTools.extraPanel.countdown;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
import org.json.JSONObject;

import java.io.IOException;

public class CDInfoControl {

    //

    private static final String path = CTInfo.DATA_PATH + "CountDown.json";

    public static CDInfo getCDInfo() {

        try {
            IOForInfo io = new IOForInfo(path);
            String s = io.getInfos();
            if (!s.equals("err")) {
                JSONObject json = new JSONObject(s);
                return new CDInfo(json.getString("title"), json.getString("targetTime"));
            } else return new CDInfo("数据出错", "2030.01.01 00:00:00");
        } catch (Exception e) {
            Log.err.print(CDInfoControl.class, "获取倒计时信息失败\n" + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public static void setCDInfo(CDInfo cdInfo) throws IOException {
        if (cdInfo != null) {
            IOForInfo io = new IOForInfo(path);
            JSONObject json = new JSONObject();
            json.put("title", cdInfo.title);
            json.put("targetTime", cdInfo.targetTime);
            io.setInfo(json.toString());
        }
    }

    /**
     * 倒计时信息
     *
     * @param title      名字
     * @param targetTime 目标时间(yyyy.MM.dd HH:mm:ss)
     */
    public record CDInfo(String title, String targetTime) {
    }
}
