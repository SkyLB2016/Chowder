package com.sky.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SKY on 2017/9/27 22:03 九月.
 */
public class GsonUtils {
    private static Gson gson;

    private GsonUtils() {
    }

    static Gson getGson() {
        if (gson == null) gson = new Gson();
        return gson;
    }

    /**
     * 解析json
     */
    public static <T> T json2Obj(@NonNull String json, Class<T> cls) {
        try {
            return getGson().fromJson(json, cls);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static <T> List<T> jsonObj2Array(@NonNull String jsonArray, Class<T> clazz) {
        List<T> lists = new ArrayList<T>();
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(jsonArray).getAsJsonArray();
        for (JsonElement obj : array) {
            T t = getGson().fromJson(obj, clazz);
            lists.add(t);
        }
        return lists;
    }
}
