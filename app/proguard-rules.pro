# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


# 基本指令
# 指定代码的压缩级别，默认就为5
-optimizationpasses 5
# 不使用大小写混合,混淆后的类名为小写
-dontusemixedcaseclassnames
# 指定不去忽略非公共的库的类
-dontskipnonpubliclibraryclasses
# 指定不去忽略非公共的库的类的成员
-dontskipnonpubliclibraryclassmembers
# 不做预校验，preverify是Proguard的4个步骤之一
# android不需要preverify，去掉这一步可加快混淆速度
-dontpreverify
# 有了verbose这句话，混淆后就会生成映射文件，包含有类名-》混淆后的类名的映射关系，然后使用printmapping指定映射文件的名称
-verbose
-printmapping proguardMapping.txt

# 混淆时所采用的算法,后面的参数是一个过滤器，这个过滤器是谷歌推荐的算法，一般不改变。
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# 保护代码中的Annotation注解，不被混淆，这在json实体映射时非常重要，比如fastJson
-keepattributes *Annotation*
# 避免混淆泛型，这在json实体映射时非常重要，如Gson，fastJson,如果混淆报错建议关掉
-keepattributes Signature
#抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable


# 需要保留的东西
# 保留所有的本地native方法不被混淆
-keepclasseswithmembernames class * { native <methods>; }
# 保留留继承自Activity、application这些类的子类，因为这些子类有可能会被外部调用，比如说，第一行就保证了所有的Activity的子类不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService

#保留在Activity中的方法参数是view的方法，从而我们在layout里边编写onClick就不会受影响
-keepclassmembers class * extends android.app.activity {
    public void *(android.view.View);
}

# 保留枚举 enum 类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保留自定义控件不被混淆
-keep class * extends android.view.View {
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}


# 保留 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# 保留Serializable序列化不被混淆
#-keep class * implements java.io.Serializable {*;}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 对于R（资源）下的所有类及其方法，都不能被混淆
#-keep public class com.sky.demo.R$*{ public static final int *; }
-keep class **.R$* {
    *;
}

# 对于带有回调函数onXXEvent的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
}

#量身定制
# 保留实体类
-keep class com.sky.chowder.model.** {
    public void set*(***);
    public *** get*();
    public *** is*();
}
#应用了WebView中复杂的操作，加入以下两段
#-keepclassmembers class * extends android.webkit.webViewClient {
#    public void *(android.webkit.WebView,java.lang.String,android.graphics.Bitmap);
#    public boolean *(android.webkit.WebView,java.lang.String);
#}
#-keepclassmembers class * extents android.webkit.webViewClient {
#    public void *(android.webkit.webView,java.lang.String);
#}


#处理JavaScript
#-keepclassmembers class （所在路径）$js(名称){ <methods>}
#处理反射参见《App研发录》第七章

# 如果有引用v4
-dontwarn androidx.viewpager.widget.**
-keep class androidx.viewpager.widget.** { *; }
-keep public class * extends androidx.viewpager.widget.**
# 自己的包下的fragment
-keep public class com.sky.chowder.ui.fragment.** {*;}


#保护指定类的成员，如果此类受到保护他们会保护的更好
-keepclassmembers class * {
    public <init>(org.json.JSONObject);
}

# 不优化输入的类文件
#-dontoptimize

#第三方jar保留

# OkHttp3
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
#-keep class okhttp3.**{*;}
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
# OkHttp3

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform$Java8
-keepattributes Exceptions

#-keepclassmembernames,allowobfuscation interface * {
#    @retrofit2.http.* <methods>;
#}
#-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
# Retrofit

# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
# RxJava RxAndroid

# Gson
-keep class com.google.gson.stream.** { *; }
-keepattributes EnclosingMethod
-keep class org.xz_sale.entity.**{*;}
# Gson

-keep class com.sky.**{*;}
-keep class common.**{*;}



-dontwarn com.google.**
-keep class com.google.** { *;}

-dontwarn github.chenupt.multiplemodel.**
-keep class github.chenupt.multiplemodel.** { *;}

-dontwarn javax.annotation.**
-dontwarn javax.inject.**