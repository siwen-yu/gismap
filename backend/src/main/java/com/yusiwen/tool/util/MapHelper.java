package com.yusiwen.tool.util;

/**
 * @author yusiwen
 * @date 2020/8/18 17:42
 */

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MapHelper implements Serializable {

    private MapHelper() {
    }

    public static Polygon createPolygon(String borderBytes) {
        return createPolygon(borderBytes, true);
    }

    public static Polygon createPolygon(String borderBytes, boolean closeLoop) {
        if (!borderBytes.startsWith("0x")) {
            return null;
        }

        return createPolygon(hexString2Bytes(borderBytes.substring(2)), closeLoop);
    }

    public static Polygon createPolygon(byte[] borderBytes, boolean closeLoop) {
        Polygon pg = new Polygon();

        int pos = 0;
        int lng = 0;
        int lat = 0;
        while (pos < borderBytes.length) {
            lng = toInt32(borderBytes, pos);
            pos += 4;

            lat = toInt32(borderBytes, pos);
            pos += 4;

            pg.addPoint(lng, lat);
        }

        if (closeLoop) {
            return closedLoop(pg);
        }
        return pg;
    }

    /**
     * 场景需要首坐标和尾坐标一致，形成闭环
     */
    private static Polygon closedLoop(Polygon pg) {
        if (pg.npoints > 0) {
            if ((pg.xpoints[0] != pg.xpoints[pg.npoints - 1]) || (pg.ypoints[0] != pg.ypoints[pg.npoints - 1])) {
                pg.addPoint(pg.xpoints[0], pg.ypoints[0]);
            }

            pg.getBounds();

            return pg;
        } else {
            return null;
        }
    }

    /**
     * 轮廓转成点，经纬度逗号分隔，点之间用分号分隔
     *
     * @param pg
     * @return
     */
    public static String polygon2String(Polygon pg) {
        StringBuilder sb = new StringBuilder();
        if (pg != null) {
            for (int i = 0; i < pg.npoints; i++) {
                sb.append(pg.xpoints[i]).append(",");
                sb.append(pg.ypoints[i]).append(";");
            }
        }
        return sb.toString();
    }

    /**
     * 通过点转成轮廓，经纬度逗号分隔，点之间用分号分隔
     *
     * @param pointStr
     * @return
     */
    public static Polygon createPolygonByPointStr(String pointStr) {
        Polygon pg = new Polygon();
        String[] points = pointStr.split(";", -1);
        for (String point : points) {
            if (StringUtils.isNotEmpty(point)) {
                String[] lnglat = point.split(",", -1);
                pg.addPoint(NumberUtils.toInt(lnglat[0], 0), NumberUtils.toInt(lnglat[1], 0));
            }
        }
        return closedLoop(pg);
    }

    public static List<Point2D> pointStr2PointList(String pointStr) {
        List<Point2D> point2DList = new ArrayList<>();
        String[] points = pointStr.split(";", -1);
        for (String point : points) {
            if (StringUtils.isNotEmpty(point)) {
                String[] lnglat = point.split(",", -1);
                point2DList.add(new Point2D.Double(NumberUtils.toDouble(lnglat[0], 0D), NumberUtils.toDouble(lnglat[1], 0D)));

            }
        }
        return point2DList;
    }

    /**
     * 2s/1亿次判断
     *
     * @param pg
     * @param x
     * @param y
     * @return
     */
    public static boolean contain(Polygon pg, int x, int y) {
        if (pg == null) {
            return false;
        }
        if (pg.contains(x, y)) {
            return true;
        }

        // 再判断一下控制点就可以了，其他点contains负责
        for (int ii = 0; ii < pg.npoints; ii++) {
            if (x == pg.xpoints[ii] && y == pg.ypoints[ii]) {
                return true;
            }
        }

        return false;
    }

    private static int toInt32(byte[] b, int pos) {
        return (b[pos + 0] & 0xFF) | ((b[pos + 1] & 0xFF) << 8) | ((b[pos + 2] & 0xFF) << 16)
                | ((b[pos + 3] & 0xFF) << 24);
    }

    private static byte[] hexString2Bytes(String src) {
        int len = src.length() / 2;
        byte[] ret = new byte[len];
        for (int i = 0; i < len; i++) {
            ret[i] = Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return ret;
    }

}
