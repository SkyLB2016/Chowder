package com.sky.demo.utils.http;

import com.google.gson.JsonObject;
import com.sky.demo.api.IDataResultImpl;
import com.sky.demo.api.RequestCallBack;
import com.sky.demo.model.ApiResponse;
import com.sky.demo.model.CouponBO;
import com.sky.demo.model.CourseEntity;
import com.sky.demo.model.Person;
import com.sky.demo.utils.LogUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * @author sky
 * @ClassName: DensityUtils
 * @Description: TODO 数据请求类，待修改
 * @date 2015年4月12日 下午1:33:47
 */
public class HttpDataUtils extends HttpUtilsBase {

    public static void getMuke(final IDataResultImpl<List<CourseEntity>> callback) {
        RequestParams params = new RequestParams("http://www.imooc.com/api/teacher?type=4&num=30");
        x.http().post(params,
                new RequestCallBack<ApiResponse<List<CourseEntity>>>(callback) {
                    @Override
                    public void onSuccess(ApiResponse<List<CourseEntity>> result) {
                        callback.onSuccessData(result.getData());
                    }

                });
    }

    public static RequestHandler getData(final IDataResultImpl<List<CouponBO>> callback) {
        RequestParams params = getRequestParams();
        params.addBodyParameter("pageSize", "20");
        params.addBodyParameter("method", "issue.listNewCoupon");
        params.addBodyParameter("currentPage", "1");
        params.addBodyParameter("appKey", "ANDROID_KCOUPON");
//        params.addHeader();
        // 请求
        final Callback.Cancelable request = x.http().post(params,
                new RequestCallBack<ApiResponse<List<CouponBO>>>(callback) {
                    @Override
                    public void onSuccess(ApiResponse<List<CouponBO>> result) {
                        callback.onSuccessData(result.getData());
                    }

                });
        // 处理handler
        RequestHandler handler = new RequestHandler() {
            @Override
            public void cancel() {
                request.cancel();
            }
        };
        return handler;
    }

    /**
     * 登录接口
     *
     * @param callback
     */
    public static void login(final IDataResultImpl<Person> callback) {
        RequestParams params = new RequestParams("http://172.16.0.142:8080/qdb.server/api/login");
        JsonObject json = new JsonObject();
        json.addProperty("loginId", "13802036201");
        json.addProperty("loginType", 1);
        json.addProperty("password", "e10adc3949ba59abbe56e057f20f883e");

        params.setBodyContent(json.toString());
        params.addHeader("Content-Type", "application/json");
        params.addHeader("accessToken", "111");
        x.http().post(params, new RequestCallBack<ApiResponse<Person>>(callback) {
            @Override
            public void onSuccess(ApiResponse<Person> result) {
                callback.onSuccessData(result.getObj());
            }
        });
    }

    /**
     * 登录接口
     *
     * @param callback
     */
    public static void getPM2(final IDataResultImpl<String> callback) {
        RequestParams params = new RequestParams("http://apis.baidu.com/apistore/aqiservice/aqi");
//        JsonObject json = new JsonObject();
//        json.addProperty("city", "北京");
//
//        params.setBodyContent(json.toString());
        params.setCharset("gbk");
        params.addHeader("charset", "GBK");
//        params.addHeader("Content-Type", "text/html");

        params.addHeader("apikey", "ebbd02e7435f1151a5d78a236500fa65");

        params.addBodyParameter("city", "北京");
        x.http().get(params, new RequestCallBack<String>(callback) {
            @Override
            public void onSuccess(String result) {
                LogUtils.i("result==" + result);
                callback.onSuccessData(result);
            }
        });
    }
}