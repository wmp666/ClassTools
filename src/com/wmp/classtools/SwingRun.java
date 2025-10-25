package com.wmp.classTools;

import com.wmp.Main;
import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.EasterEgg.EasterEgg;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.PublicTools.update.GetNewerVersion;
import com.wmp.classTools.frame.CTTools;
import com.wmp.classTools.frame.LoadingWindow;
import com.wmp.classTools.frame.MainWindow;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class SwingRun {

    //, TreeMap<String, StartupParameters> allArgs, ArrayList<String> list
    public static void show(boolean b, boolean StartUpdate) throws URISyntaxException, IOException {

        Log.info.systemPrint("SwingRun", "开始初始化UI...");

        try {
            //使用系统UI
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }


        LoadingWindow loadingWindow;
        if (b){
            loadingWindow = new LoadingWindow(Main.class.getResource("/image/loading.gif"),
                        200, 200, "useLoadingText", true, 2300);

        }else{
            loadingWindow = new LoadingWindow();
        }
        //System.out.println(sb);


        //loadingWindow.setVisible(true);
        new CTTools();

        new MainWindow(CTInfo.DATA_PATH);
        loadingWindow.setVisible(false);

        if (!(Main.allArgs.get("screenProduct:show").contains(Main.argsList) ||
                Main.allArgs.get("screenProduct:view").contains(Main.argsList))) {

            EasterEgg.showHolidayBlessings(0);
        }

        if (StartUpdate &&
                !(Main.allArgs.get("StartUpdate:false").contains(Main.argsList) ||
                        Main.allArgs.get("screenProduct:show").contains(Main.argsList) ||
                        Main.allArgs.get("screenProduct:view").contains(Main.argsList))) {
            Log.info.print("Main", "开始启动自动检查更新");
            GetNewerVersion.checkForUpdate(
                    loadingWindow, null, true, false);

        }
    }
}
