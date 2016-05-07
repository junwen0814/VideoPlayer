package com.junwen.videoplayer.utils;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by 俊文 on 2016/3/15.
 */
public class DateUtils {

    private static DateUtils mDateUtils;

    private static StringBuilder mFormatBuilder;
    private static Formatter mFormatter;

    /**
     * 描述 : 单例模式
     * 作者 : 卜俊文
     * 日期 : 2016/3/15 22:58
     * 邮箱：344176791@qq.com
     */
    public static DateUtils getInstance() {
        if (mDateUtils == null) {
            synchronized (DateUtils.class) {
                if (mDateUtils == null) {
                    mDateUtils = new DateUtils();
                }
            }
        }
        return mDateUtils;
    }

    public DateUtils() {
        // 转换成字符串的时间
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    }

    /**
     * 把毫秒转换成：1:20:30这里形式
     *
     * @param timeMs
     * @return
     */
    public  String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;

        int minutes = (totalSeconds / 60) % 60;

        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds)
                    .toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }
}
