package com.board.draw.ui.activity.base;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.board.draw.util.ImmersiveBarUtil;

/**
 * base activity
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态栏导航栏
        ImmersiveBarUtil.hideBar(getWindow().getDecorView());
        ImmersiveBarUtil.setNavigationStatusColor(getWindow());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //隐藏状态栏导航栏
        ImmersiveBarUtil.hideBar(getWindow().getDecorView());
        ImmersiveBarUtil.setNavigationStatusColor(getWindow());
    }
}
