package com.sky.chowder.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;

import com.sky.base.BaseNoPActivity;
import com.sky.chowder.R;
import com.sky.chowder.ui.adapter.MainAdapter;
import com.sky.widget.MyRecyclerView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by sky on 2015年3月26日 下午4:01:00.
 * DrawerLayout的应用
 */
public class DrawerActivity extends BaseNoPActivity {

    @BindView(R.id.dl_mian_drawer)//主布局
            DrawerLayout mDrawerLayout;
    @BindView(R.id.nv_main_navigation)//侧滑栏
            NavigationView mNavigationView;

    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;//下拉刷新的框架
    @BindView(R.id.recycler)
    MyRecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private MainAdapter adapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_drawer;
    }

    @Override
    protected void initialize() {
        super.initialize();
        baseTitle.setLeftButton(R.mipmap.ic_menu);

        //侧划栏中的点击事件
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                SetIntent();
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @OnClick({R.id.tv_left})
    public void onClick(View v) {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    private void SetIntent() {
        Intent intent = new Intent();
        intent.setAction("com.sky.action");
        intent.setAction(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addCategory(Intent.CATEGORY_SAMPLE_CODE);
        startActivity(intent);
    }

    private void satartMaterial() {
        Intent materialdesigndemo = new Intent();
        materialdesigndemo.setClassName("com.gc.materialdesigndemo",
                "com.gc.materialdesigndemo.ui.MainActivity");
        materialdesigndemo.putExtra("super", "不同程序跳转成功");
        try {
            startActivity(materialdesigndemo);
        } catch (ActivityNotFoundException e) {
            showToast("materialdesigndemo未安装");
        }
    }


    private Bitmap getViewBitmap(View addViewContent) {

        addViewContent.setDrawingCacheEnabled(true);

        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0,
                addViewContent.getMeasuredWidth(),
                addViewContent.getMeasuredHeight());

        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        return bitmap;
    }
}
