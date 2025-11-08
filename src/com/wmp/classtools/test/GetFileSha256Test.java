package com.wmp.classTools.test;

import com.wmp.PublicTools.io.FileChecksum;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class GetFileSha256Test {
    //ed281fae3340f835672a4a2bc533dc680324ccc9e798c9389828ebc5bde172f6
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入文件路径：");
        String path = sc.nextLine();
        System.out.println(FileChecksum.calculateSHA256(new File(path)));
    }
}
