package com.sky.chowder.utils.http

import com.sky.chowder.BuildConfig
import com.sky.chowder.C
import com.sky.utils.LogUtils
import com.sky.utils.StringUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.URLDecoder
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

/**
 * Created by SKY on 2017/6/1.
 * 网络请求类
 */
open class BaseHttp {

    protected var retrofit: Retrofit? = null
    protected var client: OkHttpClient? = null

    /**
     * 获取retrofit
     */
    protected fun initRetrofit() {
        if (retrofit != null) return
        retrofit = Retrofit.Builder()
                .baseUrl(C.url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client!!)
                .build()
    }

    /**
     * 初始化OkHttpClient
     */
    protected fun initClient() {
        if (client != null) return
        client = OkHttpClient.Builder()
                .connectTimeout(REQUEST_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(RESPONSE_TIME_OUT.toLong(), TimeUnit.SECONDS)
                //.addInterceptor(mLoggingInterceptor)//与图片上传会有冲突
                .build()
    }

    /**
     * 打印请求信息
     */
    protected var mLoggingInterceptor: Interceptor = Interceptor { chain ->
        val request = chain.request()

        //是否打印请求信息
        if (!BuildConfig.DEBUG)
            return@Interceptor chain.proceed(request)

        //请求的链接和请求方法
        //            XLog.i("请求链接：%s", request.url());
        //            XLog.i("请求方法：%s", request.method());

        //请求参数
        var requestParams: String
        try {
            val cloneRequest = request.newBuilder().build()
            val buffer = Buffer()
            if (cloneRequest.body() != null) {
                cloneRequest.body()!!.writeTo(buffer)
                requestParams = buffer.readUtf8()
                requestParams = URLDecoder.decode(requestParams, Charset.defaultCharset().name())
            } else {
                requestParams = "无法识别"
            }
        } catch (e: IOException) {
            requestParams = "无法识别"
        }

        /**
         * ====================请求所耗费时间====================
         */
        val startNs = System.nanoTime()
        val response = chain.proceed(request)
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        /**
         * ====================服务器响应的信息====================
         */
        val responseBody = response.body()
        val source = responseBody!!.source()
        source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
        val buffer = source.buffer()
        var charset: Charset? = UTF8
        val contentType = responseBody.contentType()
        if (contentType != null) {
            charset = contentType.charset(UTF8)
        }
        var responseStr = buffer.clone().readString(charset!!)
        if (responseBody.contentLength() != 0L) {
            responseStr = responseStr.replace(" ".toRegex(), "")
            LogUtils.i(StringUtils.stripHtml(responseStr))
        }
        response
    }

    companion object {
        private val UTF8 = Charset.forName("UTF-8")

        private const val REQUEST_TIME_OUT = 15 // 请求超时
        private const val RESPONSE_TIME_OUT = 15 // 响应超时
    }
}
