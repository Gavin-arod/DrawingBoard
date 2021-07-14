package com.board.draw.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.board.draw.R;
import com.board.draw.ui.activity.base.BaseActivity;
import com.board.draw.util.BitmapBinder;
import com.board.draw.util.GlideApp;

/**
 * 查看图片
 */
public class ShowPicActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Bundle bundle = getIntent().getExtras();
        BitmapBinder bitmapBinder = (BitmapBinder) bundle.getBinder("localImage");
        Bitmap bitmap = bitmapBinder.getBitmap();

        String name = getIntent().getStringExtra("name");

        if (bitmap == null) {
            Toast.makeText(ShowPicActivity.this, "图片数据有误", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        AppCompatImageView imageView = findViewById(R.id.iv_pic);
        AppCompatTextView tvPicName = findViewById(R.id.tv_pic_name);

        tvPicName.setText(name);
        GlideApp.with(ShowPicActivity.this)
                .asBitmap()
                .load(bitmap)
                .into(imageView);
    }
}
