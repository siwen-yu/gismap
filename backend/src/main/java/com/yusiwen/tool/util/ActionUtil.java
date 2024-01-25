package com.yusiwen.tool.util;

import java.util.HashMap;
import java.util.Map;

public class ActionUtil {

    public static Map<String, Object> responseArea(String type, String bigType, String key, Object list) {
        Map<String, Object> data = new HashMap<>();
        data.put("type", type);
        data.put("bigType", bigType);
        data.put("key", key);
        data.put("list", list);
        return data;
    }
}
