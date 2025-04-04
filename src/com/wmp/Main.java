package com.wmp;

import com.wmp.classTools.frame.EasterEgg;
import com.wmp.classTools.frame.LoadingWindow;
import com.wmp.classTools.frame.MainWindow;
import com.wmp.tools.StartupParameters;
import com.wmp.tools.update.GetNewerVersion;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class Main extends Application {

    public static String version = "1.9.0";

    public static ArrayList<String> list = new ArrayList<>();

    public static final TreeMap<String, StartupParameters> allArgs = new TreeMap<>();

    static {

        allArgs.put("TimeView:screen", StartupParameters.creative("-TimeView:screen", "/TimeView:screen"));
        allArgs.put("StartUpdate:false", StartupParameters.creative("-StartUpdate:false", "/StartUpdate:false"));
        allArgs.put("EasterEgg:", StartupParameters.creative("-EasterEgg:", "/EasterEgg:"));
        allArgs.put("screenProduct:set", StartupParameters.creative("/s", "-s"));
        allArgs.put("screenProduct:show", StartupParameters.creative("/p", "-p"));
        //allArgs.put("", StartupParameters.creative("-EasterEgg-pin:nj02", "/EasterEgg-pin:nj02"));
    }
    public static void main(String[] args) throws IOException, URISyntaxException {

        try {
            //使用系统UI
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        //new JFXPanel();

        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].replace("/", "-");
        }
        System.out.println("程序支持的启动参数:" + allArgs);

        if (args.length > 0) {
            list = new ArrayList<>(Arrays.asList(args));
            System.out.println(list);
        }

        System.out.println("Hello, World!");
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        String path = System.getenv ("LOCALAPPDATA");

        StringBuilder sb = new StringBuilder();
        sb.append(path).append("\\ClassTools\\");

        //System.out.println(sb);
        LoadingWindow loadingWindow = new LoadingWindow();

        loadingWindow.setVisible(true);

        if (!(allArgs.get("StartUpdate:false").contains(list) ||
                allArgs.get("screenProduct:show").contains(list) ||
                allArgs.get("screenProduct:set").contains(list))) {
            //执行你的代码
            GetNewerVersion.checkForUpdate(
                    loadingWindow, null);
            System.out.println("-StartUpdate:true");
        }

        if (allArgs.get("EasterEgg:").contains(list)) {
            int i = list.indexOf("-EasterEgg:") + 1;
            System.out.println("-EasterEgg:" + list.get(i));
            //System.out.println();
            EasterEgg.show(list.get(i));
        }
        /*

        if (allArgs.get(5).contains(list)){
            System.out.println("-EasterEgg-pin:nj02");
            EasterEgg.show("nj02");
        }
*/


        new MainWindow(sb.toString());
        loadingWindow.setVisible(false);
    }
}