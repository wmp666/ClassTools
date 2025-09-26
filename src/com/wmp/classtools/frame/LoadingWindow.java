package com.wmp.classTools.frame;

import com.wmp.Main;
import com.wmp.PublicTools.EasterEgg.EETextStyle;
import com.wmp.PublicTools.EasterEgg.EasterEgg;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.UITools.GetIcon;
import com.wmp.PublicTools.printLog.Log;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Arrays;

public class LoadingWindow extends JDialog {

    public static final int STYLE_NORMAL = 0;
    public static final int STYLE_SCREEN = 1;

    public LoadingWindow() {
        this(LoadingWindow.class.getResource(Main.iconPath),
                180, 180, "useLoadingText", false, 0, 0);
    }

    public LoadingWindow(String text) {
        this(LoadingWindow.class.getResource("/image/icon.png"),
                180, 180, text, false, 0, 0);
    }

    public LoadingWindow(URL url, int width, int height, String text){
        this(url, width, height, text, false, 0, 0);
    }

    public LoadingWindow(URL url, int width, int height, String text, boolean mustWait, long time){
        this(url, width, height, text, mustWait, time, 0);
    }

    public LoadingWindow(URL url, int width, int height, String text, boolean mustWait, long time, int windowStyle){

        Log.info.print("LoadingWindow-窗口", "开始初始化加载窗口");


        String showText = text;
        if (showText.equals("useLoadingText")){
            showText = getLoadingText();
        }

        // 根据文字数量调整窗口大小
        String plainText = showText.replaceAll("<html>|</html>", "").replaceAll("<br>", "\n"); // 去除HTML标签
        String[] lines = plainText.split("\n"); // 按换行符分割
        int lineCount = lines.length;
        // 计算最长行的长度 .mapToInt(String::length) 将每个字符串映射为其长度，然后使用 max() 方法找到最大值
        //.orElse(0) 如果数组为空，返回默认值 0
        int maxLength = Arrays.stream(lines).mapToInt(String::length).max().orElse(0);

        // 计算新的窗口尺寸（基础尺寸 + 动态调整）
        int baseWidth = 350;
        int baseHeight = 200;
        int newWidth = Math.max(baseWidth, maxLength * 20 + 200); // 每个字符约20像素宽度
        int newHeight = Math.max(baseHeight, lineCount * 30);  // 每多一行增加30像素高度
        time = Math.max(time, plainText.length() * 90L);

        JLabel label = new JLabel(showText, GetIcon.getIcon(url, width, height), SwingConstants.CENTER);
        label.setBackground(Color.WHITE);
        label.setFont(CTFont.getCTFont(Font.BOLD, CTFontSizeStyle.NORMAL));

        this.getContentPane().setBackground(Color.WHITE);
        this.add(label);

        this.setUndecorated(true);
        this.setAlwaysOnTop(true);
        this.pack();
        this.setLocationRelativeTo(null);

        this.setVisible(true);
        try {
            if (mustWait){
                Thread.sleep(time);
            } else if (text.equals("useLoadingText")) {
                Thread.sleep(time);
            }
        } catch (InterruptedException e) {
        throw new RuntimeException(e);
    }

        Log.info.print("LoadingWindow-窗口", "加载窗口初始化完毕");

    }

    private String getLoadingText() {
        String easterEgg = EasterEgg.getText(EETextStyle.HTML);
        /*
        if (easterEgg.contains("\n")) {
            easterEgg = "<html>" + easterEgg.replaceAll("\\n", "<br>") + "</html>";
        }*/
        return easterEgg;
    }
}
