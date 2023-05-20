package com.learning.metasdk.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.learning.metasdk.context.TypeContext;
import com.learning.metasdk.definition.MetaField;
import com.learning.metasdk.definition.MetaProperty;
import com.learning.metasdk.definition.MetaType;
import com.learning.metasdk.enums.TypeStatus;
import com.learning.utils.TextUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.*;

/**
 * @ClassName TypeHandler
 * @Description TODO
 * @Author hufei
 * @Date 2021/2/3 16:17
 * @Version 1.0
 */
public class TypeHandler {

    /**
     * @Description 获取枚举值
     * @Param [type]
     * @Author hufei
     * @Date 2021/4/23 10:34
     */
    public static List<String> getEnumValues(MetaType type) {

        if (null == type || type.getStatus() != TypeStatus.Enum) {
            return new ArrayList<>();
        }

        List<String> values = new ArrayList<>();

        for (MetaProperty property : type.getProperties()) {
            values.add(property.getName());
        }

        return values;
    }

    /**
     * @Description 反序列化class文件, 生成MetaType
     * @Param [cl]
     * @Author hufei
     * @Date 2021/4/23 10:34
     */
    public static MetaType loadTypeClass(Class<?> cl) {

        try {
            return loadTypeClass(cl, null);
        } catch (Exception ignored) {
        }

        return new MetaType().setStatus(TypeStatus.Object);
    }

    /**
     * @Description 反序列化Json字符串, 生成MetaType
     * @Param [data]
     * @Author hufei
     * @Date 2021/4/23 10:35
     */
    public static MetaType loadTypeJson(String data) {

        if (null == data || data.isEmpty()) {
            return null;
        }

        JsonNode node = TextUtil.parseJsonNode(data);

        if (null == node) {
            return null;
        }

        if (!node.isArray()) {
            return parseMetaType(node);
        } else {
            if (node.size() == 1) {
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode ot = (ObjectNode) node.get(0);
                ArrayNode propArray = mapper.createArrayNode();
                for (JsonNode item : node.get(0).path("properties")) {
                    if (item.path("type").asText().equals("ref")) {
                        ObjectNode m = (ObjectNode) item;
                        m.put("type", "isref");
                    }
                    propArray.add(item);
                    ot.replace("properties", propArray);
                }
            }

            //先把所有的类加载出来,此时包含类引用信息
            Map<String, MetaType> map = new HashMap<>();
            for (JsonNode item : node) {
                String id = getFieldStringValue(item, "id");
                MetaType meta = parseMetaType(item);
                map.put(id, meta);
            }
            //找到不被引用的类,即是需要的类
            MetaType type = analyseType(map);
            //自我引用的类先处理一遍
            handleSelfRefType(map);
            //把类引用信息替换为具体的类
            if (null != type) {
                replaceRefType(type, type.getName(), map);
            }
            return type;
        }
    }

    /**
     * @Description 反序列化Json字符串为类型上下文, 加载所有的相关类
     * @Param [data]
     * @Author hufei
     * @Date 2021/4/23 10:40
     */
    public static TypeContext loadTypeContext(String data) {

        TypeContext context = new TypeContext();

        if (null == data || data.isEmpty()) {
            return context;
        }

        JsonNode node = TextUtil.parseJsonNode(data);

        if (null == node) {
            return context;
        }

        if (!node.isArray()) {
            context.addType(parseMetaType(node));
        } else {
            //先把所有的类加载出来,此时包含类引用信息
            List<MetaType> list = new ArrayList<>();
            Map<String, MetaType> map = new HashMap<>();
            for (JsonNode item : node) {
                String id = getFieldStringValue(item, "id");
                MetaType meta = parseMetaType(item);
                map.put(id, meta);
                list.add(meta);
            }
            //自我引用的类先处理一遍
            handleSelfRefType(map);
            //把类引用信息替换为具体的类
            for (MetaType item : list) {
                MetaType type = replaceRefType(item, item.getName(), map);
                if (null != type) {
                    context.addType(type);
                }
            }
        }

        return context;
    }

    /**
     * @Description 类型信息序列化Json字符串
     * @Param [type]
     * @Author hufei
     * @Date 2021/4/23 10:42
     */
    public static String serializeTypeJson(MetaType type) {

        if (null == type) {
            return "";
        }

        Map<String, MetaType> map = new HashMap<>();
        //把所有的类析出
        subMetaType(map, type);

        if (type.getStatus() == TypeStatus.Array) {
            map.put(type.getName(), type);
        }

        return serializeType(map);
    }

    /**
     * @Description 类型上下文序列化Json字符串
     * @Param [context]
     * @Author hufei
     * @Date 2021/4/23 10:42
     */
    public static String serializeTypeContext(TypeContext context) {

        Map<String, MetaType> map = new HashMap<>();

        for (MetaType type : context.getTypes()) {
            //把所有的类析出
            subMetaType(map, type);
            if (type.getStatus() == TypeStatus.Array && !map.containsKey(type.getName())) {
                map.put(type.getName(), type);
            }
        }

        return serializeType(map);
    }

    /**
     * @Description 反序列化Json字符串, 解析类型信息, 解析后的类型包含原引用信息
     * @Param [data]
     * @Author hufei
     * @Date 2021/4/23 10:44
     */
    private static MetaType parseMetaType(JsonNode data) {

        if (null == data || data.isNull()) {
            return null;
        }

        String type = getFieldStringValue(data, "type");

        if (type.isEmpty()) {
            return null;
        }

        switch (type) {
            case "text":
                return new MetaType(TypeStatus.String);
            case "isref":
                return new MetaType(data.path("refId").asText(), TypeStatus.Isref);
            case "bool":
                return new MetaType(TypeStatus.Boolean);
            case "number":
                String format = getFieldStringValue(data, "format");
                if (format.equals("double") || format.equals("float")) {
                    return new MetaType(TypeStatus.Double);
                } else {
                    return new MetaType(TypeStatus.Int);
                }
            case "enum": {
                MetaType meta = new MetaType()
                        .setName("enum")
                        .setStatus(TypeStatus.Enum);
                JsonNode enums = data.get("enum");
                if (null != enums && enums.isArray()) {
                    for (JsonNode value : enums) {
                        meta.addEnumValues(getFieldStringValue(value, "value"));
                    }
                }
                return meta;
            }
            case "array": {
                MetaType meta = new MetaType()
                        .setName("array")
                        .setStatus(TypeStatus.Array);
                JsonNode items = data.get("items");
                if (null != items && !items.isNull() && !items.isArray()) {
                    meta.setArrayType(parseMetaType(items));
                }
                return meta;
            }
            case "ref":
                return new MetaType()
                        .setName(getFieldStringValue(data, "refId"))
                        .setStatus(TypeStatus.Ref);
            case "object":
                JsonNode id = data.get("id");
                if (null != id && !id.isNull()) {

                    String idStr = getFieldStringValue(data, "id");
                    //名称和标识
                    MetaType meta = new MetaType()
                            .setName(idStr)
                            .setStatus(TypeStatus.Object);
                    //静态属性
                    JsonNode fields = data.get("fields");
                    if (null != fields && fields.isArray()) {
                        for (JsonNode field : fields) {
                            meta.addField(new MetaField(
                                    new MetaProperty(getFieldStringValue(field, "id"), parseMetaType(field)),
                                    field.get("value")
                            ));
                        }
                    }
                    //属性
                    JsonNode properties = data.get("properties");
                    if (null != properties && properties.isArray()) {
                        for (JsonNode property : properties) {
                            meta.addProperty(new MetaProperty(
                                    getFieldStringValue(property, "id"), parseMetaType(property)
                            ));
                        }
                    }

                    return meta;
                }
                break;
        }

        return null;
    }

    /**
     * @Description 把类型信息中的引用信息替换为真正的类型信息
     * @Param [type, topTypeName, map]
     * @Author hufei
     * @Date 2021/4/23 10:56
     */
    private static MetaType replaceRefType(MetaType type, String topTypeName, Map<String, MetaType> map) {

        if (null != type) {
            if (type.getStatus() == TypeStatus.Ref) {
                //引用必须有效,即存在引用ID
                if (!type.getName().isEmpty()) {
                    String id = type.getName();
                    //引用自己,直接返回
                    if (type.getName().equals(topTypeName)) {
                        return map.get(id);
                    } else {
                        return replaceRefType(map.get(id), topTypeName, map);
                    }
                }
            } else if (type.getStatus() == TypeStatus.Array) {
                //替换数组类型元素类型
                type.setArrayType(replaceRefType(type.getTypes().get(0), topTypeName, map));
                return type;
            } else if (type.getStatus() == TypeStatus.Object) {
                //属性类型替换
                for (MetaProperty property : type.getProperties()) {
                    MetaType meta = replaceRefType(property.getType(), topTypeName, map);
                    property.setType(meta);
                    boolean existed = false;
                    for (MetaType nest : type.getTypes()) {
                        if (nest.getName().equals(meta.getName())) {
                            existed = true;
                            break;
                        }
                    }
                    //属性类型使用了其他类型,类型归并到内部类中且不重复,自己引用自己不归到内部类中
                    if (!existed && !meta.getName().equals(topTypeName)) {
                        type.addNestedType(meta);
                    }
                }
                //静态属性类型替换
                for (MetaField field : type.getFields()) {
                    field.getProperty().setType(replaceRefType(field.getProperty().getType(), topTypeName, map));
                }
                //内部类类型替换
                List<MetaType> types = new ArrayList<>();
                for (MetaType metaType : type.getTypes()) {
                    types.add(replaceRefType(metaType, topTypeName, map));
                }
                type.resetNestedType(types);
                return type;
            }
            //其他情况原样返回
            return type;
        }

        return null;
    }

    /**
     * @Description 去除被引用的类型信息, 查找真正使用的类型信息, 即获取需要的类, 排除仅作为引用的类
     * @Param [map]
     * @Author hufei
     * @Date 2021/4/23 10:57
     */
    private static MetaType analyseType(Map<String, MetaType> map) {

        List<String> refIds = new ArrayList<>();

        for (MetaType type : map.values()) {
            analyseTypeId(refIds, type.getName(), type);
        }

        for (Map.Entry<String, MetaType> entry : map.entrySet()) {
            if (!refIds.contains(String.valueOf(entry.getKey()))) {
                return entry.getValue();
            }
        }

        return null;
    }

    /**
     * @Description 查找所有被引用的类型的类型ID, 自引用的类型不能被当作引用看待
     * @Param [refIds, topTypeName, type]
     * @Author hufei
     * @Date 2021/4/23 10:59
     */
    private static void analyseTypeId(List<String> refIds, String topTypeName, MetaType type) {

        TypeStatus status = type.getStatus();

        switch (status) {
            case Ref:
                String name = type.getName();
                //自己引用自己,不能排除掉,否则无法替换
                if (null != name && !name.isEmpty() && !topTypeName.equals(name) && !refIds.contains(name)) {
                    refIds.add(name);
                }
                break;
            case Object:
                for (MetaType item : type.getTypes()) {
                    analyseTypeId(refIds, topTypeName, item);
                }
                for (MetaProperty item : type.getProperties()) {
                    analyseTypeId(refIds, topTypeName, item.getType());
                }
                for (MetaField item : type.getFields()) {
                    analyseTypeId(refIds, topTypeName, item.getProperty().getType());
                }
                break;
            case Array:
                analyseTypeId(refIds, topTypeName, type.getTypes().get(0));
                break;
            default:
                break;
        }
    }

    /**
     * @Description 扁平化所有相关的类型信息, 即把类型信息及其依赖的类型信息全部解析出来
     * @Param [map, type]
     * @Author hufei
     * @Date 2021/4/23 11:08
     */
    private static void subMetaType(Map<String, MetaType> map, MetaType type) {
        if (type.getStatus() == TypeStatus.Object) {
            if (!map.containsKey(type.getName())) {
                map.put(type.getName(), type);
                for (MetaType metaType : type.getTypes()) {
                    subMetaType(map, metaType);
                }
                for (MetaField field : type.getFields()) {
                    subMetaType(map, field.getProperty().getType());
                }
                for (MetaProperty property : type.getProperties()) {
                    subMetaType(map, property.getType());
                }
            }
        } else if (type.getStatus() == TypeStatus.Array) {
            if (!map.containsKey(type.getName())) {
                for (MetaType metaType : type.getTypes()) {
                    subMetaType(map, metaType);
                }
            }
        } else {
            map.put(type.getName(), type);
        }
    }

    /**
     * @Description 序列化类型信息为Json字符串
     * @Param [map]
     * @Author hufei
     * @Date 2021/4/23 11:09
     */
    private static String serializeType(Map<String, MetaType> map) {

        boolean isUserDefine = validateUserDefine(map);
        boolean isNormalType = validateNormalType(map);

        //从其他地方加载过来的类名称就是其ID
        Set<String> typeNames = map.keySet();
        int maxID = getMaxID(typeNames);
        Map<String, Integer> typeIdMap = getTypeID(typeNames, maxID);

        List<Map<String, Object>> list = new ArrayList<>();

        for (Map.Entry<String, MetaType> entry : map.entrySet()) {
            TypeStatus status = entry.getValue().getStatus();
            //判定常规类型需不需要放进list中进行序列化
            if ((!isUserDefine && isNormalType) || status == TypeStatus.Object || status == TypeStatus.Ref || status == TypeStatus.Array) {
                list.add(replaceRefID(entry.getValue(), entry.getKey(), entry.getKey(), typeIdMap));
            }
        }

        return TextUtil.fromJson(list);
    }

    /**
     * @Description 判断类型中是否存在用户自定义类型
     * @Param [types]
     * @Author hufei
     * @Date 2021/4/23 11:14
     */
    private static boolean validateUserDefine(Map<String, MetaType> types) {
        for (Map.Entry<String, MetaType> entry : types.entrySet()) {
            TypeStatus status = entry.getValue().getStatus();
            if (status == TypeStatus.Object || status == TypeStatus.Ref) {
                return true;
            }
        }
        return false;
    }

    /**
     * @Description 判断类型是否为常规类型
     * @Param [types]
     * @Author hufei
     * @Date 2021/4/23 11:15
     */
    private static boolean validateNormalType(Map<String, MetaType> types) {
        if (types.size() == 1) {
            for (Map.Entry<String, MetaType> entry : types.entrySet()) {
                TypeStatus status = entry.getValue().getStatus();
                if (status != TypeStatus.Object && status != TypeStatus.Ref) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @Description 获取类型信息已被标记的最大ID
     * <p>
     * 类型ID需要是唯一的, 为了避免ID重复, 先从所有的类型信息中检查是否存在已被赋予ID的类型
     * @Param [names]
     * @Author hufei
     * @Date 2021/4/23 11:27
     */
    private static int getMaxID(Collection<String> names) {

        int max = 0;

        for (String name : names) {
            try {
                int tmp = Integer.parseInt(name);
                if (tmp > max) {
                    max = tmp;
                }
            } catch (NumberFormatException ignored) {
            }
        }

        return max;
    }

    /**
     * @Description 给每个类型信息赋予一个类型ID
     * @Param [names, maxID]
     * @Author hufei
     * @Date 2021/4/23 11:33
     */
    private static Map<String, Integer> getTypeID(Collection<String> names, int maxID) {

        Map<String, Integer> map = new HashMap<>();

        for (String name : names) {
            try {
                map.put(name, Integer.parseInt(name));
            } catch (NumberFormatException ignored) {
                map.put(name, ++maxID);
            }
        }

        return map;
    }

    /**
     * @Description 序列化之前, 把真实的引用替换为引用ID
     * @Param [type, typeName, propertyName, typeID]
     * @Author hufei
     * @Date 2021/4/23 11:33
     */
    private static Map<String, Object> replaceRefID(MetaType type, String typeName, String propertyName, Map<String, Integer> typeID) {

        Map<String, Object> map = new HashMap<>();

        if (null == type) {
            return map;
        }

        if (!propertyName.isEmpty()) {
            //只有类ID才会是纯数字型
            if (propertyName.matches("^[-]?[0-9]+$"))
            //类ID修正回数字
            {
                map.put("id", Integer.parseInt(propertyName));
            } else {
                map.put("id", propertyName);
            }
        }

        if (type.getStatus() == TypeStatus.Boolean) {
            map.put("type", "bool");
        } else if (type.getStatus() == TypeStatus.Int) {
            map.put("type", "number");
            map.put("format", "int");
        } else if (type.getStatus() == TypeStatus.Double) {
            map.put("type", "number");
            map.put("format", "double");
        } else if (type.getStatus() == TypeStatus.String) {
            map.put("type", "text");
        } else if (type.getStatus() == TypeStatus.Enum) {
            map.put("type", "enum");

            List<Map<String, String>> enums = new ArrayList<>();

            for (MetaProperty property : type.getProperties()) {
                Map<String, String> item = new HashMap<>();
                item.put("value", property.getName());
                enums.add(item);
            }

            map.put("enum", enums);
        } else if (type.getStatus() == TypeStatus.Array) {
            map.put("type", "array");
            map.put("items", replaceRefID(type.getTypes().get(0), typeName, "", typeID));
        } else if (type.getStatus() == TypeStatus.Ref) {
            map.put("type", "ref");
        } else if (type.getStatus() == TypeStatus.Object) {
            //object
            if (typeName.equals(propertyName)) {
                map.put("type", "object");

                List<Map<String, Object>> properties = new ArrayList<>();

                for (MetaProperty property : type.getProperties()) {
                    properties.add(replaceRefID(property.getType(), typeName, property.getName(), typeID));
                }

                if (properties.size() > 0) {
                    map.put("properties", properties);
                }

                List<Map<String, Object>> fields = new ArrayList<>();

                for (MetaField field : type.getFields()) {
                    Map<String, Object> fieldItem = replaceRefID(field.getProperty().getType(), typeName, field.getProperty().getName(), typeID);
                    fieldItem.put("value", field.getValue());
                    fields.add(fieldItem);
                }

                if (fields.size() > 0) {
                    map.put("fields", fields);
                }
            }
            //ref
            else {
                map.put("type", "ref");
//                map.put("refId", typeID.get(type.getName()));
                map.put("refId", type.getName());
            }
        }

        return map;
    }

    /**
     * @Description 反序列化class文件, 生成MetaType
     * @Param [cl, field]
     * @Author hufei
     * @Date 2021/4/23 10:38
     */
    private static MetaType loadTypeClass(Class<?> cl, Field field) throws Exception {

        String typeName = cl.getTypeName();

        //基本类型
        switch (typeName) {
            case "java.lang.String":
            case "char":
            case "java.lang.Char":
                return new MetaType().setStatus(TypeStatus.String);
            case "int":
            case "java.lang.Integer":
            case "byte":
            case "java.lang.Byte":
            case "short":
            case "java.lang.Short":
            case "long":
            case "java.lang.Long":
            case "java.lang.Number":
                return new MetaType().setStatus(TypeStatus.Int);
            case "boolean":
            case "java.lang.Boolean":
                return new MetaType().setStatus(TypeStatus.Boolean);
            case "java.lang.Float":
            case "float":
            case "java.lang.Double":
            case "double":
                return new MetaType().setStatus(TypeStatus.Double);
        }

        //时间类型
        if (Calendar.class.isAssignableFrom(cl) || Date.class.isAssignableFrom(cl) || Instant.class.isAssignableFrom(cl)) {
            return new MetaType().setStatus(TypeStatus.String);
        }

        //枚举类
        if (cl.isEnum()) {
            MetaType type = new MetaType(cl.getName()).setStatus(TypeStatus.Enum);
            for (Object obj : cl.getEnumConstants()) {
                type.addEnumValues(obj.toString());
            }
            return type;
        }

        //数组
        if (cl.isArray()) {
            MetaType type = new MetaType(cl.getName()).setStatus(TypeStatus.Array);
            type.setArrayType(loadTypeClass(cl.getComponentType(), null));
            return type;
        }

        //List
        if (Collection.class.isAssignableFrom(cl)) {
            MetaType type = new MetaType(cl.getName()).setStatus(TypeStatus.Array);
            if (null == field) {
                type.setArrayType(null);
            } else {
                // 当前集合的泛型类型
                Type genericType = field.getGenericType();
                if (genericType instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType) genericType;
                    type.setArrayType(loadTypeClass((Class<?>) pt.getActualTypeArguments()[0], null));
                } else {
                    type.setArrayType(null);
                }
            }
            return type;
        }

        //Map
        if (Map.class.isAssignableFrom(cl)) {
            return new MetaType(cl.getName()).setStatus(TypeStatus.Object);
        }

        //其他自定义类
        MetaType type = new MetaType(cl.getName()).setStatus(TypeStatus.Object);

        //内部类
        for (Class<?> innerClazz : cl.getDeclaredClasses()) {
            type.addNestedType(loadTypeClass(innerClazz, null));
        }
        //属性+静态变量
        for (Field property : cl.getDeclaredFields()) {
            //静态变量
            if (Modifier.isStatic(property.getModifiers())) {
                property.setAccessible(true);
                type.addField(new MetaField(
                        new MetaProperty(property.getName(), loadTypeClass(property.getType(), property)),
                        property.get(cl)
                ));
            }
            //普通属性
            else {
                type.addProperty(new MetaProperty(property.getName(), loadTypeClass(property.getType(), property)));
            }
        }

        return type;
    }

    /**
     * @Description 处理自引用的类型, 去除其引用关系, 以防出现栈溢出
     * @Param [map]
     * @Author hufei
     * @Date 2021/4/23 11:34
     */
    private static void handleSelfRefType(Map<String, MetaType> map) {
        for (Map.Entry<String, MetaType> entry : map.entrySet()) {
            String index = entry.getKey();
            MetaType type = entry.getValue();
            if (validateSelfRef(type)) {
                map.put(index, repairSelfRef(type));
            }
        }
    }

    /**
     * @Description 判断类型是否为树结构, 即类型是否存在自引用
     * @Param [type]
     * @Author hufei
     * @Date 2021/4/23 11:35
     */
    private static boolean validateSelfRef(MetaType type) {

        if (type.getStatus() != TypeStatus.Object) {
            return false;
        }

        String name = type.getName();

        for (MetaProperty property : type.getProperties()) {
            TypeStatus status = property.getType().getStatus();
            if (status == TypeStatus.Ref && property.getType().getName().equals(name)) {
                return true;
            }
            if (status == TypeStatus.Array && property.getType().getTypes().get(0).getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @Description 重构自引用的类, 以防在引用类替换时出现栈溢出
     * @Param [type]
     * @Author hufei
     * @Date 2021/4/23 11:36
     */
    private static MetaType repairSelfRef(MetaType type) {

        if (null == type) {
            return null;
        }

        String name = type.getName();

        MetaType newType = new MetaType(name).setStatus(type.getStatus());

        //自我引用的类的内部类通常不会是自我引用的
        for (MetaType nestType : type.getTypes()) {
            newType.addNestedType(nestType);
        }

        //自我引用的类的静态变量通常不会是自我引用的
        for (MetaField field : type.getFields()) {
            newType.addField(field);
        }

        //重构自我引用的类
        for (MetaProperty property : type.getProperties()) {
            TypeStatus status = property.getType().getStatus();
            if (status == TypeStatus.Ref && property.getType().getName().equals(name)) {
                newType.addProperty(new MetaProperty(property.getName(), newType));
            } else if (status == TypeStatus.Array && property.getType().getTypes().get(0).getName().equals(name)) {
                newType.addProperty(new MetaProperty(property.getName(), new MetaType(property.getType().getName()).setStatus(TypeStatus.Array).setArrayType(newType)));
            } else {
                newType.addProperty(property);
            }
        }

        return newType;
    }

    private static String getFieldStringValue(JsonNode data, String key) {
        Object value = TextUtil.extractFieldValue(data, key, "");
        if (value instanceof String || value instanceof Number) {
            return value.toString();
        }
        return TextUtil.fromJson(value);
    }

}
