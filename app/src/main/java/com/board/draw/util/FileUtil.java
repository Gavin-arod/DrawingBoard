package com.board.draw.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class FileUtil {

    /**
     * 崩溃日志文件路径
     * SDCard/Android/data/<application package>/cache
     * data/data/<application package>/cache
     */
    public static String getCrashLogPath(Context context) {
        return getCachePath(context) + File.separator + "crashLogs";
    }

    /**
     * 获取app缓存路径
     * SDCard/Android/data/<application package>/cache
     * data/data/<application package>/cache
     */
    public static String getCachePath(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //外部存储可用
            cachePath = context.getExternalCacheDir().getAbsolutePath();
        } else {
            //外部存储不可用
            cachePath = context.getCacheDir().getAbsolutePath();
        }
        return cachePath;
    }

}
