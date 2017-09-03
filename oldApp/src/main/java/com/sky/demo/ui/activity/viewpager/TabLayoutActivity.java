package com.sky.demo.ui.activity.viewpager;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.sky.demo.R;
import com.sky.demo.ui.BaseActivity;
import com.sky.demo.ui.fragment.One;
import com.sky.demo.ui.fragment.RecycleFragment;
import com.sky.demo.ui.fragment.Three;
import com.sky.demo.ui.fragment.Two;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 彬 QQ 1136096189
 * @Description: TODO
 * @date 2015/8/31 14:25
 */
@ContentView(R.layout.activity_tab_vp)
public class TabLayoutActivity extends BaseActivity {
    @ViewInject(R.id.id_viewpager)
    private ViewPager mViewPager;
    @ViewInject(R.id.tabs)
    private TabLayout mTabLayout;
    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;
    @ViewInject(R.id.appbar)
    private AppBarLayout appbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle();
        setUpViewPager();
        AppBarLayout.LayoutParams mParams = (AppBarLayout.LayoutParams) appbar.getChildAt(0).getLayoutParams();
        mParams.setScrollFlags(0);//的时候AppBarLayout下的toolbar就不会随着滚动条折叠
//        mParams.setScrollFlags(5); //的时候AppBarLayout下的toolbar会随着滚动条折叠

    }

    private void setUpViewPager() {
        mTabLayout.setVisibility(View.VISIBLE);
        final List<String> titles = new ArrayList<>();
        titles.add("card");
        titles.add("MeshView");
        titles.add("倒影");
        titles.add("shape");
        for (int i = 0; i < titles.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }
        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(new RecycleFragment());
        fragments.add(new One());
        fragments.add(new Two());
        fragments.add(new Three());
        FragmentPagerAdapter adapter =
                new FragmentPagerAdapter(getSupportFragmentManager()) {
                    @Override
                    public int getCount() {
                        return fragments.size();
                    }

                    @Override
                    public Fragment getItem(int position) {
                        return fragments.get(position);
                    }

                    @Override
                    public CharSequence getPageTitle(int position) {
                        return titles.get(position);
                    }
                };
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(adapter);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showToast(tab.getText().toString());
//                tab.select();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
//        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }
}