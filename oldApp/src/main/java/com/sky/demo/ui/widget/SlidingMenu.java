package com.sky.demo.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import com.nineoldandroids.view.ViewHelper;
import com.sky.demo.utils.ScreenUtils;

/**
 * @author 彬 QQ 1136096189
 * @Description: TODO 横向的侧滑栏
 * @date 2015/8/17 15:56
 */
public class SlidingMenu extends HorizontalScrollView {

    private static final int SPEED = 2;
    private static final float SCALE = 1f;
    private static final float DEFAULTSCALE = 0.7f;

    private int screenWidth, screenHeight;
    private boolean once = true;
    private ViewGroup wallPaper;
    private ViewGroup menu;
    private ViewGroup content;

    private float content_scale = DEFAULTSCALE;//控制主布局缩放的大小
    private float mMenu_scale = DEFAULTSCALE;//控制菜单缩放的大小
    private int menuWidth;
    private float downX, downY;
    private long downTime;//按下时的时间，计算速度，展开或者关闭menu
    private State state = State.CLOSE;//默认状态

    private OnMenuListener onMenuListener;

    public void setOnMenuListener(OnMenuListener onMenuListener) {
        this.onMenuListener = onMenuListener;
    }

    public interface OnMenuListener {
        void OnScrollChangedListener(int l, int t, int oldl, int oldt);
    }

    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int[] screen = ScreenUtils.getWidthAndHeight(context);
        screenWidth = screen[0];
        screenHeight = screen[1];
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (once) {//执行一次
            wallPaper = (ViewGroup) getChildAt(0);
            menu = (ViewGroup) wallPaper.getChildAt(0);
            content = (ViewGroup) wallPaper.getChildAt(1);
            menuWidth = menu.getLayoutParams().width = screenWidth / 4 * 3;
            content.getLayoutParams().width = screenWidth;
            once = false;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //默认关闭
        this.smoothScrollTo(menuWidth, 0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN://获取按下时的信息
                downX = ev.getRawX();
                downY = ev.getRawY();
                downTime = System.currentTimeMillis();
                if (isOpen() && downX > menuWidth) return true;//打开时，某些部分的touch事件不向下传递

                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                //抬起时的XY以及时间
                float upX = ev.getRawX();
                float upY = ev.getRawY();
                long upTime = System.currentTimeMillis();
                //在open状态下判断点击后就抬起时，xy在content范围内，需要关闭menu
                if (isOpen()) {
                    float top = content.getHeight() * (SCALE - content_scale) / 2;
                    float bottom = content.getHeight() - top;
                    if (downX > menuWidth
                            //&& upX>menuWidth
                            && Math.abs(upX - downX) < 10//x方向不能移动超过10的距离
                            //&& Math.abs(upY - downY) < 10//Y方向不能移动超过10的距离
                            && downY > top && downY < bottom
                            && upY > top && upY < bottom
                            ) {
                        close();
                        return true;//不在向下传递
                    }
                }
                //计算速度,以及左右滑动时的操作
                float speed = (upX - downX) / (upTime - downTime);
                if (upX - downX < 0 //左滑
                        && Math.abs(speed) > SPEED && isOpen()) {
                    close();
                    return true;
                } else if (upX - downX > 0 //右滑
                        && Math.abs(speed) > SPEED && isClose()) {
                    open();
                    return true;
                }
                //正常滑动时，menu滑出不到一半时关闭，否则打开
                int x = getScrollX();
                if (x >= (menuWidth / 2)) {
                    close();
                } else {
                    open();
                }
                return true;//事件拦截不再向下传递
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float percent = l * SCALE / menuWidth;
        //打开时是1--0，关闭时是0--1
        //打开时menu的透明度及大小从0.7到1，content的大小从1到0.7
        //关闭时menu的透明度及大小从1到0.7，content的大小从0.7到1
        Float mPercent = SCALE - (SCALE - mMenu_scale) * percent;//0.7--1
        ViewHelper.setTranslationX(menu, menuWidth * percent * mMenu_scale);//有从后边拉出来的感觉
        ViewHelper.setAlpha(menu, mPercent);
        ViewHelper.setScaleX(menu, mPercent);
        ViewHelper.setScaleY(menu, mPercent);

        Float cPercent = content_scale + (SCALE - content_scale) * percent;
        ViewHelper.setScaleX(content, cPercent);
        ViewHelper.setScaleY(content, cPercent);
        //固定缩放时的中心
        ViewHelper.setPivotX(content, 0);
        ViewHelper.setPivotY(content, content.getHeight() / 2);
        if (onMenuListener != null)
            onMenuListener.OnScrollChangedListener(l, t, oldl, oldt);
    }

    public void open() {
        smoothScrollTo(0, 0);
        state = State.OPEN;
        if (menuState != null) menuState.OnOpen();
    }

    public void close() {
        smoothScrollTo(menuWidth, 0);
        state = State.CLOSE;
        if (menuState != null) menuState.OnClose();
    }

    public boolean isClose() {
        return state == State.CLOSE;
    }

    public boolean isOpen() {
        return state == State.OPEN;
    }

    public void toggleMenu() {
        if (isClose()) {
            open();
        } else {
            close();
        }
    }

    private MenuState menuState;

    //menu打开与关闭时的监听
    public void setOnMenuState(MenuState menuState) {
        this.menuState = menuState;
    }

    public interface MenuState {
        void OnOpen();

        void OnClose();
    }
}
