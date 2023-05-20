package com.learning.metasdk.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.learning.metasdk.definition.MetaField;
import com.learning.metasdk.definition.MetaObject;
import com.learning.metasdk.definition.MetaProperty;
import com.learning.metasdk.definition.MetaType;
import com.learning.metasdk.enums.TypeStatus;
import com.learning.utils.TextUtil;

import java.util.*;

/**
 * @ClassName ObjectHandler
 * @Description TODO
 * @Author hufei
 * @Date 2021/2/4 11:32
 * @Version 1.0
 */
public class ObjectHandler {

    private static int id = 1;

    /**
     * @Description 根据类型信息实例化一个初始化的对象
     * @Param [type]
     * @Author hufei
     * @Date 2021/4/23 11:42
     */
    public static MetaObject instanceObject(MetaType type) {

        if (null == type) {
            return null;
        }

        //常规类型直接略过
        if (type.getStatus() != TypeStatus.Object) {
            return new MetaObject(type).addField(new MetaField(null, initialTypeValue(type, "")));
        }

        //用户自定义类型初始化
        MetaObject obj = new MetaObject(type);
        String name = type.getName();

        for (MetaProperty property : type.getProperties()) {

            MetaField field = new MetaField();
            field.setProperty(property);

            switch (property.getType().getStatus()) {
                case Object:
                    //树结构自引用
                    if (name.equals(property.getType().getName())) {
                        field.setValue(new HashMap<>());//也可以设置为null
                    } else {
                        field.setValue(instanceObject(property.getType()));//也可以设置为null
                    }
                    break;
                case Boolean:
                    field.setValue(false);
                    break;
                case Int:
                    field.setValue(0);
                    break;
                case Double:
                    field.setValue(0.0);
                    break;
                case String:
                    field.setValue("");//也可以设置为null
                    break;
                case Array:
                    MetaType itemType = property.getType().getTypes().get(0);
                    //树结构自引用
                    if (name.equals(itemType.getName())) {
                        field.setValue(new ArrayList<>());//也可以设置为null
                    } else {
                        field.setValue(Collections.singleton(instanceObject(itemType)));//也可以设置为null
                    }
                    break;
                case Enum:
                    List<MetaProperty> properties = property.getType().getProperties();
                    if (properties.size() == 0) {
                        field.setValue("");
                    } else {
                        field.setValue(properties.get(0).getName());
                    }
                    break;
                default:
                    field.setValue(null);
            }

            obj.addField(field);
        }

        obj.setId(id++);

        return obj;
    }

    /**
     * @Description 根据类型信息和值信息, 实例化一个已初始化后的对象
     * @Param [type, data]
     * @Author hufei
     * @Date 2021/4/23 11:43
     */
    public static MetaObject loadObjectJson(MetaType type, String data) {

        if (null == type) {
            return null;
        }

        //常规类型直接略过
        if (type.getStatus() != TypeStatus.Object) {
            return new MetaObject(type).addField(new MetaField(null, initialTypeValue(type, data)));
        }

        //用户自定义类型按步骤赋值
        return loadMetaObject(type, TextUtil.parseJsonNode(data));
    }

    /**
     * @Description 把对象序列化为Json字符串
     * @Param [obj]
     * @Author hufei
     * @Date 2021/4/23 11:46
     */
    public static String serializeObjectJson(MetaObject obj) {

        if (null == obj) {
            return "";
        }

        return TextUtil.fromJson(serializeMetaObject(obj));
    }

    /**
     * @Description 把对象序列化为Map
     * @Param [obj]
     * @Author hufei
     * @Date 2021/4/23 11:47
     */
    private static Object serializeMetaObject(MetaObject obj) {

        TypeStatus status = obj.getType().getStatus();
        boolean empty = obj.getFields().size() == 0;

        if (status == TypeStatus.Object) {

            Map<String, Object> value = new HashMap<>();

            for (MetaField field : obj.getFields()) {
                value.put(field.getProperty().getName(), serializeField(field.getProperty().getType(), field.getValue()));
            }

            return value;
        } else if (status == TypeStatus.Array) {

            if (empty) {
                return initialTypeValue(obj.getType(), "");
            }

            Object value = obj.getFields().get(0).getValue();

            List<Object> list = new ArrayList<>();

            if (value instanceof Collection) {
                for (Object item : (Collection<?>) value) {
                    if (item instanceof MetaObject) {
                        list.add(serializeMetaObject((MetaObject) item));
                    } else {
                        list.add(item);
                    }
                }
            } else {
                Object[] objs = TextUtil.parseJson(TextUtil.fromJson(value), Object[].class);

                if (null == objs) {
                    return initialTypeValue(obj.getType(), "");
                }

                list.addAll(Arrays.asList(objs));
            }

            return list;
        } else {
            return empty ? initialTypeValue(obj.getType(), "") : obj.getFields().get(0).getValue();
        }
    }

    /**
     * @Description 序列化对象的属性
     * @Param [type, value]
     * @Author hufei
     * @Date 2021/4/23 14:12
     */
    private static Object serializeField(MetaType type, Object value) {

        TypeStatus status = type.getStatus();

        switch (status) {
            case Enum:
            case String:
            case Double:
            case Boolean:
            case Int:
                return value;
            case Array:
                List<Object> list = new ArrayList<>();
                MetaType itemType = type.getTypes().get(0);
                if (value instanceof Collection) {
                    for (Object item : (Collection<?>) value) {
                        list.add(serializeField(itemType, item));
                    }
                } else {
                    Object[] objs = TextUtil.parseJson(TextUtil.fromJson(value), Object[].class);
                    if (null != objs) {
                        for (Object obj : objs) {
                            list.add(serializeField(itemType, obj));
                        }
                    }
                }
                return list;
            case Object:
                if (value instanceof MetaObject) {
                    return serializeMetaObject((MetaObject) value);
                } else {
                    MetaObject obj = TextUtil.parseJson(TextUtil.fromJson(value), MetaObject.class);
                    if (null != obj) {
                        return serializeMetaObject(obj);
                    }
                }
        }

        return null;
    }

    /**
     * @Description 根据类型实例化对象并对属性赋值
     * @Param [type, node]
     * @Author hufei
     * @Date 2021/4/23 14:15
     */
    private static MetaObject loadMetaObject(MetaType type, JsonNode node) {

        MetaObject obj = new MetaObject(type);

        if (null == type) {
            return obj;
        }

        for (MetaProperty property : type.getProperties()) {
            MetaField field = new MetaField();
            field.setProperty(property);
            if (null == node) {
                field.setValue(null);
            } else {
                field.setValue(loadField(property.getType(), node.get(property.getName())));
            }
            obj.addField(field);
        }

        obj.setId(id++);

        return obj;
    }

    /**
     * @Description 实例化对象的属性
     * @Param [type, node]
     * @Author hufei
     * @Date 2021/4/23 14:18
     */
    private static Object loadField(MetaType type, JsonNode node) {

        TypeStatus status = type.getStatus();

        if (null == node) {
            return null;
        }

        switch (status) {
            case Enum:
                String data = node.isTextual() ? node.textValue() : "";
                return type.getEnumValues().contains(data) ? data : "";
            case String:
                return node.isTextual() ? node.textValue() : "";
            case Double:
                return node.isDouble() ? node.doubleValue() : 0.0;
            case Boolean:
                return node.isBoolean() && node.booleanValue();
            case Int:
                return node.isInt() ? node.intValue() : 0;
            case Array:
                List<Object> list = new ArrayList<>();
                if (node.isArray()) {
                    MetaType itemType = type.getTypes().get(0);
                    for (JsonNode item : node) {
                        list.add(loadField(itemType, item));
                    }
                }
                return list;
            case Object:
                return loadMetaObject(type, node);
        }

        return null;
    }

    /**
     * @Description 类型初始化赋值
     * @Param [obj, data]
     * @Author hufei
     * @Date 2021/5/20 17:06
     */
    private static Object initialTypeValue(MetaType type, String data) {

        TypeStatus status = type.getStatus();

        switch (status) {
            case Enum:
                List<String> values = type.getEnumValues();
                if (values.contains(data)) {
                    return data;
                }
                return values.size() == 0 ? "enum" : values.get(0);
            case String:
                return (null == data || data.isEmpty()) ? "\"string\"" : data;
            case Double:
                try {
                    return Double.valueOf(data);
                } catch (Exception ignored) {
                    return 0.0;
                }
            case Boolean:
                try {
                    return Boolean.valueOf(data);
                } catch (Exception ignored) {
                    return false;
                }
            case Int:
                try {
                    return Integer.valueOf(data);
                } catch (Exception ignored) {
                    return 0;
                }
            case Array:
                JsonNode node = TextUtil.parseJsonNode(data);
                MetaType itemType = type.getTypes().get(0);
                if (null == node || !node.isArray()) {
                    return Collections.singleton(initialTypeValue(itemType, ""));
                } else {
                    List<Object> list = new ArrayList<>();
                    for (JsonNode item : node) {
                        list.add(loadField(itemType, item));
                    }
                    return list;
                }
            case Object:
                return null == data ? instanceObject(type) : loadField(type, TextUtil.parseJsonNode(data));
        }

        return null;
    }
}
