package com.wa.sdk.cn.demo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Process;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;

import com.wa.sdk.WAConstants;
import com.wa.sdk.apw.WAApwProxy;
import com.wa.sdk.cn.demo.base.BaseActivity;
import com.wa.sdk.cn.demo.channels.Channel360EventInfoUtil;
import com.wa.sdk.cn.demo.channels.ChannelBaiduUtil;
import com.wa.sdk.cn.demo.model.UserModel;
import com.wa.sdk.cn.demo.tracking.TrackingActivity;
import com.wa.sdk.cn.demo.widget.TitleBar;
import com.wa.sdk.common.WACommonProxy;
import com.wa.sdk.common.WASharedPrefHelper;
import com.wa.sdk.common.model.WACallback;
import com.wa.sdk.common.model.WAPermissionCallback;
import com.wa.sdk.common.model.WAResult;
import com.wa.sdk.common.utils.LogUtil;
import com.wa.sdk.common.utils.StringUtil;
import com.wa.sdk.core.WACoreProxy;
import com.wa.sdk.core.WASdkProperties;
import com.wa.sdk.csc.WACscProxy;
import com.wa.sdk.pay.WAPayProxy;
import com.wa.sdk.pay.model.WAChannelBalance;
import com.wa.sdk.user.WAUserProxy;
import com.wa.sdk.user.model.WACertificationInfo;
import com.wa.sdk.user.model.WALoginResult;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class MainActivity extends BaseActivity {
    private static final String TAG = LogUtil.TAG;

    private WASharedPrefHelper mSharedPrefHelper;
    private String serverId = "server1";

    private final WACallback<Boolean> mCallbackAgreementWindow = new WACallback<Boolean>() {
        @Override
        public void onSuccess(int code, String message, Boolean result) {
            LogUtil.d(TAG, "openPrivacyAgreementWindow onSuccess: "+code+","+message+","+result);
            showShortToast("同意协议，是否显示UI："+result);
            LogUtil.d(TAG, "同意协议，开始初始化。。。。WACoreProxy.initialize。。。");
            WACoreProxy.initialize(MainActivity.this);

            checkYSDKPerssion();
            initChannelLoginBtn();
        }

        @Override
        public void onCancel() {
            LogUtil.d(TAG, "openPrivacyAgreementWindow onCancel: ");
        }

        @Override
        public void onError(int code, String message, Boolean result, Throwable throwable) {
            LogUtil.d(TAG, "openPrivacyAgreementWindow onError: "+code+","+message+","+result);
            showShortToast("拒绝协议");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEnableToastLog = true;

        //开启调试模式
        WACoreProxy.setDebugMode(true);

        WACommonProxy.onCreate(this, savedInstanceState);


        WACommonProxy.enableLogcat(this);

        // Demo的初始化，跟SDK无关
        WASdkDemo.getInstance().initialize(this);

        mSharedPrefHelper = WASharedPrefHelper.newInstance(this, WADemoConfig.SP_CONFIG_FILE_DEMO);
        initView();

        showHashKey(this);

        if (WAConstants.CHANNEL_OPPO.equals(WAUserProxy.getCurrChannel())) {
            WACommonProxy.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, true,
                    "应用需要开启存储读写权限，是否开启？",
                    "应用需要开启存储读写权限,不开启将无法使用应用,请前往设置中开启存储权限",
                    new WAPermissionCallback() {
                        @Override
                        public void onCancel() {
                            Process.killProcess(Process.myPid());
                        }

                        @Override
                        public void onRequestPermissionResult(String[] permissions, boolean[] grantedResults) {
                            if (null == permissions || permissions.length == 0) {
                                Process.killProcess(Process.myPid());
                                return;
                            }
                            boolean forceExit = true;
                            for (int i = 0; i < permissions.length; i++) {
                                if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permissions[i]) && grantedResults[0]) {
                                    forceExit = false;
                                }
                            }
                            if (forceExit) {
                                Process.killProcess(Process.myPid());
                            }
                        }
                    });
        }

//        Button btnLogin = findViewById(R.id.btn_login);
//        login(WAConstants.CHANNEL_WA, btnLogin);
        WAUserProxy.openPrivacyAgreementWindow(this, mCallbackAgreementWindow);
    }

    private void checkYSDKPerssion() {
        if (WAConstants.CHANNEL_YSDK.equals(WAUserProxy.getCurrChannel())) {
            WACommonProxy.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE, true,
                    "应用需要开启通讯录及电话权限，是否开启？",
                    "开启通讯录及电话权限,不开启将无法使用应用,请前往设置中开启通讯录及电话权限",
                    new WAPermissionCallback() {
                        @Override
                        public void onCancel() {
                            //showLongToast("应用需要开启通讯录及电话权限,不开启将无法使用应用");
                        }

                        @Override
                        public void onRequestPermissionResult(String[] permissions, boolean[] grantedResults) {
                            if (null == permissions || permissions.length == 0) {
                                //showLongToast("应用需要开启通讯录及电话权限");
                                return;
                            }
                            boolean forceExit = true;
                            for (int i = 0; i < permissions.length; i++) {
                                if (Manifest.permission.READ_PHONE_STATE.equals(permissions[i]) && grantedResults[0]) {
                                    forceExit = false;
                                }
                            }
                            if (forceExit) {
                                showLongToast("应用需要开启通讯录及电话权限,不开启将无法使用应用,请前往设置中开启通讯录及电话权限");
                            }
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        WACommonProxy.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onStart() {
        super.onStart();
        WACommonProxy.onStart(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        WACommonProxy.onResume(this);


    }

    @Override
    protected void onPause() {
        super.onPause();
        WACommonProxy.onPause(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        WACommonProxy.onRestart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        WACommonProxy.onStop(this);
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
    public void onBackPressed() {
        exitGame();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        WACommonProxy.onActivityResult(this, requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
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

    /**
     * TODO 退出游戏
     */
    private void exitGame() {
        String platform = WAUserProxy.getCurrChannel();
        if (StringUtil.isEmpty(platform)) {
            finish();
            return;
        }
        WACoreProxy.exitGame(this, WAUserProxy.getCurrChannel(), new WACallback<WAResult>() {
            @Override
            public void onSuccess(int code, String message, WAResult result) {
                MainActivity.this.finish();
//                Process.killProcess(Process.myPid());
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login: // 登录（WA)
                login(WAConstants.CHANNEL_WA, v);
                break;
            case R.id.btn_switch_account: // 切换账号（WA）
                switchAccount(v);
                break;
            case R.id.btn_channel_login: // 登录（第三方渠道）
                login(WAUserProxy.getCurrChannel(), v);
                break;
            case R.id.btn_payment: // 支付
                startActivity(new Intent(this, PaymentActivity.class));
                break;
            case R.id.btn_query_channel_balance:
                queryChannelBalance();
                break;
            case R.id.btn_tracking: // 数据收集
                startActivity(new Intent(this, TrackingActivity.class));
                break;
            case R.id.btn_logout: // 登出
                if (WASdkDemo.getInstance().isLogin()) {
                    WASdkDemo.getInstance().logout();
                    WAUserProxy.logout(this);
                    showShortToast("退出登录成功");
                } else {
                    showShortToast("请先登录");
                }
                break;
            case R.id.btn_test_crash: // 闪退测试
                testCrash();
                break;
            case R.id.btn_hot_update: // 热更新
                startActivity(new Intent(this, HotUpdateActivity.class));
                break;
            case R.id.btn_csc:
                startActivity(new Intent(this, CscActivity.class));
                break;
            case R.id.btn_account_manage://账户管理
                startActivity(new Intent(this, AccountManagerActivity.class));
                break;
            case R.id.btn_open_real_name_auth://调起实名认证
                openRealNameAuthManager();
                break;
            case R.id.btn_random_clientid:
                String clientId = UUID.randomUUID().toString().replace("-", "");
                WASdkProperties.getInstance().setClientId(clientId);
                showShortToast("设置成功：\n" + clientId);
                break;
            case R.id.btn_account_deletion:
                startActivity(new Intent(MainActivity.this, UserDeletionActivity.class));
                break;
            case R.id.btn_agreement_window:
                WAUserProxy.openPrivacyAgreementWindow(this, mCallbackAgreementWindow);
                break;
            case R.id.btn_version:
                //app 信息
                String info ;
                String versionName = "版本名称："+BuildConfig.VERSION_NAME;
                String versionCode = "代码版本："+BuildConfig.VERSION_CODE;
                String buildType = "打包类型：" + BuildConfig.FLAVOR+"_"+BuildConfig.BUILD_TYPE;
                String buildTime = "打包时间：" + BuildConfig.DEMO_BUILD_TIME;
                String buildEnv = "HOST环境：" + BuildConfig.DEMO_BUILD_ENV;
                String isTestRepository = "是否测试仓库包：" + (BuildConfig.IS_TEST_REPOSITORY ? "是" : "否");
                String channel = "Channel：" + WAUserProxy.getCurrChannel();
                info = versionName + "\n"
                        + versionCode+ "\n"
                        + buildType+ "\n"
                        + buildEnv+ "\n"
                        + buildTime+ "\n"
                        + isTestRepository + "\n"
                        + channel+ "\n"
                ;
                new AlertDialog.Builder(this)
                        .setMessage(info)
                        .show();
                break;
            default:
                break;
        }
    }

    /**
     * 登录
     *
     * @param channel 渠道
     * @param button  按钮View
     */
    private void login(String channel, final View button) {
        WACscProxy.setSDKLanguage("zh_CN");
        button.setEnabled(false);

        checkYSDKPerssion();

        boolean enableLoginCache = mSharedPrefHelper.getBoolean(WADemoConfig.SP_KEY_ENABLE_LOGIN_CACHE, true);
        WAUserProxy.login(this, channel, new WACallback<WALoginResult>() {
            @Override
            public void onSuccess(int code, String message, WALoginResult result) {
                button.setEnabled(true);
                String text = "code:" + code + "\nmessage:" + message;
                String txServerId = serverId;
                String gameUserId = serverId + "-role1-" + result.getUserId();
                String nickname = "青铜" + serverId + "-" + result.getUserId();

                WACoreProxy.setGameUserId(gameUserId);
                WACoreProxy.setServerId(txServerId);
                WACoreProxy.setNickname(nickname);
                WACoreProxy.setLevel(10);
                LogUtil.i(TAG, "区服：" + txServerId + " ; 角色ID：" + gameUserId + " ; 角色名称：" + nickname);

                text = "Login success->" + text
                        + "\nplatform:" + result.getPlatform()
                        + "\nuserId:" + result.getUserId()
                        + "\ntoken:" + result.getToken()
                        + "\nplatformUserId:" + result.getPlatformUserId()
                        + "\nplatformToken:" + result.getPlatformToken()
                        + "\nisBindMobile: " + result.isBindMobile()
                        + "\nisFistLogin: " + result.isFirstLogin();

                LogUtil.i(LogUtil.TAG, text);
                showShortToast(text);
                WASdkDemo.getInstance().updateLoginAccount(result);
                //360
                if (WAUserProxy.getCurrChannel().equals(WAConstants.CHANNEL_QIHU360)) {
                    Channel360EventInfoUtil.getInstance().submitRoleData(MainActivity.this);
                }


                //百度
                if (WAUserProxy.getCurrChannel().equals(WAConstants.CHANNEL_BAIDU)) {
                    ChannelBaiduUtil.getInstance().setSessionInvalidListener(MainActivity.this);
                    ChannelBaiduUtil.getInstance().setSuspendWindowChangeAccountListener(MainActivity.this);
                }

                //华为
                if (WAUserProxy.getCurrChannel().equals(WAConstants.CHANNEL_HUAWEI)) {
                    WAUserProxy.queryLoginUserAuthenticateState(MainActivity.this, WAUserProxy.getCurrChannel(), new WACallback<Integer>() {
                        @Override
                        public void onSuccess(int code, String message, Integer result) {
                            LogUtil.d(TAG, "认证结果：" + result);
                        }

                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onError(int code, String message, Integer result, Throwable throwable) {

                        }
                    });
                }


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
                if (code == WACallback.CODE_ACCOUNT_IN_DELETION_BUFFER_DAYS) {
                    // 正在删除中的账号，会返回删除状态，删除时间，及UserID
                    WASdkDemo.getInstance().updateLoginAccount(result);
                    String message1 = "code:" + code
                            + "\nmessage:" + message
                            + "\nuserId:" + result.getUserId()
                            + "\ndeleteDate:" + result.getDeleteDate();
                    // 正在删除中的账号，会返回删除状态，删除时间，及UserID
                    WASdkDemo.getInstance().updateLoginAccount(result);
                    new AlertDialog.Builder(MainActivity.this).setMessage(message1).show();
                }
            }
        }, "{\"enableCache\":" + (enableLoginCache ? "true" : "false") + ", \"extInfo\":\"\"}");
    }

    /**
     * 切换账号
     *
     * @param button 按钮View
     */
    private void switchAccount(final View button) {
//         WASdkDemo.getInstance().clear();
        WAUserProxy.switchAccount(this, WAConstants.CHANNEL_WA, new WACallback<WALoginResult>() {
            @Override
            public void onSuccess(int code, String message, WALoginResult result) {
                WASdkDemo.getInstance().updateLoginAccount(result);
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

    private void queryChannelBalance() {
        if (!WASdkDemo.getInstance().isLogin()) {
            showShortToast("没有登录，请先登录");
            return;
        }
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("游戏币查询结果");
        dialogBuilder.setPositiveButton("确定", (dialogInterface, i) -> {

        });
        final String channel = WAUserProxy.getCurrChannel();
        if (WAConstants.CHANNEL_YSDK.equals(channel)) {
            WAPayProxy.queryChannelBalance(channel, new WACallback<WAChannelBalance>() {
                @Override
                public void onSuccess(int code, String message, WAChannelBalance result) {
                    dialogBuilder.setMessage("查询游戏币成功：\nchannel: " + result.getChannel()
                                    + "\nbalance(游戏币数): " + result.getBalance()
                                    + "\ngenBalance（赠送游戏币数）: " + result.getGenBalance()
                                    + "\nsaveAmount（累计充值游戏币数）: " + result.getSaveAmount()
                                    + "\nfistSave（第一次充值，1 是， 0 否）: " + result.getFirstSave()
                                    + "\nrawData（原始数据）: " + result.getRawData())
                            .show();
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(int code, String message, WAChannelBalance result, Throwable throwable) {
                    dialogBuilder.setMessage("查询游戏币失败：\ncode: " + code + "\nmessage: " + message)
                            .setNegativeButton("重新查询", (dialogInterface, i) -> queryChannelBalance())
                            .show();
                }
            });
        } else {
            dialogBuilder.setMessage("当前平台不支持查询平台游戏币").show();
        }
    }

    /**
     * 实名认证
     */
    private void openRealNameAuthManager() {
        if (!WAUserProxy.canOpenRealNameAuth(this)) {
            showLongToast("不能调起实名认证,请检查后台实名开关和是否登录,已经实名的用户不能调起");
            return;
        }
        WAUserProxy.openRealNameAuthManager(MainActivity.this, new WACallback<WACertificationInfo>() {
            @Override
            public void onSuccess(int code, String message, WACertificationInfo result) {
                showShortToast("实名认证成功，age：" + result.getAge());
            }

            @Override
            public void onCancel() {
                showShortToast("取消实名认证");
            }

            @Override
            public void onError(int code, String message, WACertificationInfo result, Throwable throwable) {
                showShortToast("实名认证失败，" + message);
            }
        });
    }

    /**
     * 闪退测试
     */
    private void testCrash() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.warming)
                .setMessage(R.string.test_crash_warming)
                .setPositiveButton(R.string.ok, (dialog, which) -> Util.testCrash())
                .setNegativeButton(R.string.cancel, (dialog, which) -> {

                })
                .show();
    }

    private void initView() {
        mContainerId = R.id.fl_container;

        TitleBar tb = findViewById(R.id.tb_main);
        tb.setRightButton(android.R.drawable.ic_menu_close_clear_cancel, v -> exitGame());
        tb.setTitleText(R.string.app_name);


        tb.setTitleTextColor(R.color.color_white);

        ToggleButton tbtnLogcat = findViewById(R.id.tbtn_logcat);
        boolean enableLogcat = mSharedPrefHelper.getBoolean(WADemoConfig.SP_KEY_ENABLE_LOGCAT, true);
        tbtnLogcat.setChecked(enableLogcat);
        tbtnLogcat.setOnCheckedChangeListener(mOnCheckedChangeListener);

        ToggleButton tbtnExtend = findViewById(R.id.tbtn_app_wall);
        boolean enableExtend = mSharedPrefHelper.getBoolean(WADemoConfig.SP_KEY_ENABLE_APW, true);
        tbtnExtend.setChecked(enableExtend);
        tbtnExtend.setOnCheckedChangeListener(mOnCheckedChangeListener);

        ToggleButton tbtnLoginCache = findViewById(R.id.tbtn_login_cache);
        boolean enableLoginCache = mSharedPrefHelper.getBoolean(WADemoConfig.SP_KEY_ENABLE_LOGIN_CACHE, true);
        tbtnLoginCache.setChecked(enableLoginCache);
        tbtnLoginCache.setOnCheckedChangeListener(mOnCheckedChangeListener);

        ToggleButton tbtnLoginFlowType = findViewById(R.id.tbtn_login_flow_type);
        boolean enableLoginFlowType = mSharedPrefHelper.getBoolean(WADemoConfig.SP_KEY_LOGIN_FLOW_TYPE, true);
        tbtnLoginFlowType.setChecked(enableLoginFlowType);
        tbtnLoginFlowType.setOnCheckedChangeListener(mOnCheckedChangeListener);

        ToggleButton tbtnFlowView = findViewById(R.id.tbtn_flow_view);
        boolean enableFlowView = mSharedPrefHelper.getBoolean(WADemoConfig.SP_KEY_ENABLE_FLOW_VIEW, true);
        tbtnFlowView.setChecked(enableFlowView);
        tbtnFlowView.setOnCheckedChangeListener(mOnCheckedChangeListener);

        if (mSharedPrefHelper.getBoolean(WADemoConfig.SP_KEY_ENABLE_LOGCAT, true)) {
            WACommonProxy.enableLogcat(this);
        }
        if (mSharedPrefHelper.getBoolean(WADemoConfig.SP_KEY_ENABLE_APW, true)) {
            WAApwProxy.showEntryFlowIcon(this);
        }

        final Button btnSwitchServerId = findViewById(R.id.btn_switch_serverId);
        btnSwitchServerId.setOnClickListener(v -> {
            if ("server1".equals(serverId)) {
                serverId = "server2";
                showShortToast("已切换为'2'服，重新登录后生效 ");
            } else {
                serverId = "server1";
                showShortToast("已切换为'1'服，重新登录后生效 ");
            }
        });

        final Button btn_create_real_name = findViewById(R.id.btn_create_real_name);
        btn_create_real_name.setOnClickListener(v -> {
//                WAEvent.Builder builder = new WAEvent.Builder();
//                builder.setDefaultEventName(WAEventType.USER_CREATED);
//                Map<String, Object> eventValues = new HashMap<>();
//                eventValues.put("nickname", "总裁女友");
//                eventValues.put("registerTime", System.currentTimeMillis());
//                eventValues.put("fighting", 1);
//                builder.setDefaultEventValues(eventValues);
//                WATrackProxy.trackEvent(MainActivity.this, builder.build());

            WAUserProxy.queryUserCertificationInfo(MainActivity.this, WAConstants.CHANNEL_WA, new WACallback<WACertificationInfo>() {
                @Override
                public void onSuccess(int code, String message, WACertificationInfo result) {
                    LogUtil.d(TAG, "Age:" + result.getAge() + ", UserRealNameStatus:" + result.getUserRealNameStatus());
                    showShortToast("Age:" + result.getAge() + ", UserRealNameStatus:" + result.getUserRealNameStatus());
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(int code, String message, WACertificationInfo result, Throwable throwable) {

                }
            });
        });


        final Button btnOpenClientId = findViewById(R.id.btn_open_clientid_test);
        btnOpenClientId.setOnClickListener(v -> MainActivity.this.startActivity(new Intent(MainActivity.this, ClientIdTestActivity.class)));

    }

    private void initChannelLoginBtn(){
        Button btnChannelLogin = findViewById(R.id.btn_channel_login);
        if (WAUserProxy.getCurrChannel().startsWith(WAConstants.CHANNEL_YSDK)) {
            btnChannelLogin.setText(R.string.ysdk_login);
        } else if (WAConstants.CHANNEL_OPPO.equals(WAUserProxy.getCurrChannel())) {
            btnChannelLogin.setText(R.string.oppo_login);
        } else if (WAConstants.CHANNEL_VIVO.equals(WAUserProxy.getCurrChannel())) {
            btnChannelLogin.setText(R.string.vivo_login);
        } else if (WAConstants.CHANNEL_XIAOMI.equals(WAUserProxy.getCurrChannel())) {
            btnChannelLogin.setText(R.string.xiaomi_login);
        } else if (WAConstants.CHANNEL_HUAWEI.equals(WAUserProxy.getCurrChannel())) {
            btnChannelLogin.setText(R.string.huawei_login);
        } else if (WAConstants.CHANNEL_QIHU360.equals(WAUserProxy.getCurrChannel())) {
            btnChannelLogin.setText(R.string.qihoo360_login);
        } else if (WAConstants.CHANNEL_BAIDU.equals(WAUserProxy.getCurrChannel())) {
            btnChannelLogin.setText(R.string.baidu_login);
        } else if (WAConstants.CHANNEL_UC.equals(WAUserProxy.getCurrChannel())) {
            btnChannelLogin.setText(R.string.uc_login);
        } else if (WAConstants.CHANNEL_MEIZU.equals(WAUserProxy.getCurrChannel())) {
            btnChannelLogin.setText(R.string.meizu_login);
        } else if (WAConstants.CHANNEL_QQ.equals(WAUserProxy.getCurrChannel())) {
            btnChannelLogin.setText(R.string.qq_login);
        } else {
            btnChannelLogin.setText("None");
            btnChannelLogin.setEnabled(false);
        }
    }

    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.tbtn_logcat:
                    mSharedPrefHelper.saveBoolean(WADemoConfig.SP_KEY_ENABLE_LOGCAT, isChecked);
                    if (isChecked) {
                        WACommonProxy.enableLogcat(MainActivity.this);
                    } else {
                        WACommonProxy.disableLogcat(MainActivity.this);
                    }
                    break;
                case R.id.tbtn_app_wall:
                    mSharedPrefHelper.saveBoolean(WADemoConfig.SP_KEY_ENABLE_APW, isChecked);
                    if (isChecked) {
                        WAApwProxy.showEntryFlowIcon(MainActivity.this);
                    } else {
                        WAApwProxy.hideEntryFlowIcon(MainActivity.this);
                    }
                    break;
                case R.id.tbtn_login_cache:
                    mSharedPrefHelper.saveBoolean(WADemoConfig.SP_KEY_ENABLE_LOGIN_CACHE, isChecked);
                    break;
                case R.id.tbtn_login_flow_type:
                    mSharedPrefHelper.saveBoolean(WADemoConfig.SP_KEY_LOGIN_FLOW_TYPE, isChecked);
                    if (isChecked)
                        WAUserProxy.setLoginFlowType(WAConstants.LOGIN_FLOW_TYPE_REBIND);
                    else
                        WAUserProxy.setLoginFlowType(WAConstants.LOGIN_FLOW_TYPE_DEFAULT);
                    break;
                case R.id.tbtn_flow_view:
                    mSharedPrefHelper.saveBoolean(WADemoConfig.SP_KEY_ENABLE_FLOW_VIEW, isChecked);
                    WACommonProxy.floatView(MainActivity.this, WAUserProxy.getCurrChannel(), isChecked);
                    break;
                default:
                    break;
            }

        }
    };
}
