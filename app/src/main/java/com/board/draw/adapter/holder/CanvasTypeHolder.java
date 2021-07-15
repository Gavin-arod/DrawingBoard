package com.board.draw.adapter.holder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.board.draw.R;
import com.google.android.material.imageview.ShapeableImageView;

public class CanvasTypeHolder extends RecyclerView.ViewHolder {
    private ConstraintLayout parentView;
    private ShapeableImageView logoView;

    public ConstraintLayout getParentView() {
        return parentView;
    }

    public void setParentView(ConstraintLayout parentView) {
        this.parentView = parentView;
    }

    public ShapeableImageView getLogoView() {
        return logoView;
    }

    public void setLogoView(ShapeableImageView logoView) {
        this.logoView = logoView;
    }

    public CanvasTypeHolder(@NonNull View itemView) {
        super(itemView);
        setParentView(itemView.findViewById(R.id.parent_view));
        setLogoView(itemView.findViewById(R.id.item_canvas));
    }
}
