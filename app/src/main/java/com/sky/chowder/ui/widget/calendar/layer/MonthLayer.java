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
import com.sky.chowder.ui.widget.calendar.CalendarView;
import com.sky.chowder.ui.widget.calendar.common.CalendarColor;
import com.sky.chowder.ui.widget.calendar.common.CalendarInfo;
import com.sky.chowder.ui.widget.calendar.common.CalendarMode;
import com.sky.chowder.ui.widget.calendar.common.CalendarUtil;
import com.sky.chowder.ui.widget.calendar.selecttime.DayInfo;
import com.sky.chowder.ui.widget.calendar.selecttime.SelectTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MonthLayer implements CalendarLayer {
    private static final int WEEK = 7;

    private Rect mainRect;//整个view的所占空间
    private Rect mBorderRect;//view的边
    private CalendarInfo mModeInfo;
    private int year, month, mDay;
    private Paint paint;

    private int[] daysArray;//需要布局的月份数据，首尾可能包含上下两个月的
    private int[] mIndexArea = new int[2];//所选月份起终日所在位置
    private List<RectF> mDayRectList = new ArrayList<>();           //包含的天数所占的区域rect
    private HashMap<String, Float> mDayTextWidth = new HashMap<>();
    private int today = -1;//今天
    private int todayIndex = -1;//今天天在数据中的所在位置

    private int lastSelectedDay = -1;//最后一次选中的日期
    private int lastDayIndex = -1;//最后一次选中的日期在数据中的所在位置

    private int saturday, sunday;//周六日

    private int interval = 1;                                  //方块之间的间隔,单位 dp
    private int mDayWidth;                                      //小方格的宽度(代码自动算出)
    private int mDayHeight;                                     //小方格的高度(代码自动算出)
    private float mDayTextSize = 14;                            //小方格内数字的大小,单位 sp

    private int mTextColor;                                     //数字显示颜色
    private float mTextHeight;                                  //文字总高度(代码自动计算)

    private Bitmap bitmap;
    private int bitmapPad = 6;

    public MonthLayer(CalendarInfo info, Resources resources) {
        mModeInfo = info;
//        interval = resources.getDimensionPixelSize(R.dimen.wh_1);
        mDayTextSize = resources.getDimensionPixelSize(R.dimen.text_14);
        bitmapPad = resources.getDimensionPixelSize(R.dimen.wh_6);
        bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_sure_white);

        //动态计算周末开始的序号
        saturday = (7 - CalendarView.FIRST_DAY) % 7;
        sunday = (saturday + 1) % 7;

        //数据初始化
        mainRect = info.getRect();
        mBorderRect = info.getBorderRect();
        year = info.getYear();
        month = info.getMonth();
        mDay = info.getDay();
        mDayWidth = (mainRect.width() - interval * (WEEK - 1)) / WEEK;
        mDayHeight = mDayWidth;
        if (mDay <= 0) {
            mDay = 1;
        }

        //获取日历
        daysArray = CalendarUtil.getDayOfMonth(year, month, true, mIndexArea);
//        setSelectedDay(mDay);//选中天

        //重新计算背景框的高度，同事剔除多余的数据
        mainRect.bottom = mainRect.bottom - getExtraSpace();

        //初始化每天所在区域rect
        for (int i = 0; i < 42; i++) {
            RectF day = new RectF();
            day.left = mainRect.left + (i % WEEK) * (mDayWidth + interval);
            day.top = mainRect.top + (i / WEEK) * (mDayHeight + interval);
            day.right = day.left + mDayWidth;
            day.bottom = day.top + mDayHeight;
            mDayRectList.add(day);
        }

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(mDayTextSize);
        paint.setStyle(Paint.Style.FILL);

        //保存文字宽度
        for (int i = 0; i <= 31; i++) {
            String text = String.valueOf(i);
            mDayTextWidth.put(text, paint.measureText(text));
        }

        mTextHeight = paint.descent() - paint.ascent();
    }

    /**
     * 获取多余的空间的高度，总共是6*7个格子，有可能只有4*7，5*7
     */
    public int getExtraSpace() {
        int count = (mIndexArea[1] + 1) / 7;
        if ((mIndexArea[1] + 1) % 7 > 0) {
            count++;
        }
        for (int i = count * WEEK; i < daysArray.length; i++) {
            daysArray[i] = 0;
        }
        return (6 - count) * (mDayHeight + interval);
    }

    @Override
    public void onDraw(Canvas canvas) {
        //绘制月份背景
        paint.setColor(CalendarColor.D8D8D8);
        canvas.drawRect(mainRect, paint);
        //绘制每日
        for (int i = 0; i < mDayRectList.size(); i++) {
            RectF rect = mDayRectList.get(i);
            //判断数据是否有误，以及区域是否超限
            if (daysArray[i] <= 0 || isOutOfBorder(rect)) {
                continue;
            }
            paint.setColor(CalendarColor.WHITE);
            canvas.drawRect(rect, paint);
            //选中月日期的颜色
            if (i >= mIndexArea[0] && i <= mIndexArea[1]) {
                //当天日期的颜色
                if (today > 0 && todayIndex == i) {
                    mTextColor = CalendarColor.PROJECT;
                } else if (i % 7 == saturday || i % 7 == sunday) {
                    mTextColor = CalendarColor.LIGHT_GRAY;
                } else {
                    mTextColor = CalendarColor.DARK_GRAY;
                }
            } else {
                mTextColor = CalendarColor.LIGHT_GRAY;
            }
            //绘制文字
            drawText(canvas, daysArray[i], rect);
        }
        //绘制选中日期
//        List<DayInfo> selects = SelectTime.getInstance().getSelectDayList(year ,month);
        List<DayInfo> selects = (List<DayInfo>) SelectTime.getInstance().getSelectTime().getList(year, month);
        if (selects == null) return;
        for (int i = 0; i < selects.size(); i++) {
            DayInfo selectDay = selects.get(i);
            RectF rect = mDayRectList.get(selectDay.getIndex());

            //绘制选中背景
            paint.setColor(CalendarColor.PRIMARY_COLOR);
            canvas.drawRect(rect, paint);

            //绘制文字
            mTextColor = CalendarColor.WHITE;
            drawText(canvas, selectDay.getInfo().getDay(), rect);
            //画对号
            canvas.drawBitmap(bitmap, rect.right - bitmap.getWidth() - bitmapPad, rect.bottom - bitmap.getHeight() - bitmapPad, null);
        }
    }

    //绘制天单元格里的天
    private void drawText(Canvas canvas, int i, RectF rect) {
        String day = String.valueOf(i);
        float x = rect.left + (rect.width() - mDayTextWidth.get(day)) / 2;
        float baseline = rect.bottom - (rect.height() - mTextHeight) / 2 - paint.descent();
        paint.setColor(mTextColor);
        canvas.drawText(day, x, baseline, paint);
    }

    private boolean isOutOfBorder(RectF a) {
        return mBorderRect != null && (a.left > mBorderRect.right || a.right < mBorderRect.left || a.top > mBorderRect.bottom || a.bottom < mBorderRect.top);
    }

    @Override
    public void scrollBy(int dx, int dy) {
        mainRect.offset(dx, dy);
        for (int i = 0; i < mDayRectList.size(); i++) {
            mDayRectList.get(i).offset(dx, dy);
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

    /**
     * 根据索引获取天
     */
    public int getDayByIndex(int index) {
        if (index < 0 || index > daysArray.length) {
            return -1;
        }
        return daysArray[index];
    }

    public void setToday(int day) {
        if (day < 0) {
            today = -1;
            todayIndex = -1;
            return;
        }
        today = day;
        todayIndex = 0;
        for (int i = mIndexArea[0]; i <= mIndexArea[1]; i++) {
            if (day == daysArray[i]) {
                todayIndex = i;
                break;
            }
        }
    }

    public void setSelectedDay(int day) {
        if (day < 0) {
            lastSelectedDay = -1;
            lastDayIndex = -1;
            return;
        }
        lastSelectedDay = day;
        lastDayIndex = 0;
        for (int i = mIndexArea[0]; i <= mIndexArea[1]; i++) {
            if (day == daysArray[i]) {
                lastDayIndex = i;
                break;
            }
        }
        SelectTime.getInstance().getSelectTime().add(new DayInfo(new CalendarInfo(year, month, day), "", lastDayIndex));
    }

    /**
     * 根据点获取时间
     */
    public CalendarInfo getYMDByLocation(int x, int y) {
        int locIndex = getDayIndex(x, y);
        if (locIndex <= 0) {
            return null;
        }
        return new CalendarInfo(year, month, daysArray[locIndex], CalendarMode.MONTH);
    }

    /**
     * 根据点击位置获取索引
     */
    public int getDayIndex(int x, int y) {
        int index = -1;
        for (int i = 0; i < mDayRectList.size(); i++) {
            if (mDayRectList.get(i).contains(x, y)) {
                index = i;
            }
        }
        if (index >= mIndexArea[0] && index <= mIndexArea[1]) {
            return index;
        }
        return -1;
    }

    @Override
    public boolean onTouchEvent(MotionEvent evrtbent) {
        return false;
    }
}