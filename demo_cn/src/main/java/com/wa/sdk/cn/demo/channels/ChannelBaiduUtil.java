package com.wa.sdk.cn.demo.channels;

import android.app.Activity;
import android.widget.Toast;

import com.wa.sdk.cn.demo.model.UserModel;
import com.wa.sdk.common.model.WACallback;
import com.wa.sdk.common.model.WAResult;
import com.wa.sdk.common.utils.LogUtil;
import com.wa.sdk.user.WAUserProxy;
import com.wa.sdk.user.model.WALoginResult;

/**
 * Desc:
 * Created by zgq on 2019/11/21.
 */
public class ChannelBaiduUtil {
    private static final ChannelBaiduUtil ourInstance = new ChannelBaiduUtil();

    public static ChannelBaiduUtil getInstance() {
        return ourInstance;
    }

    private ChannelBaiduUtil() {
    }

    /** TODO 设置切换账号结果通知 */
    public void setSuspendWindowChangeAccountListener(final Activity activity) {
        WAUserProxy.setSuspendWindowChangeAccountListener(activity,WAUserProxy.getCurrChannel(), new WACallback<WALoginResult>() {
            @Override
            public void onSuccess(int code, String message, WALoginResult result) {
                // 登录成功,不管之前是什么登录状态,游戏内部都要切换成新的用户
                LogUtil.e(LogUtil.TAG, "百度--账号切换成功");
                UserModel.getInstance().setDatas(result);
                setSessionInvalidListener(activity);
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

                showShortToast(activity,text);
                LogUtil.i(LogUtil.TAG, text);
            }

            @Override
            public void onCancel() {
                // 操作前后的登录状态没变化
                LogUtil.e(LogUtil.TAG, "百度--账号切换取消");
            }

            @Override
            public void onError(int code, String message, WALoginResult result, Throwable throwable) {
                // 登录失败,游戏内部之前如果是已经登录的,要清楚自己记录的登录状态,设置成未登录。如果之前未登录,不用处理
                LogUtil.e(LogUtil.TAG, "百度--账号切换失败");
            }
        });
    }

    // TODO 设置会话失效监听
    public void setSessionInvalidListener(final Activity activity) {
        WAUserProxy.setSessionInvalidListener(WAUserProxy.getCurrChannel(), new WACallback<WAResult>() {
            @Override
            public void onSuccess(int code, String message, WAResult result) {
                if (code == 0) {
                    showShortToast(activity,"会话失效, 请重新登录");
                    // 此处CP可调用登录接口
                    //login();
                }
            }

            @Override
            public void onCancel() {}
            @Override
            public void onError(int code, String message, WAResult result, Throwable throwable) {}
        });
    }

    /**
     * 显示一个短Toast
     * @param text
     */
    private void showShortToast(Activity activity,CharSequence text) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
    }



}
