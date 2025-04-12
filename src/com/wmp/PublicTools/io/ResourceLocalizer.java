package com.wmp.PublicTools.io;

import com.wmp.PublicTools.videoView.VideoLocalizer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ResourceLocalizer {
    public static void copyEmbeddedVideo(String outputPath, String inputPath, String fileName) {
        File file = new File(outputPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        try (InputStream is = VideoLocalizer.class.getResourceAsStream(inputPath + fileName)) {// 获取资源流
            if (is == null) {
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
}
