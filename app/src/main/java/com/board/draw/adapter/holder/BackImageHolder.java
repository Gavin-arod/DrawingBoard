package com.board.draw.adapter.holder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.board.draw.R;
import com.google.android.material.imageview.ShapeableImageView;

public class BackImageHolder extends RecyclerView.ViewHolder {
    private ShapeableImageView backImageView;

    public ShapeableImageView getBackImageView() {
        return backImageView;
    }

    public void setBackImageView(ShapeableImageView backImageView) {
        this.backImageView = backImageView;
    }

    public BackImageHolder(@NonNull View itemView) {
        super(itemView);
        setBackImageView(itemView.findViewById(R.id.item_pic));
    }
}
