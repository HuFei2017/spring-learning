package com.learning.metasdk.definition;

import com.learning.utils.TextUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * @ClassName MetaField
 * @Description TODO
 * @Author hufei
 * @Date 2021/1/28 14:46
 * @Version 1.0
 */
@Getter
@Setter
public class MetaField {

    /**
     * 静态变量属性信息
     */
    private MetaProperty property;
    /**
     * 静态变量值
     */
    private Object value;

    public MetaField() {
    }

    public MetaField(MetaProperty property) {
        this();
        this.property = property;
    }

    public MetaField(MetaProperty property, Object value) {
        this();
        this.property = property;
        this.value = value;
    }

    /**
     * @Description 设置属性信息
     * @Param [property]
     * @Author hufei
     * @Date 2021/4/23 10:16
     */
    public MetaField setProperty(MetaProperty property) {
        this.property = property;
        return this;
    }

    /**
     * @Description 初始化变量值
     * @Param [value]
     * @Author hufei
     * @Date 2021/4/23 10:16
     */
    public MetaField setValue(Object value) {
        this.value = value;
        return this;
    }

    /**
     * @Description 静态变量判等
     * @Param [field]
     * @Author hufei
     * @Date 2021/4/23 10:16
     */
    public boolean equals(MetaField field) {

        if (null == field) {
            return false;
        }

        MetaObject obj1 = parseMetaObject(value);
        MetaObject obj2 = parseMetaObject(field.getValue());
        if (null == obj1 && null == obj2) {
            if (!value.equals(field.getValue())) {
                return false;
            }
        } else {
            if (null == obj1 || null == obj2) {
                return false;
            } else if (!obj1.equals(obj2)) {
                return false;
            }
        }

        if (property != field.getProperty()) {
            return property.equals(field.getProperty());
        }

        return true;
    }

    private MetaObject parseMetaObject(Object obj) {

        if (null == obj) {
            return null;
        }

        if (obj instanceof String || obj instanceof Number || obj instanceof UUID) {
            return null;
        }

        return obj instanceof MetaObject ? (MetaObject) obj : TextUtil.parseJson(TextUtil.fromJson(obj), MetaObject.class);
    }
}
