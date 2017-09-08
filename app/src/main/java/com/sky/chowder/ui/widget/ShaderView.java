package com.sky.chowder.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.sky.chowder.R;

/**
 * @author 彬 QQ 1136096189
 * @Description: TODO shader应用，倒影效果
 * @date 2015/8/14 14:21
 */
public class ShaderView extends View {

    private Bitmap bitmap;
    private Bitmap refBitmap;
    private Paint paint;

    public ShaderView(Context context) {
        this(context, null);
    }

    public ShaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Matrix matrix = new Matrix();
        matrix.setScale(1, -1);
        refBitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        paint = new Paint();
        paint.setShader(new LinearGradient(0,bitmap.getHeight(),0,bitmap.getHeight()*1.8f, 0xEE000000,0x10000000, Shader.TileMode.CLAMP));
//        paint.setShader(new LinearGradient(0,bitmap.getHeight(),0,bitmap.getHeight()*1.8f, Color.RED,Color.BLUE, Shader.TileMode.CLAMP));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawBitmap(refBitmap,0,bitmap.getHeight(),null);
        canvas.drawRect(0,bitmap.getHeight(),bitmap.getWidth(),bitmap.getHeight()*2,paint);
    }
}
