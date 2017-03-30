package com.sky.chowder.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.sky.chowder.R;
import com.sky.chowder.ui.adapter.GuideContoler;
import com.sky.utils.SPUtils;

/**
 * Created by 李彬 on 2017/3/6.
 * 引导页面
 */

public class GuideActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//在v7主题下下无用
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initData();
    }

    /**
     * 使用写好的库初始化引导页面
     **/
    public void initData() {
        GuideContoler contoler = new GuideContoler(this);
        int[] imgIds = {R.mipmap.guide01, R.mipmap.guide02, R.mipmap.guide03};
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.pager_end, null);
        contoler.init(imgIds, view);
        view.findViewById(R.id.bt_begin).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        SPUtils.getInstance(GuideActivity.this).put("isfirst", false);
                        startActivity(new Intent(GuideActivity.this,
                                MainActivity.class));
                        overridePendingTransition(R.anim.in_from_right,
                                R.anim.out_to_left);
                        finish();
                    }
                });
    }
}
