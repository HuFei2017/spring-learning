package com.learning.layoutsdk.enums;

/**
 * @ClassName TypeStatus
 * @Description TODO
 * @Author hufei
 * @Date 2023/2/17 14:24
 * @Version 1.0
 */
public enum TypeDict {

    Number("number"),
    Text("text"),
    Bool("bool"),
    Enum("enum"),
    Array("array"),
    Ref("ref"),
    Struct("object");

    private final String name;

    TypeDict(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
