package com.learning.metasdk.definition;

import com.learning.metasdk.enums.TypeStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName MetaType
 * @Description TODO
 * @Author hufei
 * @Date 2021/1/28 14:27
 * @Version 1.0
 */
@Getter
@Setter
public class MetaType {

    /**
     * 类型名称
     */
    private String name;
    /**
     * 类型标识
     */
    private TypeStatus status;
    /**
     * 子类型列表,status为数组时存放1个类型
     */
    private List<MetaType> types;
    /**
     * 属性列表
     */
    private List<MetaProperty> properties;
    /**
     * 静态属性列表
     */
    private List<MetaField> fields;

    public MetaType() {
        types = new ArrayList<>();
        properties = new ArrayList<>();
        fields = new ArrayList<>();
    }

    public MetaType(String name) {
        this();
        this.name = name;
    }

    public MetaType(TypeStatus status) {
        this();
        setStatus(status);
    }

    public MetaType(String name, TypeStatus status) {
        this();
        this.name = name;
        this.status = status;
    }

    public MetaType setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @Description 设置类型状态
     * @Param [status]
     * @Author hufei
     * @Date 2021/4/23 10:10
     */
    public MetaType setStatus(TypeStatus status) {
        this.status = status;

        switch (status) {
            case Int:
                this.name = "int";
                break;
            case String:
                this.name = "string";
                break;
            case Boolean:
                this.name = "bool";
                break;
            case Double:
                this.name = "double";
                break;
        }

        return this;
    }

    /**
     * @Description 初始化枚举类型的枚举值
     * @Param [values]
     * @Author hufei
     * @Date 2021/4/23 10:11
     */
    public MetaType addEnumValues(String... values) {
        if (status == TypeStatus.Enum) {
            for (String value : values) {
                properties.add(new MetaProperty(value, new MetaType(TypeStatus.String)));
            }
            types = new ArrayList<>();
            fields = new ArrayList<>();
        }
        return this;
    }

    /**
     * @Description 获取枚举类型所有的枚举值
     * @Param []
     * @Author hufei
     * @Date 2021/5/20 21:16
     */
    public List<String> getEnumValues() {
        List<String> list = new ArrayList<>();
        if (status == TypeStatus.Enum) {
            for (MetaProperty property : properties) {
                list.add(property.getName());
            }
        }
        return list;
    }

    /**
     * @Description 设置数组类型的元素类型
     * @Param [type]
     * @Author hufei
     * @Date 2021/4/23 10:11
     */
    public MetaType setArrayType(MetaType type) {
        if (status == TypeStatus.Array && type.getStatus() != TypeStatus.Array) {
            types.clear();
            types.add(type);
            properties = new ArrayList<>();
            fields = new ArrayList<>();
        }
        return this;
    }

    /**
     * @Description 获取数组类型的元素类型
     * @Param []
     * @Author hufei
     * @Date 2021/7/15 17:05
     */
    public MetaType getArrayType() {
        if (status == TypeStatus.Array && types.size() > 0) {
            return types.get(0);
        } else {
            return null;
        }
    }

    /**
     * @Description 设置内部类类型
     * @Param [type]
     * @Author hufei
     * @Date 2021/4/23 10:11
     */
    public MetaType addNestedType(MetaType type) {
        TypeStatus status = type.getStatus();
        if (status == TypeStatus.Object || status == TypeStatus.Ref
                || status == TypeStatus.Nested) {
            types.add(type);
        }
        return this;
    }

    /**
     * @Description 移除内部类类型
     * @Param [typeName]
     * @Author hufei
     * @Date 2021/4/23 10:11
     */
    public MetaType removeNestedType(String typeName) {
        for (MetaType item : types) {
            if (item.getName().equals(typeName)) {
                types.remove(item);
                break;
            }
        }
        return this;
    }

    /**
     * @Description 清空内部类类型
     * @Param [typeList]
     * @Author hufei
     * @Date 2021/4/23 10:11
     */
    public MetaType resetNestedType(List<MetaType> typeList) {
        types.clear();
        types.addAll(typeList);
        return this;
    }

    /**
     * @Description 添加属性
     * @Param [property]
     * @Author hufei
     * @Date 2021/4/23 10:12
     */
    public MetaType addProperty(MetaProperty property) {
        properties.add(property);
        return this;
    }

    /**
     * @Description 删除属性
     * @Param [propertyName]
     * @Author hufei
     * @Date 2021/4/23 10:12
     */
    public MetaType removeProperty(String propertyName) {
        for (MetaProperty item : properties) {
            if (item.getName().equals(propertyName)) {
                properties.remove(item);
                break;
            }
        }
        return this;
    }

    /**
     * @Description 添加静态变量
     * @Param [field]
     * @Author hufei
     * @Date 2021/4/23 10:12
     */
    public MetaType addField(MetaField field) {
        fields.add(field);
        return this;
    }

    /**
     * @Description 移除静态变量
     * @Param [fieldName]
     * @Author hufei
     * @Date 2021/4/23 10:12
     */
    public MetaType removeField(String fieldName) {
        for (MetaField item : fields) {
            if (item.getProperty().getName().equals(fieldName)) {
                fields.remove(item);
                break;
            }
        }
        return this;
    }

    /**
     * @Description 获取字段类型
     * @Param [fieldName]
     * @Author hufei
     * @Date 2021/7/15 17:07
     */
    public MetaType getPropertyType(String fieldName) {

        if (fieldName.startsWith(".") || fieldName.endsWith(".")) {
            return null;
        }

        if (fieldName.contains(".")) {
            int index = fieldName.indexOf(".");
            String prefix = fieldName.substring(0, index);
            String suffix = fieldName.substring(index + 1);
            for (MetaProperty property : properties) {
                if (property.getName().equals(prefix)) {
                    return property.getType().getPropertyType(suffix);
                }
            }
        } else {
            for (MetaProperty property : properties) {
                if (property.getName().equals(fieldName)) {
                    return property.getType();
                }
            }
        }

        return null;
    }

    /**
     * @Description 获取静态变量类型
     * @Param [fieldName]
     * @Author hufei
     * @Date 2021/4/23 10:12
     */
    public MetaType getFieldType(String fieldName) {

        if (fieldName.startsWith(".") || fieldName.endsWith(".")) {
            return null;
        }

        if (fieldName.contains(".")) {
            int index = fieldName.indexOf(".");
            String prefix = fieldName.substring(0, index);
            String suffix = fieldName.substring(index + 1);
            for (MetaField field : fields) {
                if (field.getProperty().getName().equals(prefix)) {
                    return field.getProperty().getType().getFieldType(suffix);
                }
            }
        } else {
            for (MetaField field : fields) {
                if (field.getProperty().getName().equals(fieldName)) {
                    return field.getProperty().getType();
                }
            }
        }

        return null;
    }

    /**
     * @Description 获取静态变量值
     * @Param [fieldName]
     * @Author hufei
     * @Date 2021/4/23 10:12
     */
    public Object getFieldValue(String fieldName) {

        if (fieldName.startsWith(".") || fieldName.endsWith(".")) {
            return null;
        }

        if (fieldName.contains(".")) {
            int index = fieldName.indexOf(".");
            String prefix = fieldName.substring(0, index);
            String suffix = fieldName.substring(index + 1);
            for (MetaField field : fields) {
                if (field.getProperty().getName().equals(prefix)) {
                    return field.getProperty().getType().getFieldValue(suffix);
                }
            }
        } else {
            for (MetaField field : fields) {
                if (field.getProperty().getName().equals(fieldName)) {
                    return field.getValue();
                }
            }
        }

        return null;
    }

    /**
     * @Description 类型判等
     * @Param [type]
     * @Author hufei
     * @Date 2021/4/23 10:13
     */
    public boolean equals(MetaType type) {

        if (null == type) {
            return false;
        }

        if (!name.equals(type.getName())) {
            return false;
        }

        if (status != type.getStatus()) {
            return false;
        }

        if (types != type.getTypes()) {
            if (null == type.getTypes()) {
                return false;
            }

            if (types.size() != type.getTypes().size()) {
                return false;
            }

            int size = types.size();

            for (int i = 0; i < size; i++) {
                if (!types.get(i).equals(type.getTypes().get(i))) {
                    return false;
                }
            }
        }

        if (properties != type.getProperties()) {
            if (null == type.getProperties()) {
                return false;
            }

            if (properties.size() != type.getProperties().size()) {
                return false;
            }

            int size = properties.size();

            for (int i = 0; i < size; i++) {
                if (!properties.get(i).equals(type.getProperties().get(i))) {
                    return false;
                }
            }
        }

        if (fields != type.getFields()) {
            if (null == type.getFields()) {
                return false;
            }

            if (fields.size() != type.getFields().size()) {
                return false;
            }

            int size = fields.size();

            for (int i = 0; i < size; i++) {
                if (!fields.get(i).equals(type.getFields().get(i))) {
                    return false;
                }
            }
        }

        return true;
    }
}
