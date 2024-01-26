package com.yusiwen.tool.service.impl.area;

import com.yusiwen.tool.dto.ConvertParams;
import com.yusiwen.tool.dto.Point;
import com.yusiwen.tool.util.RegexUtil;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StrPolygon extends BasePolygonParse {

    @Override
    public List<List<Point>> str2PointList(String polygonStr, ConvertParams params) {
        List<String[]> pointLngLats = RegexUtil.matchPointLngLats(polygonStr);
        List<Point> pointList = pointLngLats.stream().map(lnglat -> {
            Point point = new Point();
            transferPoint(params, point, getStr(lnglat, 0), getStr(lnglat, 1));
            return point;
        }).filter(p -> p.getLng() > 0 && p.getLat() > 0).collect(Collectors.toList());
        return Collections.singletonList(pointList);
    }

    private String getStr(String[] lines, int index) {
        return lines.length > index ? lines[index] : "";
    }
}
