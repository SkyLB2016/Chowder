package com.sky.chowder.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.sky.ErrorMes;
import com.sky.api.OnRequestCallback;
import com.sky.base.BaseNoPActivity;
import com.sky.chowder.R;
import com.sky.chowder.model.CourseEntity;
import com.sky.chowder.ui.adapter.LoaderURLAdapter;
import com.sky.chowder.utils.UseCase;
import com.sky.chowder.utils.http.HttpUtils;
import com.sky.model.ApiResponse;
import com.sky.utils.LogUtils;

import butterknife.BindView;
import io.reactivex.Observable;

/**
 * Created by SKY on 2015/11/28.
 * 加载网络图片
 */
public class ImageUrlActivity extends BaseNoPActivity {

    @BindView(R.id.swipe)
    SwipeRefreshLayout swipe;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private LinearLayoutManager mLayoutManager;
    private LoaderURLAdapter adapter;

    private int firstVisibleItem;
    private int lastVisibleItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    protected int getLayoutResId() {
        return R.layout.navigation_content;
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
        swipe.setColorSchemeResources(R.color.red, R.color.white, R.color.black);
//        swipe.setOnRefreshListener(this);//监听

        recycler.setHasFixedSize(true);
        adapter = new LoaderURLAdapter(R.layout.adapter_main);
        recycler.setAdapter(adapter);


        mLayoutManager = (LinearLayoutManager) recycler.getLayoutManager();
        adapter.setOnItemClickListener(new LoaderURLAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }
        });
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    adapter.setImageLoader(firstVisibleItem, lastVisibleItem, recyclerView);

                    if (lastVisibleItem + 1 == adapter.getItemCount()) {
//                        handler.sendEmptyMessageDelayed(1, 000);
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
        new UseCase<ApiResponse<CourseEntity>>() {
            @Override
            protected Observable<ApiResponse<CourseEntity>> buildObservable() {
                return HttpUtils.Companion.getInstance().getMuke();
            }
        }.subscribe(new OnRequestCallback<ApiResponse<CourseEntity>>() {
            @Override
            public void onFail(ErrorMes error) {
                LogUtils.i(error.getMessage());
            }

            @Override
            public void onSuccess(ApiResponse<CourseEntity> data) {
                adapter.setDatas(data.getData());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.cancelAlltasks();
    }
}
