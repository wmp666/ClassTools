package com.wmp.PublicTools.CTTool.dianMing;

import com.wmp.PublicTools.CTInfo;
import com.wmp.PublicTools.io.IOForInfo;

import java.io.IOException;
import java.nio.file.Path;

public class DianMingInfoControl {
    private static final String path = CTInfo.DATA_PATH + "CTTools\\DianMingInfo.txt";

    public static String[] getDianMingInfo() throws IOException {
        IOForInfo io = new IOForInfo(path);
        return io.getInfo();
    }

    public static void setDianMingInfo(String[] info) throws IOException {
        IOForInfo io = new IOForInfo(path);
        io.setInfo(info);
    }

    public static void setDianMingInfo(String path) throws IOException {
        IOForInfo.copyFile(Path.of(path), Path.of(DianMingInfoControl.path));
    }
}
