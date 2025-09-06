package com.wmp.classTools.test;

import com.nlf.calendar.Lunar;

import java.util.Date;

public class UseLunar {
    public static void main(String[] args) {
        Lunar lunar = Lunar.fromDate(new Date());
        System.out.printf("%s月%s日", lunar.getMonth(), lunar.getDay());
    }
}
