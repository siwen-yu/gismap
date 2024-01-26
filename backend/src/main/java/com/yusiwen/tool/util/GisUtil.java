package com.yusiwen.tool.util;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GisUtil implements Serializable {

    private GisUtil() {
    }

    /**
     * 经纬度倍率(10000000.0)
     */
    public static final int IRATIO = 10000000;

    /**
     * 经度-米(倍率:100)
     */
    public static final int LOT_PER_METER = 100;

    /**
     * 纬度-米(倍率:90)
     */
    public static final int LAT_PER_METER = 90;

    /**
     * 栅格大小-米
     */
    public static final int METER = 40;

    /**
     * 获取经度所属栅格的归属x
     *
     * @param longitude 经度
     * @param meter     栅格大小-米
     * @return
     */
    public static int getX(int longitude, int meter) {
        return longitude / (meter * LOT_PER_METER);
    }

    /**
     * 获取纬度所属栅格的归属y
     *
     * @param latitude 纬度
     * @param meter    栅格大小-米
     * @return
     */
    public static int getY(int latitude, int meter) {
        return latitude / (meter * LAT_PER_METER);
    }

    /**
     * 根据所属栅格的归属x获取经度
     *
     * @param x     所属栅格的归属x
     * @param meter 栅格大小-米
     * @return
     */
    public static int getLot(int x, int meter) {
        return x * (meter * LOT_PER_METER);
    }

    /**
     * 根据所属栅格的归属y获取纬度
     *
     * @param y     属栅格的归属y
     * @param meter 栅格大小-米
     * @return
     */
    public static int getLat(int y, int meter) {
        return y * (meter * LAT_PER_METER);
    }

    /**
     * 获取左上经度
     *
     * @param lot   经度
     * @param meter 米
     * @return
     */
    public static int getLTLot(int lot, int meter) {
        // 左下X
        int x = getX(lot, meter);
        return getLot(x, meter);
    }

    /**
     * 获取右下经度
     *
     * @param lot   经度
     * @param meter 米
     * @return
     */
    public static int getRBLot(int lot, int meter) {
        // 左下X
        int x = getX(lot, meter);
        return getLot(x + 1, meter);
    }

    /**
     * 获取左上纬度
     *
     * @param lat   纬度
     * @param meter 米
     * @return
     */
    public static int getLTLat(int lat, int meter) {
        // 左下Y
        int y = getY(lat, meter);
        return getLat(y + 1, meter);
    }

    /**
     * 获取右下纬度
     *
     * @param lat   纬度
     * @param meter 米
     * @return
     */
    public static int getRBLat(int lat, int meter) {
        // 左下Y
        int y = getY(lat, meter);
        return getLat(y, meter);
    }

    /**
     * 得到一个大致的圆形区域所有点（145）
     *
     * @param longitude 圆心经度
     * @param latitude  圆心纬度
     * @param radius    半径(m)
     * @return
     */
    public static List<Pair<Integer, Integer>> getRotundity(int longitude, int latitude, int radius) {
        // 取圆上145个点
        return getRotundity(longitude, latitude, radius, 145);
    }


    /**
     * 得到一个大致的圆形区域所有点
     *
     * @param longitude 圆心经度
     * @param latitude  圆心纬度
     * @param radius    半径(m)
     * @param pointSize 点的数量
     * @return 点列表
     */
    public static List<Pair<Integer, Integer>> getRotundity(int longitude, int latitude, int radius, int pointSize) {
        List<Pair<Integer, Integer>> pointList = new ArrayList<>();
        for (int i = 0; i < pointSize; i++) {
            int lng = longitude + (int) (radius * LOT_PER_METER * Math.cos(2 * i * Math.PI / pointSize));
            int lat = latitude - (int) (radius * LAT_PER_METER * Math.sin(2 * i * Math.PI / pointSize));
            pointList.add(new ImmutablePair<>(lng, lat));
        }
        return pointList;
    }

    public static double getLngLatDouble(int i) {
        return ((double) i) / GisUtil.IRATIO;
    }

    public static int getLngLatInt(double d) {
        return (int) (d * GisUtil.IRATIO);
    }
}
