package com.sky.demo.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sky
 * @ClassName: FlowLayout
 * @Description: TODO  流式布局
 * @date 2015年4月2日 下午8:43:43
 */
public class FlowLayout extends FrameLayout {

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int layoutWidth = MeasureSpec.getSize(widthMeasureSpec);//match_parent是的宽
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);//获取测量模式，match与wrap
        int layoutHeight = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        // 模式为wrap_content时，测量框架的宽高
        int width = 0;// 框架款起始值
        int height = 0;// 框架高起始值
        // 记录每一行的高度和宽度
        int lineWidth = 0;
        int lineHeight = 0;

        int childCount = getChildCount();// 获取框架内的子控件
        for (int i = 0; i < childCount; i++) {

            View view = getChildAt(i);// 获取子view
            measureChild(view, widthMeasureSpec, widthMeasureSpec);// 测量子view

            MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
            int childWidth = view.getMeasuredWidth() + lp.leftMargin
                    + lp.rightMargin;
            int childHeight = view.getMeasuredHeight() + lp.topMargin
                    + lp.bottomMargin;

            if (lineWidth + childWidth > layoutWidth - getPaddingLeft()
                    - getPaddingRight()) {
                width = Math.max(width, lineWidth);
                lineWidth = childWidth;

                height += lineHeight;
                lineHeight = childHeight;
            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }
            // 判断最后一个，获取最终宽高
            if (i == childCount - 1) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }
        }
        // 为框架父控件写入宽高
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? layoutWidth
                        : width + getPaddingLeft() + getPaddingRight(),
                heightMode == MeasureSpec.EXACTLY ? layoutHeight : height
                        + getPaddingTop() + getPaddingBottom());
    }

    // 所有控件，分行排列
    private List<List<View>> allViews = new ArrayList<List<View>>();
    // 记录行高
    private List<Integer> mLineHeight = new ArrayList<Integer>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 先清空
        allViews.clear();
        mLineHeight.clear();

        int lineWidth = 0;
        int lineHeight = 0;
        List<View> lineViews = new ArrayList<View>();
        int width = getWidth();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
            int childWidth = view.getMeasuredWidth() + lp.leftMargin
                    + lp.rightMargin;
            int childHeight = view.getMeasuredHeight() + lp.topMargin
                    + lp.bottomMargin;

            if (childWidth + lineWidth > width - getPaddingLeft()
                    - getPaddingRight()) {
                allViews.add(lineViews);
                mLineHeight.add(lineHeight);

                lineWidth = 0;
                lineHeight = childHeight;
                lineViews = new ArrayList<View>();
            }
            lineWidth += childWidth;
            lineHeight = Math.max(lineHeight, childHeight);
            lineViews.add(view);
        }

        allViews.add(lineViews);
        mLineHeight.add(lineHeight);
        // 框架的内边距
        int left = getPaddingLeft();
        int top = getPaddingTop();

        for (int i = 0; i < allViews.size(); i++) {
            lineViews = allViews.get(i);
            lineHeight = mLineHeight.get(i);
            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) child
                        .getLayoutParams();

                int leftChild = left + lp.leftMargin;
                int topChild = top + lp.topMargin;
                int rightChild = leftChild + child.getMeasuredWidth();
                int bottomChild = topChild + child.getMeasuredHeight();

                child.layout(leftChild, topChild, rightChild, bottomChild);

                left += lp.leftMargin + lp.rightMargin
                        + child.getMeasuredWidth();
            }
            left = getPaddingLeft();
            top += lineHeight;
        }
    }

    /**
     * 与当前ViewGroup对应的LayoutParams
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
        //return new MarginLayoutParams(getContext(), attrs);//继承自viewgroup
    }

}
