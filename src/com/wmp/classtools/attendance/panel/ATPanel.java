package com.wmp.classtools.attendance.panel;

import com.wmp.classtools.CTComponent.CTPanel;
import com.wmp.io.IOStreamForInf;

import java.io.File;
import java.io.IOException;

public class ATPanel extends CTPanel {
    public ATPanel(int mixY, File AllStudentPath) throws IOException {
        super(mixY);
        setLayout(null);

        IOStreamForInf ioStreamForInf = new IOStreamForInf(AllStudentPath);

        String inf = ioStreamForInf.GetInf();

        //System.out.println(inf);
        if(inf.isEmpty() || inf.equals("error")){
            //将数据改为默认-空,需要用户自行输入数据

            ioStreamForInf.SetInf("""
                    曾世通
                    陈昌焱
                    陈权浩
                    陈思源
                    程政
                    陈睿智
                    范祖轩
                    """);
        }else{

        }



    }
}
