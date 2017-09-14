package com.sky;

import com.sky.rxbus.RxBus;

/**
 * Created by SKY on 16/5/10 下午3:50.
 * 公共的常量
 */
public class Common {

    public static final int NET_NULL = 5005;//数据为空
    public static final String NET_EMPTY = "未获取到数据";//
    public static final int NET_NOT_FOUND = 4004;//

    public static final String ISFIRST = "isFirst";//第一次进入引导页为true
    public static final String USERINFO = "userInfo";//用户全部信息
    public static final String TOKEN = "token";//标识
    public final static String PHONE = "phone";//请求手机号码
    public final static String CAPTCHA = "captcha";//短信验证码
    public final static String PWD = "pwd";

    public static final String EXTRA = "extra";//默认的传输字段

    //事件总线
    public static RxBus getRxBus() {
        return RxBus.getInstance();
    }
}