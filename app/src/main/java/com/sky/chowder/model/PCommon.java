package com.sky.chowder.model;

/**
 * Created by libin on 2019/08/30 19:27 Friday.
 */
public class PCommon {
    private String name;
    private int value;//数量

    private int male;//男
    private int female;//女

    //人员走势图需要
    private int year;
    private int month;
    private int onjob;//在职
    private int join;//入职
    private int leave;//离职
    //人员走势图需要

    public String getDate() {
        return year + "." + month;
    }

    public PCommon() {
    }

    public PCommon(int value) {
        this.value = value;
    }

    public PCommon(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public PCommon(int year, int month, int onjob, int join, int leave) {
        this.year = year;
        this.month = month;
        this.onjob = onjob;
        this.join = join;
        this.leave = leave;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public int getMale() {
        return male;
    }

    public int getFemale() {
        return female;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getOnjob() {
        return onjob;
    }

    public int getJoin() {
        return join;
    }

    public int getLeave() {
        return leave;
    }
}
