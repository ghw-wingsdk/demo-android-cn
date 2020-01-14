package com.wa.sdk.cn.demo.channels;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wa.sdk.WAConstants;
import com.wa.sdk.cn.demo.PaymentActivity;
import com.wa.sdk.cn.demo.R;
import com.wa.sdk.cn.demo.base.BaseActivity;
import com.wa.sdk.cn.demo.model.UserModel;
import com.wa.sdk.cn.demo.widget.TitleBar;
import com.wa.sdk.common.WACommonProxy;
import com.wa.sdk.common.model.WACallback;
import com.wa.sdk.common.utils.LogUtil;
import com.wa.sdk.core.WACoreProxy;
import com.wa.sdk.user.WAUserProxy;
import com.wa.sdk.user.model.WALoginResult;

/**
 * 应用宝 渠道
 * Created by hank on 2017/11/16.
 */

public class ChannelYSDKActivity extends BaseActivity {
    private TitleBar mTitlebar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ysdk);

//        WACommonProxy.onCreate(this, savedInstanceState);

        initViews();
    }

    private void initViews() {
        mTitlebar = findViewById(R.id.tb_baidu);
        if (WAConstants.CHANNEL_YSDK.equals(WAUserProxy.getCurrChannel()))
            mTitlebar.setTitleText(R.string.ysdk_login);
        else if (WAConstants.CHANNEL_OPPO.equals(WAUserProxy.getCurrChannel()))
            mTitlebar.setTitleText(R.string.oppo_login);
        else if (WAConstants.CHANNEL_VIVO.equals(WAUserProxy.getCurrChannel()))
            mTitlebar.setTitleText(R.string.vivo_login);
        else if (WAConstants.CHANNEL_XIAOMI.equals(WAUserProxy.getCurrChannel()))
            mTitlebar.setTitleText(R.string.xiaomi_login);
        else if (WAConstants.CHANNEL_HUAWEI.equals(WAUserProxy.getCurrChannel()))
            mTitlebar.setTitleText(R.string.huawei_login);

        mTitlebar.setLeftButton(android.R.drawable.ic_menu_revert, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitlebar.setTitleTextColor(R.color.color_white);

        if (WAConstants.CHANNEL_HUAWEI.equals(WAUserProxy.getCurrChannel())) {
            Button btnFloatView = findViewById(R.id.btn_float_view);
            btnFloatView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        WACommonProxy.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        WACommonProxy.onPause(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        WACommonProxy.onRestart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        WACommonProxy.onStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        WACommonProxy.onDestroy(this);
        WACoreProxy.destroy(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        WACommonProxy.onNewIntent(this, intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        WACommonProxy.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login)
            login();
        else if (v.getId() == R.id.btn_pay)
            pay();
        else if (v.getId() == R.id.btn_logout)
            logout();
        else if (v.getId() == R.id.btn_float_view)
            floatView();
    }

    /** TODO 登录 */
    private void login() {
        WAUserProxy.login(this, WAUserProxy.getCurrChannel(), new WACallback<WALoginResult>() {
            @Override
            public void onSuccess(int code, String message, WALoginResult result) {
                UserModel.getInstance().setDatas(result);
                String text = "code:" + code + "\nmessage:" + message;
                if (null == result) {
                    text = "Login failed->" + text;
                } else {
                    text = "Login success->" + text
                            + "\nplatform:" + result.getPlatform()
                            + "\nuserId:" + result.getUserId()
                            + "\ntoken:" + result.getToken()
                            + "\nplatformUserId:" + result.getPlatformUserId()
                            + "\nplatformToken:" + result.getPlatformToken()
                            + "\nisBindMobile: " + result.isBindMobile()
                            + "\nisFistLogin: " + result.isFirstLogin();
                }

                showShortToast(text);
                LogUtil.i(LogUtil.TAG, text);
            }

            @Override
            public void onCancel() {
                showShortToast("登录取消");
            }

            @Override
            public void onError(int code, String message, WALoginResult result, Throwable throwable) {
                showShortToast(message);
            }
        }, "");
    }

    /**
     * TODO 支付
     */
    private void pay() {
        startActivity(new Intent(this, PaymentActivity.class));
    }

    /** TODO 退出账号 */
    private void logout() {
        UserModel.getInstance().clear();
        WAUserProxy.logout(this);
    }

    /** TODO 显示/隐藏悬浮框 */
    private void floatView() {
        Button btnFloatView = findViewById(R.id.btn_float_view);
        WACommonProxy.floatView(this, WAUserProxy.getCurrChannel(), !btnFloatView.isSelected());
        btnFloatView.setSelected(! btnFloatView.isSelected());

        btnFloatView.setText(getResources().getText(btnFloatView.isSelected() ? R.string.hidden_float_view : R.string.show_float_view));
    }
}
