package com.sky.demo.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sky.demo.R;
import com.sky.demo.model.ActivityModel;
import com.sky.demo.ui.BaseActivity;
import com.sky.demo.ui.BaseTitle;
import com.sky.demo.ui.activity.custom.PullDownActivity;
import com.sky.demo.ui.adapter.MainAdapter;
import com.sky.demo.ui.widget.pending.MyRecyclerView;
import com.sky.demo.utils.NotficationManager;
import com.sky.utils.JumpAct;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by sky on 2015年3月26日 下午4:01:00.
 * DrawerLayout的应用
 */
@ContentView(R.layout.main)
public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @ViewInject(R.id.dl_mian_drawer)//主布局
    private DrawerLayout mDrawerLayout;
    @ViewInject(R.id.nv_main_navigation)//侧滑栏
    private NavigationView mNavigationView;

    @ViewInject(R.id.swipe)
    private SwipeRefreshLayout swipe;//下拉刷新的框架
    @ViewInject(R.id.recycle)
    private MyRecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private MainAdapter adapter;

    private int lastVisibleItem;//最后一个item
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        MediaPlayer player01 = MediaPlayer.create(this, R.raw.eye);
//        player01.start();
//        setTitle(R.mipmap.ic_menu);
        BaseTitle title = new BaseTitle(this);
        title.setToolbar();
        title.setLeftButton(R.mipmap.ic_menu);
//        title.getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDrawerLayout.openDrawer(GravityCompat.START);
//
//            }
//        });
        title.setLeftOnClick(new BaseTitle.LeftOnClick() {
            @Override
            public void LeftOnClick() {
                mDrawerLayout.openDrawer(GravityCompat.START);

            }
        });

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
        final FloatingActionButton fab = getView(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("");
                setSnack(fab);
                satartMaterial();
            }
        });
        //刷新
        toRefresh();
        getMemory();
    }

    private void getMemory() {
        Runtime rt = Runtime.getRuntime();
        long maxMemory = rt.maxMemory();
        showToast("maxMemory:" + Long.toString(maxMemory / (1024 * 1024)));
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

    /**
     * 页面刷新
     */
    private void toRefresh() {
        //设置swipe的开始位置与结束位置
        swipe.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                80, getResources().getDisplayMetrics()));
        swipe.setColorSchemeResources(R.color.red, R.color.blue, R.color.black);//为进度圈设置颜色
        swipe.setOnRefreshListener(this);//监听

        recyclerView.setHasFixedSize(true);
        adapter = new MainAdapter(R.layout.adapter_main);
        recyclerView.setAdapter(adapter);

        adapter.setDatas(getData());
        mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        adapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                JumpAct.jumpActivity(MainActivity.this,
                        adapter.getDatas().get(position).getComponentName());
            }
        });
        adapter.setOnItemLongClickListener(new MainAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                showToast("长按监听已处理");
                return true;
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    handler.sendEmptyMessageDelayed(1, 000);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //  dx：大于0，向右滚动    小于0，向左滚动
                //  dy：大于0，向上滚动    小于0，向下滚动
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }, 100);
    }

    /**
     * 从manifest中获取activity的信息
     *
     * @return
     */
    protected List<ActivityModel> getData() {
        List<ActivityModel> activityInfos = new ArrayList<>();

        //Intent mainIntent = new Intent(Intent.ACTION_MAIN);//获取action为ACTION_MAIN的activity
        //mainIntent.addCategory(Intent.CATEGORY_SAMPLE_CODE);//筛选category为sample code的act
        //mainIntent.setPackage(getPackageName());//只选出自己应用的act
        Intent mainIntent = new Intent("com.sky.action");//自定义的action
        PackageManager pm = getPackageManager();
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent, 0);

        if (null == resolveInfos)
            return activityInfos;

        int len = resolveInfos.size();
        for (int i = 0; i < len; i++) {
            ResolveInfo info = resolveInfos.get(i);
            //获取label,activity中未设置的话返回程序名称
            CharSequence labelSeq = info.loadLabel(pm);
            String label = labelSeq != null ? labelSeq.toString() : info.activityInfo.name;
            //获取说明
            int descriptionRes = info.activityInfo.descriptionRes;
            String describe = descriptionRes == 0 ? "未添加" : getResources().getString(descriptionRes);
            //获取icon  Drawable icon = info.loadIcon(pm);
            int iconRes = info.activityInfo.icon;
            int icon = iconRes == 0 ? R.mipmap.ic_banner : iconRes;
            ActivityModel activityModel = new ActivityModel(label, describe, icon, info.activityInfo.name);
            activityInfos.add(activityModel);
        }
        //排序
        Collections.sort(activityInfos, sDisplayNameComparator);
//        Collections.sort(activityInfos);//使用activityModel中的compareTo进行排序
        return activityInfos;
    }

    /**
     * 为筛选出的act进行排序
     */
    private final static Comparator<ActivityModel> sDisplayNameComparator =
            new Comparator<ActivityModel>() {
                private final Collator collator = Collator.getInstance();

                @Override
                public int compare(ActivityModel lhs, ActivityModel rhs) {
                    return collator.compare(lhs.getClassName(), rhs.getClassName());
                }
            };

//    @Override
//    public void leftOnClick() {
//        mDrawerLayout.openDrawer(GravityCompat.START);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //menu.add(Menu.NONE,Menu.FIRST+1,1000,"dd").setIcon(R.mipmap.back).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_architecture://跳转到指定的别的APP的activity
                Intent architecture = new Intent();
                architecture.setClassName("com.cx.architecture",
                        "com.cx.architecture.ui.activity.LoginActivity");
                try {
                    startActivity(architecture);
                } catch (ActivityNotFoundException e) {
                    showToast("程序未安装");
                }
                break;
            case R.id.action_materialdesigndemo:
                Intent materialdesigndemo = new Intent();
                materialdesigndemo.setClassName("com.gc.materialdesigndemo",
                        "com.gc.materialdesigndemo.ui.MainActivity");
                try {
                    startActivity(materialdesigndemo);
                } catch (ActivityNotFoundException e) {
                    showToast("materialdesigndemo未安装");
                }
                break;
        }
        return false;
    }

    @Override
    public void onRefresh() {
        //swipe刷新
        handler.sendEmptyMessageDelayed(0, 3000);
    }

    @Override
    protected void handler(Message msg) {
        super.handler(msg);
        switch (msg.what) {
            case 0:
                swipe.setRefreshing(false);
                break;
            case 1:
                setSnack(recyclerView);
                break;

        }
    }

    //设置SnackBar
    public void setSnack(final View view) {
        Snackbar.make(view, "正在加载，请稍后", Snackbar.LENGTH_SHORT)
                .setAction("cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showToast("已取消");
                        NotficationManager.PushNotification(view.getContext(), R.mipmap.logo, "sky", "你中奖了",
                                PullDownActivity.class, "骗你的");
                    }
                }).show();
    }


    private long lastBack;

    @Override
    public void onBackPressed() {
        //监测mNavigationView侧滑栏是否展开
        if (mNavigationView.isShown()) {
            mDrawerLayout.closeDrawers();
//            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        long nowCurrent = System.currentTimeMillis();
        if (nowCurrent - lastBack > 3000) {
            showToast(getResources().getString(R.string.toast_exit));
            lastBack = nowCurrent;
        } else {
            super.onBackPressed();
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

    @Override
    public void initView() {

    }
}
