package com.sky.chowder.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.sky.utils.GsonUtils;

import java.io.Serializable;
import java.util.Objects;

public class ActivityEntity implements Serializable, Comparable<ActivityEntity>, Cloneable {
    private static final long serialVersionUID = -7780617194676472734L;
    @Expose
    private String className;
    @Expose
    private String describe;
    @Expose
    private String componentName;
    private int img;

    public ActivityEntity() {
    }

    public ActivityEntity(String className, String describe, String componentName, int img) {
        this.className = className;
        this.describe = describe;
        this.componentName = componentName;
        this.img = img;
    }

    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityEntity that = (ActivityEntity) o;
        return img == that.img &&
                Objects.equals(className, that.className) &&
                Objects.equals(describe, that.describe) &&
                Objects.equals(componentName, that.componentName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, describe, componentName, img);
    }

    @Override
    public int compareTo(@NonNull ActivityEntity o) {
        return className.compareTo(o.className);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        ActivityEntity entity = (ActivityEntity) super.clone();
        entity.className = this.className;
        entity.describe = this.describe;
        entity.componentName = this.componentName;
        entity.img = this.img;
        return entity;
    }
}
