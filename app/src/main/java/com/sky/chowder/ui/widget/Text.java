package com.sky.chowder.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by SKY on 2018/2/24 14:49.
 */
public class Text extends android.support.v7.widget.AppCompatTextView {
    public Text(Context context) {
        super(context);
    }

    public Text(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Text(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    Matrix matrix;
    int width;
    Paint paint;
    LinearGradient linear;

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        width = getMeasuredWidth();
//        if (width > 0) {
//            paint = getPaint();
//            linear = new LinearGradient(0, 0, width, 0, new int[]{Color.RED, Color.GREEN, Color.BLUE}, null, Shader.TileMode.CLAMP);
//            paint.setShader(linear);
//            matrix = new Matrix();
//
//        }
//    }

    int tran;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getMeasuredWidth();
        if (width > 0) {
            paint = getPaint();
            linear = new LinearGradient(0, 0, width, 0, new int[]{Color.RED, Color.GREEN, Color.BLUE}, null, Shader.TileMode.CLAMP);
            paint.setShader(linear);
            matrix = new Matrix();

        }


        tran += width / 5;
        if (tran > width * 1.1) {
            tran = -width;
        }
        matrix.setTranslate(tran, 0);
        linear.setLocalMatrix(matrix);
        postInvalidateDelayed(300);
    }
}
