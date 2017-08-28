package com.sky.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * json工具类
 * Created by keweiquan on 16/3/14.
 */
public class JsonUtils {
    private static Gson gson;

    private JsonUtils() {
    }

    static Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    /**
     * 解析json
     *
     * @param <T>      泛型
     * @param jsonData json字符串
     * @param cls      对象
     * @return 对象
     */
    public static <T> T json2Obj(@NonNull String jsonData, Class<T> cls) {
        if (TextUtils.isEmpty(jsonData)) {
            return null;
        }
        T t;
        try {
            t = getGson().fromJson(jsonData, cls);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        } catch (JsonParseException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return t;
    }

    /**
     * 解析jsonArray
     *
     * @param <T>       泛型
     * @param jsonArray json数组
     * @param clazz     对象
     * @return 对象
     */
    @Nullable
    public static <T> List<T> jsonObj2Array(@NonNull String jsonArray, Class<T> clazz) {
        if (TextUtils.isEmpty(jsonArray)) {
            return null;
        }
        List<T> lists = new ArrayList<T>();
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(jsonArray).getAsJsonArray();
        for (JsonElement obj : array) {
            T t = getGson().fromJson(obj, clazz);
            lists.add(t);
        }
        return lists;
    }

    /**
     * 解析jsonArray
     *
     * @param <T>       泛型
     * @param jsonArray json数组
     * @param clazz     对象
     * @return 对象
     */
    public static <T> List<T> jsonObj2Array(@NonNull JSONArray jsonArray, Class<T> clazz) {
        return jsonObj2Array(jsonArray.toString(), clazz);

    }

    /**
     * list转json
     *
     * @param lists list
     * @return json数据
     */
    public static <T> String list2Json(@NonNull List<T> lists) {
        return getGson().toJson(lists);
    }

    /**
     * 对象转json
     *
     * @param t 对象
     * @return json数据
     */
    public static <T> String obj2Json(@NonNull T t) {
        return getGson().toJson(t);
    }

    /**
     * 从assets读取json文件内容
     *
     * @param context  上下文
     * @param fileName 文件名
     * @return json
     */
    public static String readJsonFromAssets(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(
                    new InputStreamReader(
                            assetManager.open(fileName)
                    )
            );
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
