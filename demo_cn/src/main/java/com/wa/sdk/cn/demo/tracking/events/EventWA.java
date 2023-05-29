package com.wa.sdk.cn.demo.tracking.events;

import com.wa.sdk.WAConstants;
import com.wa.sdk.cn.demo.tracking.EventModel;
import com.wa.sdk.track.WAEventParameterName;
import com.wa.sdk.track.WAEventType;

import java.util.ArrayList;

/**
 * Event WA
 * Created by hank on 16/9/6.
 */
public class EventWA extends Event {

    public EventWA() {
        if (eventModels == null) {
            eventModels = new ArrayList<>();
            eventModels.add(new EventModel("登录游戏", WAEventType.LOGIN));
            eventModels.add(new EventModel("点击充值", WAEventType.INITIATED_PAYMENT));
            eventModels.add(new EventModel("充值完成", WAEventType.COMPLETE_PAYMENT));
            eventModels.add(new EventModel("点击购买", WAEventType.INITIATED_PURCHASE));
            eventModels.add(new EventModel("充值购买", WAEventType.COMPLETE_PURCHASE));
            eventModels.add(new EventModel("玩家等级增长", WAEventType.LEVEL_ACHIEVED));
            eventModels.add(new EventModel("创建游戏角色", WAEventType.USER_CREATED));
            eventModels.add(new EventModel("更新用户资料", WAEventType.USER_INFO_UPDATE));
            eventModels.add(new EventModel("玩家任务信息", WAEventType.TASK_UPDATE));
            eventModels.add(new EventModel("玩家货币状况变更", WAEventType.GOLD_UPDATE));
            eventModels.add(new EventModel("导入用户", WAEventType.IMPORT_USER));
            eventModels.add(new EventModel("自定义事件", WAEventType.CUSTOM_EVENT_PREFIX));
        }
    }

    @Override
    public String getChannel() {
        return WAConstants.CHANNEL_WA;
    }

    @Override
    public EventModel getEventModel(int position) {
        EventModel eventModel = new EventModel();
        eventModel.setName(eventModels.get(position).getName());
        eventModel.setEventName(eventModels.get(position).getEventName());

        if (position == 0) {            // TODO LOGIN

        } else if (position == 1) {     // INITIATED_PAYMENT

        } else if (position == 2) {     // COMPLETE_PAYMENT
            eventModel.addEventValue(WAEventParameterName.TRANSACTION_ID, "13241893274981237");
            eventModel.addEventValue(WAEventParameterName.PAYMENT_TYPE, WAConstants.CHANNEL_GOOGLE);
            eventModel.addEventValue(WAEventParameterName.CURRENCY_TYPE, WAConstants.CURRENCY_USD);
            eventModel.addEventValue(WAEventParameterName.CURRENCY_AMOUNT, 50.234f);
            eventModel.addEventValue(WAEventParameterName.VERTUAL_COIN_AMOUNT, 20000);
            eventModel.addEventValue(WAEventParameterName.VERTUAL_COIN_CURRENCY, "gold");
            eventModel.addEventValue(WAEventParameterName.IAP_ID, "1111111");
            eventModel.addEventValue(WAEventParameterName.IAP_NAME, "GGGGGG");
            eventModel.addEventValue(WAEventParameterName.IAP_AMOUNT, 20);
        } else if (position == 3) {     // TODO INITIATED_PURCHASE

        } else if (position == 4) {     // TODO COMPLETE_PURCHASE
            eventModel.addEventValue(WAEventParameterName.ITEM_NAME, "GGGGG");
            eventModel.addEventValue(WAEventParameterName.ITEM_AMOUNT, 20);
            eventModel.addEventValue(WAEventParameterName.PRICE, 50);
        } else if (position == 5) {     // TODO LEVEL_ACHIEVED
            eventModel.addEventValue(WAEventParameterName.SCORE, 3241234);
            eventModel.addEventValue(WAEventParameterName.FIGHTING, 1230020);
        } else if (position == 6) {     // TODO USER_CREATED
            eventModel.addEventValue(WAEventParameterName.ROLE_TYPE, 1);
            eventModel.addEventValue(WAEventParameterName.GENDER, WAConstants.GENDER_FEMALE);
            eventModel.addEventValue(WAEventParameterName.NICKNAME, "霸气侧漏");
            eventModel.addEventValue(WAEventParameterName.REGISTER_TIME, System.currentTimeMillis());
            eventModel.addEventValue(WAEventParameterName.VIP, 8);
            eventModel.addEventValue(WAEventParameterName.BINDED_GAME_GOLD, 100000);
            eventModel.addEventValue(WAEventParameterName.GAME_GOLD, 10000);
            eventModel.addEventValue(WAEventParameterName.FIGHTING, 1230020);
        } else if (position == 7) {     // TODO USER_INFO_UPDATE
            eventModel.addEventValue(WAEventParameterName.ROLE_TYPE, 1);
            eventModel.addEventValue(WAEventParameterName.NICKNAME, "霸气侧漏");
            eventModel.addEventValue(WAEventParameterName.VIP, 8);
        } else if (position == 8) {     // TODO TASK_UPDATE
            eventModel.addEventValue(WAEventParameterName.TASK_ID, "123");
            eventModel.addEventValue(WAEventParameterName.TASK_NAME, "刺杀希特勒");
            eventModel.addEventValue(WAEventParameterName.TASK_TYPE, "等级任务");
            eventModel.addEventValue(WAEventParameterName.TASK_STATUS, 2);
        } else if (position == 9) {     // TODO GOLD_UPDATE
            eventModel.addEventValue(WAEventParameterName.GOLD_TYPE, 1);
            eventModel.addEventValue(WAEventParameterName.APPROACH, "充值");
            eventModel.addEventValue(WAEventParameterName.AMOUNT, 100000);
            eventModel.addEventValue(WAEventParameterName.CURRENT_AMOUNT, 200000);
        } else if (position == 10) {    // TODO IMPORT_USER
            eventModel.addEventValue(WAEventParameterName.IS_FIRST_ENTER, 0);
        } else if (position == 11) {    // TODO CUSTOM_EVENT_PREFIX
            eventModel.addEventValue("to_level", 141);
            eventModel.addEventValue("fight_force", 1232320);
            eventModel.addEventValue("to_fight_force", 1220020);
            eventModel.addEventValue(WAEventParameterName.SUCCESS, true);
        }
        return eventModel;
    }
}
