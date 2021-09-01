package com.board.draw.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.board.draw.R;
import com.board.draw.app.MyApp;
import com.board.draw.ui.activity.base.BaseActivity;
import com.board.draw.util.AssetsUtil;

/**
 * about us page
 */
public class AboutUsActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        AppCompatTextView tvAboutUs = findViewById(R.id.about_us_text);
        AppCompatTextView tvDesc = findViewById(R.id.tv_desc_content);
        //设置字体
        tvAboutUs.setTypeface(AssetsUtil.getAssetsFont(AboutUsActivity.this));
        tvDesc.setTypeface(AssetsUtil.getAssetsFont(AboutUsActivity.this));

        findViewById(R.id.iv_back).setOnClickListener(v -> finish());

        AppCompatTextView tvVersion = findViewById(R.id.tv_app_version);
        tvVersion.setTypeface(AssetsUtil.getAssetsFont(AboutUsActivity.this));
        tvVersion.setText("版本号：v-".concat(MyApp.curVersionName));
    }
}
