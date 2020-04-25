package com.sky.oa.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.collection.LruCache;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sky.design.adapter.RecyclerAdapter;
import com.sky.design.app.BaseActivity;
import com.sky.design.app.BasePActivity;
import com.sky.oa.R;
import com.sky.oa.Solution;
import com.sky.oa.TreeNode;
import com.sky.oa.adapter.MainAdapter;
import com.sky.oa.api.view.IMainView;
import com.sky.oa.model.ActivityModel;
import com.sky.oa.presenter.MainP;
import com.sky.sdk.net.http.ApiResponse;
import com.sky.sdk.utils.AppUtils;
import com.sky.sdk.utils.FileUtils;
import com.sky.sdk.utils.JumpAct;
import com.sky.sdk.utils.LogUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by libin on 2018/11/13 4:26 PM.
 */
public class MainActivity extends BasePActivity<MainP> implements Toolbar.OnMenuItemClickListener, IMainView {

    @BindView(R.id.recycler)
    RecyclerView recycler;

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.fabLeft)
    FloatingActionButton fabLeft;

    private MainAdapter adapter;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            LogUtils.i("callback的handleMessage");
            return true;
        }
    }) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            LogUtils.i("自带的handleMessage");
        }
    };

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected MainP creatPresenter() {
        return new MainP(this);
    }

    @Override
    protected void initialize(@Nullable Bundle savedInstanceState) {
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//屏幕在此页面时保持常亮
        ButterKnife.bind(this);
        baseTitle.setNavigationIcon(null);
        recycler.setHasFixedSize(true);
        adapter = new MainAdapter(R.layout.adapter_main_delete);
        recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                JumpAct.jumpActivity(MainActivity.this, adapter.getDatas().get(position).getComponentName());
            }
        });
//recycler.addOnItemTouchListener();
        AppUtils.isPermissions(this,
                new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1119);

        //昔日古朝讲究德行，以德治天下，不动刀兵，但并非是不修武，而是以武威慑，以德服人。不动干戈，便只是能不动刀兵解决，能有别的方法解决的事情，便不动刀兵，而非是真的不动干戈。
        //易筋经、五禽戏、六字诀和八段锦
        //Rw2 B2 U2 Lw U2 Rw' U2 Rw U2 F2 Rw F2 Lw' B2 Rw2
        //LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fab.setVisibility(View.GONE);
//                fabLeft.setVisibility(View.VISIBLE);
                testMethod();
            }
        });
//        fabLeft.setOnClickListener(v -> {
//            fabLeft.setVisibility(View.GONE);
//            fab.setVisibility(View.VISIBLE);
//            testMethod();
//        });
        fabLeft.setVisibility(View.GONE);
    }

    private void testMethod() {
        String a = "a", b = a, g = b;
    }

    private void getArrayListLength() {
        ArrayList<Integer> list = new ArrayList();
        int size = 10;
        for (int i = 0; i < 1500; i++) {
            list.add(i);
            if (i == size) {
                size = size + (size >> 1);
                LogUtils.i("size==" + size);
                printListMaxLength(list);
            }
        }
        LogUtils.i("数组长度==" + list.toArray().length);
    }

    private void printListMaxLength(ArrayList<Integer> list) {
        Class cla = list.getClass();
        try {
            Field field = cla.getDeclaredField("elementData");
            field.setAccessible(true);
            Object[] array = (Object[]) field.get(list);
            LogUtils.i("数组长度==" + array.length);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private <T> void printIdentity(T model) {
        LogUtils.i("HashCode==" + model.hashCode());//hashcode
        LogUtils.i("内存地址==" + System.identityHashCode(model));//内存地址
    }


    static ThreadLocal<Integer> local = new ThreadLocal<Integer>() {
        @Nullable
        @Override
        protected Integer initialValue() {
            return super.initialValue();
        }
    };

    //获取程序启动时打开的线程数量
    private void startThreadNum() {
        LogUtils.i("核心数==" + Runtime.getRuntime().availableProcessors());
        LogUtils.i("thread==" + Thread.currentThread().getName());
        //MainActivity启动完成后，有多少个线程启动了。
        Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
        for (Map.Entry<Thread, StackTraceElement[]> entry : map.entrySet()) {
            LogUtils.i("thread==" + entry.getKey().getName());
        }
    }

    //获取类的加载器
    private void getCLoader() {
        ClassLoader classLoader = getClassLoader();
        LogUtils.i("默认的classLoader==" + classLoader);
        classLoader = ClassLoader.getSystemClassLoader();
        LogUtils.i("系统的classLoader==" + classLoader);
        classLoader = classLoader.getParent();
        LogUtils.i("系统父类的classLoader==" + classLoader);
        classLoader = classLoader.getParent();
        LogUtils.i("系统父类的父类的classLoader==" + classLoader);
        classLoader = BaseActivity.class.getClassLoader();
        LogUtils.i("BaseActivity的classLoader==" + classLoader);
        classLoader = AppCompatActivity.class.getClassLoader();
        LogUtils.i("AppCompatActivity的classLoader==" + classLoader);
        classLoader = FragmentActivity.class.getClassLoader();
        LogUtils.i("FragmentActivity的classLoader==" + classLoader);
        classLoader = ComponentActivity.class.getClassLoader();
        LogUtils.i("ComponentActivity的classLoader==" + classLoader);
        classLoader = androidx.core.app.ComponentActivity.class.getClassLoader();
        LogUtils.i("androidx.core.app.ComponentActivity的classLoader==" + classLoader);
        classLoader = Activity.class.getClassLoader();
        LogUtils.i("Activity的classLoader==" + classLoader);

    }

    private void testM() {
        ApiResponse<String>[] response = new ApiResponse[2];
        ApiResponse<String> ap = new ApiResponse<String>();
        ap.setObj("dddddfdfd====");
        response[0] = ap;
        LogUtils.i(response[0].getObj());
        Integer[] integers = new Integer[2];
        List<Number> list = new ArrayList<>();
        list.add(1.8);


        Lock lock = new ReentrantLock();
        lock.newCondition();
        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                return null;
            }
        };
        task.execute("");
    }

    ApiResponse<? extends Comparable> apiResponse;

    public void aa(ApiResponse<? extends Comparable> apiResponse) {

    }

    private void getEnvironment() {
        String path = Environment.getExternalStorageDirectory().getPath() + "/AExcelDemo/aa.txt";
        File file = new File(path);
        file.getParentFile().mkdir();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!file.exists()) {
            LogUtils.i("创建文件==失败");
        }
//        LogUtils.i("lujin==" + Environment.getDataDirectory());
//        LogUtils.i("lujin==" + Environment.getDownloadCacheDirectory());
//        LogUtils.i("lujin==" + Environment.getRootDirectory());
//        LogUtils.i("lujin==" + Environment.getExternalStorageState());
//        LogUtils.i("lujin==" + Environment.getExternalStorageDirectory());

//        LogUtils.i("storage==" + Environment.getExternalStorageDirectory());
//        LogUtils.i("DIRECTORY_MUSIC==" + getExternalFilesDir(Environment.DIRECTORY_MUSIC));
//        LogUtils.i("DIRECTORY_PODCASTS==" + getExternalFilesDir(Environment.DIRECTORY_PODCASTS));
//        LogUtils.i("DIRECTORY_RINGTONES==" + getExternalFilesDir(Environment.DIRECTORY_RINGTONES));
//        LogUtils.i("DIRECTORY_ALARMS==" + getExternalFilesDir(Environment.DIRECTORY_ALARMS));
//        LogUtils.i("DIRECTORY_NOTIFICATIONS==" + getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS));
//        LogUtils.i("DIRECTORY_PICTURES==" + getExternalFilesDir(Environment.DIRECTORY_PICTURES));
//        LogUtils.i("DIRECTORY_MOVIES==" + getExternalFilesDir(Environment.DIRECTORY_MOVIES));
        ArrayList<String> volumenames = new ArrayList<>(MediaStore.getExternalVolumeNames(this));
        String path1 = volumenames.get(0);
        LogUtils.i("path==" + path1);

    }

    private void testMeasure() {
        ViewGroup.LayoutParams lp = fab.getLayoutParams();
        LogUtils.i("width==" + lp.width);
        LogUtils.i("height==" + lp.height);
        LogUtils.i("width==" + fab.getWidth());
        LogUtils.i("height==" + fab.getHeight());
        int widthSpec = View.MeasureSpec.makeMeasureSpec(100, View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(100, View.MeasureSpec.EXACTLY);
        fab.measure(widthSpec, heightSpec);
        LogUtils.i("width==" + fab.getMeasuredHeight());
        LogUtils.i("height==" + fab.getHeight());
        int width = fab.getMeasuredWidth();
        int height = fab.getMeasuredHeight();
    }

    /**
     * 申请权限
     */
    private void requestWriteSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //大于等于23 请求权限
            if (!Settings.System.canWrite(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1);
            }
        } else {
            //小于23直接设置
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Settings.System.canWrite方法检测授权结果
                if (Settings.System.canWrite(getApplicationContext())) {
                    showToast("获取了权限");
                } else {
                    showToast("您拒绝了权限");
                }
            }
        }

    }

    private static final int MODE_SHIFT = 30;
    private static final int MODE_MASK = 0x3 << MODE_SHIFT;

    public static final int UNSPECIFIED = 0 << MODE_SHIFT;
    public static final int EXACTLY = 1 << MODE_SHIFT;
    public static final int AT_MOST = 2 << MODE_SHIFT;

    /**
     * MeasureSpec的运算
     */
    private void computerMeasureSpec() {
        //        LogUtils.i("MODE_SHIFT ==" + MODE_SHIFT);
//        LogUtils.i("MODE_MASK  ==" + MODE_MASK);
//        LogUtils.i("UNSPECIFIED ==" + UNSPECIFIED);
//        LogUtils.i("EXACTLY     ==" + EXACTLY);
//        LogUtils.i("AT_MOST     ==" + AT_MOST);

//        int spec = View.MeasureSpec.makeMeasureSpec(30, View.MeasureSpec.AT_MOST);
//        LogUtils.i("MODE     ==" + View.MeasureSpec.getMode(spec));
//        LogUtils.i("MODE     ==" + View.MeasureSpec.getSize(spec));
//
//        LogUtils.i("MODE_MASK ==" + (Integer.toBinaryString(MODE_MASK)));
//        LogUtils.i("MODE_MASK ==" + (Integer.toBinaryString(~MODE_MASK)));
//        LogUtils.i("UNSPECIFIED ==" + (Integer.toBinaryString(UNSPECIFIED)));
//        LogUtils.i("EXACTLY ==" + (Integer.toBinaryString(EXACTLY)));
//        LogUtils.i("AT_MOST ==" + (Integer.toBinaryString(AT_MOST)));

        LogUtils.i("size & ~MODE_MASK ==" + (Integer.toBinaryString(60 & ~MODE_MASK)));
        LogUtils.i("mode & MODE_MASK ==" + (Integer.toBinaryString(View.MeasureSpec.AT_MOST & MODE_MASK)));
        LogUtils.i("MeasureSpec ==" + (Integer.toBinaryString((60 & ~MODE_MASK) | (View.MeasureSpec.AT_MOST & MODE_MASK))));

    }

    /**
     * 二进制运算
     */
    private void binaryString() {
        LogUtils.i("60 ==" + (Integer.toBinaryString(60)));
        LogUtils.i("13 ==" + (Integer.toBinaryString(13)));
        LogUtils.i("60 & 13 ==" + (60 & 13));
        LogUtils.i("60 & 13 ==" + (Integer.toBinaryString(60 & 13)));
        LogUtils.i("60 | 13 ==" + (60 | 13));
        LogUtils.i("60 | 13 ==" + (Integer.toBinaryString(60 | 13)));
        LogUtils.i("60 ^ 13 ==" + (60 ^ 13));
        LogUtils.i("60 ^ 13 ==" + (Integer.toBinaryString(60 ^ 13)));

        int a = ~60;
        LogUtils.i("a ==" + (a));
        LogUtils.i("a的二进制 ==" + (Integer.toBinaryString(a)));
    }

    private void getRecycler() {
        View view = recycler.getChildAt(0);
        LogUtils.i("top==" + view.getTop());
        LinearLayoutManager manager = (LinearLayoutManager) recycler.getLayoutManager();
        LogUtils.i("first==" + manager.findFirstVisibleItemPosition());
    }

    private void fabState() {
        LogUtils.i("fab的Enable状态==" + fab.isEnabled());
        LogUtils.i("fab的Clickable状态==" + fab.isClickable());
        LogUtils.i("fab的LongClickable状态==" + fab.isLongClickable());

        LogUtils.i("fabLeft的Enable状态==" + fabLeft.isEnabled());
        LogUtils.i("fabLeft的Clickable状态==" + fabLeft.isClickable());
        LogUtils.i("fabLeft的LongClickable状态==" + fabLeft.isLongClickable());
    }

    //    @Override
//    protected void onUserLeaveHint() {
//        super.onUserLeaveHint();
//    }
//
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean flag = super.dispatchTouchEvent(ev);
        if (!flag) {
            LogUtils.i("flag==" + flag);
        }
        return flag;
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        LogUtils.i(getClass().getSimpleName() + "==" + Thread.currentThread().getStackTrace()[2].getMethodName());
//    }

    //
//    @Override
//    protected void onStart() {
//        super.onStart();
//        LogUtils.i(getClass().getSimpleName()+"=="+Thread.currentThread().getStackTrace()[2].getMethodName());
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        LogUtils.i(getClass().getSimpleName() + "==" + Thread.currentThread().getStackTrace()[2].getMethodName());
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        LogUtils.i(getClass().getSimpleName()+"=="+Thread.currentThread().getStackTrace()[2].getMethodName());
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        LogUtils.i(getClass().getSimpleName()+"=="+Thread.currentThread().getStackTrace()[2].getMethodName());
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        LogUtils.i(getClass().getSimpleName()+"=="+Thread.currentThread().getStackTrace()[2].getMethodName());
//    }

    private void getIdentity() {
        Integer entity = new Integer(3);
        LogUtils.i("HashCode地址==" + entity.hashCode());//hashcode
        LogUtils.i("内存地址==" + System.identityHashCode(entity));//内存地址
        entity = 5;
        LogUtils.i("HashCode地址==" + entity.hashCode());
        LogUtils.i("内存地址==" + System.identityHashCode(entity));

        String text = "Java";
        LogUtils.i("HashCode地址==" + text.hashCode());
        LogUtils.i("内存地址==" + System.identityHashCode(text));

        text = "Android";
        LogUtils.i("HashCode地址==" + text.hashCode());
        LogUtils.i("内存地址==" + System.identityHashCode(text));
    }

    private void equalPoetry() {
//        presenter.calculationTextLength();

        String poetry = FileUtils.readAssestToChar(this, "Documents/文学/道家/道德经.txt")
//        String poetry = FileUtils.readAssestToChar(this, "Documents/复写.txt")
                .replaceAll("　", "")
                .replaceAll("\n", "");
        LogUtils.i("长度==" + (poetry.length()));

//        String text = getResources().getString(R.string.new_text).replaceAll(" ", "");
//        LogUtils.i("长度==" + (text.length()));
//        String origin = getResources().getString(R.string.origin).replaceAll(" ", "");
//        LogUtils.i("长度==" + (origin.length()));
//        String[] texts = text.split("");
//        String[] origins = origin.split("");
//        for (int i = 2; i < texts.length - 2; i++) {
//            if (!texts[i].equals(origins[i])) {
////                LogUtils.i(texts[i - 2] + texts[i - 1] + texts[i] + texts[i + 1] + texts[i + 2] + "==" + origins[i - 2] + origins[i - 1] + origins[i] + origins[i + 1]+origins[i + 2]);
////                i += 2;
//                LogUtils.i(i + texts[i] + "==" + origins[i]);
//            }
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //requestCode是之前传入的请求值
        //permissions是请求的权限集合
        //grantResults是权限是否同意的集合，与permissions一一对应。
        LogUtils.i("requestCode==" + requestCode);
        for (int i = 0; i < permissions.length; i++) {
            LogUtils.i("permissions==" + permissions[i]);
        }
        for (int i = 0; i < grantResults.length; i++) {
            LogUtils.i("grantResults==" + i + "==" + grantResults[i]);
        }
    }

    @Override
    public void setData(@NotNull List<ActivityModel> data) {
        adapter.setDatas(data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //需要在toolbar中注册，
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_01:
                startOther("com.cx.architecture", "com.cx.architecture.ui.activity.LoginActivity");
                break;
            case R.id.action_02:
                startOther("com.gc.materialdesigndemo", "com.gc.materialdesigndemo.ui.MainActivity");
                break;
        }
        return false;
    }

    //原来的
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_01:
                startOther("com.cx.architecture", "com.cx.architecture.ui.activity.LoginActivity");
                break;
            case R.id.action_02:
                startOther("com.gc.materialdesigndemo", "com.gc.materialdesigndemo.ui.MainActivity");
                break;
        }
        return false;
    }


    private void startOther(String packageName, String componentName) {
        try {
            JumpAct.jumpActivity(this, packageName, componentName);
        } catch (ActivityNotFoundException e) {
            showToast("程序未安装");
        }
    }

    private String cacheDir;//缓存目录 /storage/emulated/0/Android/data/包名/cache
    private String fileCacheDir;//文件日志缓存文件夹目录 /storage/emulated/0/Android/data/包名/files/Documents
    private String picCacheDir;//图片文件夹目录 /storage/emulated/0/Android/data/包名/files/Pictures

    /**
     * 获取缓存文件夹
     */
    private void getDataCacheDir() {
        cacheDir = getExternalCacheDir().getAbsolutePath() + File.separator;
        fileCacheDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() + File.separator;
        picCacheDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator;
    }

    private long lastBack = 0;

    @Override
    public void onBackPressed() {
        long now = System.currentTimeMillis();
        if (now - lastBack > 3000) {
            showToast(getString(R.string.toast_exit));
            lastBack = now;
        } else
            super.onBackPressed();
    }

    private void testTree() {
//        handler.sendEmptyMessage(1);
        TreeNode tree = new TreeNode(4);
        tree.left = getTreeNode(2, 1, 3);
        tree.right = getTreeNode(7, 6, 9);

        LogUtils.i(tree.val + getTreeString(tree));
        new Solution().invertTree(tree);
        LogUtils.i(tree.val + getTreeString(tree));
    }

    private String getTreeString(TreeNode tree) {
        StringBuilder builder = new StringBuilder();
//        builder.append(tree.val);
        //单侧获取
//        if (tree.left != null)
//            builder.append(getTreeString(tree.left));
//        if (tree.right != null)
//            builder.append(getTreeString(tree.right));
        if (tree.right != null && tree.right != null) {
            builder.append(tree.left.val);
            builder.append(tree.right.val);
            builder.append(getTreeString(tree.left));
            builder.append(getTreeString(tree.right));
//        } else if (tree.left != null) {
//            builder.append(tree.left.val);
//            builder.append(getTreeString(tree.left));
//        } else if (tree.right != null) {
//            builder.append(tree.right.val);
//            builder.append(getTreeString(tree.right));
        }
        return builder.toString();
    }


    private TreeNode getTreeNode(int root, int left, int right) {
        TreeNode treeNode = new TreeNode(root);
        if (left == -1) {
//            treeNode.left = null;
        } else {
            treeNode.left = getTreeNode(left, -1, -1);
        }
        if (right == -1) {
//            treeNode.right = null;
        } else {
            treeNode.right = getTreeNode(right, -1, -1);
        }
        return treeNode;
    }
}
