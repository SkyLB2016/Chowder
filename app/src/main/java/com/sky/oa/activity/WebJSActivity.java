package com.sky.oa.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sky.design.app.BaseActivity;
import com.sky.oa.R;
import com.sky.oa.utils.PaxWebChromeClient;

public class WebJSActivity extends BaseActivity {
    private PaxWebChromeClient chromeClient;
    private String mUrl;
    private WebView webView;

    public static void start(Context context, String url) {
        Intent intent = new Intent(context, WebJSActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_web;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = (WebView) findViewById(R.id.webView);
//        Intent intent = getIntent();

        mUrl = "https://realnameverify-test.fadada.com/fddAuthenticationService/v1/api/synsAuthentication.action?transaction_no=DC81F39AB1D45052988F95C9076889A90FC28F37FB0ACBD6406E3BB235F400E0ADA0483DC1757F39&sign=NTkxOTA0RUM3RUYwNjcyNzlENzY4RURCMzlGRjQxMzhBMjk5NDkwOQ==&app_id=401952&timestamp=1585200074307";

        chromeClient = new PaxWebChromeClient(this);

        initWebViewSettings();

        webView.setWebChromeClient(chromeClient);
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(mUrl);
        initChildInfo();
    }

    private void initWebViewSettings() {
        if (webView == null) {
            throw new NullPointerException("webview is null. please use initWebView(WebView webView) method");
        }

        webView.requestFocusFromTouch();// 支持获取手势焦点，输入用户名、密码或其他

        WebSettings webSettings = webView.getSettings();
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        webSettings.setJavaScriptEnabled(true);//支持js
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口

        webSettings.setDatabaseEnabled(false);// 数据库缓存
        webSettings.setAppCacheEnabled(false);// 打开缓存
        webSettings.setDomStorageEnabled(true);// 打开dom storage缓存
        webSettings.setAllowFileAccess(true);  //设置可以访问文件
        webSettings.setAllowContentAccess(true); // 是否可访问Content Provider的资源，默认值 true
        webSettings.setAllowFileAccess(true);    // 是否可访问本地文件，默认值 true
        webSettings.setJavaScriptEnabled(true);// 允许网页与JS交互
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);// 提高渲染级别
        webSettings.setLoadsImagesAutomatically(true);// 自动加载网络图片
        webSettings.setDefaultTextEncodingName("utf-8");// 设置默认编码方式
        // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
        webSettings.setAllowFileAccessFromFileURLs(false);
        // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
        webSettings.setAllowUniversalAccessFromFileURLs(false);
        // 支持缩放
        webSettings.setSupportZoom(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);// 布局方式
    }

    protected void initChildInfo() {
        webView.addJavascriptInterface(new onJavaScriptInterface(), "android");
    }

    private class onJavaScriptInterface {
        /**
         * 返回上个界面
         */
        @JavascriptInterface
        public void finishView() {
            webView.post(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
        }

        /**
         * 打电话
         */
        @JavascriptInterface
        public void callMobile(final String mobile) {
            webView.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + mobile);
                    intent.setData(data);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        chromeClient.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 处理返回键，在webview界面，按下返回键，不退出程序
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
            } else {
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            webView.stopLoading();
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearHistory();
            webView.removeAllViews();
            webView.destroy();
        } catch (Exception e) {
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            hideStatusView();
        }
    }
}
