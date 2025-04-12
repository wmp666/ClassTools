package com.wmp.classTools.infSet.tools;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.read.listener.ReadListener;


public class ExcelListener<T>  implements ReadListener<T> {
    // 读取excel内容
    @Override
    public void invoke(T data, AnalysisContext context) {

    }

    // 读取完成之后
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        //ReadListener.super.extra(extra, context);
        switch (extra.getType()) {
            case COMMENT:
                System.out.println("读取到一条评论数据：" + extra.getText());
                break;
            case HYPERLINK:
                System.out.println("读取到一条链接数据：" + extra.getText());
                break;
            case MERGE:
                System.out.printf("额外信息是合并单元格,在firstRowIndex:%s,firstColumnIndex:%s,lastRowIndex:%s,lastColumnIndex:%s\n",
                        extra.getFirstRowIndex(), extra.getFirstColumnIndex(), extra.getLastRowIndex(),
                        extra.getLastColumnIndex());
                break;
        }
    }
}
