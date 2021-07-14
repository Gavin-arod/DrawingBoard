package com.board.draw.util;

import android.graphics.Bitmap;
import android.os.Binder;

public class BitmapBinder extends Binder {
    private final Bitmap bitmap;

    public BitmapBinder(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
