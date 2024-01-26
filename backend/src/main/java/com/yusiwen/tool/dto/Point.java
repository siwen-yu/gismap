package com.yusiwen.tool.dto;

/**
 * @author yusiwen
 * @date 2019/12/4 10:46
 */
public class Point {

    private double lng;
    private double lat;

    private double oldLng;
    private double oldLat;

    private double wgsLng;
    private double wgsLat;
    private String name;

    public Point() {
    }

    public Point(double lng, double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    public static Point createByWgs84(double lng, double lat) {
        Point point = new Point();
        point.setWgsLng(lng);
        point.setWgsLat(lat);
        return point;
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

    public double getOldLng() {
        return oldLng;
    }

    public void setOldLng(double oldLng) {
        this.oldLng = oldLng;
    }

    public double getOldLat() {
        return oldLat;
    }

    public void setOldLat(double oldLat) {
        this.oldLat = oldLat;
    }

    public double getWgsLng() {
        return wgsLng;
    }

    public void setWgsLng(double wgsLng) {
        this.wgsLng = wgsLng;
    }

    public double getWgsLat() {
        return wgsLat;
    }

    public void setWgsLat(double wgsLat) {
        this.wgsLat = wgsLat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
