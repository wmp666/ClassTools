package com.wmp.classTools.CTComponent;

public abstract class CTSetsPanel extends CTPanel {

    private String name;
    //基础数据路径
    private String basicDataPath;

    public CTSetsPanel(String basicDataPath) {
        this.basicDataPath = basicDataPath;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getBasicDataPath() {
        return basicDataPath;
    }

    public void setBasicDataPath(String basicDataPath) {
        this.basicDataPath = basicDataPath;
    }

    /**
     * 获取信息每段数据用 \n 隔开
     *
     * @return String 信息
     */
    public abstract void save();


}
