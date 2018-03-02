package com.sky.chowder.ui.widget;

import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.Transformation;

/**
 * Created by SKY on 2018/3/2 15:43.
 */
public class Custom extends Animation {

    int centerX,centerY;
    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        setDuration(2000);
        setFillAfter(true);
        setInterpolator(new BounceInterpolator());
        centerX=width/2;
        centerY=height/2;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        Matrix matrix = t.getMatrix();
        matrix.postScale(1,1-interpolatedTime,centerX,centerY);
    }
}
