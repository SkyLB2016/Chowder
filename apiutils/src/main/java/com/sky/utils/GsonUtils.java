package com.sky.utils;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Arrays;
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
     * @param json {"":"","":"","":"","":[{"":""},{"":""},{"":""}]} 可包含数组，但类型要明确
     * @param cls  kotlin: (ActivityModel::class.java) java :(ActivityModel[].class) 类中的数组类型要明确
     * @return
     */
    public static <T> T jsonToEntity(@NonNull String json, Class<T> cls) {
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

    /**
     * @param json    {"":"","":"","":"","":[{"":""},{"":""},{"":""}]}
     * @param typeOfT kotlin: (object : TypeToken<*<*<*>>>() {}.type) java :(new TypeToken<*<*<*>>>() {}.type)
     * @return
     */
    public static <T> T jsonToEntity(@NonNull String json, Type typeOfT) {
        try {
            return getGson().fromJson(json, typeOfT);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param json [{"":""},{"":""},{"":""}]
     * @param cls  kotlin: (Array<*>::class.java) java :(ActivityModel[].class)
     * @return
     */
    public static <T> List<T> jsonToList(@NonNull String json, Class<T[]> cls) {
        try {
            return Arrays.asList(getGson().fromJson(json, cls));
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @NotNull
    public static <T> String toJson(@NotNull T json) {
        return getGson().toJson(json);
    }
}
