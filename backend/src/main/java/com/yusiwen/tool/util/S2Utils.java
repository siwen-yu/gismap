package com.yusiwen.tool.util;

import com.google.common.geometry.S2Cell;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2LatLng;

public class S2Utils {

    private S2Utils() {
        throw new IllegalStateException("工具类不能被初始化");
    }

    /**
     * 经纬度转s2Id
     *
     * @param longitude : 经度
     * @param latitude  : 纬度
     * @param s2Level   : s2等级
     * @return S2CellId 对象
     */
    public static S2CellId lonLatToS2CellId(double longitude, double latitude, int s2Level) {
        final S2LatLng s2LatLng = S2LatLng.fromDegrees(latitude, longitude);
        return S2CellId.fromLatLng(s2LatLng)
                .parent(s2Level);
    }

    public static S2LatLng cellIdCenterPoint(long cellId) {
        S2Cell s2Cell = new S2Cell(new S2CellId(cellId));
        return new S2LatLng(s2Cell.getCenter());
    }
}
