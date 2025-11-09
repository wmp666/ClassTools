package com.wmp.classTools.extraPanel.classForm;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.DateTools;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class CFInfoControl {
    private static final String FilesPath = CTInfo.DATA_PATH + "ClassForm\\";

    /**
     * 获取某天的课表
     *
     * @param week 1~7
     * @return 课程信息
     */
    private static String getOneDayClasses(int week) {
        IOForInfo io = new IOForInfo(FilesPath + week + ".json");
        String infos;
        try {
            infos = io.getInfos();
            if (infos.equals("err"))
                return "[]";
        } catch (IOException e) {
            Log.err.print(CFInfoControl.class, FilesPath + week + ".json文件读取失败", e);
            return "[]";
        }
        return infos;
    }

    /**
     * 获取当前时间段内的课程
     *
     * @return 课程信息
     */
    public static String[] getNowClasses() {
        Calendar calendar = Calendar.getInstance();
        int week = 0;
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY -> week = 1;
            case Calendar.TUESDAY -> week = 2;
            case Calendar.WEDNESDAY -> week = 3;
            case Calendar.THURSDAY -> week = 4;
            case Calendar.FRIDAY -> week = 5;
            case Calendar.SATURDAY -> week = 6;
            case Calendar.SUNDAY -> week = 7;
        }
        //当天的课程表 [{"time":"HH:mm:ss-HH:mm:ss" , "class":"" }]
        JSONArray info = new JSONArray(getOneDayClasses(week));
        ArrayList<String> list = new ArrayList<>();
        info.forEach(object -> {
            if (object instanceof JSONObject jsonObject) {

                if (!(jsonObject.has("class") && jsonObject.has("time"))) return;

                String timePeriod = jsonObject.getString("time");
                String[] time = timePeriod.split("-");
                if (DateTools.isInTimePeriod(time[0], time[1])) {
                    list.add(jsonObject.getString("class"));
                }
            }
        });
        return list.toArray(new String[0]);
    }

    /**
     * 获取下一节课
     *
     * @return 课程信息
     */
    public static nextClassInfo getNextClass() {
        Calendar calendar = Calendar.getInstance();
        int week = 0;
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY -> week = 1;
            case Calendar.TUESDAY -> week = 2;
            case Calendar.WEDNESDAY -> week = 3;
            case Calendar.THURSDAY -> week = 4;
            case Calendar.FRIDAY -> week = 5;
            case Calendar.SATURDAY -> week = 6;
            case Calendar.SUNDAY -> week = 7;
        }
        //当天的课程表 [{"time":"HH:mm:ss-HH:mm:ss" , "class":"" }]
        JSONArray info = new JSONArray(getOneDayClasses(week));
        String[] tempData = new String[3];// 0-时间 1-课程 2-间隔时间
        info.forEach(object -> {
            if (object instanceof JSONObject jsonObject) {

                if (!(jsonObject.has("class") && jsonObject.has("time"))) return;

                String timePeriod = jsonObject.getString("time");
                String s = timePeriod.split("-")[0];//获取开始时间


                if (DateTools.getRemainderTime(s) <= 0) {
                    return;
                }

                if (tempData[0] == null ||
                        DateTools.getRemainderTime(s) < DateTools.getRemainderTime(tempData[0])) {
                    tempData[0] = s;
                    tempData[1] = jsonObject.getString("class");
                    tempData[2] = String.valueOf(DateTools.getRemainderTime(s) / 1000 / 60 + 1);
                }

            }
        });
        return new nextClassInfo(tempData[2], tempData[1]);
    }

    public record nextClassInfo(String time, String className) {
    }
}
