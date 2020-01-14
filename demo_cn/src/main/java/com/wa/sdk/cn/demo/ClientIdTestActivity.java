package com.wa.sdk.cn.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wa.sdk.cn.demo.base.BaseActivity;
import com.wa.sdk.common.utils.StringUtil;
import com.wa.sdk.core.WACoreProxy;
import com.wa.sdk.core.WASdkProperties;

/**
 * Desc:
 * Created by zgq on 2020/1/9.
 */
public class ClientIdTestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientid_test);
        initView();
    }


    private void initView() {
        Button btnSetClientid = findViewById(R.id.btn_set_clientid);
        Button btnClearClientid = findViewById(R.id.btn_clear_clientid);
        final EditText edtClientid = findViewById(R.id.edt_clientid);

        btnClearClientid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WACoreProxy.clearClientId(ClientIdTestActivity.this);
                showShortToast("清除成功");
                finish();
            }
        });

        btnSetClientid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringUtil.isEmpty(edtClientid.getText().toString())){
                    showShortToast("clientid不能为空");
                    return;
                }
                WASdkProperties.getInstance().setClientId(edtClientid.getText().toString());
                showShortToast("设置成功");
                edtClientid.setText("");
                finish();
            }
        });
    }
}
