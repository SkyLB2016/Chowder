//package com.sky.chowder.ui.widget.calendar;
//
//import android.content.Context;
//import android.graphics.Rect;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//
//import com.sky.chowder.R;
//import com.sky.chowder.ui.widget.calendar.common.CalendarColor;
//import com.sky.chowder.ui.widget.calendar.common.CalendarInfo;
//import com.sky.chowder.ui.widget.calendar.common.CalendarMode;
//import com.sky.chowder.ui.widget.calendar.impl.OnLayerClickListener;
//import com.sky.chowder.ui.widget.calendar.impl.OnPageChangeListenerWrapper;
//import com.sky.chowder.ui.widget.calendar.impl.OnTimeChange;
//import com.sky.chowder.ui.widget.calendar.layer.MonthLayer;
//import com.sky.chowder.ui.widget.calendar.layer.WeekBarLayer;
//import com.sky.chowder.ui.widget.calendar.layer.YearBarLayer;
//import com.sky.chowder.ui.widget.calendar.manager.MonthLayerManager;
//
///**
// * Created by hezhiqiang on 16/10/8.
// * 修改 廖子尧
// */
//public class CalendarMonthView extends CalendarView {
//
//    private float mDownX;
//    private float mDownY;
//    private OnLayerClickListener yearLayerClickListener;
//
//    public CalendarMonthView(Context context) {
//        this(context, null);
//    }
//
//    public CalendarMonthView(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public CalendarMonthView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        CalendarColor.setCalendarPrimaryColor(CalendarColor.PROJECT);
//    }
//
//    @Override
//    public void initLayer() {
//        if (isDetached) {
//            return;
//        }
//        if (initListener != null) {
//            initListener.onInitBefore(this);
//        }
//        int mYear = todayInfo.getYear();
//        int mMonth = todayInfo.getMonth();
//        int mDay = todayInfo.getDay();
//        curMode = todayInfo.getMode();
//        //年份导航条
//        int yearBarHeight = getResources().getDimensionPixelOffset(R.dimen.wh_40);
//        Rect yearBarRect = new Rect(0, 0, getWidth(), yearBarHeight);
//        CalendarInfo yearBarInfo = new CalendarInfo(yearBarRect, mYear, mMonth, 1, null);
//        yearBarLayer = new YearBarLayer(yearBarInfo, getResources());
//        initYearBarLayerEvent(yearBarInfo, curMode);
//        //星期导航条
//        int weekBarHeight = getResources().getDimensionPixelOffset(R.dimen.wh_40);
//        Rect weekBarRect = new Rect(0, yearBarRect.bottom, getWidth(), yearBarRect.bottom + weekBarHeight);
//        CalendarInfo weekBarInfo = new CalendarInfo(weekBarRect, mYear, mMonth, 1, null);
//        weekBarLayer = new WeekBarLayer(weekBarInfo, getResources());
//        //月模式视图
//        Rect monthRect = new Rect(0, weekBarRect.bottom, getWidth(), getWidth() / MONTH_OF_DAY_COL * MONTH_OF_DAY_ROW + weekBarRect.bottom);
//        CalendarInfo monthInfo = new CalendarInfo(monthRect, mYear, mMonth, mDay, CalendarMode.MONTH);
//        monthLayerManager = new MonthLayerManager(this, monthRect, new MonthLayer(monthInfo, getResources()));
//        monthLayerManager.registerOnPageChangeListener(new OnPageChangeListenerWrapper() {
//            @Override
//            public void onPageScrollEnd(CalendarInfo info) {
//                yearBarLayer.setYearMonth(info, CalendarMode.MONTH);
//                invalidate();
//            }
//        });
//        //点选时间改变的监听
//        MouthOnTimeChangeListener timeChangeListener = new MouthOnTimeChangeListener();
//        monthLayerManager.setOnTimeChangeListener(timeChangeListener);
//        monthLayerManager.setShow(false);
//        yearBarLayer.setShow(false);
//        weekBarLayer.setShow(false);
//        setCalendarMode(CalendarMode.MONTH);
//        if (initListener != null) {
//            initListener.onInitFinished(this);
//        }
//        showOrHideTodayBtn(CalendarMode.MONTH);
//    }
//
//    @Override
//    public void initYearBarLayerEvent(CalendarInfo info, final CalendarMode mode) {
//        yearBarLayer.setYearMonth(info, mode);//先确定模式
//        showOrHideTodayBtn(mode);
//        yearBarLayer.setLeftArrowClick(new OnLayerClickListener() {
//            @Override
//            public void onClick(CalendarInfo info) {
//                selectCalendarInfo(info, mode);
//                if (yearLayerClickListener != null) {
//                    yearLayerClickListener.onClick(info);
//                }
//            }
//        });
//        yearBarLayer.setRightArrowClick(new OnLayerClickListener() {
//            @Override
//            public void onClick(CalendarInfo info) {
//                selectCalendarInfo(info, mode);
//                if (yearLayerClickListener != null) {
//                    yearLayerClickListener.onClick(info);
//                }
//            }
//        });
//        yearBarLayer.setTodayBtnClick(new OnLayerClickListener() {
//            @Override
//            public void onClick(CalendarInfo info) {
//                selectToday(info, mode);
//                if (yearLayerClickListener != null) {
//                    yearLayerClickListener.onClick(info);
//                }
//            }
//        });
//    }
//
//    private void selectToday(CalendarInfo info, CalendarMode mode) {
//        if (mode == CalendarMode.MONTH) {
//            setCalendarSelInfo(info);
//            getYearBarLayer().setTodayShow(false);
//            getYearBarLayer().setYearMonth(info, CalendarMode.MONTH);
//            if (isMonthMode()) {
//                CalendarInfo monthInfo = new CalendarInfo(getMonthLayerManager().getDefRect(), getMonthLayerManager().getDefRect(), info.getYear(), info.getMonth(), info.getDay(), CalendarMode.MONTH);
//                getMonthLayerManager().setCurLayerMode(monthInfo);
//            } else if (isWeekMode()) {
//                CalendarInfo weekInfo = new CalendarInfo(getWeekLayerManager().getDefRect(), getWeekLayerManager().getDefRect(), info.getYear(), info.getMonth(), info.getDay(), CalendarMode.WEEK);
//                getWeekLayerManager().setCurLayerMode(weekInfo);
//            }
//        }
//        yearBarLayer.setYearMonth(info, mode);
//        showOrHideTodayBtn(mode);
//        invalidate();
//    }
//
//    @Override
//    public void selectCalendarInfo(CalendarInfo info, CalendarMode mode) {
//        if (mode == CalendarMode.MONTH) {
//            monthLayerManager.setCurLayerMode(new CalendarInfo(monthLayerManager.getDefRect(), monthLayerManager.getDefRect(), info.getYear(), info.getMonth(), 1, CalendarMode.MONTH));
//            setCalendarSelInfo(info);
//        }
//        yearBarLayer.setYearMonth(info, mode);
//        setCalendarMode(mode);
//        showOrHideTodayBtn(mode);
//        invalidate();
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
//        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
//        // 在wrap_content的情况下默认长度为200dp
//        if (monthLayerManager != null && monthLayerManager.getCurLayer() != null && monthLayerManager.getCurLayer().getBorderRect() != null) {
//            int minSize = monthLayerManager.getCurLayer().getBorderRect().bottom;
//            // wrap_content的specMode是AT_MOST模式，这种情况下宽/高等同于specSize
//            // 查表得这种情况下specSize等同于parentSize，也就是父容器当前剩余的大小
//            // 在wrap_content的情况下如果特殊处理，效果等同martch_parent
//            if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
//                setMeasuredDimension(minSize, minSize);
//            } else if (widthSpecMode == MeasureSpec.AT_MOST) {
//                setMeasuredDimension(minSize, heightSpecSize);
//            } else if (heightSpecMode == MeasureSpec.AT_MOST) {
//                setMeasuredDimension(widthSpecSize, minSize);
//            }
//        }
//    }
//
//    @Override
//    public void setCalendarMode(CalendarMode mode) {
//        curMode = mode;
//        if (mode == CalendarMode.MONTH) {
//            monthLayerManager.setShow(true);
//            yearBarLayer.setShow(true);
//            weekBarLayer.setShow(true);
//            int dy = monthLayerManager.getDefRect().top - monthLayerManager.getCurLayer().getBorderRect().top;
//            int dx = monthLayerManager.getDefRect().left - monthLayerManager.getCurLayer().getBorderRect().left;
//            monthLayerManager.getCurLayer().scrollBy(dx, dy);
//        }
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) { //解决滑动冲突
//            case MotionEvent.ACTION_DOWN:
//                mDownX = event.getX();
//                mDownY = event.getY();
//                //ACTION_DOWN的时候，赶紧把事件hold住
//                if (getParent() != null) {
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                }
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (getParent() != null) {
//                    if (Math.abs(event.getX() - mDownX) > Math.abs(event.getY() - mDownY)) {
//                        getParent().requestDisallowInterceptTouchEvent(true);
//                    } else {
//                        //发现不是自己处理，还给父类
//                        getParent().requestDisallowInterceptTouchEvent(false);
//                    }
//                }
//                break;
//            default:
//                break;
//        }
//        monthLayerManager.onTouchEvent(event);
//        yearBarLayer.onTouchEvent(event);
//        return true;
//    }
//
//    @Override
//    public void clear() {
//        monthLayerManager.clear();
//    }
//
//    /**
//     * WeekLayer 和 MonthLayer
//     */
//    class MouthOnTimeChangeListener implements OnTimeChange {
//        @Override
//        public void onTimeChange(CalendarInfo info) {
//            setCalendarSelInfo(info);
//            yearBarLayer.setYearMonth(info, CalendarMode.MONTH);
//            showOrHideTodayBtn(curMode);
//            requestLayout();
//            invalidate();
//            //回传选择的日期
//            if (onTimeChangeListener != null) {
//                onTimeChangeListener.onTimeChange(info.getYear(), info.getMonth(), info.getDay());
//            }
//        }
//    }
//
//    /**
//     * 负责将选择的日期回调到使用的页面
//     */
//    public interface OnTimeChangeListener {
//        void onTimeChange(int year, int month, int day);
//    }
//
//    private OnTimeChangeListener onTimeChangeListener;
//
//    public void setOnTimeChangeListener(OnTimeChangeListener listener) {
//        this.onTimeChangeListener = listener;
//    }
//
//    public void setOnYearLayerClickListener(OnLayerClickListener listener) {
//        this.yearLayerClickListener = listener;
//    }
//}
