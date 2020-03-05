package com.wa.sdk.cn.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.wa.sdk.cn.demo.base.BaseActivity;
import com.wa.sdk.cn.demo.widget.TitleBar;
import com.wa.sdk.csc.WACscProxy;
import com.wa.sdk.common.WACommonProxy;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;


/**
 * 测试客服系統
 * 按照說明對應著來
 */
public class CscActivity extends BaseActivity {

    private TitleBar mTitlebar;
    private String faqId = "";
    private String sectionPublishId = "";
    private EditText mEtFaq;
    private EditText mEtSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Demo的初始化，跟SDK无关
        WASdkDemo.getInstance().initialize(this);

        setContentView(R.layout.activity_csc);

        initView();

        WACscProxy.setSDKLanguage("zh_CN");
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        WACommonProxy.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (WACommonProxy.onRequestPermissionsResult(this, requestCode, permissions, grantResults)) {
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelLoadingDialog();
    }

    String showConversationFlag = "1";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show_elva: {
                HashMap<String, Object> map = new HashMap();
                map.put("private_welcome_str", "hi");

                HashMap<String, Object> config = new HashMap();
                config.put("elva-custom-metadata", map);
                WACscProxy.showElva(showConversationFlag, config);
                break;
            }
            case R.id.btn_show_elva_op: {
                WACscProxy.showElvaOP(showConversationFlag, null);
                break;
            }
            case R.id.btn_show_faqs: {
                HashMap<String, Object> config = new HashMap();
                config.put("showContactButtonFlag", "1");
                config.put("showConversationFlag", "1");
                //config.put("directConversation", "1");//若打开此项则直接跳到人工客服
                WACscProxy.showFAQs(config);
                break;
            }
            case R.id.btn_show_conversation: {
                WACscProxy.showConversation(null);
                break;
            }
            case R.id.btn_show_single_faq: {
                faqId = mEtFaq.getText().toString().trim();
                if (TextUtils.isEmpty(faqId)) {
                    showShortToast("faqId不能为空");
                } else {
                    WACscProxy.showSingleFAQ(faqId, null);
                }
                break;
            }
            case R.id.btn_show_faq_section: {
                sectionPublishId = mEtSection.getText().toString().trim();
                if (TextUtils.isEmpty(sectionPublishId)) {
                    showShortToast("sectionPublishId不能为空");
                } else {
                    WACscProxy.showFAQSection(sectionPublishId, null);
                }

                break;
            }
            case R.id.btn_openAiHelp: {
                if (WACscProxy.isOpenAiHelp()) {
                    WACscProxy.openAiHelp(null);
                }
                break;
            }
            case R.id.btn_isOpenAiHelp: {
                String tip = WACscProxy.isOpenAiHelp() ? "已开启" : "未开启";
                showShortToast(tip);
                break;
            }
            default:
                break;
        }
    }

    private void initView() {
        mEtFaq = findViewById(R.id.et_show_single_faq);
        mEtSection = findViewById(R.id.et_show_faq_section);
        mTitlebar = (TitleBar) findViewById(R.id.tb_csc);
        mTitlebar.setTitleText(R.string.csc);
        mTitlebar.setLeftButton(android.R.drawable.ic_menu_revert, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
        mTitlebar.setTitleTextColor(R.color.color_white);
    }

    public void exit() {
        finish();
    }

}
