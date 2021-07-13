package com.board.draw.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间、日期转换
 */
public class DateUtil {

    /**
     * 时间戳转换为当前日期
     */
    public static String timeToDate() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        return dateFormat.format(date);
    }
}
