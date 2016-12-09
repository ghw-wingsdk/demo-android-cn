package com.wa.sdk.cn.demo.tracking;

import java.util.HashMap;
import java.util.Map;

/**
 * Event Model
 * Created by hank on 16/9/6.
 */
public class EventModel {
    private String name = null;
    private String eventName = null;
    private HashMap<String, Object> eventValues = null;

    public EventModel() {
        eventValues = new HashMap<>();
    }

    public EventModel(String name, String eventName) {
        this.name = name;
        this.eventName = eventName;
        eventValues = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public HashMap<String, Object> getEventValues() {
        return eventValues;
    }

    public void addEventValue(String key, Object value) {
        this.eventValues.put(key, value);
    }

    public void addEventValues(Map<String, Object> eventValues) {
        this.eventValues.putAll(eventValues);
    }

    public void changeEventValues(Map<String, Object> eventValues) {
        this.eventValues.clear();
        this.eventValues.putAll(eventValues);
    }
}
