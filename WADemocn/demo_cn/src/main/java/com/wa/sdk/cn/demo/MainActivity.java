package com.wa.sdk.cn.demo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Process;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.wa.sdk.WAConstants;
import com.wa.sdk.appwall.WAApwProxy;
import com.wa.sdk.cn.demo.base.BaseActivity;
import com.wa.sdk.cn.demo.channels.Channel360Activity;
import com.wa.sdk.cn.demo.channels.ChannelBaiduActivity;
import com.wa.sdk.cn.demo.channels.ChannelUCActivity;
import com.wa.sdk.cn.demo.model.UserModel;
import com.wa.sdk.cn.demo.tracking.TrackingActivity;
import com.wa.sdk.cn.demo.widget.TitleBar;
import com.wa.sdk.common.WACommonProxy;
import com.wa.sdk.common.WASharedPrefHelper;
import com.wa.sdk.common.model.WACallback;
import com.wa.sdk.common.model.WAResult;
import com.wa.sdk.core.WACoreProxy;
import com.wa.sdk.track.WATrackProxy;
import com.wa.sdk.user.WAUserProxy;
import com.wa.sdk.user.model.WALoginResult;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    private WASharedPrefHelper mSharedPrefHelper;
    private UserModel userModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WACoreProxy.setDebugMode(true);

        WACoreProxy.initialize(this);
        WACoreProxy.setServerId("China");
        WACoreProxy.setLevel(10);
        WACoreProxy.setSDKType(WAConstants.WA_SDK_TYPE_CN);
        WACommonProxy.enableLogcat(this);
        WAUserProxy.setLoginFlowType(WAConstants.LOGIN_FLOW_TYPE_REBIND);

        // Demo的初始化，跟SDK无关
        WASdkDemo.getInstance().initialize(this);

        userModel = UserModel.getInstance();

        mSharedPrefHelper = WASharedPrefHelper.newInstance(this, WADemoConfig.SP_CONFIG_FILE_DEMO);
        initView();

        showHashKey(this);

//        String msg = "";
//        try {
//            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
//            String metaName = appInfo.metaData.getString("meta_name");
//            msg = "meta_name = " + metaName;
//        } catch (PackageManager.NameNotFoundException e) {
//            msg = "NameNotFoundException meta_name";
//        }
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
//        alertDialog.setMessage(msg);
//        alertDialog.show();
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    public void showHashKey(Context context) {

        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES); //Your            package name here

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "---onResume---");
        WATrackProxy.startHeartBeat(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "---onPause---");
        WATrackProxy.stopHeartBeat(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0
                && (WAConstants.CHANNEL_QIHU360.equals(WAUserProxy.getCurrChannel())
                || WAConstants.CHANNEL_BAIDU.equals(WAUserProxy.getCurrChannel())
                || WAConstants.CHANNEL_UC.equals(WAUserProxy.getCurrChannel()))) {

            exitGame();
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    /** TODO 退出游戏 */
    private void exitGame() {
        WACoreProxy.exitGame(this, WAUserProxy.getCurrChannel(), new WACallback<WAResult>() {
            @Override
            public void onSuccess(int code, String message, WAResult result) {
                MainActivity.this.finish();
                Process.killProcess(Process.myPid());
            }

            @Override
            public void onCancel() {
                showShortToast("exit game cancel");
            }

            @Override
            public void onError(int code, String message, WAResult result, Throwable throwable) {
                showShortToast(message);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WACoreProxy.destroy(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_360) {               // 360
            if (! WAConstants.CHANNEL_QIHU360.equals(WAUserProxy.getCurrChannel())) {
                showShortToast("不支持360渠道");
                return;
            }

            startActivity(new Intent(this, Channel360Activity.class));
        } else if (v.getId() == R.id.btn_baidu) {      // 百度
            if (! WAConstants.CHANNEL_BAIDU.equals(WAUserProxy.getCurrChannel())) {
                showShortToast("不支持百度渠道");
                return;
            }

            startActivity(new Intent(this, ChannelBaiduActivity.class));
        } else if (v.getId() == R.id.btn_uc) {         // UC
            if (! WAConstants.CHANNEL_UC.equals(WAUserProxy.getCurrChannel())) {
                showShortToast("不支持UC渠道");
                return;
            }

            startActivity(new Intent(this, ChannelUCActivity.class));
        } else
        if(v.getId() == R.id.btn_login) { // wa 登录
            login((Button) v);
        } else if(v.getId() == R.id.btn_payment) { // wa 支付
            startActivity(new Intent(this, PaymentActivity.class));
        }
        else if(v.getId() == R.id.btn_tracking) { // wa 统计
            startActivity(new Intent(this, TrackingActivity.class));
        }
        else if (v.getId() == R.id.btn_test_crash) { // 闪退测试
            testCrash();
        } else if (v.getId() == R.id.btn_hot_update) { // 热更新
            startActivity(new Intent(this, HotUpdateActivity.class));
        } else if (v.getId() == R.id.btn_switch_account) { // 切换账号
            userModel.clear();
            switchAccount((Button) v);
        } else if (v.getId() == R.id.btn_logout) {  // 登出
            if (userModel.isLogin()) {
                userModel.clear();
                WAUserProxy.logout();
                showShortToast("退出登录成功");
            } else {
                showShortToast("请先登录");
            }
        }
    }

    /** TODO WA 登录 */
    private void login(final Button button) {
        if (WAConstants.CHANNEL_QIHU360.equals(WAUserProxy.getCurrChannel())
            || WAConstants.CHANNEL_BAIDU.equals(WAUserProxy.getCurrChannel())
            || WAConstants.CHANNEL_UC.equals(WAUserProxy.getCurrChannel())) {
            showShortToast("不支持WA登录");
            return;
        }

        button.setEnabled(false);

        boolean enableLoginCache = mSharedPrefHelper.getBoolean(WADemoConfig.SP_KEY_ENABLE_LOGIN_CACHE, true);
        WAUserProxy.login(this, WAConstants.CHANNEL_WA, new WACallback<WALoginResult>() {
            @Override
            public void onSuccess(int code, String message, WALoginResult result) {
                userModel.setDatas(result);
                showShortToast(message);
                button.setEnabled(true);
            }

            @Override
            public void onCancel() {
                showShortToast("Login Cancel");
                button.setEnabled(true);
            }

            @Override
            public void onError(int code, String message, WALoginResult result, Throwable throwable) {
                showShortToast(message);
                button.setEnabled(true);
            }
        }, "{\"enableCache\":" + (enableLoginCache ? "true" : "false") + ", \"extInfo\":\"\"}");
    }

    /** TODO 切换账号 */
    private void switchAccount(final Button button) {
        if (WAConstants.CHANNEL_QIHU360.equals(WAUserProxy.getCurrChannel())
                || WAConstants.CHANNEL_BAIDU.equals(WAUserProxy.getCurrChannel())
                || WAConstants.CHANNEL_UC.equals(WAUserProxy.getCurrChannel())) {
            showShortToast("不支持WA登录");
            return;
        }

        WAUserProxy.switchAccount(this, WAConstants.CHANNEL_WA, new WACallback<WALoginResult>() {
            @Override
            public void onSuccess(int code, String message, WALoginResult result) {
                userModel.setDatas(result);
                showShortToast(message);
                button.setEnabled(true);
            }

            @Override
            public void onCancel() {
                showShortToast("Login Cancel");
                button.setEnabled(true);
            }

            @Override
            public void onError(int code, String message, WALoginResult result, Throwable throwable) {
                showShortToast(message);
                button.setEnabled(true);
            }
        });
    }

    /** TODO 闪退测试 */
    private void testCrash() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.warming)
                .setMessage(R.string.test_crash_warming)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Util.testCrash();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private void initView() {

        TitleBar tb = (TitleBar) findViewById(R.id.tb_main);
        tb.setRightButton(android.R.drawable.ic_menu_close_clear_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WAConstants.CHANNEL_QIHU360.equals(WAUserProxy.getCurrChannel())
                        || WAConstants.CHANNEL_BAIDU.equals(WAUserProxy.getCurrChannel())
                        || WAConstants.CHANNEL_UC.equals(WAUserProxy.getCurrChannel())) {
                    exitGame();
                } else {
                    finish();
                }
            }
        });
        tb.setTitleText(R.string.app_name);
        tb.setTitleTextColor(R.color.color_white);

        ToggleButton tbtnLogcat = (ToggleButton) findViewById(R.id.tbtn_logcat);
        boolean enableLogcat = mSharedPrefHelper.getBoolean(WADemoConfig.SP_KEY_ENABLE_LOGCAT, true);
        tbtnLogcat.setChecked(enableLogcat);
        tbtnLogcat.setOnCheckedChangeListener(mOnCheckedChangeListener);

        ToggleButton tbtnExtend = (ToggleButton) findViewById(R.id.tbtn_app_wall);
        boolean enableExtend = mSharedPrefHelper.getBoolean(WADemoConfig.SP_KEY_ENABLE_APW, true);
        tbtnExtend.setChecked(enableExtend);
        tbtnExtend.setOnCheckedChangeListener(mOnCheckedChangeListener);

        ToggleButton tbtnLoginCache = (ToggleButton) findViewById(R.id.tbtn_login_cache);
        boolean enableLoginCache = mSharedPrefHelper.getBoolean(WADemoConfig.SP_KEY_ENABLE_LOGIN_CACHE, true);
        tbtnLoginCache.setChecked(enableLoginCache);
        tbtnLoginCache.setOnCheckedChangeListener(mOnCheckedChangeListener);

        if (mSharedPrefHelper.getBoolean(WADemoConfig.SP_KEY_ENABLE_LOGCAT, true)) {
            WACommonProxy.enableLogcat(this);
        }
        if (mSharedPrefHelper.getBoolean(WADemoConfig.SP_KEY_ENABLE_APW, true)) {
            WAApwProxy.showEntryFlowIcon(this);
        }
    }

    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.tbtn_logcat:
                    mSharedPrefHelper.saveBoolean("enable_logcat", isChecked);
                    if (isChecked) {
                        WACommonProxy.enableLogcat(MainActivity.this);
                    } else {
                        WACommonProxy.disableLogcat(MainActivity.this);
                    }
                    break;
                case R.id.tbtn_app_wall:
                    mSharedPrefHelper.saveBoolean("enable_app_wall", isChecked);
                    if (isChecked) {
                        WAApwProxy.showEntryFlowIcon(MainActivity.this);
                    } else {
                        WAApwProxy.hideEntryFlowIcon(MainActivity.this);
                    }
                    break;
                case R.id.tbtn_login_cache:
                    mSharedPrefHelper.saveBoolean(WADemoConfig.SP_KEY_ENABLE_LOGIN_CACHE, isChecked);
                    break;
                default:
                    break;
            }

        }
    };
}
