package com.learning.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @ClassName TextUtil
 * @Description 文本工具类, 封装所有的文本转换及对象反序列化
 * @Author hufei
 * @Date 2020/7/17 10:09
 * @Version 1.0
 */
public class TextUtil {

    private static final Logger log = LoggerFactory.getLogger(TextUtil.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * @Description 对象序列化
     * @Param [obj] 对象实例
     * @Return java.lang.String 序列化文本
     * @Author hufei
     * @Date 2020/8/19 11:16
     */
    public static String fromJson(Object obj) {

        if (null == obj) {
            return "";
        }

        if (obj.getClass().getName().equals("java.lang.String")) {
            return String.valueOf(obj);
        }

        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            log.error("the object " + obj + " can not be deserialize successfully.", ex);
        }
        return "";
    }

    /**
     * @Description 字符串转Json对象
     * @Param [str] 文本内容
     * @Return com.fasterxml.jackson.databind.JsonNode Json对象
     * @Author hufei
     * @Date 2020/9/25 15:49
     */
    public static JsonNode parseJsonNode(String str) {
        try {
            return mapper.readTree(str);
        } catch (JsonProcessingException ex) {
            log.error("the string " + str + " can not be formatted successfully.", ex);
        }
        return mapper.nullNode();
    }

    /**
     * @Description 文本转换实体类
     * @Param [str, tClass] str:文本内容; tClass:实体类
     * @Return T
     * @Author hufei
     * @Date 2020/8/19 11:16
     */
    public static <T> T parseJson(String str, Class<T> tClass) {
        if (null == str || str.isEmpty()) {
            return null;
        }
        try {
            return mapper.readValue(str, tClass);
        } catch (JsonProcessingException ex) {
            log.error("the string " + str + " can not be deserialize successfully.", ex);
        }
        return null;
    }

    /**
     * @Description 文本转换实体类
     * @Param [str, tClass] str:文本内容; tClass:实体类
     * @Return T
     * @Author hufei
     * @Date 2020/8/19 11:16
     */
    public static <T> List<T> parseJsonToList(String str, Class<T[]> tClass) {
        if (null == str || str.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            T[] ret = mapper.readValue(str, tClass);
            return new ArrayList<>(Arrays.asList(ret));
        } catch (JsonProcessingException ex) {
            log.error("the string " + str + " can not be deserialize successfully.", ex);
        }
        return new ArrayList<>();
    }

    /**
     * @Description 文本转换实体类
     * @Param [str]
     * @Return java.util.Map<java.lang.String, java.lang.Object>
     * @Author hufei
     * @Date 2022/6/21 9:10
     */
    public static Map<String, Object> parseJsonToMap(String str) {
        Map ret = parseJson(str, Map.class);
        if (null == ret) {
            return null;
        }
        Map<String, Object> result = new HashMap<>();
        for (Object key : ret.keySet()) {
            result.put(key.toString(), ret.get(key));
        }
        return result;
    }

    /**
     * @Description Json对象获取指定字段值
     * @Param [node, pos] node:Json对象; pos:字段位置
     * @Return java.lang.String 字段值
     * @Author hufei
     * @Date 2020/11/25 13:47
     */
    public static Object extractFieldValue(JsonNode node, String pos) {
        return extractFieldValue(node, pos, null);
    }

    /**
     * @Description Json对象获取指定字段值
     * @Param [node, pos] node:Json对象; pos:字段位置
     * @Return java.lang.String 字段值
     * @Author hufei
     * @Date 2020/11/25 13:47
     */
    public static Object extractFieldValue(JsonNode node, String pos, Object initVal) {

        if (null == node || node.isEmpty()) {
            return initVal;
        }

        if (pos.startsWith(".") || pos.endsWith(".")) {
            return initVal;
        }

        if (pos.contains(".")) {
            return extractFieldValue(node.get(pos.substring(0, pos.indexOf("."))), pos.substring(pos.indexOf(".") + 1));
        } else {
            Object obj = getJsonNodeValue(node.get(pos));
            return null == obj ? initVal : obj;
        }
    }

    private static Object getJsonNodeValue(JsonNode node) {

        if (null == node || node.isNull()) {
            return null;
        }

        if (node.isArray()) {
            List<Object> list = new ArrayList<>();
            for (JsonNode nodeItem : node) {
                list.add(getJsonNodeValue(nodeItem));
            }
            return list;
        } else if (node.isObject()) {
            Map<String, Object> map = new HashMap<>();
            for (Map.Entry<String, JsonNode> item : node.properties()) {
                map.put(item.getKey(), getJsonNodeValue(item.getValue()));
            }
            return map;
        } else if (node.isNumber()) {
            return node.numberValue();
        } else if (node.isBoolean()) {
            return node.booleanValue();
        } else if (node.isTextual()) {
            return node.textValue();
        }

        return null;
    }

    /**
     * @Description 文本Base64加密
     * @Param [str] 加密前内容
     * @Return java.lang.String 加密后内容
     * @Author hufei
     * @Date 2020/8/19 11:17
     */
    public static String fromBase64(String str) {
        return Base64.getUrlEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8)).replace('-', '+').replace('_', '/');
    }

    /**
     * @Description 文本Base64解密
     * @Param [base64Str] 解密前内容
     * @Return java.lang.String 解密后内容
     * @Author hufei
     * @Date 2020/8/19 11:17
     */
    public static String parseBase64(String base64Str) {
        return new String(Base64.getDecoder().decode(base64Str.replaceAll("[^A-Za-z0-9+/=]", "+")), StandardCharsets.UTF_8);
    }
}
