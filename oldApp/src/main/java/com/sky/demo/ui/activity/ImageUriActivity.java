package com.sky.demo.ui.activity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sky.demo.R;
import com.sky.demo.model.ImageFloder;
import com.sky.demo.ui.BaseActivity;
import com.sky.demo.ui.adapter.LoaderURIAdapter;
import com.sky.demo.ui.dialog.BasePop;
import com.sky.demo.ui.dialog.FloderPop;
import com.sky.demo.ui.dialog.URIPop;
import com.sky.demo.utils.SDCardUtils;
import com.sky.demo.utils.ScreenUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * @author sky QQ:1136096189
 * @Description: 加载本地图片
 * @date 15/11/28 下午12:38
 */
@ContentView(R.layout.activity_uri)
public class ImageUriActivity extends BaseActivity {

    @ViewInject(R.id.recycle)
    private RecyclerView recyclerView;
    @ViewInject(R.id.layout)
    private RelativeLayout layout;
    @ViewInject(R.id.flodername)
    private TextView flodername;
    @ViewInject(R.id.number)
    private TextView number;
    private LoaderURIAdapter adapter;
    //瀑布流布局
    private StaggeredGridLayoutManager layoutManager;
    private int firstVisibleItem;//初始可见item
    private int lastVisibleItem;//最后一个可见item
    private boolean first = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle();
        //刷新
        initView();
        setdata();
    }

    private int totalCount = 0;
    private List<ImageFloder> floders = new ArrayList<>();
    private File parent;
    private int maxCount = 0;//图片数量最多的文件
    private BasePop floderPop;
    private URIPop imagePop;

    private void setdata() {
        if (!SDCardUtils.isSDCardEnable()) {
            showToast("暂无外部存储");
            return;
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                checkDiskImage();
                handler.sendEmptyMessage(0x99);
            }
        }.start();
    }

    public void checkDiskImage() {
        //临时的辅助类，用于防止同一个文件夹的多次扫描
        HashSet<String> parentPaths = new HashSet<>();

        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(imageUri, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or " +
                        MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED);
        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            File parentFile = new File(path).getParentFile();
            //parent如果为空跳出此次循环
            if (parentFile == null) continue;
            String parentPath = parentFile.getAbsolutePath();
            //开始收集父级文件夹得信息
            ImageFloder floder;
            //每个文件夹只添加一次，不能重复添加
            if (parentPaths.contains(parentPath)) {
                continue;
            } else {
                parentPaths.add(parentPath);
                floder = new ImageFloder();
                //路径
                floder.setDirPath(parentPath);
                //第一张图片
                floder.setFirstImagePath(path);
            }
            int count = parentFile.list(filter).length;
            //收集图片的总数量
            totalCount += count;
            floder.setCount(count);
            floders.add(floder);
            //选出图片数量最多的floder
            if (count > maxCount) {
                maxCount = count;
                parent = parentFile;
            }
        }
        cursor.close();
    }

    public void initView() {
        recyclerView.setHasFixedSize(true);
        //瀑布流布局
        layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(this,null,0,0));
//        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        adapter = new LoaderURIAdapter(R.layout.adapter_uri);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new LoaderURIAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                createPopShowImage(position, parent);
                if (!imagePop.isShowing())
                    imagePop.showAtLocation(recyclerView, Gravity.CENTER, 0, 0);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //获取可见的第一个与最后一个item
                int[] firstPositions = layoutManager.findFirstVisibleItemPositions(new int[layoutManager.getSpanCount()]);
                firstVisibleItem = getMinPositions(firstPositions);
                int[] lastPositions = layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]);
                lastVisibleItem = getMaxPositions(lastPositions);
                //首次加载执行
                if (lastVisibleItem > 0 && first) {
                    first = false;
                    adapter.setImageLoader(firstVisibleItem, lastVisibleItem, recyclerView);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    adapter.setImageLoader(firstVisibleItem, lastVisibleItem, recyclerView);
                }
            }
        });
    }

    private int getMinPositions(int[] firstPositions) {
        int position = firstPositions[0];
        for (int i = 1; i < firstPositions.length; i++) {
            position = Math.min(position, firstPositions[i]);
        }
        return position;
    }

    private int getMaxPositions(int[] lastPositions) {
        int position = lastPositions[0];
        for (int i = 1; i < lastPositions.length; i++) {
            position = Math.max(position, lastPositions[i]);
        }
        return position;
    }

    @Event(R.id.layout)
    private void layoutEvent(View view) {
        createPopShowFloder();
        if (!floderPop.isShowing())
            floderPop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    private void createPopShowFloder() {
        int[] wh = ScreenUtils.getWidthAndHeight(this);
        if (floderPop == null)
            floderPop = new FloderPop(LayoutInflater.from(this).inflate(R.layout.pop_adapter, null),
                    wh[0], (int) (wh[1] * 0.7));
        floderPop.setDatas(floders);
        floderPop.setOnItemClickListener(new BasePop.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ImageFloder floder = floders.get(position);
                parent = new File(floder.getDirPath());
                handler.sendEmptyMessage(0x99);

                floderPop.dismiss();
            }
        });
    }

    private void createPopShowImage(int position, File parent) {
        if (imagePop == null)
            imagePop = new URIPop(LayoutInflater.from(this).inflate(R.layout.pop_viewpager, null));
        List<String> imageNames = Arrays.asList(parent.list(filter));
        imagePop.setParentPath(parent.getAbsolutePath());
        imagePop.setDatas(imageNames);
        imagePop.setCurrentItem(position);
    }

    @Override
    protected void handler(Message msg) {
        super.handler(msg);
        switch (msg.what) {
            case 0x99:
                setAdapterData();
                break;
        }
    }

    private void setAdapterData() {
        List<String> imageNames = Arrays.asList(parent.list(filter));
        adapter.setParentPath(parent.getAbsolutePath());
        adapter.setDatas(imageNames);
        String path = parent.getAbsolutePath();
        flodername.setText(path.substring(path.lastIndexOf("/") + 1));
        number.setText(Integer.toString(imageNames.size()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.interruptExecutors();
    }

    FilenameFilter filter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String filename) {
            if (filename.endsWith(".jpg") || filename.endsWith(".JPG") ||
                    filename.endsWith(".jpeg") || filename.endsWith(".JPEG") ||
                    filename.endsWith(".png") || filename.endsWith(".PNG"))
                return true;
            return false;
        }
    };
}
