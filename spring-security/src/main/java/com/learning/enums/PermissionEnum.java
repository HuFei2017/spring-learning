package com.learning.enums;

import lombok.Getter;

/**
 * @EnumName PermissionEnum
 * @Description TODO
 * @Author hufei
 * @Date 2023/5/4 16:16
 * @Version 1.0
 */
public enum PermissionEnum {

    READ("仅查看", PermissionActionEnum.LIST),
    EDIT("可编辑", PermissionActionEnum.LIST, PermissionActionEnum.ADD, PermissionActionEnum.EDIT),
    DELETE("可删除", PermissionActionEnum.LIST, PermissionActionEnum.ADD, PermissionActionEnum.EDIT, PermissionActionEnum.DELETE),
    MANAGE("可管理", PermissionActionEnum.LIST, PermissionActionEnum.ADD, PermissionActionEnum.EDIT, PermissionActionEnum.DELETE, PermissionActionEnum.TEAM),
    OWNER("所有者", PermissionActionEnum.values());

    @Getter
    private final String name;
    @Getter
    private final PermissionActionEnum[] actionList;

    PermissionEnum(String name, PermissionActionEnum... actionList) {
        this.name = name;
        this.actionList = actionList;
    }
}
