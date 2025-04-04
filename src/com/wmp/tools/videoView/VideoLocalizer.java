package com.wmp.tools.videoView;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class VideoLocalizer {
    private static String EMBEDDED_VIDEO_PATH = "/video";
    
    public static void copyEmbeddedVideo(String targetDir, String videoName) {
        try (InputStream is = VideoLocalizer.class.getResourceAsStream(EMBEDDED_VIDEO_PATH + "/" + videoName)) {
            if (is == null) {
                throw new IOException("内置视频文件未找到");
            }
            
            Files.createDirectories(Paths.get(targetDir, "video"));
            Files.copy(is, 
                Paths.get(targetDir, "video/"+ videoName),
                StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("视频本地化失败: " + e.getMessage());
        }
    }
}
