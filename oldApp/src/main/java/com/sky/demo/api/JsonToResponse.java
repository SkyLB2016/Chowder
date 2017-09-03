package com.sky.demo.api;

import com.google.gson.Gson;
import com.sky.demo.utils.LogUtils;

import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

import java.lang.reflect.Type;

public class JsonToResponse implements ResponseParser {
    @Override
    public void checkResponse(UriRequest request) throws Throwable {
//        LogUtils.i(request+"");
    }

    @Override
    public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {
        LogUtils.i(result+"");
         new Gson().fromJson(result,resultType);
        return new Gson().fromJson(result,resultType);
    }
}
