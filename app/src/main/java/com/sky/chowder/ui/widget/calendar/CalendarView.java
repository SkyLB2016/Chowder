package com.sky.chowder.ui.widget.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import com.sky.chowder.R;
import com.sky.chowder.ui.widget.calendar.common.CalendarColor;
import com.sky.chowder.ui.widget.calendar.common.CalendarInfo;
import com.sky.chowder.ui.widget.calendar.common.CalendarMode;
import com.sky.chowder.ui.widget.calendar.impl.OnEveryDayClickListener;
import com.sky.chowder.ui.widget.calendar.impl.OnInitListener;
import com.sky.chowder.ui.widget.calendar.impl.OnLayerClickListener;
import com.sky.chowder.ui.widget.calendar.impl.OnPageChangeListenerWrapper;
import com.sky.chowder.ui.widget.calendar.impl.OnTimeChange;
import com.sky.chowder.ui.widget.calendar.layer.CalendarLayer;
import com.sky.chowder.ui.widget.calendar.layer.ListLayer;
import com.sky.chowder.ui.widget.calendar.layer.MonthLayer;
import com.sky.chowder.ui.widget.calendar.layer.WeekBarLayer;
import com.sky.chowder.ui.widget.calendar.layer.WeekLayer;
import com.sky.chowder.ui.widget.calendar.layer.YearBarLayer;
import com.sky.chowder.ui.widget.calendar.layer.YearLayer;
import com.sky.chowder.ui.widget.calendar.manager.ListLayerManager;
import com.sky.chowder.ui.widget.calendar.manager.MonthLayerManager;
import com.sky.chowder.ui.widget.calendar.manager.WeekLayerManager;
import com.sky.chowder.ui.widget.calendar.manager.YearLayerManager;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;

public class CalendarView extends View {

    public static final int MONTH_OF_DAY_ROW = 6;   //行数
    public static final int MONTH_OF_DAY_COL = 7;   //列数

    public static int FIRST_DAY = Calendar.MONDAY;  //每周的第一天是周几

    private YearLayerManager mYearLayerManager;
    protected MonthLayerManager mMonthLayerManager;
    private WeekLayerManager mWeekLayerManager;
    protected YearBarLayer mYearBarLayer;
    protected WeekBarLayer mWeekBarLayer;
    private ListLayerManager mListLayerManager;
    private boolean mIsMeasured;
    protected CalendarMode mCurMode;

    private static final int TOUCH_SLOP = 16;
    private boolean mIsDragBegin = false;
    private int mDownY, mDownX, mPreDownY;
    private int mScrollY;
    private Scroller mScroller;
    private InnerHandler mHandler;

    private int mDataListUpTop, mDataListDownTop;
    private int mScrollReset;                     // 大于0，向下，小于0，向上
    private boolean mIsModeChanging;
    protected CalendarInfo selInfo;
    protected CalendarInfo today;
    private int preDir = 1;     //用于判断当滑动距离为0时,切换当前模式到星期还是月份

    protected OnInitListener mInitListener;
    protected OnEveryDayClickListener onEveryDayClickListener;
    protected boolean isDetached;
    private ListLayer listLayer;

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
        mInitListener = listener;
    }

    public void setTime(int year, int month, int day) {
        today = new CalendarInfo(year, month, day, CalendarMode.MONTH);
        selInfo = today;
        setCalendarSelInfo(today);
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

    private void init() {
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (!mIsMeasured) {
                    mIsMeasured = true;
                    initLayer();
                }
                return mIsMeasured;
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //Fix-bug 1194 导致 getContext 的空指针
        isDetached = true;
    }

    protected void initLayer() {
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
        mHandler = new InnerHandler(getContext().getMainLooper());
        mScroller = new Scroller(getContext(), new DecelerateInterpolator());
        //年份导航条
        int yearBarHeight = getResources().getDimensionPixelOffset(R.dimen.wh_40);
        Rect yearBarRect = new Rect(0, 0, getWidth(), yearBarHeight);
        CalendarInfo yearBarInfo = new CalendarInfo(yearBarRect, mYear, mMonth, 1, CalendarMode.YEAR);
        mYearBarLayer = new YearBarLayer(yearBarInfo, getResources());
        initYearBarLayerEvent(yearBarInfo, mCurMode);
        //星期导航条
        int weekBarHeight = getResources().getDimensionPixelOffset(R.dimen.wh_40);
        Rect weekBarRect = new Rect(0, yearBarRect.bottom, getWidth(), yearBarRect.bottom + weekBarHeight);
        CalendarInfo weekBarInfo = new CalendarInfo(weekBarRect, mYear, mMonth, 1, CalendarMode.YEAR);
        mWeekBarLayer = new WeekBarLayer(weekBarInfo, getResources());
        //年模式视图
        Rect yearRect = new Rect(0, yearBarRect.bottom, getWidth(), getHeight());
        CalendarInfo yearInfo = new CalendarInfo(yearRect, mYear, 0, 1, CalendarMode.YEAR);
        mYearLayerManager = new YearLayerManager(this, yearRect, new YearLayer(yearInfo, getResources()));
        mYearLayerManager.setOnYearLayerClickListener(new OnLayerClickListener() {
            @Override
            public void onClick(CalendarInfo info) {
                //年份层展开后，点击其中的一个月
                selectCalendarInfo(info, CalendarMode.MONTH);
                initYearBarLayerEvent(info, CalendarMode.MONTH);
            }
        });
        mYearLayerManager.setOnPageChangeListener(new OnPageChangeListenerWrapper() {
            @Override
            public void onPageScrollEnd(CalendarInfo info) {
                //年份滑动翻页
                mYearBarLayer.setYearMonth(info, CalendarMode.YEAR);
                setCalendarSelInfo(info);
                showOrHideTodayBtn(CalendarMode.YEAR);
                invalidate();
            }
        });
        //月模式视图
        int monthItemHeight = getWidth() / MONTH_OF_DAY_COL;    //宽高使用一样的
        Rect monthRect = new Rect(0, weekBarRect.bottom, getWidth(), monthItemHeight * MONTH_OF_DAY_ROW + weekBarRect.bottom);
        CalendarInfo monthInfo = new CalendarInfo(monthRect, mYear, mMonth, mDay, CalendarMode.MONTH);
        mMonthLayerManager = new MonthLayerManager(this, monthRect, new MonthLayer(monthInfo, getResources()));
        mMonthLayerManager.setOnTimeChangeListener(new DefaultOnTimeChangeListener());
        mMonthLayerManager.registerOnPageChangeListener(new OnPageChangeListenerWrapper() {
            @Override
            public void onPageScrollEnd(CalendarInfo info) {
                mYearBarLayer.setYearMonth(info, CalendarMode.MONTH);
                invalidate();
            }
        });
        //周模式视图
        Rect weekRect = new Rect(0, weekBarRect.bottom, getWidth(), weekBarRect.bottom + monthItemHeight * MONTH_OF_DAY_ROW);
        CalendarInfo weekInfo = new CalendarInfo(weekRect, mYear, mMonth, mDay, CalendarMode.WEEK);
        mWeekLayerManager = new WeekLayerManager(this, weekRect, new WeekLayer(weekInfo, getResources()));
        mWeekLayerManager.setOnTimeChangeListener(new DefaultOnTimeChangeListener());
        mWeekLayerManager.registerOnPageChangeListener(new OnPageChangeListenerWrapper() {
            @Override
            public void onPageScrollEnd(CalendarInfo info) {
                mYearBarLayer.setYearMonth(info, CalendarMode.WEEK);
                invalidate();
            }
        });
        //列表视图
        int dataLayerTop = mMonthLayerManager.getCurLayer().getBorderRect().bottom;
        Rect monthModeDataRect = new Rect(0, dataLayerTop, getWidth(), yearRect.bottom);
        Rect weekModeDataRect = new Rect(0, weekRect.bottom, getWidth(), yearRect.bottom);
        CalendarInfo listInfo = new CalendarInfo(monthModeDataRect, weekModeDataRect, mYear, mMonth, mDay, CalendarMode.DAY);
        listLayer = new ListLayer(this, listInfo);
        mListLayerManager = new ListLayerManager(this, weekModeDataRect, listLayer);
        mListLayerManager.registerOnPageChangeListener(new OnPageChangeListenerWrapper() {
            @Override
            public void onPageScrollEnd(CalendarInfo info) {
                //周模式下,对List数据滑动翻页
                if (isWeekMode()) {
                    WeekLayer layer = (WeekLayer) mWeekLayerManager.getCurLayer();
                    int index = layer.dayInWeekIndex(info.getDay());
                    if (index >= 0) {
                        //当前weeklayer不变
                        mWeekLayerManager.setCurLayerTime(info.getYear(), info.getMonth(), info.getDay());
                        invalidate();
                    } else {
                        CalendarInfo curInfo = new CalendarInfo(mWeekLayerManager.getDefRect(), mWeekLayerManager.getDefRect(), info.getYear(), info.getMonth(), info.getDay(), CalendarMode.WEEK);
                        mWeekLayerManager.setCurLayerMode(curInfo);
                    }
                }
            }
        });
        //初始显示状态修改
        mYearBarLayer.setShow(true);
        mYearBarLayer.setTodayShow(false);
        mWeekBarLayer.setShow(true);
        mYearLayerManager.setShow(false);
        mMonthLayerManager.setShow(false);
        mWeekLayerManager.setShow(false);
        mListLayerManager.setShow(false);
        setCalendarMode(CalendarMode.MONTH);
        showOrHideTodayBtn(CalendarMode.MONTH);
        if (mInitListener != null) {
            mInitListener.onInitFinished(this);
        }
        mWeekLayerManager.setShow(true);
    }

    /**
     * 初始化导航条
     * 之所以要多次初始化的原因是，不同的模式，点击行为不一样
     */
    public void initYearBarLayerEvent(CalendarInfo info, final CalendarMode mode2) {
        final CalendarMode mode = mCurMode;
        mYearBarLayer.setYearMonth(info, mode);//先确定模式
        showOrHideTodayBtn(mode);

        //导航条点击左箭头
        mYearBarLayer.setLeftArrowClick(new OnLayerClickListener() {
            @Override
            public void onClick(CalendarInfo info) {
                selectCalendarInfo(info, mode);
            }
        });
        //导航条点击右箭头
        mYearBarLayer.setRightArrowClick(new OnLayerClickListener() {
            @Override
            public void onClick(CalendarInfo info) {
                selectCalendarInfo(info, mode);
            }
        });
        //导航条点击年
        mYearBarLayer.setYearBtnClick(new OnLayerClickListener() {
            @Override
            public void onClick(CalendarInfo info) {
                if (isWeekMode()) {
                    roll(1);
                } else if (isMonthMode()) {
                    mYearLayerManager.setCurLayerMode(new CalendarInfo(mYearLayerManager.getDefRect(), mYearLayerManager.getDefRect(), info.getYear(), 0, 1, CalendarMode.YEAR));
                    setCalendarMode(CalendarMode.YEAR);
                    initYearBarLayerEvent(info, CalendarMode.YEAR);
                    mYearBarLayer.setYearMonth(info, CalendarMode.YEAR);
                }
            }
        });
        //导航条点击今
        mYearBarLayer.setTodayBtnClick(new OnLayerClickListener() {
            @Override
            public void onClick(CalendarInfo info) {
                if (mode == CalendarMode.MONTH) {
                    setCalendarSelInfo(info);
                    Rect rect = null;
                    Rect defRect = getListLayerManager().getDefRect();
                    if (isMonthMode()) {
                        CalendarInfo monthInfo = new CalendarInfo(getMonthLayerManager().getDefRect(), getMonthLayerManager().getDefRect(), info.getYear(), info.getMonth(), info.getDay(), CalendarMode.MONTH);
                        getMonthLayerManager().setCurLayerMode(monthInfo);
                        rect = new Rect();
                        rect.left = defRect.left;
                        rect.right = defRect.right;
                        rect.top = getMonthLayerManager().getCurLayer().getBorderRect().bottom;
                        rect.bottom = defRect.bottom;
                    } else if (isWeekMode()) {
                        CalendarInfo weekInfo = new CalendarInfo(getWeekLayerManager().getDefRect(), getWeekLayerManager().getDefRect(), info.getYear(), info.getMonth(), info.getDay(), CalendarMode.WEEK);
                        getWeekLayerManager().setCurLayerMode(weekInfo);
                        rect = new Rect();
                        rect.left = defRect.left;
                        rect.right = defRect.right;
                        rect.top = getWeekLayerManager().getCurLayer().getBorderRect().bottom;
                        rect.bottom = defRect.bottom;
                    }
                    CalendarInfo listInfo = new CalendarInfo(rect, getListLayerManager().getDefRect(), info.getYear(), info.getMonth(), info.getDay(), CalendarMode.DAY);
                    getListLayerManager().setCurLayerMode(listInfo);
                } else {
                    CalendarInfo curInfo = new CalendarInfo(mYearLayerManager.getDefRect(), mYearLayerManager.getDefRect(), info.getYear(), info.getMonth(), 1, CalendarMode.YEAR);
                    setCalendarSelInfo(curInfo);
                    mYearLayerManager.setCurLayerMode(curInfo);
                    setCalendarMode(mode);
                }
                mYearBarLayer.setYearMonth(info, mode);
                showOrHideTodayBtn(mode);
                invalidate();
            }
        });
    }

    public void selectCalendarInfo(CalendarInfo info, CalendarMode mode) {
        if (mode == CalendarMode.MONTH) {
            mMonthLayerManager.setCurLayerMode(new CalendarInfo(mMonthLayerManager.getDefRect(), mMonthLayerManager.getDefRect(), info.getYear(), info.getMonth(), 1, CalendarMode.MONTH));
            Rect defRect = mListLayerManager.getDefRect();
            Rect rect = new Rect();
            rect.left = defRect.left;
            rect.right = defRect.right;
            rect.top = mMonthLayerManager.getCurLayer().getBorderRect().bottom;
            rect.bottom = defRect.bottom;
            CalendarInfo curInfo = new CalendarInfo(rect, mListLayerManager.getDefRect(), info.getYear(), info.getMonth(), 1, CalendarMode.DAY);
            setCalendarSelInfo(curInfo);
            mListLayerManager.setCurLayerMode(curInfo);
        } else {
            CalendarInfo curInfo = new CalendarInfo(mYearLayerManager.getDefRect(), mYearLayerManager.getDefRect(), info.getYear(), info.getMonth(), 1, CalendarMode.YEAR);
            setCalendarSelInfo(curInfo);
            mYearLayerManager.setCurLayerMode(curInfo);
        }
        mYearBarLayer.setYearMonth(info, mode);
        setCalendarMode(mode);
        showOrHideTodayBtn(mode);
        invalidate();
    }

    public void setCalendarMode(CalendarMode mode) {
        mCurMode = mode;
        switch (mode) {
            case YEAR:
                mWeekLayerManager.setShow(false);
                mMonthLayerManager.setShow(false);
                mYearBarLayer.setShow(true);
                mWeekBarLayer.setShow(false);
                mYearLayerManager.setShow(true);
                mListLayerManager.setShow(false);
                break;
            case MONTH:
                mWeekLayerManager.setShow(false);
                mMonthLayerManager.setShow(true);
                mYearBarLayer.setShow(true);
                mWeekBarLayer.setShow(true);
                mYearLayerManager.setShow(false);
                mListLayerManager.setShow(true);
                int dy = mMonthLayerManager.getDefRect().top - mMonthLayerManager.getCurLayer().getBorderRect().top;
                int dx = mMonthLayerManager.getDefRect().left - mMonthLayerManager.getCurLayer().getBorderRect().left;
                mMonthLayerManager.getCurLayer().scrollBy(dx, dy);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mYearLayerManager != null) {
            mYearLayerManager.onDraw(canvas);
        }
        if (mListLayerManager != null) {
            mListLayerManager.onDraw(canvas);
        }
        if (mMonthLayerManager != null) {
            mMonthLayerManager.onDraw(canvas);
        }
        if (mYearBarLayer != null) {
            mYearBarLayer.onDraw(canvas);
        }
        if (mWeekBarLayer != null) {
            mWeekBarLayer.onDraw(canvas);
        }
        if (mWeekLayerManager != null) {
            mWeekLayerManager.onDraw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsModeChanging) {
            return true;
        }

        boolean disableDrag = !isScroll();//当前有视图在滚动的时候，isScroll返回True
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                mDownX = (int) event.getX();
                mDownY = mPreDownY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mPreDownY = mDownY;
                int distanceX = (int) (event.getX() - mDownX);
                int distanceY = (int) (event.getY() - mDownY);
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                int absDy = Math.abs(distanceY);
                int absDx = Math.abs(distanceX);
                //mIsDragBegin设置滚动标志
                if (!mIsDragBegin && absDy >= TOUCH_SLOP && absDy >= absDx && disableDrag) {
                    if (isWeekMode()) {
                        mListLayerManager.getLayer().setFlingStatus(true);
                    } else if (isMonthMode()) {
                        mListLayerManager.getLayer().setFlingStatus(false);
                    }
                    if (distanceY >= 0 && isDataListCanPullDown()) {//向下
                        mIsDragBegin = false; //下拉,并且 datalist 有数据可以下拉,则拉动datalist
                    } else if (distanceY < 0 && isDataListCanPullUp()) {//向上
                        mIsDragBegin = false; //上拉,并且 datalist 有数据可以上拉,则拉动datalist
                    } else {
                        mIsDragBegin = true;  //否则,滑动改变日历控件
                        distanceY = 0;
                    }
                }
                if (disableDrag && mIsDragBegin) {
                    onScrollY(distanceY);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_POINTER_UP:
                if (disableDrag && mIsDragBegin) {
                    roll(mDownY - mPreDownY);
                }
                mListLayerManager.getLayer().setFlingStatus(true);
                break;
            default:
                break;
        }
        if (mIsDragBegin) {
            return true;
        }
        if (mYearLayerManager.onTouchEvent(event)) {
            return true;
        }
        mYearBarLayer.onTouchEvent(event);
        mWeekBarLayer.onTouchEvent(event);
        boolean isRefreshShow = mListLayerManager.getLayer().isRefreshRectShow();
        if (isRefreshShow) {
            mListLayerManager.onTouchEvent(event);
            mListLayerManager.getCurLayer().onTouchEvent(event);
        } else {
            if (!mWeekLayerManager.onTouchEvent(event)) {
                if (isWeekMode()) {
                    mListLayerManager.setCanScrollLeftOfRight(true);
                    boolean flag = mListLayerManager.onTouchEvent(event);
                    mListLayerManager.setCurLayerCanDrag(true);
                    if (flag && !mListLayerManager.isDragLeftOrRight()) {
                        mListLayerManager.getCurLayer().onTouchEvent(event);
                    }
                }
            }
        }
        mMonthLayerManager.onTouchEvent(event);
        if (isMonthMode()) {
            mListLayerManager.setCanScrollLeftOfRight(false);
            mListLayerManager.setCurLayerCanDrag(false);
            mListLayerManager.getCurLayer().onTouchEvent(event);
        } else if (isWeekMode()) {
            mListLayerManager.setCanScrollLeftOfRight(true);
            mListLayerManager.setCurLayerCanDrag(true);
        }
        return true;
    }

    //判断当前是否有视图在滚动，true:有视图滚动，false:没有视图滚动
    public boolean isScroll() {
        return mMonthLayerManager.isScroll() || mWeekLayerManager.isScroll() || mListLayerManager.isScroll();
    }

    private boolean isDataListCanPullUp() {
        return isWeekMode() && mListLayerManager.isCanPullUp();
    }

    private boolean isDataListCanPullDown() {
        return isWeekMode() && mListLayerManager.isCanPullDown();
    }

    /**
     * 视图滚动到原位, dir表示方向
     */
    private void roll(int dir) {
        if (dir < 0) {// 向上回位
            mScrollReset = mDataListUpTop - mListLayerManager.getLayer().getBorderRect().top;
        } else {//向下回位
            mScrollReset = mDataListDownTop - mListLayerManager.getLayer().getBorderRect().top;
        }
        mIsModeChanging = true;
        mScroller.startScroll(0, 0, 0, mScrollReset);
        mScroller.computeScrollOffset();
        Message msg = Message.obtain();
        mScrollY = 0;
        msg.what = InnerHandler.MESSAGE_SCROLL;
        msg.arg1 = mScroller.getCurrY() - mScrollY;
        mHandler.sendMessage(msg);
        mScrollY = mScroller.getCurrY();
    }

    public void onScrollY(int dy) {
        if (isYearMode()) {
            return;
        }

        if (isWeekMode()) {//当前为周模式
            if (!mMonthLayerManager.isShow() && dy >= 0) {//向下滑动
                mMonthLayerManager.setShow(true);
                mListLayerManager.getLayer().setFlingStatus(false);
                //根据当前日期计算月.然后设置位置
                Rect rect = new Rect();
                Rect monthDefRect = mMonthLayerManager.getDefRect();
                rect.left = monthDefRect.left;
                rect.right = monthDefRect.right;
                rect.bottom = monthDefRect.top + mWeekLayerManager.getDefRect().height();
                rect.top = rect.bottom - monthDefRect.height();
                CalendarInfo info = new CalendarInfo(rect, mMonthLayerManager.getDefRect(), mWeekLayerManager.getYear(), mWeekLayerManager.getMonth(), mWeekLayerManager.getDay(), CalendarMode.MONTH);
                mMonthLayerManager.setCurLayerMode(info);
                //如果在周模式下切换了，月可能变化，setCurLayerMode后month layer会改变，重设位置
                Rect curMonthLayerRect = mMonthLayerManager.getCurLayer().getBorderRect();
                mMonthLayerManager.getCurLayer().scrollBy(0, monthDefRect.height() - curMonthLayerRect.height());
                mDataListUpTop = mWeekLayerManager.getDefRect().bottom;
                mDataListDownTop = monthDefRect.top + curMonthLayerRect.height();
            }
        } else if (isMonthMode()) {
            mWeekLayerManager.setCurLayerMode(new CalendarInfo(mWeekLayerManager.getDefRect(), mWeekLayerManager.getDefRect(), mMonthLayerManager.getYear(), mMonthLayerManager.getMonth(), mMonthLayerManager.getDay(), CalendarMode.WEEK));
            Rect curMonthLayerRect = mMonthLayerManager.getCurLayer().getBorderRect();
            Rect monthDefRect = mMonthLayerManager.getDefRect();
            mDataListUpTop = mWeekLayerManager.getDefRect().bottom;
            mDataListDownTop = monthDefRect.top + curMonthLayerRect.height();
        }

        int listLayerTop = mListLayerManager.getLayer().getBorderRect().top;
        if (listLayerTop + dy <= mDataListUpTop) {
            dy = mDataListUpTop - listLayerTop;
        }
        if (listLayerTop + dy > mDataListDownTop) {
            dy = mDataListDownTop - listLayerTop;
        }

        if (dy != 0) {
            preDir = dy;
        }

        mListLayerManager.getLayer().scrollBy(0, dy);
        Rect borderRect = mListLayerManager.getLayer().getBorderRect();
        Rect dataRect = mListLayerManager.getLayer().getRect();
        borderRect.bottom -= dy;
        dataRect.bottom -= dy;
        mMonthLayerManager.getCurLayer().scrollBy(0, dy);
        //当 当前天的顶部比星期视图的顶部小时,即为划过星期以上,此时显示星期视图
        int diff = mMonthLayerManager.getSelectedDayRect().top - mWeekLayerManager.getCurLayer().getBorderRect().top;
        if (diff == 0) {
            if (preDir > 0) {
                mWeekLayerManager.setShow(false);
                mListLayerManager.getLayer().setFlingStatus(false);
            } else {
                mWeekLayerManager.setShow(true);
                mListLayerManager.getLayer().setFlingStatus(true);
            }
        }
        if (diff < 0) {
            mWeekLayerManager.setShow(true);
            mListLayerManager.getLayer().setFlingStatus(true);
        }
        if (diff > 0) {
            mWeekLayerManager.setShow(false);
            mListLayerManager.getLayer().setFlingStatus(false);
        }
        invalidate();
    }

    protected void showOrHideTodayBtn(CalendarMode mode) {
        if (mode == CalendarMode.MONTH) {
            if (today.getYear() == selInfo.getYear() && today.getMonth() == selInfo.getMonth() && today.getDay() == selInfo.getDay()) {
                mYearBarLayer.setTodayShow(false);
            } else {
                mYearBarLayer.setTodayShow(true);
            }
        } else {
            if (today.getYear() == selInfo.getYear()) {
                mYearBarLayer.setTodayShow(false);
            } else {
                mYearBarLayer.setTodayShow(true);
            }
        }
    }

    public void clear() {
        if (mWeekLayerManager != null) {
            mWeekLayerManager.clear();
        }
        if (mMonthLayerManager != null) {
            mMonthLayerManager.clear();
        }
        if (mYearLayerManager != null) {
            mYearLayerManager.clear();
        }
        if (mListLayerManager != null) {
            mListLayerManager.clear();
        }
    }

    public void setNoData(int resNoDataId, @NonNull String noDataStr) {
        listLayer.setNoData(resNoDataId, noDataStr);
    }

    public CalendarInfo getCalendarSelInfo() {
        return selInfo;
    }

    public void setCalendarSelInfo(CalendarInfo calendarInfo) {
        selInfo = calendarInfo;
    }

    public boolean isMonthMode() {
        return mCurMode == CalendarMode.MONTH;
    }

    public boolean isWeekMode() {
        return mCurMode == CalendarMode.WEEK;
    }

    public boolean isYearMode() {
        return mCurMode == CalendarMode.YEAR;
    }

    public MonthLayerManager getMonthLayerManager() {
        return mMonthLayerManager;
    }

    public WeekLayerManager getWeekLayerManager() {
        return mWeekLayerManager;
    }

    public ListLayerManager getListLayerManager() {
        return mListLayerManager;
    }

    public YearBarLayer getYearBarLayer() {
        return mYearBarLayer;
    }

    private class InnerHandler extends Handler {

        public static final int MESSAGE_SCROLL = 1;

        public InnerHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_SCROLL) {
                onScrollY(msg.arg1);
                if (mScroller.computeScrollOffset()) {
                    Message msg2 = Message.obtain();
                    msg2.what = MESSAGE_SCROLL;
                    msg2.arg1 = mScroller.getCurrY() - mScrollY;
                    mHandler.sendMessage(msg2);
                    mScrollY = mScroller.getCurrY();
                } else {
                    mScrollY = 0;
                    mHandler.removeMessages(MESSAGE_SCROLL);
                    mIsDragBegin = false;
                    mIsModeChanging = false;
                    onScrollEnd(mScrollReset);
                }
            }
        }
    }

    public void onScrollEnd(int dir) {
        if (isYearMode()) {
            return;
        }

        //当滑动距离为0时,保持现在状态
        if (dir == 0) {
            if (preDir > 0) {
                mWeekLayerManager.setShow(false);
                mMonthLayerManager.setShow(true);
                mCurMode = CalendarMode.MONTH;
                resetListLayerRectInMonthMode();
                mListLayerManager.setCurLayerCanDrag(false);
            } else {
                mCurMode = CalendarMode.WEEK;
                mWeekLayerManager.setShow(true);
                mMonthLayerManager.setShow(false);
                mListLayerManager.setCurLayerCanDrag(true);
                resetListLayerRectInWeekMode();
            }
        }
        if (dir > 0) {
            //向下滑动结束,显示模式 month
            mWeekLayerManager.setShow(false);
            mMonthLayerManager.setShow(true);
            mCurMode = CalendarMode.MONTH;
            resetListLayerRectInMonthMode();
            mListLayerManager.setCurLayerCanDrag(false);
        }
        if (dir < 0) {
            //向上滑动结束,显示模式 week
            mCurMode = CalendarMode.WEEK;
            mWeekLayerManager.setShow(true);
            mMonthLayerManager.setShow(false);
            mListLayerManager.setCurLayerCanDrag(true);
            resetListLayerRectInWeekMode();
        }
    }

    private void resetListLayerRectInWeekMode() {
        Set<String> keySet = mListLayerManager.getLayerMap().keySet();
        Rect weekRect = mWeekLayerManager.getCurLayer().getBorderRect();
        Iterator<String> iterator = keySet.iterator();
        CalendarLayer layer;
        while (iterator.hasNext()) {
            layer = mListLayerManager.getLayerMap().get(iterator.next());
            layer.scrollBy(0, weekRect.bottom - layer.getBorderRect().top);
            layer.getBorderRect().bottom = getHeight();
        }
    }

    private void resetListLayerRectInMonthMode() {
        Set<String> keySet = mListLayerManager.getLayerMap().keySet();
        Iterator<String> iterator = keySet.iterator();
        CalendarLayer layer;
        while (iterator.hasNext()) {
            layer = mListLayerManager.getLayerMap().get(iterator.next());
            layer.getBorderRect().bottom = getHeight();
        }
    }

    /**
     * WeekLayer 和 MonthLayer 选择不同的时间回调
     */
    public class DefaultOnTimeChangeListener implements OnTimeChange {

        @Override
        public void onTimeChange(CalendarInfo info) {
            if (onEveryDayClickListener != null) {
                onEveryDayClickListener.onEveryDayClick(info);
            }
            setCalendarSelInfo(info);
            mYearBarLayer.setYearMonth(info, CalendarMode.MONTH);
            Rect rect = null;
            Rect defRect = mListLayerManager.getDefRect();
            if (isWeekMode()) {
                rect = new Rect();
                rect.left = defRect.left;
                rect.right = defRect.right;
                rect.top = mWeekLayerManager.getDefRect().bottom;
                rect.bottom = defRect.bottom;
            } else if (isMonthMode()) {
                rect = new Rect();
                rect.left = defRect.left;
                rect.right = defRect.right;
                rect.top = mMonthLayerManager.getCurLayer().getBorderRect().bottom;
                rect.bottom = defRect.bottom;
            }
            CalendarInfo dayInfo = new CalendarInfo(rect, mListLayerManager.getDefRect(), info.getYear(), info.getMonth(), info.getDay(), CalendarMode.DAY);
            mListLayerManager.setCurLayerMode(dayInfo);
            showOrHideTodayBtn(CalendarMode.MONTH);
            invalidate();
        }
    }
}