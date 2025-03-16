package com.wmp.panel.duty.type;

import java.util.ArrayList;
import java.util.Arrays;

public class DutyDay {
    private ArrayList<String> ClFloor;
    private ArrayList<String> ClBlackBroad;

    public DutyDay() {
    }

    public DutyDay(ArrayList<String> clFloor, ArrayList<String> clBlackBroad) {
        ClFloor = clFloor;
        ClBlackBroad = clBlackBroad;
    }


    public ArrayList<String> getClFloor() {
        return ClFloor;
    }

    public void setClFloor(ArrayList<String> clFloor) {
        ClFloor = clFloor;
    }

    //重载setClFloor
    public void setClFloor(String... people) {
        ClFloor = setDutyPersonList(people);

    }

    public ArrayList<String> getClBlackBroad() {
        return ClBlackBroad;
    }

    public void setClBlackBroad(ArrayList<String> clBlackBroad) {
        ClBlackBroad = clBlackBroad;
    }

    public void setClBlackBroad(String... people) {
        ClBlackBroad = setDutyPersonList(people);
    }

    public static ArrayList<String> setDutyPersonList(String... people){
        return new ArrayList<>(Arrays.asList(people));
    }

    @Override
    public String toString() {
        return "DutyDay{" +
                "ClFloor=" + ClFloor +
                ", ClBlackBroad=" + ClBlackBroad +
                '}' + "\n";
    }
}
