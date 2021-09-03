package com.board.draw.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类：避免叠加显示
 */
public class ToastUtil {
    private static long curTime;

    public static void showMessage(Context context, String text, int duration) {
        if (System.currentTimeMillis() - curTime > duration) {
            Toast.makeText(context, text, duration).show();
            curTime = System.currentTimeMillis();
        }
    }
}
