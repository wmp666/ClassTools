package com.wmp.classTools.test;

import com.wmp.PublicTools.io.DownloadURLFile;
import com.wmp.PublicTools.web.GetWebInf;
import com.wmp.classTools.frame.ScreenProduct;

import java.io.File;
import java.io.IOException;

public class GetBingBGTest {
    public static void main(String[] args) throws Exception {
        File file = new File(System.getenv("USERPROFILE"), "DeskTop");
        System.out.println(file);
        System.out.println(DownloadURLFile.downloadWebFile(null, null, "https://bing.img.run/uhd.php", file.getAbsolutePath()));
    }
}
