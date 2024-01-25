package com.yusiwen.tool.service.impl.area;

import com.yusiwen.tool.dto.ConvertParamsV2;
import com.yusiwen.tool.dto.Point;
import com.yusiwen.tool.util.MapHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HexPolygon extends BasePolygonParse {

    @Override
    public List<List<com.yusiwen.tool.dto.Point>> str2PointList(String polygonStr, ConvertParamsV2 params) {
        List<com.yusiwen.tool.dto.Point> pointList = new ArrayList<>();
        Polygon polygon = MapHelper.createPolygon(polygonStr, params.isCloseLoop());
        if (polygon != null) {
            for (int i = 0; i < polygon.npoints; i++) {
                com.yusiwen.tool.dto.Point point = new Point();
                //二进制默认只有整型
                params.setFormat("int");
                transferPoint(params, point, polygon.xpoints[i] + "", polygon.ypoints[i] + "");
                pointList.add(point);
            }
        }
        return Collections.singletonList(pointList);
    }
}
