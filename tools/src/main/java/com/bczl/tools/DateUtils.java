package com.bczl.tools;



import com.bczl.tools.constant.DateFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

/**
 * 日期工具
 * <p>
 * Created by licheng1 on 2016/12/22.
 */
public class DateUtils {


    public static final String DEFAULT_FORMAT = DateFormatter.UTC_FULL.format();

    public static final String LOCAL_FORMAT = DateFormatter.LOCAL_SIMPLE.format();

    public static final TimeZone LOCAL_TIMEZONE = TimeZone.getDefault();

    public static final TimeZone UTC_TIMEZONE = TimeZone.getTimeZone("UTC");


    public static Date parse(String source) throws Exception {
        return parse(source, UTC_TIMEZONE);
    }

    public static Date parse(String source, TimeZone timeZone) throws Exception {
        return parse(source, matchFormat(source), timeZone);
    }

    public static Date parse(String source, String format, TimeZone timeZone) throws Exception {
        Objects.requireNonNull(source, "date parse source string cannot be null");
        Objects.requireNonNull(format, "date parse format string cannot be null");
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(timeZone);
        return dateFormat.parse(source);
    }


    public static String format(Date date) {
        return format(date, DEFAULT_FORMAT);
    }

    public static String format(Date date, String format) {
        return format(date, format, UTC_TIMEZONE);
    }

    public static String format(Date date, String format, TimeZone timeZone) {
        Objects.requireNonNull(date, "date format source date cannot be null");
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        if (Objects.nonNull(timeZone))
            dateFormat.setTimeZone(timeZone);
        return dateFormat.format(date);
    }

    public static String matchFormat(String date) {

        Objects.requireNonNull(date, "matchFormat date string is null");

        for (DateFormatter format : DateFormatter.values()) {
            String regex = format.pattern();
            if (date.matches(regex)) {
                return format.format();
            }
        }
        return null;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param sDate
     * @param eDate
     * @return
     * @throws ParseException
     */
    public static int daysBetween(String sDate, String eDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(sDate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(eDate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }


    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     *
     * @param date1
     * @param date2
     * @return days
     */
    public static int millisBetween(Date date1, Date date2) {
        int time = (int) ((date2.getTime() - date1.getTime()));
        return time;
    }


    //获取当前时间的n天前的日期
    public static String DateBeforeNDays(Date sDate, Integer n) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(sDate);
        c.add(Calendar.DATE, -n);
        Date date = c.getTime();
        return format.format(date);
    }

    /**
     * 获取当前时间的n年前的日期
     *
     * @param date
     * @param n
     * @return days
     */
    public static String yearBeforeNDays(Date date, Integer n) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, n);
        return format.format(c.getTime());
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     *
     * @param date1
     * @param date2
     * @return days
     */
    public static int differentDays(Date date1, Date date2) {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
        return days;
    }

}
