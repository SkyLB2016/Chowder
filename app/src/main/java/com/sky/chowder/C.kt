package com.sky.chowder

/**
 * Created by SKY on 2017/3/3.
 */
object C {
    const val handler_0x002 = 0x002
    const val handler_0x003 = 0x003

    val ACTION_NEW_VERSION = "apk.update.action"
    val ACTION_PUSH_DATA = "fm.data.push.action"
    val ACTION_MY = "android.intent.action.MYBROADCAST"

    //    val BASE_URL = "http://uat.b.quancome.com/platform/api"
    private const val TEST_BASE_URL = "http://test.services.banyunbang.com.cn/"//测试请求地址
    private const val TEST_IMAGE_URL = "http://test.mg.banyunbang.com.cn/"//图片请求地址
    //版本控制
    const val VERSION = "api_v3"

    val url: String
        get() = TEST_BASE_URL

    val imageUrl: String
        get() = TEST_IMAGE_URL
}
