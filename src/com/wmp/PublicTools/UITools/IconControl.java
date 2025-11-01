package com.wmp.PublicTools.UITools;

import com.wmp.Main;
import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.io.IOForInfo;
import com.wmp.PublicTools.printLog.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class IconControl {
    public static final int COLOR_DEFAULT = 0;
    public static final int COLOR_COLORFUL = 1;


    private static final Map<String, ImageIcon> DEFAULT_IMAGE_MAP = new HashMap<>();
    private static final Map<String, Map<String, ImageIcon>> COLORFUL_IMAGE_MAP = new HashMap<>();

    private static final Map<String, String> ICON_STYLE_MAP = new HashMap<>();

    static{

        DEFAULT_IMAGE_MAP.put("图标", new ImageIcon(IconControl.class.getResource(CTInfo.iconPath)));

        String infos = IOForInfo.getInfos(IconControl.class.getResource("imagePath.json"));
        JSONArray jsonArray = new JSONArray(infos);
        jsonArray.forEach(object -> {
            Log.info.print("IconControl", object.toString());

            JSONObject jsonObject = (JSONObject) object;
            Log.info.print("IconControl", String.format("名称:%s|位置:%s", jsonObject.getString("name"), jsonObject.getString("path")));
            String pathStr = jsonObject.getString("path");
            URL path = IconControl.class.getResource(pathStr);
            if (path == null) {
                Log.warn.print("IconControl", String.format("图标文件%s不存在", jsonObject.getString("path")));
                DEFAULT_IMAGE_MAP.put(jsonObject.getString("name"),
                        new ImageIcon(IconControl.class.getResource("/image/optionDialogIcon/warn.png")));
            }else {
                DEFAULT_IMAGE_MAP.put(jsonObject.getString("name"),
                        new ImageIcon(path));
            }
            ICON_STYLE_MAP.put(jsonObject.getString("name"), jsonObject.getString("style"));
        });

        COLORFUL_IMAGE_MAP.put("light", DEFAULT_IMAGE_MAP);
        COLORFUL_IMAGE_MAP.put("dark",
                getColorfulImageMap(DEFAULT_IMAGE_MAP,CTColor.getParticularColor("white")));
        COLORFUL_IMAGE_MAP.put("err",
                getColorfulImageMap(DEFAULT_IMAGE_MAP,CTColor.getParticularColor("blue")));


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
        return colorStyle == COLOR_DEFAULT? getDefaultIcon(name) : getColorfulIcon(name);
    }
}
