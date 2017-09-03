package com.sky.demo;

import android.os.Bundle;

import com.sky.demo.model.Student;
import com.sky.demo.ui.BaseActivity;
import com.sky.demo.utils.LogUtils;

/**
 * Created by IF on 16/5/11 下午8:31.
 */
public class ServiceActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService();
        bindService();
//        实体类测试
        Student p = new Student();
        p.setId(12);
        p.setName("name");
        LogUtils.i(p.toString());
    }
}
