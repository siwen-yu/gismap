package com.yusiwen.tool.service.impl;

import cn.hutool.core.convert.Convert;
import com.google.common.geometry.S2LatLng;
import com.yusiwen.tool.dto.AreaPoint;
import com.yusiwen.tool.dto.ConvertParams;
import com.yusiwen.tool.dto.Point;
import com.yusiwen.tool.enums.GcjPointSystemEnum;
import com.yusiwen.tool.enums.Wgs84PointSystemEnum;
import com.yusiwen.tool.service.ICoordinateConvertService;
import com.yusiwen.tool.service.impl.area.*;
import com.yusiwen.tool.util.FileUtil;
import com.yusiwen.tool.util.RegexUtil;
import com.yusiwen.tool.util.S2Utils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * @author yusiwen
 * @date 2019/12/4 10:26
 */
@Service
public class CoordinateConvertServiceImpl implements ICoordinateConvertService {
    @Override
    public List<Point> convert(InputStream inputStream, ConvertParams params) {
        BiFunction<Double, Double, double[]> function;
        if (GcjPointSystemEnum.GCJ02.toString().equals(params.getReturnCoordinate())) {
            function = GcjPointSystemEnum.getEnum(params.getCoordinate()).getFunction();
        } else {
            function = Wgs84PointSystemEnum.getEnum(params.getCoordinate()).getFunction();
        }
        return FileUtil.lines(inputStream, line -> {
            if (!StringUtils.isEmpty(line)) {
                String[] nameLines = line.split("\\|", -1);
                String name = nameLines.length >= 2 ? nameLines[1] : "";
                return str2Point(nameLines[0], name, params, function);
            }
            return new Point();
        }).stream().filter(p -> p.getLng() > 0 && p.getLat() > 0).collect(Collectors.toList());
    }

    private Point str2Point(String pointStr, String name, ConvertParams params,
                            BiFunction<Double, Double, double[]> function) {
        Point point = new Point();
        double lng, lat;
        if (NumberUtils.isNumber(pointStr)) {
            // S2
            S2LatLng latLng = S2Utils.cellIdCenterPoint(NumberUtils.toLong(pointStr, 0L));
            lng = latLng.lngDegrees();
            lat = latLng.latDegrees();
        } else {
            String[] lines = RegexUtil.matchPointLngLats(pointStr).get(0);
            lng = getPoint(lines[0], params.getFormat());
            lat = getPoint(lines[1], params.getFormat());
        }

        if (lat > lng) {
            double tmp = lng;
            lng = lat;
            lat = tmp;
        }

        point.setOldLng(lng);
        point.setOldLat(lat);
        if (lng > 0 && lat > 0) {
            if (function != null) {
                double[] lnglat = function.apply(lat, lng);
                lat = (double) Math.round(lnglat[0] * 10000000) / 10000000;
                lng = (double) Math.round(lnglat[1] * 10000000) / 10000000;
            }
        }
        point.setLng(lng);
        point.setLat(lat);
        if (StringUtils.isEmpty(name)) {
            point.setName(pointStr);
        } else {
            point.setName(name);
        }
        return point;
    }

    private double getPoint(String pointStr, String format) {
        double point = 0;
        if ("double".equalsIgnoreCase(format)) {
            point = Convert.toDouble(pointStr, 0D);
        } else if ("int".equalsIgnoreCase(format)) {
            point = Convert.toDouble(pointStr, 0D) / 10000000;
        }
        return point;
    }



    @Override
    public List<AreaPoint> convertArea(InputStream inputStream, ConvertParams params) {
        return FileUtil.lines(inputStream, line -> {
            String[] lines = line.split("\\|", -1);
            if (lines.length >= 2) {
                return str2AreaPoint(lines[0], lines[1], params);
            } else if (lines.length == 1) {
                return str2AreaPoint("", lines[0], params);
            }
            return new AreaPoint();
        }).stream().filter(a -> !a.getPointList().isEmpty()).collect(Collectors.toList());
    }


    private AreaPoint str2AreaPoint(String name, String polygonStr, ConvertParams params) {
        AreaPoint areaPoint = new AreaPoint();
        areaPoint.setName(name);
        BasePolygonParse basePolygonParse;
        if (polygonStr.startsWith("0x")) {
            basePolygonParse = new HexPolygon();
        } else if (NumberUtils.isNumber(polygonStr)) {
            basePolygonParse = new S2Polygon();
        } else if (isWkt(polygonStr)) {
            basePolygonParse = new WktPolygon();
        } else if (polygonStr.startsWith("[[[") && polygonStr.endsWith("]]]")) {
            basePolygonParse = new StrSpecPolygon();
        } else if (isGeoHash(polygonStr)) {
            basePolygonParse = new GeoHashPolygon();
        } else {
            basePolygonParse = new StrPolygon();
        }
        areaPoint.setPointList(basePolygonParse.str2PointList(polygonStr, params));
        return areaPoint;
    }

    private static final String BASE32_CHARS = "0123456789bcdefghjkmnpqrstuvwxyz";
    private boolean isGeoHash(String geohash) {
        // GeoHash 通常为 1 到 12 个字符长
        int length = geohash.length();
        if (length < 1 || length > 12) {
            return false;
        }

        // 检查字符串中的每个字符是否在 BASE32_CHARS 中
        for (char c : geohash.toCharArray()) {
            if (BASE32_CHARS.indexOf(c) == -1) {
                return false;
            }
        }
        return true;
    }

    private boolean isWkt(String polygonStr) {
        return StringUtils.startsWithIgnoreCase(polygonStr, "POINT") ||
                StringUtils.startsWithIgnoreCase(polygonStr, "LINESTRING") ||
                StringUtils.startsWithIgnoreCase(polygonStr, "LINEARRING") ||
                StringUtils.startsWithIgnoreCase(polygonStr, "POLYGON") ||
                StringUtils.startsWithIgnoreCase(polygonStr, "MULTIPOINT") ||
                StringUtils.startsWithIgnoreCase(polygonStr, "MULTILINESTRING") ||
                StringUtils.startsWithIgnoreCase(polygonStr, "MULTIPOLYGON") ||
                StringUtils.startsWithIgnoreCase(polygonStr, "GEOMETRYCOLLECTION");
    }
}
