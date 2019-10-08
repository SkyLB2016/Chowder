package com.sky.chowder.ui.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.Nullable;

import com.sky.SkyApp;
import com.sky.sdk.utils.LogUtils;

/**
 * Created by SKY on 2017/9/22 10:46.
 */
public class IntentTest extends IntentService {

    public static void startIntent(Context context,  String action) {
        Intent intent = new Intent(context, IntentTest.class);
        intent.setAction(action);
        intent.putExtra("jj", "lkjlkjlk");
        context.startService(intent);
    }

    public IntentTest() {
        super("IntentTest");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SkyApp.getInstance().showToast("intent.getAction().toString()");
        LogUtils.i(intent.getAction());
        LogUtils.i(intent.getStringExtra("jj"));

    }
}
