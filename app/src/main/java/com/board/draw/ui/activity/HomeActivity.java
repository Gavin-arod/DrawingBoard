package com.board.draw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.board.draw.R;
import com.board.draw.ui.activity.base.BaseActivity;

/**
 * home page
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        AppCompatButton btnDrawing = findViewById(R.id.btn_Drawing);
        AppCompatButton btnAbout = findViewById(R.id.btn_about);

        btnDrawing.setOnClickListener(this);
        btnAbout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btn_Drawing) {
            Intent intent = new Intent(HomeActivity.this, CornerPathActivity.class);
            startActivity(intent);
            overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out);
        } else if (viewId == R.id.btn_about) {
            Intent intent = new Intent(HomeActivity.this, AboutUsActivity.class);
            startActivity(intent);
            overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out);
        }
    }
}
