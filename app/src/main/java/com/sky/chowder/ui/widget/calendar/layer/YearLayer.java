package com.sky.chowder.ui.widget.calendar.layer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.sky.chowder.R;
import com.sky.chowder.ui.widget.calendar.common.CalendarColor;
import com.sky.chowder.ui.widget.calendar.common.CalendarInfo;
import com.sky.chowder.ui.widget.calendar.common.CalendarMode;
import com.sky.chowder.ui.widget.calendar.impl.OnClickListener;
import com.sky.chowder.ui.widget.calendar.selecttime.SelectTime;
import com.sky.chowder.ui.widget.calendar.selecttime.YearInfo;

import java.util.ArrayList;
import java.util.List;

public class YearLayer implements CalendarLayer {

    private static final String YEAR = "年";
    private static final int ROW_COUNT = 2;//2行
    private static final int COL_COUNT = 6;//6列

    private CalendarInfo mModeInfo;
    private Rect mainRect;
    private Rect mBorderRect;
    private Paint paint;
    private int mYear;

    private int interval = 1;                       //年与年之间的间隔,单位 dp
    private int yearTextSize = 14;                  //字体大小

    private int textColor = CalendarColor.DARK_GRAY;  //字体默认颜色

    private int thisMonth = -1;//本月

    private Bitmap bitmap;
    private int bitmapPad = 6;

    private List<String> years = new ArrayList<>(); //年份中的月
    private List<RectF> yearRects = new ArrayList<>(); //年的空间

    public YearLayer(CalendarInfo info, Resources resources) {
        mModeInfo = info;
        mainRect = mModeInfo.getRect();
        mYear = mModeInfo.getYear();
        mBorderRect = info.getBorderRect();
        yearTextSize = resources.getDimensionPixelSize(R.dimen.text_14);
        bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_sure_white);

        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        //宽高
        int itemWidth = (mainRect.width() - interval * (COL_COUNT - 1)) / COL_COUNT;
        int itemHeight = (mainRect.height() - interval * (ROW_COUNT - 1)) / ROW_COUNT;

        //年份中每月的显示区域
        for (int i = 0; i < 12; i++) {
            RectF rect = new RectF();
            rect.left = mainRect.left + (i % COL_COUNT) * (itemWidth + interval);
            rect.top = mainRect.top + (i / COL_COUNT) * (itemHeight + interval);
            rect.right = rect.left + itemWidth;
            rect.bottom = rect.top + itemHeight;
            yearRects.add(rect);
            years.add((i + 1) + "月");
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
//        if (isOutOfBorder(mainRect)) {
//            return;
//        }
        paint.setColor(CalendarColor.D8D8D8);
        paint.setTextSize(yearTextSize);
        //绘制白色背景
        canvas.drawRect(mainRect, paint);


        int limit = SelectTime.getInstance().getLimit();//时间选择限制
        int start = getlastYearStart(limit);//去年需要选中的开始月份
        //绘制每月
        for (int i = 0; i < yearRects.size(); i++) {
            if (isOutOfBorder(yearRects.get(i))) {
                continue;
            }
            RectF rect = yearRects.get(i);
            paint.setColor(CalendarColor.WHITE);
            canvas.drawRect(rect, paint);

            if (i == thisMonth) {
                textColor = CalendarColor.PROJECT;
            } else if (thisMonth > 0 && thisMonth - limit <= i && i < thisMonth) {
                textColor = CalendarColor.DARK_GRAY;
            } else if (start > 0 && i >= start) {//去年是否有可选择的月份
                textColor = CalendarColor.DARK_GRAY;
            } else {
                textColor = CalendarColor.LIGHT_GRAY;
            }


            paint.setColor(textColor);
            String text = years.get(i);
            float tw = paint.measureText(text);
            float th = paint.descent() - paint.ascent();

            float x = rect.left + (rect.width() - tw) / 2;
            float baseline = rect.bottom - (rect.height() - th) / 2 - paint.descent();
            canvas.drawText(text, x, baseline, paint);
        }
        //绘制选中的月份
        List<YearInfo> selectYears = (List<YearInfo>) SelectTime.getInstance().getSelectTime().getList(mYear, 0);
        if (selectYears == null) return;
        for (int i = 0; i < selectYears.size(); i++) {
            int index = selectYears.get(i).getIndex();
            RectF rect = yearRects.get(index);
            paint.setColor(CalendarColor.PROJECT);
            canvas.drawRect(rect, paint);

            textColor = CalendarColor.WHITE;
            paint.setColor(textColor);
            String text = years.get(index);
            float tw = paint.measureText(text);
            float th = paint.descent() - paint.ascent();

            float x = rect.left + (rect.width() - tw) / 2;
            float baseline = rect.bottom - (rect.height() - th) / 2 - paint.descent();
            canvas.drawText(text, x, baseline, paint);
            //画对号
            canvas.drawBitmap(bitmap, rect.right - bitmap.getWidth() - bitmapPad, rect.bottom - bitmap.getHeight() - bitmapPad, null);
        }

    }

    private int getlastYearStart(int limit) {
        int start = -1;
        //判断去年是否有需要选中的月份
        if (thisMonth < 0) {
            CalendarInfo today = SelectTime.getInstance().getToday();
            if (mYear + 1 == today.getYear() && today.getMonth() - limit < 0) {
                start = 11 + (today.getMonth() - limit) + 1;//去年需要选中的开始月份（去年可选择的个数，）
            }
        }
        return start;
    }

    @Override
    public void scrollBy(int dx, int dy) {
        mainRect.offset(dx, dy);
        for (int i = 0; i < yearRects.size(); i++) {
            yearRects.get(i).offset(dx, dy);
        }
    }

    @Override
    public Rect getBorderRect() {
        return mainRect;
    }

    @Override
    public CalendarInfo getModeInfo() {
        return mModeInfo;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    private boolean isOutOfBorder(RectF rect) {
        return mBorderRect != null && (rect.left > mBorderRect.right || rect.right < mBorderRect.left || rect.top > mBorderRect.bottom || rect.bottom < mBorderRect.top);
    }

    public int getOnClickIndex(int x, int y) {
        int index = -1;
        for (int i = 0; i < yearRects.size(); i++) {
            if (yearRects.get(i).contains(x, y)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public CalendarInfo getYMByLocation(int x, int y) {
        int year = mYear;
        int month = getOnClickIndex(x, y);
        return new CalendarInfo(year, month, 1, CalendarMode.MONTH);
    }

    public void selectMonth(int x, int y) {
        int index = getOnClickIndex(x, y);
        int limit = SelectTime.getInstance().getLimit();
        int start = getlastYearStart(limit);
        //今年和去年可选择的月份
        if ((thisMonth >= 0 && thisMonth - limit <= index && index <= thisMonth)
                || (start > 0 && index >= start)) {
            CalendarInfo info = new CalendarInfo(mYear, index, 1, CalendarMode.MONTH);
            SelectTime.getInstance().getSelectTime().add(new YearInfo(info, "", index));
        } else {
            if (onClick != null) {
                onClick.onClick("超出选择范围");
            }
        }
    }

    public void setThisMonth(int thisMonth) {
        this.thisMonth = thisMonth;
    }

    private OnClickListener onClick;

    public void setOnClickListener(OnClickListener onClickListener) {
        onClick = onClickListener;
    }
}