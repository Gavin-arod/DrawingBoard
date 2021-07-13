package com.board.draw.ui.activity.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.board.draw.util.ImmersiveBarUtil;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

/**
 * base activity
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式状态栏
        ImmersionBar immersionBar = ImmersionBar.with(this);
        immersionBar
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .init();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //隐藏状态栏导航栏
        ImmersiveBarUtil.hideBar(getWindow().getDecorView());
        ImmersiveBarUtil.setNavigationStatusColor(getWindow());
    }
}
