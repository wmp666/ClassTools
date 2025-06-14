package com.wmp.PublicTools.UITools;

import java.awt.*;

public class CTFont {

    //public static final int BIG = 0;
    //public static final int NORMAL = 1;
    //public static final int SMALL = 2;
    //public static final int MORE_SMALL = 3;
    public static Font getCTFont(int fontStyle, CTFontSizeStyle sizeStyle) {
        int size = 0;
        switch (sizeStyle) {
            case BIG -> size = 24;
            case NORMAL -> size = 19;
            case SMALL -> size = 15;
            case MORE_SMALL -> size = 12;
        }//12 14/-15-/16 18/(-19-/)20 -23-/24/25
        return new Font("微软雅黑", fontStyle, size);
    }
}
