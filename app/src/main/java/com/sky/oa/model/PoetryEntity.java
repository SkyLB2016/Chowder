package com.sky.oa.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by libin on 2019/12/20 12:42 Friday.
 */
public class PoetryEntity implements Serializable, Cloneable {
    private String name;
    private String format = ".txt";
    private String path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @NonNull
    @Override
    public String toString() {
        return "名称==" + name + "；地址==" + path + "；";
    }
}
