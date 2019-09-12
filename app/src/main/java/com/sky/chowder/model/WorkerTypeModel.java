package com.sky.chowder.model;

/**
 * Created by libin on 2019/08/30 19:27 Friday.
 */
public class WorkerTypeModel {
    private String name;//种类
    private int value;//数量
    private String scale;//比例

    public WorkerTypeModel() {
    }

    public WorkerTypeModel(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }
}
