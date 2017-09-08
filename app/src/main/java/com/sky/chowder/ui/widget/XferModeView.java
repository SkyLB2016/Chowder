package com.sky.chowder.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.sky.chowder.R;

/**
 * @author sky
 * @Description: TODO Xfermode应用
 * @date 2015/8/7 17:45
 */
public class XferModeView extends View {

    private Bitmap mBitmap;
    private Bitmap mOut;
    private Paint paint;


    public XferModeView(Context context) {
        this(context, null);
    }

    public XferModeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XferModeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        mOut = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mOut);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            canvas.drawRoundRect(0, 0, mBitmap.getWidth(), mBitmap.getHeight(), 50, 50, paint);
        else
            canvas.drawCircle(mBitmap.getWidth() /2, mBitmap.getHeight() / 2, mBitmap.getHeight() / 2, paint);
//        RectF rectF = new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
//        canvas.drawRoundRect(rectF,50,50,paint);
//        canvas.drawOval(rectF,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(mBitmap, 0, 0, paint);
//        canvas.drawBitmap(mBitmap, null, rectF, paint);
        paint.setXfermode(null);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mOut, 0, 0, null);
    }
}
