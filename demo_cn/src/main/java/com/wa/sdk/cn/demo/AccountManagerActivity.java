package com.wa.sdk.cn.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wa.sdk.cn.demo.base.BaseActivity;
import com.wa.sdk.common.WACommonProxy;
import com.wa.sdk.common.utils.LogUtil;
import com.wa.sdk.core.WACoreProxy;
import com.wa.sdk.user.WAUserProxy;
import com.wa.sdk.user.model.WAAccountCallback;
import com.wa.sdk.user.model.WABindResult;
import com.wa.sdk.user.model.WALoginResult;


public class AccountManagerActivity extends BaseActivity {


    private static final String TAG = AccountManagerActivity.class.getSimpleName();
    private Button btn_open_account_bind;
    private Button btnOpenAccountCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manager);

        initView();

    }

    private void initView() {
        btn_open_account_bind = findViewById(R.id.btn_open_account_bind);
        btn_open_account_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WAUserProxy.openAccountManager(AccountManagerActivity.this, new WAAccountCallback() {
                    @Override
                    public void onLoginAccountChanged(WALoginResult currentAccount) {
                        LogUtil.d(TAG, "WALoginResult:" + currentAccount);
                    }

                    @Override
                    public void onBoundAccountChanged(boolean binding, WABindResult result) {
                        LogUtil.d(TAG, "binding:" + binding + "WABindResult:" + result.toString());
                    }
                });
            }
        });

        btnOpenAccountCenter = findViewById(R.id.btn_open_account_center);
        btnOpenAccountCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WAUserProxy.openAccountCenter(AccountManagerActivity.this, new WAAccountCallback() {
                    @Override
                    public void onLoginAccountChanged(WALoginResult result) {
                        String text = "code:" + result.getCode() + "\nmessage:" + result.getMessage();
                        if (null == result) {
                            text = "Login failed->" + text;
                        } else {
                            WACoreProxy.setGameUserId("gUid01");
                            text = "Login success->" + text
                                    + "\nplatform:" + result.getPlatform()
                                    + "\nuserId:" + result.getUserId()
                                    + "\ntoken:" + result.getToken()
                                    + "\nplatformUserId:" + result.getPlatformUserId()
                                    + "\nplatformToken:" + result.getPlatformToken()
                                    + "\nisBindMobile: " + result.isBindMobile()
                                    + "\nisFistLogin: " + result.isFirstLogin();
                        }
                        LogUtil.i(LogUtil.TAG, text);
                        showShortToast(text);
                    }

                    @Override
                    public void onBoundAccountChanged(boolean binding, WABindResult result) {
                        LogUtil.i(TAG, "binding:" + binding + "AccountCenter-WABindResult:" + result.toString());
                        String text = "platform:" + result.getPlatform() +
                                "\n" + "AccessToken:" + result.getAccessToken() +
                                "\n" + "PlatformUserId:" + result.getPlatformUserId();
                        showShortToast(text);
                    }
                });
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        WACommonProxy.onActivityResult(this, requestCode, resultCode, data);//CP需要添加
        super.onActivityResult(requestCode, resultCode, data);
    }
}
