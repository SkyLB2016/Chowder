package com.sky.oa.model;

/**
 * Created by libin on 2020/04/15 4:19 PM Wednesday.
 */
public class A {
    public A() {
    }

    public A(String name) {
        this.name = name;
    }

    String name = getClass().getName();

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "A{" +
                "name='" + name + '\'' +
                '}';
    }
}
