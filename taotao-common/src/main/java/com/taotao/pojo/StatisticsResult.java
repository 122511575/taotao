package com.taotao.pojo;

import java.io.Serializable;

public class StatisticsResult implements Serializable{
    private String Name;
    private Integer value;

    @Override
    public String toString() {
        return "StatisticsResult{" +
                "Name='" + Name + '\'' +
                ", value=" + value +
                '}';
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
