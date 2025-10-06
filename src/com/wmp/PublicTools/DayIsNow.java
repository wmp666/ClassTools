package com.wmp.PublicTools;

import com.nlf.calendar.Lunar;
import com.nlf.calendar.Solar;
import com.wmp.PublicTools.printLog.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DayIsNow {
    /**
     * 判断当前时间是否是目标时间
     *
     * @param targetDate 目标时间,格式为MM-dd
     * @return true:是目标时间
     */
    public static boolean dayIsNow(String targetDate) {
        if (targetDate.startsWith("lunar")) {
            Lunar lunar = Lunar.fromDate(new Date());

            String[] split = targetDate.substring(5).split("-");
            int targetMouth = Integer.parseInt(split[0]);
            int targetDay = Integer.parseInt(split[1]);

            return (targetMouth == Math.abs(lunar.getMonth()) && targetDay == lunar.getDay());
        } else {
            DateFormat dateFormat = new SimpleDateFormat("MM-dd");
            String date = dateFormat.format(new Date());
            return targetDate.equals(date);
        }
    }

    /**
     * 获取两个时间间隔天数
     *
     * @param targetDate 目标时间,格式为(lunar)MM-dd,"lunar"为农历的意思
     * @return 间隔天数
     */
    public static int getRemainderDay(String targetDate) {


        int day = 0;
        //获取今年年份 - 公历
        Solar solar = Solar.fromDate(new Date());
        try {
            if (targetDate.startsWith("lunar")) {
                //获取今天的农历日期
                //1.分割目标时间 - 农历
                String[] split = targetDate.substring(5).split("-");
                int targetMonth = Integer.parseInt(split[0]);
                int targetDay = Integer.parseInt(split[1]);
                //2.获取今年年份 - 农历
                int lunarYear = Lunar.fromDate(new Date()).getYear();
                //3.获取农历,并转成公历
                Lunar lunar = Lunar.fromYmd(lunarYear, targetMonth, targetDay);
                Solar targetSolar = lunar.getSolar();
                //计算间隔时间
                day = targetSolar.subtract(solar);


            } else {

                //获取目标时间 - 公历
                //1.分割目标时间
                String[] split = targetDate.split("-");
                int targetMonth = Integer.parseInt(split[0]);
                int targetDay = Integer.parseInt(split[1]);
                //2.获取目标时间
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, targetMonth - 1);
                calendar.set(Calendar.DAY_OF_MONTH, targetDay);
                Solar targetSolar = Solar.fromCalendar(calendar);

                //获取间隔天数
                day = targetSolar.subtract(solar);

            }
        } catch (Exception e) {
            Log.err.print(DayIsNow.class, "获取目标时间失败: \n" + e.getMessage());
            throw new RuntimeException(e);
        }

        return day;
    }

    /**
     * 判断当前时间是否在指定时间段内
     *
     * @param beginTime 开始时间, 格式为HH:mm
     * @param endTime   结束时间, 格式为HH:mm
     * @return true:在指定时间段内
     */
    public static boolean isInTimePeriod(String beginTime, String endTime) {

        long i = getRemainderTime(beginTime);
        return i <= 0 && getRemainderTime(endTime) >= 0;

    }

    /**
     * 获取两个时间间隔时间
     *
     * @param targetTime 目标时间,格式为HH:mm
     * @return 间隔时间
     */
    public static long getRemainderTime(String targetTime) {
        long time = 0;

        if (targetTime == null || targetTime.isEmpty()) return time;

        if (targetTime.startsWith("lunar")) {
            Log.err.print(DayIsNow.class, "获取目标时间失败: \n" + "不支持农历");
        }
        try {

            //1.分割目标时间
            String[] split = targetTime.split(":");
            int targetHour = Integer.parseInt(split[0]);
            int targetMin = Integer.parseInt(split[1]);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, targetHour);
            calendar.set(Calendar.MINUTE, targetMin);

            time = calendar.getTime().getTime() - new Date().getTime();
        } catch (Exception e) {
            Log.err.print(DayIsNow.class, "获取目标时间失败: \n" + e.getMessage());
            throw new RuntimeException(e);
        }
        return time;
    }
}
