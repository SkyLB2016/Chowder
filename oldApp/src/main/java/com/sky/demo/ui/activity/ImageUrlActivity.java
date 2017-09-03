package com.sky.demo.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.sky.demo.R;
import com.sky.demo.api.IDataResultImpl;
import com.sky.demo.model.CourseEntity;
import com.sky.demo.ui.BaseActivity;
import com.sky.demo.ui.adapter.LoaderURLAdapter;
import com.sky.demo.utils.http.HttpDataUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sky QQ:1136096189
 * @Description: 加载网络图片
 * @date 15/11/28 下午12:38
 */
@ContentView(R.layout.navigation_content)
public class ImageUrlActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @ViewInject(R.id.swipe)
    private SwipeRefreshLayout swipe;
    @ViewInject(R.id.recycle)
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private LoaderURLAdapter adapter;

    private int firstVisibleItem;
    private int lastVisibleItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle();
        FloatingActionButton fab = getView(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "正在加载，请稍后", Snackbar.LENGTH_LONG)
                        .setAction("cancel", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showToast("已取消");
                            }
                        }).show();
            }
        });
        //刷新
        toRefresh();
    }

    boolean first;

    /**
     * 页面刷新
     */
    private void toRefresh() {
        first = true;
        //设置swipe的开始位置与结束位置
        swipe.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources()
                        .getDisplayMetrics()));
        //为进度圈设置颜色
        swipe.setColorSchemeColors(R.color.red, R.color.white, R.color.black);
        swipe.setOnRefreshListener(this);//监听

        recyclerView.setHasFixedSize(true);
        adapter = new LoaderURLAdapter(R.layout.adapter_main);
        recyclerView.setAdapter(adapter);


        mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        adapter.setOnItemClickListener(new LoaderURLAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    adapter.setImageLoader(firstVisibleItem, lastVisibleItem, recyclerView);

                    if (lastVisibleItem + 1 == adapter.getItemCount()) {
                        handler.sendEmptyMessageDelayed(1, 000);
                    }
                } else {
                    adapter.cancelAlltasks();

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //  dx：大于0，向右滚动    小于0，向左滚动
                //  dy：大于0，向上滚动    小于0，向下滚动
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

                if (lastVisibleItem > 0 && first) {
                    first = false;
                    adapter.setImageLoader(firstVisibleItem, lastVisibleItem, recyclerView);
                }
            }
        });
        HttpDataUtils.getMuke(new IDataResultImpl<List<CourseEntity>>() {
            @Override
            public void onSuccessData(List<CourseEntity> data) {
                ArrayList<CourseEntity> list = (ArrayList<CourseEntity>) data;
                list.trimToSize();
                adapter.setDatas(data);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.cancelAlltasks();
    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(0, 3000);
    }

    @Override
    protected void handler(Message msg) {
        super.handler(msg);
        switch (msg.what) {
            case 0:
                adapter.setDatas(null);
                handler.sendEmptyMessageDelayed(2, 3000);

                break;
            case 1:
                Snackbar.make(recyclerView, "正在加载，请稍后",
                        Snackbar.LENGTH_LONG)
                        .setAction("Cancel", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showToast("已取消");
                            }
                        }).show();
                break;
            case 2:
                swipe.setRefreshing(false);
                HttpDataUtils httpUtils = new HttpDataUtils();
                httpUtils.getMuke(new IDataResultImpl<List<CourseEntity>>() {
                    @Override
                    public void onSuccessData(List<CourseEntity> data) {
                        adapter.setDatas(data);
                    }
                });
                break;
        }
    }
}
