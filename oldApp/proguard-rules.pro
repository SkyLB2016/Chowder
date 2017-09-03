# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\development\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-optimizationpasses 5                                                           # 指定代码的压缩级别
-dontusemixedcaseclassnames                                                    # 是否使用大小写混合,包明不混合大小写
-dontskipnonpubliclibraryclasses                                              # 是否混淆第三方jar
-dontpreverify                                                                  # 混淆时是否做预校验
-verbose                                                                         # 混淆时是否记录日志
-keepattributes *Annotation*                                                    #保护注解
#-dontoptimize                                                                   #不优化输入的类文件
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*        # 混淆时所采用的算法

-keep public class * extends android.app.Activity                               # 保持哪些类不被混淆
-keep public class * extends android.app.Application                            # 保持哪些类不被混淆
-keep public class * extends android.app.Service                                # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver                  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider                    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper               # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference                      # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService              # 保持哪些类不被混淆
#如果有引用v4包
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.app.Fragment
-keep class com.sky.demo.Models.** { *; }                                       # 保持哪些类不被混淆
-keep public class com.sky.demo.R$*{ public static final int *; }               #不混淆资源类
-keep class * implements java.io.Serializable {*;}                              #保护serializable类不被混淆

-keepclassmembers class * {                                                    #保护指定类的成员，如果此类受到保护他们会保护的更好
    public <init>(org.json.JSONObject);
    void *(android.view.View);
    public void *(android.view.View);
    private void *(android.view.View);
    protected void *(android.view.View);
}
-keepclasseswithmembernames class * { native <methods>; }  # 保持 native 方法不被混淆
-keepclasseswithmembers class * { public <init>(android.content.Context, android.util.AttributeSet); }      # 保持自定义控件类不被混淆
-keepclasseswithmembers class * { public <init>(android.content.Context, android.util.AttributeSet, int); } # 保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity { public void *(android.view.View); }                 # 保持自定义控件类不被混淆
-keepclassmembers enum * {                                                                                   # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {      # 保持 Parcelable 不被混淆
  public static final android.os.Parcelable$Creator *;
}
-keepattributes Signature  #避免混淆泛型 如果混淆报错建议关掉

#第三方jar保留
-keep class org.apache.http.**  {*;}
-dontwarn org.apache.http.**

# OkHttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**

# roboguice
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**