package com.ssm.common.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 */
public class Tools {

    private static final Random RANDOM_STR_LEM = new Random();

    /**
     * 获取客户端IP地址
     *
     * @param request
     * @return
     */

    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果是多级代理，那么取第一个ip为客户ip,但是为了防止伪造,取最后一个由Azure封装的真实IP
        if (ip != null && ip.indexOf(",") != -1) {
            ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
        }
        if (ip != null && ip.contains(":")) {
            ip = ip.substring(0, ip.indexOf(":"));
        }
        return ip;
    }

    /**
     * 获取一定长度的随机字符串
     *
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = RANDOM_STR_LEM.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 是否有空(String为""和Collection.size为0也判定为空)
     */
    public static boolean isEmpty(Object... os) {
        for (Object o : os) {
            if (o == null) {
                return true;
            } else if (o instanceof String) {
                String s = (String) o;
                if ("".equals(s)) {
                    return true;
                }
            } else if (o instanceof Collection) {
                Collection<?> c = (Collection<?>) o;
                if (c.size() == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证手机号码
     *
     * @param mobileNumber
     * @return
     */
    public static boolean checkPhone(String mobileNumber) {
        boolean flag = false;
        try {//19942364101
            Pattern regex = Pattern.compile("^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(19[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$");
            Matcher matcher = regex.matcher(mobileNumber);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
}
