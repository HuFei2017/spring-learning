package com.learning.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CollectionUtil
 * @Description TODO
 * @Author hufei
 * @Date 2023/5/19 17:13
 * @Version 1.0
 */
public class CollectionUtil {

    /**
     * @Description 拼接集合元素
     * @Param [list, separator] list:集合; separator:连接符
     * @Return java.lang.String
     * @Author hufei
     * @Date 2020/10/19 14:01
     */
    public static String join(Iterable<?> list, char separator) {
        return StringUtils.join(list, separator);
    }

    /**
     * @Description 拼接集合元素
     * @Param [list, separator] list:集合; separator:连接符
     * @Return java.lang.String
     * @Author hufei
     * @Date 2020/10/19 14:01
     */
    public static String join(Iterable<?> list, String separator) {
        return StringUtils.join(list, separator);
    }

    /**
     * @Description 拼接集合元素
     * @Param [array, separator] array:集合; separator:连接符
     * @Return java.lang.String
     * @Author hufei
     * @Date 2020/10/19 14:01
     */
    public static String join(Object[] array, char separator) {
        return StringUtils.join(array, separator);
    }

    /**
     * @Description 拼接集合元素
     * @Param [array, separator] array:集合; separator:连接符
     * @Return java.lang.String
     * @Author hufei
     * @Date 2020/10/19 14:01
     */
    public static String join(Object[] array, String separator) {
        return StringUtils.join(array, separator);
    }

    /**
     * 将一组数据平均分成n组
     *
     * @param list 要分组的数据源
     * @param n    平均分成n组
     */
    public static <T> List<List<T>> averageAssign(List<T> list, int n) {
        List<List<T>> result = new ArrayList<>();
        int remainder = list.size() % n;  //(先计算出余数)
        int number = list.size() / n;  //然后是商
        int offset = 0;//偏移量
        for (int i = 0; i < n; i++) {
            List<T> value;
            if (remainder > 0) {
                value = list.subList(i * number + offset, (i + 1) * number + offset + 1);
                remainder--;
                offset++;
            } else {
                value = list.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }

    /**
     * 将一组数据固定分组，每组n个元素
     *
     * @param source 要分组的数据源
     * @param n      每组n个元素
     */
    public static <T> List<List<T>> fixedGrouping(List<T> source, int n) {
        if (null == source || source.size() == 0 || n <= 0) {
            return null;
        }
        List<List<T>> result = new ArrayList<>();
        int remainder = source.size() % n;
        int size = (source.size() / n);
        for (int i = 0; i < size; i++) {
            List<T> subset;
            subset = source.subList(i * n, (i + 1) * n);
            result.add(subset);
        }
        if (remainder > 0) {
            List<T> subset;
            subset = source.subList(size * n, size * n + remainder);
            result.add(subset);
        }
        return result;
    }

    /**
     * 列表截取
     *
     * @param source 原始数据
     * @param page   页数
     * @param size   每页数量
     * @param <T>    数据类型
     * @return 返回的列表
     */
    public static <T> List<T> subList(List<T> source, int page, int size) {

        boolean invalidTag = page <= 0 || size <= 0;

        //分页参数无效,无法分页
        if (invalidTag) {
            return new ArrayList<>();
        }

        int length = source.size();

        //原始数据为空,不需要分页
        if (length == 0) {
            return new ArrayList<>();
        }

        int min = size * (page - 1);
        int max = size * page;

        //最小起始位置超过了最大位置,不需要分页
        if (min >= length) {
            return new ArrayList<>();
        }

        return source.subList(min, Math.min(max, length));
    }
}
