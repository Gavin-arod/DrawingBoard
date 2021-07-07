package com.board.draw.util;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public class ScreenUtil {

    /**
     * 当前是否是横屏
     *
     * @param context context
     * @return boolean
     */
    public static boolean isLandScape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 当前是否是竖屏
     *
     * @param context context
     * @return boolean
     */
    public static boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 获取手机X轴方向长度(width)
     */
    public static int getWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getRealMetrics(dm);
        Log.e("宽度：", dm.widthPixels + "");
        return dm.widthPixels;
    }

    /**
     * 获取手机Y轴方向长度(Height)
     */
    public static int getHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getRealMetrics(dm);
        Log.e("高度：", dm.heightPixels + "");
        return dm.heightPixels;
    }

    /**
     * 获取横屏状态下宽
     */
    public static int getHorizontalWidth(Context context) {
        int width = getWidth(context);
        int height = getHeight(context);
        return Math.max(height, width);
    }

    /**
     * 获取横屏状态下高
     */
    public static int getHorizontalHeight(Context context) {
        int width = getWidth(context);
        int height = getHeight(context);
        return Math.min(height, width);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
