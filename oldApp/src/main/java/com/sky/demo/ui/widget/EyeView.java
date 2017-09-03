package com.sky.demo.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class EyeView extends FrameLayout {

    private Paint paint;
    private Bitmap bitmap;

    public EyeView(Context context) {
        super(context);
        init();
    }

    public EyeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EyeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setDrawingCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * dispatchDraw
     * View组件的绘制会调用draw(Canvas canvas)方法，
     * draw过程中主要是先画Drawable背景，
     * 对 drawable调用setBounds()然后是draw(Canvas c)方法.
     * 有点注意的是背景drawable的实际大小会影响view组件的大小，
     * drawable的实际大小通过getIntrinsicWidth()和getIntrinsicHeight()获取，
     * 当背景比较大时view组件大小等于背景drawable的大小.
     * 画完背景后，draw过程会调用onDraw(Canvas canvas)方法，
     * 然后就是dispatchDraw(Canvas canvas)方法, dispatchDraw()主要是分发给子组件进行绘制，
     * 我们通常定制组件的时候重写的是onDraw()方法。值得注意的是ViewGroup容器组件的绘制，
     * 当它没有背景时直接调用的是dispatchDraw()方法, 而绕过了draw()方法，
     * 当它有背景的时候就调用draw()方法，而draw()方法里包含了dispatchDraw()方法的调用。
     * 因此要在ViewGroup上绘制东西的时候往往重写的是dispatchDraw()方法而不是onDraw()方法，
     * 或者自定制一个Drawable，重写它的draw(Canvas c)和 getIntrinsicWidth(),getIntrinsicHeight()方法，然后设为背景。
     * @param canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (bitmap != null) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawBitmap(bitmap, 0, 0, paint);
            paint.setXfermode(null);
        }
    }

    public void setRadius(int radius) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius, paint);
        invalidate();
    }

}
