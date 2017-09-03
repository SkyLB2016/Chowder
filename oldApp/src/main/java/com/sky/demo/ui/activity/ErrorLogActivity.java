package com.sky.demo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.sky.demo.ui.BaseActivity;
import com.sky.utils.ActivityLifecycle;

/**
 * @author sky QQ:1136096189
 * @Description:
 * @date 15/11/22 下午5:16
 */
public class ErrorLogActivity extends BaseActivity {
    public static void startThis(String errorMsg) {
        Context context = ActivityLifecycle.getInstance().getCurrentActivity();
        Intent intent = new Intent(context, ErrorLogActivity.class);
        intent.putExtra("errorMsg", errorMsg);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tvContent = new TextView(this);
        tvContent.setText(getIntent().getStringExtra("errorMsg"));
        setContentView(tvContent);
    }

}
