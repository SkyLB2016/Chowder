package com.sky.oa.activity;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.facebook.drawee.view.SimpleDraweeView;
import com.sky.design.app.BaseActivity;
import com.sky.oa.R;

/**
 * Created by libin on 2020/03/31 10:31 AM Tuesday.
 */
public class FrescoActivity extends BaseActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_fresco;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Uri uri = Uri.parse("https://qiniu.lejiehuo.com/FjHN7VjbwJ9YgmoeMUbedaAyNwM3");//大图
        Uri uri = Uri.parse("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1208538952,1443328523&fm=26&gp=0.jpg");
//        Uri uri = Uri.parse("https://qiniu.lejiehuo.com/FqH0URzT6tjJrV8o5ApGm7w63RIb");
        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.imageView);
        draweeView.setImageURI(uri);
    }
}
