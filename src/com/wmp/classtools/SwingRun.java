package com.wmp.classTools;

import com.wmp.Main;
import com.wmp.PublicTools.StartupParameters;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.PublicTools.update.GetNewerVersion;
import com.wmp.PublicTools.EasterEgg.EasterEgg;
import com.wmp.classTools.frame.LoadingWindow;
import com.wmp.classTools.frame.MainWindow;
import com.wmp.classTools.frame.tools.cookie.StartCookie;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.TreeMap;

public class SwingRun {

    public static void show(boolean b, TreeMap<String, StartupParameters> allArgs, ArrayList<String> list, boolean StartUpdate) throws URISyntaxException, IOException {

        try {
            //使用系统UI
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }


        LoadingWindow loadingWindow;
        if (b){
            if (allArgs.get("screenProduct:show").contains(list)){
                loadingWindow = new LoadingWindow(Main.class.getResource("/image/start.gif"),
                        692, 491, "", true, 1300, LoadingWindow.STYLE_SCREEN);
            }else{
                loadingWindow = new LoadingWindow(Main.class.getResource("/image/loading.gif"),
                        200, 200, "useLoadingText", true, 2300);
            }

        }else{
            loadingWindow = new LoadingWindow();
        }
        //System.out.println(sb);


        //loadingWindow.setVisible(true);

        if (StartUpdate &&
                !(allArgs.get("StartUpdate:false").contains(list) ||
                        allArgs.get("screenProduct:show").contains(list) ||
                        allArgs.get("screenProduct:view").contains(list))) {
            Log.info.print("Main", "开始启动自动检查更新");
            GetNewerVersion.checkForUpdate(
                    loadingWindow, null, true);

        }

        if (allArgs.get("EasterEgg:").contains(list)) {
            int i = list.indexOf("-EasterEgg:") + 1;
            Log.info.print("Main", "-EasterEgg:" + list.get(i));
            //System.out.println();
            EasterEgg.showEasterEgg(EasterEgg.STYLE_EE_VIDEO, list.get(i));
        }
        if (allArgs.get("Cookie:StartUp").contains(list)) {
            int i = list.indexOf("-OpenCookie:") + 1;
            Log.info.print("Main", "-OpenCookie:" + list.get(i));
            //System.out.println();
            StartCookie.showCookie(list.get(i).split(";"));
        }

        new MainWindow(Main.DATA_PATH);
        loadingWindow.setVisible(false);
    }
}
