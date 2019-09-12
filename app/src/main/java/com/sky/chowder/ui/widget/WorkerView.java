package com.sky.chowder.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.sky.chowder.R;
import com.sky.chowder.model.WorkerMonthModel;

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
    private List<WorkerMonthModel> workers = new ArrayList<>();//员工集合
    private int workerColor = getResources().getColor(R.color.color_7d91e2);//在职的颜色
    private int entryColor = getResources().getColor(R.color.color_b1bdee);//入职的颜色
    private int quitColor = getResources().getColor(R.color.color_5d75db);//离职的颜色

    private int LinePadLeft = getResources().getDimensionPixelOffset(R.dimen.wh_22);//横线X的起始位置，也就是左间距
    private int columnPad = getResources().getDimensionPixelOffset(R.dimen.wh_32);//柱状图X的起始位置，柱状图的左间距，
    private int columnWidth = getResources().getDimensionPixelOffset(R.dimen.wh_18);//柱状图的宽

    //坐标系计算，7条线，分六份，每份的大小
    private int workerPiece = 0;//柱状图每份的大小,
    private int quitPiece = 0;//离职每份的大小
    private int pieceVertical;//竖直方向上每份的高度
    private int interval;//横向方向上柱状图的间隔的宽度

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

    public void setWorkers(List<WorkerMonthModel> workers) {
        this.workers = workers;
        int maxWorker = workers.get(0).getWorker();
        for (int i = 1; i < workers.size(); i++) {
            maxWorker = Math.max(maxWorker, workers.get(i).getWorker());
        }
        workerPiece = maxWorker / 6 + 1;

        int maxQuit = workers.get(0).getQuitWorker();
        for (int i = 1; i < workers.size(); i++) {
            maxQuit = Math.max(maxQuit, workers.get(i).getQuitWorker());
        }
        quitPiece = maxQuit / 6 + 1;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(getResources().getColor(R.color.white));
        //重置文字的画笔
        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textP.reset();
        textP.setFlags(Paint.ANTI_ALIAS_FLAG);

        width = getMeasuredWidth();
        height = getMeasuredHeight();
        //个人设定分为8份，上六分画图，下两份写月份与文字
        pieceVertical = height / 8;
        //算柱状图的的间隔
        interval = (width - columnPad - (columnPad - LinePadLeft) - columnWidth * 2 * workers.size()) / (workers.size() - 1);

        //7条线
        paint.setColor(getResources().getColor(R.color.color_E6E6E6));
        paint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.wh_1));
        for (int i = 0; i < 7; i++) {
            canvas.drawLine(LinePadLeft, pieceVertical * i, width, pieceVertical * i, paint);
        }

        textP.setTextSize(getResources().getDimension(R.dimen.text_10));
        textP.setColor(getResources().getColor(R.color.color_333333));
        //刻度
        String textPiece = workerPiece + "/" + quitPiece;
        Paint.FontMetrics metrics = textP.getFontMetrics();
        float baseline = pieceVertical * 5 + (metrics.descent - metrics.ascent) / 2 - metrics.descent;
        textP.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(textPiece, width, baseline, textP);
        textP.setTextAlign(Paint.Align.LEFT);

        //设置月份与底部文字
        setText(canvas);
        //设置柱状图与折线图
        setChart(canvas);
        //遮罩层
        drawMask(canvas);
    }

    private void setText(Canvas canvas) {
        //月份的文字
        Paint.FontMetrics metrics = textP.getFontMetrics();
        float baseline = pieceVertical * 6 + pieceVertical / 2 + (metrics.descent - metrics.ascent) / 2 - metrics.descent;
        for (int i = 0; i < 6; i++) {
            int leftX = columnPad + columnWidth * 2 * i + interval * i;
            canvas.drawText(workers.get(i).getDate(), leftX, baseline, textP);
        }

        //下一行文字的基线
        baseline += pieceVertical;
        int radius = getResources().getDimensionPixelOffset(R.dimen.wh_4);
        paint.setStyle(Paint.Style.FILL);
        //在职人数
        paint.setColor(workerColor);
        int centerX = columnPad + columnWidth * 2 + interval / 2;
        int centerY = pieceVertical * 7 + pieceVertical / 2;
        canvas.drawCircle(centerX, centerY, radius, paint);
        canvas.drawText(text.get(0), centerX + interval / 2, baseline, textP);

        //入职人数
        paint.setColor(entryColor);
        centerX = columnPad + columnWidth * 2 * 2 + interval * 2 + columnWidth / 2;
        canvas.drawCircle(centerX, centerY, radius, paint);
        canvas.drawText(text.get(1), centerX + interval / 2, baseline, textP);

        //离职人数
        paint.setColor(quitColor);
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
        float bottomR = pieceVertical * 6.0f;//所有柱状图的底线都是第7条线。
        int worker;
        float workerH;
        Path path = new Path();
        RectF rectF;
        for (int i = 0; i < workers.size(); i++) {
            //在职人数
            worker = workers.get(i).getWorker();
            workerH = 1.0f * worker / workerPiece * pieceVertical;
            leftR = columnPad + (columnWidth * 2 * i + interval * i);
            rightR = columnPad + (columnWidth * 2 * i + interval * i) + columnWidth;
            topR = bottomR - workerH;
            rectF = new RectF(leftR, topR, rightR, bottomR);
            paint.setColor(workerColor);
            canvas.drawRect(rectF, paint);

            //入职人数
            worker = workers.get(i).getEntryWorker();
            workerH = 1.0f * worker / workerPiece * pieceVertical;
            leftR = columnPad + (columnWidth * 2 * i + interval * i) + columnWidth;
            rightR = columnPad + (columnWidth * 2 * i + interval * i) + columnWidth * 2;
            topR = bottomR - workerH;
            rectF = new RectF(leftR, topR, rightR, bottomR);
            paint.setColor(entryColor);
            canvas.drawRect(rectF, paint);

            //离职人数
            worker = workers.get(i).getQuitWorker();
            workerH = 1.0f * worker / quitPiece * pieceVertical;
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
        paint.setColor(quitColor);
        canvas.drawPath(path, paint);

        //画折线第一个点的外圆与内圆
        worker = workers.get(0).getQuitWorker();
        workerH = 1.0f * worker / quitPiece * pieceVertical;
        leftR = columnPad + columnWidth;
        topR = bottomR - workerH;
        int radiusOut = getResources().getDimensionPixelOffset(R.dimen.wh_6);//折线外圆半径
        int radiusIn = getResources().getDimensionPixelOffset(R.dimen.wh_3);//折线内圆半径
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(android.R.color.white));
        canvas.drawCircle(leftR, topR, radiusOut, paint);
        paint.setColor(getResources().getColor(R.color.color_5d75db));
        canvas.drawCircle(leftR, topR, radiusIn, paint);
    }

    private void drawMask(Canvas canvas) {
        //矩形背景
        Rect rectBounds = new Rect(0, pieceVertical + 5, columnPad + columnWidth * 3 + interval + 5, new Double(pieceVertical * 3).intValue());
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
        paint.setColor(entryColor);
        centerY = new Float(paddingTop + textHeight * 3 / 2 + lineIntervel).intValue();
        canvas.drawCircle(centerX, centerY, radius, paint);
        baseline = paddingTop + textHeight * 2 + lineIntervel - metrics.bottom;                             //第二行的基线
        canvas.drawText(text.get(1), leftX, baseline, textP);

        //离职人数
        paint.setColor(quitColor);
        paint.setStrokeWidth(lineIntervel);
        centerY = new Float(paddingTop + textHeight * 5 / 2 + lineIntervel * 2).intValue();
        canvas.drawLine(centerX - radius * 2, centerY, centerX + radius * 2, centerY, paint);
        baseline = paddingTop + textHeight * 3 + lineIntervel * 2 - metrics.bottom;
        canvas.drawText(text.get(2), leftX, baseline, textP);

        //职工的具体人数
        WorkerMonthModel worker = workers.get(workers.size() - 1);
        textP.setTextSize(getResources().getDimension(R.dimen.text_12));
        textP.setColor(getResources().getColor(android.R.color.white));
        textP.setTextAlign(Paint.Align.RIGHT);//右对齐
        leftX = rectBounds.width() - 20;
        baseline = paddingTop + textHeight - metrics.bottom;
        canvas.drawText(worker.getWorker() + "", leftX, baseline, textP);
        baseline = paddingTop + textHeight * 2 + lineIntervel - metrics.bottom;
        canvas.drawText(worker.getEntryWorker() + "", leftX, baseline, textP);
        baseline = paddingTop + textHeight * 3 + lineIntervel * 2 - metrics.bottom;
        canvas.drawText(worker.getQuitWorker() + "", leftX, baseline, textP);
    }

}
