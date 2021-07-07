package com.board.draw.util;

import android.content.SharedPreferences;

public class SPUtil {

    /**
     * SharedPreferences 保存String类型
     *
     * @param key   key
     * @param value value
     */
    public static void putString(String key, String value) {
        SharedPreferences.Editor editor = Constant.SP.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * SharedPreferences 获取String类型
     *
     * @param key key
     * @return value
     */
    public static String getString(String key) {
        SharedPreferences sp = Constant.SP;
        return sp.getString(key, "");
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences.Editor et = Constant.SP.edit();
        et.putBoolean(key, value);
        et.apply();
        et.commit();
    }

    public static boolean getBoolean(String key) {
        return Constant.SP.getBoolean(key, false);
    }

    public static void putInt(String key, int type) {
        SharedPreferences.Editor et = Constant.SP.edit();
        et.putInt(key, type);
        et.apply();
        et.commit();
    }

    public static int getInt(String key) {
        return Constant.SP.getInt(key, -1);
    }

}
