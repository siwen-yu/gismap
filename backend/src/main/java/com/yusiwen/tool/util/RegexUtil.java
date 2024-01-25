package com.yusiwen.tool.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

    private RegexUtil() {
    }

    public static final Pattern NUMBER_REG = Pattern.compile("^(\\d+)");
    public static final Pattern CLASS_REGEX = Pattern.compile("\\n*\\s*public\\s+class\\s+(\\w+)\\b");
    public static final Pattern MAIN_REGEX = Pattern.compile("\\n*\\s*public\\s+static\\s+void\\s+main\\s*\\(\\s*String\\[\\]\\s+args\\s*\\)");

    public static final Pattern POINT_SPLIT = Pattern.compile("\\d+(\\.\\d+)?");

    public static String matchFirstNum(String input) {
        if (StringUtils.isEmpty(input)) {
            throw new NumberFormatException();
        }
        Matcher matcher = NUMBER_REG.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new NumberFormatException();
        }
    }

    public static String matchClassName(String input) {
        if (StringUtils.isEmpty(input)) {
            return "";
        }
        Matcher matcher = CLASS_REGEX.matcher(input);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }

    public static boolean matchMainMethod(String input) {
        if (StringUtils.isEmpty(input)) {
            return false;
        }
        Matcher matcher = MAIN_REGEX.matcher(input);
        return matcher.find();
    }

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
