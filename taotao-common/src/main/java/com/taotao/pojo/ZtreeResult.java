package com.taotao.pojo;

import java.io.Serializable;

public class ZtreeResult implements Serializable{
    private Long id;
    private String name;
    private Boolean isParent;

    @Override
    public String toString() {
        return "ZtreeResult{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isParent=" + isParent +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(Boolean isParent) {
        this.isParent = isParent;
    }
}
