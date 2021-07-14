package com.board.draw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.board.draw.R;
import com.board.draw.adapter.holder.LocalImageHolder;
import com.board.draw.constants.LocalPic;
import com.board.draw.impl.ClickLocalPicItemListener;
import com.board.draw.util.GlideApp;

import java.util.List;

/**
 * 本地绘本适配器
 */
public class LocalImagesAdapter extends RecyclerView.Adapter<LocalImageHolder> {
    private List<LocalPic> data;
    private final Context mContext;
    private ClickLocalPicItemListener clickLocalPicItemListener;

    public void setItemClickListener(ClickLocalPicItemListener itemClickListener) {
        this.clickLocalPicItemListener = itemClickListener;
    }

    public void setData(List<LocalPic> data) {
        this.data = data;
    }

    public LocalImagesAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public LocalImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_local_image, parent, false);
        return new LocalImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalImageHolder holder, int position) {
        LocalPic localPic = data.get(position);

        String name = localPic.getName();
        String[] splitName = name.split("\\.");
        holder.getNameView().setText(splitName[0]);

        GlideApp.with(mContext)
                .asBitmap()
                .load(localPic.getBitmap())
                .into(holder.getPicView());

        holder.itemView.setOnClickListener(v ->
                clickLocalPicItemListener.clickLocalPic(localPic));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
