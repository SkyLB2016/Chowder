package com.sky.chowder.model;

import java.io.Serializable;

/**
 * Created by sky on 15/12/9 下午8:54.
 * activity信息类
 */
public class ActivityModel implements Serializable, Comparable<ActivityModel> {
    private String className;//activity的名称
    private String describe;//activity的描述
    private int img;//代表图片
    private String componentName;//跳转所需

    public ActivityModel(String className, String describe, int img, String componentName) {
        this.className = className;
        this.describe = describe;
        this.img = img;
        this.componentName = componentName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    /**
     * Collections.sort(student)
     *
     * @param another
     * @return
     */
    @Override
    public int compareTo(ActivityModel another) {
        return className.compareTo(another.className);
    }
}
