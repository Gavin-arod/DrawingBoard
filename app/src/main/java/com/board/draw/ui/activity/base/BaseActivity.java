package com.board.draw.ui.activity.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.board.draw.impl.SelectImageListener;
import com.board.draw.util.ImmersiveBarUtil;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

/**
 * base activity
 */
public class BaseActivity extends AppCompatActivity {
    private SelectImageListener selectImageListener;

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

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ImmersionBar immersionBar = ImmersionBar.with(this);
        immersionBar
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .init();
    }

    public void setSelectImageListener(SelectImageListener listener) {
        this.selectImageListener = listener;
    }

    /**
     * 返回相册选择Result
     */
    protected ActivityResultLauncher<String> launcher = registerForActivityResult(new ResultContract(), new ActivityResultCallback<Intent>() {
        @Override
        public void onActivityResult(Intent result) {
            selectImageListener.selectLocalResult(result.getData());
        }
    });

    /**
     * 打开系统相册
     */
    public static class ResultContract extends ActivityResultContract<String, Intent> {

        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, String input) {
            Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            return pickIntent;
        }

        @Override
        public Intent parseResult(int resultCode, @Nullable Intent intent) {
            if (resultCode == RESULT_OK && null != intent) {
                return intent;
            }
            return null;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out);
    }

}
