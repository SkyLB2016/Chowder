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
public class WorkerColumnView extends View {

    private int width;
    private int height;
    private Paint paint;
    private List<WorkerTypeModel> workers = new ArrayList<>();//员工集合
    private int color = getResources().getColor(R.color.color_5d75db);

    private int LinePad = getResources().getDimensionPixelOffset(R.dimen.wh_14);//横线X的起始位置
    private int columnPad = getResources().getDimensionPixelOffset(R.dimen.wh_19);//柱状图X的起始位置
    private int columnWidth = getResources().getDimensionPixelOffset(R.dimen.wh_12);//柱状图的宽

    //坐标系计算，7条线，分六份，每份的大小
    private int workerPiece = 0;//柱状图每份的大小,
    private int pieceVertical;//竖直方向上每份的高度
    private int interval;//横向方向上柱状图的间隔的宽度

    public WorkerColumnView(Context context) {
        this(context, null);
    }

    public WorkerColumnView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WorkerColumnView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int layoutWidth = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(layoutWidth, layoutWidth);
    }


    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setWorkers(List<WorkerTypeModel> workers) {
        this.workers = workers;
        int maxWorker = workers.get(0).getValue();
        for (int i = 1; i < workers.size(); i++) {
            maxWorker = Math.max(maxWorker, workers.get(i).getValue());
        }
        workerPiece = maxWorker / 6 + 1;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(getResources().getColor(R.color.white));
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        //分6份
        pieceVertical = height / 6;
        //算柱状图的的间隔
        interval = (width - columnPad * 2 - columnWidth * workers.size()) / (workers.size() - 1);

        paint.setColor(getResources().getColor(R.color.color_E6E6E6));
        paint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.wh_1));
        canvas.drawLine(LinePad, pieceVertical * 6, width - LinePad, pieceVertical * 6, paint);

        float leftR;
        float rightR;
        float topR;
        float bottomR = pieceVertical * 6.0f;//所有柱状图的底线都是第7条线。
        int worker;
        float workerH;
        RectF rectF;
        paint.setColor(color);
        for (int i = 0; i < workers.size(); i++) {
            worker = workers.get(i).getValue();
            workerH = 1.0f * worker / workerPiece * pieceVertical;
            leftR = columnPad + (columnWidth * i + interval * i);
            rightR = columnPad + (columnWidth * i + interval * i) + columnWidth;
            topR = bottomR - workerH;
            rectF = new RectF(leftR, topR, rightR, bottomR);
            canvas.drawRect(rectF, paint);
        }
    }
}
