package com.learning.layoutsdk.enums;

/**
 * @ClassName RuleDict
 * @Description TODO
 * @Author hufei
 * @Date 2023/2/17 16:52
 * @Version 1.0
 */
public enum RuleDict {

    Form("Rule.Form", "com.ronds.schema.default.Rule.Form");

    private final String id;

    private final String name;

    RuleDict(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
