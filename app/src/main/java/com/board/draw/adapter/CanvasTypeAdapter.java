package com.board.draw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.board.draw.R;
import com.board.draw.adapter.holder.CanvasTypeHolder;
import com.board.draw.constants.CanvasType;
import com.board.draw.impl.CanvasTypeClickListener;
import com.board.draw.util.GlideApp;

import java.util.List;

/**
 * 画布适配器
 */
public class CanvasTypeAdapter extends RecyclerView.Adapter<CanvasTypeHolder> {
    private final Context mContext;
    private List<CanvasType> data;
    private final CanvasTypeClickListener canvasTypeClickListener;
    //已选中的
    private int selectedPos = -1;

    public CanvasTypeAdapter(Context mContext, CanvasTypeClickListener listener) {
        this.mContext = mContext;
        this.canvasTypeClickListener = listener;
    }

    public void setData(List<CanvasType> canvasTypeList) {
        this.data = canvasTypeList;
    }

    @NonNull
    @Override
    public CanvasTypeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_canvas_type, parent, false);
        return new CanvasTypeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CanvasTypeHolder holder, int position) {
        CanvasType canvasType = data.get(position);
        if (canvasType.isSelected()) {
            holder.getParentView().setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_canvas_selected));
        } else {
            holder.getParentView().setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_canvas_unselected));
        }

        GlideApp.with(mContext)
                .load(canvasType.getGraphicsId())
                .into(holder.getLogoView());

        holder.getParentView().setOnClickListener(v -> {
            canvasType.setSelected(selectedPos != holder.getAdapterPosition());
            selectedPos = holder.getAdapterPosition();
            holder.getParentView().setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_canvas_selected));
            canvasTypeClickListener.clickCanvasItemType(canvasType);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
