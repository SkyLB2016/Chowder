package com.sky.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by sky on 16/5/10 下午3:50.
 * SharedPreferences管理类
 */
public class SPUtils {
    //保存在手机里面的默认文件名
    public static final String FILE_NAME = "USER_INFO";
    private static Context context;

    public static SPUtils instance;
    public static SharedPreferences sp;
    private static Map<String, SharedPreferences> SPMaps = new HashMap<>();


    public static void init(Context context) {
        SPUtils.context = context.getApplicationContext();
    }

    public static SPUtils getInstance() {
        return getInstance(FILE_NAME);
    }

    public static SPUtils getInstance(String fileName) {
        sp = SPMaps.get(fileName);
        if (sp == null) {
            instance = new SPUtils(fileName);
            SPMaps.put(fileName, sp);
        }
//        if (instance == null) instance = new SPUtils(fileName);
        return instance;
    }

    private SPUtils(String fileName) {
        if (context == null) throw new NullPointerException("SPUtils的context不能为null");
        sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    /**
     * 保存数据，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key    key
     * @param object value
     */
    public void put(String key, Object object) {
        if (object instanceof String) sp.edit().putString(key, (String) object).apply();
        else if (object instanceof Integer) sp.edit().putInt(key, (Integer) object).apply();
        else if (object instanceof Boolean) sp.edit().putBoolean(key, (Boolean) object).apply();
        else if (object instanceof Float) sp.edit().putFloat(key, (Float) object).apply();
        else if (object instanceof Long) sp.edit().putLong(key, (Long) object).apply();
        else if (object == null) sp.edit().putString(key, "").apply();
        else sp.edit().putString(key, object.toString()).apply();
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param map map集合
     */
    public void put(Map<String, Object> map) {
        for (Iterator iter = map.keySet().iterator(); iter.hasNext(); ) {
            String key = (String) iter.next();
            put(key, map.get(key));
        }
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key   key
     * @param value 读取失败时，返回的数据
     * @return value
     */
    public Object get(String key, Object value) {
        if (value instanceof String) return sp.getString(key, (String) value);
        else if (value instanceof Integer) return sp.getInt(key, (Integer) value);
        else if (value instanceof Boolean) return sp.getBoolean(key, (Boolean) value);
        else if (value instanceof Float) return sp.getFloat(key, (Float) value);
        else if (value instanceof Long) return sp.getLong(key, (Long) value);

        return "";
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key key
     */
    public void remove(String key) {
        sp.edit().remove(key).apply();
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        sp.edit().clear().apply();
    }

    /**
     * @param key key
     * @return 查询某个key是否已经存在
     */
    public boolean contains(String key) {
        return sp.contains(key);
    }

    /**
     * @return 返回所有的键值对
     */
    public Map<String, ?> getAll() {
        return sp.getAll();
    }

    /**
     * @param obj 需要转化的实体类数据
     * @return get方法的map集合
     */
    public Map<String, Object> getValue(Object obj) {
        Map<String, Object> map = new HashMap();
        try {
            Class cla = Class.forName(obj.getClass().getName());//获取类名
//            Field[] fields = cla.getDeclaredFields();
            Method[] methods = cla.getMethods();//获取类中的方法
            for (Method method : methods) {
                String key = method.getName();//获取方法名称
                if (!key.startsWith("get") && !key.startsWith("is")) continue;//既不是get也不是is结束此次循环
                int sub = 3;//默认为get长度
                if (key.startsWith("is")) sub = 2;//判断是否为is
                key = key.substring(sub);//去除get或者is
//                key = key.substring(0, 1).toUpperCase() + key.substring(1);//首字母大写化
                key = key.substring(0, 1).toLowerCase() + key.substring(1);//首字母小写化
                map.put(key, method.invoke(obj));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return map;
    }

    public <T extends Object> T getObject(String text, T a) {
        return (T) get(text, a);
    }

    public <T extends Object> void setObject(String text, T a) {
        put(text, a);
    }

}