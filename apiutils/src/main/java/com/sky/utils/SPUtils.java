package com.sky.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by sky on 16/5/10 下午3:50.
 * SharedPreferences管理类
 */
public class SPUtils {
    public SPUtils(Context context) {
        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }


    //保存在手机里面的文件名
    public static final String FILE_NAME = "USER_INFO";
    public static SPUtils instance;
    public static SharedPreferences sp;
    public static SharedPreferences.Editor editor;


    public static SPUtils getInstance(Context context) {
        if (instance == null) {
            synchronized (SPUtils.class) {
                if (instance == null) {
                    instance = new SPUtils(context);
                }
            }
        }
        return instance;
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key    key
     * @param object value
     */
    public void put(String key, Object object) {
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        editor.commit();
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param map map集合
     */
    public void put(Map<String, Object> map) {
        Set set = map.keySet();
        for (Iterator iter = set.iterator(); iter.hasNext(); ) {
            String key = (String) iter.next();
            Object object = map.get(key);
            if (object instanceof String) {
                editor.putString(key, (String) object);
            } else if (object instanceof Integer) {
                editor.putInt(key, (Integer) object);
            } else if (object instanceof Boolean) {
                editor.putBoolean(key, (Boolean) object);
            } else if (object instanceof Float) {
                editor.putFloat(key, (Float) object);
            } else if (object instanceof Long) {
                editor.putLong(key, (Long) object);
            } else if (object == null) {
                editor.putString(key, null);
            } else {
                editor.putString(key, object.toString());
            }
        }
        editor.commit();
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key           key
     * @param defaultObject 读取失败时，返回的数据
     * @return value
     */
    public Object get(String key, Object defaultObject) {
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key key
     */
    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }

    /**
     * 清除所有数据
     *
     * @param context context
     */
    public void clear(Context context) {
        editor.clear();
        editor.commit();
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key key
     * @return 查询某个key是否已经存在
     */
    public boolean contains(String key) {
        return sp.contains(key);
    }

    /**
     * @param context context
     * @return 返回所有的键值对
     */
    public Map<String, ?> getAll(Context context) {
        return sp.getAll();
    }

    public Map getValue(Object obj) {
        Map map = new HashMap();
        Class cla;
        try {
            cla = Class.forName(obj.getClass().getName());
            Method[] m = cla.getMethods();
            for (int i = 0; i < m.length; i++) {
                String method = m[i].getName();
                if (method.startsWith("get")) {
                    try {
                        Object value = m[i].invoke(obj);
//						if (value != null)
//						{
                        String key = method.substring(3);
                        key = key.substring(0, 1).toLowerCase() + key.substring(1);
                        map.put(key, value);
//						}
                    } catch (Exception e) {
                        System.out.println("error:" + method);
                    }
                }
                if (method.startsWith("is")) {
                    try {
                        Object value = m[i].invoke(obj);
//						if (value != null)
//						{
                        String key = method.substring(2);
//							key=key.substring(0,1).toUpperCase()+key.substring(1);
                        map.put(key, value);
//						}
                    } catch (Exception e) {
                        System.out.println("error:" + method);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

}