package com.sky.gson;

import android.support.annotation.NonNull;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.sky.chowder.model.ActivityEntity;
import com.sky.chowder.model.ActivityModel;
import com.sky.utils.LogUtils;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by SKY on 2017/9/27 22:03 九月.
 */
public class GsonUtils {
    private static Gson gson;

    private GsonUtils() {
        GsonBuilder builder = new GsonBuilder();
        //字段过滤相关方法
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
        builder.registerTypeAdapter(ActivityEntity.class, new ActivityTypeAdapter());

        builder.registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory<String>());
        builder.registerTypeAdapterFactory(NullStringToEmptyAdapterFactory.getInstance());
        builder.registerTypeAdapterFactory(TypeAdapters.newFactory(String.class, new StringNullAdapter()));
    }
    private void gsonBuilder() {
        GsonBuilder builder = new GsonBuilder();
        //字段过滤相关方法
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
        builder.registerTypeAdapter(ActivityEntity.class, new ActivityTypeAdapter());

        builder.registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory<String>());
        builder.registerTypeAdapterFactory(NullStringToEmptyAdapterFactory.getInstance());
        builder.registerTypeAdapterFactory(TypeAdapters.newFactory(String.class, new StringNullAdapter()));
    }

    static Gson getGson() {
        if (gson == null) gson = new Gson();
        return gson;
    }

    /**
     * @param json {"":"","":"","":"","":[{"":""},{"":""},{"":""}]} 可包含数组，但类型要明确
     * @param cls  kotlin: (ActivityModel::class.java) java :(ActivityModel[].class 类中的数组类型要明确)
     * @return
     */
    public static <T> T jsonToEntity(@NonNull String json, Class<T> cls) {
        try {
            return getGson().fromJson(json, cls);
        } catch (JsonSyntaxException e) {
            LogUtils.d(e.toString());
        } catch (JsonParseException e) {
            LogUtils.d(e.toString());
        } catch (Exception e) {
            LogUtils.d(e.toString());
        }
        return null;
    }

    /**
     * @param json    {"":"","":"","":"","":[{"":""},{"":""},{"":""}]}
     * @param typeOfT kotlin: (object : TypeToken() {}.type)
     * @return
     */
    public static <T> T jsonToEntity(@NonNull String json, Type typeOfT) {
        try {
            return getGson().fromJson(json, typeOfT);
        } catch (JsonSyntaxException e) {
            LogUtils.d(e.toString());
        } catch (JsonParseException e) {
            LogUtils.d(e.toString());
        } catch (Exception e) {
            LogUtils.d(e.toString());
        }
        return null;
    }

    /**
     * @param json [{"":""},{"":""},{"":""}]
     * @param cls  kotlin: (Array::class.java)
     * @return
     */
    public static <T> List<T> jsonToList(@NonNull String json, Class<T[]> cls) {
        try {
            return new ArrayList(Arrays.asList(getGson().fromJson(json, cls)));
        } catch (JsonSyntaxException e) {
            LogUtils.d(e.toString());
        } catch (JsonParseException e) {
            LogUtils.d(e.toString());
        } catch (Exception e) {
            LogUtils.d(e.toString());
        }
        return null;
    }

    public static <T> T[] jsonToArray(@NonNull String json, Class<T[]> cls) {
        try {
            return getGson().fromJson(json, cls);
        } catch (JsonSyntaxException e) {
            LogUtils.d(e.toString());
        } catch (JsonParseException e) {
            LogUtils.d(e.toString());
        } catch (Exception e) {
            LogUtils.d(e.toString());
        }
        return null;
    }

    /**
     * 转换成json对象
     *
     * @param object 需要转换的对象
     * @return
     */
    @NotNull
    public static <T> String toJson(@NotNull T object) {
        return getGson().toJson(object);
    }
}
