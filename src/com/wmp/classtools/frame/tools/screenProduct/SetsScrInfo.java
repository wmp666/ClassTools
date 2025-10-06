package com.wmp.classTools.frame.tools.screenProduct;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SetsScrInfo {

    private JSONObject jsonObject;


    public SetsScrInfo() throws IOException {
        File BGPath = new File(CTInfo.DATA_PATH + "\\ScreenProduct\\background.json");
        if (!BGPath.exists()) {
            try {
                FileWriter fileWriter = new FileWriter(BGPath);
                fileWriter.write("{}");
                fileWriter.close();
            } catch (IOException e) {
                Log.err.print(SetsScrInfo.class, "初始化失败:" + e.getMessage());
                throw new RuntimeException(e);
            }
        }
        jsonObject = new JSONObject(new IOForInfo(BGPath).getInfos());

        Log.info.print("屏保数据", "初始化完成:" + jsonObject);
    }

    public int getBGImagesLength() {
        if (jsonObject.has("path")) {
            if (new File(jsonObject.getString("path")).isDirectory()) {
                String path = jsonObject.getString("path");
                File[] files = new File(path).listFiles();
                if (files != null) {
                    return files.length;
                }
            }
        }
        return 0;
    }

    public int getRepaintTimer() {
        if (jsonObject.has("repaintTimer")) {
            return jsonObject.getInt("repaintTimer");
        }
        return 0;
    }

    public String getBGImagePath(int index) {
        if (jsonObject.has("path")) {
            if (new File(jsonObject.getString("path")).isDirectory()) {
                String path = jsonObject.getString("path");
                File[] files = new File(path).listFiles();
                if (files != null && files.length > index) {
                    return files[index].getPath().isEmpty() ? files[index].getPath() : null;
                }
            } else {
                return jsonObject.getString("path").isEmpty() ? jsonObject.getString("path") : null;
            }

        }
        return null;
    }

    public String getMainColor() {
        if (jsonObject.has("mainColor")) {
            return jsonObject.getString("mainColor");
        }
        return null;
    }

    public String getMainTheme() {
        if (jsonObject.has("mainTheme")) {
            return jsonObject.getString("mainTheme");
        }
        return null;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
