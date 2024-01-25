package com.yusiwen.tool.service.impl.area;

import com.yusiwen.tool.dto.ConvertParamsV2;
import com.yusiwen.tool.dto.Point;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StrSpecPolygon extends BasePolygonParse {

    /**
     * 格式样例 [[[1,1],[2,2],[3,3]],[[1,1],[2,2],[3,3]]]
     *
     * @param polygonStr
     * @param params
     * @return
     */
    @Override
    public List<List<Point>> str2PointList(String polygonStr, ConvertParamsV2 params) {
        String[] polygonStrs = polygonStr.trim().split("]],\\[\\[", -1);
        return Arrays.stream(polygonStrs).map(ps -> Arrays.stream(ps.split("],\\[", -1)).map(pointStr -> {
            String[] split = pointStr.replace("[","").replace("]", "").split(",", -1);
            Point point = new Point();
            transferPoint(params, point, split[0], split[1]);
            return point;
        }).filter(p -> p.getLng() > 0 && p.getLat() > 0).collect(Collectors.toList())).collect(Collectors.toList());
    }
}
