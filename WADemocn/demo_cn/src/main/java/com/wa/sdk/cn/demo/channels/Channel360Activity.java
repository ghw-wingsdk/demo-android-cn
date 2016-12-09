package com.wa.sdk.cn.demo.channels;

import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.view.View;

import com.wa.sdk.WAConstants;
import com.wa.sdk.cn.demo.PaymentActivity;
import com.wa.sdk.cn.demo.R;
import com.wa.sdk.cn.demo.base.BaseActivity;
import com.wa.sdk.cn.demo.model.UserModel;
import com.wa.sdk.cn.demo.tracking.TrackingActivity;
import com.wa.sdk.cn.demo.widget.TitleBar;
import com.wa.sdk.common.model.WACallback;
import com.wa.sdk.common.model.WAResult;
import com.wa.sdk.core.WACoreProxy;
import com.wa.sdk.social.WASocialProxy;
import com.wa.sdk.social.model.WAFriendsResult;
import com.wa.sdk.social.model.WARequestSendResult;
import com.wa.sdk.social.model.WAShareLinkContent;
import com.wa.sdk.social.model.WAShareResult;
import com.wa.sdk.user.WAUserProxy;
import com.wa.sdk.user.model.WALoginResult;
import com.wa.sdk.user.model.WAUser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 360 渠道
 * Created by hank on 16/7/22.
 */
public class Channel360Activity extends BaseActivity {
    private TitleBar mTitlebar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_360);

        initViews();
    }

    private void initViews() {
        mTitlebar = (TitleBar) findViewById(R.id.tb_360);
        mTitlebar.setTitleText(R.string.a360);
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
            startActivity(new Intent(this, PaymentActivity.class));
        else if (v.getId() == R.id.btn_query_init_info_intent)
            queryInitInfoIntent();
        else if (v.getId() == R.id.btn_upload_score)
            uploadScore();
//        else if (v.getId() == R.id.btn_invite)
//            invite();
//        else if (v.getId() == R.id.btn_invite_ui)
//            inviteUI();
        else if (v.getId() == R.id.btn_share)
            share();
        else if (v.getId() == R.id.btn_tracking)
            tracking();
        else if (v.getId() == R.id.btn_switch_account)
            switchAccount();
        else if (v.getId() == R.id.btn_query_authenticate_state)
            queryLoginUserAuthenticateState();
        else if (v.getId() == R.id.btn_rank)
            startActivity(new Intent(this, Rank360Activity.class));
        else if (v.getId() == R.id.btn_logout)
            logout();
        else if (v.getId() == R.id.btn_exit_game)
            exitGame();
    }

    /**
     * TODO 登录
     */
    private void login() {
        showLoadingDialog("360登录...", null);

        WAUserProxy.login(this, WAUserProxy.getCurrChannel(), new WACallback<WALoginResult>() {
            @Override
            public void onSuccess(int code, String message, WALoginResult result) {
                UserModel.getInstance().setDatas(result);
                cancelLoadingDialog();
                showShortToast(message);

                /*
                 * TODO 游戏角色信息上传 (必接)
                * @param _id 当前情景，支持 enterServer（登录），levelUp（升级），createRole（创建角色）
                * @param roleId 当前登录的玩家角色 ID，若无，可传入 userid
                * @param roleName 当前登录的玩家角色名，不能为空，不能为 null，若无，传入“游戏名称+username”，
                如“皇室战争大名鼎鼎”
                * @param roleLevel 当前登录的玩家角色等级，必须为数字，且不能为 0，若无，传入 1
                * @param zoneId 当前登录的游戏区服 ID，必须为数字，且不能为 0，若无，传入 1
                * @param zoneName 当前登录的游戏区服名称，不能为空，不能为 null，若无，传入游戏名称+”1 区”，如“梦
                幻西游 1 区”
                * @param balance 当前用户游戏币余额，必须为数字，若无，传入 0
                * @param vip 当前用户 VIP 等级，必须为数字，若无，传入 1
                * @param partyName 当前用户所属帮派，不能为空，不能为 null，若无，传入”无帮派”
                * */
                HashMap<String, Object> dataMap = new HashMap();
                dataMap.put("id", "enterServer");
                dataMap.put("roleId", UserModel.getInstance().getUserId());
                dataMap.put("roleName", "角色名");
                dataMap.put("roleLevel", "1");
                dataMap.put("zoneId", "1");
                dataMap.put("zoneName", "服区");
                dataMap.put("balance", "10");
                dataMap.put("vip", "1");
                dataMap.put("partyName", "无帮派");

                WAUserProxy.submitExtendData(WAUserProxy.getCurrChannel(), "statEvent", dataMap);
            }

            @Override
            public void onCancel() {
                cancelLoadingDialog();
                showShortToast("取消登录");
            }

            @Override
            public void onError(int code, String message, WALoginResult result, Throwable throwable) {
                cancelLoadingDialog();
                showShortToast("登录失败");
            }
        }, "");
    }

    /**
     * TODO 获取社交初始化信息
     */
    private void queryInitInfoIntent() {
        showLoadingDialog("获取社交初始化信息...", null);
        WASocialProxy.queryInitInfoIntent(this, WAUserProxy.getCurrChannel(), new WACallback<String>() {
            @Override
            public void onSuccess(int code, String message, String result) {
                cancelLoadingDialog();
                showShortToast(result);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(int code, String message, String result, Throwable throwable) {
                cancelLoadingDialog();
                showShortToast(message);
            }
        });
    }

    /**
     * TODO 邀请
     */
    private void invite() {
        /** TODO 查询可邀请好友列表 */
        WASocialProxy.queryInvitableFriends(this, WAUserProxy.getCurrChannel(), 10, new WACallback<WAFriendsResult>() {
            @Override
            public void onSuccess(int code, String message, WAFriendsResult result) {
                if (result.getFriends().isEmpty()) {
                    showShortToast("可邀请列表为空!");
                    return;
                }

                WAUser user = result.getFriends().get(0);

                ArrayList<String> inviteIds = new ArrayList<>();
                inviteIds.add(user.getId());

                WASocialProxy.sendRequest(Channel360Activity.this, WAUserProxy.getCurrChannel(),
                        WAConstants.REQUEST_INVITE, "邀请标题", "邀请信息", null, inviteIds,
                        new WACallback<WARequestSendResult>() {
                            @Override
                            public void onSuccess(int code, String message, WARequestSendResult result) {
                                showShortToast(message);
                            }

                            @Override
                            public void onCancel() {
                                showShortToast("取消邀请");
                            }

                            @Override
                            public void onError(int code, String message, WARequestSendResult result, Throwable throwable) {
                                showShortToast(message);
                            }
                        }, "");
            }

            @Override
            public void onCancel() {
                showShortToast("取消查询可邀请好友列表");
            }

            @Override
            public void onError(int code, String message, WAFriendsResult result, Throwable throwable) {
                showShortToast(message);
            }
        });
    }

    /**
     * TODO 邀请(带界面)
     */
    private void inviteUI() {
        showLoadingDialog("邀请好友...", null);

        WASocialProxy.inviteFriendUI(this, WAUserProxy.getCurrChannel(), "邀请信息", new WACallback<WARequestSendResult>() {
            @Override
            public void onSuccess(int code, String message, WARequestSendResult result) {
                cancelLoadingDialog();
                showShortToast(message);
            }

            @Override
            public void onCancel() {
                cancelLoadingDialog();
                showShortToast("取消邀请");
            }

            @Override
            public void onError(int code, String message, WARequestSendResult result, Throwable throwable) {
                cancelLoadingDialog();
                showShortToast(message);
            }
        });
    }

    /**
     * TODO 上传积分接口（若接入社交相关功能，则接口必接）
     */
    private void uploadScore() {
        showLoadingDialog("上传积分...", null);
        WASocialProxy.uploadScore(this, WAUserProxy.getCurrChannel(), 123456, "10", new WACallback<Integer>() {
            @Override
            public void onSuccess(int code, String message, Integer result) {
                cancelLoadingDialog();
                showShortToast(result == 1? "更新成功" : "更新失败");
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(int code, String message, Integer result, Throwable throwable) {
                cancelLoadingDialog();
                showShortToast(message);
            }
        });
    }

    /**
     * TODO 分享
     */
    private void share() {
        showLoadingDialog("分享...", null);

        // setImageUri 可选参数,分享的图片路径,微博分享时会使用
        // (必须是本地路径如:/sdcard/1.png,后缀可以是png、jpg、jpeg,
        // 大小不能超过5M,尺寸不能超过1280x720)
        WAShareLinkContent shareLinkContent = new WAShareLinkContent.Builder()
                .setContentTitle("Test share")
                .setContentDescription("Test WA share")
//                .setContentUri(Uri.parse("https://www.baidu.com/"))
//                .setImageUri(Uri.parse("/storage/emulated/0/DCIM/Camera/IMG_20130502_094017.jpg"))
                .build();

        WASocialProxy.share(this, WAUserProxy.getCurrChannel(), shareLinkContent, false, "", new WACallback<WAShareResult>() {
            @Override
            public void onSuccess(int code, String message, WAShareResult result) {
                cancelLoadingDialog();
                showShortToast(message);
            }

            @Override
            public void onCancel() {
                cancelLoadingDialog();
                showShortToast("取消分享");
            }

            @Override
            public void onError(int code, String message, WAShareResult result, Throwable throwable) {
                cancelLoadingDialog();
                showShortToast(message);
            }
        });
    }

    /**
     * TODO 统计数据
     */
    private void tracking() {
        Intent intent = new Intent(this, TrackingActivity.class);
        intent.putExtra("channel", 3);
        startActivity(intent);
    }

    /**
     * TODO 切换账号
     */
    private void switchAccount() {
        showLoadingDialog("切换账号...", null);
        WAUserProxy.switchAccount(this, WAUserProxy.getCurrChannel(), new WACallback<WALoginResult>() {
            @Override
            public void onSuccess(int code, String message, WALoginResult result) {
                cancelLoadingDialog();
                showShortToast(message);
            }

            @Override
            public void onCancel() {
                cancelLoadingDialog();
                showShortToast("切换账号取消");
            }

            @Override
            public void onError(int code, String message, WALoginResult result, Throwable throwable) {
                cancelLoadingDialog();
                showShortToast(message);
            }
        });
    }

    // TODO 查询登录用户实名认证状态
    private void queryLoginUserAuthenticateState() {
        showLoadingDialog("查询登录用户实名认证状态...", null);
        WAUserProxy.queryLoginUserAuthenticateState(this, WAUserProxy.getCurrChannel(),new WACallback<Integer>() {
            @Override
            public void onSuccess ( int code, String message, Integer result) {
                // resut 0 无此用户数据; 1 未成年; 2 已成年
                String resultStr = "无此用户数据";
                if (result == 1)
                    resultStr = "未成年";
                else if (result == 2)
                    resultStr = "已成年";

                cancelLoadingDialog();
                showShortToast(resultStr);
            }

            @Override
            public void onCancel () {

            }

            @Override
            public void onError ( int code, String message, Integer result, Throwable throwable) {
                cancelLoadingDialog();
                showShortToast(message);
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
