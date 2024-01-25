package com.yusiwen.tool.enums;

import com.yusiwen.tool.util.CoordinateConvert;

import java.util.Arrays;
import java.util.function.BiFunction;

/**
 * @author yusiwen
 * @date 2019/12/4 13:20
 */
public enum GcjPointSystemEnum {
    /**
     * 坐标系
     */
    WGS84(CoordinateConvert::wgs2GCJ),
    GCJ02(null),
    BD09(CoordinateConvert::bd092GCJ);

    private BiFunction<Double, Double, double[]> function;

    GcjPointSystemEnum(BiFunction<Double, Double, double[]> function) {
        this.function = function;
    }

    public BiFunction<Double, Double, double[]> getFunction() {
        return function;
    }

    public static GcjPointSystemEnum getEnum(String system) {
        return Arrays.stream(values()).filter(e -> e.toString().equalsIgnoreCase(system)).findFirst().orElse(WGS84);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
