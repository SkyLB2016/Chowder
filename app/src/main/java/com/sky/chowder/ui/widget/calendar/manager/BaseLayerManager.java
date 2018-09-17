package com.sky.chowder.ui.widget.calendar.manager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import com.sky.chowder.ui.widget.calendar.CalendarView;
import com.sky.chowder.ui.widget.calendar.common.CalendarInfo;
import com.sky.chowder.ui.widget.calendar.impl.OnLayerCreateListener;
import com.sky.chowder.ui.widget.calendar.impl.OnPageChangeListener;
import com.sky.chowder.ui.widget.calendar.layer.CalendarLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class BaseLayerManager {

    private static final int ACTION_POINTER_DOWN_ID = 0;  //当前按下的手指
    public static final int DIR_HOR = 1;
    public static final int DIR_VER = 2;
    private static final int ROLL_BACK_DUR = 250;
    private static final int TOUCH_SLOP = 64;

    private CalendarView mView;
    private CalendarInfo mCurSelectedModeInfo;
    private Scroller mScroller;
    private InnerHandler mHandler;
    private VelocityTracker mVelocityTracker;
    private int mMinimumFlingVelocity;
    private int mMaximumFlingVelocity;
    private Rect mDefRect;                          // 边界
    private CalendarLayer mCurLayer;
    private CalendarLayer mPreLayer;
    private CalendarLayer mNextLayer;
    private int mHalfX;
    private int mHalfY;                             // 判断焦点
    private int mPreX;
    private int mPreY;
    private int mScrollX;
    private int mScrollY;
    private int mCurDir = DIR_HOR;
    private boolean mIsShow = true;
    private boolean mIsScroll = false;
    private boolean mIsForceScroll = false;
    private boolean mIsDragLeftOrRight = false;
    private boolean mCanScrollLeftOfRight = true;
    private boolean mAlwaysInTapRegion = true;
    private boolean mIsFirstTouchInArea;
    private HashMap<String, CalendarLayer> mLayerMap = new HashMap<>();
    private List<OnPageChangeListener> mPageListenerList = new ArrayList<>();
    private List<OnLayerCreateListener> mLayerCreateListenerList = new ArrayList<>();

    public abstract CalendarLayer createLayer(CalendarInfo info);

    public abstract CalendarInfo getPreModeInfo(CalendarInfo curInfo);

    public abstract CalendarInfo getNextModeInfo(CalendarInfo curInfo);

    public BaseLayerManager(CalendarView view, Rect defRect, CalendarLayer layer) {
        mView = view;
        mCurLayer = layer;
        mDefRect = new Rect(defRect);
        mHalfX = mDefRect.width() / 2;
        mHalfY = mDefRect.height() / 2;
        mCurSelectedModeInfo = layer.getModeInfo();
        mLayerMap.put(mCurSelectedModeInfo.getString(), mCurLayer);

        Context context = mView.getContext();
        mScroller = new Scroller(context, new DecelerateInterpolator());
        mHandler = new InnerHandler(mView.getContext().getMainLooper());
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity();
        mVelocityTracker = VelocityTracker.obtain();
    }

    public void onDraw(Canvas canvas) {
        if (!isShow()) {
            return;
        }
        mCurLayer.onDraw(canvas);
        if (mPreLayer != null) {
            mPreLayer.onDraw(canvas);
        }
        if (mNextLayer != null) {
            mNextLayer.onDraw(canvas);
        }
    }

    public void onClick(int x, int y) {
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!isShow()) {
            return false;
        }

        if (!mDefRect.contains((int) event.getX(), (int) event.getY())) {
            mAlwaysInTapRegion = true;
            if (mIsFirstTouchInArea) {
                rollBackToCurLayer();
                mIsFirstTouchInArea = false;
            }
            return false;
        }

        if (mIsForceScroll) {
            rollBackToCurLayer();
            return false;
        }

        mVelocityTracker.addMovement(event);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                mScroller.forceFinished(true);
                if (mCurLayer.getBorderRect().left != mDefRect.left) {
                    mIsForceScroll = true;
                }
                mPreX = (int) event.getX(ACTION_POINTER_DOWN_ID);
                mPreY = (int) event.getY(ACTION_POINTER_DOWN_ID);
                mIsFirstTouchInArea = true;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) (event.getX(ACTION_POINTER_DOWN_ID) - mPreX);
                int dy = (int) (event.getY(ACTION_POINTER_DOWN_ID) - mPreY);
                mPreX = (int) event.getX(ACTION_POINTER_DOWN_ID);
                mPreY = (int) event.getY(ACTION_POINTER_DOWN_ID);
                int distance = dx * dx + dy * dy;
                if (mAlwaysInTapRegion && distance > TOUCH_SLOP) {
                    mAlwaysInTapRegion = false;
                    if (Math.abs(dx) > Math.abs(dy)) {
                        mIsDragLeftOrRight = true;
                    } else if (Math.abs(dx) < Math.abs(dy)) {
                        mIsDragLeftOrRight = false;
                    }
                    dx = 0;
                    dy = 0;
                }
                if (!mAlwaysInTapRegion && mIsDragLeftOrRight && mCanScrollLeftOfRight) {
                    computeItem(dx, dy);
                }
                break;
            case MotionEvent.ACTION_UP:
                mIsFirstTouchInArea = false;
                if (mAlwaysInTapRegion) {
                    onClick(mPreX, mPreY);
                    mView.invalidate();
                    mAlwaysInTapRegion = true;
                    break;
                }
                mAlwaysInTapRegion = true;
                if (mCanScrollLeftOfRight && mIsDragLeftOrRight && !isFling()) {
                    rollBackToCurLayer();
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                int pointerId = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >>> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                int eventCount = event.getPointerCount();
                if (eventCount == 2) {
                    mPreY = (int) event.getY(1 - ACTION_POINTER_DOWN_ID);
                } else if (eventCount > 2) {
                    int id1 = 0;
                    if (pointerId == 0) {
                        id1 = 1;
                    }
                    mPreY = (int) event.getY(id1);
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void computeItem(int dx, int dy) {
        if (dx == 0 && dy == 0) {
            return;
        }
        if (mCurDir == DIR_HOR && dx != 0) {
            mCurLayer.scrollBy(dx, 0);
            mIsScroll = true;
            computeItemLocationX();
            mView.invalidate();
        } else if (mCurDir == DIR_VER && dy != 0) {
            mCurLayer.scrollBy(0, dy);
            mIsScroll = true;
            computeItemLocationY();
            mView.invalidate();
        }
        for (int i = 0; i < mPageListenerList.size(); i++) {
            mPageListenerList.get(i).onPageScrolled(mCurLayer.getModeInfo());
        }
    }

    private void computeItemLocationX() {
        CalendarLayer layer = mLayerMap.get(mCurSelectedModeInfo.getString());
        //修复 Bug-930
        if (layer == null) {
            return;
        }

        Rect curRect = layer.getBorderRect();
        if (curRect.left > mDefRect.left) {
            CalendarInfo preMode = getPreModeInfo(mCurSelectedModeInfo);
            CalendarLayer preLayer = mLayerMap.get(preMode.getString());
            if (preLayer == null) {
                Rect rect = new Rect();
                rect.left = curRect.left - curRect.width();
                rect.top = mDefRect.top;
                rect.right = rect.left + curRect.width();
                rect.bottom = mDefRect.bottom;
                preMode = new CalendarInfo(rect, mDefRect, preMode.getYear(), preMode.getMonth(), preMode.getDay(), preMode.getMode());
                preLayer = createLayer(preMode);
                mLayerMap.put(preMode.getString(), preLayer);
                for (int i = 0; i < mLayerCreateListenerList.size(); i++) {
                    mLayerCreateListenerList.get(i).onAfterLayerCreate(preMode);
                }
            } else {
                Rect rect = preLayer.getBorderRect();
                preLayer.scrollBy(curRect.left - rect.right, mDefRect.top - rect.top);
            }
            mPreLayer = preLayer;
        } else {
            mPreLayer = null;
        }

        if (curRect.right < mDefRect.right) {
            CalendarInfo nextMode = getNextModeInfo(mCurSelectedModeInfo);
            CalendarLayer nextLayer = mLayerMap.get(nextMode.getString());
            if (nextLayer == null) {
                Rect rect = new Rect();
                rect.left = curRect.right;
                rect.top = mDefRect.top;
                rect.right = curRect.right + curRect.width();
                rect.bottom = mDefRect.bottom;
                nextMode = new CalendarInfo(rect, mDefRect, nextMode.getYear(), nextMode.getMonth(), nextMode.getDay(), nextMode.getMode());
                nextLayer = createLayer(nextMode);
                mLayerMap.put(nextMode.getString(), nextLayer);
                for (int i = 0; i < mLayerCreateListenerList.size(); i++) {
                    mLayerCreateListenerList.get(i).onAfterLayerCreate(nextMode);
                }
            } else {
                Rect rect = nextLayer.getBorderRect();
                nextLayer.scrollBy(curRect.right - rect.left, mDefRect.top - rect.top);
            }
            mNextLayer = nextLayer;
        } else {
            mNextLayer = null;
        }
        computeSelectedPositionX();
    }

    private void computeSelectedPositionX() {
        Rect rect = mCurLayer.getBorderRect();
        if (rect.left < mHalfX && rect.right > mHalfX) {
            return;  // 当前选中状态不变
        }

        if (mPreLayer != null) {
            rect = mPreLayer.getBorderRect();
            if (rect.left < mHalfX && rect.right > mHalfX) {
                mNextLayer = mCurLayer;
                mCurLayer = mPreLayer;
                mCurSelectedModeInfo = mCurLayer.getModeInfo();
                for (int i = 0; i < mPageListenerList.size(); i++) {
                    mPageListenerList.get(i).onPageSelectedChange(mCurLayer.getModeInfo());
                }
                return;
            }
        }

        if (mNextLayer != null) {
            rect = mNextLayer.getBorderRect();
            if (rect.left < mHalfX && rect.right > mHalfX) {
                mPreLayer = mCurLayer;
                mCurLayer = mNextLayer;
                mCurSelectedModeInfo = mCurLayer.getModeInfo();
                for (int i = 0; i < mPageListenerList.size(); i++) {
                    mPageListenerList.get(i).onPageSelectedChange(mCurLayer.getModeInfo());
                }
            }
        }
    }

    private void computeItemLocationY() {
        CalendarLayer layer = mLayerMap.get(mCurSelectedModeInfo.getString());
        //修复 Bug-930
        if (layer == null) {
            return;
        }

        Rect curRect = layer.getBorderRect();
        if (curRect.top > mDefRect.top) {
            CalendarInfo preMode = getPreModeInfo(mCurSelectedModeInfo);
            CalendarLayer preLayer = mLayerMap.get(preMode.getString());
            if (preLayer == null) {
                Rect rect = new Rect();
                rect.left = curRect.left;
                rect.top = curRect.top - curRect.height();
                rect.right = curRect.right;
                rect.bottom = rect.top + curRect.height();
                preMode = new CalendarInfo(rect, preMode.getYear(), preMode.getMonth(), preMode.getDay(), preMode.getMode());
                preLayer = createLayer(preMode);
                mLayerMap.put(preMode.getString(), preLayer);
                for (int i = 0; i < mLayerCreateListenerList.size(); i++) {
                    mLayerCreateListenerList.get(i).onAfterLayerCreate(preMode);
                }
            } else {
                Rect rect = preLayer.getBorderRect();
                preLayer.scrollBy(0, curRect.top - rect.bottom);
            }
            mPreLayer = preLayer;
        } else {
            mPreLayer = null;
        }

        if (curRect.bottom < mDefRect.bottom) {
            CalendarInfo nextMode = getNextModeInfo(mCurSelectedModeInfo);
            CalendarLayer nextLayer = mLayerMap.get(nextMode.getString());
            if (nextLayer == null) {
                Rect rect = new Rect();
                rect.left = curRect.left;
                rect.top = curRect.bottom;
                rect.right = curRect.right;
                rect.bottom = rect.top + curRect.height();
                nextMode = new CalendarInfo(rect, mDefRect, nextMode.getYear(), nextMode.getMonth(), nextMode.getDay(), nextMode.getMode());
                nextLayer = createLayer(nextMode);
                mLayerMap.put(nextMode.getString(), nextLayer);
                for (int i = 0; i < mLayerCreateListenerList.size(); i++) {
                    mLayerCreateListenerList.get(i).onAfterLayerCreate(nextMode);
                }
            } else {
                Rect rect = nextLayer.getBorderRect();
                nextLayer.scrollBy(0, curRect.bottom - rect.top);
            }
            mNextLayer = nextLayer;
        } else {
            mNextLayer = null;
        }
        computeSelectedPositionY();
    }

    private void computeSelectedPositionY() {
        Rect rect = mCurLayer.getBorderRect();
        if (rect.top < mHalfY && rect.bottom > mHalfY) {
            return;  // 当前选中状态不变
        }

        if (mPreLayer != null) {
            rect = mPreLayer.getBorderRect();
            if (rect.top < mHalfY && rect.bottom > mHalfY) {
                mNextLayer = mCurLayer;
                mCurLayer = mPreLayer;
                mCurSelectedModeInfo = mCurLayer.getModeInfo();
                return;
            }
        }

        if (mNextLayer != null) {
            rect = mNextLayer.getBorderRect();
            if (rect.top < mHalfY && rect.bottom > mHalfY) {
                mPreLayer = mCurLayer;
                mCurLayer = mNextLayer;
                mCurSelectedModeInfo = mCurLayer.getModeInfo();
            }
        }
    }

    private void rollBackToCurLayer() {
        Rect curRect = mCurLayer.getBorderRect();
        int dx = mDefRect.left - curRect.left;
        if (dx == 0) {
            for (int i = 0; i < mPageListenerList.size(); i++) {
                mPageListenerList.get(i).onPageScrollEnd(mCurLayer.getModeInfo());
            }
            return;
        }
        mIsScroll = true;
        mScroller.startScroll(0, 0, dx, 0, ROLL_BACK_DUR);
        mScrollY = 0;
        mScrollX = 0;
        mScroller.computeScrollOffset();
        Message msg = Message.obtain();
        msg.what = InnerHandler.MESSAGE_FLING;
        msg.arg1 = mScroller.getCurrX() - mScrollX;
        msg.arg2 = mScroller.getCurrY() - mScrollY;
        mHandler.sendMessage(msg);
        mScrollY = mScroller.getCurrY();
        mScrollX = mScroller.getCurrX();
    }

    private boolean isFling() {
        mVelocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
        boolean isFling = false;
        float velocityX = mVelocityTracker.getXVelocity();
        if (Math.abs(velocityX) > mMinimumFlingVelocity && Math.abs(velocityX) <= mMaximumFlingVelocity) {
            int dx;
            Rect curRect = mCurLayer.getBorderRect();
            if (velocityX > 0) {
                if (curRect.left > mDefRect.left) {
                    dx = mDefRect.right - curRect.left;
                } else {
                    dx = mDefRect.left - curRect.left;
                }
            } else {
                if (curRect.right < mDefRect.right) {
                    dx = mDefRect.left - curRect.right;
                } else {
                    dx = mDefRect.left - curRect.left;
                }
            }
            if (dx == 0) {
                return false;
            }
            mIsScroll = true;
            mScroller.startScroll(0, 0, dx, 0, ROLL_BACK_DUR);
            isFling = true;
        }
        mVelocityTracker.clear();
        if (isFling) {
            mScrollY = 0;
            mScrollX = 0;
            mScroller.computeScrollOffset();
            Message msg = Message.obtain();
            msg.what = InnerHandler.MESSAGE_FLING;
            msg.arg1 = mScroller.getCurrX() - mScrollX;
            msg.arg2 = mScroller.getCurrY() - mScrollY;
            mHandler.sendMessage(msg);
            mScrollY = mScroller.getCurrY();
            mScrollX = mScroller.getCurrX();
            return true;
        }
        return false;
    }

    private class InnerHandler extends Handler {

        public static final int MESSAGE_FLING = 1;

        public InnerHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_FLING) {
                computeItem(msg.arg1, msg.arg2);
                if (mScroller.computeScrollOffset()) {
                    Message msg2 = Message.obtain();
                    msg2.what = MESSAGE_FLING;
                    msg2.arg1 = mScroller.getCurrX() - mScrollX;
                    msg2.arg2 = mScroller.getCurrY() - mScrollY;
                    mHandler.sendMessage(msg2);
                    mScrollY = mScroller.getCurrY();
                    mScrollX = mScroller.getCurrX();
                } else {
                    mScrollY = 0;
                    mScrollX = 0;
                    mHandler.removeMessages(MESSAGE_FLING);
                    mIsScroll = false;
                    mIsDragLeftOrRight = false;
                    if (mCurLayer.getBorderRect().left == mDefRect.left) {
                        mIsForceScroll = false;
                        for (int i = 0; i < mPageListenerList.size(); i++) {
                            mPageListenerList.get(i).onPageScrollEnd(mCurLayer.getModeInfo());
                        }
                    } else {
                        mAlwaysInTapRegion = true;
                        rollBackToCurLayer();
                    }
                }
            }
        }
    }

    public void registerOnPageChangeListener(OnPageChangeListener listener) {
        if (mPageListenerList.contains(listener)) {
            return;
        }
        mPageListenerList.add(listener);
    }

    public void registerOnLayerCreateListener(OnLayerCreateListener listener) {
        if (mLayerCreateListenerList.contains(listener)) {
            return;
        }
        mLayerCreateListenerList.add(listener);
    }

    public void setCurLayerMode(CalendarInfo curInfo) {
        CalendarLayer layer = mLayerMap.get(curInfo.getString());
        if (layer == null) {
            layer = createLayer(curInfo);
            mLayerMap.put(curInfo.getString(), layer);
            for (int i = 0; i < mLayerCreateListenerList.size(); i++) {
                mLayerCreateListenerList.get(i).onAfterLayerCreate(curInfo);
            }
        }
        mCurLayer = layer;
        int dx = curInfo.getRect().left - mCurLayer.getBorderRect().left;
        int dy = curInfo.getRect().top - mCurLayer.getBorderRect().top;
        mCurLayer.scrollBy(dx, dy);
        mCurSelectedModeInfo = curInfo;
        computeItemLocationX();
        mView.invalidate();
    }

    public View getView() {
        return mView;
    }

    public boolean isScroll() {
        return isShow() && (mIsScroll || mIsForceScroll);
    }

    public void setCanScrollLeftOfRight(boolean flag) {
        mCanScrollLeftOfRight = flag;
    }

    public Rect getDefRect() {
        return mDefRect;
    }

    public CalendarLayer getCurLayer() {
        return mCurLayer;
    }

    public boolean isDragLeftOrRight() {
        return mIsDragLeftOrRight;
    }

    public void setShow(boolean show) {
        mIsShow = show;
        if (show) {
            computeItemLocationX();
        }
        mView.invalidate();
    }

    public boolean isShow() {
        return mIsShow;
    }

    public boolean containInfo(CalendarInfo info) {
        return info != null && mLayerMap.containsKey(info.getString());
    }

    public CalendarLayer getLayerInCache(CalendarInfo info) {
        return mLayerMap.get(info.getString());
    }

    public HashMap<String, CalendarLayer> getLayerMap() {
        return mLayerMap;
    }

    public void clear() {
        mLayerMap.clear();
        mPageListenerList.clear();
        mLayerCreateListenerList.clear();
    }
}