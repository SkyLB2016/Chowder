package com.sky.oa.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * Created by SKY on 2018/5/8 11:33.
 */
public class TestSerivice extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    boolean flag = true;

    class work implements Runnable {

        @Override
        public void run() {
            while (flag) {

            }
        }
    }

}
