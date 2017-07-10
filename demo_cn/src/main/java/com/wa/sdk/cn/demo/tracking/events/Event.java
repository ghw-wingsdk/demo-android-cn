package com.wa.sdk.cn.demo.tracking.events;

import com.wa.sdk.cn.demo.tracking.EventModel;

import java.util.ArrayList;

/**
 * Event
 * Created by hank on 16/9/6.
 */
public abstract class Event {
    protected ArrayList<EventModel> eventModels = null;

    public ArrayList<String> getNames() {
        ArrayList<String> names = new ArrayList<>();

        if (eventModels != null) {
            for (EventModel eventModel : eventModels) {
                names.add(eventModel.getName() + " " + eventModel.getEventName());
            }
        }

        return names;
    }

    public abstract EventModel getEventModel(int position);

    public abstract String getChannel();
}
