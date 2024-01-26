package com.yusiwen.tool.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

    private RegexUtil() {
    }

    public static final Pattern POINT_SPLIT = Pattern.compile("\\d+(\\.\\d+)?");

    public static List<String[]> matchPointLngLats(String pointStr) {
        List<String[]> list = new ArrayList<>();
        String[] lngLat = new String[]{"", ""};
        Matcher matcher = POINT_SPLIT.matcher(pointStr);
        int i = 0;
        while (matcher.find()) {
            lngLat[i++ % 2] = matcher.group();
            if (i % 2 == 0) {
                list.add(new String[]{lngLat[0], lngLat[1]});
            }
        }
        if (list.isEmpty()) {
            list.add(lngLat);
        }
        return list;
    }
}
