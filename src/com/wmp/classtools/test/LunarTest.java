package com.wmp.classTools.test;

import com.nlf.calendar.Lunar;

import java.util.Date;

public class LunarTest {
    public static void main(String[] args) {
        Lunar lunar = Lunar.fromDate(new Date());

        String[] days = new String[]{"初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八", "初九", "初十",
                "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十",
                "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十"};
        String[] months = new String[]{"正月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};

        StringBuilder sb = new StringBuilder();

        if (lunar.getMonth() < 0) {
            sb.append("闰");
        }
        sb.append(months[lunar.getMonth() - 1])//月
                .append(days[lunar.getDay() - 1])//日
                .append(" ")
                .append(lunar.getYearInGanZhi())
                .append("[")
                .append(lunar.getYearShengXiao())
                .append("]年 ")
                .append(lunar.getJieQi());


        //农历 八月廿七 乙巳 [蛇] 年 大雪
        System.out.println( sb);
    }
}
