package com.sky.demo.model;

import java.io.Serializable;

public class ImageFloder implements Serializable {
    private String dirPath;//文件夹的路径
    private String firstImagePath;//文件夹中的第一张图片，用于显示
    private String name;//文件夹得名称
    private int count;//文件夹内包含的图片数量

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
        int last = dirPath.lastIndexOf("/");
        this.name = dirPath.substring(last+1);
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
