package com.yusiwen.tool.service.impl.area;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;
import com.yusiwen.tool.dto.ConvertParamsV2;
import com.yusiwen.tool.dto.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WktPolygon extends BasePolygonParse {

    @Override
    public List<List<Point>> str2PointList(String polygonStr, ConvertParamsV2 params) {
        List<List<Point>> polygonList = new ArrayList<>();
        try {
            Geometry geometry = new WKTReader().read(polygonStr);
            if (geometry instanceof MultiPolygon) {
                for (int i = 0; i < geometry.getNumGeometries(); i++) {
                    Geometry geometryN = geometry.getGeometryN(i);
                    polygonList.add(geometry2WKTPoint(geometryN, params));
                }
            } else if (geometry instanceof Polygon) {
                polygonList.add(geometry2WKTPoint(geometry, params));
            }
        } catch (Exception e) {
            // 解析失败的wkt
        }

        return polygonList;
    }

    private List<Point> geometry2WKTPoint(Geometry geometry, ConvertParamsV2 params) {
        return Arrays.stream(geometry.getCoordinates())
                .map(coordinate -> {
                    Point point = new Point();
                    transferPoint(params, point, coordinate.x + "", coordinate.y + "");
                    return point;
                }).collect(Collectors.toList());
    }
}
