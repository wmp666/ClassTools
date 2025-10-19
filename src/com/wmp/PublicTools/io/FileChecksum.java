package com.wmp.PublicTools.io;

import com.wmp.PublicTools.printLog.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

/**
 * 文件校验和工具类，用于验证下载文件的完整性
 */
public class FileChecksum {
    
    /**
     * 计算文件的MD5校验和
     * 
     * @param file 要计算校验和的文件
     * @return 文件的MD5校验和字符串
     * @throws IOException 当文件读取出现问题时抛出
     */
    public static String calculateMD5(File file) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    md.update(buffer, 0, bytesRead);
                }
            }
            byte[] digest = md.digest();
            return new BigInteger(1, digest).toString(16);
        } catch (NoSuchAlgorithmException e) {
            Log.err.print(FileChecksum.class, "计算MD5校验和失败", e);
            throw new RuntimeException("无法获取MD5算法", e);
        }
    }
    
    /**
     * 计算文件的SHA-256校验和
     * 
     * @param file 要计算校验和的文件
     * @return 文件的SHA-256校验和字符串
     * @throws IOException 当文件读取出现问题时抛出
     */
    public static String calculateSHA256(File file) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    md.update(buffer, 0, bytesRead);
                }
            }
            byte[] digest = md.digest();
            return new BigInteger(1, digest).toString(16);
        } catch (NoSuchAlgorithmException e) {
            Log.err.print(FileChecksum.class, "计算SHA-256校验和失败", e);
            throw new RuntimeException("无法获取SHA-256算法", e);
        }
    }
    
    /**
     * 验证文件的MD5校验和
     * 
     * @param file 待验证的文件
     * @param expectedMD5 期望的MD5校验和
     * @return 如果文件的MD5与期望值一致返回true，否则返回false
     * @throws IOException 当文件读取出现问题时抛出
     */
    public static boolean verifyMD5(File file, String expectedMD5) throws IOException {
        String actualMD5 = calculateMD5(file);
        return actualMD5.equalsIgnoreCase(expectedMD5);
    }
    
    /**
     * 验证文件的SHA-256校验和
     * 
     * @param file 待验证的文件
     * @param expectedSHA256 期望的SHA-256校验和
     * @return 如果文件的SHA-256与期望值一致返回true，否则返回false
     * @throws IOException 当文件读取出现问题时抛出
     */
    public static boolean verifySHA256(File file, String expectedSHA256) throws IOException {
        String actualSHA256 = calculateSHA256(file);
        return actualSHA256.equalsIgnoreCase(expectedSHA256);
    }
}