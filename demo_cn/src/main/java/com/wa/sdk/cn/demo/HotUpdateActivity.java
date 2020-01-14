package com.wa.sdk.cn.demo;

import android.os.Bundle;
import android.view.View;

import com.wa.sdk.cn.demo.widget.TitleBar;
import com.wa.sdk.common.model.WACallback;
import com.wa.sdk.common.utils.LogUtil;
import com.wa.sdk.cn.demo.base.BaseActivity;
import com.wa.sdk.hup.WAHupProxy;
import com.wa.sdk.hup.model.WAUpdateInfo;
import com.wa.sdk.cn.demo.R;

/**
 *
 * Created by yinglovezhuzhu@gmail.com on 2016/7/14.
 */
public class HotUpdateActivity extends BaseActivity {

    WAUpdateInfo mUpdateInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hot_update);

        initView();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_check_update:
                checkUpdate();
                break;
            case R.id.btn_download_patch:
                downloadPatch();
                break;
            default:
                break;
        }
    }

    private void initView() {
        TitleBar titleBar = findViewById(R.id.tb_hot_update);
        titleBar.setTitleText(R.string.hot_update);
        titleBar.setLeftButton(android.R.drawable.ic_menu_revert, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBar.setTitleTextColor(R.color.color_white);
    }

    public void checkUpdate() {
        WAHupProxy.checkUpdate(new WACallback<WAUpdateInfo>() {
            @Override
            public void onSuccess(int code, String message, WAUpdateInfo result) {
                String text = "Check SDK update success" + result.toString();
                LogUtil.d(LogUtil.TAG, text);
                mUpdateInfo = result;
                showLongToast(text);
            }

            @Override
            public void onCancel() {
                LogUtil.d(LogUtil.TAG, "Check SDK update canceled");
            }

            @Override
            public void onError(int code, String message, WAUpdateInfo result, Throwable throwable) {
                String text = "Check SDK update failed:" + message
                        + "\n--->>>" + (null == result ? "" : result.toString())
                        + "\n--->" + (null == throwable ? "" : LogUtil.getStackTrace(throwable));
                LogUtil.d(LogUtil.TAG, text);
                showLongToast(text);
            }
        });
    }

    public void downloadPatch() {
        WAHupProxy.startUpdate(mUpdateInfo, new WACallback<String>() {
            @Override
            public void onSuccess(int code, String message, String result) {
                String text = "Update succeed! patch file: " + result;
                LogUtil.d(LogUtil.TAG, text);
                showLongToast(text);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(int code, String message, String result, Throwable throwable) {
                String text = "Download patch file failed: " + (null == result ? "" : result) + "\n"
                        + (null == throwable ? "" : LogUtil.getStackTrace(throwable));
                LogUtil.e(LogUtil.TAG, text);
                showLongToast(text);
            }
        });
    }
}
