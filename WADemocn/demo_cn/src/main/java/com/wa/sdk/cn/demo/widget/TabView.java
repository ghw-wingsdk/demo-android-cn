package com.wa.sdk.cn.demo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.wa.sdk.cn.demo.R;



/**
 * Logcat tab
 * Created by yinglovezhuzhu@gmail.com on 16-3-22.
 */
public class TabView extends ALinearLayout {

    private TextView mTvTitle;

    public TabView(Context context) {
        super(context);
        initView(context);
    }

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public TabView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    public void setTextSize(float size) {
        mTvTitle.setTextSize(size);
    }

    public void setTitle(CharSequence title) {
        mTvTitle.setText(title);
    }

    public void setTitle(int resId) {
        mTvTitle.setText(resId);
    }

    private void initView(Context context) {
        inflate(context, R.layout.layout_tab_view, this);

        mTvTitle = (TextView) findViewById(R.id.tv_tab_view_title);
    }
}
