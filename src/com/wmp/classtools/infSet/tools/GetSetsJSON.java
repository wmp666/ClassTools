package com.wmp.classTools.infSet.tools;

import com.wmp.Main;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GetSetsJSON {

    private JSONObject jsonObject;

    private boolean canExit = true;
    private boolean StartUpdate = true;
    private final ArrayList<String> disButList = new ArrayList<>();

    public GetSetsJSON() throws IOException {
        boolean exists = new File(Main.DATA_PATH + "setUp.json").exists();

        if (exists) {
            IOForInfo sets = new IOForInfo(new File(Main.DATA_PATH + "setUp.json"));


            try {
                jsonObject = new JSONObject(sets.GetInfos());
            }catch (JSONException e){
                Log.err.print("获取个性化数据", "数据获取发生错误:" + e.getMessage());
                return;
            }

            //设置颜色
            if (jsonObject.has("mainColor")) {
                switch (jsonObject.getString("mainColor")) {
                    case "black" -> CTColor.setMainColorColor(CTColor.MAIN_COLOR_BLACK);
                    case "white" -> CTColor.setMainColorColor(CTColor.MAIN_COLOR_WHITE);
                    case "green" -> CTColor.setMainColorColor(CTColor.MAIN_COLOR_GREEN);
                    case "red" -> CTColor.setMainColorColor(CTColor.MAIN_COLOR_RED);
                    default -> CTColor.setMainColorColor(CTColor.MAIN_COLOR_BLUE);
                }
            }
            //设置主题
            if (jsonObject.has("mainTheme")) {
                if (jsonObject.getString("mainTheme").equals("dark")) {
                    CTColor.setMainTheme(CTColor.STYLE_DARK);
                } else {
                    CTColor.setMainTheme(CTColor.STYLE_LIGHT);
                }
            }
            //设置字体
            if (jsonObject.has("FontName")) {
                CTFont.setFontName(jsonObject.getString("FontName"));
            }
            //设置隐藏按钮
            if (jsonObject.has("disposeButton")){
                JSONArray disButtonList = jsonObject.getJSONArray("disposeButton");
                disButtonList.forEach(object -> {
                    disButList.add(object.toString());
                });
            }
            //设置是否可以退出
            if (jsonObject.has("canExit")) {
                canExit = jsonObject.getBoolean("canExit");
            }
            //设置是否可以更新
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

    public JSONObject getJsonObject() {
        return jsonObject;
    }
}
