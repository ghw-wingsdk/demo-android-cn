package com.wa.sdk.cn.demo.tracking.events;

import com.wa.sdk.WAConstants;
import com.wa.sdk.cn.demo.tracking.EventModel;
import com.wa.sdk.track.WAEventParameterName;
import com.wa.sdk.track.WAEventType;

import java.util.ArrayList;

/**
 * Event Chartboost
 * Created by hank on 16/9/6.
 */
public class EventCB extends Event {

    public EventCB() {
        if (eventModels == null) {
            eventModels = new ArrayList<>();
            eventModels.add(new EventModel("自定义事件统计", WAEventType.CUSTOM_EVENT_PREFIX));
        }
    }

    @Override
    public String getChannel() {
        return WAConstants.CHANNEL_BAIDU;
    }

    @Override
    public EventModel getEventModel(int position) {
        EventModel eventModel = new EventModel();
        eventModel.setName(eventModels.get(position).getName());
        eventModel.setEventName(eventModels.get(position).getEventName());

        if (eventModel.getEventName().equals(WAEventType.CUSTOM_EVENT_PREFIX)) { // TODO 自定义事件统计
            eventModel.addEventValue(WAEventParameterName.LEVEL, 140);
            eventModel.addEventValue("to_level", 141);
            eventModel.addEventValue("fight_force", 1232320);
            eventModel.addEventValue("to_fight_force", 1220020);
            eventModel.addEventValue(WAEventParameterName.SUCCESS, true);
        }

        return eventModel;
    }
}
