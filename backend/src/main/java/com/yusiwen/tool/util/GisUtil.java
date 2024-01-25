package com.yusiwen.tool.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    private static int lotGridSize = METER * LOT_PER_METER;
    private static int latGridSize = METER * LAT_PER_METER;

    /**
     * 获取经度所属栅格的归属x
     *
     * @param longitude 经度
     * @return
     */
    public static int getX(int longitude) {
        return longitude / lotGridSize;
    }

    /**
     * 获取纬度所属栅格的归属y
     *
     * @param latitude 纬度
     * @return
     */
    public static int getY(int latitude) {
        return latitude / latGridSize;
    }

    /**
     * 根据所属栅格的归属x获取经度
     *
     * @param x 所属栅格的归属x
     * @return
     */
    public static int getLot(int x) {
        return x * lotGridSize;
    }

    /**
     * 根据所属栅格的归属y获取纬度
     *
     * @param y 属栅格的归属y
     * @return
     */
    public static int getLat(int y) {
        return y * latGridSize;
    }

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
     * 获取经度对应的真实x
     *
     * @param longitude 经度
     * @param meter     栅格大小-米
     * @return
     */
    public static double getDoubleX(int longitude, int meter) {
        return (double) longitude / (meter * LOT_PER_METER);
    }

    /**
     * 获取纬度对应的真实y
     *
     * @param latitude 纬度
     * @param meter    栅格大小-米
     * @return
     */
    public static double getDoubleY(int latitude, int meter) {
        return (double) latitude / (meter * LAT_PER_METER);
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
     * 获取栅格中心经度
     *
     * @param lot   经度
     * @param meter 栅格大小
     * @param type  经度当前位置点类型 0-左 1-右
     * @return
     */
    public static int getMeterLot(int lot, int meter, int type) {
        int partLot = (int) (0.5 * meter * LOT_PER_METER);
        switch (type) {
            case 0:
                return lot + partLot;
            case 1:
                return lot - partLot;
            default:
                return 0;
        }
    }

    /**
     * 获取栅格中心纬度
     *
     * @param lat   左上纬度
     * @param meter 栅格大小
     * @param type  纬度当前位置点类型 0-下 1上
     * @return
     */
    public static int getMeterLat(int lat, int meter, int type) {
        int partLat = (int) (0.5 * meter * LAT_PER_METER);
        switch (type) {
            case 0:
                return lat + partLat;
            case 1:
                return lat - partLat;
            default:
                return 0;
        }
    }

    /**
     * 基于余弦定理求两经纬度距离
     *
     * @param longitude1 第一点的精度
     * @param latitude1  第一点的纬度
     * @param longitude2 第二点的精度
     * @param latitude2  第二点的纬度
     * @return 返回的距离，单位m
     */
    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        double longitudeDistance = (Math.sin((90 - latitude2) * 2 * Math.PI / 360) + Math.sin((90 - latitude1) * 2 * Math.PI / 360))
                / 2 * (longitude2 - longitude1) / 360 * 40075360;
        double latitudeDistance = (latitude2 - latitude1) / 360 * 39940670;
        return Math.sqrt(longitudeDistance * longitudeDistance + latitudeDistance * latitudeDistance);
    }

    public static double getDistance(int longitude1, int latitude1, int longitude2, int latitude2) {
        double lon1 = (double) longitude1 / IRATIO;
        double lon2 = (double) longitude2 / IRATIO;
        double lat1 = (double) latitude1 / IRATIO;
        double lat2 = (double) latitude2 / IRATIO;
        return getDistance(lon1, lat1, lon2, lat2);
    }

    /**
     * 获取跟当前位置距离 一定范围内 的栅格key集合
     *
     * @param longitude  经度
     * @param latitude   纬度
     * @param gridMeter  栅格大小-m
     * @param rangeMeter 范围-m
     * @return
     */
    public static List<String> getGridKeysInRange(int longitude, int latitude, int gridMeter, int rangeMeter) {
        List<String> gridKeys = new ArrayList<>();

        int x = GisUtil.getX(longitude, gridMeter);
        int y = GisUtil.getY(latitude, gridMeter);

        // 获取范围内栅格外扩的范围
        int n = (rangeMeter - 1) / gridMeter + 1;

        // 将范围内栅格加入key
        for (int i = x - n; i <= x + n; i++) {
            for (int j = y - n; j <= y + n; j++) {
                gridKeys.add(i + "_" + j);
            }
        }

        return gridKeys;
    }

    /**
     * 得到一个大致的圆形区域所有点
     *
     * @param longitude 圆心经度
     * @param latitude  圆心纬度
     * @param radius    半径(m)
     * @return
     */
    public static List<Point> getRotundity(int longitude, int latitude, int radius) {
        List<Point> pointList = new ArrayList<>();
        // 取圆上145个点
        int allPoint = 145;
        for (int i = 0; i < allPoint; i++) {
            Point point = new Point();
            point.setLongitude(longitude + (int) (radius * LOT_PER_METER * Math.cos(2 * i * Math.PI / allPoint)));
            point.setLatitude(latitude - (int) (radius * LAT_PER_METER * Math.sin(2 * i * Math.PI / allPoint)));
            pointList.add(point);
        }
        return pointList;
    }

    /**
     * 随机得到一个圆形区域一个点
     *
     * @param longitude 圆心经度
     * @param latitude  圆心纬度
     * @param radius    半径(m)
     * @return
     */
    public static Point getRotundityRandom(int longitude, int latitude, int radius) {
        int allPoint = 360;
        int randomI = new Random().nextInt(allPoint);
        Point point = new Point(longitude, latitude);
        int maxLongitudeOffset = (int) (radius * LOT_PER_METER * Math.cos(2 * randomI * Math.PI / allPoint));
        int maxLatitudeOffset = (int) (radius * LAT_PER_METER * Math.sin(2 * randomI * Math.PI / allPoint));
        if (maxLongitudeOffset > 0) {
            point.setLongitude(longitude + new Random().nextInt(Math.abs(maxLongitudeOffset)));
        } else if (maxLongitudeOffset < 0) {
            point.setLongitude(longitude - new Random().nextInt(Math.abs(maxLongitudeOffset)));
        }
        if (maxLatitudeOffset > 0) {
            point.setLatitude(latitude - new Random().nextInt(Math.abs(maxLatitudeOffset)));
        } else if (maxLatitudeOffset < 0) {
            point.setLatitude(latitude + new Random().nextInt(Math.abs(maxLatitudeOffset)));
        }
        return point;
    }

    public static double getLngLatDouble(int i) {
        return ((double) i) / GisUtil.IRATIO;
    }

    public static int getLngLatInt(double d) {
        return (int) (d * GisUtil.IRATIO);
    }
}
