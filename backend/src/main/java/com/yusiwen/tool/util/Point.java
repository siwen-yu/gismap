package com.yusiwen.tool.util;


import java.io.Serializable;

/**
 * GIS中的一个点
 *
 * @author yusiwen
 */
public class Point implements Serializable {
    private Integer longitude;
    private Integer latitude;

    public Point() {
    }

    public Point(Integer longitude, Integer latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Integer getLongitude() {
        return longitude;
    }

    public void setLongitude(Integer longitude) {
        this.longitude = longitude;
    }

    public Integer getLatitude() {
        return latitude;
    }

    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }
}

