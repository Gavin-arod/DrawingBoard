package com.board.draw.util;

import android.content.SharedPreferences;

public class Constant {
    //设置画图样式
    //路径
    private static final int DRAW_PATH = 0x001;
    //圆
    private static final int DRAW_CIRCLE = 0x002;
    //长方形
    private static final int DRAW_RECTANGLE = 0x003;
    //箭头
    private static final int DRAW_ARROW = 0x004;

    //本地存储
    public static SharedPreferences SP;
}
