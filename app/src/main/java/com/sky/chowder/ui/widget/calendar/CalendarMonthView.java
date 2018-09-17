package com.sky.chowder.ui.widget.calendar;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.sky.chowder.R;
import com.sky.chowder.ui.widget.calendar.common.CalendarColor;
import com.sky.chowder.ui.widget.calendar.common.CalendarInfo;
import com.sky.chowder.ui.widget.calendar.common.CalendarMode;
import com.sky.chowder.ui.widget.calendar.impl.OnLayerClickListener;
import com.sky.chowder.ui.widget.calendar.impl.OnPageChangeListenerWrapper;
import com.sky.chowder.ui.widget.calendar.impl.OnTimeChange;
import com.sky.chowder.ui.widget.calendar.layer.MonthLayer;
import com.sky.chowder.ui.widget.calendar.layer.WeekBarLayer;
import com.sky.chowder.ui.widget.calendar.layer.YearBarLayer;
import com.sky.chowder.ui.widget.calendar.manager.MonthLayerManager;

/**
 * Created by hezhiqiang on 16/10/8.
 * 修改 廖子尧
 */
public class CalendarMonthView extends CalendarView {

    private float mDownX;
    private float mDownY;
    private OnLayerClickListener yearLayerClickListener;

    public CalendarMonthView(Context context) {
        this(context, null);
    }

    public CalendarMonthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarMonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        CalendarColor.setCalendarPrimaryColor(CalendarColor.PROJECT);
    }

    @Override
    public void initLayer() {
        if (isDetached) {
            return;
        }
        if (mInitListener != null) {
            mInitListener.onInitBefore(this);
        }
        int mYear = today.getYear();
        int mMonth = today.getMonth();
        int mDay = today.getDay();
        mCurMode = today.getMode();
        //年份导航条
        int yearBarHeight = getResources().getDimensionPixelOffset(R.dimen.wh_40);
        Rect yearBarRect = new Rect(0, 0, getWidth(), yearBarHeight);
        CalendarInfo yearBarInfo = new CalendarInfo(yearBarRect, mYear, mMonth, 1, null);
        mYearBarLayer = new YearBarLayer(yearBarInfo, getResources());
        initYearBarLayerEvent(yearBarInfo, mCurMode);
        //星期导航条
        int weekBarHeight = getResources().getDimensionPixelOffset(R.dimen.wh_40);
        Rect weekBarRect = new Rect(0, yearBarRect.bottom, getWidth(), yearBarRect.bottom + weekBarHeight);
        CalendarInfo weekBarInfo = new CalendarInfo(weekBarRect, mYear, mMonth, 1, null);
        mWeekBarLayer = new WeekBarLayer(weekBarInfo, getResources());
        //月模式视图
        Rect monthRect = new Rect(0, weekBarRect.bottom, getWidth(), getWidth() / MONTH_OF_DAY_COL * MONTH_OF_DAY_ROW + weekBarRect.bottom);
        CalendarInfo monthInfo = new CalendarInfo(monthRect, mYear, mMonth, mDay, CalendarMode.MONTH);
        mMonthLayerManager = new MonthLayerManager(this, monthRect, new MonthLayer(monthInfo, getResources()));
        mMonthLayerManager.registerOnPageChangeListener(new OnPageChangeListenerWrapper() {
            @Override
            public void onPageScrollEnd(CalendarInfo info) {
                mYearBarLayer.setYearMonth(info, CalendarMode.MONTH);
                invalidate();
            }
        });
        //点选时间改变的监听
        MouthOnTimeChangeListener timeChangeListener = new MouthOnTimeChangeListener();
        mMonthLayerManager.setOnTimeChangeListener(timeChangeListener);
        mMonthLayerManager.setShow(false);
        mYearBarLayer.setShow(false);
        mWeekBarLayer.setShow(false);
        setCalendarMode(CalendarMode.MONTH);
        if (mInitListener != null) {
            mInitListener.onInitFinished(this);
        }
        showOrHideTodayBtn(CalendarMode.MONTH);
    }

    @Override
    public void initYearBarLayerEvent(CalendarInfo info, final CalendarMode mode) {
        mYearBarLayer.setYearMonth(info, mode);//先确定模式
        showOrHideTodayBtn(mode);
        mYearBarLayer.setLeftArrowClick(new OnLayerClickListener() {
            @Override
            public void onClick(CalendarInfo info) {
                selectCalendarInfo(info, mode);
                if (yearLayerClickListener != null) {
                    yearLayerClickListener.onClick(info);
                }
            }
        });
        mYearBarLayer.setRightArrowClick(new OnLayerClickListener() {
            @Override
            public void onClick(CalendarInfo info) {
                selectCalendarInfo(info, mode);
                if (yearLayerClickListener != null) {
                    yearLayerClickListener.onClick(info);
                }
            }
        });
        mYearBarLayer.setTodayBtnClick(new OnLayerClickListener() {
            @Override
            public void onClick(CalendarInfo info) {
                selectToday(info, mode);
                if (yearLayerClickListener != null) {
                    yearLayerClickListener.onClick(info);
                }
            }
        });
    }

    private void selectToday(CalendarInfo info, CalendarMode mode) {
        if (mode == CalendarMode.MONTH) {
            setCalendarSelInfo(info);
            getYearBarLayer().setTodayShow(false);
            getYearBarLayer().setYearMonth(info, CalendarMode.MONTH);
            if (isMonthMode()) {
                CalendarInfo monthInfo = new CalendarInfo(getMonthLayerManager().getDefRect(), getMonthLayerManager().getDefRect(), info.getYear(), info.getMonth(), info.getDay(), CalendarMode.MONTH);
                getMonthLayerManager().setCurLayerMode(monthInfo);
            } else if (isWeekMode()) {
                CalendarInfo weekInfo = new CalendarInfo(getWeekLayerManager().getDefRect(), getWeekLayerManager().getDefRect(), info.getYear(), info.getMonth(), info.getDay(), CalendarMode.WEEK);
                getWeekLayerManager().setCurLayerMode(weekInfo);
            }
        }
        mYearBarLayer.setYearMonth(info, mode);
        showOrHideTodayBtn(mode);
        invalidate();
    }

    @Override
    public void selectCalendarInfo(CalendarInfo info, CalendarMode mode) {
        if (mode == CalendarMode.MONTH) {
            mMonthLayerManager.setCurLayerMode(new CalendarInfo(mMonthLayerManager.getDefRect(), mMonthLayerManager.getDefRect(), info.getYear(), info.getMonth(), 1, CalendarMode.MONTH));
            setCalendarSelInfo(info);
        }
        mYearBarLayer.setYearMonth(info, mode);
        setCalendarMode(mode);
        showOrHideTodayBtn(mode);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        // 在wrap_content的情况下默认长度为200dp
        if (mMonthLayerManager != null && mMonthLayerManager.getCurLayer() != null && mMonthLayerManager.getCurLayer().getBorderRect() != null) {
            int minSize = mMonthLayerManager.getCurLayer().getBorderRect().bottom;
            // wrap_content的specMode是AT_MOST模式，这种情况下宽/高等同于specSize
            // 查表得这种情况下specSize等同于parentSize，也就是父容器当前剩余的大小
            // 在wrap_content的情况下如果特殊处理，效果等同martch_parent
            if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
                setMeasuredDimension(minSize, minSize);
            } else if (widthSpecMode == MeasureSpec.AT_MOST) {
                setMeasuredDimension(minSize, heightSpecSize);
            } else if (heightSpecMode == MeasureSpec.AT_MOST) {
                setMeasuredDimension(widthSpecSize, minSize);
            }
        }
    }

    @Override
    public void setCalendarMode(CalendarMode mode) {
        mCurMode = mode;
        if (mode == CalendarMode.MONTH) {
            mMonthLayerManager.setShow(true);
            mYearBarLayer.setShow(true);
            mWeekBarLayer.setShow(true);
            int dy = mMonthLayerManager.getDefRect().top - mMonthLayerManager.getCurLayer().getBorderRect().top;
            int dx = mMonthLayerManager.getDefRect().left - mMonthLayerManager.getCurLayer().getBorderRect().left;
            mMonthLayerManager.getCurLayer().scrollBy(dx, dy);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) { //解决滑动冲突
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                //ACTION_DOWN的时候，赶紧把事件hold住
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (getParent() != null) {
                    if (Math.abs(event.getX() - mDownX) > Math.abs(event.getY() - mDownY)) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    } else {
                        //发现不是自己处理，还给父类
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                break;
            default:
                break;
        }
        mMonthLayerManager.onTouchEvent(event);
        mYearBarLayer.onTouchEvent(event);
        return true;
    }

    @Override
    public void clear() {
        mMonthLayerManager.clear();
    }

    /**
     * WeekLayer 和 MonthLayer
     */
    class MouthOnTimeChangeListener implements OnTimeChange {
        @Override
        public void onTimeChange(CalendarInfo info) {
            setCalendarSelInfo(info);
            mYearBarLayer.setYearMonth(info, CalendarMode.MONTH);
            showOrHideTodayBtn(mCurMode);
            requestLayout();
            invalidate();
            //回传选择的日期
            if (onTimeChangeListener != null) {
                onTimeChangeListener.onTimeChange(info.getYear(), info.getMonth(), info.getDay());
            }
        }
    }

    /**
     * 负责将选择的日期回调到使用的页面
     */
    public interface OnTimeChangeListener {
        void onTimeChange(int year, int month, int day);
    }

    private OnTimeChangeListener onTimeChangeListener;

    public void setOnTimeChangeListener(OnTimeChangeListener listener) {
        this.onTimeChangeListener = listener;
    }

    public void setOnYearLayerClickListener(OnLayerClickListener listener) {
        this.yearLayerClickListener = listener;
    }
}
