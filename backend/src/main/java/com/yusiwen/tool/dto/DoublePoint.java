package com.yusiwen.tool.dto;

/**
 * @author yusiwen
 * @date 2019/12/4 10:46
 */
public class DoublePoint {

    private double lng;
    private double lat;
    private int size;

    public DoublePoint(double lng, double lat, int size) {
        this.lng = lng;
        this.lat = lat;
        this.size = size;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
