package com.wa.sdk.cn.demo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.wa.sdk.cn.demo.R;

import java.util.ArrayList;

/**
 * TabBarView
 * Created by hank on 16/9/7.
 */
public class TabBarView extends HorizontalScrollView implements View.OnClickListener {
    private final String TAG = "TabBarView";

    private Context mContext = null;

    private LinearLayout llContent = null;

    private ArrayList<String> titleList = null;
    private int itemWidth = 150;
    private int selectItemIndex = 0;

    private OnItemSelectedListener listener = null;

    public TabBarView(Context context) {
        super(context);
        mContext = context;
        initialize(null);
    }

    public TabBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initialize(attrs);
    }

    public TabBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initialize(attrs);
    }

    private void initialize(AttributeSet attrs) {
        setHorizontalScrollBarEnabled(false);
        setSmoothScrollingEnabled(true);

        titleList = new ArrayList<>();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);

        llContent = new LinearLayout(mContext);
        llContent.setOrientation(LinearLayout.HORIZONTAL);
        addView(llContent, params);
    }

    private void initData() {
        int screenWidth = this.getResources().getDisplayMetrics().widthPixels;
        if (itemWidth * titleList.size() < screenWidth)
            itemWidth = screenWidth / titleList.size();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                itemWidth, LayoutParams.MATCH_PARENT);

        llContent.removeAllViews();

        Button button = null;
        for (int i = 0; i < titleList.size(); i++) {
            button = new Button(mContext);
            button.setTag(i + 1);
            button.setBackgroundResource(R.drawable.selector_tab_view_bg);
            button.setText(titleList.get(i));
            button.setEnabled(selectItemIndex != i);
            button.setOnClickListener(this);
            llContent.addView(button, params);
        }
    }

    public void setTitles(String[] titles) {
        titleList.clear();
        if (titles != null)
            for (String title : titles)
                titleList.add(title);

        initData();
    }

    public void setTitles(ArrayList<String> titleList) {
        this.titleList.clear();
        if (titleList != null)
            this.titleList.addAll(titleList);

        initData();
    }

    private int newScrollX = 0;
    public void setSelectItemIndex(int index) {
        selectItemIndex = index;

        View viewChild = null;
        for (int i = 0; i < llContent.getChildCount(); i++) {
            viewChild = llContent.getChildAt(i);
            viewChild.setEnabled(selectItemIndex != i);
        }

        // Auto scroll
        int scrollX = getScrollX();
        int offset = 5;
        int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        if (scrollX > selectItemIndex * itemWidth - offset) { // 向前(左)自动滚动
            if (selectItemIndex > 0) // 如果点击的前面还有选项
                newScrollX = (selectItemIndex - 1) * itemWidth;
            else                     // 如果点击的是第一个选项
                newScrollX = 0;
        } else if (scrollX < (selectItemIndex + 1) * itemWidth - screenWidth + offset) { // 向后(右)自动滚动
            if (selectItemIndex + 1 < titleList.size()) // 如果点击的后面还有选项
                newScrollX = (selectItemIndex + 2) * itemWidth - screenWidth;
            else                                        // 如果点击的是最后个选项
                newScrollX = (selectItemIndex + 1) * itemWidth - screenWidth;
        }
        post(new Runnable() {
            @Override
            public void run() {
//                scrollTo(newScrollX, 0);
                smoothScrollTo(newScrollX, 0);
            }
        });

        if (listener != null)
            listener.onItemSelected(selectItemIndex, titleList.get(selectItemIndex));
    }

    public ArrayList<String> getTitleList() {
        return titleList;
    }

    @Override
    public void onClick(View v) {
        int index = ((int)v.getTag()) - 1;
        if (index != selectItemIndex) {
            setSelectItemIndex(index);
        }
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int position, String title);
    }
}
