package com.yusiwen.tool.util;

import com.google.common.geometry.*;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
     * @return S2 ID
     */
    public static long lonLatToS2Id(double longitude, double latitude, int s2Level) {
        return lonLatToS2CellId(longitude, latitude, s2Level).id();
    }

    /**
     * S2 ID 转经纬度（无需传入 S2 等级，会自动推断）
     *
     * @param s2Id S2 ID
     * @return 经纬度
     */
    public static LngLat s2IdToLonLat(long s2Id) {
        return s2CellIdToLonLat(new S2CellId(s2Id));
    }

    /**
     * 经纬度转s2Token
     *
     * @param longitude : 经度
     * @param latitude  : 纬度
     * @param s2Level   : S2 等级
     * @return S2 Token
     */
    public static String lonLatToS2Token(double longitude, double latitude, int s2Level) {
        return lonLatToS2CellId(longitude, latitude, s2Level).toToken();
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

    /**
     * 获取s2的Cell的中心经纬度
     *
     * @param id S2CellId 对象
     * @return 中心经纬度
     */
    public static LngLat s2CellIdToLonLat(S2CellId id) {
        S2Cell cell = new S2Cell(id);
        S2Point center = cell.getCenter();
        double lng = S2LatLng.longitude(center).degrees();
        double lat = S2LatLng.latitude(center).degrees();
        return new LngLat(lng, lat);
    }

    public static S2LatLng cellIdCenterPoint(long cellId) {
        S2Cell s2Cell = new S2Cell(new S2CellId(cellId));
        return new S2LatLng(s2Cell.getCenter());
    }

    /**
     * s2Id转换等级
     *
     * @param s2Id  : s2 id
     * @param level : s2 等级
     * @return 更换等级后的 s2 id
     */
    public static long s2ChangeLevel(long s2Id, int level) {
        return new S2CellId(s2Id).parent(level).id();
    }

    /**
     * 将单个cellId转换为多体格指定Level的cellId
     *
     * @param s2CellId   : S2CellId 对象
     * @param desS2Level : 要转的 s2 id 等级
     * @return S2CellId 列表
     */
    public static List<S2CellId> getChildrenCellId(S2CellId s2CellId, int desS2Level) {

        List<S2CellId> s2CellIds = new ArrayList<>();
        S2CellId begin = s2CellId.childBegin(desS2Level);
        S2CellId end = s2CellId.childEnd(desS2Level);

        while (!begin.equals(end)) {
            s2CellIds.add(begin);
            begin = begin.next();
        }
        return s2CellIds;
    }

    /**
     * 将单个cellId转换为多体格指定Level的cellId(非常愚蠢的造轮子)
     *
     * @param s2CellId   : S2CellId 对象
     * @param curLevel   : 当前的 s2 id 等级
     * @param desS2Level : 要转的 s2 id 等级
     * @return S2CellId 列表
     */
    public static List<S2CellId> getChildrenCellIdStupid(
            S2CellId s2CellId,
            int curLevel,
            int desS2Level) {

        if (curLevel < desS2Level) {
            long interVal = (s2CellId.childEnd().id() - s2CellId.childBegin().id()) / 4;
            List<S2CellId> s2CellIds = new ArrayList<>();
            for (int i = 0; i <= 3; i++) {
                long newId = s2CellId.childBegin().id() + interVal * i;
                s2CellIds.addAll(
                        getChildrenCellIdStupid(new S2CellId(newId), curLevel + 1, desS2Level));
            }
            return s2CellIds;
        } else {
            return Collections.singletonList(s2CellId);
        }
    }

    /**
     * S2Id转Token
     *
     * @param s2Id : S2Id
     * @return S2Token
     */
    public static String s2IdToS2Token(Long s2Id) {
        String hexWithZero = Long.toHexString(s2Id);
        int zeroNum = 0;
        boolean zeroFlag = true;
        for (char c : StringUtils.reverse(hexWithZero).toCharArray()) {
            if (c != '0') {
                zeroFlag = false;
            }
            if (zeroFlag) {
                zeroNum += 1;
            }
        }
        return hexWithZero.substring(0, hexWithZero.length() - zeroNum);
    }

    /**
     * 将左上经纬度和右下经纬度转换为S2Rect
     *
     * @param longitude1 : 左上经度
     * @param latitude1  : 左上纬度
     * @param longitude2 : 右下经度
     * @param latitude2  : 右下纬度
     * @return S2Rect
     */
    public static S2LatLngRect lonLatBoxToS2Rect(
            double longitude1,
            double latitude1,
            double longitude2,
            double latitude2) {

        S2LatLng latlng1 = S2LatLng.fromDegrees(latitude1, longitude1);
        S2LatLng latLng2 = S2LatLng.fromDegrees(latitude2, longitude2);
        return new S2LatLngRect(latlng1, latLng2);
    }

    /**
     * function : 将左上经纬度和右下经纬度转换为S2Rect
     *
     * @param longitude1   : 左上经度
     * @param latitude1    : 左上纬度
     * @param longitude2   : 右下经度
     * @param latitude2    : 右下纬度
     * @param cellMaxLevel : 最大栅格等级
     * @param cellMinLevel : 最小栅格等级
     * @return S2CellId 列表
     */
    public static List<S2CellId> lonLatBoxToS2CellList(
            double longitude1,
            double latitude1,
            double longitude2,
            double latitude2,
            int cellMaxLevel,
            int cellMinLevel) {

        S2LatLngRect rect = lonLatBoxToS2Rect(longitude1, latitude1, longitude2, latitude2);
        S2RegionCoverer coverer = new S2RegionCoverer();
        coverer.setMaxLevel(cellMaxLevel);
        coverer.setMinLevel(cellMinLevel);
        final ArrayList<S2CellId> ids = new ArrayList<>();
        coverer.getCovering(rect, ids);
        return ids;
    }

    /**
     * 将经纬度序列转换为S2Polygon
     *
     * @param latLngList : 经纬度序列
     * @return S2Polygon S2 多边形
     */
    public static S2Polygon lngLatListToS2Polygon(List<LngLat> latLngList) {
        List<S2Point> pointList = latLngList.stream()
                .map(x -> S2LatLng.fromDegrees(x.latitude(), x.longitude()).toPoint())
                .collect(Collectors.toList());

        S2PolygonBuilder builder = new S2PolygonBuilder();
        int pointListLength = pointList.size();

        for (int index = 0; index < pointList.size(); index++) {
            if (index == pointListLength - 1) {
                builder.addEdge(pointList.get(index), pointList.get(0));
            } else {
                builder.addEdge(pointList.get(index), pointList.get(index + 1));
            }
        }
        return builder.assemblePolygon();
    }

    /**
     * 将S2Polygon转换为{@code List<S2CellId>}
     *
     * @param polygon        : S2Polygon
     * @param cellIdMinLevel : 最小Cell等级
     * @param cellIdMaxLevel : 最大Cell等级
     * @param maxCellNumber  : 最大Cell个数
     * @return S2CellId 列表
     */
    public static List<S2CellId> s2PolygonToS2CellIdList(
            S2Polygon polygon, int cellIdMinLevel,
            int cellIdMaxLevel, int maxCellNumber) {

        S2RegionCoverer coverer = new S2RegionCoverer();
        coverer.setMinLevel(cellIdMinLevel);
        coverer.setMaxLevel(cellIdMaxLevel);
        coverer.setMaxCells(maxCellNumber);
        ArrayList<S2CellId> ids = new ArrayList<>();
        coverer.getCovering(polygon, ids);
        return ids;
    }

    /**
     * 将经纬度序列转换为{@code List<S2CellId>}
     *
     * @param lngLats        : 经纬度序列
     * @param cellIdMinLevel : 最小Cell等级
     * @param cellIdMaxLevel : 最大Cell等级
     * @param maxCellNumber  : 最大Cell个数
     * @return List[S2CellId]
     */
    public static List<S2CellId> lngLatListToS2CellIdList(
            List<LngLat> lngLats, int cellIdMinLevel,
            int cellIdMaxLevel, int maxCellNumber) {
        return s2PolygonToS2CellIdList(lngLatListToS2Polygon(lngLats), cellIdMinLevel, cellIdMaxLevel, maxCellNumber);
    }

    /**
     * 判断 ID 1 是否包含 ID 2
     *
     * @param id1 S2 ID 1
     * @param id2 S2 ID 2
     * @return 包含返回 true，否则返回 false
     */
    public static boolean contains(long id1, long id2) {
        return new S2CellId(id1).contains(new S2CellId(id2));
    }

    /**
     * @see #vertexs(S2CellId)
     */
    public static List<LngLat> vertexs(long s2Id) {
        return vertexs(new S2CellId(s2Id));
    }

    /**
     * 获取S2栅格四个顶点的经纬度
     *
     * @param s2Id S2 Id
     * @return 一个列表，内含四个坐标：
     * <ol>
     * <li>SW西南（左下）方向的坐标</li>
     * <li>SE东南（右下）方向的坐标</li>
     * <li>NE东北（右上）方向的坐标</li>
     * <li>NW西北（左上）方向的坐标</li>
     * </ol>
     */
    public static List<LngLat> vertexs(S2CellId s2Id) {
        final S2Cell cell = new S2Cell(s2Id);

        List<LngLat> res = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            final S2Point vertex = cell.getVertex(i);
            final double lng = S2LatLng.longitude(vertex).degrees();
            final double lat = S2LatLng.latitude(vertex).degrees();

            res.add(new LngLat(lng, lat));
        }
        return res;
    }

    public static class LngLat implements Serializable {
        private static final long serialVersionUID = 1L;

        private final double lng;
        private final double lat;

        public LngLat(double lng, double lat) {
            this.lng = lng;
            this.lat = lat;
        }

        public double longitude() {
            return lng;
        }

        public double latitude() {
            return lat;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            LngLat lngLat = (LngLat) o;
            return Double.compare(lngLat.lng, lng) == 0 &&
                    Double.compare(lngLat.lat, lat) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(lng, lat);
        }

        @Override
        public String toString() {
            return "(" + lng + ", " + lat + ')';
        }
    }
}
