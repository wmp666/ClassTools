package com.wmp.PublicTools.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ResourceLocalizer {
    public static void copyEmbeddedFile(String outputPath, String inputPath, String fileName) {
        File file = new File(outputPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        try (InputStream is = ResourceLocalizer.class.getResourceAsStream(inputPath + fileName)) {// 获取资源流
            if (is == null) {
                System.out.println("内置文件:" + ResourceLocalizer.class.getResource(inputPath + fileName));
                throw new IOException("内置文件[" + fileName + "]未找到");
            }

            Files.createDirectories(Paths.get(inputPath, "video"));
            Files.copy(is,
                    Paths.get(outputPath, fileName),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("文件[" + fileName + "]本地化失败: " + e.getMessage());
        }
    }

    public static void copyWebVideo(String outputPath, String webPath, String fileName) {
        File file = new File(outputPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File targetFile = new File(outputPath + "/" + fileName);
        if (targetFile.exists()) {
            return;
        }
        System.out.println("开始下载视频:" + webPath);
        DownloadURLFile.downloadWebFile(null, null, webPath, outputPath);
    }
}
