package com.wmp.PublicTools.CTTool.dianMing;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.io.IOForInfo;

import java.io.IOException;
import java.nio.file.Path;

public class DianMingInfoControl {
    private static final String path = CTInfo.DATA_PATH + "CTTools\\DianMing\\";

    public static String[] getDianMingInfo() throws IOException {
        IOForInfo io = new IOForInfo(path + "DianMingInfo.txt");
        String[] info = io.getInfo();
        if (info == null || info.length == 0 || info[0].equals("err")) {
            return null;
        }
        return info;
    }

    public static void setDianMingInfo(String[] info, int count) throws IOException {
        IOForInfo io = new IOForInfo(path + "DianMingInfo.txt");
        io.setInfo(info);

        IOForInfo io2 = new IOForInfo(path + "Count.txt");
        io2.setInfo(String.valueOf(count));
    }

    public static void setDianMingNameList(String path) throws IOException {
        IOForInfo.copyFile(Path.of(path), Path.of(DianMingInfoControl.path));
    }

    public static void setCount(int count) throws IOException {
        IOForInfo io = new IOForInfo(path + "Count.txt");
        io.setInfo(String.valueOf(count));
    }

    public static int getCount(){
        try {
            IOForInfo io = new IOForInfo(path + "Count.txt");
            String infos = io.getInfos();

            if (infos == null || infos.equals("err")) {
                return 1;
            }
            return Integer.parseInt(infos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
