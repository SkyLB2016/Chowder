package com.sky.demo.ui;

import android.graphics.BlurMaskFilter;
import android.graphics.EmbossMaskFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sky.demo.R;
import com.sky.demo.ui.widget.pending.DrawView;


public class DrawActivity extends AppCompatActivity {
    private DrawView drawView;
    BlurMaskFilter blurMaskFilter;
    EmbossMaskFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        drawView = (DrawView) findViewById(R.id.draw);

        filter = new EmbossMaskFilter(new float[]{1.5f, 1.5f, 1.5f}, 0.6f, 6, 4.2f);
        blurMaskFilter = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
//        drawView.cachePaint.setMaskFilter(filter);
    }

    public void change(View v) {
        drawView.getCachePaint().setMaskFilter(blurMaskFilter);

    }

}