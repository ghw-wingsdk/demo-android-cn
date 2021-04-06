package com.wa.sdk.cn.demo.tracking;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.viewpager.widget.ViewPager;

import com.wa.sdk.cn.demo.R;
import com.wa.sdk.cn.demo.WADemoConfig;
import com.wa.sdk.cn.demo.base.BaseActivity;
import com.wa.sdk.cn.demo.tracking.events.Event;
import com.wa.sdk.cn.demo.tracking.events.Event360;
import com.wa.sdk.cn.demo.tracking.events.EventBaidu;
import com.wa.sdk.cn.demo.tracking.events.EventWA;
import com.wa.sdk.cn.demo.widget.AdapterPager;
import com.wa.sdk.cn.demo.widget.TabBarView;
import com.wa.sdk.cn.demo.widget.TabBarView.OnItemSelectedListener;
import com.wa.sdk.cn.demo.widget.TitleBar;

import java.util.ArrayList;

/**
 * TrackingActivity
 * Created by hank on 16/9/6.
 */
public class TrackingActivity extends BaseActivity implements ViewPager.OnPageChangeListener, OnItemClickListener
        ,OnItemSelectedListener {
    private final String TAG = "TrackingActivity";
    private TitleBar tb = null;
    private TabBarView tabBarView = null;
    private ViewPager vp = null;

    private ArrayList<View> viewList = null;
    private Event[] events = null;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        Intent intent = getIntent();
        if (intent != null) {
            position = intent.getIntExtra("channel", 0);
        }

        initViews();

        if (position > 0)
            tabBarView.setSelectItemIndex(position);
    }

    private void initViews() {
        // 标题
        tb = findViewById(R.id.tb_tracking);
        tb.setLeftButton(android.R.drawable.ic_menu_revert, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tb.setTitleText(R.string.tracking);
        tb.setTitleTextColor(R.color.color_white);

        tabBarView = findViewById(R.id.tbv);
        tabBarView.setTitles(new String[]{"WA", "360", "百度"});
        tabBarView.setOnItemSelectedListener(this);

        events = new Event[]{new EventWA(), new Event360(),  new EventBaidu()};

        viewList = new ArrayList<>();
        ListView listView = null;
        for (int i = 0; i < events.length; i++) {
            listView = new ListView(this);
            listView.setTag(i + 1);
            listView.setDivider(new ColorDrawable(Color.parseColor("#FF808080")));
            listView.setDividerHeight(1);
            listView.setSelector(android.R.drawable.list_selector_background);
            listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, events[i].getNames()));
            listView.setOnItemClickListener(this);
            viewList.add(listView);
        }

        vp = findViewById(R.id.vp);
        vp.addOnPageChangeListener(this);
        vp.setAdapter(new AdapterPager(viewList));
    }

    @Override
    public void onItemSelected(int position, String title) {
        vp.setCurrentItem(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tabBarView.setSelectItemIndex(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Event event = events[(int)parent.getTag() - 1];
        EventModel eventModel = event.getEventModel(position);


        Intent intent = new Intent(TrackingActivity.this, TrackingSendActivity.class);
        intent.putExtra(WADemoConfig.EXTRA_EVENT_NAME, eventModel.getEventName());
        if (eventModel.getEventValues() != null)
            intent.putExtra(WADemoConfig.EXTRA_EVENT_VALUES, eventModel.getEventValues());
        startActivity(intent);
    }
}
