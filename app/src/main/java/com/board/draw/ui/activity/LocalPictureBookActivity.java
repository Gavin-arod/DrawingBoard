package com.board.draw.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.board.draw.R;
import com.board.draw.adapter.LocalImagesAdapter;
import com.board.draw.constants.LocalPic;
import com.board.draw.impl.ClickLocalPicItemListener;
import com.board.draw.ui.activity.base.BaseActivity;
import com.board.draw.util.BitmapBinder;
import com.board.draw.util.FileUtil;

import java.util.ArrayList;

/**
 * 本地保存的绘本列表页面
 */
public class LocalPictureBookActivity extends BaseActivity implements ClickLocalPicItemListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_book);

        RecyclerView rvImages = findViewById(R.id.rv_local_images);
        AppCompatTextView tvNoneImage = findViewById(R.id.tv_none_local_image);

        ArrayList<LocalPic> imageList = FileUtil.getDrawSaveFileList(LocalPictureBookActivity.this);
        if (imageList.size() == 0) {
            tvNoneImage.setVisibility(View.VISIBLE);
            rvImages.setVisibility(View.GONE);
            tvNoneImage.setOnClickListener(v -> {
                Intent intent = new Intent(LocalPictureBookActivity.this, DrawingBoardActivity.class);
                startActivity(intent);
                overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out);
            });
            return;
        }

        rvImages.setVisibility(View.VISIBLE);
        tvNoneImage.setVisibility(View.GONE);

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvImages.setLayoutManager(manager);
        LocalImagesAdapter adapter = new LocalImagesAdapter(LocalPictureBookActivity.this);
        adapter.setItemClickListener(this);
        adapter.setData(imageList);
        rvImages.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void clickLocalPic(LocalPic localPic) {
        Intent intent = new Intent(LocalPictureBookActivity.this, ShowPicActivity.class);
        Bitmap bitmap = BitmapFactory.decodeFile(localPic.getPath());
        Bundle bundle = new Bundle();
        //putBinder是利用共享内存,能满足大图传输
        bundle.putBinder("localImage", new BitmapBinder(bitmap));
        intent.putExtra("name", localPic.getName());
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out);
    }


}
