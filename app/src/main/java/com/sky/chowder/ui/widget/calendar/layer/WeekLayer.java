package com.sky.chowder.ui.widget.calendar.layer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.sky.chowder.R;
import com.sky.chowder.ui.widget.calendar.common.CalendarColor;
import com.sky.chowder.ui.widget.calendar.common.CalendarInfo;
import com.sky.chowder.ui.widget.calendar.common.CalendarUtil;
import com.sky.chowder.ui.widget.calendar.common.WeekInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WeekLayer implements CalendarLayer {

    private int WEEK_OF_MONTH = 6;//一月最多六周

    private Rect mainRect;//主背景
    private Rect mBorderRect;
    private Paint mPaint;

    private CalendarInfo mModeInfo;
    private int mYear, mMonth, mDay;
    private int weekIndex;//本周所在位置
    private int thisWeek = 0;//本周所在位置

    private int selectWeek = -1;//选中的周
    private int selectWeekIndex = -1;//选中周的索引

    private List<WeekInfo> weekList = new ArrayList<>();//每月包含几周
    private List<WeekInfo> selectWeeks = new ArrayList<>();//选中的周
    private List<RectF> weekRectList = new ArrayList<>();//每周所占的空间
    private HashMap<String, Float> mTextWidthMap = new HashMap<>();//每周的字体所占宽度

    private int interval = 1;                                  //周与周之间的间隔,单位 dp
    private int weekHeight;                                     //周高
    private float weekTextSize = 16;                            //小方格内数字的大小,单位 sp
    private int mTextColor;                                     //数字显示颜色
    private float mTextHeight;                                  //文字总高度(代码自动计算)
    private float mTextDescent;                                 //文字下坡值(代码自动计算)
    private int padLeft = 14;                                  //文字的左间距,单位 dp

    private Bitmap bitmap;
    private int bitmapPad = 6;

    public WeekLayer(CalendarInfo info, Resources resources) {
        //数据初始化
        mModeInfo = info;
        mainRect = info.getRect();
        mBorderRect = info.getBorderRect();
        mYear = info.getYear();
        mMonth = info.getMonth();
        mDay = info.getDay();

        weekTextSize = resources.getDimensionPixelSize(R.dimen.text_16);
        padLeft = resources.getDimensionPixelSize(R.dimen.wh_14);
        bitmapPad = resources.getDimensionPixelSize(R.dimen.wh_6);
        bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_sure_white);


        //计算周高度
        weekHeight = (mainRect.height() - interval * (WEEK_OF_MONTH - 1)) / WEEK_OF_MONTH;

        //获取源数据
        weekList = CalendarUtil.getWeekOfMonth(mYear, mMonth, mDay);
        //本月有几周
        WEEK_OF_MONTH = weekList.size();
        //重新计算高度
        mainRect.bottom = mainRect.bottom - (6 - WEEK_OF_MONTH) * (weekHeight + interval);

        //计算每周高度
        for (int i = 0; i < WEEK_OF_MONTH; i++) {
            RectF item = new RectF();
            item.left = mainRect.left;
            item.top = mainRect.top + i * (weekHeight + interval);
            item.right = mainRect.right;
            item.bottom = item.top + weekHeight;
            weekRectList.add(item);
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        //初始化每周的显示
        mPaint.setTextSize(weekTextSize);
        for (int i = 0; i < WEEK_OF_MONTH; i++) {
            String text = String.valueOf(weekList.get(i).getFormatDate());
            float textWidth = mPaint.measureText(text);
            mTextWidthMap.put(text, textWidth);
        }
        FontMetrics fontMetrics = mPaint.getFontMetrics();
        mTextHeight = fontMetrics.descent - fontMetrics.ascent;
        mTextDescent = fontMetrics.descent;
    }

    @Override
    public void onDraw(Canvas canvas) {
        //绘制月份背景
        mPaint.setStyle(Style.FILL);
        mPaint.setColor(CalendarColor.D8D8D8);
        canvas.drawRect(mainRect, mPaint);
        //绘制每周
        for (int i = 0; i < weekRectList.size(); i++) {
            RectF rect = weekRectList.get(i);
            if (isOutOfBorder(rect)) {
                continue;
            }
            mPaint.setColor(CalendarColor.WHITE);
            canvas.drawRect(rect, mPaint);

            if (weekList.get(i).isThisWeek()) {
                mTextColor = CalendarColor.PROJECT;
            } else {
                mTextColor = CalendarColor.DARK_GRAY;
            }
            //绘制天单元格里的天
            String day = String.valueOf(weekList.get(i).getFormatDate());
            float x = rect.left + padLeft;
            float y = rect.bottom - (rect.height() - mTextHeight) / 2 - mTextDescent;
            mPaint.setTextSize(weekTextSize);
            mPaint.setColor(mTextColor);
            canvas.drawText(day, x, y, mPaint);
        }
        for (int i = 0; i < selectWeeks.size(); i++) {
            WeekInfo info = selectWeeks.get(i);
            RectF rect = weekRectList.get(info.getWeekOfMonth());
            mPaint.setColor(CalendarColor.PROJECT);
            canvas.drawRect(rect,mPaint);

            mTextColor = CalendarColor.WHITE;

            String week = info.getFormatDate();
            float x = rect.left + padLeft;
            float y = rect.bottom - (rect.height() - mTextHeight) / 2 - mTextDescent;
            mPaint.setTextSize(weekTextSize);
            mPaint.setColor(mTextColor);
            canvas.drawText(week, x, y, mPaint);
            //画对号
            canvas.drawBitmap(bitmap, rect.right - bitmap.getWidth() - bitmapPad, rect.bottom - bitmap.getHeight() - bitmapPad, null);
        }


    }

    private boolean isOutOfBorder(RectF a) {
        return mBorderRect != null && (a.left > mBorderRect.right || a.right < mBorderRect.left || a.top > mBorderRect.bottom || a.bottom < mBorderRect.top);
    }

    @Override
    public void scrollBy(int dx, int dy) {
        mainRect.offset(dx, dy);
        for (int i = 0; i < weekRectList.size(); i++) {
            weekRectList.get(i).offset(dx, dy);
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

//    public int getDayByIndex(int index) {
//        return dayArray[index];
//    }

//    public int getDefaultDay() {
//        return mDefaultDay;
//    }
//
//    public void setDefaultDay(int day) {
//        if (day < 0) {
//            mDefaultDay = -1;
//            mDefaultIndex = -1;
//            return;
//        }
//        mDefaultDay = day;
//        mDefaultIndex = 0;
//        for (int i = 0; i < dayArray.length; i++) {
//            if (day == dayArray[i]) {
//                mDefaultIndex = i;
//                break;
//            }
//        }
//    }

//    public int getSelectedDay() {
//        return mSelectedDay;
//    }

//    public int getSelectedDayIndex() {
//        return mSelectedDayIndex;
//    }

    public void setSelectedDay(WeekInfo info) {
//        if (day < 0) {
//            mSelectedDay = -1;
//            mSelectedDayIndex = -1;
//            return;
//        }
//        mSelectedDay = day;
//        mSelectedDayIndex = 0;
//        for (int i = 0; i < dayArray.length; i++) {
//            if (day == dayArray[i]) {
//                mSelectedDayIndex = i;
//                break;
//            }
//        }
        boolean contains = false;
        for (int i = 0; i < selectWeeks.size(); i++) {
            if (selectWeeks.contains(info)) {
                selectWeeks.remove(info);
                contains = true;
                break;
            }
        }
        if (!contains) {
            selectWeeks.add(info);
        }
    }
//
//    public Rect getSelectedRect() {
//        RectF rectF = weekRectList.get(mSelectedDayIndex);
//        Rect rect = new Rect();
//        rect.top = (int) rectF.top;
//        rect.bottom = (int) rectF.bottom;
//        rect.right = (int) rectF.right;
//        rect.left = (int) rectF.left;
//        return rect;
//    }

//    public int[] getDays() {
//        return dayArray;
//    }

    /**
     * 根据点击位置获取索引
     */
    public int getDayIndex(int x, int y) {
        for (int i = 0; i < weekRectList.size(); i++) {
            if (weekRectList.get(i).contains(x, y)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 根据点获取时间
     */
    public WeekInfo getYMDByLocation(int x, int y) {
        int locIndex = getDayIndex(x, y);
        return weekList.get(locIndex);
    }

    /**
     * 当前week是否有这一天，有返回索引，没有返回-1
     */
    public int dayInWeekIndex(int day) {
//        for (int i = 0; i < dayArray.length; i++) {
//            if (day == dayArray[i]) {
//                return i;
//            }
//        }
        return -1;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public int getDayIndex() {
        return weekIndex;
    }
}