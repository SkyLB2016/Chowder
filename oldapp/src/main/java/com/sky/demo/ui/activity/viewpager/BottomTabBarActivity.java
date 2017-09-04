package com.sky.demo.ui.activity.viewpager;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sky.demo.R;
import com.sky.demo.ui.BaseActivity;
import com.sky.demo.ui.widget.TabTextView;
import com.sky.demo.ui.widget.ZoomImageView;
import com.sky.demo.utils.ImageUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;


/**
 * @author 彬 QQ 1136096189
 * @Description: TODO 底部标签栏
 * @date 2015/8/31 14:25
 */
@ContentView(R.layout.activity_bottomtabbar)
public class BottomTabBarActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.id_viewpager)
    private ViewPager viewPager;
    private int[] images = new int[]{R.mipmap.ic_banner, R.mipmap.cheese_3, R.mipmap.main_menu, R.mipmap.main_menu};
    private ImageView[] imageViews = new ImageView[images.length];
    @ViewInject(R.id.id_main)
    private TabTextView first;
    @ViewInject(R.id.id_two)
    private TabTextView two;
    @ViewInject(R.id.id_three)
    private TabTextView three;
    @ViewInject(R.id.id_four)
    private TabTextView four;
    private List<TabTextView> tabTextViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle();
        //添加底部控件
        tabTextViews = new ArrayList<>();
        tabTextViews.add(first);
        tabTextViews.add(two);
        tabTextViews.add(three);
        tabTextViews.add(four);
        //底部按钮监听
        first.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);

        first.setIconAlpha(1.0f);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ZoomImageView imageView = new ZoomImageView(BottomTabBarActivity.this);
                if (position == 0) {
                    imageView.setImageBitmap(ImageUtils.createImageNegative(BitmapFactory.decodeResource(getResources(), images[position])));
                } else if (position == 1) {
                    imageView.setImageBitmap(ImageUtils.createImageOldPhoto(BitmapFactory.decodeResource(getResources(), images[position])));
                } else if (position == 2) {
                    imageView.setImageBitmap(ImageUtils.createImagePixelsrelier(BitmapFactory.decodeResource(getResources(), images[position])));
                } else if (position == 3) {
                    imageView.setImageBitmap(ImageUtils.createImageNegative(BitmapFactory.decodeResource(getResources(), images[position])));
                }
                container.addView(imageView);
                imageViews[position] = imageView;
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(imageViews[position]);
                imageViews[position]=null;
            }

            @Override
            public int getCount() {
                return imageViews.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (positionOffset > 0) {
                    tabTextViews.get(position).setIconAlpha(1 - positionOffset);
                    tabTextViews.get(position + 1).setIconAlpha(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        resetOtherTabs();
        switch (v.getId()) {
            case R.id.id_main:
                viewPager.setCurrentItem(0, false);
                first.setIconAlpha(1.0f);
                break;
            case R.id.id_two:
                viewPager.setCurrentItem(1, false);
                two.setIconAlpha(1.0f);
                break;
            case R.id.id_three:
                viewPager.setCurrentItem(2, false);
                three.setIconAlpha(1.0f);
                break;
            case R.id.id_four:
                viewPager.setCurrentItem(3, false);
                four.setIconAlpha(1.0f);
                break;
        }
    }

    //重置底部view
    private void resetOtherTabs() {
        for (int i = 0; i < tabTextViews.size(); i++) {
            tabTextViews.get(i).setIconAlpha(0f);
        }
    }
}