package com.board.draw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.board.draw.R;
import com.board.draw.adapter.holder.BrushSelectHolder;
import com.board.draw.constants.BrushType;
import com.board.draw.impl.OnBrushTypeSelectListener;
import com.board.draw.util.AssetsUtil;
import com.board.draw.util.SPUtil;

import java.util.List;

/**
 * brush type selection adapter
 */
public class BrushSelectAdapter extends RecyclerView.Adapter<BrushSelectHolder> {
    private final Context mContext;
    private List<BrushType> data;
    private final OnBrushTypeSelectListener brushTypeSelectListener;

    public BrushSelectAdapter(Context mContext, OnBrushTypeSelectListener listener) {
        this.mContext = mContext;
        this.brushTypeSelectListener = listener;
    }

    public void setData(List<BrushType> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public BrushSelectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_dialog_brush, parent, false);
        return new BrushSelectHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrushSelectHolder holder, int position) {
        BrushType brushType = data.get(position);
        holder.getBrushView().setTypeface(AssetsUtil.getAssetsFont(mContext));
        holder.getBrushView().setText(brushType.getName());

        int selectedBrush = SPUtil.getInt("selectedBrushType");
        if (selectedBrush == -1) {
            if (position == 0) {
                selectedItem(holder);
                return;
            }
            unSelectItem(holder);
        } else {
            if (selectedBrush == brushType.getType()) {
                selectedItem(holder);
            } else {
                unSelectItem(holder);
            }
        }

        holder.itemView.setOnClickListener(v ->
                brushTypeSelectListener.onSelectBrushType(brushType));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void selectedItem(BrushSelectHolder holder) {
        holder.getItemParentView().setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_item_brush_clicked));
        holder.getBrushView().setTextColor(ContextCompat.getColor(mContext, R.color.purple_500));
    }

    private void unSelectItem(BrushSelectHolder holder) {
        holder.getItemParentView().setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_item_brush_unclicked));
        holder.getBrushView().setTextColor(ContextCompat.getColor(mContext, R.color.color_ff202020));
    }

}
