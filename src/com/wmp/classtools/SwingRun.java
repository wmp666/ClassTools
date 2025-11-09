package com.wmp.classTools;

import com.wmp.Main;
import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.EasterEgg.EasterEgg;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.PublicTools.update.GetNewerVersion;
import com.wmp.classTools.frame.LoadingWindow;
import com.wmp.classTools.frame.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicReference;

public class SwingRun {

    //, TreeMap<String, StartupParameters> allArgs, ArrayList<String> list
    public static void show(boolean b, boolean StartUpdate) throws URISyntaxException, IOException {

        Log.info.systemPrint("SwingRun", "开始初始化UI...");

        try {
            //使用系统UI
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("PopupMenu.borderInsets", new Insets(5, 10, 5, 10));

        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        //LoadingWindow loadingWindow;
        AtomicReference<LoadingWindow> loadingWindowRef = new AtomicReference<>();
        //SwingUtilities.invokeLater(() -> {

        if (b) {
            loadingWindowRef.set(new LoadingWindow(200, 200, "EasterEgg", true, 2300));
            //loadingWindow = new LoadingWindow(200, 200, "EasterEgg", true, 2300);

        } else {
            loadingWindowRef.set(new LoadingWindow());
            //loadingWindow = new LoadingWindow();
        }
        //});
        //System.out.println(sb);


        //loadingWindow.setVisible(true);


        new MainWindow(CTInfo.DATA_PATH);
        loadingWindowRef.get().setVisible(false);

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
                    loadingWindowRef.get(), null, true, false);

        }
    }
}
