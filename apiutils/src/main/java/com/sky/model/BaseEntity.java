package com.sky.model;

import java.io.Serializable;

/**
 * Created by SKY on 2017/5/29.
 */
public class BaseEntity implements Serializable {
    private boolean success;
    private String msg;
    private int code;
    private int status;

    public boolean isSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }
}
