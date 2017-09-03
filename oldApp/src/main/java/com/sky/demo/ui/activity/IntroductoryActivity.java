package com.sky.demo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.sky.demo.R;
import com.sky.demo.ui.BaseActivity;
import com.sky.demo.ui.adapter.GuideContoler;
import com.sky.utils.SPUtils;

/**
 * @author LiBin
 * @ClassName: IntroductoryActivity
 * @Description: TODO 首次引导页面
 * @date 2015年3月26日 下午4:01:00
 */
public class IntroductoryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//在v7主题下下无用
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introductory_viewpager);
        initViewPager();
    }

    /**
     * 使用写好的库初始化引导页面
     **/
    private void initViewPager() {

        GuideContoler contoler = new GuideContoler(this);
        int[] imgIds = {R.mipmap.introductory_01, R.mipmap.introductory_02, R.mipmap.introductory_03};
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.pager_four, null);
        contoler.init(imgIds, view);
        view.findViewById(R.id.bt_login).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        SPUtils.put(IntroductoryActivity.this, "isfirst", false);
                        startActivity(new Intent(IntroductoryActivity.this,
                                MainActivity.class));
                        overridePendingTransition(R.anim.in_from_right,
                                R.anim.out_to_left);
                        finish();
                    }
                });
    }
}
