package com.wmp.classTools.infSet.DataStyle;

import com.alibaba.excel.annotation.ExcelProperty;


public class AllStu extends BasicData{

    @ExcelProperty("姓名")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String[] getData() {
        return new String[]{name};
    }
}
