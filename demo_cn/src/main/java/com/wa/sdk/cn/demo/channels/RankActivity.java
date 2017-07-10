package com.wa.sdk.cn.demo.channels;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wa.sdk.WAConstants;
import com.wa.sdk.cn.demo.R;
import com.wa.sdk.cn.demo.base.BaseActivity;
import com.wa.sdk.cn.demo.widget.AdapterPager;
import com.wa.sdk.cn.demo.widget.TitleBar;
import com.wa.sdk.common.model.WACallback;
import com.wa.sdk.social.WASocialProxy;
import com.wa.sdk.social.model.WARankObject;
import com.wa.sdk.social.model.WARankResult;

import java.util.ArrayList;

/**
 * 360 排行
 * Created by hank on 16/7/26.
 */
public class RankActivity extends BaseActivity implements OnPageChangeListener, AdapterView.OnItemClickListener {
    private TitleBar mTitleBar = null;
    private ViewPager vp = null;

    private ArrayList<View> viewList = null;
    private int loadCount = 0;
    private ArrayList<WARankObject> rankList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        initViews();

        showLoadingDialog("加载排行榜...", null);
        queryRankFriend();
        queryGlobalRankList();
    }

    private void initViews() {
        mTitleBar = (TitleBar)findViewById(R.id.tb_rank);
        mTitleBar.setTitleTextColor(R.color.color_white);
        mTitleBar.setTitleText(R.string.rank);
        mTitleBar.setLeftButton(android.R.drawable.ic_menu_revert, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewList = new ArrayList<View>();
        ListView listView = null;
        for (int i = 0; i < 2; i++) {
            listView = new ListView(this);
            listView.setTag(i + 1);
            listView.setBackgroundColor(Color.TRANSPARENT);
            listView.setDivider(new ColorDrawable(Color.parseColor("#FF808080")));
            listView.setDividerHeight(1);
            listView.setCacheColorHint(Color.TRANSPARENT);
            listView.setOnItemClickListener(this);
            viewList.add(listView);
        }

        vp = (ViewPager) findViewById(R.id.vp);
        vp.addOnPageChangeListener(this);
        vp.setAdapter(new AdapterPager(viewList));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_rank_friend)
            vp.setCurrentItem(0);
        else if (v.getId() == R.id.btn_global_rank)
            vp.setCurrentItem(1);
        else if (v.getId() == R.id.btn_global_rank_ui)
            queryGlobalRankListUI();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void queryRankFriend() {
        WASocialProxy.queryRankFriend(this, WAConstants.CHANNEL_QIHU360, 0, true, new WACallback<WARankResult>() {
            @Override
            public void onSuccess(int code, String message, WARankResult result) {
                loadCount++;
                if (loadCount >= 2)
                    cancelLoadingDialog();

                ArrayList<String> datas = new ArrayList<String>();
                for (WARankObject ranModel : result.getRankList()) {
                    datas.add(ranModel.getNick());
                }

                ListView listView = (ListView)viewList.get(0);
                listView.setAdapter(new ArrayAdapter<String>(RankActivity.this, android.R.layout.simple_list_item_1, datas));
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(int code, String message, WARankResult result, Throwable throwable) {
                showShortToast(message);
            }
        });
    }

    private void queryGlobalRankList() {
        WASocialProxy.queryGlobalRankList(this, WAConstants.CHANNEL_QIHU360, 0, true, new WACallback<WARankResult>() {
            @Override
            public void onSuccess(int code, String message, WARankResult result) {
                loadCount++;
                if (loadCount >= 2)
                    cancelLoadingDialog();

                rankList = new ArrayList<WARankObject>();
                rankList.addAll(result.getRankList());

                ArrayList<String> datas = new ArrayList<String>();
                for (WARankObject rankModel : rankList) {
                    datas.add(rankModel.getNick());
                }

                ListView listView = (ListView)viewList.get(1);
                listView.setAdapter(new ArrayAdapter<String>(RankActivity.this, android.R.layout.simple_list_item_1, datas));
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(int code, String message, WARankResult result, Throwable throwable) {
                showShortToast(message);
            }
        });
    }

    private void queryGlobalRankListUI() {
        WASocialProxy.queryGlobalRankListUI(this, WAConstants.CHANNEL_QIHU360, 0, new WACallback<Integer>() {
            @Override
            public void onSuccess(int code, String message, Integer result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(int code, String message, Integer result, Throwable throwable) {
                showShortToast(message);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        WARankObject rankModel = rankList.get(position);
        // TODO 获取根据ID用户信息 (目前360使用)
        WASocialProxy.queryUserDetail(this, WAConstants.CHANNEL_QIHU360, rankModel.getId(), new WACallback<String>() {
            @Override
            public void onSuccess(int code, String message, String result) {
                showShortToast(result);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(int code, String message, String result, Throwable throwable) {

            }
        });
    }
}
