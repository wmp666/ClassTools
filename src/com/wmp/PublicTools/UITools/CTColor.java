package com.wmp.PublicTools.UITools;

import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.frame.tools.screenProduct.SetsScrInfo;

import java.awt.*;
import java.io.IOException;

public class CTColor {
    /**
     * 主色 白色
     */
    public static final String MAIN_COLOR_WHITE = "white";
    /**
     * 主色 蓝色
     */
    public static final String MAIN_COLOR_BLUE = "blue";
    /**
     * 主色 绿色
     */
    public static final String MAIN_COLOR_GREEN = "green";
    /**
     * 主色 红色
     */
    public static final String MAIN_COLOR_RED = "red";
    /**
     * 主色 黑色
     */
    public static final String MAIN_COLOR_BLACK = "black";
    /**
     * 主题 灰色
     */
    public static final String STYLE_DARK = "dark";
    /**
     * 主题 亮色
     */
    public static final String STYLE_LIGHT = "light";
    public static String style = STYLE_LIGHT;
    public static Color mainColor = new Color(0x29A8E3);
    public static Color textColor = Color.BLACK;
    public static Color backColor = Color.WHITE;
    private static boolean canRemove = true;

    public static void setScreenProductColor() throws IOException {
        setAllColor(CTColor.MAIN_COLOR_WHITE, CTColor.STYLE_DARK);

        SetsScrInfo setsScrInfo = new SetsScrInfo();
        if (setsScrInfo.getMainColor() != null) {
            setMainColor(setsScrInfo.getMainColor(), true);
        }
        if (setsScrInfo.getMainTheme() != null) {
            setMainTheme(setsScrInfo.getMainTheme(), true);
        }

        canRemove = false;

    }

    public static void setErrorColor() {
        canRemove = false;

        mainColor = new Color(0x29A8E3);
        textColor = new Color(0x29A8E3);
        backColor = new Color(246, 250, 255);
        style = "err";
    }

    public static void setAllColor(String mainColorStr, String tempStyle) {

        setMainColor(mainColorStr);
        setMainTheme(tempStyle);

    }

    public static void setMainColor(String mainColorStr) {
        setMainColor(mainColorStr, false);
    }

    private static void setMainColor(String mainColorStr, boolean mustRemove) {


        if (!mustRemove && !canRemove) return;

        mainColor = getParticularColor(mainColorStr);

        Log.info.print("CTColor", "mainColor:" + String.format("#%06x", mainColor.getRGB()));
    }

    public static void setMainTheme(String tempStyle) {
        setMainTheme(tempStyle, false);
    }

    private static void setMainTheme(String tempStyle, boolean mustRemove) {

        if (!mustRemove && !canRemove) return;

        style = tempStyle;
        switch (tempStyle) {
            case STYLE_DARK -> {
                textColor = Color.WHITE;
                backColor = Color.BLACK;
            }
            case STYLE_LIGHT -> {
                textColor = Color.BLACK;
                backColor = new Color(0xFFFFFF);
            }
        }
        Log.info.print("CTColor", "style:" + tempStyle);
    }

    /**
     * 获取指定颜色
     *
     * @param colorStyle 颜色样式
     * @return 颜色
     * @see CTColor
     * @see #MAIN_COLOR_WHITE
     * @see #MAIN_COLOR_BLUE
     * @see #MAIN_COLOR_GREEN
     * @see #MAIN_COLOR_RED
     * @see #MAIN_COLOR_BLACK
     *
     */
    public static Color getParticularColor(String colorStyle) {
        return switch (colorStyle) {
            case MAIN_COLOR_WHITE -> Color.WHITE;
            case MAIN_COLOR_BLUE -> new Color(0x29A8E3);
            case MAIN_COLOR_GREEN -> new Color(0x00FF00);
            case MAIN_COLOR_RED -> new Color(0xFF0000);
            case MAIN_COLOR_BLACK -> Color.BLACK;
            default -> Color.DARK_GRAY;
        };

    }

    @Override
    public String toString() {
        return "CTColor{ mainColor:" + String.format("#%06x", mainColor.getRGB()) +
                " textColor:" + String.format("#%06x", textColor.getRGB()) +
                " backColor:" + String.format("#%06x", backColor.getRGB()) + "}";
    }
}
