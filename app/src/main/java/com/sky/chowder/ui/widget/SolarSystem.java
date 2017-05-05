package com.sky.chowder.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.widget.FrameLayout;

import com.sky.chowder.R;


/**
 * @author sky
 * @ClassName: SolarSystem
 * @Description: TODO 卫星式菜单
 * @date 2015年4月9日 下午9:10:39
 */
public class SolarSystem extends FrameLayout implements OnClickListener {

    // 九个位置定义
    public static final int LEFT_TOP = 0;
    public static final int LEFT_BOTTOM = 1;
    public static final int RIGHT_TOP = 2;
    public static final int RIGHT_BOTTOM = 3;
    public static final int CENTER_TOP = 4;
    public static final int CENTER_BOTTOM = 5;
    public static final int CENTER = 6;
    public static final int CENTER_LEFT = 7;
    public static final int CENTER_RIGHT = 8;

    public static final int RADIUS = 100;
    public static final int TIME = 500;
    //计算XY的位置时用
    public static final int NEGATIVE = -1;
    private int tempX = 1;
    private int tempY = 1;


    /**
     * 子菜单点击事件接口
     */
    public interface onMenuItemClickListener {
        void onMenuItemClick(View view, int position);
    }

    /**
     * 菜单状态监听接口
     */
    public interface MenuState {
        void openMenu();

        void closeMenu();
    }

    private int position = CENTER;// 默认位置
    private State mState = State.CLOSE;// 默认状态
    //    private View centerMenu;// 默认选择最后一个控件为menu
    private onMenuItemClickListener menuItemClick;
    private MenuState menuState;
    private int radius;
    /**
     * 中心按钮是否旋转，默认旋转
     */
    private boolean rotaFlag = true;
    /**
     * 点击子view后是否回收,默认回收
     */
    private boolean recoverChildView = true;
    /**
     * true单次依次执行,false点击就执行，混乱效果很好玩。与isNowRotating绑定使用
     */
    private boolean isRotate = false;
    /**
     * 是否正在旋转
     */
    private boolean isNowRotating = false;

    /**
     * true单次依次执行,false点击就执行，混乱效果很好玩。与isNowRotating绑定使用
     */
    public void setIsRotate(boolean isRotating) {
        this.isRotate = isRotating;
    }

    public boolean isRecoverChildView() {
        return recoverChildView;
    }

    /**
     * 点击子view后是否回收,默认回收
     */
    public void setRecoverChildView(boolean recoverChildView) {
        this.recoverChildView = recoverChildView;
    }

    public void setPosition(int position) {
        if (this.position == position)
            return;
        if (isOpen()) toggleMenu(300);
        this.position = position;
        setTempXY();
        requestLayout();
    }

    /**
     * 中心按钮是否旋转，默认旋转
     */
    public void setRotaMenu(boolean flag) {
        rotaFlag = flag;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setOnMenuItemClickListener(onMenuItemClickListener menuItemClick) {
        this.menuItemClick = menuItemClick;
    }

    public void setOnMenuState(MenuState MenuState) {
        this.menuState = MenuState;
    }

    public SolarSystem(Context context) {
        this(context, null);
    }

    public SolarSystem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SolarSystem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 半径默认值
        radius = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, RADIUS, getResources().getDisplayMetrics());
        // 获取自定义属性的值
        TypedArray style = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SolarSystem, defStyleAttr, 0);
        position = style.getInt(R.styleable.SolarSystem_position, CENTER);//控件所处位置
        radius = style.getDimensionPixelSize(R.styleable.SolarSystem_radius, radius);//获取半径值
        style.recycle();// 释放
        setTempXY();

    }

    private void setTempXY() {
        tempX = tempY = 1;//重置
        if (position == RIGHT_TOP || position == RIGHT_BOTTOM || position == CENTER_RIGHT) {
            tempX = NEGATIVE;
        }
        if (position == LEFT_BOTTOM || position == RIGHT_BOTTOM || position == CENTER_BOTTOM || position == CENTER_RIGHT) {
            tempY = NEGATIVE;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 获取宽高与模式
        int layoutWidth = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int layoutHeight = MeasureSpec.getSize(heightMeasureSpec);
        int HeightMode = MeasureSpec.getMode(heightMeasureSpec);

//        // AT_MOST模式即wrap_content时测量父控件的宽高
        int width = 0;
        int height = 0;
        View childAt;
        MarginLayoutParams lp;
        int childWidth;
        int childHeight;
        int childCount = getChildCount();// 子view的数量
        for (int i = 0; i < childCount; i++) {
            childAt = getChildAt(i);
            measureChild(childAt, widthMeasureSpec, heightMeasureSpec);// 测量子view
            // 获取子view的margin
            lp = (MarginLayoutParams) childAt.getLayoutParams();
            childWidth = childAt.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            childHeight = childAt.getMeasuredHeight() + lp.bottomMargin + lp.topMargin;
            // 取出子view中最大的宽高
            width = Math.max(childWidth, width);
            height = Math.max(childHeight, height);
        }

        if (widthMode == MeasureSpec.EXACTLY && HeightMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(layoutWidth, layoutHeight);
            return;
        }
        // 居于四个角时父控件的宽高
        width += radius;
        height += radius;
        // 居中时宽高翻倍
        if (position == CENTER) {
            width += width;
            height += height;
        } else if (position == CENTER_BOTTOM || position == CENTER_TOP) {
            // 居中且在顶部或底部时，宽翻倍
            width += width;
        } else if (position == CENTER_LEFT || position == CENTER_RIGHT) {
            // 居中且在左或右边时高翻倍
            height += height;
        }
        // 判断宽高是否超越EXACTLY即match模式下的宽高
        if (layoutWidth - getPaddingLeft() - getPaddingRight() < width) {
            width = layoutWidth - getPaddingLeft() - getPaddingRight();
        }
        if (layoutHeight - getPaddingTop() - getPaddingBottom() < height) {
            height = layoutHeight - getPaddingTop() - getPaddingBottom();
        }
        // 载入宽高
        setMeasuredDimension(width + getPaddingLeft() + getPaddingRight(),
                height + getPaddingTop() + getPaddingBottom());
    }
//测试ondraw与dispatchDraw的区别，定义view用ondraw，定义viewgroup用dispatchDraw
//    @Override
//    protected void dispatchDraw(Canvas canvas) {
//        super.dispatchDraw(canvas);
//        Paint paint =new Paint();
//        paint.setColor(Color.BLACK);
//        paint.setStrokeWidth(50);
//        paint.setStyle(Paint.Style.FILL);
//        canvas.drawLine(0,0,1000,1000,paint);
//    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        // 获取宽高 padding 子view个数
        int childCount = getChildCount();

        int childPadLeft;
        int childPadTop;
        // 定义centermenu的监听
        setMenuListener(childCount);
        int menuWidth = getChildAt(childCount - 1).getWidth();
        int menuHeight = getChildAt(childCount - 1).getHeight();
        View childAt;
        MarginLayoutParams lp;
        int childWidth;
        int childHeight;
        for (int i = 0; i < childCount; i++) {
            childAt = getChildAt(i);
            childWidth = childAt.getMeasuredWidth();
            childHeight = childAt.getMeasuredHeight();
            lp = (MarginLayoutParams) childAt.getLayoutParams();
            // 判断不同位置时所在的区域，
            childPadLeft = getChildPadLeft(menuWidth, lp, childWidth);
            childPadTop = getChildPadTop(menuHeight, lp, childHeight);
            childAt.layout(childPadLeft, childPadTop, childPadLeft + childWidth, childPadTop + childHeight);
        }
    }

    //九个位置三列
    private int getChildPadLeft(int menuWidth, MarginLayoutParams lp, int childWidth) {
        int childPadLeft = 0;

        int paddingLeft = getPaddingLeft();
        int layoutWidth = getWidth();
        switch (position) {
            case CENTER:
            case CENTER_TOP:
            case CENTER_BOTTOM:
                childPadLeft = (layoutWidth - childWidth) / 2 + lp.leftMargin + paddingLeft - getPaddingRight();
                break;
            case LEFT_TOP:
            case LEFT_BOTTOM:
            case CENTER_LEFT:
                childPadLeft = paddingLeft + lp.leftMargin + (menuWidth - childWidth) / 2;
                break;
            case RIGHT_TOP:
            case RIGHT_BOTTOM:
            case CENTER_RIGHT:
                childPadLeft = layoutWidth - childWidth - lp.rightMargin - getPaddingRight() - (menuWidth - childWidth) / 2;
                break;
        }
        return childPadLeft;
    }

    //九个位置三行
    private int getChildPadTop(int menuHeight, MarginLayoutParams lp, int childHeight) {
        int childPadTop = 0;
        int layoutHeight = getHeight();
        int paddingTop = getPaddingTop();
        switch (position) {
            case CENTER:
            case CENTER_LEFT:
            case CENTER_RIGHT:
                childPadTop = (layoutHeight - childHeight) / 2 + lp.topMargin + paddingTop - getPaddingBottom();
                break;
            case LEFT_TOP:
            case RIGHT_TOP:
            case CENTER_TOP:
                childPadTop = paddingTop + lp.topMargin + (menuHeight - childHeight) / 2;
                break;
            case LEFT_BOTTOM:
            case CENTER_BOTTOM:
            case RIGHT_BOTTOM:
                childPadTop = layoutHeight - childHeight - lp.bottomMargin - getPaddingBottom() - (menuHeight - childHeight) / 2;
                break;
        }
        return childPadTop;
    }

    /**
     * 中心按钮点击事件
     *
     * @param childCount
     */
    private void setMenuListener(int childCount) {
        getChildAt(childCount - 1).setOnClickListener(this);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        //return new MarginLayoutParams(getContext(), attrs);//继承自viewgroup,蛋疼的三星
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    public void onClick(View v) {
        //监测是否正在旋转
        if (isNowRotating) return;
        //true单次依次执行,则isNowRotating也为true；false点击就执行，isNowRotating为false
        if (isRotate) isNowRotating = true;
        // 菜单状态切换
        if (null == menuState) {

        } else if (isOpen())
            menuState.closeMenu();
        else
            menuState.openMenu();

        // 主菜单是否旋转
        if (rotaFlag)
            rotationMenu(v, 0f, 720f, 1000);
        // 子view弹出与收回
        toggleMenu(TIME);
    }

    /**
     * 子view的弹出与收回
     *
     * @param Duration
     */
    public void toggleMenu(int Duration) {
        int childCount = getChildCount();

        ObjectAnimator animator01;
        ObjectAnimator animator02;
        //旋转动画定义
        ObjectAnimator rotation;
        AnimatorSet set;
        // 弹出后子view的xy位置，去除最后一个子view
        float ix = 0;
        float iy = 0;
        View childAt;

        for (int i = 0; i < childCount - 1; i++) {
            childAt = getChildAt(i);
            //开始计算每个view的弹出位置
            switch (position) {
                case LEFT_TOP://直角时的xy值，默认的位置为LEFT_TOP；默认cong从3点位置开始顺时针旋转;sin与cos互换则从六点位置开始逆时针旋转
                case LEFT_BOTTOM://与LEFT_TOP的Y轴相反，颠倒y轴方向即可；Y*-1
                case RIGHT_TOP://与LEFT_TOP的X轴相反，颠倒X轴方向即可；X*-1
                case RIGHT_BOTTOM://与LEFT_TOP的XY轴都相反，颠倒X轴方向即可；X*-1,Y*-1
                    ix = getCos(2, childCount - 2, i) * tempX;
                    iy = getSin(2, childCount - 2, i) * tempY;
                    break;
                case CENTER://位于中间时的xy值，与LEFT_TOP一样，只是大了四倍而已,同时第一个与最后一个view连接，多了一个角度
                    ix = getCos((float) 1 / 2, childCount - 1, i);
                    iy = getSin((float) 1 / 2, childCount - 1, i);
                    break;
                case CENTER_TOP://平角时的xy值；默认为CENTER_TOP；CENTER_BOTTOM,CENTER_LEFT,CENTER_RIGHT
                case CENTER_BOTTOM://与LEFT_BOTTOM一样，只是大了两倍而已
                    ix = getCos(1, childCount - 2, i);
                    iy = getSin(1, childCount - 2, i) * tempY;
                    break;
                case CENTER_LEFT://弹出位置改变，sin与cos互换
                    ix = getSin(1, childCount - 2, i);
                    iy = getCos(1, childCount - 2, i);
                    break;
                case CENTER_RIGHT://与CENTER_LEFT的X相反，X*-1
                    ix = getSin(1, childCount - 2, i) * tempX;
                    iy = getCos(1, childCount - 2, i);
                    break;
            }
            // 根据菜单的状态判断是弹出还是收回，平移的动画定义
            if (mState == State.CLOSE) {
                animator01 = ObjectAnimator.ofFloat(childAt, "translationX", 0F, ix);
                animator02 = ObjectAnimator.ofFloat(childAt, "translationY", 0F, iy);
            } else {
                animator01 = ObjectAnimator.ofFloat(childAt, "translationX", ix, 0F);
                animator02 = ObjectAnimator.ofFloat(childAt, "translationY", iy, 0F);
            }
            //旋转动画定义
            rotation = ObjectAnimator.ofFloat(childAt, "rotation", 0f, 360f);
            //把几个动画组合在一起
            set = new AnimatorSet();
            set.setInterpolator(new BounceInterpolator());//差值器
            set.playTogether(animator01, animator02, rotation);
            set.setDuration(Duration);
            set.setStartDelay((i + 1) * 100);// 每个的子view的延迟时间
            //子view的点击事件
            final int pos = i;
            childAt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (menuItemClick != null)
                        menuItemClick.onMenuItemClick(v, pos);
                    // 子view点击之后的效果
                    childAnimator(pos);
                    // 点击之后收回子view
                    if (recoverChildView)
                        toggleMenu(300);
                }
            });
            // 执行动画
            set.start();
            //监听最后一个view的动画是否停止，
            if (isNowRotating && i == childCount - 2) {
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        isNowRotating = false;
                    }
                });
            }
        }
        // 执行完成后切换菜单状态
        changeState();
    }

    private float getCos(float num, int count, int num3) {
        return (float) (radius * Math.cos(Math.PI / num / count * num3));
    }

    private float getSin(float num, int count, int num3) {
        return (float) (radius * Math.sin(Math.PI / num / count * num3));
    }

    /**
     * 子view的动画效果
     *
     * @param pos
     */
    protected void childAnimator(int pos) {
        ObjectAnimator scaleX;
        View childAt;
        // 透明化效果
        ObjectAnimator alpha;
        AnimatorSet set;
        for (int i = 0; i < getChildCount() - 1; i++) {
            childAt = getChildAt(i);
            if (i == pos) {// 让被选中的childat变大，其他变小
                scaleX = ObjectAnimator.ofFloat(childAt, "scaleX", 1f, 2f, 1f);
            } else {
                scaleX = ObjectAnimator.ofFloat(childAt, "scaleX", 1f, 0f, 1f);
            }
            // 透明化效果
            alpha = ObjectAnimator.ofFloat(childAt, "alpha", 1f, 0f, 1f);
            set = new AnimatorSet();
            set.playTogether(alpha, scaleX);
//            set.setDuration(300 * (i + 1));
            set.setDuration(1000);
            set.start();
        }
    }

    /**
     * 切换菜单状态
     */
    public void changeState() {
        mState = (mState == State.OPEN ? State.CLOSE : State.OPEN);
    }

    /**
     * 主菜单旋转效果
     *
     * @param v 主菜单
     * @param f 起始值
     * @param g 结束时
     * @param i 时间
     */
    public void rotationMenu(View v, float f, float g, int i) {
        ObjectAnimator.ofFloat(v, "rotation", f, g).setDuration(i).start();
    }

    /**
     * 判断是打开还是关闭状态
     *
     * @return
     */
    public boolean isOpen() {
        return mState == State.OPEN;
    }
}