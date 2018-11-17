package com.bczl.tools.constant;

import java.util.TimeZone;

/**
 * 时间格式匹配枚举
 * <p>
 * Created by licheng1 on 2016/12/23.
 */
public enum DateFormatter {

    UTC_FULL("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", TimeZone.getTimeZone("UTC")),
    UTC_MS_TWO_BIT("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{2}Z", "yyyy-MM-dd'T'HH:mm:ss.SS'Z'", TimeZone.getTimeZone("UTC")),
    UTC_MS_ONE_BIT("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{1}Z", "yyyy-MM-dd'T'HH:mm:ss.S'Z'", TimeZone.getTimeZone("UTC")),
    UTC_NO_MS("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z", "yyyy-MM-dd'T'HH:mm:ss'Z'", TimeZone.getTimeZone("UTC")),
    LOCAL_SIMPLE("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}", "yyyy-MM-dd HH:mm:ss", TimeZone.getDefault()),
    LOCAL_SIMPLE_FULL("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}", "yyyy-MM-dd HH:mm:ss.SSS", TimeZone.getDefault()),
    LOCAL_SIMPLE_NO_SECONDS("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}", "yyyy-MM-dd HH:mm", TimeZone.getDefault()),
    LOCAL_SIMPLE_DATE("\\d{4}-\\d{2}-\\d{2}", "yyyy-MM-dd", TimeZone.getDefault()),
    LOCAL_SIMPLE_NO_FORMAT("\\d{14}", "yyyyMMddHHmmss", TimeZone.getDefault());

    private String pattern;
    private String format;
    private TimeZone timeZone;

    DateFormatter(String pattern, String format, TimeZone timeZone) {
        this.pattern = pattern;
        this.format = format;
        this.timeZone = timeZone;
    }

    public String pattern() {
        return pattern;
    }


    public String format() {
        return format;
    }

    public TimeZone timeZone() {
        return timeZone;
    }
}
