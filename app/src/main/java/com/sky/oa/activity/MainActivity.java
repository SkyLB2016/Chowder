package com.sky.oa.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.sky.design.adapter.RecyclerAdapter;
import com.sky.design.app.BasePActivity;
import com.sky.oa.R;
import com.sky.oa.Solution;
import com.sky.oa.TreeNode;
import com.sky.oa.adapter.MainAdapter;
import com.sky.oa.api.view.IMainView;
import com.sky.oa.model.ActivityModel;
import com.sky.oa.presenter.MainP;
import com.sky.sdk.utils.AppUtils;
import com.sky.sdk.utils.FileUtils;
import com.sky.sdk.utils.JumpAct;
import com.sky.sdk.utils.LogUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by libin on 2018/11/13 4:26 PM.
 */
public class MainActivity extends BasePActivity<MainP> implements Toolbar.OnMenuItemClickListener, IMainView {

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
        baseTitle.setNavigationIcon(null);
        RecyclerView recycler = findViewById(R.id.recycler);
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
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testMethod();
            }
        });
    }

    private void testMethod() {
//        handler.sendEmptyMessage(1);
//                equalPoetry();
//        ActivityManager
//                ActivityThread
//        TextView
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        String add = map.put("add", "add");
        map.put("add1", "add");
        map.put("add2", "add");
        map.put("add3", "add");
        map.put("add4", "add");
        map.put("add5", "add");
        map.put("add6", "add999");

        LogUtils.i(map.get("add1"));
        String add1 = map.put("add3", "不同的");
        LogUtils.i("add1==" + add1);
        String add4 = map.put("add4", "add");
        LogUtils.i("add4==" + add4);
        Map.Entry<String,String> toEvict = null;
        for (Map.Entry<String,String>  entry : map.entrySet()) {
            toEvict = entry;
        }

        LogUtils.i("add4==" + toEvict.getKey()+"=="+toEvict.getValue());

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.i(getClass().getSimpleName() + "==" + Thread.currentThread().getStackTrace()[2].getMethodName());
    }

    //
//    @Override
//    protected void onStart() {
//        super.onStart();
//        LogUtils.i(getClass().getSimpleName()+"=="+Thread.currentThread().getStackTrace()[2].getMethodName());
//    }
//
    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.i(getClass().getSimpleName() + "==" + Thread.currentThread().getStackTrace()[2].getMethodName());
    }
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