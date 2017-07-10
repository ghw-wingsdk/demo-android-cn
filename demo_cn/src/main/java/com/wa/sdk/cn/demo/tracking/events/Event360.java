package com.wa.sdk.cn.demo.tracking.events;

import com.wa.sdk.WAConstants;
import com.wa.sdk.cn.demo.tracking.EventModel;
import com.wa.sdk.track.WAEventParameterName;
import com.wa.sdk.track.WAEventType;

import java.util.ArrayList;

/**
 * Event 360
 * Created by hank on 16/9/6.
 */
public class Event360 extends Event {

    public Event360() {
        if (eventModels == null) {
            eventModels = new ArrayList<>();
            eventModels.add(new EventModel("开始关卡", WAEventType.START_LEVEL));
            eventModels.add(new EventModel("结束关卡", WAEventType.LEVEL_ACHIEVED));
            eventModels.add(new EventModel("失败关卡", WAEventType.FAIL_LEVEL));
            eventModels.add(new EventModel("开始任务", WAEventType.TASK_UPDATE));
            eventModels.add(new EventModel("结束任务", WAEventType.TASK_UPDATE));
            eventModels.add(new EventModel("失败任务", WAEventType.FAIL_TASK));
            eventModels.add(new EventModel("支付金币统计", WAEventType.COMPLETE_PAYMENT));
            eventModels.add(new EventModel("支付道具统计", WAEventType.COMPLETE_PAYMENT));
            eventModels.add(new EventModel("虚拟币购买物品统计", WAEventType.COMPLETE_PURCHASE));
            eventModels.add(new EventModel("物品消耗统计", WAEventType.USE));
            eventModels.add(new EventModel("玩家统计", WAEventType.PLAYER));
            eventModels.add(new EventModel("角色统计", WAEventType.USER_CREATED));
            eventModels.add(new EventModel("自定义事件统计", WAEventType.CUSTOM_EVENT_PREFIX));
        }
    }

    @Override
    public String getChannel() {
        return WAConstants.CHANNEL_QIHU360;
    }

    @Override
    public EventModel getEventModel(int position) {
        EventModel eventModel = new EventModel();
        eventModel.setName(eventModels.get(position).getName());
        eventModel.setEventName(eventModels.get(position).getEventName());

        if (position == 0) {        // TODO 开始关卡 START_LEVEL
            eventModel.addEventValue(WAEventParameterName.LEVEL, "level name"); // 关卡名称
        } else if (position == 1) { // TODO 结束关卡 LEVEL_ACHIEVED
            eventModel.addEventValue(WAEventParameterName.LEVEL, "level name"); // 关卡名称
        } else if (position == 2) { // TODO 失败关卡 FAIL_LEVEL
//            builder.setDefaultEventName(WAEventType.FAIL_LEVEL);
            eventModel.addEventValue(WAEventParameterName.LEVEL, "level name");    // 关卡名称
            eventModel.addEventValue(WAEventParameterName.DESCRIPTION, "失败原因"); // 失败原因
        } else if (position == 3) { // TODO 开始任务 TASK_UPDATE
            eventModel.addEventValue(WAEventParameterName.TASK_NAME, "task name"); // 任务名称
            eventModel.addEventValue(WAEventParameterName.TASK_TYPE, "任务类型");   // 任务类型
            eventModel.addEventValue(WAEventParameterName.TASK_STATUS, 2);         // 任务状态
        } else if (position == 4) { // TODO 结束任务 TASK_UPDATE
            eventModel.addEventValue(WAEventParameterName.TASK_NAME, "task name"); // 任务名称
            eventModel.addEventValue(WAEventParameterName.TASK_TYPE, "任务类型");   // 任务类型
            eventModel.addEventValue(WAEventParameterName.TASK_STATUS, 4);         // 任务状态
        } else if (position == 5) { // TODO 失败任务 FAIL_TASK
            eventModel.addEventValue(WAEventParameterName.TASK_NAME, "task name"); // 任务名称
            eventModel.addEventValue(WAEventParameterName.DESCRIPTION, "失败原因"); // 失败原因
        } else if (position == 6) { // TODO 支付金币统计 COMPLETE_PAYMENT
            eventModel.addEventValue(WAEventParameterName.CURRENCY_AMOUNT, 15.0f);      // 支付金额
            eventModel.addEventValue(WAEventParameterName.CURRENCY_TYPE, "CNY");     // 支付货币类型
            eventModel.addEventValue(WAEventParameterName.PAYMENT_TYPE, WAConstants.CHANNEL_QIHU360);          // 支付方式（游戏自定义，给每个支付渠道定义的整型值）
            eventModel.addEventValue(WAEventParameterName.VERTUAL_COIN_AMOUNT, 150l); // 金币数量
            eventModel.addEventValue(WAEventParameterName.CHAPTER, "关卡1");           // 关卡
        } else if (position == 7) { // TODO 支付道具统计 COMPLETE_PAYMENT
            eventModel.addEventValue(WAEventParameterName.CURRENCY_AMOUNT, 15.0f);  // 支付金额
            eventModel.addEventValue(WAEventParameterName.CURRENCY_TYPE, "CNY"); // 支付货币类型
            eventModel.addEventValue(WAEventParameterName.PAYMENT_TYPE, WAConstants.CHANNEL_QIHU360);      // 支付方式（游戏自定义，给每个支付渠道定义的整型值）
            eventModel.addEventValue(WAEventParameterName.IAP_NAME, "道具名称");  // 道具名称
            eventModel.addEventValue(WAEventParameterName.IAP_AMOUNT, 1);        // 道具数量
            eventModel.addEventValue(WAEventParameterName.CHAPTER, "关卡1");       // 关卡
        } else if (position == 8) { // TODO 虚拟币购买物品统计 COMPLETE_PURCHASE
            eventModel.addEventValue(WAEventParameterName.ITEM_NAME, "虚拟物品名称");  // 虚拟物品名称
            eventModel.addEventValue(WAEventParameterName.ITEM_AMOUNT, 2);           // 虚拟物品数量
            eventModel.addEventValue(WAEventParameterName.VERTUAL_COIN_AMOUNT, 200); // 虚拟货币数量
        } else if (position == 9) { // TODO 物品消耗统计 USE
            eventModel.addEventValue(WAEventParameterName.ITEM_NAME, "物品名称");               // 物品名称
            eventModel.addEventValue(WAEventParameterName.ITEM_AMOUNT, 1);                     // 物品数量
            eventModel.addEventValue(WAEventParameterName.ITEM_TYPE, "物品类型");               // 物品类型
            eventModel.addEventValue(WAEventParameterName.VERTUAL_COIN_CURRENCY, "虚拟币类型"); // 虚拟币类型
            eventModel.addEventValue(WAEventParameterName.VERTUAL_COIN_AMOUNT, 100);           // 物品对应的虚拟币数量
            eventModel.addEventValue(WAEventParameterName.LEVEL, "关卡名称");                   // 关卡名称
        } else if (position == 10) { // TODO 玩家统计 PLAYER
            eventModel.addEventValue(WAEventParameterName.USER_ID, "123456789");     // 玩家标识
            eventModel.addEventValue(WAEventParameterName.AGE, 20);                  // 年龄
            eventModel.addEventValue(WAEventParameterName.GENDER, 0);                // 性别
            eventModel.addEventValue(WAEventParameterName.SOURCE, "qihoo360");       // 玩家来源（游戏自定义，给每个渠道用户定义的字符串类型值，如:”qihoo360”,”weibo”）
            eventModel.addEventValue(WAEventParameterName.DESCRIPTION, "服务器名称"); // 其他备注信息
        } else if (position == 11) { // TODO 角色统计 USER_CREATED
            eventModel.addEventValue(WAEventParameterName.NICKNAME, "玩家昵称"); // 玩家昵称
        } else if (position == 12) { // TODO 自定义事件统计
            eventModel.addEventValue(WAEventParameterName.LEVEL, 140);
            eventModel.addEventValue("to_level", 141);
            eventModel.addEventValue("fight_force", 1232320);
            eventModel.addEventValue("to_fight_force", 1220020);
            eventModel.addEventValue(WAEventParameterName.SUCCESS, true);
        }

        return eventModel;
    }
}
