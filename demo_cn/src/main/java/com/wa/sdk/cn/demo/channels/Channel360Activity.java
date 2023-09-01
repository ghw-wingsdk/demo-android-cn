package com.wa.sdk.cn.demo.channels;

import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.view.View;

import com.wa.sdk.WAConstants;
import com.wa.sdk.cn.demo.PaymentActivity;
import com.wa.sdk.cn.demo.R;
import com.wa.sdk.cn.demo.WASdkDemo;
import com.wa.sdk.cn.demo.base.BaseActivity;
import com.wa.sdk.cn.demo.tracking.TrackingActivity;
import com.wa.sdk.cn.demo.widget.TitleBar;
import com.wa.sdk.common.WAActivityAdPage;
import com.wa.sdk.common.WACommonProxy;
import com.wa.sdk.common.model.WACallback;
import com.wa.sdk.common.model.WAResult;
import com.wa.sdk.common.utils.LogUtil;
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
    private WAActivityAdPage activityAdPage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_360);

        // 游戏 activity 生命周期接口 (必接)
        activityAdPage = WACommonProxy.getActivityAdPage(this, WAUserProxy.getCurrChannel(), null);
        initViews();
    }

    private void initViews() {
        mTitlebar = findViewById(R.id.tb_360);
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
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_pay:
                startActivity(new Intent(this, PaymentActivity.class));
                break;
            case R.id.btn_query_init_info_intent:
                queryInitInfoIntent();
                break;
            case R.id.btn_upload_score:
                uploadScore();
                break;
//            case R.id.btn_invite:
//                invite();
//                break;
//            case R.id.btn_invite_ui:
//                inviteUI();
//                break;
            case R.id.btn_share:
                share();
                break;
            case R.id.btn_tracking:
                tracking();
                break;
            case R.id.btn_switch_account:
                switchAccount();
                break;
            case R.id.btn_query_authenticate_state:
                queryLoginUserAuthenticateState();
                break;
            case R.id.btn_rank:
                startActivity(new Intent(this, Rank360Activity.class));
                break;
            case R.id.btn_logout:
                logout();
                break;
            case R.id.btn_exit_game:
                exitGame();
                break;
            default:
                break;
        }
    }

    /**
     * TODO 登录
     */
    private void login() {
        showLoadingDialog("360登录...", null);

        WAUserProxy.login(this, WAUserProxy.getCurrChannel(), new WACallback<WALoginResult>() {
            @Override
            public void onSuccess(int code, String message, WALoginResult result) {
                WASdkDemo.getInstance().updateLoginAccount(result);
                cancelLoadingDialog();

                /**
                 * TODO 游戏角色信息上传 (必接) 必填必须传，选填可不传
                 * 上传数据时角色状态更新 type 必填 string 数据可根据场景定义随时上传，以下场 景必传: enterServer(登录)，levelUp(升级)，createRole(创建角色)， exitServer(退出)
                 * 当前登录的游戏区服ID zoneid 必填 int 不能为空，必须为数字，若无，传入 0
                 * 当前登录的游戏区服名称 zonename 必填 string 不能为空，区服名称，要求与游戏界面 展示的服务器名保持一致,长度不超过 50，不能为 null，若无，传入“无”
                 * 当前登录的玩家角色ID roleid 必填 string 角色 ID，一个角色同一个服 ID 保持唯 一,长度不超过 50
                 * 当前登录的玩家角色名 rolename 必填 string 不能为空，角色昵称,长度不超过 50，不 能为 null，若无，传入“无”
                 * 当前登录玩家的职业ID professionid 必填 int 不能为空，必须为数字，若无，传入 0
                 * 当前登录玩家的职业名称 profession 必填 string 不能为空，不能为 null，若无，传入“无”
                 * 当前登录玩家的性别 gender 必填 string 不能为空，不能为 null，可传入参数 “男、女、无”
                 * 当前登录的玩家角色等级 rolelevel 必填 int 不能为空，必须为数字，且不能为 null， 若无，传入0, 如游戏存在转生，转职等， 等级需累加，长度不超过 10
                 * 战力值数值 power 必填 int 不能为空，必须为数字,不能为 null，若 无，传入 0
                 * 当前用户VIP等级 vip 必填 int 不能为空，必须为数字,若无，传入 0
                 * 当前用户所属帮派帮派ID partyid 必填 int 不能为空，必须为数字,不能为 null，若 无，传入 0
                 * 当前用户所属帮派名称 partyname 必填 string 不能为空，不能为 null，若无，传入 “无”
                 * 帮派称号ID partyroleid 必填 int 帮派会长/帮主必传 1，其他可自定义， 不能为空，不能为 null，若无，传入 0
                 * 帮派称号名称 partyrolename 必填 string 不能为空，不能为 null，若无，传入 “无”
                 * 他的好友 friendlist 必填
                 关系角色 ID(roleid) int 传入用户的所有好友角色 id 列表，不能 为空，必须与角色 id(roleid)格式保 持一致，不能为 null，若无，传入“无”
                 亲密度(intimacy) int 必须为数字，不能为空，不能为 null，若无，传入 0
                 关系 ID(nexusid) int 必须为数字，不能为空，不能为 null，若无，传入 0
                 关系名称 (nexusname) string 预定字段:以下对应方式为 nexusid:nexusnam: 1:夫妻，2:结拜，3:情侣，4:师徒 ，5: 仇人;其余关系从 6 开始自定义编号
                 传入用户的所有好友角色 id 列表，不能 为空，必须与角色 id(roleid)格式保 持一致，不能为 null，若无，传入“无”
                 示例: [{"roleid":1,"intimacy":"0","nexusid": "600","nexusname":"情侣 "},{"roleid":2,"intimacy":"0","nexusid ":"200","nexusname":"仇人"}]
                 *
                 * 职业称号ID professionroleid 选填 int 不能为空，不能为 null，若无，传入 0
                 * 职业称号 rofessionrolename 选填 string 不能为空，不能为 null，若无，传入"无"
                 * 账号余额 balance 选填
                 货币 ID(balanceid) int、货币名称 (balancename) string、货币数额 (balancenum) int，
                 不能为空，不能为 null，若无，传入 0 int 的传参必须为数字
                 示例: [{"balanceid":1,"balancename":"\u91d1\u5e01","balancenum":"600"},{" balanceid":1,"balancename":"\u91d1\u5e01","balancenum":"600"}]
                 * 排行榜列表 ranking 选填
                 榜单 ID(listid) int、榜单名称 (listname) string、传入角色当前排名 (num) int、排名指标 ID (coin) int、排名指标名称 Value(cost) string
                 不能为空，int 必须为数字，不能为 null， 若无，传入 “无”
                 示例: [{"listid":1,"listname":"\u91d1\u5e01 ","num":"600","coin":"XX","cost":"XX "},{"listid":1,"listname":"\u91d1\u5e01","num":"600","coin":"XX","cost":"XX"}]
                 */
                HashMap<String, Object> dataMap = new HashMap();
                dataMap.put("type", "enterServer");         // 上传数据时角色状态更新
                dataMap.put("zoneid", 123456);              // 游戏区服ID
                dataMap.put("rolename", "服区");             // 游戏区服名称
                dataMap.put("roleid", "1234567890");        // 玩家角色ID
                dataMap.put("rolename", "角色名");           // 玩家角色名
                dataMap.put("professionid", 123652);        // 玩家的职业ID
                dataMap.put("profession", "职业名称");       // 玩家的职业名称
                dataMap.put("gender", "男");                // 玩家的性别
                dataMap.put("rolelevel", 11);               // 玩家角色等级
                dataMap.put("power", 9999);                 // 战力值数值
                dataMap.put("vip", 1);                      // VIP等级
                dataMap.put("partyid", 123456789);          // 所属帮派帮派ID
                dataMap.put("partyName", "帮派名称");        // 所属帮派名称
                dataMap.put("partyroleid", 123456789);      // 帮派称号ID
                dataMap.put("partyrolename", "帮派称号名称"); // 帮派称号名称
                dataMap.put("friendlist", "[{'roleid':1,'intimacy':'0','nexusid': '600','nexusname':'情侣'}," +
                        "{'roleid':2,'intimacy':'0','nexusid':'200','nexusname':'仇人'}]");    // 他的好友

                WAUserProxy.submitRoleData(WAUserProxy.getCurrChannel(), Channel360Activity.this, dataMap);
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
        intent.putExtra("channel", 1);
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
        WASdkDemo.getInstance().logout();
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

    @Override
    protected void onStart() {
        super.onStart();
        if(null != activityAdPage) {
            activityAdPage.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(null != activityAdPage) {
            activityAdPage.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(null != activityAdPage) {
            activityAdPage.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(null != activityAdPage) {
            activityAdPage.onStop();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(null != activityAdPage) {
            activityAdPage.onRestart();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(null != activityAdPage) {
            activityAdPage.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(null != activityAdPage) {
            activityAdPage.onNewIntent(intent);
        }
    }
}
