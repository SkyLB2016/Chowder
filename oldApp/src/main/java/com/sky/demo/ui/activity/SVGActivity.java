package com.sky.demo.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.sky.demo.R;
import com.sky.demo.ui.BaseActivity;
import com.sky.demo.utils.pending.FileUtils;

/**
 * @author LiBin
 * @ClassName: WelcomeActivity
 * @Description: 欢迎页
 * @date 2015年3月26日 下午4:19:07
 */
public class SVGActivity extends BaseActivity {

    public Boolean flag;//是否首次运行，true代表首次
    private int TIME = 4000;//handler延迟时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏,在v7中AppCompatActivity下无用
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        super.onCreate(savedInstanceState);
        WebView view = new WebView(this);
        // view.loadUrl("file:///android_asset/svg_page_loading.html");
        loadAssets(view);
        setContentView(view);
        //加载动画
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        view.startAnimation(animation);
    }

    /**
     * 加载本地html，并显示图片。
     * 待研究
     * @param view
     */
    private void loadAssets(WebView view) {
        WebSettings settings = view.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(false);//图形：false小，true大
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(false);// 允许放大缩小
        settings.setBuiltInZoomControls(false);// 显示放大缩小按钮
        settings.setDisplayZoomControls(false);// api 11以上
        settings.setSupportMultipleWindows(true);
        settings.setGeolocationEnabled(true);// 启用地理定位

        view.setHorizontalScrollBarEnabled(false);
        view.setBackgroundColor(Color.TRANSPARENT); // 设置背景色
        String html = FileUtils.readAssest(this, "html/svg_page_loading.html");

        html = html.replace("@image_assets", "html/logo.png");
        String baseurl = "file:///android_asset/";
        view.loadDataWithBaseURL(baseurl, html, "text/html", "UTF-8", null);
    }
}