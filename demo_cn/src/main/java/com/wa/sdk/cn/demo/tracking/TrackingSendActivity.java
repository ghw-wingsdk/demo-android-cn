package com.wa.sdk.cn.demo.tracking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.wa.sdk.cn.demo.R;
import com.wa.sdk.cn.demo.WADemoConfig;
import com.wa.sdk.cn.demo.base.BaseActivity;
import com.wa.sdk.cn.demo.base.BaseFragment;
import com.wa.sdk.cn.demo.tracking.fragment.DefaultEventFragment;
import com.wa.sdk.cn.demo.widget.TitleBar;
import com.wa.sdk.common.utils.StringUtil;
import com.wa.sdk.track.model.WAEvent;

import java.util.HashMap;

/**
 * TrackingSend
 * Created by yinglovezhuzhu@gmail.com on 2016/1/28.
 */
public class TrackingSendActivity extends BaseActivity {
//    private String channel = null;
    private String mDefaultEventName = null;
    private float mDefaultValue = 0.0f;
    private HashMap<String, Object> mDefaultEventValues = new HashMap<String, Object>();

//    private Map<String, Boolean> mChannelSwitchMap = new HashMap<String, Boolean>();
//    private Map<String, String> mEventNameMap = new HashMap<String, String>();
//    private Map<String, Float> mValueMap = new HashMap<String, Float>();
//    private Map<String, Map<String, Object>> mEventValueMap = new HashMap<String, Map<String,Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_send);

        initData();
        initView();
    }

    private void initView() {
        initTitlebar();

        BaseFragment fragment = null;

//        if (mDefaultEventName == null || "".equals(mDefaultEventName)) {
//            Bundle afArgs = new Bundle();
//            afArgs.putString(WADemoConfig.EXTRA_DATA, channel);
////            mFtTabHost.addTab(mFtTabHost.newTabSpec("Default").setIndicator(waTracking), CustomEventFragment.class, afArgs);
//            fragment = new CustomEventFragment();
//        } else {
            Bundle defaultEvent = new Bundle();
            defaultEvent.putString(WADemoConfig.EXTRA_EVENT_NAME, mDefaultEventName);
            defaultEvent.putFloat(WADemoConfig.EXTRA_COUNT_VALUE, mDefaultValue);
            defaultEvent.putSerializable(WADemoConfig.EXTRA_EVENT_VALUES, mDefaultEventValues);
            DefaultEventFragment defaultEventFragment = new DefaultEventFragment();
//            mFtTabHost.addTab(mFtTabHost.newTabSpec("Default").setIndicator(waTracking), DefaultEventFragment.class, defaultEvent);
            fragment = new DefaultEventFragment();
            fragment.setArguments(defaultEvent);
//        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_tracking_channel_container, fragment);
        transaction.commit();
    }

    private void initData() {
        Intent intent = getIntent();
        if(intent.hasExtra(WADemoConfig.EXTRA_EVENT_NAME)) {
            mDefaultEventName = intent.getStringExtra(WADemoConfig.EXTRA_EVENT_NAME);
        } else {
            finish();
            showShortToast("缺少事件名称参数");
        }
        if(intent.hasExtra(WADemoConfig.EXTRA_COUNT_VALUE)) {
            mDefaultValue = intent.getFloatExtra(WADemoConfig.EXTRA_EVENT_VALUES, 0.0f);
        }
        if(intent.hasExtra(WADemoConfig.EXTRA_EVENT_VALUES)) {
            HashMap<String, Object> eventValues = (HashMap<String, Object>) intent.getSerializableExtra(WADemoConfig.EXTRA_EVENT_VALUES);
            if(null != eventValues && !eventValues.isEmpty()) {
                mDefaultEventValues.putAll(eventValues);
            }
        }

    }

    private void initTitlebar() {
        TitleBar tb = findViewById(R.id.tb_tracking_send);
        tb.setLeftButton(android.R.drawable.ic_menu_revert, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tb.setTitleText(R.string.send_tracking);
        tb.setTitleTextColor(R.color.color_white);
        tb.setRightButtonTextColorResource(R.color.color_white);
        tb.setRightButtonWithText(R.string.send, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTracking();
                showShortToast(R.string.event_send);
            }
        });
    }

    private void sendTracking() {
        WAEvent.Builder eventBuilder = new WAEvent.Builder()
                .setDefaultEventName(mDefaultEventName)
                .setDefaultValue(mDefaultValue)
                .setDefaultEventValues(mDefaultEventValues);

//        Set<String> channelSwitchKeys = mChannelSwitchMap.keySet();
//        for(String channel : channelSwitchKeys) {
//            if(mChannelSwitchMap.get(channel)) {
//                eventBuilder.disableChannel(channel);
//            }
//        }
//
//        Set<String> channelEventKeys = mEventNameMap.keySet();
//        for(String channel : channelEventKeys) {
//            eventBuilder.setChannelEventName(channel, mEventNameMap.get(channel));
//        }
//
//        Set<String> channelValueKeys = mValueMap.keySet();
//        for(String channel : channelValueKeys) {
//            eventBuilder.setChannelValue(channel, mValueMap.get(channel));
//        }
//
//        Set<String> channelValuesKeys = mEventValueMap.keySet();
//        for(String channel : channelValuesKeys) {
//            eventBuilder.setChannelEventValues(channel, mEventValueMap.get(channel));
//        }
//
        eventBuilder.build().track(TrackingSendActivity.this);

    }

    public void onEventNameChanged(String oldName, String newName) {
//        if(StringUtil.isEmpty(newName)) {
//            mEventNameMap.remove(channel);
//        } else {
//            mEventNameMap.put(channel, newName);
//        }
        mDefaultEventName = newName;
    }

    public void onCountValueChanged(float oldValue, float newValue) {
        mDefaultValue = newValue;
//        mValueMap.put(channel, newValue);
    }

    public void onParameterChanged(String key, boolean isKey, Object oldValue, Object newValue) {
        if(isKey) {
            if(StringUtil.isEmpty(key)) {
                mDefaultEventValues.put(String.valueOf(newValue), "");
            } else {
                Object value = mDefaultEventValues.get(key);
                mDefaultEventValues.remove(key);
                mDefaultEventValues.put(String.valueOf(newValue), value);
            }
        } else {
            if(null == newValue) { // newValue 为null，删除
                mDefaultEventValues.remove(key);
            } else {
        mDefaultEventValues.put(key, newValue);
            }
        }
    }
}
