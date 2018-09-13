package com.ssm.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 */
public class Tools {

    private static Logger LGR = LoggerFactory.getLogger(Tools.class);

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
            String check = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(19[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(mobileNumber);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 检查密码
     *
     * @param pwd
     * @return
     */
    public static boolean checkPassword(String pwd) {
        boolean flag = false;
        try {
            // 密码.任意字符，6 -12 位
            String check = "^.{6,12}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(pwd);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 获取订单号
     *
     * @param key
     * @return
     */
    public static String getOrderNo(String key) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddmmssSSS");
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append(sdf.format(new Date()));
        sb.append(Tools.getRandomNumberByLength(6));
        return sb.toString();
    }

    /**
     * 获取一定长度的随机数字
     *
     * @param length 指定数字长度
     * @return 一定长度的数字
     */
    public static String getRandomNumberByLength(int length) {
        String base = "0123456789";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = RANDOM_STR_LEM.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 利用java原生的摘要实现SHA256加密
     *
     * @param str 加密后的报文
     * @return
     */
    public static String getSHA256StrJava(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            LGR.error("", e);
        }
        return encodeStr;
    }

    /**
     * 将byte转为16进制
     *
     * @param bytes
     * @return
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

}
