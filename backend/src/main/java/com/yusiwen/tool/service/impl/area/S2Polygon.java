package com.yusiwen.tool.service.impl.area;

import com.google.common.geometry.S2Cell;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2LatLng;
import com.yusiwen.tool.dto.ConvertParamsV2;
import com.yusiwen.tool.dto.Point;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class S2Polygon extends BasePolygonParse {

    @Override
    public List<List<Point>> str2PointList(String polygonStr, ConvertParamsV2 params) {
        List<Point> pointList = new ArrayList<>();
        S2Cell s2Cell = new S2Cell(new S2CellId(NumberUtils.toLong(polygonStr, 0L)));
        for (int i = 0; i < 4; i++) {
            Point point = new Point();
            //s2默认只有浮点型
            params.setFormat("double");
            S2LatLng latLng = new S2LatLng(s2Cell.getVertex(i));
            transferPoint(params, point, latLng.lngDegrees() + "", latLng.latDegrees() + "");
            pointList.add(point);
        }
        return Collections.singletonList(pointList);
    }
}
