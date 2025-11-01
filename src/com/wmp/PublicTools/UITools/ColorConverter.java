package com.wmp.PublicTools.UITools;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ColorConverter {

    /**
     * 应用特定色调
     * @param original 原始图片
     * @param redFactor 红色因子
     * @param greenFactor 绿色因子
     * @param blueFactor 蓝色因子
      */

    public static BufferedImage applyColorTone(BufferedImage original,
                                               double redFactor,
                                               double greenFactor,
                                               double blueFactor) {
        int width = original.getWidth();
        int height = original.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = original.getRGB(x, y);
                int alpha = (rgb >> 24) & 0xff;
                //int red = (rgb >> 16) & 0xff;
                //int green = (rgb >> 8) & 0xff;
                //int blue = rgb & 0xff;

                //red = (int)(red * redFactor);
                //green = (int)(green * greenFactor);
                //blue = (int)(blue * blueFactor);

                //red = Math.min(255, Math.max(0, red));
                //green = Math.min(255, Math.max(0, green));
                //blue = Math.min(255, Math.max(0, blue));

                int newRGB = (alpha << 24) | ((int)redFactor << 16) | ((int)greenFactor << 8) | (int)blueFactor;
                result.setRGB(x, y, newRGB);
            }
        }
        return result;
    }

    /** 转换为灰度图
     *
     * @param original  图片
     * @return 灰度图(灰色)
     */
    public static BufferedImage convertToGrayscale(BufferedImage original) {
        int width = original.getWidth();
        int height = original.getHeight();
        BufferedImage whiteImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = original.getRGB(x, y);

                // 获取Alpha通道（透明度）
                int alpha = (rgb >> 24) & 0xff;

                // 创建灰色像素，保留原始透明度
                int whiteRGB = (alpha << 24) | 0xB3B1B1;

                whiteImage.setRGB(x, y, whiteRGB);
            }
        }
        return whiteImage;
    }
}
