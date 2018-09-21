package com.sky.chowder.ui.widget.calendar.layer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.sky.chowder.R;
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

public class MonthLayer implements CalendarLayer {
    private static final int WEEK = 7;

    private Rect mainRect;//整个view的所占空间
    private Rect mBorderRect;//view的边
    private CalendarInfo mModeInfo;
    private int mYear, mMonth, mDay;
    private Paint mPaint;

    private int[] mDayArray;//需要布局的月份数据，首尾可能包含上下两个月的
    private int[] mIndexArea = new int[2];//所选月份起终日所在位置
    private List<RectF> mDayRectList = new ArrayList<>();           //包含的天数所占的区域rect
    //    private List<LunarInfo> mDayLunarInfoList = new ArrayList<>();
    private HashMap<String, Float> mDayTextWidth = new HashMap<>();
    private int mDefaultDay = -1;//今天
    private int mDefaultIndex = -1;//今天在数据中的所在位置

    private List<SelectDay> selectDayList = new ArrayList<SelectDay>();//选中天
    private int lastSelectedDay = -1;//最后一次选中的日期
    private int lastDayIndex = -1;//最后一次选中的日期在数据中的所在位置

    private int first, second;

    private int interval = 1;                                  //方块之间的间隔,单位 dp
    private int mDayWidth;                                      //小方格的宽度(代码自动算出)
    private int mDayHeight;                                     //小方格的高度(代码自动算出)
    //    private int mDayPaddingTop = 4;                             //小方格内部与顶部的padding
    private float mDayTextSize = 14;                            //小方格内数字的大小,单位 sp
//    private float fillet = 2.5f;                                //圆角

    private int mTextColor;                                     //数字显示颜色
    private float mTextHeight;                                  //文字总高度(代码自动计算)
    private float mTextDescent;                                 //文字下坡值(代码自动计算)

    private Bitmap bitmap;
    private int bitmapPad = 6;

    public MonthLayer(CalendarInfo info, Resources resources) {
        mModeInfo = info;
//        interval = resources.getDimensionPixelSize(R.dimen.wh_1);
        mDayTextSize = resources.getDimensionPixelSize(R.dimen.text_14);
        bitmapPad = resources.getDimensionPixelSize(R.dimen.wh_6);
        bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_sure_white);


        //动态计算周末开始的序号
        first = (7 - CalendarView.FIRST_DAY) % 7;
        second = (first + 1) % 7;

        //数据初始化
        mainRect = info.getRect();
        mBorderRect = info.getBorderRect();
        mYear = info.getYear();
        mMonth = info.getMonth();
        mDay = info.getDay();
        mDayWidth = (mainRect.width() - interval * (WEEK - 1)) / WEEK;
        mDayHeight = mDayWidth;
        if (mDay <= 0) {
            mDay = 1;
        }

        //获取日历
        mDayArray = CalendarUtil.getDayOfMonth(mYear, mMonth, true, mIndexArea);
        setDefaultDay(mDay);//默认今天
        setSelectedDay(mDay);//选中今天

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
//            mDayLunarInfoList.add(getLunInfoByIndex(i));
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mDayTextSize);
        //保存文字宽度
        for (int i = 0; i <= 31; i++) {
            String text = String.valueOf(i);
            mDayTextWidth.put(text, mPaint.measureText(text));
        }

//        FontMetrics fontMetrics = mPaint.getFontMetrics();
        mTextHeight = mPaint.descent() - mPaint.ascent();
        mTextDescent = mPaint.descent();
    }

    /**
     * 获取多余的空间的高度，总共是6*7个格子，有可能只有4*7，5*7
     */
    public int getExtraSpace() {
        int count = (mIndexArea[1] + 1) / 7;
        if ((mIndexArea[1] + 1) % 7 > 0) {
            count++;
        }
        for (int i = count * WEEK; i < mDayArray.length; i++) {
            mDayArray[i] = 0;
        }
        return (6 - count) * (mDayHeight + interval);
    }

    /**
     * 根据和今天的天数差额,获取农历信息
     */
    private LunarInfo getLunInfoByIndex(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(mYear, mMonth, mDay);
//        calendar.add(Calendar.DAY_OF_MONTH, i - lastDayIndex);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return LunarData.getInstance().getLunarInfo(year, month, day);
    }

    @Override
    public void onDraw(Canvas canvas) {
        //绘制月份背景
        mPaint.setStyle(Style.FILL);
        mPaint.setColor(CalendarColor.D8D8D8);
        canvas.drawRect(mainRect, mPaint);
        //绘制每日
        for (int i = 0; i < mDayRectList.size(); i++) {
            RectF rect = mDayRectList.get(i);
            //判断数据是否有误，以及区域是否超限
            if (mDayArray[i] <= 0 || isOutOfBorder(rect)) {
                continue;
            }
            mPaint.setColor(CalendarColor.WHITE);
            canvas.drawRect(rect, mPaint);
            //选中月日期的颜色
            if (i >= mIndexArea[0] && i <= mIndexArea[1]) {
                //当天日期的颜色
                if (mDefaultDay > 0 && mDefaultIndex == i) {
                    mTextColor = CalendarColor.PROJECT;
                } else if (i % 7 == first || i % 7 == second) {
                    mTextColor = CalendarColor.LIGHT_GRAY;
                } else {
                    mTextColor = CalendarColor.DARK_GRAY;
                }
            } else {
                mTextColor = CalendarColor.LIGHT_GRAY;
            }
            //绘制天单元格里的天
            String day = String.valueOf(mDayArray[i]);
            float x = rect.left + (rect.width() - mDayTextWidth.get(day)) / 2;
            float y = rect.bottom - (rect.height() - mTextHeight) / 2 - mTextDescent;
            mPaint.setTextSize(mDayTextSize);
            mPaint.setColor(mTextColor);
            canvas.drawText(day, x, y, mPaint);
        }
        //绘制选中日期
        for (int i = 0; i < selectDayList.size(); i++) {
            SelectDay selectDay = selectDayList.get(i);
            RectF rect = mDayRectList.get(selectDay.index);

            mPaint.setStyle(Style.FILL);
            mPaint.setColor(CalendarColor.PRIMARY_COLOR);
            canvas.drawRect(rect, mPaint);
            mTextColor = CalendarColor.WHITE;

            String day = String.valueOf(selectDay.info.getDay());
            float x = rect.left + (rect.width() - mDayTextWidth.get(day)) / 2;
            float y = rect.bottom - (rect.height() - mTextHeight) / 2 - mTextDescent;
            mPaint.setTextSize(mDayTextSize);
            mPaint.setColor(mTextColor);
            canvas.drawText(day, x, y, mPaint);
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
        if (index < 0 || index > mDayArray.length) {
            return -1;
        }
        return mDayArray[index];
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
        for (int i = mIndexArea[0]; i <= mIndexArea[1]; i++) {
            if (day == mDayArray[i]) {
                mDefaultIndex = i;
                break;
            }
        }
    }

    public List<SelectDay> getSelectDayList() {
        return selectDayList;
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
            if (day == mDayArray[i]) {
                lastDayIndex = i;
                break;
            }
        }
        String lable = mYear + "年" + mMonth + "月" + day + "日";
        boolean contains = false;
        for (int i = 0; i < selectDayList.size(); i++) {
            if (selectDayList.get(i).lable.equals(lable)) {
                contains = true;
                selectDayList.remove(i);
                break;
            }
        }
        if (!contains) {
            selectDayList.add(new SelectDay(new CalendarInfo(mYear, mMonth, day), lable, lastDayIndex));
        }
    }

    /**
     * 根据点获取时间
     */
    public CalendarInfo getYMDByLocation(int x, int y) {
//        if (lastSelectedDay < 0) {
//            return null;
//        }
        int locIndex = getDayIndex(x, y);
        if (locIndex <= 0) {
            return null;
        }

        int year = mYear;
        int month = mMonth;
        int day = lastSelectedDay;
//        int day = selectDayList.get(selectDayList.size()-1).info.getDay();
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, day);
        //根据当前的索引和上次选中的索引之间的差,计算当前的选中时间
        calendar.add(Calendar.DAY_OF_MONTH, locIndex - lastDayIndex);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        return new CalendarInfo(year, month, day, CalendarMode.MONTH);
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

    public Rect getSelectedRect() {
        RectF rectF = mDayRectList.get(lastDayIndex);
        Rect rect = new Rect();
        rect.top = (int) rectF.top;
        rect.bottom = (int) rectF.bottom;
        rect.right = (int) rectF.right;
        rect.left = (int) rectF.left;
        return rect;
    }

    @Override
    public boolean onTouchEvent(MotionEvent evrtbent) {
        return false;
    }

    public class SelectDay {
        public CalendarInfo info;
        public String lable;
        public int index;

        public SelectDay() {
        }

        public SelectDay(CalendarInfo info, String lable, int index) {
            this.info = info;
            this.lable = lable;
            this.index = index;
        }

        public SelectDay(CalendarInfo info, int index) {
            this.info = info;
            this.index = index;
        }
    }
}