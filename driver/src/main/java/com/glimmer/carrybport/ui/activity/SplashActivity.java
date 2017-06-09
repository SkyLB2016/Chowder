package com.glimmer.carrybport.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.glimmer.carrybport.BuildConfig;
import com.glimmer.carrybport.MyApplication;
import com.glimmer.carrybport.R;
import com.sky.Common;
import com.sky.utils.JumpAct;
import com.sky.utils.SPUtils;

/**
 * Created by ASUS on 2017/6/7.
 */
public class SplashActivity extends AppCompatActivity {
    public Boolean flag;//是否首次运行，true代表首次
    private int TIME = 1000;//handler延迟时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏,在v7中AppCompatActivity下无用
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        super.onCreate(savedInstanceState);
        ImageView view = new ImageView(this);
        view.setBackgroundResource(R.mipmap.bg_splash);
        setContentView(view);
        if (BuildConfig.DEBUG) MyApplication.getInstance().showToast("测试环境");
        flag = (Boolean) SPUtils.getInstance().get(Common.ISFIRST, true);
        //加载动画
        AlphaAnimation animation = new AlphaAnimation(0.5f, 1f);
        animation.setDuration(TIME);
        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 判断程序与第几次运行，如果是第一次运行引导页面
                        JumpAct();
                    }
                }, TIME);
            }
        });
    }

    private void JumpAct() {
        if (MyApplication.getInstance().getObject(Common.ISFIRST, true)) {
            JumpAct.jumpActivity(this, GuideActivity.class);
//        } else if (MyApplication.getInstance().getUsertOnline()){
//            JumpAct.jumpActivity(this, MainActivity.class);
        }else{
            JumpAct.jumpActivity(this, LoginActivity.class);
        }
        finish();
    }
}
