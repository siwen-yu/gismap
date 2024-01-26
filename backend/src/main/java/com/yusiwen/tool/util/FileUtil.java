package com.yusiwen.tool.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 封装了些文件相关的操作
 */
public final class FileUtil {

    private FileUtil() {

    }

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 以列表的方式获取文件的所有行
     *
     * @param inputStream 需要出来的文件
     * @return 包含所有行的list
     */
    public static <T> List<T> lines(InputStream inputStream, Function<String, T> function) {
        List<T> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(function.apply(line));
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return list;
    }
}