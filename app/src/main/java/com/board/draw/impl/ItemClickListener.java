package com.board.draw.impl;

import android.graphics.Bitmap;

public interface ItemClickListener {
    void itemClick(Bitmap bitmap);

    void openLocalAlbum();
}