package com.glimmer.carrybport.model.requestparams;

/**
 * Created by sky on 2017/6/7.
 */
public class LoginParams {
    private String tel;
    private String pwd;
    private String code;

    public LoginParams(String tel, String pwd) {
        this.tel = tel;
        this.pwd = pwd;
    }
}
