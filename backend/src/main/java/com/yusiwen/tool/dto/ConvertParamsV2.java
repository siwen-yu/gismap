package com.yusiwen.tool.dto;

import com.yusiwen.tool.enums.GcjPointSystemEnum;
import com.yusiwen.tool.enums.Type;

/**
 * @author yusiwen
 * @date 2019/12/4 10:44
 */
public class ConvertParamsV2 {
    private String coordinate = GcjPointSystemEnum.WGS84.toString();
    private String format = "double";
    private String returnCoordinate = GcjPointSystemEnum.GCJ02.toString();
    private boolean closeLoop = true;

    private String type = Type.POINT.toString();

    private String bigType = Type.POINT.toString();

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getReturnCoordinate() {
        return returnCoordinate;
    }

    public void setReturnCoordinate(String returnCoordinate) {
        this.returnCoordinate = returnCoordinate;
    }

    public boolean isCloseLoop() {
        return closeLoop;
    }

    public void setCloseLoop(boolean closeLoop) {
        this.closeLoop = closeLoop;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBigType() {
        return bigType;
    }

    public void setBigType(String bigType) {
        this.bigType = bigType;
    }
}
