package com.power.grid.plan.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author wangyucheng on 2020/8/20 9:53 下午
 */
public class DateUtils {


    public static final String FORMAT_DEFALUT = "MM/dd/yyyy";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    private DateUtils() {
    }

    /**
     * 获取SimpleDateFormat
     *
     * @param parttern 日期格式
     * @return SimpleDateFormat对象
     * @throws RuntimeException 异常：非法日期格式
     */
    private static SimpleDateFormat getDateFormat(String parttern) {
        return new SimpleDateFormat(parttern);
    }


    /**
     * 将日期字符串转化为日期。失败返回null。
     *
     * @param date     日期字符串
     * @param parttern 日期格式
     * @return 日期
     */
    public static Date stringToDate(String date, String parttern) throws ParseException {
        Date myDate = null;
        if (date != null) {
            myDate = getDateFormat(parttern).parse(date);
        }
        return myDate;
    }


    /**
     * 将日期转化为日期字符串。失败返回null。
     *
     * @param date     日期
     * @param parttern 日期格式
     * @return 日期字符串
     */
    public static String dateToString(Date date, String parttern) {
        String dateString = null;
        if (date != null) {
            dateString = getDateFormat(parttern).format(date);
        }
        return dateString;
    }



}
