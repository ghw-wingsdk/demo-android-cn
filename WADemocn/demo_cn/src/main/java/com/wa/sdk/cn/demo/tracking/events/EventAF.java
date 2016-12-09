package com.wa.sdk.cn.demo.tracking.events;

import com.wa.sdk.WAConstants;
import com.wa.sdk.cn.demo.tracking.EventModel;
import com.wa.sdk.track.WAEventParameterName;
import com.wa.sdk.track.WAEventType;

import java.util.ArrayList;

/**
 * Event Appsflyer
 * Created by hank on 16/9/6.
 */
public class EventAF extends Event {

    public EventAF() {
        if (eventModels == null) {
            eventModels = new ArrayList<>();
            eventModels.add(new EventModel("", WAEventType.LOGIN));
            eventModels.add(new EventModel("", WAEventType.INITIATED_PAYMENT));
            eventModels.add(new EventModel("", WAEventType.COMPLETE_PAYMENT));
            eventModels.add(new EventModel("", WAEventType.LEVEL_ACHIEVED));
            eventModels.add(new EventModel("自定义事件统计", WAEventType.CUSTOM_EVENT_PREFIX));
        }
    }

    @Override
    public String getChannel() {
        return WAConstants.CHANNEL_APPSFLYER;
    }

    @Override
    public EventModel getEventModel(int position) {
        EventModel eventModel = new EventModel();
        eventModel.setName(eventModels.get(position).getName());
        eventModel.setEventName(eventModels.get(position).getEventName());

        if (eventModel.getEventName().equals(WAEventType.LOGIN)) { // TODO LOGIN
            eventModel.addEventValue(WAEventParameterName.LEVEL, 140);
        } else if (eventModel.getEventName().equals(WAEventType.INITIATED_PAYMENT)) { // TODO INITIATED_PAYMENT

        } else if (eventModel.getEventName().equals(WAEventType.COMPLETE_PAYMENT)) { // TODO COMPLETE_PAYMENT
            eventModel.addEventValue(WAEventParameterName.TRANSACTION_ID, "13241893274981237");
            eventModel.addEventValue(WAEventParameterName.PAYMENT_TYPE, WAConstants.CHANNEL_GOOGLE);
            eventModel.addEventValue(WAEventParameterName.CURRENCY_TYPE, WAConstants.CURRENCY_USD);
            eventModel.addEventValue(WAEventParameterName.CURRENCY_AMOUNT, 50.234f);
            eventModel.addEventValue(WAEventParameterName.VERTUAL_COIN_AMOUNT, 20000);
            eventModel.addEventValue(WAEventParameterName.VERTUAL_COIN_CURRENCY, "gold");
            eventModel.addEventValue(WAEventParameterName.IAP_ID, "1111111");
            eventModel.addEventValue(WAEventParameterName.IAP_NAME, "GGGGGG");
            eventModel.addEventValue(WAEventParameterName.IAP_AMOUNT, 20);
            eventModel.addEventValue(WAEventParameterName.LEVEL, 120);

        } else if (eventModel.getEventName().equals(WAEventType.LEVEL_ACHIEVED)) { // TODO LEVEL_ACHIEVED
            eventModel.addEventValue(WAEventParameterName.LEVEL, "120");
            eventModel.addEventValue(WAEventParameterName.SCORE, 3241234);
            eventModel.addEventValue(WAEventParameterName.FIGHTING, 1230020);

        } else if (eventModel.getEventName().equals(WAEventType.CUSTOM_EVENT_PREFIX)) { // TODO 自定义事件统计
            eventModel.addEventValue(WAEventParameterName.LEVEL, 140);
            eventModel.addEventValue("to_level", 141);
            eventModel.addEventValue("fight_force", 1232320);
            eventModel.addEventValue("to_fight_force", 1220020);
            eventModel.addEventValue(WAEventParameterName.SUCCESS, true);
        }

        return eventModel;
    }
}
