# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
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

-keepattributes SourceFile,LineNumberTable

-dontwarn com.squareup.**
-dontwarn android.support.**

# 360
-dontwarn cn.pp.**
-dontwarn com.alipay.android.app.**
-dontwarn com.qihoo.**
-dontwarn com.qihoo360.**
-dontwarn com.qihoopp.**
-dontwarn com.yeepay.safekeyboard.**
-dontwarn com.amap.**
-dontwarn org.apache.http.conn.ssl.SSLSocketFactory

# 百度
-dontwarn com.baidu.**
-dontwarn com.alipay.**
-dontwarn com.duoku.**
-dontwarn android.content.pm.**
-dontwarn com.slidingmenu.**
-dontwarn com.squareup.okhttp.**
-dontwarn okio.**
-dontwarn com.unionpay.**
-dontwarn com.UCMobile.**
-dontwarn com.ta.**
-dontwarn com.ut.**
-dontwarn android.net.**
-dontwarn com.android.internal.**
-dontwarn org.apache.**

# UC
-dontwarn android.**
-dontwarn cn.uc.**
-dontwarn com.alipay.**
-dontwarn com.ta.**
-dontwarn com.ut.**
-dontwarn org.json.**
-dontwarn org.apache.http.conn.ssl.SSLSocketFactory

-keep class com.squareup.** { *; }
-keep class com.taobao.android.dexposed.** { *; }
-keep class android.support.** { *; }
# WingA
-keep class com.wa.sdk.** { *; }

# Activity、BroadCastReceiver、Service等需排除，否则配置将无法找到
-keep class * extends android.app.Activity {
    public *;
}
-keep class * extends android.content.BroadcastReceiver {
    public *;
}

# 百度360
-keep class a.a.a.** { *; }
-keep class cn.pp.** { *; }
-keep class com.alipay.** {*;}
-keep class com.qihoo.** {*;}
-keep class com.qihoo360.** { *; }
-keep class com.qihoopp.** { *; }
-keep class com.yeepay.safekeyboard.** { *; }
-keep class com.amap.** {*;}
-keep class com.aps.** {*;}
-keep class com.iapppay.** {*;}
-keep class com.ipaynow.** {*;}
-keep class com.junnet.heepay.** {*;}
-keep class com.tencent.mm.** {*;}
-keep class com.ta.utdid2.** {*;}
-keep class com.ut.device.** {*;}
-keep class com.qihoo.sdkplugging.host.** {*;}
-keep public class com.qihoo.gamecenter.sdk.matrix.PluggingHostProxy {*;}

# 百度
-keep public class com.baidu.** { *; }
-keep public class com.alipay.** { *; }
-keep public class com.duoku.** { *; }
-keep public class android.content.pm.** { *; }
-keep public class com.squareup.okhttp.** { *; }
-keep public class okio.** { *; }
-keep public class com.slidingmenu.** { *; }
-keep public class com.unionpay.** { *; }
-keep public class com.UCMobile.** { *; }
-keep public class com.ta.** { *; }
-keep public class com.ut.** { *; }
-keep public class android.net.** { *; }
-keep public class com.android.internal.** { *; }
-keep public class org.apache.** { *; }

# UC
-keep class android.**{
<methods>;
<fields>;
 }
-keep class cn.uc.**{
<methods>;
<fields>;
}
-keep class com.alipay.**{
<methods>;
<fields>;
}
-keep class com.ta.**{
<methods>;
<fields>;
}
-keep class com.ut.**{
<methods>;
<fields>;
}
-keep class org.json.**{
<methods>;
<fields>;
}

-keep class **.R$* { *; }
