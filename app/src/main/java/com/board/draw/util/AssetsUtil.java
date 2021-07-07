package com.board.draw.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AssetsUtil {

    public static List<Bitmap> getAssetsFiles(Context context) {
        String picPath = "images";
        InputStream inputStream;
        List<Bitmap> images = new ArrayList<>();
        try {
            AssetManager assetManager = context.getAssets();
            String[] picsList = assetManager.list(picPath);
            if (picsList != null) {
                for (String s : picsList) {
                    if (s.startsWith("bg_pic")) {
                        Log.e("路径：", s);
                        inputStream = assetManager.open(picPath + "/" + s);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        images.add(bitmap);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return images;
    }
}
