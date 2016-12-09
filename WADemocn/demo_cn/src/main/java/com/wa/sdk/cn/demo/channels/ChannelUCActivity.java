package com.wa.sdk.cn.demo.channels;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.widget.Button;

import com.wa.sdk.cn.demo.PaymentActivity;
import com.wa.sdk.cn.demo.R;
import com.wa.sdk.cn.demo.base.BaseActivity;
import com.wa.sdk.cn.demo.model.UserModel;
import com.wa.sdk.cn.demo.widget.TitleBar;
import com.wa.sdk.common.WACommonProxy;
import com.wa.sdk.common.model.WACallback;
import com.wa.sdk.common.model.WAResult;
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

        // TODO 创建悬浮窗
        WACommonProxy.createFloatButton(this, WAUserProxy.getCurrChannel());

        initViews();
    }

    private void initViews() {
        mTitlebar = (TitleBar) findViewById(R.id.tb_uc);
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
    protected void onDestroy() {
        super.onDestroy();
        // TODO 销毁悬浮窗
        WACommonProxy.destoryFloatButton(this, WAUserProxy.getCurrChannel());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login)
            login();
        else if (v.getId() == R.id.btn_pay)
            pay();
        else if (v.getId() == R.id.btn_float_view)
            showOrHiddenFloatView();
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
                showShortToast(message);

                setSessionInvalidListener();

                // TODO 游戏中为用户成功登录游戏角色时,提交该“游戏登录角色”数据 loginGameRole (必接)
                long time = Long.parseLong(String.valueOf(new Date().getTime()).substring(0, 10));
                HashMap<String, Object> dataMap = new HashMap();
                dataMap.put("roleId", "654321");       // String 必填 角色 ID
                dataMap.put("roleName", "角色昵称");    // String 必填 角色昵称
                dataMap.put("zoneId", "123456");       // String 必填 区服 ID
                dataMap.put("zoneName", "区服名称");    // String 必填 区服名称
                dataMap.put("roleCTime", time);        // long   必填 角色创建时间(单 位:秒)
                dataMap.put("roleLevel", "11");        // String 必填 角色等级
                dataMap.put("roleLevelMTime", time);   // long   可选 角色等级变化时 间(单位:秒)

                WAUserProxy.submitExtendData(WAUserProxy.getCurrChannel(), "loginGameRole", dataMap);

                // TODO 用户个人信息 userInfo (可选)
                dataMap = new HashMap();
                dataMap.put("guildId", "123456");      // String 必填 用户加入的公会的ID
                dataMap.put("guildName", "公会名称");   // String 必填 用户加入的公会的名称
                dataMap.put("guildLevel", 10);         // int    必填 用户加入的公会的等级
                dataMap.put("guildLeader", "654321");  // String 必填 用户加入的公会的会长的账号ID
                dataMap.put("zoneId", "654321");       // String 必填 区服 ID
                dataMap.put("zoneName", "公会名称");    // String 必填 区服名称
                dataMap.put("power", 123456789);       // int    必填 个人战力值

                WAUserProxy.submitExtendData(WAUserProxy.getCurrChannel(), "userInfo", dataMap);
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

    /**
     * TODO 显示隐藏悬浮窗
     */
    private void showOrHiddenFloatView() {
        Button button = (Button) findViewById(R.id.btn_float_view);

        WACommonProxy.floatView(this, WAUserProxy.getCurrChannel(), new Point(0, 0), !button.isSelected());
        button.setSelected(! button.isSelected());
        button.setText(getResources().getText(button.isSelected() ? R.string.hidden_float_view : R.string.show_float_view));
    }

    // TODO 设置退出账号侦听器
    private void setSessionInvalidListener() {
        WAUserProxy.setSessionInvalidListener(WAUserProxy.getCurrChannel(),new WACallback<WAResult>() {
            @Override
            public void onSuccess ( int code, String message, WAResult result){
                showShortToast("UC 账号已退出");
            }

            @Override
            public void onCancel () {

            }

            @Override
            public void onError ( int code, String message, WAResult result, Throwable throwable){

            }
        });
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
                showShortToast("取消退出游戏");
            }

            @Override
            public void onError(int code, String message, WAResult result, Throwable throwable) {
                showShortToast(message);
            }
        });
    }

}
