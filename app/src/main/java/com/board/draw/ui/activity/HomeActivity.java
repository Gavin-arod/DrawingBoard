package com.board.draw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

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

        CardView btnDrawing = findViewById(R.id.cv_drawing);
        CardView btnLocalDrawing = findViewById(R.id.cv_local_image);
        CardView btnAbout = findViewById(R.id.cv_about_us);

        btnDrawing.setOnClickListener(this);
        btnLocalDrawing.setOnClickListener(this);
        btnAbout.setOnClickListener(this);
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
        } else if (viewId == R.id.cv_about_us) {
            Intent intent = new Intent(HomeActivity.this, AboutUsActivity.class);
            startActivity(intent);
        }
        overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out);
    }
}
