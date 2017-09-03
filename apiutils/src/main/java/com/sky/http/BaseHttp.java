package com.sky.http;

import com.sky.Common;
import com.sky.utils.LogUtils;
import com.sky.utils.TextUtil;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SKY on 2017/6/1.
 * 网络请求类
 */
public class BaseHttp {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private final static int REQUEST_TIME_OUT = 15; // 请求超时
    private final static int RESPONSE_TIME_OUT = 15; // 响应超时

    protected Retrofit retrofit;
    protected OkHttpClient client;

    /**
     * 获取retrofit
     */
    protected void initRetrofit() {
        if (retrofit != null) return;
        retrofit = new Retrofit.Builder()
                .baseUrl(Common.getUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    /**
     * 初始化OkHttpClient
     */
    protected void initClient() {
        if (client != null) return;
        client = new OkHttpClient.Builder()
                .connectTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(RESPONSE_TIME_OUT, TimeUnit.SECONDS)
//                .addInterceptor(mLoggingInterceptor)//与图片上传会有冲突
                .build();
    }

    /**
     * 打印请求信息
     */
    protected static Interceptor mLoggingInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            //是否打印请求信息
            if (!Common.DEBUG)
                return chain.proceed(request);

            //请求的链接和请求方法
//            XLog.i("请求链接：%s", request.url());
//            XLog.i("请求方法：%s", request.method());

            //请求参数
            String requestParams;
            try {
                final Request cloneRequest = request.newBuilder().build();
                final Buffer buffer = new Buffer();
                if (cloneRequest.body() != null) {
                    cloneRequest.body().writeTo(buffer);
                    requestParams = buffer.readUtf8();
                    requestParams = URLDecoder.decode(requestParams, Charset.defaultCharset().name());
                } else {
                    requestParams = "无法识别";
                }
            } catch (final IOException e) {
                requestParams = "无法识别";
            }

            /**
             * ====================请求所耗费时间====================
             */
            long startNs = System.nanoTime();
            Response response = chain.proceed(request);
            long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

            /**
             * ====================服务器响应的信息====================
             */
            ResponseBody responseBody = response.body();
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            String responseStr = buffer.clone().readString(charset);
            if (responseBody.contentLength() != 0) {
                responseStr = responseStr.replaceAll(" ", "");
                LogUtils.i(TextUtil.stripHtml(responseStr));
            }
            return response;
        }
    };
}
