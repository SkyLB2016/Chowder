package com.sky.chowder.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sky.chowder.R;
import com.sky.utils.ScreenUtils;

/**
 * Created by SKY on 2015/8/19.
 * 下拉菜单，待调整
 */
public class PullLayout extends ScrollView {

    //private ViewGroup wallPaper;
    private ViewGroup mMenu;
    private ViewGroup mContent;
    private ViewGroup titleBar;
    private Button back;//返回键
    private Button right;
    private TextView action_title;//标题

    private ViewGroup ll_weather;
    private EyeView ev;

    private ObjectAnimator animator;

    private Boolean once = true;
    private int screenWidth, screenHeight;

    private State mState = State.OPEN;
    private float downX, downY;//按下时的xy
    private int menuHeight;

    public PullLayout(Context context) {
        this(context, null);
    }

    public PullLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        screenWidth = ScreenUtils.getWidthPX(context);
        screenHeight = ScreenUtils.getHeightPX(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setVerticalScrollBarEnabled(false);
        titleBar = (ViewGroup) findViewById(R.id.titlebar);
        action_title = (TextView) findViewById(R.id.action_title);
        back = (Button) findViewById(R.id.action_back);
        right = (Button) findViewById(R.id.action_menu);
        setTitleBar(R.mipmap.ic_back, R.color.white);

        mMenu = (ViewGroup) findViewById(R.id.rl_menu);
        ev = (EyeView) findViewById(R.id.ev);
        ll_weather = (ViewGroup) findViewById(R.id.ll_weather);
        ev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

        mContent = (ViewGroup) findViewById(R.id.ll_content);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (once) {
            menuHeight = mMenu.getLayoutParams().height = screenHeight / 4 * 3;
            once = false;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        titleBar.layout(0, 0, titleBar.getWidth(), titleBar.getHeight());
        mContent.layout(0, menuHeight, mContent.getWidth(), mContent.getHeight() + menuHeight);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ((int) ev.getY() - downY);
                //moveY>0下拉，同时改变menu的大小；<0上拉，无操作
//                if (moveY > 0 && getScrollY() == 0) {
                if (moveY > 0 && isOpen()) {
                    setT(-moveY / 4);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                //open状态下下拉后，恢复menu的状态
                if (ev.getY() - downY > 0 && isOpen()) {
                    animatePull(0);
                    return true;
                }
                //menu的打开与关闭，getScrollY()必须小于menuHeight
                if (ev.getY() - downY > 20 && getScrollY() < menuHeight) {
                    open();
                } else if (ev.getY() - downY < -20 && getScrollY() < menuHeight) {
                    close();
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (t > menuHeight) {
            ObjectAnimator.ofFloat(titleBar,"translationY",t);
            return;
        } else {
            animateScroll(t);
        }
    }

    //整个 view中的精华之所在
    private void setT(int t) {
        scrollTo(0, t);
        if (t < 0) {
            animatePull(t);
        }
    }

    private void animateScroll(int t) {
        float percent = (float) t / menuHeight;
        ObjectAnimator.ofFloat(mMenu,"translationY",t);
        ObjectAnimator.ofFloat(mContent,"translationY", titleBar.getHeight() * percent);
        ObjectAnimator.ofFloat(ev,"translationY",-t / 2);
        ObjectAnimator.ofFloat(ll_weather,"translationY",-t / 2);

//        ViewHelper.setTranslationY(mMenu, t);
//        ViewHelper.setTranslationY(mContent, titleBar.getHeight() * percent);
//        ViewHelper.setTranslationY(ev, -t / 2);
//        ViewHelper.setTranslationY(ll_weather, -t / 2);
        ev.setRadius((int) (menuHeight * 0.25f * (1 - percent)));
        action_title.setTextColor(evaluate(percent, Color.WHITE, Color.BLACK));
        ObjectAnimator.ofFloat(titleBar,"translationY",t);
//        ViewHelper.setTranslationY(titleBar, t);
        titleBar.setBackgroundColor(evaluate(percent, Color.argb(00, 255, 255, 255), Color.argb(255, 255, 255, 255)));
        if (percent == 1) {
            back.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.ic_back), null, null, null);
        } else if (percent == 0) {
            back.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.ic_done), null, null, null);
        }
    }

    private void animatePull(int t) {
        mMenu.getLayoutParams().height = menuHeight - t;
        mMenu.requestLayout();
        ObjectAnimator.ofFloat(mContent,"translationY",-t);
//        ViewHelper.setTranslationY(mContent, -t);

        float percent = (float) t / menuHeight;
        ev.setScaleX(1-percent);
        ev.setScaleY(1-percent);
        ObjectAnimator.ofFloat(ll_weather,"translationY", -t / 2);
//        ViewHelper.setScaleX(ev, 1 - percent);
//        ViewHelper.setScaleY(ev, 1 - percent);
//        ViewHelper.setTranslationY(ll_weather, -t / 2);

    }

    private Integer evaluate(float fraction, Object startValue, Integer endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;
        int endInt = endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;
        return  (startA + (int) (fraction * (endA - startA))) << 24
                | (startR + (int) (fraction * (endR - startR))) << 16
                |  (startG + (int) (fraction * (endG - startG))) << 8
                |  (startB + (int) (fraction * (endB - startB)));
    }

    public Boolean isOpen() {
        return mState == State.OPEN;
    }

    public void toggleMenu() {
        if (isOpen()) {
            close();
        } else {
            open();
        }
    }

    public void close() {
        if (animator != null && animator.isRunning()) {
            return;
        }
        int start = getScrollY();
        animator = ObjectAnimator.ofInt(this, "t", start, menuHeight);
//        animator.setInterpolator(new DecelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mState = State.CLOSE;
            }
        });
        animator.setDuration(250);
        animator.start();
    }

    public void open() {
        if (animator != null && animator.isRunning()) {
            return;
        }
        int start = getScrollY();
        int center = (int) (-getScrollY() / 2.2f);
        animator = ObjectAnimator.ofInt(this, "t", start, center, 0);
//        animator.setInterpolator(new DecelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mState = State.OPEN;
            }
        });
        animator.setDuration(400);
        animator.start();
    }

    /**
     * 设置titlebar
     */
    private void setTitleBar(int backImgId, int titleColorId) {
        back.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(backImgId), null, null, null);
        titleBar.setBackgroundResource(R.color.dark_orange);
        action_title.setTextColor(getResources().getColor(titleColorId));
    }

}