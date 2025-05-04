package com.wmp.classTools.test;

import com.wmp.PublicTools.update.SslUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test01 {
    public static void main(String[] args) throws Exception {
        SslUtils.ignoreSsl();
        String url = "https://api.github.com/repos/wmp666/ClassTools/releases/latest";

        try {
            // 1. 获取网页内容
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0") // 设置UA
                    .timeout(3000)  // 超时设置
                    .ignoreContentType(true) // 忽略内容类型
                    .get();

            // 2. 解析内容
            String title = doc.title();
            System.out.println("网页标题: " + title);

            // 3. 提取所有链接
            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String absUrl = link.attr("abs:href");
                System.out.println("发现链接: " + absUrl);
            }

            Element body = doc.body();

            // 正则表达式匹配大括号内的内容 -\\[([^]]+)
            //"tag_name":"1.5.2"
            Pattern pattern = Pattern.compile("\"tag_name\":\"([^\"]+)");
            Matcher matcher = pattern.matcher(body.html());

            String result = "";
            while (matcher.find()) {
                result = matcher.group(1);
                //System.out.println("find");
            }
            System.out.println(result);

            System.out.println(body);
            //System.out.println(doc.body());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
