package com.sky.demo.model;

import java.io.Serializable;
/**
 * Created by sky on 15/12/9 下午8:54.
 * 人的基本属性
 */
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String birthday;
    private transient int age;//transient不会进行jvm默认的序列化，要想序列化，必须自定义；writeObject与readObject
    private String gender;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthday='" + birthday + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                '}';
    }
}