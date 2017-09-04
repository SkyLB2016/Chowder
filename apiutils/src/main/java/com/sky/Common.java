package com.sky;

import com.sky.rxbus.RxBus;

/**
 * Created by sky on 16/5/10 下午3:50.
 * 公共的常量
 */
public class Common {

    public static final int NET_NULL = 5005;//数据为空
    public static final String NET_EMPTY = "未获取到数据";//
    public static final int NET_NOT_FOUND = 4004;//

    public static final int PAGESIZE = 10;// 每一页请求的数据
    public static final int LOGIN = 1001;//未登录

    public static final String ISFIRST = "isFirst";//第一次进入引导页为true
    public static final String USERINFO = "userInfo";//用户全部信息
    public static final String TOKEN = "token";//标识
    public final static String PHONE = "phone";//请求手机号码
    public final static String CAPTCHA = "captcha";//短信验证码
    public final static String PWD = "pwd";

    public static final String EXTRA = "extra";//默认的传输字段

    public static final String PIC_IMG = "pic.jpg";

    public static final String SERVICETEL = "4000914113";//客服电话

    public static final String CITY = "city";
    public static final String CITY_DEFAULT = "北京市";

    //0."待抢单";1:"已经抢单";2:"到达目的地";3:"暂无此状态";4:"开始装车";5:"装车完成";6:"开始卸车"；7：卸车完成；8支付完成；
    public static final String ORDERNO = "orderNo";
    public static final String ORDERSTATUS = "Orderstatus";

    public static final String BASE_URL = "https://services.banyunbang.com.cn/";//服务器请求地址
    public static final String IMAGE_URL = "http://mg.banyunbang.com.cn/";
    public static final String TEST_BASE_URL = "http://test.services.banyunbang.com.cn/";//测试请求地址
    public static final String TEST_IMAGE_URL = "http://test.mg.banyunbang.com.cn/";//图片请求地址
    //版本控制
    public static final String VERSION = "api_v3";

    public static Boolean DEBUG = true;//测试true,正式false

    public static String getUrl() {
        return DEBUG ? TEST_BASE_URL : BASE_URL;
    }

    public static String getImageUrl() {
        return DEBUG ? TEST_IMAGE_URL : IMAGE_URL;
    }

    /**
     * 事件总线
     *
     * @return
     */
    public static RxBus getRxBus() {
        return RxBus.getInstance();
    }
}
