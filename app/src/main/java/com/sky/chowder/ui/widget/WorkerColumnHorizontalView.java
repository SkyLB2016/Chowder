package com.sky.chowder.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.sky.chowder.R;
import com.sky.chowder.model.WorkerTypeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 员工柱状图，司龄，年龄，学历
 * Created by libin on 2019/08/30 16:47 Friday.
 */
public class WorkerColumnHorizontalView extends View {

    private Paint paint;
    private Paint textP;
    private List<WorkerTypeModel> workers = new ArrayList<>();//员工集合
    private int color = getResources().getColor(R.color.color_7d91e2);

    private int columnHeight = getResources().getDimensionPixelOffset(R.dimen.wh_12);//柱状图的宽

    //坐标系计算，7条线，分六份，每份的大小
    private int workerPiece = 0;//柱状图每份的大小,
    private int pieceHorizontal;//横向方向上每份的高度
    private int interval = getResources().getDimensionPixelOffset(R.dimen.wh_14);//竖向方向上柱状图的间隔的宽度

    public WorkerColumnHorizontalView(Context context) {
        this(context, null);
    }

    public WorkerColumnHorizontalView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WorkerColumnHorizontalView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int layoutWidth = MeasureSpec.getSize(widthMeasureSpec);
//        setMeasuredDimension(layoutWidth, layoutWidth);
//    }


    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textP = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setWorkers(List<WorkerTypeModel> workers) {
        this.workers = workers;
        int maxWorker = workers.get(0).getValue();
        for (int i = 1; i < workers.size(); i++) {
            maxWorker = Math.max(maxWorker, workers.get(i).getValue());
        }
        workerPiece = maxWorker / 10 + 1;//会默认取整，所以加1
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(getResources().getColor(R.color.white));
        int width = getMeasuredWidth();
//        int height = getMeasuredHeight();
        //宽度分3份，文字占四分之一，柱图最大占八分之五，剩余写文字。
        //个人设定，柱图八分之五的宽度均分10份
        pieceHorizontal = width / 8 * 5 / 10;

        float leftR = width / 4;
        float rightR;
        float topR;
        float bottomR;
        int number;
        WorkerTypeModel worker;
        float workerH;
        RectF rectF;
        paint.setColor(color);
        textP.setTextSize(getResources().getDimension(R.dimen.text_14));
        float wordInterval = getResources().getDimension(R.dimen.wh_10);//文字与柱状图的间隔
        float wordLeft = leftR - wordInterval;
        float baseline;
        for (int i = 0; i < workers.size(); i++) {
            worker = workers.get(i);
            number = worker.getValue();
            workerH = 1.0f * number / workerPiece * pieceHorizontal;
            rightR = leftR + workerH;
            topR = (columnHeight + interval) * i;
            bottomR = topR + columnHeight;
            rectF = new RectF(leftR, topR, rightR, bottomR);
            canvas.drawRect(rectF, paint);

            Paint.FontMetrics metrics = textP.getFontMetrics();
            baseline = rectF.centerY() + (metrics.bottom - metrics.top) / 2 - metrics.bottom;

            textP.setTextAlign(Paint.Align.RIGHT);
            textP.setColor(getResources().getColor(R.color.color_999999));
            canvas.drawText(worker.getName(), wordLeft, baseline, textP);

            textP.setTextAlign(Paint.Align.LEFT);
            textP.setColor(getResources().getColor(R.color.color_333333));
            canvas.drawText(worker.getScale(), rectF.right + wordInterval, baseline, textP);
        }
    }
}
