package com.wmp.classTools.infSet.tools;

import com.wmp.CTColor;
import com.wmp.Main;
import com.wmp.PublicTools.io.IOStreamForInf;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;

public class GetSetsJSON {
    private boolean canExit = true;
    private boolean StartUpdate = true;
    private final ArrayList<String> disButList = new ArrayList<>();

    public GetSetsJSON() throws IOException {
        boolean exists = new File(Main.DATA_PATH + "setUp.json").exists();

        if (exists) {
            IOStreamForInf sets = new IOStreamForInf(new File(Main.DATA_PATH + "setUp.json"));
            System.out.println(sets);
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(sets.GetInf()[0]);
            }catch (JSONException e){
                System.err.println(e.getMessage());
                return;
            }

            if (jsonObject.has("mainColor")) {
                switch (jsonObject.getString("mainColor")) {
                    case "black" -> CTColor.setMainColorColor(CTColor.MAIN_COLOR_BLACK);
                    case "white" -> CTColor.setMainColorColor(CTColor.MAIN_COLOR_WHITE);
                    case "green" -> CTColor.setMainColorColor(CTColor.MAIN_COLOR_GREEN);
                    case "red" -> CTColor.setMainColorColor(CTColor.MAIN_COLOR_RED);
                    default -> CTColor.setMainColorColor(CTColor.MAIN_COLOR_BLUE);
                }
            }
            if (jsonObject.has("mainTheme")) {
                switch (jsonObject.getString("mainTheme")) {
                    case "dark" -> CTColor.setMainTheme(CTColor.STYLE_DARK);
                    default -> CTColor.setMainTheme(CTColor.STYLE_LIGHT);
                }
            }
            if (jsonObject.has("disposeButton")){
                JSONArray disButtonList = jsonObject.getJSONArray("disposeButton");
                disButtonList.forEach(object -> {
                    disButList.add(object.toString());
                });
                System.out.println(disButList);
            }
            if (jsonObject.has("canExit")) {
                canExit = jsonObject.getBoolean("canExit");
            }
            if (jsonObject.has("StartUpdate")) {
                StartUpdate = jsonObject.getBoolean("StartUpdate");
            }
        }

        //加载颜色(CTColor)数据
        //判断当前时间是否是4月1日
        // 明确指定时区
        LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Shanghai"));
        boolean b = currentDate.getMonth() == Month.APRIL
                && currentDate.getDayOfMonth() == 1;
        if (b){
            CTColor.setAllColor(CTColor.MAIN_COLOR_GREEN, CTColor.STYLE_LIGHT);
        }

        System.out.println(new CTColor());
    }

    public boolean isCanExit() {
        return canExit;
    }

    public boolean isStartUpdate() {
        return StartUpdate;
    }

    public ArrayList<String> getDisButList() {
        return disButList;
    }

}
