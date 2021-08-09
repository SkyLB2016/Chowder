package com.sky.oa.model;

import java.io.Serializable;

public class IdName implements Serializable {

    private String id;
    private String name;
    private String age;
//    public byte[] bytes=new byte[10*1024*1024];
//    public byte[] bytes=new byte[100];

    {
        age = "AGE";
    }

    public IdName() {
    }

    public IdName(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "IdName{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
