package com.yusiwen.tool.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author yusiwen
 * @date 2019/12/4 10:46
 */
public class AreaPoint {

    private String name;
    private List<List<Point>> pointList = new ArrayList<>();

    public AreaPoint() {
    }

    public AreaPoint(String name, List<Point> pointList) {
        this.name = name;
        this.pointList = Collections.singletonList(pointList);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<List<Point>> getPointList() {
        return pointList;
    }

    public void setPointList(List<List<Point>> pointList) {
        this.pointList = pointList;
    }
}
