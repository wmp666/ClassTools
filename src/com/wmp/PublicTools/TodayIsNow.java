package com.wmp.PublicTools;

import com.nlf.calendar.Lunar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TodayIsNow {
    public static boolean todayIsNow(String targetDate) {
        if (targetDate.startsWith("lunar")) {
            Lunar lunar = Lunar.fromDate(new Date());
            return targetDate.substring(5).equals(Math.abs(lunar.getMonth()) + "-" + lunar.getDay());
        } else {
            DateFormat dateFormat = new SimpleDateFormat("MM-dd");
            String date = dateFormat.format(new Date());
            return targetDate.equals(date);
        }
    }
}
