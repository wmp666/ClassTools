package com.wmp.classTools.test;

import com.wmp.PublicTools.CTInfo;
import com.wmp.classTools.extraPanel.classForm.CFInfoControl;

import java.util.Arrays;

public class CFInfoControlTest {
    public static void main(String[] args) {
        CTInfo.init();


        System.out.println(Arrays.toString(CFInfoControl.getNowClasses()));
        System.out.println(CFInfoControl.getNextClass());
    }
}
