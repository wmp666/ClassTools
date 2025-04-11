package com.wmp;

import java.awt.*;

public class CTColor {

    public static final String MAIN_COLOR_WHITE = "white";
    public static final String MAIN_COLOR_BLUE = "blue";
    public static final String MAIN_COLOR_GREEN = "green";
    public static final String MAIN_COLOR_RED = "red";
    public static final String MAIN_COLOR_BLACK = "black";

    public static final String STYLE_DARK = "dark";
    public static final String STYLE_LIGHT = "light";

    public static String style = STYLE_LIGHT;
    public static Color mainColor = Color.WHITE;
    public static Color textColor = Color.WHITE;
    public static Color backColor = Color.WHITE;

    public static void setColor(String mainColorStr, String tempStyle){
        // = new Color(0x0090FF)
        // = Color.BLACK;
        // = new Color(0xFFFFFF)
        switch (mainColorStr){
            case MAIN_COLOR_WHITE->{
                mainColor = Color.WHITE;
            }

            case MAIN_COLOR_BLUE->{
                mainColor = new Color(0x0090FF);

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
    }


}
