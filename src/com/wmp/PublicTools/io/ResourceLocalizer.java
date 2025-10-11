package com.wmp.PublicTools.io;

import com.wmp.PublicTools.printLog.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ResourceLocalizer {
    // 将内置文件复制到指定目录
    public static void copyEmbeddedFile(String outputPath, String inputPath, String fileName) {
        File file = new File(outputPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        try (InputStream is = ResourceLocalizer.class.getResourceAsStream(inputPath + fileName)) {// 获取资源流
            if (is == null) {
                Log.err.print(ResourceLocalizer.class, "内置文件[" + ResourceLocalizer.class.getResource(inputPath + fileName) + "]未找到");
                return;
            }

            Files.createDirectories(Paths.get(inputPath, "video"));
            Files.copy(is,
                    Paths.get(outputPath, fileName),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            Log.err.print(ResourceLocalizer.class, "文件[" + fileName + "]本地化失败", e);
        }
    }

    public static void copyWebFile(String outputPath, String webPath, String fileName) {

        File file = new File(outputPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File targetFile = new File(outputPath + "/" + fileName);
        if (targetFile.exists()) {
            return;
        }
        Log.info.print("ResourceLocalizer-下载文件", "开始下载文件:" + webPath);
        DownloadURLFile.downloadWebFile(null, null, webPath, outputPath);
    }
}
