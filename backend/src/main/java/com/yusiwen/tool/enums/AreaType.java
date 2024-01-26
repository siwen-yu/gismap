package com.yusiwen.tool.enums;

import com.google.common.geometry.S2Cell;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2LatLng;
import com.yusiwen.tool.dto.AreaPoint;
import com.yusiwen.tool.dto.DoublePoint;
import com.yusiwen.tool.dto.Point;
import com.yusiwen.tool.util.GisUtil;
import com.yusiwen.tool.util.S2Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum AreaType {
    CIRCLE(doublePoint -> {
        List<Point> pointList = GisUtil.getRotundity(GisUtil.getLngLatInt(doublePoint.getLng()),
                        GisUtil.getLngLatInt(doublePoint.getLat()), doublePoint.getSize())
                .stream()
                .map(point -> Point.createByWgs84(GisUtil.getLngLatDouble(point.getLeft()), GisUtil.getLngLatDouble(point.getRight())))
                .collect(Collectors.toList());
        return new AreaPoint("", pointList);
    }),
    GRID(doublePoint -> {
        int ltLot = GisUtil.getLTLot(GisUtil.getLngLatInt(doublePoint.getLng()), doublePoint.getSize());
        int ltLat = GisUtil.getLTLat(GisUtil.getLngLatInt(doublePoint.getLat()), doublePoint.getSize());
        int rbLot = GisUtil.getRBLot(GisUtil.getLngLatInt(doublePoint.getLng()), doublePoint.getSize());
        int rbLat = GisUtil.getRBLat(GisUtil.getLngLatInt(doublePoint.getLat()), doublePoint.getSize());
        List<Point> pointList = new ArrayList<>();
        pointList.add(Point.createByWgs84(GisUtil.getLngLatDouble(ltLot), GisUtil.getLngLatDouble(ltLat)));
        pointList.add(Point.createByWgs84(GisUtil.getLngLatDouble(ltLot), GisUtil.getLngLatDouble(rbLat)));
        pointList.add(Point.createByWgs84(GisUtil.getLngLatDouble(rbLot), GisUtil.getLngLatDouble(rbLat)));
        pointList.add(Point.createByWgs84(GisUtil.getLngLatDouble(rbLot), GisUtil.getLngLatDouble(ltLat)));
        return new AreaPoint("", pointList);
    }),
    S2(doublePoint -> {
        List<Point> pointList = new ArrayList<>();
        S2CellId s2CellId = S2Utils.lonLatToS2CellId(doublePoint.getLng(), doublePoint.getLat(), doublePoint.getSize());
        S2Cell s2Cell = new S2Cell(s2CellId);
        for (int i = 0; i < 4; i++) {
            S2LatLng latLng = new S2LatLng(s2Cell.getVertex(i));
            pointList.add(Point.createByWgs84(latLng.lngDegrees(), latLng.latDegrees()));
        }
        return new AreaPoint("", pointList);
    });

    final Function<DoublePoint, AreaPoint> genAreaFunction;

    AreaType(Function<DoublePoint, AreaPoint> genAreaFunction) {
        this.genAreaFunction = genAreaFunction;
    }

    public Function<DoublePoint, AreaPoint> getGenAreaFunction() {
        return genAreaFunction;
    }

    public static AreaType match(String type) {
        for (AreaType value : values()) {
            if (value.toString().equalsIgnoreCase(type)) {
                return value;
            }
        }
        return null;
    }
}
