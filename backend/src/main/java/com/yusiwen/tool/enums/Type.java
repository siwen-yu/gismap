package com.yusiwen.tool.enums;

/**
 * 打点类型
 */
public enum Type {
    /**
     * 点
     */
    POINT,
    /**
     * 轮廓
     */
    POLYGON;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
