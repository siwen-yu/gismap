package com.yusiwen.tool.util;

/**
 * @author yusiwen
 * @date 2020/8/18 17:42
 */

import java.awt.*;
import java.io.Serializable;

public class MapHelper implements Serializable {

    private MapHelper() {
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

    private static int toInt32(byte[] b, int pos) {
        return (b[pos] & 0xFF) | ((b[pos + 1] & 0xFF) << 8) | ((b[pos + 2] & 0xFF) << 16)
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
