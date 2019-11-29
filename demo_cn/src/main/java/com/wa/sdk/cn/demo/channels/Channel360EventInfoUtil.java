package com.wa.sdk.cn.demo.channels;

import android.app.Activity;

import com.wa.sdk.user.WAUserProxy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Desc:
 * Created by zgq on 2019/11/19.
 */
public class Channel360EventInfoUtil {
    private static final Channel360EventInfoUtil ourInstance = new Channel360EventInfoUtil();

    public static Channel360EventInfoUtil getInstance() {
        return ourInstance;
    }

    private Channel360EventInfoUtil() {
    }


    public void submitRoleData(Activity activity){
        //----------------------------模拟数据------------------------------
        //帐号余额
        JSONArray balancelist = new JSONArray();
        JSONObject balance1 = new JSONObject();
        JSONObject balance2 = new JSONObject();

        //好友关系
        JSONArray friendlist = new JSONArray();
        JSONObject friend1 = new JSONObject();
        JSONObject friend2 = new JSONObject();

        //排行榜列表
        JSONArray ranklist = new JSONArray();
        JSONObject rank1 = new JSONObject();
        JSONObject rank2 = new JSONObject();

        try {
            balance1.put("balanceid","1");
            balance1.put("balancename","bname1");
            balance1.put("balancenum","200");
            balance2.put("balanceid","2");
            balance2.put("balancename","bname2");
            balance2.put("balancenum","300");
            balancelist.put(balance1).put(balance2);


            friend1.put("roleid","1");
            friend1.put("intimacy","0");
            friend1.put("nexusid","300");
            friend1.put("nexusname","情侣");
            friend2.put("roleid","2");
            friend2.put("intimacy","0");
            friend2.put("nexusid","600");
            friend2.put("nexusname","情侣");
            friendlist.put(friend1).put(friend2);

            rank1.put("listid","1");
            rank1.put("listname","listname1");
            rank1.put("num","num1");
            rank1.put("coin","coin1");
            rank1.put("cost","cost1");
            rank2.put("listid","2");
            rank2.put("listname","listname2");
            rank2.put("num","num2");
            rank2.put("coin","coin2");
            rank2.put("cost","cost2");
            ranklist.put(rank1).put(rank2);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        HashMap<String, Object> eventParams=new HashMap<String, Object>();

        eventParams.put("type","createRole");  //（必填）角色状态（enterServer（登录），levelUp（升级），createRole（创建角色），exitServer（退出））
        eventParams.put("zoneid","2");  //（必填）游戏区服ID
        eventParams.put("zonename","测试服");  //（必填）游戏区服名称
        eventParams.put("roleid","123456");  //（必填）玩家角色ID
        eventParams.put("rolename","总裁女友");  //（必填）玩家角色名
        eventParams.put("professionid","1");  //（必填）职业ID
        eventParams.put("profession","战士");  //（必填）职业名称
        eventParams.put("gender","男");  //（必填）性别
        eventParams.put("professionroleid","0");  //（选填）职业称号ID
        eventParams.put("professionrolename","无");  //（选填）职业称号
        eventParams.put("rolelevel","30");  //（必填）玩家角色等级
        eventParams.put("power","120000");  //（必填）战力数值
        eventParams.put("vip","5");  //（必填）当前用户VIP等级
        eventParams.put("balance",balancelist.toString());  //（必填）帐号余额
        eventParams.put("partyid","100");  //（必填）所属帮派帮派ID
        eventParams.put("partyname","王者依旧");  //（必填）所属帮派名称
        eventParams.put("partyroleid","1");  //（必填）帮派称号ID
        eventParams.put("partyrolename","会长");  //（必填）帮派称号名称
        eventParams.put("friendlist",friendlist.toString());  //（必填）好友关系
        eventParams.put("ranking",ranklist.toString());  //（选填）排行榜列表
        //参数eventParams相关的 key、value键值对 相关具体使用说明，请参考文档。
        //----------------------------模拟数据------------------------------
        WAUserProxy.submitRoleData(WAUserProxy.getCurrChannel(), activity, eventParams);
    }

}
