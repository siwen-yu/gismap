package com.yusiwen.tool.service.impl.area;

import ch.hsr.geohash.BoundingBox;
import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import com.yusiwen.tool.dto.ConvertParams;
import com.yusiwen.tool.dto.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeoHashPolygon extends BasePolygonParse {

    @Override
    public List<List<Point>> str2PointList(String polygonStr, ConvertParams params) {
        List<Point> pointList = new ArrayList<>();
        GeoHash geoHash = GeoHash.fromGeohashString(polygonStr);
        // 获取 GeoHash 表示的范围（边界框）
        BoundingBox bbox = geoHash.getBoundingBox();
        //geoHash默认只有浮点型
        params.setFormat("double");

        addPoint(pointList, params, bbox.getNorthEastCorner());
        addPoint(pointList, params, bbox.getNorthWestCorner());
        addPoint(pointList, params, bbox.getSouthWestCorner());
        addPoint(pointList, params, bbox.getSouthEastCorner());

        return Collections.singletonList(pointList);
    }

    private void addPoint(List<Point> pointList, ConvertParams params, WGS84Point wgs84Point) {
        Point point = new Point();
        transferPoint(params, point, wgs84Point.getLongitude() + "", wgs84Point.getLatitude() + "");
        pointList.add(point);
    }
}
