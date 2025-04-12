package com.wmp.classTools.infSet.DataStyle;

import com.alibaba.excel.annotation.ExcelProperty;

public class Temp extends BasicData{

    @ExcelProperty("www")
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

    @Override
    public String toString() {
        return "Temp{" +
                "name='" + name + '\'' +
                '}';
    }
}
