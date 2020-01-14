package com.wa.sdk.cn.demo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wa.sdk.WAConstants;
import com.wa.sdk.cn.demo.base.BaseActivity;
import com.wa.sdk.cn.demo.model.UserModel;
import com.wa.sdk.cn.demo.widget.TitleBar;
import com.wa.sdk.common.WACommonProxy;
import com.wa.sdk.common.model.WACallback;
import com.wa.sdk.common.model.WAResult;
import com.wa.sdk.common.utils.StringUtil;
import com.wa.sdk.pay.WAPayProxy;
import com.wa.sdk.pay.model.WAPurchaseResult;
import com.wa.sdk.pay.model.WASkuDetails;
import com.wa.sdk.pay.model.WASkuResult;
import com.wa.sdk.user.WAUserProxy;

import java.util.ArrayList;
import java.util.List;

/**
 * 网页支付页面
 * Created by ghw on 16/5/8.
 */
public class PaymentActivity extends BaseActivity {
    private final String TAG = "PaymentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payment);

        TitleBar titleBar = findViewById(R.id.tb_payment);
        titleBar.setTitleText(R.string.payment);
        titleBar.setLeftButton(android.R.drawable.ic_menu_revert, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBar.setTitleTextColor(R.color.color_white);

        WAPayProxy.initialize(this, new WACallback<WAResult>(){

            @Override
            public void onSuccess(int code, String message, WAResult result) {
                Log.d(TAG,"WAPayProxy.initialize success");
                showLongToast("支付初始化成功");
            }

            @Override
            public void onCancel() {
                Log.d(TAG,"PayUIActitivy:WAPayProxy.initialize has been cancelled.");
            }

            @Override
            public void onError(int code, String message, WAResult result, Throwable throwable) {
                Log.d(TAG,"WAPayProxy.initialize error");
                showLongToast("支付初始化失败");
            }
        });

        showLoadingDialog("正在查询库存....", null);
        WAPayProxy.queryInventory(new WACallback<WASkuResult>() {
            @Override
            public void onSuccess(int code, String message, WASkuResult result) {

                List<String> waProductIdList = new ArrayList<>();
                for (WASkuDetails waSkudetails : result.getSkuList()) {
                    if(waSkudetails!=null && !StringUtil.isEmpty(waSkudetails.getSku()))
                    waProductIdList.add(waSkudetails.getSku());
                }

                if (waProductIdList.size() > 0){
                    ListView listView = findViewById(R.id.lv_payment_sku);
                    ArrayAdapter<String> payUIAdapter = new ArrayAdapter<String>(PaymentActivity.this, R.layout.payui_item, waProductIdList);
                    listView.setAdapter(payUIAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            TextView tv = (TextView)view;
                            if (UserModel.getInstance().isLogin())
                                payUI(tv.getText().toString(), "extInfotest");
                            else
                                showShortToast("请先登录");

                        }
                    });
                }

                cancelLoadingDialog();
                showLongToast("查询商品成功");

            }

            @Override
            public void onCancel() {
                cancelLoadingDialog();
                showLongToast("取消查询商品");
            }

            @Override
            public void onError(int code, String message, WASkuResult result, Throwable throwable) {
                cancelLoadingDialog();
                showLongToast("查询商品失败，请稍后再试");
            }
        });

    }

    private void payUI(String waProductId, String extInfo){
        if(!WAPayProxy.isPayServiceAvailable(this)) {
            showShortToast("支付不可用");
            return;
        }
//        showLoadingDialog("支付中...", null);
        WAPayProxy.payUI(this, waProductId, extInfo, new WACallback<WAPurchaseResult>() {
            @Override
            public void onSuccess(int code, String message, WAPurchaseResult result) {
//                cancelLoadingDialog();
                if (WAUserProxy.getCurrChannel().equals(WAConstants.CHANNEL_UC)) {
                    Log.d(TAG, "下单成功");
                    showLongToast("下单成功");
                } else {
                    Log.d(TAG, "支付成功");
                    showLongToast("支付成功");
                }
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "取消支付");
//                cancelLoadingDialog();
                showLongToast("取消支付");
            }

            @Override
            public void onError(int code, String message, WAPurchaseResult result, Throwable throwable) {
                Log.d(TAG, "支付失败: " + message);
//                cancelLoadingDialog();
                showLongToast(StringUtil.isEmpty(message) ? "此时无法使用支付" : message);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelLoadingDialog();
        WAPayProxy.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (WACommonProxy.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
