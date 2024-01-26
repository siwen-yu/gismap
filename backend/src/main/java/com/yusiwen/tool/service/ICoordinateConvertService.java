package com.yusiwen.tool.service;

import com.yusiwen.tool.dto.AreaPoint;
import com.yusiwen.tool.dto.ConvertParams;
import com.yusiwen.tool.dto.Point;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author yusiwen
 * @date 2019/12/4 10:26
 */
public interface ICoordinateConvertService {
    List<Point> convert(InputStream inputStream, ConvertParams params) throws IOException;

    List<AreaPoint> convertArea(InputStream inputStream, ConvertParams params) throws IOException;
}
