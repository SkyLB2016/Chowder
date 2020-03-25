package com.sky.oa.model;

import java.io.File;

/**
 * Created by libin on 2020/3/22 22:59.
 */
public class PeopleEntity {

    private String name;
    private String gender;
    private String age;
    private String local;
    private File image;

    public PeopleEntity(String name, String gender, String age, String local) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.local = local;
    }

    public PeopleEntity(String name, String gender, File image) {
        this.name = name;
        this.gender = gender;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }
}
