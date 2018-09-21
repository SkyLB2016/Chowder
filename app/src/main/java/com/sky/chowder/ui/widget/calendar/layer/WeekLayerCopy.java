package com.sky.chowder.ui.widget.calendar.layer;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.MotionEvent;

import com.sky.chowder.ui.widget.calendar.CalendarView;
import com.sky.chowder.ui.widget.calendar.common.CalendarColor;
import com.sky.chowder.ui.widget.calendar.common.CalendarInfo;
import com.sky.chowder.ui.widget.calendar.common.CalendarMode;
import com.sky.chowder.ui.widget.calendar.common.CalendarUtil;
import com.sky.chowder.ui.widget.calendar.lunar.LunarData;
import com.sky.chowder.ui.widget.calendar.lunar.LunarInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class WeekLayerCopy implements CalendarLayer {

    private static final int COL_COUNT = 7;
    private static final String HOLIDAY = "假";

    private Rect mRect;
    private Rect mBorderRect;
    private Paint mPaint;
    private CalendarInfo mModeInfo;
    private int mYear, mMonth, mDay;
    private int mDayIndex;

    private int[] mWeekDays;
    private int mDefaultDay = -1;
    private int mDefaultIndex = -1;
    private int mSelectedDay = -1;//选中的天
    private int mSelectedDayIndex = -1;//选中的天的索引
    private int first, second;

    private List<RectF> mDayRectList = new ArrayList<>();
    private SparseIntArray mLabelList = new SparseIntArray();
    private List<LunarInfo> mLunInfoList = new ArrayList<>();
    private HashMap<String, Float> mTextWidthMap = new HashMap<>();

    private Rect mRectDay = new Rect();
    private Rect mRectLun = new Rect();
    private Rect mRectHoliday = new Rect();
    private Rect mRectLabel = new Rect();

    private int mDaySpace = 2;                                  //天与天之间的间隔,单位 dp
    private int mDayWidth;                                      //小方格的宽度(代码自动算出)
    private int mDayHeight;                                     //小方格的高度(代码自动算出)
    private int mDayPaddingTop = 4;                             //小方格内部与顶部的padding
    private float mDayTextSize = 16;                            //小方格内数字的大小,单位 sp
    private float mLabelRadius = 2.5f;                          //小圆点的半径,单位 dp
    private int mLabelBgColor = CalendarColor.PRIMARY_COLOR;    //小圆点的背景色
    private int mLabelPaddingBottom = 5;                        //小圆点与底部的距离,单位 dp
    private float mLabelSpace = 2.5f;                           //小圆点之间的距离,单位 dp
    private int mHolidayRadius = 7;                             //假期的半径,单位 dp
    private float mHolidayTextSize = 8;                         //假期的文字大小,单位 sp
    private int mHolidayBgColor = CalendarColor.ORIGIN;         //假期的背景色
    private int mHolidayBorderColor = CalendarColor.ORIGIN_BORDER;  //假期的背景色
    private float mHolidayBorderWidth = 0.5f;                   //假期边框宽度
    private int mHolidayTextColor = CalendarColor.WHITE;        //假期的文本颜色
    private int mHolidayTextHeight;                             //文字总高度(代码自动计算)
    private int mHolidayTextDescent;                            //文字下坡值(代码自动计算)
    private int mTextColor;                                     //数字显示颜色
    private float mTextHeight;                                  //文字总高度(代码自动计算)
    private float mTextDescent;                                 //文字下坡值(代码自动计算)
    private int mLunPaddingTop = 2;                             //农历与数字的间距,单位 dp
    private int mLunTextColor;                                  //农历文本颜色
    private int mLunTextHeight;                                 //文字总高度(代码自动计算)
    private int mLunTextDescent;                                //文字下坡值(代码自动计算)
    private float mLunTextSize = 10;                            //农历文字大小,单位 sp

    public WeekLayerCopy(CalendarInfo info, Resources resources) {
        mModeInfo = info;
        DisplayMetrics metrics = resources.getDisplayMetrics();
        mDaySpace = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mDaySpace, metrics);
        mLabelRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mLabelRadius, metrics);
        mDayPaddingTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mDayPaddingTop, metrics);
        mLunPaddingTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mLunPaddingTop, metrics);
        mLabelPaddingBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mLabelPaddingBottom, metrics);
        mLabelSpace = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mLabelSpace, metrics);
        mHolidayRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mHolidayRadius, metrics);
        mHolidayBorderWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mHolidayBorderWidth, metrics);
        mDayTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mDayTextSize, metrics);
        mLunTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mLunTextSize, metrics);
        mHolidayTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mHolidayTextSize, metrics);

        //动态计算周末开始的序号
        first = (7 - CalendarView.FIRST_DAY) % 7;
        second = (first + 1) % 7;
        //数据初始化
        mRect = info.getRect();
        mBorderRect = info.getBorderRect();
        mYear = info.getYear();
        mMonth = info.getMonth();
        mDay = info.getDay();

        mDayWidth = (mRect.width() - mDaySpace * (COL_COUNT - 1)) / COL_COUNT;
        mDayHeight = mDayWidth;
        mWeekDays = CalendarUtil.getDayOfWeek(mYear, mMonth, mDay);

        for (int i = 0; i < mWeekDays.length; i++) {
            if (mDay == mWeekDays[i]) {
                mDayIndex = i;
                break;
            }
        }

        for (int i = 0; i < COL_COUNT; i++) {
            RectF item = new RectF();
            item.left = mRect.left + i * (mDayWidth + mDaySpace);
            item.top = mRect.top;
            item.right = item.left + mDayWidth;
            item.bottom = item.top + mDayHeight;
            mDayRectList.add(item);
            mLunInfoList.add(getLunInfoByIndex(i));
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        //初始化每天的显示
        mPaint.setTextSize(mDayTextSize);
        for (int i = 0; i < COL_COUNT; i++) {
            String text = String.valueOf(mWeekDays[i]);
            float textWidth = mPaint.measureText(text);
            mTextWidthMap.put(text, textWidth);
        }
        FontMetrics fontMetrics = mPaint.getFontMetrics();
        mTextHeight = fontMetrics.descent - fontMetrics.ascent;
        mTextDescent = fontMetrics.descent;
        //初始化农历的显示
        mPaint.setTextSize(mLunTextSize);
        fontMetrics = mPaint.getFontMetrics();
        mLunTextHeight = (int) (fontMetrics.descent - fontMetrics.ascent);
        mLunTextDescent = (int) fontMetrics.descent;
        //初始化假显示
        mPaint.setTextSize(mHolidayTextSize);
        fontMetrics = mPaint.getFontMetrics();
        mHolidayTextHeight = (int) (fontMetrics.descent - fontMetrics.ascent);
        mHolidayTextDescent = (int) fontMetrics.descent;
    }

    private LunarInfo getLunInfoByIndex(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(mYear, mMonth, mDay);
        int index = mDayIndex;
        calendar.add(Calendar.DAY_OF_MONTH, i - index);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return LunarData.getInstance().getLunarInfo(year, month, day);
    }

    @Override
    public void onDraw(Canvas canvas) {
        //绘制月份背景
        mPaint.setStyle(Style.FILL);
        mPaint.setColor(CalendarColor.WHITE);
        canvas.drawRect(mRect, mPaint);
        //绘制每日
        for (int i = 0; i < mDayRectList.size(); i++) {
            RectF rect = mDayRectList.get(i);
            if (isOutOfBorder(rect)) {
                continue;
            }

            if (mDefaultDay > 0 && mDefaultIndex == i && mSelectedDayIndex != mDefaultIndex) {
                //绘制今天
                mPaint.setStyle(Style.FILL);
                mPaint.setColor(CalendarColor.TITLE_GRAY);
                canvas.drawRoundRect(rect, mLabelRadius, mLabelRadius, mPaint);
                mTextColor = CalendarColor.WHITE;
                mLunTextColor = CalendarColor.WHITE;
            } else if (mSelectedDay > 0 && mSelectedDayIndex == i) {
                //绘制选中
                mPaint.setStyle(Style.FILL);
                mPaint.setColor(CalendarColor.PRIMARY_COLOR);
                canvas.drawRoundRect(rect, mLabelRadius, mLabelRadius, mPaint);
                mTextColor = CalendarColor.WHITE;
                mLunTextColor = CalendarColor.WHITE;
            } else {
                mPaint.setStyle(Style.FILL);
                mPaint.setColor(CalendarColor.WHITE);
                canvas.drawRect(rect, mPaint);
                if (i % 7 == first || i % 7 == second) {
                    mTextColor = CalendarColor.LIGHT_GRAY;
                    mLunTextColor = CalendarColor.LIGHT_GRAY;
                } else {
                    mTextColor = CalendarColor.DARK_GRAY;
                    mLunTextColor = CalendarColor.LIGHT_GRAY;
                }
            }
            //绘制天单元格里的天
            String day = String.valueOf(mWeekDays[i]);
            mRectDay.set((int) rect.left, (int) rect.top + mDayPaddingTop, (int) rect.right, (int) (rect.top + mDayPaddingTop + mTextHeight));
            float x = mRectDay.left + (mRectDay.width() - mTextWidthMap.get(day)) / 2;
            float y = mRectDay.bottom - mTextDescent;
            mPaint.setStyle(Style.FILL);
            mPaint.setTextSize(mDayTextSize);
            mPaint.setColor(mTextColor);
            canvas.drawText(day, x, y, mPaint);
            //绘制农历
            LunarInfo lun = mLunInfoList.get(i);
            mRectLun.set((int) rect.left, mRectDay.bottom + mLunPaddingTop, (int) rect.right, mRectDay.bottom + mLunPaddingTop + mLunTextHeight);
            mPaint.setTextSize(mLunTextSize);
            String text = lun.showStr;
            x = mRectLun.left + (mRectLun.width() - mPaint.measureText(text)) / 2;
            y = mRectLun.bottom - mLunTextDescent;
            if (lun.isFestival) {
                mLunTextColor = CalendarColor.ORIGIN;
            }
            if (mSelectedDay > 0 && mSelectedDayIndex == i) {
                mLunTextColor = CalendarColor.WHITE;
            }
            mPaint.setColor(mLunTextColor);
            canvas.drawText(text, x, y, mPaint);
            Typeface typeface = mPaint.getTypeface();
            if (lun.isHoliday) {
                //绘制假期标签
                mRectHoliday.set((int) (rect.right - mHolidayRadius * 2), (int) rect.top, (int) rect.right, (int) (rect.top + mHolidayRadius * 2));
                mPaint.setColor(mHolidayBgColor);
                canvas.drawCircle(mRectHoliday.centerX(), mRectHoliday.centerY(), mHolidayRadius, mPaint);
                mPaint.setColor(mHolidayBorderColor);
                mPaint.setStyle(Style.STROKE);
                mPaint.setStrokeWidth(mHolidayBorderWidth);
                canvas.drawCircle(mRectHoliday.centerX(), mRectHoliday.centerY(), mHolidayRadius - mHolidayBorderWidth / 2, mPaint);
                mPaint.setStyle(Style.FILL);
                mPaint.setColor(mHolidayTextColor);
                mPaint.setTextSize(mHolidayTextSize);
                mPaint.setTypeface(Typeface.DEFAULT_BOLD);
                x = mRectHoliday.left + (mRectHoliday.width() - mPaint.measureText(HOLIDAY)) / 2;
                y = (mRectHoliday.centerY() + mHolidayTextHeight / 2) - mHolidayTextDescent;
                canvas.drawText(HOLIDAY, x, y, mPaint);
            }
            mPaint.setTypeface(typeface);
            //绘制标签
            mRectLabel.set((int) rect.left, (int) (rect.bottom - mLabelPaddingBottom - mLabelRadius * 2), (int) rect.right, (int) rect.bottom - mLabelPaddingBottom);
            mPaint.setColor(mLabelBgColor);
            if (mLabelList.indexOfKey(i) >= 0) {
                mPaint.setColor(mLabelBgColor);
                int num = Math.min(mLabelList.get(i), 3);   //最多展示3个小圆点
                float center = rect.centerX() - (num - 1) * (mLabelRadius + mLabelSpace / 2);
                for (int n = 0; n < num; n++) {
                    canvas.drawCircle(center, mRectLabel.top + mLabelRadius, mLabelRadius, mPaint);
                    center = center + mLabelRadius * 2 + mLabelSpace;
                }
            }
        }
    }

    private boolean isOutOfBorder(RectF a) {
        return mBorderRect != null && (a.left > mBorderRect.right || a.right < mBorderRect.left || a.top > mBorderRect.bottom || a.bottom < mBorderRect.top);
    }

    @Override
    public void scrollBy(int dx, int dy) {
        mRect.offset(dx, dy);
        for (int i = 0; i < mDayRectList.size(); i++) {
            mDayRectList.get(i).offset(dx, dy);
        }
    }

    @Override
    public Rect getBorderRect() {
        return mRect;
    }

    @Override
    public CalendarInfo getModeInfo() {
        return mModeInfo;
    }

    public int getDayByIndex(int index) {
        return mWeekDays[index];
    }

    public int getDefaultDay() {
        return mDefaultDay;
    }

    public void setDefaultDay(int day) {
        if (day < 0) {
            mDefaultDay = -1;
            mDefaultIndex = -1;
            return;
        }
        mDefaultDay = day;
        mDefaultIndex = 0;
        for (int i = 0; i < mWeekDays.length; i++) {
            if (day == mWeekDays[i]) {
                mDefaultIndex = i;
                break;
            }
        }
    }

    public int getSelectedDay() {
        return mSelectedDay;
    }

    public int getSelectedDayIndex() {
        return mSelectedDayIndex;
    }

    public void setSelectedDay(int day) {
        if (day < 0) {
            mSelectedDay = -1;
            mSelectedDayIndex = -1;
            return;
        }
        mSelectedDay = day;
        mSelectedDayIndex = 0;
        for (int i = 0; i < mWeekDays.length; i++) {
            if (day == mWeekDays[i]) {
                mSelectedDayIndex = i;
                break;
            }
        }
    }

    public Rect getSelectedRect() {
        RectF rectF = mDayRectList.get(mSelectedDayIndex);
        Rect rect = new Rect();
        rect.top = (int) rectF.top;
        rect.bottom = (int) rectF.bottom;
        rect.right = (int) rectF.right;
        rect.left = (int) rectF.left;
        return rect;
    }

    public int[] getDays() {
        return mWeekDays;
    }

    /** 根据点击位置获取索引 */
    public int getDayIndex(int x, int y) {
        for (int i = 0; i < mDayRectList.size(); i++) {
            if (mDayRectList.get(i).contains(x, y)) {
                return i;
            }
        }
        return -1;
    }

    /** 根据点获取时间 */
    public CalendarInfo getYMDByLocation(int x, int y) {
        int year = mYear;
        int month = mMonth;
        int day = mDay;
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, day);
        int locIndex = getDayIndex(x, y);
        calendar.add(Calendar.DAY_OF_MONTH, locIndex - mDayIndex);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        return new CalendarInfo(year, month, day, CalendarMode.MONTH);
    }

    /** 当前week是否有这一天，有返回索引，没有返回-1 */
    public int dayInWeekIndex(int day) {
        for (int i = 0; i < mWeekDays.length; i++) {
            if (day == mWeekDays[i]) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public List<LunarInfo> getLunarInfoList() {
        return mLunInfoList;
    }

    public int getDayIndex() {
        return mDayIndex;
    }

    public void addData(Integer key, Integer value) {
        mLabelList.put(key, value);
    }

    public void clearData() {
        mLabelList.clear();
    }
}