package com.ddtankfcb.angame.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.wa.sdk.pay.WAPayProxy;
import com.wa.sdk.social.WASocialProxy;
import com.wa.sdk.user.WAUserProxy;

/**
 * WA 微信工具类
 */
public class WXEntryActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);

        WAUserProxy.entryLoginInit(this, getIntent());
        WASocialProxy.entryShareInit(this, getIntent());

        finish();
    }
}
