package com.board.draw.adapter.holder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.board.draw.R;

public class BrushSelectHolder extends RecyclerView.ViewHolder {
    private ConstraintLayout itemParentView;
    private AppCompatTextView brushView;

    public ConstraintLayout getItemParentView() {
        return itemParentView;
    }

    public void setItemParentView(ConstraintLayout itemParentView) {
        this.itemParentView = itemParentView;
    }

    public AppCompatTextView getBrushView() {
        return brushView;
    }

    public void setBrushView(AppCompatTextView brushView) {
        this.brushView = brushView;
    }

    public BrushSelectHolder(@NonNull View itemView) {
        super(itemView);
        setItemParentView(itemView.findViewById(R.id.item_root_view));
        setBrushView(itemView.findViewById(R.id.btn_brush_type));
    }
}
