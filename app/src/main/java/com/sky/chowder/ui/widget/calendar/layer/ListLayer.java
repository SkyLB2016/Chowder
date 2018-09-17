package com.sky.chowder.ui.widget.calendar.layer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import com.sky.chowder.R;
import com.sky.chowder.ui.widget.calendar.common.CalendarColor;
import com.sky.chowder.ui.widget.calendar.common.CalendarInfo;
import com.sky.chowder.ui.widget.calendar.impl.OnListLayerScrollListener;

import java.util.ArrayList;
import java.util.List;

public class ListLayer implements CalendarLayer {

    private static final int DOWN_ID = 0;
    private static final int TOUCH_SLOP = 64;
    private static final int MAX_FLING_DISTANCE = 2000;
    private static final int MIN_FLING_DISTANCE = -2000;

    private static final int STATE_PULL_REFRESH = 1;        //上拉刷新
    private static final int STATE_RELEASE_REFRESH = 2;     //松手刷新
    private static final int STATE_REFRESHING = 3;          //正在刷新

    private Rect mRect = new Rect();
    private Rect mBorderRect = new Rect();
    private Rect mDividerRect = new Rect();
    private View mView;
    private Paint mPaint = new Paint();
    private CalendarInfo mModeInfo;
    private InnerHandler mHandler;
    private VelocityTracker mVelocityTracker;
    private Scroller mScroller;
    private Scroller mPullUpScroller;
    private int mDownY, mDownX;
    private int mScrollY, mScrollX, mPullUpScrollY, mPullUpScrollX;
    private int mMinimumFlingVelocity;
    private int mMaximumFlingVelocity;
    private boolean mAlwaysInTapRegion = true;
    private boolean mIsCanDrag = true;
    private boolean mIsCanFling = true;
    private int refreshState = STATE_PULL_REFRESH;
    private boolean mIsRefreshRectShow;
    private boolean mIsRefreshing;

    private OnItemClickListener mItemClickLis;
    private OnListLayerScrollListener mListLayerLis;
    private OnPullUpRefreshListener mPullUpRefreshLis;
    private List<CalendarLayer> mListData = new ArrayList<>();

    private String mNoDataStr = "今天还没有日程~";
    private String mPullRefreshStr = "上拉刷新";
    private String mRefreshingStr = "正在刷新...";
    private String mReleaseStr = "松手刷新";
    private Bitmap mNoDataBmp;
    private int mNoDataWidth, mNoDataHeight;
    private int mNoDataBmpWidth, mNoDataBmpHeight;
    private int mNoDataTextColor = CalendarColor.LIGHT_GRAY;
    private int mRefreshTextColor = CalendarColor.TITLE_GRAY;
    private int mNoDataTextPaddingTop = 8;                  //没有数据时,文字与图片的间距
    private int mDividerHeight = 10;                        //分隔符的高度,单位 dp
    private float mNoDataTextSize = 14;                     //无数据文字大小,单位 sp
    private float mNoDataTextDescent;
    private float mNoDataStrWidth;
    private int mLoadBmpWidth = 25;
    private int mLoadBmpHeight = 25;
    private int mLoadAreaHeight = 50;
    private int mLoadImgPaddingLeft = 20;
    private int mLoadTextPaddingImgLeft = 10;
    private int mLoadTextSize = 16;
    private Rect mRefreshRect;
    private float mRefreshTextHeight;
    private float mRefreshTextAscent;
    private Resources resources;

    public ListLayer(View view, CalendarInfo info) {
        mView = view;
        mModeInfo = info;
        Context context = view.getContext();
        resources = view.getResources();

        DisplayMetrics metrics = resources.getDisplayMetrics();
        mNoDataTextPaddingTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mNoDataTextPaddingTop, metrics);
        mDividerHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mDividerHeight, metrics);
        mLoadAreaHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mLoadAreaHeight, metrics);
        mLoadImgPaddingLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mLoadImgPaddingLeft, metrics);
        mLoadTextPaddingImgLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mLoadTextPaddingImgLeft, metrics);
        mLoadBmpWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mLoadBmpWidth, metrics);
        mLoadBmpHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mLoadBmpHeight, metrics);
        mNoDataTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mNoDataTextSize, metrics);
        mLoadTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mLoadTextSize, metrics);

        mDividerRect.top = mModeInfo.getRect().top;
        mDividerRect.left = mModeInfo.getRect().left;
        mDividerRect.right = mModeInfo.getRect().right;
        mDividerRect.bottom = mModeInfo.getRect().top + mDividerHeight;

        mRect.top = mDividerRect.bottom;
        mRect.left = mDividerRect.left;
        mRect.right = mDividerRect.right;
        mRect.bottom = mModeInfo.getRect().bottom;

        mBorderRect = mModeInfo.getRect();

        mHandler = new InnerHandler(context.getMainLooper());
        mScroller = new Scroller(context, new DecelerateInterpolator());
        mPullUpScroller = new Scroller(context, new DecelerateInterpolator());
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity();
        mVelocityTracker = VelocityTracker.obtain();

        mRefreshRect = new Rect(0, 0, mRect.width(), mLoadAreaHeight);
        mRefreshRect.offsetTo(mRect.left, mRect.bottom);
        initPaint(R.mipmap.empty_schedule, mNoDataStr);

    }

    private void initPaint(int resNoDataId, String noDataStr) {
        mPaint.setAntiAlias(true);
        //初始化下拉刷新的数据
        mPaint.setTextSize(mLoadTextSize);
        FontMetrics textFontMetrics = mPaint.getFontMetrics();
        mRefreshTextHeight = textFontMetrics.descent - textFontMetrics.ascent;
        mRefreshTextAscent = textFontMetrics.ascent;
        //初始化空数据的信息
        mPaint.setTextSize(mNoDataTextSize);
        mNoDataStrWidth = mPaint.measureText(noDataStr);
        textFontMetrics = mPaint.getFontMetrics();
        float noDataTextHeight = textFontMetrics.descent - textFontMetrics.ascent;
        mNoDataTextDescent = textFontMetrics.descent;
        //初始化空日程和下拉刷新的数据
        mNoDataBmp = BitmapFactory.decodeResource(resources, resNoDataId);
        mNoDataBmpWidth = mNoDataBmp.getWidth();    // 除密度之后, 在高分辨率手机上显示特别小
        mNoDataBmpHeight = mNoDataBmp.getHeight();
        if (mNoDataBmpWidth < mNoDataStrWidth) {
            mNoDataWidth = (int) mNoDataStrWidth;
        } else {
            mNoDataWidth = mNoDataBmpWidth;
        }
        mNoDataHeight = (int) (mNoDataTextPaddingTop + noDataTextHeight + mNoDataBmpHeight);

        resetItemLocationByTop();
    }

    public void setNoData(int resNoDataId, @NonNull String noDataStr) {
        this.mNoDataStr = noDataStr;
        initPaint(resNoDataId, mNoDataStr);
    }

    public void resetItemLocationByTop() {
        int tempHeight = 0;
        for (int i = 0; i < mListData.size(); i++) {
            CalendarLayer layer = mListData.get(i);
            layer.scrollBy(mRect.left - layer.getBorderRect().left, mRect.top + tempHeight - layer.getBorderRect().top);
            tempHeight += layer.getBorderRect().height();
        }
        mView.invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        //画背景
        mPaint.setStyle(Style.FILL_AND_STROKE);
        mPaint.setColor(CalendarColor.BG);
        canvas.drawRect(mRect, mPaint);
        //画分割线(这里背景和分割线其实是一样的颜色,分割线不画也可以)
        mPaint.setStyle(Style.FILL_AND_STROKE);
        mPaint.setColor(CalendarColor.BG);
        canvas.drawRect(mDividerRect, mPaint);
        if (mListData.size() <= 0) {
            //画无数据
            drawNoData(canvas, mPaint);
        } else {
            //画条目
            for (int i = 0; i < mListData.size(); i++) {
                CalendarLayer layer = mListData.get(i);
                if (!isOutOfBorder(layer.getBorderRect())) {
                    layer.onDraw(canvas);
                }
            }
        }
        //画上拉刷新
        drawPullStr(canvas, mPaint);
    }

    private void drawNoData(Canvas canvas, Paint paint) {
        //计算图片和文字的需要的区域
        Rect noDataRect = new Rect();
        noDataRect.left = mRect.left + (mRect.width() - mNoDataWidth) / 2;
        noDataRect.right = noDataRect.left + mNoDataWidth;
        noDataRect.top = mRect.top + (mRect.height() - mNoDataHeight) / 2;
        noDataRect.bottom = noDataRect.top + mNoDataHeight;
        //绘制没有数据的图片部分
        Rect src = new Rect(0, 0, mNoDataBmp.getWidth(), mNoDataBmp.getHeight());
        Rect noDataBitmapRect = new Rect();
        noDataBitmapRect.left = noDataRect.left + (mNoDataWidth - mNoDataBmpWidth) / 2;
        noDataBitmapRect.right = noDataBitmapRect.left + mNoDataBmpWidth;
        noDataBitmapRect.top = noDataRect.top;
        noDataBitmapRect.bottom = noDataRect.top + mNoDataBmpHeight;
        canvas.drawBitmap(mNoDataBmp, src, noDataBitmapRect, paint);
        //绘制没有数据的文字部分
        paint.setTextSize(mNoDataTextSize);
        paint.setColor(mNoDataTextColor);
        float x = mRect.left + (mRect.width() - mNoDataStrWidth) / 2;
        float y = noDataRect.bottom + mNoDataTextPaddingTop - mNoDataTextDescent;
        canvas.drawText(mNoDataStr, x, y, paint);
    }

    private void drawPullStr(Canvas canvas, Paint paint) {
        if (mIsRefreshRectShow) {
            //画刷新背景
            mPaint.setColor(CalendarColor.LIGHT_GRAY);
            canvas.drawRect(mRefreshRect, paint);
            //画刷新文字
            paint.setTextSize(mLoadTextSize);
            String text = "";
            if (refreshState == STATE_REFRESHING) {
                text = mRefreshingStr;
            }
            if (refreshState == STATE_RELEASE_REFRESH) {
                text = mReleaseStr;
            }
            if (refreshState == STATE_PULL_REFRESH) {
                text = mPullRefreshStr;
            }
            float x = mRefreshRect.centerX() - paint.measureText(text) / 2;
            float y = mRefreshRect.top + (mRefreshRect.height() - mRefreshTextHeight) / 2 - mRefreshTextAscent;
            paint.setColor(mRefreshTextColor);
            canvas.drawText(text, x, y, paint);
        }
    }

    private boolean isOutOfBorder(Rect a) {
        return mBorderRect != null && (a.left > mBorderRect.right || a.right < mBorderRect.left || a.top > mBorderRect.bottom || a.bottom < mBorderRect.top);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsCanFling) {
            return false;
        }
        if (!mPullUpScroller.isFinished()) {
            return false;
        }
        if (!mRect.contains((int) event.getX(), (int) event.getY())) {
            sendRefreshStartMessage();
            return false;
        }
        mVelocityTracker.addMovement(event);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                mScroller.forceFinished(true);
                mDownX = (int) event.getX(DOWN_ID);
                mDownY = (int) event.getY(DOWN_ID);
                if (!mIsRefreshRectShow) {
                    mRefreshRect.offsetTo(mRect.left, mRect.bottom);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int distanceX = (int) (event.getX(DOWN_ID) - mDownX);
                int distanceY = (int) (event.getY(DOWN_ID) - mDownY);
                mDownX = (int) event.getX(DOWN_ID);
                mDownY = (int) event.getY(DOWN_ID);
                int distance = distanceX * distanceX + distanceY * distanceY;
                //判断是否有滑动,以便触发点击事件
                if (mAlwaysInTapRegion && distance >= TOUCH_SLOP) {
                    mAlwaysInTapRegion = false;
                    distanceX = 0;
                    distanceY = 0;
                }
                if (mIsCanDrag && !mAlwaysInTapRegion) {
                    if (mListLayerLis != null) {
                        mListLayerLis.onScrollStart();
                    }
                    int dy;
                    if (isReadyPullUp(distanceY)) {
                        //允许上滑,计算更新刷新文字的区域
                        dy = computeRefreshRect(distanceY);
                    } else {
                        dy = resetDistanceY(distanceY);
                        if (mIsRefreshRectShow) {
                            mRefreshRect.offset(0, dy);
                        }
                    }
                    scrollItem(0, dy);
                    mView.invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mAlwaysInTapRegion) {
                    onClick(mDownX, mDownY);
                    mAlwaysInTapRegion = true;
                    break;
                }
                sendRefreshStartMessage();
                mAlwaysInTapRegion = true;
                if (mIsCanDrag && !isFling()) {
                    if (mListLayerLis != null) {
                        mListLayerLis.onScrollEnd();
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }

    public void sendRefreshStartMessage() {
        if (refreshState == STATE_PULL_REFRESH) {
            int offset = mRect.bottom - mRefreshRect.top;
            if (offset > 0) {
                //刷新头没有完全隐藏,隐藏刷新头
                sendRefreshEndMessage(offset, 200, InnerHandler.MESSAGE_REFRESH_END);
            } else {
                //已经隐藏,重置刷新头
                resetRefreshFlag();
            }
        } else {
            refreshState = STATE_REFRESHING;
            if (!mIsRefreshing && mPullUpRefreshLis != null) {
                mPullUpRefreshLis.onRefreshBegin(this);
            }
            mIsRefreshing = true;
            int distance = mRect.bottom - mRefreshRect.bottom;
            if (distance < 0) {
                distance = mRect.bottom - mRefreshRect.top;
            }
            sendRefreshEndMessage(distance, 200, InnerHandler.MESSAGE_REFRESHING);
        }
    }

    public void sendRefreshEndMessage(int distance, int dur, int messageWhat) {
        mPullUpScrollY = 0;
        mPullUpScrollX = 0;
        mPullUpScroller.startScroll(0, 0, 0, distance, dur);
        mPullUpScroller.computeScrollOffset();
        Message msg = Message.obtain();
        msg.what = messageWhat;
        msg.arg1 = mPullUpScroller.getCurrX() - mScrollX;
        msg.arg2 = mPullUpScroller.getCurrY() - mScrollY;
        mHandler.sendMessage(msg);
        mPullUpScrollY = mPullUpScroller.getCurrY();
        mPullUpScrollX = mPullUpScroller.getCurrX();
    }

    private void resetRefreshFlag() {
        mIsRefreshing = false;
        mIsRefreshRectShow = false;
        refreshState = STATE_PULL_REFRESH;
    }

    /**
     * 是否允许上滑拉出刷新文字
     */
    public boolean isReadyPullUp(int dy) {
        if (mListData.size() <= 0) {
            return true;
        }
        CalendarLayer last = mListData.get(mListData.size() - 1);
        return dy < 0 && mRect.bottom > (last.getBorderRect().bottom + dy);
    }

    /**
     * 根据滑动距离计算刷新状态
     */
    private int computeRefreshRect(int dy) {
        if (mRefreshRect.bottom + dy < mBorderRect.bottom) {
            dy = mBorderRect.bottom - mRefreshRect.bottom;
        }
        mRefreshRect.top += dy;
        mRefreshRect.bottom += dy;
        if (!mIsRefreshing) {
            mIsRefreshRectShow = true;
            if (mRefreshRect.bottom - mRect.bottom <= 0) {
                refreshState = STATE_RELEASE_REFRESH;
            } else {
                refreshState = STATE_PULL_REFRESH;
            }
        } else {
            refreshState = STATE_REFRESHING;
        }
        return dy;
    }

    private int resetDistanceY(int dy) {
        if (mListData.size() <= 0) {
            return 0;
        }
        CalendarLayer first = mListData.get(0);
        CalendarLayer last = mListData.get(mListData.size() - 1);

        if (dy > 0) {
            // 下滑
            if (first == null) {
                return 0;
            }
            int offset = mRect.top - first.getBorderRect().top;
            if (offset <= dy) {
                return offset;
            }
        } else {
            // 上滑
            if (last == null) {
                return 0;
            }
            int offset = mRect.bottom - last.getBorderRect().bottom;
            if (offset >= 0) {
                return 0;
            }
            if (offset >= dy) {
                return offset;
            }
        }
        return dy;
    }

    private void scrollItem(int dx, int dy) {
        if (mIsCanFling) {
            for (int i = 0; i < mListData.size(); i++) {
                mListData.get(i).scrollBy(dx, dy);
            }
            mView.invalidate();
        }
    }

    public void onClick(int x, int y) {
        int index = -1;
        for (int i = 0; i < mListData.size(); i++) {
            CalendarLayer layer = mListData.get(i);
            Rect rect = layer.getBorderRect();
            if ((mRect.contains(rect) || Rect.intersects(mRect, rect)) && rect.contains(x, y)) {
                index = i;
                break;
            }
        }
        if (index >= 0 && mItemClickLis != null) {
            mItemClickLis.onItemClick(this, index, mListData.get(index));
        }
    }

    private boolean isFling() {
        if (mIsRefreshRectShow) {
            return true;
        }
        mVelocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
        boolean isFling = false;
        float velocityY = mVelocityTracker.getYVelocity();
        if (Math.abs(velocityY) > mMinimumFlingVelocity && Math.abs(velocityY) <= mMaximumFlingVelocity) {
            if (mListData.size() <= 0) {
                return false;
            }
            CalendarLayer first = mListData.get(0);
            CalendarLayer last = mListData.get(mListData.size() - 1);
            if (velocityY > 0) {
                // 下滑
                if (first == null) {
                    return false;
                }
                int maxDy = mRect.top - first.getBorderRect().top;
                if (maxDy <= 0) {
                    return false;
                }
            } else {
                if (last == null) {
                    return false;
                }
                int minDy = mRect.bottom - last.getBorderRect().bottom;
                if (minDy >= 0) {
                    return false;
                }
            }
            mScroller.fling(0, 0, 0, (int) velocityY, 0, 0, MIN_FLING_DISTANCE, MAX_FLING_DISTANCE);
            isFling = true;
        }
        mVelocityTracker.clear();
        if (isFling && mIsCanFling) {
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

    @Override
    public void scrollBy(int dx, int dy) {
        mRect.offset(dx, dy);
        mBorderRect.offset(dx, dy);
        mDividerRect.offset(dx, dy);
        mRefreshRect.offset(dx, dy);
        for (int i = 0; i < mListData.size(); i++) {
            mListData.get(i).scrollBy(dx, dy);
        }
    }

    @Override
    public Rect getBorderRect() {
        return mBorderRect;
    }

    public Rect getRect() {
        return mRect;
    }

    @Override
    public CalendarInfo getModeInfo() {
        return mModeInfo;
    }

    private class InnerHandler extends Handler {
        public static final int MESSAGE_FLING = 1;
        public static final int MESSAGE_REFRESH_END = 2;
        public static final int MESSAGE_REFRESHING = 3;

        public InnerHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_FLING:
                    int dy = resetDistanceY(msg.arg2);
                    if (msg.arg2 != 0 && dy == 0) {
                        //到达边界
                        mScroller.forceFinished(true);
                    } else {
                        scrollItem(0, dy);
                        if (msg.arg2 > 0 && mIsRefreshing) {
                            mRefreshRect.offset(0, dy);
                        }
                    }
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
                        if (mListLayerLis != null) {
                            mListLayerLis.onScrollEnd();
                        }
                    }
                    break;
                case MESSAGE_REFRESH_END:
                    mRefreshRect.offset(0, msg.arg2);
                    scrollItem(0, msg.arg2);
                    if (mPullUpScroller.computeScrollOffset()) {
                        Message msg2 = Message.obtain();
                        msg2.what = MESSAGE_REFRESH_END;
                        msg2.arg1 = mPullUpScroller.getCurrX() - mPullUpScrollX;
                        msg2.arg2 = mPullUpScroller.getCurrY() - mPullUpScrollY;
                        mHandler.sendMessage(msg2);
                        mPullUpScrollY = mPullUpScroller.getCurrY();
                        mPullUpScrollX = mPullUpScroller.getCurrX();
                    } else {
                        mPullUpScrollY = 0;
                        mPullUpScrollX = 0;
                        resetRefreshFlag();
                        mRefreshRect.offsetTo(mRect.left, mRect.bottom);
                        mHandler.removeMessages(MESSAGE_REFRESH_END);
                    }
                    break;
                case MESSAGE_REFRESHING:
                    mRefreshRect.offset(0, msg.arg2);
                    scrollItem(0, msg.arg2);
                    if (mPullUpScroller.computeScrollOffset()) {
                        Message msg2 = Message.obtain();
                        msg2.what = MESSAGE_REFRESHING;
                        msg2.arg1 = mPullUpScroller.getCurrX() - mPullUpScrollX;
                        msg2.arg2 = mPullUpScroller.getCurrY() - mPullUpScrollY;
                        mHandler.sendMessage(msg2);
                        mPullUpScrollY = mPullUpScroller.getCurrY();
                        mPullUpScrollX = mPullUpScroller.getCurrX();
                    } else {
                        mPullUpScrollY = 0;
                        mPullUpScrollX = 0;
                        mHandler.removeMessages(MESSAGE_REFRESHING);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void refreshData() {
        List<CalendarLayer> result = null;
        if (mPullUpRefreshLis != null) {
            result = mPullUpRefreshLis.onRefresh(this);
        }
        // result不为空，更新，为空，不更新
        if (mIsRefreshing) {
            if (result != null) {
                mListData.clear();
                mListData.addAll(result);
                resetItemLocationByBottom();
            }
            mPullUpScroller.forceFinished(true);
            mHandler.removeMessages(InnerHandler.MESSAGE_REFRESHING);
            int distance = mRect.bottom - mRefreshRect.top;
            if (distance > 0) {
                sendRefreshEndMessage(distance, 300, InnerHandler.MESSAGE_REFRESH_END);
            } else {
                resetRefreshFlag();
            }
        } else {
            if (result != null) {
                mListData.clear();
                mListData.addAll(result);
                resetItemLocationByTop();
            }
        }
    }

    public List<CalendarLayer> getDataList() {
        return mListData;
    }

    public void resetItemLocationByBottom() {
        if (mListData.size() <= 0) {
            return;
        }
        int bottom = mRefreshRect.top;
        int offset = mRect.bottom - mRefreshRect.top;
        if (offset < 0) {
            bottom = mRect.bottom;
        }
        CalendarLayer layer;
        int tempHeight = 0;
        int first = mListData.size() - 1;
        for (int i = first; i >= 0; i--) {
            layer = mListData.get(i);
            layer.scrollBy(mRect.left - layer.getBorderRect().left, bottom - layer.getBorderRect().height() - tempHeight);
            tempHeight += layer.getBorderRect().height();
        }
        layer = mListData.get(0);
        int border = mRect.top - (mRect.bottom - mRefreshRect.top);
        if (layer.getBorderRect().top > border) {
            scrollItem(0, border - layer.getBorderRect().top);
        }
        mView.invalidate();
    }

    public interface OnItemClickListener {
        void onItemClick(ListLayer layer, int index, CalendarLayer item);
    }

    public void setOnItemClickListener(OnItemClickListener lis) {
        mItemClickLis = lis;
    }

    public interface OnPullUpRefreshListener {
        void onRefreshBegin(ListLayer layer);

        List<CalendarLayer> onRefresh(ListLayer layer);
    }

    public void setOnPullUpRefreshListener(OnPullUpRefreshListener lis) {
        mPullUpRefreshLis = lis;
    }

    public void setOnListLayerScrollListener(OnListLayerScrollListener listener) {
        if (listener == null && !mScroller.isFinished()) {
            //如果之前有监听,先将当前当前滑动事件终止,再使用新的监听
            mHandler.removeMessages(InnerHandler.MESSAGE_FLING);
            if (mListLayerLis != null) {
                mListLayerLis.onScrollEnd();
            }
        }
        mListLayerLis = listener;
    }

    public boolean isCanPullUp() {
        return true;
    }

    public boolean isCanPullDown() {
        if (mListData.size() <= 0) {
            return false;
        }
        CalendarLayer first = mListData.get(0);
        return mRect.top > first.getBorderRect().top;
    }

    public boolean isRefreshRectShow() {
        return mIsRefreshRectShow;
    }

    public void setFlingStatus(boolean isFling) {
        this.mIsCanFling = isFling;
    }

    public void setIsCanDrag(boolean flag) {
        mIsCanDrag = flag;
    }

    public boolean isCanDrag() {
        return mIsCanDrag;
    }

    public Resources getResources() {
        return mView.getResources();
    }
}
