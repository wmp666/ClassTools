package com.wmp.classTools.infSet.tools;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.wmp.PublicTools.io.IOStreamForInf;
import com.wmp.classTools.infSet.DataStyle.BasicData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetExcelData <T extends BasicData>{
    public void getExcelData(String filePath, File dataPath, Class<?> clazz) {

        ExcelReaderBuilder read = EasyExcel.read(filePath, clazz, new ExcelListener<T>());

        // 读取一个sheet
        List<T> userList = read.head(clazz)
                .sheet()
                .doReadSync();

        /*List<T> userList = EasyExcel.read(filePath)
                .head(clazz)    // 绑定实体类
                .sheet()            // 默认读取第一个 Sheet
                .doReadSync();      // 同步读取（适合小文件）*/

        // 遍历数据
        ArrayList<String> allStu = new ArrayList<>();
        //String[] allStu = new String[userList.size()];
        userList.forEach(data -> {
            System.out.println(Arrays.toString(data.getData()));
            ArrayList<String> tempList = new ArrayList<>();
            for (int i = 0; i < data.getData().length; i++) {
                if (data.getData()[i] != null){
                    if (!data.getData()[i].equals("null")){
                        tempList.add(data.getData()[i]);
                    }
                }
            }
            if (!tempList.contains("null") && !tempList.isEmpty()){
                if (tempList.size() == 1){
                    allStu.add(userList.indexOf(data), tempList.get(0));
                } else if (tempList.size() > 1) {
                    StringBuilder temp = new StringBuilder();
                    for (String s : tempList) {
                        temp.append("[").append(s).append("]");
                    }
                    allStu.add(userList.indexOf(data), temp.toString());
                }

            }

        });
        try {
            //将allStu转换为string[]

            new IOStreamForInf(dataPath).SetInf(allStu.toArray(new String[0]));

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
