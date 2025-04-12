package com.wmp.classTools.infSet.DataStyle;

import com.alibaba.excel.annotation.ExcelProperty;

public class Duty extends BasicData{
    @ExcelProperty("扫地")
    private String clFloor;
    @ExcelProperty("擦黑板")
    private String clBlackBroad;

    public String getClFloor() {
        return clFloor;
    }

    public void setClFloor(String clFloor) {
        this.clFloor = clFloor;
    }

    public String getClBlackBroad() {
        return clBlackBroad;
    }

    public void setClBlackBroad(String clBlackBroad) {
        this.clBlackBroad = clBlackBroad;
    }

    @Override
    public String[] getData() {
        return new String[]{clFloor, clBlackBroad};
    }
}
