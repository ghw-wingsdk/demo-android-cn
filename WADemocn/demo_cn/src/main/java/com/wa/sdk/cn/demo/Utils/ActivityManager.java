package com.wa.sdk.cn.demo.Utils;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by hank on 16/8/3.
 */
public class ActivityManager {
    private static ActivityManager activityManager = null;

    private ArrayList<Activity> activityList = null;

    private ActivityManager() {
        activityList = new ArrayList<Activity>();
    }

    public static ActivityManager getInstance() {
        if (activityManager == null)
            activityManager = new ActivityManager();

        return activityManager;
    }

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void removeActivity(int index) {
        if (index < activityList.size()) {
            activityList.remove(index);
        }
    }

    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public void finishAll() {
        Activity activity = null;
        int count = activityList.size();
        for (int i = 0; i < count; i++) {
            activity = activityList.get(0);
            removeActivity(activity);
            activity.finish();
        }
    }
}
