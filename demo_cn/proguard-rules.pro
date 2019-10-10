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
-dontwarn org.apache.**
-dontwarn org.apache.http.conn.ssl.SSLSocketFactory

-keep class com.squareup.** { *; }

-keep class com.taobao.android.dexposed.** { *; }

-keep class android.support.** { *; }

-keep class com.wa.sdk.** { *; }

# Activity、BroadCastReceiver、Service等需排除，否则配置将无法找到
-keep class * extends android.app.Activity {
    public *;
}
-keep class * extends android.content.BroadcastReceiver {
    public *;
}

# 排除微信SDK
-keep class com.tencent.mm.opensdk.* { *; }

# 百度360
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
-keep class com.coolcloud.uac.android.** {*;}
-keep class tv.cjump.jni.** {*;}
-keep class HttpUtils.** {*;}
-keep class com.a.a.** {*;}
-keep class com.emoji.** {*;}
-keep class com.google.android.exoplayer.** {*;}
-keep class com.ta.utdid2.** {*;}
-keep class com.ut.device.** {*;}
-keep class com.master.flame.danmaku.** {*;}

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

# VIVO
-dontwarn org.simalliance.openmobileapi.**
-dontwarn com.vivo.upgradelibrary.**
-dontwarn org.apache.**
-dontwarn android.net.**

-keep class com.alipay.** { *; }
-keep class com.ta.utdid2.** { *; }
-keep class com.ut.device.** { *; }
-keep class org.json.alipay.** { *; }
-keep class org.apache.james.mime4j.** { *; }
-keep class org.apache.http.entity.mime.** { *; }
-keep class com.huanju.data.** { *; }
-keep class com.tencent.** { *; }
-keep class com.tencent.mobileqq.openpay.** { *; }
-keep class android.net.** { *; }
-keep class com.android.internal.http.multipart.** { *; }
-keep class org.apache.** { *; }
-keep class com.unionpay.** { *; }
-keep class cn.gov.pbc.tsm.client.mobile.android.bank.service.** { *; }
-keep class com.UCMobile.PayPlugin.** { *; }
-keep class com.bbk.payment.** { *; }
-keep class com.bbkmobile.iqoo.payment.** { *; }
-keep class com.union.** { *; }
-keep class com.vivo.** { *; }

# OPPO
-keep class com.nearme.** { *; }

# 应用宝begin
#该代码混淆文件为YSDK对外提供个游戏开发者集成YSDK时使用
-optimizationpasses 5                   # 指定代码的压缩级别
-dontusemixedcaseclassnames             # 指定代码的压缩级别
-dontskipnonpubliclibraryclasses        # 是否混淆第三方jar
-dontpreverify                          # 混淆时是否做预校验
-dontoptimize
-ignorewarning                          # 忽略警告，避免打包时某些警告出现
-verbose                                # 混淆时是否记录日志
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*    # 混淆时所采用的算法

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}


-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepattributes InnerClasses
-keep class com.tencent.bugly.**{*;}
-keep class com.tencent.stat.**{*;}
-keep class com.tencent.smtt.**{*;}
-keep class com.tencent.beacon.**{*;}
-keep class com.tencent.mm.**{*;}
-keep class com.tencent.apkupdate.**{*;}
-keep class com.tencent.tmassistantsdk.**{*;}
-keep class org.apache.http.** { * ;}
-keep class com.qq.jce.**{*;}
-keep class com.qq.taf.**{*;}
-keep class com.tencent.connect.**{*;}
-keep class com.tencent.map.**{*;}
-keep class com.tencent.open.**{*;}
-keep class com.tencent.qqconnect.**{*;}
-keep class com.tencent.mobileqq.**{*;}
-keep class com.tencent.tauth.**{*;}
-keep class com.tencent.feedback.**{*;}
-keep class common.**{*;}
-keep class exceptionupload.**{*;}
-keep class mqq.**{*;}
-keep class qimei.**{*;}
-keep class strategy.**{*;}
-keep class userinfo.**{*;}
-keep class com.pay.**{*;}
-keep class com.demon.plugin.**{*;}
-keep class com.tencent.midas.**{*;}
-keep class oicq.wlogin_sdk.**{*;}
-keep class com.tencent.ysdk.**{*;}
-keepclasseswithmembernames class * {
    native <methods>;
}

-dontwarn java.nio.file.Files
-dontwarn java.nio.file.Path
-dontwarn java.nio.file.OpenOption
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement


