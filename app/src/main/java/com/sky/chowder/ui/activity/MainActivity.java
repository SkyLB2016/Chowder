package com.sky.chowder.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toolbar;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.sky.adapter.RecyclerAdapter;
import com.sky.chowder.R;
import com.sky.chowder.api.view.IMainView;
import com.sky.chowder.model.ActivityEntity;
import com.sky.chowder.model.ActivityModel;
import com.sky.chowder.model.gson.ActivityEntityTypeAdapter;
import com.sky.chowder.model.gson.ActivityDeserial;
import com.sky.chowder.model.gson.ActivityListSerial;
import com.sky.chowder.model.gson.ActivitySerial;
import com.sky.chowder.model.gson.NullStringToEmptyAdapterFactory;
import com.sky.chowder.model.gson.StringNullAdapter;
import com.sky.chowder.ui.adapter.MainAdapter;
import com.sky.chowder.ui.presenter.MainP;
import com.sky.utils.AppUtils;
import com.sky.utils.JumpAct;
import com.sky.utils.LogUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import common.base.BasePActivity;
import common.model.ApiResponse;

/**
 * Created by libin on 2018/11/13 4:26 PM.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends BasePActivity<MainP> implements Toolbar.OnMenuItemClickListener, IMainView {
    //需要替换的图片
    //drawable-hdpi notification_default_icon.png
    //drawable-xhdpi notification_default_icon.png,landing_center
    //drawable-xhdpi ic_splash_indicator_selected
    //lib.account drawable ic_splash_indicator_selected

    private MainAdapter adapter;

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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        baseTitle.setLeftButton(-1);
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
        AppUtils.isPermissions(this,
                new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE},
                new int[]{0, 0, 0}
        );
        //Rw2 B2 U2 Lw U2 Rw' U2 Rw U2 F2 Rw F2 Lw' B2 Rw2
        final double dou = 82973.908;
        //LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")

        final String a = null;
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.add(a);
                LogUtils.i(list.size() + "");
                gsonBuilder();
            }
        });
    }

    List<String> list = new ArrayList<>();

    private void gsonBuilder() {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();//需要与@Expose搭配使用
        builder.excludeFieldsWithModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL);//排除这些修饰符修饰的属性
        //@SerializedName注解拥有最高优先级，在加有@SerializedName注解的字段上FieldNamingStrategy不生效！
        builder.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY);
        builder.setFieldNamingStrategy(new FieldNamingStrategy() {
            @Override
            public String translateName(Field f) {
                String name = f.getName();
                if ("name".equals(name)) {
                    return "className";
                } else if ("component".equals(name)) {
                    return "componentName";
                } else if ("des".equals(name)) {
                    return "describe";
                } else if ("image".equals(name)) {
                    return "img";
                } else if ("version".equals(name)) {
                    return "version";
                } else {
                    LogUtils.i("name==" + name);
                    return name;
                }
            }
        });
        //自定义排除字段
        builder.addSerializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        });
        builder.addDeserializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        });

        builder.serializeNulls();//属性值为空时输出 key:null
        builder.setPrettyPrinting();//tojson输出时，格式化json字符串
        builder.setVersion(1);//与@Since(1.2)或@Until(1.4)搭配使用才有效果

        builder.setDateFormat("yyyy-MM-dd");// 设置日期时间格式，另有2个重载方法,在序列化和反序化时均生效
        builder.disableInnerClassSerialization();// 禁此序列化内部类
        builder.generateNonExecutableJson();//生成不可执行的Json（多了 )]}' 这4个字符）
        builder.disableHtmlEscaping();//禁止转义html标签


        //自定义的序列化JsonSerializer与反序列化JsonDeserializer，定点适配，局限性很大，需要做费控判定。适用于单独解析特定的json语句
        builder.registerTypeAdapter(ActivityModel.class, new ActivitySerial());//自定义的序列化
        builder.registerTypeAdapter(ActivityEntity.class, new ActivityListSerial());//自定义序列化
        builder.registerTypeAdapter(new TypeToken<List<ActivityModel>>() {
        }.getRawType(), new ActivityDeserial());//自定义反序列化，fromJson时会用，注意实体类不匹配的话会崩溃

        //另一种自定义的序列化与反序列化，自定义TypeAdapter，重写read与write方法，定点适配，局限性很大，需要做费控判定。也只适用于单独解析特定的json数据
        builder.registerTypeAdapter(String.class, new StringNullAdapter());//不支持继承，但支持泛型
        builder.registerTypeHierarchyAdapter(Number.class, new StringNullAdapter());//支持继承，但不支持泛型
        builder.registerTypeAdapter(ActivityEntity.class, new ActivityEntityTypeAdapter());

        builder.registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory<String>());
        builder.registerTypeAdapterFactory(NullStringToEmptyAdapterFactory.getInstance());
        builder.registerTypeAdapterFactory(TypeAdapters.newFactory(String.class, new StringNullAdapter()));


        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory<String>())
                .create();

//        ActivityEntity entity = gson.fromJson(getString(R.string.jsonobj),ActivityEntity.class);
//        LogUtils.i(entity.toString());
//        LogUtils.i(gson.toJson(entity));
        ApiResponse<List<ActivityEntity>> entity = gson.fromJson(getString(R.string.jsonlist), new TypeToken<ApiResponse<List<ActivityEntity>>>() {
        }.getType());
        LogUtils.i(gson.toJson(entity.getObjList()));
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


    private void startOther(String packageName, String componentName) {
        try {
            JumpAct.jumpActivity(this, packageName, componentName);
        } catch (ActivityNotFoundException e) {
            showToast("程序未安装");
        }
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
}