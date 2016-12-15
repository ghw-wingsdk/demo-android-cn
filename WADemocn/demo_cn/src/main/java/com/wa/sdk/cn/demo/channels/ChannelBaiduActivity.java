package com.wa.sdk.cn.demo.channels;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.widget.Button;

import com.wa.sdk.WAActivityAnalytics;
import com.wa.sdk.cn.demo.PaymentActivity;
import com.wa.sdk.cn.demo.R;
import com.wa.sdk.cn.demo.base.BaseActivity;
import com.wa.sdk.cn.demo.model.UserModel;
import com.wa.sdk.cn.demo.tracking.TrackingActivity;
import com.wa.sdk.cn.demo.widget.TitleBar;
import com.wa.sdk.common.WAActivityAdPage;
import com.wa.sdk.common.WACommonProxy;
import com.wa.sdk.common.model.WACallback;
import com.wa.sdk.common.model.WAResult;
import com.wa.sdk.core.WACoreProxy;
import com.wa.sdk.user.WAUserProxy;
import com.wa.sdk.user.model.WALoginResult;

/**
 * 360 渠道
 * Created by hank on 16/7/22.
 */
public class ChannelBaiduActivity extends BaseActivity {
    private TitleBar mTitlebar = null;

    private WAActivityAdPage activityAdPage = null;
    private WAActivityAnalytics activityAnalytics = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu);
        WACommonProxy.getAnnouncementInfo(this, WAUserProxy.getCurrChannel());

        // TODO 暂停页
        activityAdPage = WACommonProxy.getActivityAdPage(this, WAUserProxy.getCurrChannel(), new WACallback<WAResult>() {
            @Override
            public void onSuccess(int code, String message, WAResult result) {
                // TODO 关闭暂停页, CP可以让玩家继续游戏
                showShortToast("继续游戏");
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(int code, String message, WAResult result, Throwable throwable) {
                showShortToast(message);
            }
        });

        // TODO 统计
        activityAnalytics = WACommonProxy.getActivityAnalytics(this, WAUserProxy.getCurrChannel());

        initViews();
    }

    private void initViews() {
        mTitlebar = (TitleBar) findViewById(R.id.tb_baidu);
        mTitlebar.setTitleText(R.string.baidu);
        mTitlebar.setLeftButton(android.R.drawable.ic_menu_revert, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitlebar.setTitleTextColor(R.color.color_white);

    }

    @Override
    protected void onResume() {
        super.onResume();
        activityAdPage.onResume();
        activityAnalytics.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        activityAdPage.onPause();
        activityAnalytics.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        activityAdPage.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityAdPage.onDestroy();

        Button btnFloatView = (Button) findViewById(R.id.btn_float_view);
        if (btnFloatView.isSelected()) { // 如果打开了百度悬浮框 就需要关闭
            floatView();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login)
            login();
        else if (v.getId() == R.id.btn_pay)
            pay();
        else if (v.getId() == R.id.btn_float_view)
            floatView();
        else if (v.getId() == R.id.btn_query_authenticate_state)
            queryLoginUserAuthenticateState();
        else if (v.getId() == R.id.btn_tracking)
            tracking();
        else if (v.getId() == R.id.btn_logout)
            logout();
        else if (v.getId() == R.id.btn_exit_game)
            exitGame();
    }

    /** TODO 登录 */
    private void login() {
        WAUserProxy.login(this, WAUserProxy.getCurrChannel(), new WACallback<WALoginResult>() {
            @Override
            public void onSuccess(int code, String message, WALoginResult result) {
                UserModel.getInstance().setDatas(result);
                showShortToast(message);
                setSuspendWindowChangeAccountListener();
                setSessionInvalidListener();
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

    // TODO 设置会话失效监听
    private void setSessionInvalidListener() {
        WAUserProxy.setSessionInvalidListener(WAUserProxy.getCurrChannel(), new WACallback<WAResult>() {
            @Override
            public void onSuccess(int code, String message, WAResult result) {
                if (code == 0) {
                    showShortToast("会话失效, 请重新登录");
                    // 此处CP可调用登录接口
                    login();
                }
            }

            @Override
            public void onCancel() {}
            @Override
            public void onError(int code, String message, WAResult result, Throwable throwable) {}
        });
    }

    /** TODO 设置切换账号结果通知 */
    private void setSuspendWindowChangeAccountListener() {
        WAUserProxy.setSuspendWindowChangeAccountListener(WAUserProxy.getCurrChannel(), new WACallback<WAResult>() {
            @Override
            public void onSuccess(int code, String message, WAResult result) {
                // 登录成功,不管之前是什么登录状态,游戏内部都要切换成新的用户
            }

            @Override
            public void onCancel() {
                // 操作前后的登录状态没变化
            }

            @Override
            public void onError(int code, String message, WAResult result, Throwable throwable) {
                // 登录失败,游戏内部之前如果是已经登录的,要清楚自己记录的登录状态,设置成未登录。如果之前未登录,不用处理
            }
        });
    }

    /** TODO 查询登录用户实名认证状态 */
    private void queryLoginUserAuthenticateState() {
        WAUserProxy.queryLoginUserAuthenticateState(this,WAUserProxy.getCurrChannel(),new WACallback<Integer>() {
            @Override
            public void onSuccess (int code, String message, Integer result){
                // resut 0 无此用户数据; 1 未成年; 2 已成年
                String resultStr = "无此用户数据";
                if (result == 1)
                    resultStr = "未成年";
                else if (result == 2)
                    resultStr = "已成年";

                showShortToast(resultStr);
            }

            @Override
            public void onCancel () {}

            @Override
            public void onError (int code, String message, Integer result, Throwable throwable){
                showShortToast(message);
            }
        });
    }

    /**
     * TODO 统计数据
     */
    private void tracking() {
        Intent intent = new Intent(this, TrackingActivity.class);
        intent.putExtra("channel", 2);
        startActivity(intent);
    }

    /** TODO 显示/隐藏悬浮框 */
    private void floatView() {
        Button btnFloatView = (Button) findViewById(R.id.btn_float_view);
        WACommonProxy.floatView(this, WAUserProxy.getCurrChannel(), new Point(10, 10), !btnFloatView.isSelected());
        btnFloatView.setSelected(! btnFloatView.isSelected());

        btnFloatView.setText(getResources().getText(btnFloatView.isSelected() ? R.string.hidden_float_view : R.string.show_float_view));
    }

    /** TODO 退出账号 */
    private void logout() {
        UserModel.getInstance().clear();
        WAUserProxy.logout();
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
                showShortToast("取消游戏退出");
            }

            @Override
            public void onError(int code, String message, WAResult result, Throwable throwable) {
                showShortToast(message);
            }
        });
    }
}
