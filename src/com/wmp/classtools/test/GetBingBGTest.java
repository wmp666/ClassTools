package com.wmp.classTools.test;

import com.wmp.PublicTools.io.DownloadURLFile;

import java.io.File;

public class GetBingBGTest {
    public static void main(String[] args) throws Exception {
        File file = new File(System.getenv("USERPROFILE"), "DeskTop");
        System.out.println(file);
        System.out.println(DownloadURLFile.downloadWebFile(null, null, "https://bing.img.run/uhd.php", file.getAbsolutePath()));
    }
}
