package com.wmp.classTools.infSet.tools;

import com.wmp.CTColor;
import com.wmp.Main;
import com.wmp.PublicTools.io.IOStreamForInf;
import com.wmp.PublicTools.printLog.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GetSetsJSON {
    private boolean canExit = true;
    private boolean StartUpdate = true;
    private final ArrayList<String> disButList = new ArrayList<>();

    public GetSetsJSON() throws IOException {
        boolean exists = new File(Main.DATA_PATH + "setUp.json").exists();

        if (exists) {
            IOStreamForInf sets = new IOStreamForInf(new File(Main.DATA_PATH + "setUp.json"));

            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(sets.GetInf()[0]);
            }catch (JSONException e){
                Log.error.print("获取个性化数据", "数据获取发生错误:" + e.getMessage());
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
                if (jsonObject.getString("mainTheme").equals("dark")) {
                    CTColor.setMainTheme(CTColor.STYLE_DARK);
                } else {
                    CTColor.setMainTheme(CTColor.STYLE_LIGHT);
                }
            }
            if (jsonObject.has("disposeButton")){
                JSONArray disButtonList = jsonObject.getJSONArray("disposeButton");
                disButtonList.forEach(object -> {
                    disButList.add(object.toString());
                });
            }
            if (jsonObject.has("canExit")) {
                canExit = jsonObject.getBoolean("canExit");
            }
            if (jsonObject.has("StartUpdate")) {
                StartUpdate = jsonObject.getBoolean("StartUpdate");
            }
        }


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
