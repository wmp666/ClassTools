package com.wmp.PublicTools;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfProcess {
    //处理duty.txt中的内容-三
    public static ArrayList<String[]> RDExtractNames(String input) {
        ArrayList<String[]> result = new ArrayList<>();
        // 正则表达式匹配大括号内的内容 {
        Pattern pattern = Pattern.compile("\\[([^]]+)");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            // 获取匹配到的内容（去掉大括号后的部分）
            String group = matcher.group(1);
            // 按逗号分割并添加到结果列表
            String[] names = group.split(",");
            result.add(names);
        }
        return result;
    }


    //处理duty.txt中的内容-二
    public static ArrayList<String> NDExtractNames(String input) {
        ArrayList<String> result = new ArrayList<>();
        // 正则表达式匹配大括号内的内容 {
        Pattern pattern = Pattern.compile("\\[([^]]+)");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            // 获取匹配到的内容（去掉大括号后的部分）
            String group = matcher.group(1);
            result.add(group);
        }
        return result;
    }
}
