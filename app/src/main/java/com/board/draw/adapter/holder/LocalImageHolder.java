package com.board.draw.adapter.holder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.board.draw.R;
import com.google.android.material.imageview.ShapeableImageView;

public class LocalImageHolder extends RecyclerView.ViewHolder {
    private ShapeableImageView picView;
    private AppCompatTextView nameView;

    public ShapeableImageView getPicView() {
        return picView;
    }

    public void setPicView(ShapeableImageView picView) {
        this.picView = picView;
    }

    public AppCompatTextView getNameView() {
        return nameView;
    }

    public void setNameView(AppCompatTextView nameView) {
        this.nameView = nameView;
    }

    public LocalImageHolder(@NonNull View itemView) {
        super(itemView);
        setPicView(itemView.findViewById(R.id.iv_image_cover));
        setNameView(itemView.findViewById(R.id.tv_image_name));
    }
}
