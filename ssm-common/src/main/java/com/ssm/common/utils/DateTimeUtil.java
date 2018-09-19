package com.ssm.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * date 工具类
 */
public class DateTimeUtil {
    /**
     * 类型年
     */
    public static final byte TYPE_YEAR = 1;
    /**
     * 类型月
     */
    public static final byte TYPE_MONTH = 2;
    /**
     * 类型日
     */
    public static final byte TYPE_DAY = 3;
    /**
     * 类型时
     */
    public static final byte TYPE_HOUR = 4;
    /**
     * 类型分
     */
    public static final byte TYPE_MINUTE = 5;
    /**
     * 类型秒
     */
    public static final byte TYPE_SECOND = 6;


    /**
     * Date ---> LocalDate
     *
     * @param date
     * @return LocalDate
     */
    public static LocalDate date2LocalDate(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * LocalDate ---> Date
     *
     * @param localDate
     * @return Date
     */
    public static Date localDate2Date(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 将时间字符串转换为Date类型
     *
     * @param dateStr
     * @return Date
     */
    public static Date toDate(String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        return convertLocalDateToDate(date);
    }

    /**
     * 将LocalDate类型的时间转换为Date
     *
     * @param localDate
     * @return Date
     */
    public static Date convertLocalDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 根据出生日期获取人的年龄
     *
     * @param dateBirthDate (yyyy-mm-dd or yyyy/mm/dd)
     * @return
     */
    public static String getPersonAgeByBirthDate(Date dateBirthDate) {
        if (dateBirthDate == null) {
            return "";
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime localDateTime = convertDateToLocalDateTime(dateBirthDate);
        String strBirthDate = localDateTime.format(dtf);

        // 读取当前日期
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DATE);
        // 计算年龄
        int age = year - Integer.parseInt(strBirthDate.substring(0, 4)) - 1;
        if (Integer.parseInt(strBirthDate.substring(5, 7)) < month) {
            age++;
        } else if (Integer.parseInt(strBirthDate.substring(5, 7)) == month
                && Integer.parseInt(strBirthDate.substring(8, 10)) <= day) {
            age++;
        }
        return String.valueOf(age);
    }

    /**
     * 将Date类型的时间转换为LocalDateTime
     *
     * @param date
     * @return LocalDateTime
     */
    public static LocalDateTime convertDateToLocalDateTime(Date date) {
        return LocalDateTime
                .ofInstant(date.toInstant(), ZoneId.systemDefault())
                .withNano(0);
    }

    /**
     * 给时间累加
     *
     * @param date   时间
     * @param type   类型时分秒，天
     * @param amount 数字：+ 号表示向后累加时间 未来， - 表示向前推移时间，过去
     * @return
     */
    public static Date addDate(Date date, int type, int amount) {
        if (date == null) {
            return null;
        }
        // 读取当前日期
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        switch (type) {
            case TYPE_YEAR:
                c.add(Calendar.YEAR, amount);
                break;
            case TYPE_MONTH:
                c.add(Calendar.MONTH, amount);
                break;
            case TYPE_DAY:
                c.add(Calendar.DATE, amount);
                break;
            case TYPE_HOUR:
                c.add(Calendar.HOUR, amount);
                break;
            case TYPE_MINUTE:
                c.add(Calendar.MINUTE, amount);
                break;
            case TYPE_SECOND:
                c.add(Calendar.SECOND, amount);
                break;

            default:
                break;
        }
        return c.getTime();
    }
}
