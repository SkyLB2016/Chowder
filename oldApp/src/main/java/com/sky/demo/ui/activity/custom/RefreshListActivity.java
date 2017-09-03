package com.sky.demo.ui.activity.custom;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.sky.demo.R;
import com.sky.demo.ui.BaseActivity;
import com.sky.demo.ui.dialog.Pop;
import com.sky.demo.ui.widget.listview.RefreshListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class RefreshListActivity extends BaseActivity implements RefreshListView.OnRefreshListener {

    private RefreshListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> items = new ArrayList<String>();
    private Handler mHandler;
    private int mIndex = 0;
    private int mRefreshIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
        setTitle();
        geneItems();
        initView();
    }

    public void initView() {
        mHandler = new Handler();

        mListView = (RefreshListView) findViewById(R.id.list_view);
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setAutoLoadEnable(true);
        mListView.setRefreshListener(this);
        mListView.setRefreshTime(getTime());

        mAdapter = new ArrayAdapter<String>(this, R.layout.list_item, items);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                createPOP(new String[]{"18322301875", "18322301596"});
                if (!pop.isShowing())
                    pop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
            }
        });
    }

    private void geneItems() {
        for (int i = 0; i != 6; ++i) {
            items.add("Test XListView item " + (++mIndex));
        }
    }

    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

//		if (hasFocus) {
//			mListView.autoRefresh();
//		}
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mIndex = ++mRefreshIndex;
                items.clear();
                geneItems();
                mAdapter = new ArrayAdapter<String>(RefreshListActivity.this,
                        R.layout.list_item, items);
                mListView.setAdapter(mAdapter);
                onLoad();
            }
        }, 2500);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                geneItems();
                mAdapter.notifyDataSetChanged();
                onLoad();
            }
        }, 2500);
    }


    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime(getTime());
    }

    private Pop pop;

    private void createPOP(final String[] phone) {
        if (pop == null)
            pop = new Pop(LayoutInflater.from(this).inflate(R.layout.pop_phone, null));
        pop.setDatas(Arrays.asList(phone));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
