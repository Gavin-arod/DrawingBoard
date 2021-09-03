package com.board.draw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;

import com.board.draw.R;
import com.board.draw.ui.activity.base.BaseActivity;
import com.board.draw.util.AssetsUtil;

/**
 * home page
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private long exitTime = 0L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        CardView btnDrawing = findViewById(R.id.cv_drawing);
        CardView btnLocalDrawing = findViewById(R.id.cv_local_image);
        CardView btnSetting = findViewById(R.id.cv_setting);
        AppCompatTextView tvDrawing = findViewById(R.id.tv_drawing);
        AppCompatTextView tvPicBooks = findViewById(R.id.tv_picture_books);
        AppCompatTextView tvSetting = findViewById(R.id.tv_setting);

        tvDrawing.setTypeface(AssetsUtil.getAssetsFont(HomeActivity.this));
        tvPicBooks.setTypeface(AssetsUtil.getAssetsFont(HomeActivity.this));
        tvSetting.setTypeface(AssetsUtil.getAssetsFont(HomeActivity.this));

        btnDrawing.setOnClickListener(this);
        btnLocalDrawing.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.cv_drawing) {
            Intent intent = new Intent(HomeActivity.this, DrawingBoardActivity.class);
            startActivity(intent);
        } else if (viewId == R.id.cv_local_image) {
            //本地保存的图片
            Intent intent = new Intent(HomeActivity.this, LocalPictureBookActivity.class);
            startActivity(intent);
        } else if (viewId == R.id.cv_setting) {
            Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
            startActivity(intent);
        }
        overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ||
                keyCode == KeyEvent.KEYCODE_ESCAPE) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(HomeActivity.this, getString(R.string.exit_app_when_press_again), Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                moveTaskToBack(false);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
