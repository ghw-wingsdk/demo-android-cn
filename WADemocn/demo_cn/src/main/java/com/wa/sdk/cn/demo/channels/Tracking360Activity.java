package com.wa.sdk.cn.demo.channels;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wa.sdk.cn.demo.R;
import com.wa.sdk.cn.demo.base.BaseActivity;
import com.wa.sdk.cn.demo.widget.TitleBar;
import com.wa.sdk.track.WAEventParameterName;
import com.wa.sdk.track.WAEventType;
import com.wa.sdk.track.WATrackProxy;
import com.wa.sdk.track.model.WAEvent.Builder;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 360 事件上报
 * Created by hank on 16/7/26.
 */
public class Tracking360Activity extends BaseActivity implements OnItemClickListener {
    private TitleBar mTitleBar = null;
    private ListView listView = null;

    private ArrayList<String> datas = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        WATrackProxy.startHeartBeat(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        WATrackProxy.stopHeartBeat(this);
    }

    private void initViews() {
        mTitleBar = (TitleBar)findViewById(R.id.tb_tracking);
        mTitleBar.setTitleTextColor(R.color.color_white);
        mTitleBar.setTitleText(R.string.tracking);
        mTitleBar.setLeftButton(android.R.drawable.ic_menu_revert, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        datas = new ArrayList<String>();
        datas.add("开始关卡");
        datas.add("结束关卡");
        datas.add("失败关卡");
        datas.add("开始任务");
        datas.add("结束任务");
        datas.add("失败任务");
        datas.add("支付金币统计");
        datas.add("支付道具统计");
        datas.add("虚拟币购买物品统计");
        datas.add("物品消耗统计");
        datas.add("玩家统计");
        datas.add("角色统计");
        datas.add("自定义事件统计");

//        listView = (ListView) findViewById(R.id.lv_events);
//        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datas));
//        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Builder builder = new Builder();
        HashMap<String, Object> eventValues = new HashMap<String, Object>();

        if (position == 0) {        // TODO 开始关卡
            builder.setDefaultEventName(WAEventType.START_LEVEL);
            eventValues.put(WAEventParameterName.LEVEL, "level name"); // 关卡名称
        } else if (position == 1) { // TODO 结束关卡
            builder.setDefaultEventName(WAEventType.LEVEL_ACHIEVED);
            eventValues.put(WAEventParameterName.LEVEL, "level name"); // 关卡名称
        } else if (position == 2) { // TODO 失败关卡
            builder.setDefaultEventName(WAEventType.FAIL_LEVEL);
            eventValues.put(WAEventParameterName.LEVEL, "level name");    // 关卡名称
            eventValues.put(WAEventParameterName.DESCRIPTION, "失败原因"); // 失败原因
        } else if (position == 3) { // TODO 开始任务
            builder.setDefaultEventName(WAEventType.TASK_UPDATE);
            eventValues.put(WAEventParameterName.TASK_NAME, "task name"); // 任务名称
            eventValues.put(WAEventParameterName.TASK_TYPE, "任务类型");   // 任务类型
            eventValues.put(WAEventParameterName.TASK_STATUS, 2);         // 任务状态
        } else if (position == 4) { // TODO 结束任务
            builder.setDefaultEventName(WAEventType.TASK_UPDATE);
            eventValues.put(WAEventParameterName.TASK_NAME, "task name"); // 任务名称
            eventValues.put(WAEventParameterName.TASK_TYPE, "任务类型");   // 任务类型
            eventValues.put(WAEventParameterName.TASK_STATUS, 4);         // 任务状态
        } else if (position == 5) { // TODO 失败任务
            builder.setDefaultEventName(WAEventType.FAIL_TASK);
            eventValues.put(WAEventParameterName.TASK_NAME, "task name"); // 任务名称
            eventValues.put(WAEventParameterName.DESCRIPTION, "失败原因"); // 失败原因
        } else if (position == 6) { // TODO 支付金币统计
            builder.setDefaultEventName(WAEventType.COMPLETE_PAYMENT);
            eventValues.put(WAEventParameterName.CURRENCY_AMOUNT, 15);      // 支付金额
            eventValues.put(WAEventParameterName.CURRENCY_TYPE, "CNY");     // 支付货币类型
            eventValues.put(WAEventParameterName.PAYMENT_TYPE, 1);          // 支付方式（游戏自定义，给每个支付渠道定义的整型值）
            eventValues.put(WAEventParameterName.LEVEL, "11");              // 玩家等级
            eventValues.put(WAEventParameterName.VERTUAL_COIN_AMOUNT, 150); // 金币数量
        } else if (position == 7) { // TODO 支付道具统计
            builder.setDefaultEventName(WAEventType.COMPLETE_PAYMENT);
            eventValues.put(WAEventParameterName.CURRENCY_AMOUNT, 15);  // 支付金额
            eventValues.put(WAEventParameterName.CURRENCY_TYPE, "CNY"); // 支付货币类型
            eventValues.put(WAEventParameterName.PAYMENT_TYPE, 1);      // 支付方式（游戏自定义，给每个支付渠道定义的整型值）
            eventValues.put(WAEventParameterName.LEVEL, "11");          // 玩家等级
            eventValues.put(WAEventParameterName.IAP_NAME, "道具名称");  // 道具名称
            eventValues.put(WAEventParameterName.IAP_AMOUNT, 1);        // 道具数量
        } else if (position == 8) { // TODO 虚拟币购买物品统计
            builder.setDefaultEventName(WAEventType.COMPLETE_PURCHASE);
            eventValues.put(WAEventParameterName.ITEM_NAME, "虚拟物品名称");  // 虚拟物品名称
            eventValues.put(WAEventParameterName.ITEM_AMOUNT, 2);           // 虚拟物品数量
        } else if (position == 9) { // TODO 物品消耗统计
            builder.setDefaultEventName(WAEventType.USE);
            eventValues.put(WAEventParameterName.ITEM_NAME, "物品名称");               // 物品名称
            eventValues.put(WAEventParameterName.ITEM_AMOUNT, 1);                     // 物品数量
            eventValues.put(WAEventParameterName.ITEM_TYPE, "物品类型");               // 物品类型
            eventValues.put(WAEventParameterName.VERTUAL_COIN_CURRENCY, "虚拟币类型"); // 虚拟币类型
            eventValues.put(WAEventParameterName.VERTUAL_COIN_AMOUNT, 100);           // 物品对应的虚拟币数量
            eventValues.put(WAEventParameterName.LEVEL, "关卡名称");                   // 关卡名称
        } else if (position == 10) { // TODO 玩家统计
            builder.setDefaultEventName(WAEventType.PLAYER);
            eventValues.put(WAEventParameterName.USER_ID, "123456789");     // 玩家标识
            eventValues.put(WAEventParameterName.AGE, 20);                  // 年龄
            eventValues.put(WAEventParameterName.GENDER, 0);                // 性别
            eventValues.put(WAEventParameterName.SOURCE, "qihoo360");       // 玩家来源（游戏自定义，给每个渠道用户定义的字符串类型值，如:”qihoo360”,”weibo”）
            eventValues.put(WAEventParameterName.LEVEL, "15");              // 玩家等级
            eventValues.put(WAEventParameterName.SERVER_ID, "服务器名称");   // 区域服务器名称
            eventValues.put(WAEventParameterName.DESCRIPTION, "服务器名称"); // 其他备注信息
        } else if (position == 11) { // TODO 角色统计
            builder.setDefaultEventName(WAEventType.USER_CREATED);
            eventValues.put(WAEventParameterName.NICKNAME, "玩家昵称"); // 玩家昵称
        } else if (position == 12) { // TODO 自定义事件统计
            builder.setDefaultEventName("");
            eventValues.put("", "");
            eventValues.put("", "");
        }

        builder.setDefaultEventValues(eventValues);
        WATrackProxy.trackEvent(this, builder.build());
    }
}
