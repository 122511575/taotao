package com.taotao.pojo;

import java.io.Serializable;

public class TbItemParamKey implements Serializable{
    private Integer id;
    private String paramName;
    private Integer groupId;
    private TbItemParamGroup paramGroup;
    private TbItemParamValue tbItemParamValue;

    public TbItemParamValue getTbItemParamValue() {
        return tbItemParamValue;
    }

    public void setTbItemParamValue(TbItemParamValue tbItemParamValue) {
        this.tbItemParamValue = tbItemParamValue;
    }

    @Override
    public String toString() {
        return "TbItemParamKey{" +
                "id=" + id +
                ", paramName='" + paramName + '\'' +
                ", groupId=" + groupId +
                ", paramGroup=" + paramGroup +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public TbItemParamGroup getParamGroup() {
        return paramGroup;
    }

    public void setParamGroup(TbItemParamGroup paramGroup) {
        this.paramGroup = paramGroup;
    }
}
