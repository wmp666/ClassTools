package com.wmp.PublicTools.appFileControl;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.UITools.CTColor;
import com.wmp.PublicTools.io.DownloadURLFile;
import com.wmp.PublicTools.io.GetPath;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.io.ZipPack;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.PublicTools.update.GetNewerVersion;
import com.wmp.PublicTools.web.GetWebInf;
import com.wmp.classTools.CTComponent.CTOptionPane;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class MusicControl {

    private static final Map<String, Player> PLAYER_MAP = new HashMap<>();
    public static void init() {
        try {
            PLAYER_MAP.clear();

            //获取基础音频
            String resourceInfos = IOForInfo.getInfos(MusicControl.class.getResource("musicIndex.json"));
            JSONArray resourceJsonArray = new JSONArray(resourceInfos);
            resourceJsonArray.forEach(object -> {

                JSONObject jsonObject = (JSONObject) object;
                Log.info.print("MusicControl", String.format("名称:%s|位置:%s", jsonObject.getString("name"), jsonObject.getString("path")));
                String pathStr = jsonObject.getString("path");
                URL path = MusicControl.class.getResource(pathStr);
                if (path == null) {
                    Log.warn.print("MusicControl", String.format("音频文件%s不存在", jsonObject.getString("path")));
                } else {
                    PLAYER_MAP.put(jsonObject.getString("name"),
                            getPlayer(MusicControl.class.getResourceAsStream(pathStr)));
                }
            });

        } catch (Exception e) {
            Log.err.print(MusicControl.class, "音频加载失败", e);
        }
        try {
            //判断磁盘中是否有音频
            getNewMusic();

        } catch (Exception e) {
            Log.err.print(MusicControl.class, "音频数据判断失败", e);
        }

        try {
            //获取磁盘中的图标
            String musicInfos = IOForInfo.getInfos(CTInfo.APP_INFO_PATH + "music\\musicIndex.json");

            JSONArray musicJsonArray = new JSONArray(musicInfos);
            musicJsonArray.forEach(object -> {
                JSONObject jsonObject = (JSONObject) object;
                Log.info.print("MusicControl", String.format("名称:%s|位置:%s", jsonObject.getString("name"), jsonObject.getString("path")));
                String pathStr = new File(CTInfo.APP_INFO_PATH, jsonObject.getString("path")).getPath();
                try {
                    File file = new File(pathStr);
                    if (!file.exists()) {
                        Log.warn.print("MusicControl", String.format("音频文件%s不存在", jsonObject.getString("path")));
                    } else {
                        PLAYER_MAP.put(jsonObject.getString("name"),
                                getPlayer(new FileInputStream(file)));
                    }

                } catch (Exception e) {
                    Log.warn.print("MusicControl", String.format("音频文件%s不存在", jsonObject.getString("path")));
                }


            });


        } catch (Exception e) {
            Log.err.print(MusicControl.class, "本地音频加载失败", e);
        }

    }

    public static Player getPlayer(String key) {
        return PLAYER_MAP.get(key);
    }

    private static void getNewMusic() {
        try {
            boolean needDownload = false;
            JSONObject jsonObject = new JSONObject(
                    GetWebInf.getWebInf("https://api.github.com/repos/wmp666/ClassTools_Music/releases/latest", false));
            AtomicReference<String> downloadURL = new AtomicReference<>("");
            AtomicReference<String> version = new AtomicReference<>("");
            String oldVersion = IOForInfo.getInfo(CTInfo.APP_INFO_PATH + "music\\version.txt")[0];
            //判断是否存在
            version.set(jsonObject.getString("tag_name"));
            jsonObject.getJSONArray("assets").forEach(object -> {
                JSONObject asset = (JSONObject) object;
                if (asset.getString("name").equals("music.zip")) {
                    downloadURL.set(asset.getString("browser_download_url"));
                }
            });

            if (!oldVersion.equals("err")) {
                String[] split = oldVersion.split(":");
                int newerVersion = GetNewerVersion.isNewerVersion(version.get(), split[split.length - 1]);
                if (newerVersion != 0) {
                    if (!split[0].equals("zip")) {
                        Log.info.print("MusicControl", "有新音频");
                        needDownload = true;
                    } else {
                        int i = Log.warn.showChooseDialog(null, "MusicControl", "我们已经更新了官方音频库,而您的音频似乎是使用压缩包导入的(可能为第三方),我们无法确认是否要更新,如果你已经有了相关的最新版本/想要使用官方音频库,请按\"是\",否则按\"否\"");
                        if (i == CTOptionPane.YES_OPTION) {
                            needDownload = true;
                        }
                    }
                }


            } else {
                needDownload = true;
            }
            if (needDownload) {
                if (downloadFile(downloadURL, version)) {
                }
            }
        } catch (Exception e) {
            Log.err.print(MusicControl.class, "获取图标版本失败", e);
        }
    }

    public static boolean downloadFile(AtomicReference<String> downloadURL, AtomicReference<String> version) throws InterruptedException {
        String choose = Log.info.showChooseDialog(null, "MusicControl", "音频文件不存在/存在新版,选择获取方式", "下载", "导入压缩包");
        String zipPath = "";


        if (choose.equals("下载")) {
            //下载文件
            boolean b = DownloadURLFile.downloadWebFile(null, null, downloadURL.get(), CTInfo.TEMP_PATH + "appInfo");
            if (!b) return true;
            zipPath = CTInfo.TEMP_PATH + "appInfo\\music.zip";

        } else if (choose.equals("导入压缩包")) {
            Log.warn.message(null, "MusicControl", "若导入的音频库为第三方,可能需要自行更新");
            version.set("zip:" + version.get());
            zipPath = GetPath.getFilePath(null, "导入音频", ".zip", "音频压缩包");
        } else {
            Log.warn.message(null, "MusicControl", "若不下载/导入音频,可能造成程序异常");
            return true;
        }
        //清空文件
        Thread thread1 = IOForInfo.deleteDirectoryRecursively(Path.of(CTInfo.APP_INFO_PATH + "music"));
        thread1.join();

        //解压文件
        Thread thread = ZipPack.unzip(zipPath, CTInfo.APP_INFO_PATH);

        if (thread != null) {
            thread.join();
        }
        //生成版本文件
        try {
            new IOForInfo(CTInfo.APP_INFO_PATH + "music\\version.txt").setInfo(version.get());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private static Player getPlayer(InputStream inputStream) {
        if (inputStream != null) {
            try {
                return new Player(inputStream);
            } catch (JavaLayerException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
