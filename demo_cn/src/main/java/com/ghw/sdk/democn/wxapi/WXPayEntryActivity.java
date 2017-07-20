package com.ghw.sdk.democn.wxapi;

import android.os.Bundle;
import android.view.Window;

import com.wa.sdk.cn.demo.base.BaseActivity;
import com.wa.sdk.pay.WAPayProxy;

/**
 * 微信支付
 * Created by hank on 16/8/12.
 */
public class WXPayEntryActivity extends BaseActivity {
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);

        WAPayProxy.entryPayInit(this, getIntent());
        finish();
    }
}
