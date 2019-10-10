package com.wa.sdk.democn;

import android.content.Context;
import android.content.res.Configuration;

import com.wa.sdk.WAApplication;
import com.wa.sdk.WAIApplicationListener;
import com.wa.sdk.common.utils.LogUtil;

/**
 * Created by yinglovezhuzhu@gmail.com on 2017/11/29.
 */

public class DemoAppListener implements WAIApplicationListener {
    @Override
    public void onAppCreate(WAApplication app) {
        LogUtil.e("DemoCN", "Application onCreate()");
        // 需要在Application的onCreate中的操作，放在这里
    }

    @Override
    public void onAppAttachBaseContext(WAApplication app, Context base) {
        LogUtil.e("DemoCN", "onAppAttachBaseContext onCreate()");
        // 需要在Application的attachBaseContext中的操作，放在这里
    }

    @Override
    public void onAppConfigurationChanged(WAApplication app, Configuration newConfig) {
        LogUtil.e("DemoCN", "Application onAppConfigurationChanged()");
        // 需要在Application的onConfigurationChanged中的操作，放在这里
    }

    @Override
    public void onAppTerminate(WAApplication app) {
        LogUtil.e("DemoCN", "Application onAppTerminate()");
        // 需要在Application的onAppTerminate中的操作，放在这里
    }
}
