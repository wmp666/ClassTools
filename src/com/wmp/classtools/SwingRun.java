package com.wmp.classTools;

import com.formdev.flatlaf.FlatLightLaf;
import com.wmp.Main;
import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.EasterEgg.EasterEgg;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.PublicTools.update.GetNewerVersion;
import com.wmp.classTools.CTComponent.CTGradientRoundProgressBarUI;
import com.wmp.classTools.frame.LoadingWindow;
import com.wmp.classTools.frame.MainWindow;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.ProgressBarUI;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicReference;

public class SwingRun {

    //, TreeMap<String, StartupParameters> allArgs, ArrayList<String> list
    public static void show(boolean b, boolean StartUpdate) throws URISyntaxException, IOException {

        Log.info.loading.showDialog("窗口加载", "正在将UI更改为系统样式...");

        try {

            FlatLightLaf.install();

            //使用系统UI
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            //设置默认字体
            FontUIResource fontRes = new FontUIResource(CTFont.getDefaultFont(Font.PLAIN, CTFontSizeStyle.SMALL));
            for(Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();){
                Object key = keys.nextElement();
                Object value = UIManager.get(key);
                if(value instanceof FontUIResource)
                    UIManager.put(key, fontRes);
            }
            UIManager.put("PopupMenu.borderInsets", new Insets(5, 10, 5, 10));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Log.info.loading.updateDialog("窗口加载", "正在显示加载界面...");
        AtomicReference<LoadingWindow> loadingWindowRef = new AtomicReference<>();

        if (b) {
            loadingWindowRef.set(new LoadingWindow(200, 200, "EasterEgg", true, 2300));

        } else {
            loadingWindowRef.set(new LoadingWindow());
        }
        Log.info.loading.closeDialog("窗口加载");


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
