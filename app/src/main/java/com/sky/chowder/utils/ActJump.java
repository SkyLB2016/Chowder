package com.sky.chowder.utils;

import android.content.Context;
import android.support.annotation.Nullable;

import com.sky.Common;
import com.sky.utils.JumpAct;

import java.io.Serializable;

/**
 * Created by SKY on 2017/6/19.
 */

public class ActJump {
    private static void toActivity(Context context, Class<?> cls) {
        JumpAct.jumpActivity(context, cls);
    }

    private static void toActivity(Context context, Class<?> cls, @Nullable Serializable serial) {
        JumpAct.jumpActivity(context, cls, Common.EXTRA, serial);
    }


    public static void toWebActivity(Context context, String title, String url) {
//        JumpAct.jumpActivity(context, WebActivity.class,
//                Common.EXTRA, new Extra(title, url));
//        JumpAct.jumpActivity(context, WebActivity.class, "url", url, "title", title);
    }
}
