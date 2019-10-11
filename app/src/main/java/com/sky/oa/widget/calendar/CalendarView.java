package com.sky.oa.widget.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import com.sky.oa.R;
import com.sky.oa.widget.calendar.common.CalendarColor;
import com.sky.oa.widget.calendar.common.CalendarInfo;
import com.sky.oa.widget.calendar.common.CalendarMode;
import com.sky.oa.widget.calendar.impl.OnEveryDayClickListener;
import com.sky.oa.widget.calendar.impl.OnInitListener;
import com.sky.oa.widget.calendar.impl.OnLayerClickListener;
import com.sky.oa.widget.calendar.impl.OnPageChangeListenerWrapper;
import com.sky.oa.widget.calendar.layer.MonthLayer;
import com.sky.oa.widget.calendar.layer.WeekBarLayer;
import com.sky.oa.widget.calendar.layer.WeekLayer;
import com.sky.oa.widget.calendar.layer.YearBarLayer;
import com.sky.oa.widget.calendar.layer.YearLayer;
import com.sky.oa.widget.calendar.manager.MonthLayerManager;
import com.sky.oa.widget.calendar.manager.WeekLayerManager;
import com.sky.oa.widget.calendar.manager.YearLayerManager;
import com.sky.sdk.utils.LogUtils;

import java.util.Calendar;

public class CalendarView extends View {

    public static final int MONTH_OF_DAY_ROW = 6;   //行数
    public static final int MONTH_OF_DAY_COL = 7;   //列数

    public static int FIRST_DAY = Calendar.MONDAY;  //每周的第一天是周几

    protected YearBarLayer yearBarLayer;//年bar

    protected WeekBarLayer weekBarLayer;//星期头
    protected MonthLayerManager monthLayerManager;//月历
    private WeekLayerManager weekLayerManager;//周历
    private YearLayerManager yearLayerManager;//年历

    protected CalendarInfo todayInfo;
    protected CalendarMode curMode;//当前的显示模式
    protected CalendarInfo selInfo;//选择的日期

    protected OnInitListener initListener;
    protected OnEveryDayClickListener onEveryDayClickListener;

    /**
     * 该接口主要用于埋点统计
     */
    public void setOnEveryDayClickListener(OnEveryDayClickListener listener) {
        this.onEveryDayClickListener = listener;
    }

    /**
     * 初始化接口
     */
    public void setOnInitListener(OnInitListener listener) {
        initListener = listener;
    }

    /**
     * 设置时间模式
     *
     * @param year
     * @param month
     * @param day
     * @param mode  年月周日
     */
    public void setTime(int year, int month, int day, CalendarMode mode) {
        todayInfo = new CalendarInfo(year, month, day, mode);
        setCalendarSelInfo(todayInfo);
    }

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        CalendarColor.setCalendarPrimaryColor(CalendarColor.PROJECT);
    }

    int num = 1;

    private void init() {
        //此监听里的方法会在视图开始绘制之前执行
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                LogUtils.i("执行的第" + num++ + "次");
                if (getViewTreeObserver().isAlive()) {
                    getViewTreeObserver().removeOnPreDrawListener(this);
                    initLayer();
                }
                return true;
            }
        });
    }

    protected void initLayer() {
        if (initListener != null) {
            initListener.onInitBefore(this);
        }
        //今天的日期
        int year = todayInfo.getYear();
        int month = todayInfo.getMonth();
        int today = todayInfo.getDay();
        curMode = todayInfo.getMode();

        //导航条高度，其他几种模式，基于此为顶部
        int yearBarHeight = getResources().getDimensionPixelOffset(R.dimen.wh_40);
        //行高使用一样的
        int rowHeight = getWidth() / MONTH_OF_DAY_COL;

        //年份导航条
        initYearBar(year, month, yearBarHeight);

        yearBarLayer.setShow(true);
        yearBarLayer.setTodayShow(false);


//        initYear(year, month, yearBarHeight, rowHeight);
//        initMonth(year, month, today, yearBarHeight, rowHeight);
//        initWeek(year, month, today, yearBarHeight, rowHeight);
//
//        weekBarLayer.setShow(false);
//        monthLayerManager.setShow(false);
//        weekLayerManager.setShow(false);
//        yearLayerManager.setShow(false);
        //视图的显示与隐藏
        if (curMode == CalendarMode.MONTH) {
            initMonth(year, month, today, yearBarHeight, rowHeight);
            weekBarLayer.setShow(true);
            monthLayerManager.setShow(true);
        } else if (curMode == CalendarMode.WEEK) {
            initWeek(year, month, today, yearBarHeight, rowHeight);
            weekLayerManager.setShow(true);
        } else if (curMode == CalendarMode.YEAR) {
            initYear(year, month, yearBarHeight, rowHeight);
            yearLayerManager.setShow(true);
        }
        if (initListener != null) {
            initListener.onInitFinished(this);
        }
    }

    private void initYearBar(int mYear, int mMonth, int yearBarHeight) {
        CalendarInfo yearBarInfo = new CalendarInfo(new Rect(0, 0, getWidth(), yearBarHeight), mYear, mMonth, 1, CalendarMode.YEAR);
        yearBarLayer = new YearBarLayer(yearBarInfo, getResources());
        initYearBarLayerEvent(yearBarInfo, curMode);
    }

    /**
     * 年模式视图
     *
     * @param top       y轴开始位置
     * @param rowHeight 每行高度
     */
    private void initYear(int year, int month, int top, int rowHeight) {
        Rect yearRect = new Rect(0, top, getWidth(), top + rowHeight * 2);
        CalendarInfo yearInfo = new CalendarInfo(yearRect, year, 0, 1, CalendarMode.YEAR);
        yearLayerManager = new YearLayerManager(this, yearRect, new YearLayer(yearInfo, getResources()));
        yearLayerManager.getLayer().setThisMonth(month);
        yearLayerManager.setOnPageChangeListener(new OnPageChangeListenerWrapper() {
            @Override
            public void onPageScrollEnd(CalendarInfo info) {
                //年份滑动翻页
                yearBarLayer.setYearMonth(info, CalendarMode.YEAR);
                setCalendarSelInfo(info);
                invalidate();
            }
        });
    }

    /**
     * 月模式视图，与星期导航条是绑定状态
     *
     * @param top       y轴开始位置
     * @param rowHeight 每行高度
     * @return
     */
    private void initMonth(int year, int month, int day, int top, int rowHeight) {
        //星期导航条
        int weekBarHeight = getResources().getDimensionPixelOffset(R.dimen.wh_40);
        Rect weekBarRect = new Rect(0, top, getWidth(), top + weekBarHeight);
        CalendarInfo weekBarInfo = new CalendarInfo(weekBarRect, year, month, 1, CalendarMode.YEAR);
        weekBarLayer = new WeekBarLayer(weekBarInfo, getResources());

        //月视图
        Rect monthRect = new Rect(0, weekBarRect.bottom, getWidth(), rowHeight * MONTH_OF_DAY_ROW + weekBarRect.bottom);
        CalendarInfo monthInfo = new CalendarInfo(monthRect, year, month, day, CalendarMode.MONTH);
        monthLayerManager = new MonthLayerManager(this, monthRect, new MonthLayer(monthInfo, getResources()));
        monthLayerManager.getLayer().setToday(day);
        monthLayerManager.registerOnPageChangeListener(new OnPageChangeListenerWrapper() {
            @Override
            public void onPageScrollEnd(CalendarInfo info) {
                yearBarLayer.setYearMonth(info, CalendarMode.MONTH);
                invalidate();
            }
        });
    }

    /**
     * 周模式视图
     *
     * @param top       y轴开始位置
     * @param rowHeight 每行高度
     */
    private void initWeek(int year, int month, int day, int top, int rowHeight) {
        Rect weekRect = new Rect(0, top, getWidth(), top + rowHeight * MONTH_OF_DAY_ROW);
        CalendarInfo weekInfo = new CalendarInfo(weekRect, year, month, day, CalendarMode.WEEK);
        weekLayerManager = new WeekLayerManager(this, weekRect, new WeekLayer(weekInfo, getResources()));
        Calendar cal = Calendar.getInstance();
        int monthStart = cal.get(Calendar.DAY_OF_WEEK);
        int weekOfMonth = cal.get(Calendar.WEEK_OF_MONTH);
        //系统内以周日为一周的开始，值是1，APP内需要以周一开始，所以第一天是周日的话，weekOfMonth需要减1
        if (monthStart == 1) {
            weekOfMonth--;
        }
        //项目内以0为开始
        weekLayerManager.getLayer().setThisWeek(--weekOfMonth);
        weekLayerManager.registerOnPageChangeListener(new OnPageChangeListenerWrapper() {
            @Override
            public void onPageScrollEnd(CalendarInfo info) {
                yearBarLayer.setYearMonth(info, CalendarMode.WEEK);
                invalidate();
            }
        });
    }

    /**
     * 初始化导航条
     * 之所以要多次初始化的原因是，不同的模式，点击行为不一样
     */
    public void initYearBarLayerEvent(CalendarInfo info, final CalendarMode mode2) {
        final CalendarMode mode = curMode;
        yearBarLayer.setYearMonth(info, mode);//先确定模式
        //导航条点击左箭头
        yearBarLayer.setLeftArrowClick(new OnLayerClickListener() {
            @Override
            public void onClick(CalendarInfo info) {
                selectCalendarInfo(info, mode);
            }
        });
        //导航条点击右箭头
        yearBarLayer.setRightArrowClick(new OnLayerClickListener() {
            @Override
            public void onClick(CalendarInfo info) {
                selectCalendarInfo(info, mode);
            }
        });
    }

    public void selectCalendarInfo(CalendarInfo info, CalendarMode mode) {
        if (curMode == CalendarMode.MONTH) {
            monthLayerManager.setCurLayerMode(new CalendarInfo(monthLayerManager.getDefRect(), monthLayerManager.getDefRect(), info.getYear(), info.getMonth(), 1, CalendarMode.MONTH));
        } else if (curMode == CalendarMode.WEEK) {
            weekLayerManager.setCurLayerMode(new CalendarInfo(weekLayerManager.getDefRect(), weekLayerManager.getDefRect(), info.getYear(), info.getMonth(), 1, CalendarMode.WEEK));
        } else {
            CalendarInfo curInfo = new CalendarInfo(yearLayerManager.getDefRect(), yearLayerManager.getDefRect(), info.getYear(), info.getMonth(), 1, CalendarMode.YEAR);
            setCalendarSelInfo(curInfo);
            yearLayerManager.setCurLayerMode(curInfo);
        }
        yearBarLayer.setYearMonth(info, curMode);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (yearBarLayer != null) {
            yearBarLayer.onDraw(canvas);
        }
        if (yearLayerManager != null) {
            yearLayerManager.onDraw(canvas);
        }
        if (weekBarLayer != null) {
            weekBarLayer.onDraw(canvas);
        }
        if (monthLayerManager != null) {
            monthLayerManager.onDraw(canvas);
        }
        if (weekLayerManager != null) {
            weekLayerManager.onDraw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        yearBarLayer.onTouchEvent(event);
        if (yearLayerManager != null) {
            yearLayerManager.onTouchEvent(event);
        }
        if (monthLayerManager != null) {
            weekBarLayer.onTouchEvent(event);
            monthLayerManager.onTouchEvent(event);
        }
        if (weekLayerManager != null) {
            weekLayerManager.onTouchEvent(event);
        }
        return true;
    }

    public void clear() {
        if (weekLayerManager != null) {
            weekLayerManager.clear();
        }
        if (monthLayerManager != null) {
            monthLayerManager.clear();
        }
        if (yearLayerManager != null) {
            yearLayerManager.clear();
        }
    }

    public CalendarInfo getCalendarSelInfo() {
        return selInfo;
    }

    public void setCalendarSelInfo(CalendarInfo calendarInfo) {
        selInfo = calendarInfo;
    }


    public MonthLayerManager getMonthLayerManager() {
        return monthLayerManager;
    }

    public WeekLayerManager getWeekLayerManager() {
        return weekLayerManager;
    }

    public YearLayerManager getYearLayerManager() {
        return yearLayerManager;
    }


    public CalendarInfo getTodayInfo() {
        return todayInfo;
    }
}
