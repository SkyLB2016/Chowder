package com.sky.base;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

import com.sky.R;
import com.sky.adapter.RecyclerAdapter;
import com.sky.api.IRefreshV;
import com.sky.widget.MyRecyclerView;

import java.util.List;

/**
 * Created by SKY on 2017/6/15.
 */
public abstract class RecyclerPActivity<T, P extends RefreshP> extends BasePActivity<P> implements IRefreshV<T>, SwipeRefreshLayout.OnRefreshListener {
    MyRecyclerView recycler;
    SwipeRefreshLayout swipe;
    protected RecyclerAdapter<T> adapter;

//    protected int firstVisibleItem;
//    protected int lastVisibleItem;

    private boolean firstTail = false;//第一次到达底部

    @Override
    protected void initialize() {
        recycler = getView(R.id.recycler);
        swipe = getView(R.id.swipe);
        //设置swipe的开始位置与结束位置
        swipe.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics()));
        //为进度圈设置颜色
        swipe.setColorSchemeResources(android.R.color.black, android.R.color.holo_green_dark, android.R.color.white);
        swipe.setOnRefreshListener(this);//监听
        recycler.setHasFixedSize(true);

        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                onRecyclerScrollStateChanged(recyclerView, newState);
                //SCROLL_STATE_SETTLING惯性，SCROLL_STATE_DRAGGING拖拽，SCROLL_STATE_IDLE停止、
                if (recyclerView.canScrollVertically(1)) firstTail = false;

                if (newState == RecyclerView.SCROLL_STATE_IDLE && firstTail) loadMore();

                if (!recyclerView.canScrollVertically(1)) firstTail = true;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                onRecyclerScrolled(recyclerView, dx, dy);
                //  dx：大于0，向右滚动    小于0，向左滚动
                //  dy：小于0，向上滚动    大于0，向下滚动
                if (dy < -1) firstTail = false;
//                if (recycler.getLayoutManager() instanceof LinearLayoutManager) {
//                    LinearLayoutManager layoutManager = (LinearLayoutManager) recycler.getLayoutManager();
////                    firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
////                    lastVisibleItem = layoutManager.findLastVisibleItemPosition();
//                } else if (recycler.getLayoutManager() instanceof StaggeredGridLayoutManager) {
//                    StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recycler.getLayoutManager();
//                    int[] positions = layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]);
//                    lastVisibleItem = findMax(positions);
//                }
            }
        });
        creatAdapter();
        recycler.setAdapter(adapter);
    }

//    private int findMax(int[] positions) {
//        int position = 0;
//        for (int posi : positions) {
//            if (posi > position) position = posi;
//        }
//        return position;
//    }

    protected abstract void creatAdapter();

    public void onRecyclerScrollStateChanged(RecyclerView recyclerView, int newState) {
    }

    public void onRecyclerScrolled(RecyclerView recyclerView, int dx, int dy) {
    }

    public void setRefreshing(boolean isrefresh) {
        if (swipe == null) return;
        swipe.setRefreshing(isrefresh);
    }

    public void setSwipeEnable(boolean enable) {
        swipe.setEnabled(enable);
    }

    @Override
    public void setAdapterList(List<T> entities) {
        adapter.setDatas(entities);
    }

    @Override
    public void addAdapterList(List<T> entities) {
        adapter.addDatas(entities);
    }

    @Override
    public void addHintView() {

    }

    @Override
    public void removeHintView() {

    }

    public void setRecyclerLayout(RecyclerView.LayoutManager manager) {
        recycler.setLayoutManager(manager);
    }

    @Override
    public void onRefresh() {
        presenter.onRefresh();
    }

    public void loadMore() {
        if (adapter.getDatas() == null || adapter.getDatas().isEmpty()) return;
        presenter.onLoadMore(adapter.getDatas().size());
    }
}