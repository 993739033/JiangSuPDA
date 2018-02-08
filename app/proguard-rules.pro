-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# For retrolambda
-dontwarn java.lang.invoke.*
#greendao
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties
-dontwarn org.greenrobot.greendao.database.**
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#rocoofix 热修复
-keep class com.dodola.rocoofix.** {*;}
-keep class com.lody.legend.** {*;}

# Glide图片库的混淆处理
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# OkHttp3
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
#okio
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**
# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

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

# Gson
-keep class com.google.gson.** {*;}
#-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.** {
    <fields>;
    <methods>;
}


#butterknife
#报错的包路径
-dontwarn butterknife.**
# 报错的包路径
-keep class butterknife.** { *;}

#高德地图定位
-dontwarn com.amap.**
-dontwarn com.autonavi.aps.amapapi.model.**
-keep class com.amap.** { *;}
-keep class com.autonavi.aps.amapapi.model.** { *;}

#Glide
-dontwarn com.bumptech.glide.**
-keep class com.bumptech.glide.** { *;}

#打印机驱动
-dontwarn com.epson.isv.eprinterdriver.**
-keep class com.epson.isv.eprinterdriver.** { *;}

#打印机类库
-dontwarn jp.co.canon.android.**
-keep class jp.co.canon.android.** { *;}

#zxing
-dontwarn com.google.zxing.**
-keep class com.google.zxing.** { *;}

#nineoldandroids 动画
-dontwarn com.nineoldandroids.**
-keep class com.nineoldandroids.** { *;}

#javax
-dontwarn javax.annotation.**
-keep class javax.annotation.** { *;}

#okhttp3
-dontwarn okhttp3.**
-keep class okhttp3.** { *;}
#commons-cli-1.2
-dontwarn org.apache.commons.cli.**
-keep class org.apache.commons.cli.** { *;}

#greendao
-dontwarn org.greenrobot.greendao.**
-keep class org.greenrobot.greendao.** { *;}
#rx
-dontwarn rx.**
-keep class rx.** { *;}
-keep class com.wyw.jiangsu.db.CodeXD{*;}
-keep class com.wyw.jiangsu.db.Unit{*;}
-keep class com.wyw.jiangsu.db.User{*;}

#okhttpdownloader
-dontwarn com.squareup.picasso.**
-keep class com.squareup.picasso.**{*;}

#bottombar
-dontwarn com.roughike.bottombar.**
-keep class com.roughike.bottombar.**{*;}

#保证实体类不能被混淆
-keep class com.wyw.jiangsu.bean.** { *;}
-keep class com.wyw.jiangsu.db.** {*;}

-dontwarn com.zhy.m.permission.**
-keep class com.zhy.m.permission.**{*;}
-keep class **$$PermissionProxy { *; }
-keep class android.Manifest{*;}
#-keepnames class com.wyw.jiangsu.activity.LoginActivity$*{
#    public <fields>;
#    public <methods>;
#}
-dontwarn io.codetail.**
-dontwarn zxing.decoding.**
-keep class io.codetail.**{*;}
-keep class zxing.decoding.**{*;}
-keep class zxing.decoding.CaptureActivityHandler$*{*;}

-keep class cn.finalteam.okhttpfinal.**{*;}
-keep class okio.*{*;}
-keep class cn.finalteam.toolsfinal.*{*;}
-keep class com.mingnong.scanappnew.bean.*{*;}
# -------------系统类不需要混淆 --------------------------
-keep public class * extends Android.app.Fragment
-keep public class * extends Android.app.Activity
-keep public class * extends Android.app.Application
-keep public class * extends Android.app.Service
-keep public class * extends Android.content.BroadcastReceiver
-keep public class * extends Android.content.ContentProvider
-keep public class * extends Android.app.backup.BackupAgentHelper
-keep public class * extends Android.preference.Preference
-keep public class * extends Android.support.**
-keep public class com.Android.vending.licensing.ILicensingService
-keepclasseswithmembernames class * { # 保持native方法不被混淆
    native <methods>;
}
-keepclasseswithmembernames class * { # 保持自定义控件不被混淆
    public <init>(Android.content.Context, Android.util.AttributeSet);
}
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
-keepclasseswithmembernames class * { # 保持自定义控件不被混淆
    public <init>(Android.content.Context, Android.util.AttributeSet, int);
}
-keepclassmembers enum * { # 保持枚举enum类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements Android.os.Parcelable { # 保持Parcelable不被混淆
  public static final Android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

