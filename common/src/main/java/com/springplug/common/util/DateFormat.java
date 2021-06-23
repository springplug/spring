package com.springplug.common.util;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.springplug.common.util.string.StringUtils;

import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat extends StdDateFormat {

    private static final long serialVersionUID = -3201781773655300201L;

    public static final DateFormat instance = new DateFormat();

    @Override
    /**
     * 只要覆盖parse(String)这个方法即可
     */
    public Date parse(String dateStr, ParsePosition pos) {
        return getDate(dateStr, pos);
    }

    @Override
    public Date parse(String dateStr) {
        ParsePosition pos = new ParsePosition(0);
        return getDate(dateStr, pos);
    }

    private Date getDate(String dateStr, ParsePosition pos) {
        SimpleDateFormat sdf = null;
        if (StringUtils.isBlank(dateStr)) {
            return null;
        } else if (dateStr.matches("^\\d{4}-\\d{1,2}$")) {
            sdf = new SimpleDateFormat("yyyy-MM");
            return sdf.parse(dateStr, pos);
        } else if (dateStr.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(dateStr, pos);
        } else if (dateStr.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return sdf.parse(dateStr, pos);
        } else if (dateStr.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(dateStr, pos);
        } else if (dateStr.length() == 23) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            return sdf.parse(dateStr, pos);
        }
        return super.parse(dateStr, pos);
    }

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        return new StringBuffer(String.valueOf(date.getTime()));
    }

    @Override
    public DateFormat clone() {
        return new DateFormat();
    }

}
