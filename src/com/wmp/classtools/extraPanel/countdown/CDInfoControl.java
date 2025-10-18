package com.wmp.classTools.extraPanel.countdown;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.DateTools;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
import org.json.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class CDInfoControl {

    //

    private static final String path = CTInfo.DATA_PATH + "CountDown.json";

    public static CDInfo getCDInfo() {

        try {
            IOForInfo io = new IOForInfo(path);
            String s = io.getInfos();
            if (!s.equals("err")) {
                if (s.startsWith("{")) {
                    JSONObject json = new JSONObject(s);
                    return new CDInfo(json.getString("title"), json.getString("targetTime"));
                }else{
                    JSONArray jsonArray = new JSONArray(s);

                    AtomicReference<CDInfo> resultInfo = new AtomicReference<>(new CDInfo("数据出错", "2030.01.01 00:00:00"));
                    //CDInfo resultInfo = new CDInfo("数据出错", "2030.01.01 00:00:00");
                    AtomicLong remainderTime = new AtomicLong();

                    jsonArray.forEach(o -> {
                        if (o instanceof JSONObject json){
                            CDInfo info = new CDInfo(json.getString("title"), json.getString("targetTime"));
                            long tempRemainderTime = DateTools.getRemainderTime(info.targetTime, "yyyy.MM.dd HH:mm:ss");
                            if (tempRemainderTime > 0){
                                if (resultInfo.get().title.equals("数据出错")) {
                                    resultInfo.set(info);
                                    remainderTime.set(tempRemainderTime);
                                    return;
                                }

                                if (remainderTime.get() > tempRemainderTime) {
                                    resultInfo.set(info);
                                    remainderTime.set(tempRemainderTime);
                                }
                            }
                        }
                    });
                    return resultInfo.get();
                }


            } else return new CDInfo("数据出错", "2030.01.01 00:00:00");
        } catch (Exception e) {
            Log.err.print(CDInfoControl.class, "获取倒计时信息失败", e);
        }
        return new CDInfo("数据出错", "2030.01.01 00:00:00");
    }

    public static CDInfo[] getCDInfos(){
        try {
            IOForInfo io = new IOForInfo(path);
            String s = io.getInfos();
            if (!s.equals("err")) {
                if (s.startsWith("{")) {
                    JSONObject json = new JSONObject(s);
                    return new CDInfo[]{new CDInfo(json.getString("title"), json.getString("targetTime"))};
                }else{
                    JSONArray jsonArray = new JSONArray(s);
                    ArrayList<CDInfo> resultInfo = new ArrayList<>();

                    jsonArray.forEach(o -> {
                        if (o instanceof JSONObject json){
                            CDInfo info = new CDInfo(json.getString("title"), json.getString("targetTime"));
                            resultInfo.add(info);
                        }
                    });
                    return resultInfo.toArray(new CDInfo[0]);
                }


            } else return new CDInfo[]{new CDInfo("数据出错", "2030.01.01 00:00:00")};
        } catch (Exception e) {
            Log.err.print(CDInfoControl.class, "获取倒计时信息失败", e);
        }
        return new CDInfo[]{new CDInfo("数据出错", "2030.01.01 00:00:00")};
    }
    public static void setCDInfo(CDInfo[] cdInfo) throws IOException {
        if (cdInfo != null) {
            JSONArray jsonArray = new JSONArray();
            for (CDInfo info : cdInfo) {
                if (info == null) continue;

                JSONObject json = new JSONObject();
                json.put("title", info.title);
                json.put("targetTime", info.targetTime);

                jsonArray.put(json);
            }
            IOForInfo io = new IOForInfo(path);

            io.setInfo(jsonArray.toString());
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
