package com.wmp.panel.duty.test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameExtractor {
    public static void main(String[] args) {
        String input = "{name1666,你好}-{name3,name4}";
        List<String> names = extractNames(input);
        System.out.println(names); // 输出: [name1, name2, name3, name4]
    }

    public static List<String> extractNames(String input) {
        List<String> result = new ArrayList<>();
        // 正则表达式匹配大括号内的内容 {
        Pattern pattern = Pattern.compile("\\{([^}]+)\\}");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            // 获取匹配到的内容（去掉大括号后的部分）
            String group = matcher.group(1);
            // 按逗号分割并添加到结果列表
            String[] names = group.split(",");
            for (String name : names) {
                result.add(name.trim()); // 使用trim()去除可能的空格
            }
        }
        return result;
    }
}
