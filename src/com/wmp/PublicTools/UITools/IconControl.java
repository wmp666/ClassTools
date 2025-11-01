package com.wmp.PublicTools.UITools;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.io.DownloadURLFile;
import com.wmp.PublicTools.io.GetPath;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.io.ZipPack;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.PublicTools.update.GetNewerVersion;
import com.wmp.PublicTools.web.GetWebInf;
import com.wmp.classTools.CTComponent.CTOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class IconControl {
    public static final int COLOR_DEFAULT = 0;
    public static final int COLOR_COLORFUL = 1;


    private static final Map<String, ImageIcon> DEFAULT_IMAGE_MAP = new HashMap<>();
    private static final Map<String, Map<String, ImageIcon>> COLORFUL_IMAGE_MAP = new HashMap<>();

    private static final Map<String, String> ICON_STYLE_MAP = new HashMap<>();

    static {
        init();

    }

    public static void init() {
        DEFAULT_IMAGE_MAP.clear();
        COLORFUL_IMAGE_MAP.clear();

        DEFAULT_IMAGE_MAP.put("图标", new ImageIcon(IconControl.class.getResource(CTInfo.iconPath)));

        //获取基础图标
        String resourceInfos = IOForInfo.getInfos(IconControl.class.getResource("imagePath.json"));
        JSONArray resourceJsonArray = new JSONArray(resourceInfos);
        resourceJsonArray.forEach(object -> {

            JSONObject jsonObject = (JSONObject) object;
            Log.info.print("IconControl", String.format("名称:%s|位置:%s", jsonObject.getString("name"), jsonObject.getString("path")));
            String pathStr = jsonObject.getString("path");
            URL path = IconControl.class.getResource(pathStr);
            if (path == null) {
                Log.warn.print("IconControl", String.format("图标文件%s不存在", jsonObject.getString("path")));
                DEFAULT_IMAGE_MAP.put(jsonObject.getString("name"),
                        new ImageIcon(IconControl.class.getResource("/image/default.png")));
            } else {
                DEFAULT_IMAGE_MAP.put(jsonObject.getString("name"),
                        new ImageIcon(path));
            }
            ICON_STYLE_MAP.put(jsonObject.getString("name"), jsonObject.getString("style"));
        });
        COLORFUL_IMAGE_MAP.put("light", DEFAULT_IMAGE_MAP);
        COLORFUL_IMAGE_MAP.put("dark",
                getColorfulImageMap(DEFAULT_IMAGE_MAP, CTColor.getParticularColor("white")));
        COLORFUL_IMAGE_MAP.put("err",
                getColorfulImageMap(DEFAULT_IMAGE_MAP, CTColor.getParticularColor("blue")));

        //判断磁盘中是否有图片
        getNewImage();


        //获取磁盘中的图标
        String iconInfos = IOForInfo.getInfos(CTInfo.APP_INFO_PATH + "image\\imagePath.json");

        JSONArray iconJsonArray = new JSONArray(iconInfos);
        iconJsonArray.forEach(object -> {
            JSONObject jsonObject = (JSONObject) object;
            Log.info.print("IconControl", String.format("名称:%s|位置:%s", jsonObject.getString("name"), jsonObject.getString("path")));
            String pathStr = new File(CTInfo.APP_INFO_PATH, jsonObject.getString("path")).getPath();
            URL path = null;
            try {
                path = new File(pathStr).toURI().toURL();
                DEFAULT_IMAGE_MAP.put(jsonObject.getString("name"),
                        new ImageIcon(path));
            } catch (MalformedURLException e) {
                Log.warn.print("IconControl", String.format("图标文件%s不存在", jsonObject.getString("path")));
                if (DEFAULT_IMAGE_MAP.get(jsonObject.getString("name")) != null)
                    DEFAULT_IMAGE_MAP.put(jsonObject.getString("name"),
                            new ImageIcon(IconControl.class.getResource("/image/default.png")));
            }

            ICON_STYLE_MAP.put(jsonObject.getString("name"), jsonObject.getString("style"));

        });

        COLORFUL_IMAGE_MAP.put("light", DEFAULT_IMAGE_MAP);
        COLORFUL_IMAGE_MAP.put("dark",
                getColorfulImageMap(DEFAULT_IMAGE_MAP, CTColor.getParticularColor("white")));
        COLORFUL_IMAGE_MAP.put("err",
                getColorfulImageMap(DEFAULT_IMAGE_MAP, CTColor.getParticularColor("blue")));

    }

    private static void getNewImage() {
        try {
            boolean needDownload = false;
            JSONObject jsonObject = new JSONObject(
                    GetWebInf.getWebInf("https://api.github.com/repos/wmp666/ClassTools_Image/releases/latest" , false));
            AtomicReference<String> downloadURL = new AtomicReference<>("");
            AtomicReference<String> version = new AtomicReference<>("");
            String oldVersion = IOForInfo.getInfo(CTInfo.APP_INFO_PATH + "image\\version.txt")[0];
            //判断是否存在
            version.set(jsonObject.getString("tag_name"));
            jsonObject.getJSONArray("assets").forEach(object -> {
                JSONObject asset = (JSONObject) object;
                if (asset.getString("name").equals("image.zip")) {
                    downloadURL.set(asset.getString("browser_download_url"));
                }
            });

            if (!oldVersion.equals("err")) {


                int newerVersion = GetNewerVersion.isNewerVersion(version.get(), oldVersion);
                if (newerVersion != 0) {
                    Log.info.print("IconControl", "有新图片");
                    needDownload = true;
                }


            } else {
                needDownload = true;
            }
            if (needDownload) {
                String choose = Log.info.showChooseDialog(null, "IconControl", "图片文件不存在/存在新版,选择获取方式", "下载", "导入压缩包");
                String zipPath = "";

                //清空文件
                Thread thread1 = IOForInfo.deleteDirectoryRecursively(Path.of(CTInfo.APP_INFO_PATH + "image"));
                thread1.join();

                if (choose.equals("下载")) {
                    //下载文件
                    DownloadURLFile.downloadWebFile(null, null, downloadURL.get(), CTInfo.TEMP_PATH + "appInfo");
                    zipPath = CTInfo.TEMP_PATH + "appInfo\\image.zip";

                } else if (choose.equals("导入压缩包")) {
                    zipPath = GetPath.getFilePath(null, "导入图片", ".zip", "图片压缩包");
                }
                //解压文件
                Thread thread = ZipPack.unzip(zipPath, CTInfo.APP_INFO_PATH);

                if (thread != null) {
                    thread.join();
                }
                //生成版本文件
                try {
                    new IOForInfo(CTInfo.APP_INFO_PATH + "image\\version.txt").setInfo(version.get());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (Exception e) {
            Log.err.print(IconControl.class, "获取图标版本失败", e);
        }
    }

    private static Map<String, ImageIcon> getColorfulImageMap(Map<String, ImageIcon> map, Color color) {
        Map<String, ImageIcon> colorfulImageMap = new HashMap<>();
        map.forEach((name, imageIcon) -> {
            BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = bufferedImage.createGraphics();
            g2d.drawImage(imageIcon.getImage(), 0, 0, null);

            ImageIcon icon = new ImageIcon(ColorConverter.applyColorTone(
                    ColorConverter.convertToGrayscale(bufferedImage),
                    color.getRed(), color.getGreen(), color.getBlue()));

            colorfulImageMap.put(name, icon);
        });
        return colorfulImageMap;
    }

    public static String getIconStyle(String name) {
        return ICON_STYLE_MAP.getOrDefault(name, "png");
    }

    public static ImageIcon getDefaultIcon(String name) {
        ImageIcon icon = DEFAULT_IMAGE_MAP.getOrDefault(name,
                DEFAULT_IMAGE_MAP.get("default"));
        return icon;
    }

    public static ImageIcon getColorfulIcon(String name) {

        HashMap<String, ImageIcon> defaultMap = new HashMap<>();
        defaultMap.put("default", DEFAULT_IMAGE_MAP.get("default"));
        ImageIcon icon = COLORFUL_IMAGE_MAP.getOrDefault(CTColor.style, defaultMap)
                .getOrDefault(name, DEFAULT_IMAGE_MAP.get("default"));
        return icon;
    }


    public static ImageIcon getIcon(String name, int colorStyle) {
        ImageIcon imageIcon = colorStyle == COLOR_DEFAULT ? getDefaultIcon(name) : getColorfulIcon(name);
        if (imageIcon == null) return new ImageIcon();
        return imageIcon;
    }
}
