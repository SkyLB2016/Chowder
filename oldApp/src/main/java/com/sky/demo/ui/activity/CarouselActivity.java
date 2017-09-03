package com.sky.demo.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sky.demo.R;
import com.sky.demo.common.Constants;
import com.sky.demo.ui.BaseActivity;
import com.sky.demo.utils.ScreenUtils;

public class CarouselActivity extends BaseActivity {
    private ViewPager viewPager;
    private LinearLayout group_point;
    private int[] images = {R.mipmap.introductory_01, R.mipmap.introductory_02, R.mipmap.introductory_03, R.mipmap.introductory_04};
    private ImageView[] imageViews = new ImageView[images.length];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);
        setTitle();
        setViewPager();
    }

    boolean flag = false;

    private void setViewPager() {
        viewPager = (ViewPager) findViewById(R.id.id_viewpager);
        ViewGroup.LayoutParams viewPagerLayoutParamsp = viewPager.getLayoutParams();
        viewPagerLayoutParamsp.height = ScreenUtils.getWidthAndHeight(this)[1] * 3 / 5;
        viewPager.setLayoutParams(viewPagerLayoutParamsp);
        group_point = (LinearLayout) findViewById(R.id.id_Group_point);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return Integer.MAX_VALUE;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = new ImageView(CarouselActivity.this);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setImageDrawable(getResources().getDrawable(images[position % 4]));
//                imageView.setImageBitmap(ImageUtils.getBitmapFromRes(PullActivity.this, images[position]));
                container.addView(imageView);
                imageViews[position%images.length] = imageView;
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
//                container.removeViewAt(position);//会造成加载不及时
                container.removeView(imageViews[position%images.length]);
            }
        });
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        flag = true;
                        break;
                }
                return false;
            }
        });
        final View[] points = new View[images.length];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(25, 25);
        lp.setMargins(0, 0, 15, 0);
        for (int i = 0; i < images.length; i++) {
            points[i] = new View(this);
            if (i == 0) points[i].setBackgroundResource(R.drawable.shape_selected_oval);
            else points[i].setBackgroundResource(R.drawable.shape_unselected_oval);
            points[i].setLayoutParams(lp);
            group_point.addView(points[i]);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < points.length; i++) {
                    if (i == position % 4) points[i].setBackgroundResource(R.drawable.shape_selected_oval);
                    else points[i].setBackgroundResource(R.drawable.shape_unselected_oval);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //state=1时正在滑动，2滑动完毕，0什么都没做
//                if (flag && state == 1) {
//                    handler.removeMessages(Constants.handler_pull);
//                } else if (flag && state == 0) {
//                    handler.sendEmptyMessageDelayed(Constants.handler_pull, 5000);
//                    flag=true;
//                }
            }
        });
        viewPager.setCurrentItem(2 * images.length);
        handler.sendEmptyMessageDelayed(Constants.handler_pull, 5000);
    }

    @Override
    protected void handler(Message msg) {
        super.handler(msg);
        if (msg.what == Constants.handler_pull) {
            int position = viewPager.getCurrentItem() + 1;
            if (position%images.length == imageViews.length) position = 0;
            viewPager.setCurrentItem(position);
            handler.sendEmptyMessageDelayed(Constants.handler_pull, 5000);
        }
    }
//    private ObjectAnimator animator;
//    public void open(View view) {
//        open();
//    }
//
//    public void close(View view) {
//        close();
//    }
//
//    private void setSky(int t) {
//        LogUtils.i("tttttt=" + t);
//    }
//
//    public void close() {
//        if (animator != null && animator.isRunning()) {
//            return;
//        }
//        animator = ObjectAnimator.ofInt(this, "sky", 100, 0, 100, 0, 100, 0);
//        animator.setInterpolator(new DecelerateInterpolator());
//        animator.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//            }
//        });
//        animator.setDuration(2500);
//        animator.start();
//    }
//
//    public void open() {
//        if (animator != null && animator.isRunning()) {
//            return;
//        }
//        animator = ObjectAnimator.ofInt(this, "sky", 0, 20000, 10000);
//        animator.setInterpolator(new DecelerateInterpolator());
//        animator.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//            }
//        });
//        animator.setDuration(4000);
//        animator.start();
//    }
}
