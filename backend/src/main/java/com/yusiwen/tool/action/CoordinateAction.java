package com.yusiwen.tool.action;

import cn.hutool.extra.servlet.ServletUtil;
import com.yusiwen.tool.dto.*;
import com.yusiwen.tool.enums.Type;
import com.yusiwen.tool.service.ICoordinateConvertService;
import com.yusiwen.tool.service.IFeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yusiwen
 * @date 2019/12/4 10:22
 */
@RestController
@RequestMapping("/coordinate")
@CrossOrigin
public class CoordinateAction {

    private static final Logger log = LoggerFactory.getLogger(CoordinateAction.class);

    @Autowired
    ICoordinateConvertService convertService;
    @Autowired
    IFeedbackService feedbackService;

    @PostMapping("/convert")
    public @ResponseBody
    ResponseMsg convert(@RequestParam("file") MultipartFile file, ConvertParams params) {
        List<Point> pointList = new ArrayList<>();
        String key = "";
        if (file != null && !file.isEmpty()) {
            try {
                params.setBigType(Type.POINT.toString());
                pointList = convertService.convert(file.getInputStream(), params);
            } catch (Exception e) {
                log.error("获取上传文件失败", e);
            }
        }
        return ResponseMsg.success(responseArea(params.getType(), Type.POINT.toString(), key, pointList));
    }

    @PostMapping("/convert/area")
    public @ResponseBody
    ResponseMsg convertArea(@RequestParam("file") MultipartFile file, ConvertParams params) {
        List<AreaPoint> areaPointList = new ArrayList<>();
        String key = "";
        if (file != null && !file.isEmpty()) {
            try {
                params.setBigType(Type.POLYGON.toString());
                areaPointList = convertService.convertArea(file.getInputStream(), params);
            } catch (Exception e) {
                log.error("获取上传文件失败", e);
            }
        }
        return ResponseMsg.success(responseArea(params.getType(), Type.POLYGON.toString(), key, areaPointList));
    }

    @PostMapping("/feedback")
    public @ResponseBody
    ResponseMsg feedback(HttpServletRequest request, @ModelAttribute Feedback feedback) {
        feedback.setIp(ServletUtil.getClientIP(request));
        feedbackService.feedback(feedback);
        return ResponseMsg.success(new HashMap<>());
    }

    private static Map<String, Object> responseArea(String type, String bigType, String key, Object list) {
        Map<String, Object> data = new HashMap<>();
        data.put("type", type);
        data.put("bigType", bigType);
        data.put("key", key);
        data.put("list", list);
        return data;
    }
}
