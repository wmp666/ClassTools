package com.wmp.PublicTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StartupParameters {
    private ArrayList<String> parameterList = new ArrayList<>();

    public StartupParameters(String... parameters) {
        this.parameterList.addAll(List.of(parameters));
    }

    public static StartupParameters creative(String... parameters) {
        return new StartupParameters(parameters);
    }

    public ArrayList<String> getParameterList() {
        return parameterList;
    }

    public void setParameterList(ArrayList<String> parameterList) {
        this.parameterList = parameterList;
    }

    public boolean contains(String parameter) {
        return parameterList.contains(parameter);
    }

    public boolean contains(ArrayList<String> parameters) {
        for (String parameter : parameters) {
            if (parameterList.contains(parameter)) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "StartupParameters{" +
                "启动参数=" + parameterList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StartupParameters that)) return false;
        if (Objects.equals(getParameterList(), that.getParameterList())) return true;
        return getParameterList().contains(o);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getParameterList());
    }
}
