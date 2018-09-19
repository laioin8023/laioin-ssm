package com.ssm.common.utils;

import java.time.LocalDate;
import java.util.Date;

/**
 * 星座工具类
 * 
 */
public class ConstellationUtil {
	/**
	 * 星座日期界限
	 */
	static int[] DAY_ARR = new int[] { 20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22 };
    // 12星座
    static  String[] CONSTELLATION_ARR = new String[] { "摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座",
            "天蝎座", "射手座", "摩羯座" };
	public static String getConstellation(int month, int day) {
		return day < DAY_ARR[month - 1] ? CONSTELLATION_ARR[month - 1]
				: CONSTELLATION_ARR[month];
	}

	public static String getConstellation(Date date) {
		LocalDate localDate = DateTimeUtil.date2LocalDate(date);
		return getConstellation(localDate.getMonthValue(), localDate.getDayOfMonth());
	}

	public static void main(String[] args) {
		LocalDate localDate = LocalDate.of(1991, 05, 04);
		Date date = DateTimeUtil.localDate2Date(localDate);
		System.out.println(getConstellation(date));
		System.out.println(getConstellation(3, 21));
	}
}
