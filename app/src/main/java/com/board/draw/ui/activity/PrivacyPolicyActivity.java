package com.board.draw.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.board.draw.R;
import com.board.draw.ui.activity.base.BaseActivity;
import com.board.draw.util.AssetsUtil;

/**
 * 隐私政策页面
 */
public class PrivacyPolicyActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        String linkType = getIntent().getStringExtra("linkType");

        findViewById(R.id.iv_privacy_back).setOnClickListener(v -> finish());

        AppCompatTextView tvContent = findViewById(R.id.tv_privacy_policy);
        AppCompatTextView tvTitle = findViewById(R.id.tv_title_content);
        tvContent.setTypeface(AssetsUtil.getAssetsFont(PrivacyPolicyActivity.this));
        tvTitle.setTypeface(AssetsUtil.getAssetsFont(PrivacyPolicyActivity.this));

        if (linkType != null) {
            switch (linkType) {
                case "1"://隐私政策
                    tvTitle.setText("隐私政策");
                    tvContent.setText(getString(R.string.content_privacy_policy));
                    break;
                case "2"://用户协议
                    tvTitle.setText("用户协议");
                    tvContent.setText(getString(R.string.content_user_agreement));
                    break;
            }
        }
    }
}
