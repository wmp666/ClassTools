package com.wmp;

import com.wmp.PublicTools.StartupParameters;
import com.wmp.PublicTools.update.GetNewerVersion;
import com.wmp.classTools.frame.EasterEgg;
import com.wmp.classTools.frame.LoadingWindow;
import com.wmp.classTools.frame.MainWindow;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class Main{

    public static String DataPath = "null";

    public static String TempPath = "null";

    public static String version = "1.10.1";

    public static ArrayList<String> list = new ArrayList<>();

    public static final TreeMap<String, StartupParameters> allArgs = new TreeMap<>();

    static {


        allArgs.put("TimeView:screen", StartupParameters.creative("-TimeView:screen", "/TimeView:screen"));
        allArgs.put("StartUpdate:false", StartupParameters.creative("-StartUpdate:false", "/StartUpdate:false"));
        allArgs.put("EasterEgg:", StartupParameters.creative("-EasterEgg:", "/EasterEgg:"));
        allArgs.put("screenProduct:show", StartupParameters.creative("/s", "-s"));
        allArgs.put("screenProduct:view", StartupParameters.creative("/p", "-p"));
        //allArgs.put("", StartupParameters.creative("-EasterEgg-pin:nj02", "/EasterEgg-pin:nj02"));
    }
    public static void main(String[] args) throws IOException, URISyntaxException {


        //加载颜色(CTColor)数据
        //判断当前时间是否是4月1日
        // 明确指定时区
        LocalDate currentDate = LocalDate.now(ZoneId.of("Asia/Shanghai"));
        boolean b = currentDate.getMonth() == Month.APRIL
                && currentDate.getDayOfMonth() == 1;
        if (b){

        }


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

        String path = System.getenv("LOCALAPPDATA");

        StringBuilder sb = new StringBuilder();
        DataPath = sb.append(path).append("\\ClassTools\\").toString();

        StringBuilder sb2 = new StringBuilder();

        TempPath = sb2.append(path).append("\\ClassToolsTemp\\").toString();

        //System.out.println(sb);
        LoadingWindow loadingWindow = new LoadingWindow();

        loadingWindow.setVisible(true);

        if (!(allArgs.get("StartUpdate:false").contains(list) ||
                allArgs.get("screenProduct:show").contains(list) ||
                allArgs.get("screenProduct:view").contains(list))) {
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
        DataPath = sb.toString();

        new MainWindow(DataPath);
        loadingWindow.setVisible(false);

    }
}