package com.board.draw.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.board.draw.R;
import com.board.draw.adapter.holder.BackImageHolder;
import com.board.draw.impl.ItemClickListener;
import com.board.draw.util.GlideApp;

import java.util.List;

public class BackImageAdapter extends RecyclerView.Adapter<BackImageHolder> {
    private final Context mContext;
    private List<Bitmap> data;
    private final ItemClickListener itemClickListener;

    public BackImageAdapter(Context mContext, ItemClickListener listener) {
        this.mContext = mContext;
        this.itemClickListener = listener;
    }

    public void setData(List<Bitmap> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public BackImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_select_background, parent, false);
        return new BackImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BackImageHolder holder, int position) {
        Bitmap bitmap = data.get(position);

        GlideApp.with(mContext)
                .asBitmap()
                .load(bitmap)
                .into(holder.getBackImageView());

        holder.itemView.setOnClickListener(v -> {
            if (position == data.size() - 1) {
                //打开本地相册
                itemClickListener.openLocalAlbum();
            } else {
                itemClickListener.itemClick(bitmap);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
