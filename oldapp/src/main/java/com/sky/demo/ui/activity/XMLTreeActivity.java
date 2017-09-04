package com.sky.demo.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sky.demo.R;
import com.sky.demo.ui.BaseActivity;
import com.sky.demo.ui.adapter.TreeAdapter;
import com.sky.demo.ui.tree.FileBean;
import com.sky.demo.ui.widget.pending.MyRecyclerView;
import com.sky.demo.utils.pending.XmlParser;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * @author sky QQ:1136096189
 * @Description:
 * @date 15/11/28 下午2:00
 */
@ContentView(R.layout.activity_tree)
public class XMLTreeActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    @ViewInject(R.id.recycle)
    private MyRecyclerView recyclerView;
    @ViewInject(R.id.swipe)
    private SwipeRefreshLayout swipe;

    private LinearLayoutManager mLayoutManager;
    private TreeAdapter adapter;

    private int lastVisibleItem;

    List<FileBean> fileBeans = new ArrayList<>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Toast.makeText(XMLTreeActivity.this, "DOWN", Toast.LENGTH_SHORT).show();
                    swipe.setRefreshing(false);

                    break;
                case 1:
                    Snackbar.make(recyclerView, "Snackbar comes out",
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(
                                            XMLTreeActivity.this,
                                            "Toast comes out",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                    Toast.makeText(XMLTreeActivity.this, "UP", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle();
        swipe.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources()
                        .getDisplayMetrics()));
        swipe.setColorSchemeColors(R.color.red, R.color.white, R.color.black, R.color.white, R.color.black);
        swipe.setOnRefreshListener(this);

        recyclerView.setHasFixedSize(true);
        adapter = new TreeAdapter(R.layout.adapter_tree_structure, R.layout.recycle_footerview);
        recyclerView.setAdapter(adapter);
        mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));

        adapter.setOnItemClickListener(new TreeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                showToast("onItemClick=" + position);
                adapter.toggle(position);
            }
        });
        adapter.setOnItemLongClickListener(new TreeAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, final int position) {
                final EditText ed = new EditText(XMLTreeActivity.this);
                new AlertDialog.Builder(XMLTreeActivity.this, 5)
                        .setTitle("地址")
                        .setView(ed)
                        .setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String text = ed.getText().toString().trim();
                                if (!TextUtils.isEmpty(text))
                                    adapter.addNode(text, position);
                            }
                        }).setNeutralButton(getResources().getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
//                showToast("onItemLongClick=" + position);
                return true;
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    handler.sendEmptyMessageDelayed(1, 000);
                }
//                if (newState == RecyclerView.SCROLL_STATE_IDLE
//                        && lastVisibleItem + 1 == adapter.getItemCount()) {
//                    swipe.setRefreshing(true);
//                    // 此处在现实项目中，请换成网络请求数据代码，sendRequest .....
//                    handler.sendEmptyMessageDelayed(0, 3000);
//                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    setData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 100);

    }

    private void setData() throws IOException {
        InputStream Provinces = XMLTreeActivity.this.getResources().getAssets().open("citycoding/Provinces.xml");
        List<FileBean> fileProvinces = new XmlParser().xmlPullParser(Provinces);
        InputStream Cities = getResources().getAssets().open("citycoding/Cities.xml");
        List<FileBean> fileCities = new XmlParser().xmlPullParser(Cities);
        InputStream Districts = getResources().getAssets().open("citycoding/Districts.xml");
        List<FileBean> fileDistricts = new XmlParser().xmlPullParser(Districts);
        Provinces.close();
        Cities.close();
        Districts.close();

        fileBeans.addAll(fileProvinces);
        fileBeans.addAll(fileCities);
        fileBeans.addAll(fileDistricts);
        adapter.setAllNodes(fileBeans);
        //adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(0, 3000);
    }

}
