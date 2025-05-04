package com.wmp.classTools.extraPanel.duty.type;

import java.util.ArrayList;
import java.util.Arrays;

public class DutyDay {

    private ArrayList<String> ClFloorList;

    private ArrayList<String> ClBlackBroadList;

    public DutyDay() {
    }

    public DutyDay(ArrayList<String> clFloorList, ArrayList<String> clBlackBroadList) {
        ClFloorList = clFloorList;
        ClBlackBroadList = clBlackBroadList;
    }


    public ArrayList<String> getClFloorList() {
        return ClFloorList;
    }

    public void setClFloorList(ArrayList<String> clFloorList) {
        ClFloorList = clFloorList;
    }

    //重载setClFloor
    public void setClFloor(String... people) {
        ClFloorList = setDutyPersonList(people);

    }

    public ArrayList<String> getClBlackBroadList() {
        return ClBlackBroadList;
    }

    public void setClBlackBroadList(ArrayList<String> clBlackBroadList) {
        ClBlackBroadList = clBlackBroadList;
    }

    public void setClBlackBroad(String... people) {
        ClBlackBroadList = setDutyPersonList(people);
    }

    public static ArrayList<String> setDutyPersonList(String... people){
        return new ArrayList<>(Arrays.asList(people));
    }

    @Override
    public String toString() {
        return "DutyDay{" +
                "ClFloorList=" + ClFloorList +
                ", ClBlackBroadList=" + ClBlackBroadList +
                '}' + "\n";
    }

}
