package com.learning.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @ClassName TimeUtil
 * @Description 时间工具类, 负责时间序列化及转换
 * @Author jiashudong
 * @Date 2020/5/18 16:15
 * @Version 1.0
 */
public class TimeUtil {

    private static final Logger log = LoggerFactory.getLogger(TimeUtil.class);

    /**
     * @Description 非标准时间字符串转换
     * @Param [str]
     * @Return java.lang.String
     * @Author hufei
     * @Date 2022/8/11 18:57
     */
    public static String formatTimeStr(String str) {
        if (null == str || str.matches("^\\d{4}(-\\d{2}){2} \\d{2}(:\\d{2}){2}$")) {
            return str;
        }
        //处理 null、none、nan、nat等无效时间的情况
        if (str.equals("空") || str.toLowerCase().startsWith("n")) {
            return null;
        }
        str = str.replace("T", " ").replace("/", "-").replace("Z", "");
        if (str.contains(".")) {
            str = str.substring(0, str.indexOf("."));
        }
        boolean hasSubStr = str.contains(" ");
        String dateStr;
        String timeStr;
        if (hasSubStr) {
            String[] tmp = str.split(" ");
            dateStr = tmp[0];
            timeStr = tmp[1];
        } else {
            if (str.contains(":")) {
                dateStr = "";
                timeStr = str;
            } else {
                dateStr = str;
                timeStr = "";
            }
        }
        return doFormatTimeStr(false, dateStr, "-", 1) +
                " " +
                doFormatTimeStr(true, timeStr, ":", 0);
    }

    private static String doFormatTimeStr(boolean isTime, String str, String regex, int initValue) {
        if (str.isEmpty()) {
            return isTime ? "00:00:00" : LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        StringBuilder builder = new StringBuilder();
        String[] tmp = str.split(regex);
        int len = tmp.length;
        for (int i = 0; i < 3; i++) {
            int var;
            if (i < len) {
                var = Integer.parseInt(tmp[i]);
            } else {
                var = initValue;
            }
            if (i == 0) {
                builder.append(String.format("%0" + (isTime ? 2 : 4) + "d", var));
            } else {
                builder.append(String.format("%02d", var));
            }
            if (i < 2) {
                builder.append(regex);
            }
        }
        return builder.toString();
    }

    /**
     * @Description 获取当前时间
     * @Param []
     * @Return String
     * @Author hufei
     * @Date 2020/5/6 15:32
     */
    public static String getCurrentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * @Description 时间戳转时间
     * @Param []
     * @Return String
     * @Author hufei
     * @Date 2020/5/6 15:32
     */
    public static String getTimeStr(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * @Description 时间转时间戳
     * @Param []
     * @Return long
     * @Author hufei
     * @Date 2020/5/6 15:32
     */
    public static long getTimestamp(String time) {
        return LocalDateTime.parse(
                formatTimeStr(time),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        ).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }
}