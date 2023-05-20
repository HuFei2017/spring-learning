package com.learning.metasdk.definition;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName MetaProperty
 * @Description TODO
 * @Author hufei
 * @Date 2021/1/28 14:27
 * @Version 1.0
 */
@Getter
@Setter
public class MetaProperty {

    /**
     * 属性名称
     */
    private String name;
    /**
     * 属性类型
     */
    private MetaType type;

    public MetaProperty() {
    }

    public MetaProperty(String name) {
        this();
        this.name = name;
    }

    public MetaProperty(String name, MetaType type) {
        this();
        this.name = name;
        this.type = type;
    }

    /**
     * @Description 设置属性名称
     * @Param [name]
     * @Author hufei
     * @Date 2021/4/23 10:14
     */
    public MetaProperty setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @Description 设置属性类型
     * @Param [type]
     * @Author hufei
     * @Date 2021/4/23 10:14
     */
    public MetaProperty setType(MetaType type) {
        this.type = type;
        return this;
    }

    /**
     * @Description 属性判等
     * @Param [property]
     * @Author hufei
     * @Date 2021/4/23 10:14
     */
    public boolean equals(MetaProperty property) {

        if (null == property) {
            return false;
        }

        if (!name.equals(property.getName())) {
            return false;
        }

        if (type != property.getType()) {
            return type.equals(property.getType());
        }

        return true;
    }

}
