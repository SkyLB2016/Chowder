package com.sky.chowder.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.sky.utils.ImageUtils;

/**
 * @author 彬 QQ 1136096189
 * @Description: TODO drawbitmapmesh应用
 * @date 2015/8/14 15:27
 */
public class MeshView extends ImageView {

    private int MESHWIDTH = 200;
    private int MESHHEIGHT = 200;
    private int COUNT = (MESHWIDTH + 1) * (MESHHEIGHT + 1);
    private float[] oldPoint = new float[COUNT * 2];
    private float[] newPoint = new float[COUNT * 2];
    private Bitmap mBitmap;

    public MeshView(Context context) {
        this(context, null);
    }

    public MeshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MeshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.setScaleType(ScaleType.CENTER);
        initView();
    }
    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmap = bm;
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap = ImageUtils.getBitmapFromDrawable(drawable);
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        mBitmap = ImageUtils.getBitmapFromDrawable(getDrawable());
    }
    private void initView() {
        float bitWidth = mBitmap.getWidth();
        float bitHeight = mBitmap.getHeight();
        for (int i = 0; i < MESHHEIGHT + 1; i++) {
            float bitY = bitHeight*i/ MESHHEIGHT;
            for (int j = 0; j < MESHHEIGHT + 1; j++) {
                float bitX = bitWidth*j/ MESHWIDTH;
                newPoint[(i*(MESHHEIGHT +1)+j)*2+0]=oldPoint[(i*(MESHHEIGHT +1)+j)*2+0]=bitX;
                newPoint[(i*(MESHHEIGHT +1)+j)*2+1]=oldPoint[(i*(MESHHEIGHT +1)+j)*2+1]=bitY;
            }
        }

    }
    private float k=1;

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < MESHHEIGHT + 1; i++) {
            for (int j = 0; j < MESHWIDTH + 1; j++) {
                float offSetY = (float) Math.sin((float) j / MESHWIDTH * 2 * Math.PI+k*2* Math.PI) ;
                newPoint[(i * (MESHHEIGHT + 1) + j) * 2 + 0] =oldPoint[(i * (MESHHEIGHT + 1) + j) * 2 + 0]+offSetY* 100+200;
                newPoint[(i * (MESHHEIGHT + 1) + j) * 2 + 1] = oldPoint[(i * (MESHHEIGHT + 1) + j) * 2 + 1] + offSetY* 50 + 400;
            }
        }
        canvas.drawBitmapMesh(mBitmap, MESHWIDTH, MESHHEIGHT, newPoint, 0, null, 0, null);
        k+=0.05;
        invalidate();
    }
}
