package com.sky;

/**
 * Created by sky on 16/5/10 下午3:50.
 * 公共的常量
 */
public class Common {
    //是否是第一次进程序的标志
    public static final String ISFIRST = "isFirst";//第一次进入引导页为true
    public static final String USERNAME = "userName";//用户名
    public static final String USERID = "userId";//用户id
    public static final String TOKEN = "token";//标识
    public final static String PHONE = "phone";//请求手机号码
    public static final String ISONLINE = "isOnline";//是否在线
    public final static String CAPTCHA = "captcha";//短信验证码
    public final static String PWD = "pwd";

    public static final String EXTRA = "extra";
    public static final int NET_REQUEST_ERR = -1;
    public static final int NET_REQUEST_NOT_FOUND = -2;
    public static final int LOGIN = 1001;

    public static final String DIR_PIC = "pic";//获取图片保存文件夹
    public static final String DIR_FILE = "file";//文件缓存保存文件夹
    public static final String DIR_LOG = "log";//log保存文件夹

    //0."待抢单";1:"已经抢单";2:"司机核对完成";3:"用户确认完成";4:"开始装车";5:"装车完成";6:"开始卸车"；7：卸车完成；8支付完成；
    public static final String ORDERSTATUS = "Orderstatus";
    public static final int WORK0 = 0;
    public static final int WORK1 = 1;
    public static final int WORK2 = 2;
    public static final int WORK3 = 3;
    public static final int WORK4 = 4;
    public static final int WORK5 = 5;
    public static final int WORK6 = 6;
    public static final int WORK7 = 7;
    public static final int WORK8 = 8;

    public static final String BASE_URL = "https://services.banyunbang.com.cn/";//服务器请求地址
    public static final String IMAGE_URL = "http://mg.banyunbang.com.cn/";
    public static final String TEST_BASE_URL = "http://test.services.banyunbang.com.cn/";//测试请求地址
    public static final String TEST_IMAGE_URL = "http://test.mg.banyunbang.com.cn/";//图片请求地址
    //版本控制
    public static final String VERSION = "api_v2";

    public static String getUrl() {
        return BuildConfig.DEBUG == true ? TEST_BASE_URL : BASE_URL;
//        return  Constants.BASE_URL;
    }

    public static String getImageUrl() {
        return BuildConfig.DEBUG == true ? TEST_IMAGE_URL : IMAGE_URL;
    }

}
