package com.wmp.classTools.frame.tools.cookie;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

public class StartCookie {
    public static void showCookie(String pin){
        try {
            GetCookie getCookie = new GetCookie();
            TreeMap<String, File> cookieMap = getCookie.getCookieMap();
            if (cookieMap.containsKey(pin)) {
                File cookieFile = cookieMap.get(pin);
                Runtime runtime = Runtime.getRuntime();
                runtime.exec(cookieFile.getPath(), null, cookieFile.getParentFile());
                //Runtime.getRuntime().exec(cookieFile.getPath());
            } else {
                JOptionPane.showMessageDialog(null, "错误的pin", "世界拒绝了我", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
