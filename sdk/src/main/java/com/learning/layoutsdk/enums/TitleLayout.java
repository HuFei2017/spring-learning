package com.learning.layoutsdk.enums;

/**
 * @ClassName TitleLayout
 * @Description TODO
 * @Author hufei
 * @Date 2023/2/17 13:44
 * @Version 1.0
 */
public enum TitleLayout {

    UP("vertical"),
    MID("horizontal");

    private final String name;

    TitleLayout(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
