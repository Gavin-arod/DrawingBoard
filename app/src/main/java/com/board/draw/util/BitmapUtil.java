package com.board.draw.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtil {

    public static Bitmap getBitmap(View view) {
        Bitmap newBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        view.draw(canvas);
        return newBitmap;
    }

    /**
     * 把bitmap保存到本地
     */
    public static void saveBitmapToLocal(Context context, View view, String fileName) {
        try {
            String filePath = context.getExternalFilesDir(null) + "/";
            File dirFile = new File(filePath);
            if (!dirFile.exists()) {
                try {
                    boolean result = dirFile.mkdirs();
                    if (result) {
                        Log.i("---", "文件创建成功!");
                    }
                } catch (Exception e) {
                    Log.e("Exception：", e.getMessage());
                }
            }
            File file = new File(filePath, fileName + ".png");
            FileOutputStream fos = new FileOutputStream(file);
            getBitmap(view).compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

            // 把文件插入到系统图库
//            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
