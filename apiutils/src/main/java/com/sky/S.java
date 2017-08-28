package com.sky;

import android.content.Context;

/**
 * Created by SKY on 2017/8/23.
 */
public class S {
    private static Context context;

    public static void init(Context con) {
        context = con.getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
