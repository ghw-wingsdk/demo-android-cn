package com.wa.sdk.cn.demo.channels;

import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.View;

import com.wa.sdk.cn.demo.PaymentActivity;
import com.wa.sdk.cn.demo.R;
import com.wa.sdk.cn.demo.base.BaseActivity;
import com.wa.sdk.cn.demo.model.UserModel;
import com.wa.sdk.cn.demo.widget.TitleBar;
import com.wa.sdk.common.model.WACallback;
import com.wa.sdk.common.model.WAResult;
import com.wa.sdk.common.utils.LogUtil;
import com.wa.sdk.core.WACoreProxy;
import com.wa.sdk.user.WAUserProxy;
import com.wa.sdk.user.model.WALoginResult;

import java.util.Date;
import java.util.HashMap;

/**
 * 360 渠道
 * Created by hank on 16/7/22.
 */
public class ChannelUCActivity extends BaseActivity {
    private TitleBar mTitlebar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uc);

        initViews();
    }

    private void initViews() {
        mTitlebar = findViewById(R.id.tb_uc);
        mTitlebar.setTitleText(R.string.uc);
        mTitlebar.setLeftButton(android.R.drawable.ic_menu_revert, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitlebar.setTitleTextColor(R.color.color_white);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login)
            login();
        else if (v.getId() == R.id.btn_pay)
            pay();
        else if (v.getId() == R.id.btn_logout)
            logout();
        else if (v.getId() == R.id.btn_exit_game)
            exitGame();
    }

    /**
     * TODO 登录
     */
    private void login() {
        WAUserProxy.login(this, WAUserProxy.getCurrChannel(), new WACallback<WALoginResult>() {
            @Override
            public void onSuccess(int code, String message, WALoginResult result) {
                UserModel.getInstance().setDatas(result);

                // TODO 提交游戏角色数据信息 (必接)
                long time = Long.parseLong(String.valueOf(new Date().getTime()).substring(0, 10));
                HashMap<String, Object> dataMap = new HashMap();
                dataMap.put("roleId", "654321");       // String 必填 角色 ID
                dataMap.put("roleName", "角色昵称");    // String 必填 角色昵称
                dataMap.put("roleLevel", 11l);          // 角色等级
                dataMap.put("roleCTime", time);        // long   必填 角色创建时间(单 位:秒) 10位
                dataMap.put("zoneId", "123456");       // String 必填 区服 ID
                dataMap.put("zoneName", "区服名称");    // String 必填 区服名称

                WAUserProxy.submitRoleData(WAUserProxy.getCurrChannel(), ChannelUCActivity.this, dataMap);
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
                showShortToast("登录失败");
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

    /** TODO 退出游戏 */
    private void exitGame() {
        WACoreProxy.exitGame(this, WAUserProxy.getCurrChannel(), new WACallback<WAResult>() {
            @Override
            public void onSuccess(int code, String message, WAResult result) {
                activityManager.finishAll();
                Process.killProcess(Process.myPid());
            }

            @Override
            public void onCancel() {
                showShortToast("取消退出游戏");
            }

            @Override
            public void onError(int code, String message, WAResult result, Throwable throwable) {
                showShortToast(message);
            }
        });
    }

}
