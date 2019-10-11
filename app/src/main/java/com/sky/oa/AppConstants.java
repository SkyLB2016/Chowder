package com.sky.oa;

import android.provider.SyncStateContract;

/**
 * Created by SKY on 16/5/10 下午3:50.
 * 公共的常量
 */
public class AppConstants {
    public final static int NET_NULL = 5005;//数据为空
    public final static String NET_EMPTY = "未获取到数据";//
    public final static int NET_NOT_FOUND = 4004;//

    public final static String ISFIRST = "isFirst";//第一次进入引导页为true
    public final static String USERINFO = "userInfo";//用户全部信息
    public final static String TOKEN = "token";//标识
    public final static String PHONE = "phone";//请求手机号码
    public final static String CAPTCHA = "captcha";//短信验证码
    public final static String PWD = "pwd";

    public final static String EXTRA = "extra";//默认的传输字段

//    public final static int SOLAR = 2001;
//
//    //    val BASE_URL = "http://uat.b.quancome.com/platform/api"
//    public final static String TEST_BASE_URL = "http://test.services.banyunbang.com.cn/";//测试请求地址
//    public final static String TEST_IMAGE_URL = "http://test.mg.banyunbang.com.cn/";//图片请求地址
//
//    public static String getUrl() {
//        return TEST_BASE_URL;
//    }
//
//    public static String getImageUrl() {
//        return TEST_IMAGE_URL;
//    }
}