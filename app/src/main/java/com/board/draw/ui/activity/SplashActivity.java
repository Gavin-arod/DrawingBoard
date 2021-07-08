package com.board.draw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.board.draw.R;
import com.board.draw.ui.activity.base.BaseActivity;

/**
 * 启动页
 */
public class SplashActivity extends BaseActivity {
    private final Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actiivty_splash);
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
        }, 3500);
    }
}
