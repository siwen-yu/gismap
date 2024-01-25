package com.yusiwen.tool.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 封装了些文件相关的操作
 */
public final class FileUtil {

    private FileUtil() {

    }

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 以列表的方式获取文件的所有行
     *
     * @param file 需要出来的文件
     * @return 包含所有行的list
     */
    public static <T> List<T> lines(File file, Function<String, T> function) {
        List<T> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(function.apply(line));
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return list;
    }

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

    /**
     * 以列表的方式获取文件的所有行
     *
     * @param file     需要处理的文件
     * @param encoding 指定读取文件的编码
     * @return 包含所有行的list
     */
    public static <T> List<T> lines(File file, String encoding, Function<String, T> function) {
        List<T> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(function.apply(line));
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return list;
    }

    /**
     * 以列表的方式获取文件的指定的行数数据
     *
     * @param file  处理的文件
     * @param lines 需要读取的行数
     * @return 包含制定行的list
     */
    public static <T> List<T> lines(File file, int lines, Function<String, T> function) {
        List<T> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(function.apply(line));
                if (list.size() == lines) {
                    break;
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return list;
    }

    /**
     * 以列表的方式获取文件的指定的行数数据
     *
     * @param file     需要处理的函数
     * @param lines    需要处理的行还俗
     * @param encoding 指定读取文件的编码
     * @return 包含制定行的list
     */
    public static <T> List<T> lines(File file, int lines, String encoding, Function<String, T> function) {
        List<T> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(function.apply(line));
                if (list.size() == lines) {
                    break;
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return list;
    }

}