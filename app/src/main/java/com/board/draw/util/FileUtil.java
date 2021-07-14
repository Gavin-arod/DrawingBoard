package com.board.draw.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.board.draw.constants.LocalPic;

import java.io.File;
import java.util.ArrayList;

public class FileUtil {

    /**
     * 崩溃日志文件路径
     * SDCard/Android/data/<application package>/cache
     */
    public static String buildCrashLogPath(Context context) {
        return context.getExternalFilesDir(null) + File.separator + "crashLogs";
    }

    /**
     * 绘画图片保存路径
     */
    public static String buildDrawSavePath(Context context) {
        return context.getExternalFilesDir(null) + File.separator + "PictureBook";
    }

    /**
     * 获取本地保存的绘画图片列表
     */
    public static ArrayList<LocalPic> getDrawSaveFileList(Context context) {
        ArrayList<LocalPic> localPics = new ArrayList<>();
        String filePath = buildDrawSavePath(context);
        File PicFile = new File(filePath);
        String[] allFiles = PicFile.list();
        if (allFiles == null) {
            return localPics;
        }

        for (String file : allFiles) {
            String imagePath = filePath.concat(File.separator).concat(file);
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            LocalPic localPic = new LocalPic();
            if (bitmap != null) {
                localPic.setBitmap(bitmap);
                localPic.setName(file);
                localPic.setPath(imagePath);
            }
            localPics.add(localPic);
        }
        return localPics;
    }

}
