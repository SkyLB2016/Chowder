package com.sky.chowder.ui.activity;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sky.chowder.R;
import com.sky.chowder.model.ActivityModel;
import com.sky.model.ApiResponse;
import com.sky.utils.GsonUtils;
import com.sky.utils.LogUtils;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by SKY on 2017/9/28 9:44.
 */
public class Test {
    public void ddd(Context context) {
        GsonUtils.jsonToList("",ActivityModel[].class);
    }
    public void dd(Context context) {
        toObj(context.getString(R.string.jsonlist), new TypeToken<ApiResponse<List<ActivityModel>>>() {
        }.getType());
        ApiResponse<List<ActivityModel>> model = GsonUtils.jsonToEntity(context.getString(R.string.jsonlist),
                 new TypeToken<ApiResponse<List<ActivityModel>>>() { }.getType());
        LogUtils.i(model.getObjList().get(3).getClassName());
    }

    private void toObj(String json, Type type) {
        ApiResponse<List<ActivityModel>> model = new Gson().fromJson(json, type);
        LogUtils.i(model.getObjList().get(4).getClassName());
    }
}
