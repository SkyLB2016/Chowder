package com.sky.chowder.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sky.chowder.R;
import com.sky.chowder.model.PCommon;

import java.util.ArrayList;
import java.util.List;

/**
 * 人员走势分析图
 * Created by libin on 2019/08/30 16:47 Friday.
 */
public class WorkerView extends View {

    private int width;
    private int height;
    private Paint paint;
    private Paint textP;
    private List<String> text = new ArrayList<>();//种类集合
    private List<PCommon> workers = new ArrayList<>();//员工集合
    private int workerColor = getResources().getColor(R.color.color_7d91e2);//在职的颜色
    private int joinColor = getResources().getColor(R.color.color_b1bdee);//入职的颜色
    private int leaveColor = getResources().getColor(R.color.color_5D75DB);//离职的颜色

    private int LinePadLeft = getResources().getDimensionPixelOffset(R.dimen.wh_22);//横线X的起始位置，也就是左间距
    private int columnPad = getResources().getDimensionPixelOffset(R.dimen.wh_32);//柱状图X的起始位置，柱状图的左间距，
    private int columnWidth = getResources().getDimensionPixelOffset(R.dimen.wh_18);//柱状图的宽

    //坐标系计算，7条线，分六份，每份的大小
    private int number = 6;//每个柱状图折线图默认分成多少份
    private int scaleWorker = 0;//柱状图每份刻度
    private int scaleLeave = 0;//离职每份刻度
    private int scaleHeight;//竖直方向上每份刻度的高度
    private int interval;//横向方向上柱状图的间隔的宽度

    private List<RectF> rectFs = new ArrayList<>();
    private int touchMonth = -1;//点击的位置

    public WorkerView(Context context) {
        this(context, null);
    }

    public WorkerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WorkerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int layoutWidth = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(layoutWidth, layoutWidth / 5 * 4);
    }

    private void init() {
        text.add("在职人数");
        text.add("入职人数");
        text.add("离职人数");

        paint = new Paint();
        textP = new Paint();
    }

    public void setWorkers(List<PCommon> workers) {
        this.workers = workers;
        //柱图刻度计算
        int maxWorker = workers.get(0).getOnjob();
        for (int i = 1; i < workers.size(); i++) {
            maxWorker = Math.max(maxWorker, workers.get(i).getOnjob());
        }
        if (maxWorker % number == 0) {//没余值
            scaleWorker = maxWorker / number;
        } else {//有余值，需加1
            scaleWorker = maxWorker / number + 1;
        }

        //折线刻度计算
        int maxLeave = workers.get(0).getLeave();
        for (int i = 1; i < workers.size(); i++) {
            maxLeave = Math.max(maxLeave, workers.get(i).getLeave());
        }
        if (maxLeave % number == 0) {//没余值
            scaleLeave = maxLeave / number;
        } else {//有余值，需加1
            scaleLeave = maxLeave / number + 1;
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(getResources().getColor(R.color.white));
        //重置画笔
        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textP.reset();
        textP.setFlags(Paint.ANTI_ALIAS_FLAG);

        width = getMeasuredWidth();
        height = getMeasuredHeight();
        //整张图分两部分，个人设定分为8份，上六份画图，下两份写月份与文字
        scaleHeight = height / (number + 2);
        //算柱状图的的间隔
        interval = (width - columnPad - (columnPad - LinePadLeft) - columnWidth * 2 * workers.size()) / (workers.size() - 1);

        //7条线：number+1
        paint.setColor(getResources().getColor(R.color.color_E6E6E6));
        paint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.wh_1));
        for (int i = 0; i < 7; i++) {
            canvas.drawLine(LinePadLeft, scaleHeight * i, width, scaleHeight * i, paint);
        }

        textP.setTextSize(getResources().getDimension(R.dimen.text_10));
        textP.setColor(getResources().getColor(R.color.color_333333));

        if (workers.size() == 0) return;
        //设置月份与底部文字
        setText(canvas);
        //设置柱状图与折线图
        setChart(canvas);
        //遮罩层
        drawMark(canvas);
    }

    private void setText(Canvas canvas) {
        //月份的文字
        Paint.FontMetrics metrics = textP.getFontMetrics();
        float baseline = scaleHeight * 6 + scaleHeight / 2 + (metrics.descent - metrics.ascent) / 2 - metrics.descent;
        for (int i = 0; i < 6; i++) {
            int leftX = columnPad + columnWidth * 2 * i + interval * i;
            canvas.drawText(workers.get(i).getDate(), leftX, baseline, textP);
        }

        //下一行文字的基线
        baseline += scaleHeight;
        int radius = getResources().getDimensionPixelOffset(R.dimen.wh_4);
        paint.setStyle(Paint.Style.FILL);
        //在职人数
        paint.setColor(workerColor);
        int centerX = columnPad + columnWidth * 2 + interval / 2;
        int centerY = scaleHeight * 7 + scaleHeight / 2;
        canvas.drawCircle(centerX, centerY, radius, paint);
        canvas.drawText(text.get(0), centerX + interval / 2, baseline, textP);

        //入职人数
        paint.setColor(joinColor);
        centerX = columnPad + columnWidth * 2 * 2 + interval * 2 + columnWidth / 2;
        canvas.drawCircle(centerX, centerY, radius, paint);
        canvas.drawText(text.get(1), centerX + interval / 2, baseline, textP);

        //离职人数
        paint.setColor(leaveColor);
        paint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.wh_2));
        int leftX = columnPad + columnWidth * 2 * 4 + interval * 3 - columnWidth / 2;
        int lineWidth = getResources().getDimensionPixelOffset(R.dimen.wh_14);//线宽
        canvas.drawLine(leftX, centerY, leftX + lineWidth, centerY, paint);
        canvas.drawText(text.get(2), leftX + columnWidth, baseline, textP);
    }

    private void setChart(Canvas canvas) {
        float leftR;
        float rightR;
        float topR;
        float bottomR = scaleHeight * 6.0f;//所有柱状图的底线都是第7条线。
        int worker;
        float workerH;
        Path path = new Path();
        rectFs.clear();
        for (int i = 0; i < workers.size(); i++) {
            //在职人数
            worker = workers.get(i).getOnjob();
            workerH = 1.0f * worker / scaleWorker * scaleHeight;
            leftR = columnPad + (columnWidth * 2 * i + interval * i);
            rightR = columnPad + (columnWidth * 2 * i + interval * i) + columnWidth;
            topR = bottomR - workerH;
            RectF rectF = new RectF(leftR, topR, rightR, bottomR);
            paint.setColor(workerColor);
            canvas.drawRect(rectF, paint);
            rectFs.add(rectF);

            //入职人数
            worker = workers.get(i).getJoin();
            workerH = 1.0f * worker / scaleWorker * scaleHeight;
            leftR = columnPad + (columnWidth * 2 * i + interval * i) + columnWidth;
            rightR = columnPad + (columnWidth * 2 * i + interval * i) + columnWidth * 2;
            topR = bottomR - workerH;
            RectF rectF2 = new RectF(leftR, topR, rightR, bottomR);
            paint.setColor(joinColor);
            canvas.drawRect(rectF2, paint);
            rectFs.add(rectF2);

            //离职人数
            worker = workers.get(i).getLeave();
            workerH = 1.0f * worker / scaleLeave * scaleHeight;
            leftR = columnPad + (columnWidth * 2 * i + interval * i) + columnWidth;
            topR = bottomR - workerH;
            if (i == 0) {
                path.moveTo(leftR, topR);
            } else {
                path.lineTo(leftR, topR);
            }
        }
        //离职人数
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(leaveColor);
        canvas.drawPath(path, paint);

        //画折线第一个点的外圆与内圆
        worker = workers.get(0).getLeave();
        workerH = 1.0f * worker / scaleLeave * scaleHeight;
        leftR = columnPad + columnWidth;
        topR = bottomR - workerH;
        int radiusOut = getResources().getDimensionPixelOffset(R.dimen.wh_6);//折线外圆半径
        int radiusIn = getResources().getDimensionPixelOffset(R.dimen.wh_3);//折线内圆半径
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(android.R.color.white));
        canvas.drawCircle(leftR, topR, radiusOut, paint);
        paint.setColor(getResources().getColor(R.color.color_5D75DB));
        canvas.drawCircle(leftR, topR, radiusIn, paint);
    }

    private void drawMark(Canvas canvas) {
        if (touchMonth == -1) return;
        //矩形背景
        Rect rectBounds = new Rect(0, scaleHeight + 5, columnPad + columnWidth * 3 + interval + 5, new Double(scaleHeight * 3).intValue());
        Drawable rectBg = getResources().getDrawable(R.drawable.shape_rect_gray);
        rectBg.setBounds(rectBounds);
        rectBg.draw(canvas);//划出矩形背景
        //绘制文字，先计算三行文字所占高度，
        Paint.FontMetrics metrics = textP.getFontMetrics();

        float textHeight = metrics.bottom - metrics.top;
        int lineIntervel = getResources().getDimensionPixelSize(R.dimen.wh_1);                              //每行间距为1dp；
        float paddingTop = rectBounds.top + (rectBounds.height() - textHeight * 3 - lineIntervel * 2) / 2;  //计算矩形框的上间距，也是第一行文字的顶部起始位置
        float baseline = paddingTop + textHeight - metrics.bottom;                                          //计算baseline

        int leftX = rectBounds.width() / 4;                                                                 //文字X的起始位置，默认设为矩形宽的四分之一
        int centerX = rectBounds.width() / 8;                                                               //小圆心X即为四分之一宽的中心
        int centerY = new Float(paddingTop + textHeight / 2).intValue();                              //小圆心Y即为此行文字的中心
        int radius = getResources().getDimensionPixelOffset(R.dimen.wh_2half);                              //小圆的半径

        //在职人数
        paint.setColor(workerColor);
        canvas.drawCircle(centerX, centerY, radius, paint);
        textP.setColor(getResources().getColor(R.color.color_c8c8c8));
        canvas.drawText(text.get(0), leftX, baseline, textP);

        //入职人数
        paint.setColor(joinColor);
        centerY = new Float(paddingTop + textHeight * 3 / 2 + lineIntervel).intValue();
        canvas.drawCircle(centerX, centerY, radius, paint);
        baseline = paddingTop + textHeight * 2 + lineIntervel - metrics.bottom;                             //第二行的基线
        canvas.drawText(text.get(1), leftX, baseline, textP);

        //离职人数
        paint.setColor(leaveColor);
        paint.setStrokeWidth(lineIntervel);
        centerY = new Float(paddingTop + textHeight * 5 / 2 + lineIntervel * 2).intValue();
        canvas.drawLine(centerX - radius * 2, centerY, centerX + radius * 2, centerY, paint);
        baseline = paddingTop + textHeight * 3 + lineIntervel * 2 - metrics.bottom;
        canvas.drawText(text.get(2), leftX, baseline, textP);

        //职工的具体人数
        PCommon worker = workers.get(touchMonth);
        textP.setTextSize(getResources().getDimension(R.dimen.text_12));
        textP.setColor(getResources().getColor(android.R.color.white));
        textP.setTextAlign(Paint.Align.RIGHT);//右对齐
        leftX = rectBounds.width() - 20;
        baseline = paddingTop + textHeight - metrics.bottom;
        canvas.drawText(worker.getOnjob() + "", leftX, baseline, textP);
        baseline = paddingTop + textHeight * 2 + lineIntervel - metrics.bottom;
        canvas.drawText(worker.getJoin() + "", leftX, baseline, textP);
        baseline = paddingTop + textHeight * 3 + lineIntervel * 2 - metrics.bottom;
        canvas.drawText(worker.getLeave() + "", leftX, baseline, textP);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                for (int i = 0; i < rectFs.size(); i++) {
                    if (rectFs.get(i).contains(x, y)) {
                        touchMonth = i / 2;
                        if (handler.hasMessages(1)) {
                            handler.removeMessages(1);
                        }
                        invalidate();
                        return true;
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                touchMonth = -1;
                invalidate();
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (touchMonth != -1) {
                    handler.sendEmptyMessageDelayed(1, 3000);
                    return true;
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
