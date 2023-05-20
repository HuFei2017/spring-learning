package com.learning.metasdk.definition;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.learning.utils.TextUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName MetaObject
 * @Description TODO
 * @Author hufei
 * @Date 2021/1/28 14:36
 * @Version 1.0
 */
@Getter
@Setter
public class MetaObject {

    /**
     * 对象ID
     */
    private int id;
    /**
     * 对象类型
     */
    private MetaType type;
    /**
     * 属性值
     */
    private List<MetaField> fields;

    public MetaObject() {
        fields = new ArrayList<>();
    }

    public MetaObject(MetaType type) {
        this();
        this.type = type;
    }

    /**
     * @Description 设置对象ID
     * @Param [id]
     * @Author hufei
     * @Date 2021/4/23 10:23
     */
    public MetaObject setId(int id) {
        this.id = id;
        return this;
    }

    /**
     * @Description 设置对象类型
     * @Param [type]
     * @Author hufei
     * @Date 2021/4/23 10:23
     */
    public MetaObject setType(MetaType type) {
        this.type = type;
        return this;
    }

    /**
     * @Description 为属性赋值, 即添加赋值后的属性
     * @Param [field]
     * @Author hufei
     * @Date 2021/4/23 10:23
     */
    public MetaObject addField(MetaField field) {
        fields.add(field);
        return this;
    }

    /**
     * @Description 常规类型赋值
     * @Param [value]
     * @Author hufei
     * @Date 2021/5/21 9:51
     */
    @JsonIgnore
    public MetaObject setValue(Object value) {
        fields.clear();
        fields.add(new MetaField(null, value));
        return this;
    }

    /**
     * @Description 常规类型取值
     * @Param []
     * @Author hufei
     * @Date 2021/5/21 9:51
     */
    @JsonIgnore
    public Object getValue() {

        if (fields.size() == 0) {
            return null;
        }

        return fields.get(0).getValue();
    }

    /**
     * @Description 对象判等
     * @Param [obj]
     * @Author hufei
     * @Date 2021/4/23 10:24
     */
    public boolean equals(MetaObject obj) {

        if (null == obj) {
            return false;
        }

        if (type != obj.getType()) {
            if (!type.equals(obj.getType())) {
                return false;
            }
        }

        if (fields != obj.getFields()) {
            if (null == obj.getFields()) {
                return false;
            }

            if (fields.size() != obj.getFields().size()) {
                return false;
            }

            int size = fields.size();

            for (int i = 0; i < size; i++) {
                if (!fields.get(i).equals(obj.getFields().get(i))) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * @Description 获取静态属性类型
     * @Param [fieldName]
     * @Author hufei
     * @Date 2021/4/23 10:24
     */
    public MetaType getStaticFieldType(String fieldName) {

        if (fieldName.startsWith(".") || fieldName.endsWith(".")) {
            return null;
        }

        return type.getFieldType(fieldName);
    }

    /**
     * @Description 获取属性类型
     * @Param [fieldName]
     * @Author hufei
     * @Date 2021/4/23 10:24
     */
    public MetaType getFieldType(String fieldName) {

        if (fieldName.startsWith(".") || fieldName.endsWith(".")) {
            return null;
        }

        return type.getPropertyType(fieldName);
    }

    /**
     * @Description 获取属性值
     * @Param [fieldName]
     * @Author hufei
     * @Date 2021/4/23 10:24
     */
    public Object getFieldValue(String fieldName) {

        if (fieldName.startsWith(".") || fieldName.endsWith(".")) {
            return null;
        }

        if (fieldName.contains(".")) {
            int index = fieldName.indexOf(".");
            String prefix = fieldName.substring(0, index);
            String suffix = fieldName.substring(index + 1);
            return parseFieldValue(prefix, suffix);
        } else {
            return parseFieldValue(fieldName, "");
        }
    }

    /**
     * @Description 设置属性值
     * @Param [fieldName, value]
     * @Author hufei
     * @Date 2021/4/23 10:25
     */
    public void setFieldValue(String fieldName, Object value) {
        if (!fieldName.startsWith(".") && !fieldName.endsWith(".")) {
            if (fieldName.contains(".")) {
                int index = fieldName.indexOf(".");
                String prefix = fieldName.substring(0, index);
                String suffix = fieldName.substring(index + 1);
                for (MetaField field : fields) {
                    if (field.getProperty().getName().equals(prefix)) {
                        MetaObject object = TextUtil.parseJson(TextUtil.fromJson(field.getValue()), MetaObject.class);
                        if (null != object) {
                            object.setFieldValue(suffix, value);
                            field.setValue(object);
                        }
                    }
                }
            } else {
                for (MetaField field : fields) {
                    if (field.getProperty().getName().equals(fieldName)) {
                        field.setValue(value);
                        break;
                    }
                }
            }
        }
    }

    /**
     * @Description 递归获取属性值, 支持路径索引
     * @Param [pName, cName]
     * @Author hufei
     * @Date 2021/4/23 10:25
     */
    private Object parseFieldValue(String pName, String cName) {

        if (pName.equals("[]")) {
            return null;
        }

        if (pName.contains("[") || pName.contains("]")) {

            if (!pName.matches("\\w+\\[[0-9]+]$")) {
                return null;
            }

            int startIndex = pName.indexOf("[");
            int endIndex = pName.indexOf("]");
            String subField = pName.substring(0, startIndex);
            int subScript = Integer.parseInt(pName.substring(startIndex + 1, endIndex));

            for (MetaField field : fields) {
                if (field.getProperty().getName().equals(subField)) {
                    if (cName.isEmpty()) {
                        Object[] objs = TextUtil.parseJson(TextUtil.fromJson(field.getValue()), Object[].class);
                        if (null == objs || objs.length <= subScript) {
                            return null;
                        }
                        return objs[subScript];
                    } else {
                        MetaObject[] objs = TextUtil.parseJson(TextUtil.fromJson(field.getValue()), MetaObject[].class);
                        if (null == objs || objs.length <= subScript) {
                            return null;
                        }
                        return objs[subScript].getFieldValue(cName);
                    }
                }
            }
        } else {
            for (MetaField field : fields) {
                if (field.getProperty().getName().equals(pName)) {
                    if (cName.isEmpty()) {
                        return field.getValue();
                    } else {
                        MetaObject object = TextUtil.parseJson(TextUtil.fromJson(field.getValue()), MetaObject.class);
                        if (null == object) {
                            return null;
                        }
                        return object.getFieldValue(cName);
                    }
                }
            }
        }

        return null;
    }
}
