package com.learning.layoutsdk.enums;

/**
 * @ClassName TypeStatus
 * @Description TODO
 * @Author hufei
 * @Date 2023/2/17 14:24
 * @Version 1.0
 */
public enum NumberDict {

    Double("double"),
    Integer("int");

    private final String name;

    NumberDict(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
