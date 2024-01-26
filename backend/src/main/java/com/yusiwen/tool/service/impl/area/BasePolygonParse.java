package com.yusiwen.tool.service.impl.area;

import cn.hutool.core.convert.Convert;
import com.yusiwen.tool.dto.ConvertParams;
import com.yusiwen.tool.dto.Point;
import com.yusiwen.tool.enums.GcjPointSystemEnum;

import java.io.Serializable;
import java.util.List;
import java.util.function.BiFunction;

public abstract class BasePolygonParse implements Serializable {

    public abstract List<List<Point>> str2PointList(String polygonStr, ConvertParams params);

    void transferPoint(ConvertParams params, Point point, String lngStr, String latStr) {
        double oldLng = getPoint(lngStr, params.getFormat());
        double oldLat = getPoint(latStr, params.getFormat());
        point.setOldLng(oldLng);
        point.setOldLat(oldLat);
        BiFunction<Double, Double, double[]> function = GcjPointSystemEnum.getEnum(params.getCoordinate()).getFunction();
        double[] doubles = transferCoordinate(function, oldLng, oldLat);
        point.setLng(doubles[1]);
        point.setLat(doubles[0]);
        function = GcjPointSystemEnum.getEnum(params.getCoordinate()).getFunction();
        doubles = transferCoordinate(function, oldLng, oldLat);
        point.setWgsLng(doubles[1]);
        point.setWgsLat(doubles[0]);
    }


    private double[] transferCoordinate(BiFunction<Double, Double, double[]> function, double lng, double lat) {
        if (lng > 0 && lat > 0 && function != null) {
            double[] lnglat = function.apply(lat, lng);
            lat = (double) Math.round(lnglat[0] * 10000000) / 10000000;
            lng = (double) Math.round(lnglat[1] * 10000000) / 10000000;
        }
        return new double[]{lat, lng};
    }

    private double getPoint(String pointStr, String format) {
        double point;
        if ("int".equalsIgnoreCase(format)) {
            point = Convert.toDouble(pointStr, 0D) / 10000000;
        } else {
            point = Convert.toDouble(pointStr, 0D);
        }
        return point;
    }
}
