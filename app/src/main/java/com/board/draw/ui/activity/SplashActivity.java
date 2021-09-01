package com.board.draw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.board.draw.R;
import com.board.draw.ui.activity.base.BaseActivity;
import com.board.draw.util.AssetsUtil;

/**
 * 启动页
 */
public class SplashActivity extends BaseActivity {
    private final Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actiivty_splash);

        //设置字体
        AppCompatTextView appName = findViewById(R.id.app_name);
        appName.setTypeface(AssetsUtil.getAssetsFont(SplashActivity.this));

        autoToHomePage();
    }

    /**
     * 跳转到首页
     */
    private void autoToHomePage() {
        mHandler.postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out);
        }, 2000);
    }
}
