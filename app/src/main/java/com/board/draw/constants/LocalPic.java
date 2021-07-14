package com.board.draw.constants;

import android.graphics.Bitmap;

/**
 * 本地图片实体类
 */
public class LocalPic {
    private String path;
    private Bitmap bitmap;
    private String name;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
