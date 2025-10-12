package com.wmp.PublicTools.UITools;

import com.wmp.PublicTools.printLog.Log;
import com.wmp.classTools.frame.tools.screenProduct.SetsScrInfo;

import java.awt.*;
import java.io.IOException;

public class CTColor {
    private static boolean canRemove = true;

    public static final String MAIN_COLOR_WHITE = "white";
    public static final String MAIN_COLOR_BLUE = "blue";
    public static final String MAIN_COLOR_GREEN = "green";
    public static final String MAIN_COLOR_RED = "red";
    public static final String MAIN_COLOR_BLACK = "black";

    public static final String STYLE_DARK = "dark";
    public static final String STYLE_LIGHT = "light";

    public static String style = STYLE_LIGHT;
    public static Color mainColor = new Color(0x29A8E3);
    public static Color textColor = Color.BLACK;
    public static Color backColor = Color.WHITE;


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

        textColor = new Color(0x29A8E3);
        backColor = new Color(246, 250, 255);
        style = "error";
    }
    public static void setAllColor(String mainColorStr, String tempStyle){

        setMainColor(mainColorStr);
        setMainTheme(tempStyle);

    }

    public static void setMainColor(String mainColorStr) {
        setMainColor(mainColorStr, false);
    }

    private static void setMainColor(String mainColorStr, boolean mustRemove) {


        if (!mustRemove && !canRemove) return;

        switch (mainColorStr){
            case MAIN_COLOR_WHITE->{
                mainColor = Color.WHITE;
            }

            case MAIN_COLOR_BLUE->{
                mainColor = new Color(0x29A8E3);

            }
            case MAIN_COLOR_GREEN->{
                mainColor = new Color(0x00FF00);
            }
            case MAIN_COLOR_RED->{
                mainColor = new Color(0xFF0000);
            }
            case MAIN_COLOR_BLACK->{
                mainColor = Color.BLACK;
            }
        }
        Log.info.print("CTColor", "mainColor:" + String.format("#%06x", mainColor.getRGB()));
    }

    public static void setMainTheme(String tempStyle){
        setMainTheme(tempStyle, false);
    }

    private static void setMainTheme(String tempStyle, boolean mustRemove) {

        if (!mustRemove && !canRemove) return;

        style = tempStyle;
        switch (tempStyle){
            case STYLE_DARK->{
                textColor = Color.WHITE;
                backColor = Color.BLACK;
            }
            case STYLE_LIGHT->{
                textColor = Color.BLACK;
                backColor = new Color(0xFFFFFF);
            }
        }
        Log.info.print("CTColor", "style:" + tempStyle);
    }

    @Override
    public String toString() {
        return "CTColor{ mainColor:" + String.format("#%06x", mainColor.getRGB()) +
                " textColor:" + String.format("#%06x", textColor.getRGB()) +
                " backColor:" + String.format("#%06x", backColor.getRGB()) + "}";
    }
}
