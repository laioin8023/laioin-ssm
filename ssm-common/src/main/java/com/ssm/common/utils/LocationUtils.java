package com.ssm.common.utils;

import java.text.DecimalFormat;

public class LocationUtils {
    // 单位千米
    private static double EARTH_RADIUS = 6378.137;

    /**
     * 角度弧度计算公式 rad:(). <br/>
     * <p>
     * 360度=2π π=Math.PI
     * <p>
     * x度 = x*π/360 弧度
     *
     * @return
     */
    private static double getRadian(double degree) {
        return degree * Math.PI / 180.0;
    }

    /**
     * 依据经纬度计算两点之间的距离 GetDistance:(). <br/>
     *
     * @param lat1Str 1点的纬度
     * @param lng1Str 1点的经度
     * @param lat2Str 2点的纬度
     * @param lng2Str 2点的经度
     * @return 距离 单位 米
     */
    public static Double getDistance(String lat1Str, String lng1Str, String lat2Str, String lng2Str) {
        if (Tools.isEmpty(lat1Str, lng1Str, lat2Str, lng2Str)) {
            return Double.MAX_VALUE;
        }
        Double lat1 = Double.parseDouble(lat1Str);
        Double lng1 = Double.parseDouble(lng1Str);
        Double lat2 = Double.parseDouble(lat2Str);
        Double lng2 = Double.parseDouble(lng2Str);
        double radLat1 = getRadian(lat1);
        double radLat2 = getRadian(lat2);
        // 两点纬度差
        double a = radLat1 - radLat2;
        // 两点的经度差
        double b = getRadian(lng1) - getRadian(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
                * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return s * 1000;
    }

    /**
     * @param lat1Str 1点的纬度
     * @param lng1Str 1点的经度
     * @param lat2Str 2点的纬度
     * @param lng2Str 2点的经度
     * @return 距离：小于1000 显示 M，大于 1000 显示 千米
     */
    public static String getDistanceFormat(String lat1Str, String lng1Str, String lat2Str, String lng2Str) {
        Double distanceM = LocationUtils.getDistance(lat1Str, lng1Str, lat2Str, lng2Str);
        String distanceStr = "";
        if (distanceM < 1000) {
            // 显示m
            distanceStr = distanceM.intValue() + "m";
        } else if (distanceM >= 1000) {
            // 显示km
            DecimalFormat df = new DecimalFormat(".##");
            distanceM = distanceM / 1000;
            distanceStr = df.format(distanceM) + "km";
        }
        return distanceStr;
    }
}
