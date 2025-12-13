package com.wmp.classTools;

import com.formdev.flatlaf.FlatLightLaf;
import com.wmp.Main;
import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.EasterEgg.EasterEgg;
import com.wmp.PublicTools.UITools.CTFont;
import com.wmp.PublicTools.UITools.CTFontSizeStyle;
import com.wmp.PublicTools.io.GetPath;
import com.wmp.PublicTools.printLog.Log;
import com.wmp.PublicTools.update.GetNewerVersion;
import com.wmp.classTools.frame.LoadingWindow;
import com.wmp.classTools.frame.MainWindow;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SwingRun {

    //, TreeMap<String, StartupParameters> allArgs, ArrayList<String> list
    public static void show(boolean b, boolean StartUpdate) throws URISyntaxException, IOException {

        Log.info.loading.showDialog("窗口加载", "正在将UI更改为系统样式...");

        //更新UI
        try {

            FlatLightLaf.install();

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

        if(CTInfo.isError){
            Log.info.loading.updateDialog("窗口加载", "骇客已强制介入加载过程");
            Log.err.systemPrint(SwingRun.class, "程序加载出错!", new Exception("加载异常"));

            Log.info.loading.showDialog("修复", "正在启动修复程序...");

            Log.info.loading.updateDialog("修复", "开始扫描程序文件...");
            {
                AtomicInteger count = new AtomicInteger(0);
                Path appPath = Path.of(GetPath.getAppPath(GetPath.APPLICATION_PATH));
                long fileCount = Files.walk(appPath)
                        .sorted()
                        .map(Path::toFile)
                        .count();

                if (fileCount > 0) {
                    Files.walk(appPath)
                            .sorted()
                            .map(Path::toFile)
                            .forEach(file -> {
                                int currentCount = count.incrementAndGet();
                                double percentage = (currentCount * 100.0) / fileCount;
                                int progress = (int) Math.round(percentage);
                                Log.info.loading.updateDialog("修复",
                                        String.format("开始扫描程序文件%.2f%%", percentage),
                                        progress);
                            });
                }
            }

            Log.info.loading.updateDialog("修复", "正在扫描数据文件...");
            {
                AtomicInteger count = new AtomicInteger(0);
                Path dataPath = Path.of(CTInfo.TEMP_PATH).getParent();
                long fileCount = Files.walk(dataPath)
                        .sorted()
                        .map(Path::toFile)
                        .count();

                if (fileCount > 0) {
                    Files.walk(dataPath)
                            .sorted()
                            .map(Path::toFile)
                            .forEach(file -> {
                                int currentCount = count.incrementAndGet();
                                double percentage = (currentCount * 100.0) / fileCount;
                                int progress = (int) Math.round(percentage);
                                Log.info.loading.updateDialog("修复",
                                        String.format("开始扫描数据文件%.2f%%", percentage),
                                        progress);
                            });
                }
            }

            Log.info.loading.updateDialog("修复", "正在修复文件...", -1);
            {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {

                }
                Log.info.loading.updateDialog("修复", "修复出错!");
                Log.err.systemPrint(SwingRun.class, "修复出错", new Exception("Silver Wolf强制截停修复进程"));
            }

            Log.info.loading.updateDialog("修复", "修复出错,正在准备关闭程序", -1);
            Log.err.systemPrint(SwingRun.class, "骇客已入侵", new Exception("关闭程序时出现错误,无法修复"));

            Log.info.loading.showDialog("骇客已入侵", "正在修改修复程序");
            {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ignored) {

                }
                Log.info.loading.updateDialog("骇客已入侵", "已修改修复程序");
                Log.info.loading.closeDialog("骇客已入侵");
            }

            Log.info.loading.updateDialog("修复", "修复成功!正在启动程序", -1);
            {
                for (int i = 0; i < 100; i++) {
                    Log.info.loading.updateDialog("修复", i);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException ignored) {

                    }
                }
            }

            Log.info.loading.closeDialog("修复");
            Log.info.systemPrint("骇客已入侵", "这次能让我玩得开心点么？");
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
