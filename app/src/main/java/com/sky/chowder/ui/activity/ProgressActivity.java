package com.sky.chowder.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sky.chowder.R;
import com.sky.chowder.ui.widget.ProgressView;

/**
 * Created by libin on 2018/9/6 上午10:41.
 */
public class ProgressActivity extends AppCompatActivity {

    ProgressView progress;
//    private String[] datas = new String[]{"签署委托协议", "资料填写", "协议签署", "录像存证"};
//    private String[] realText = new String[]{"身份认证", "人脸认证"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setProgress(progress.getProgress() + 1);
                progress.invalidate();
                if (progress.getProgress() == 4) {
                    progress.setTexts(new String[]{"签署委托协议", "资料填写"});

                }
            }
        });
        progress = findViewById(R.id.progress);
        progress.setTexts(new String[]{"签署委托协议", "资料填写", "协议签署", "录像存证"});


    }
}
