package com.learning.layoutsdk.component;

import com.learning.layoutsdk.component.definition.JsonProviderMetaType;
import com.learning.layoutsdk.enums.TypeDict;

/**
 * @InterfaceName NumberComponent
 * @Description TODO
 * @Author hufei
 * @Date 2023/2/17 10:41
 * @Version 1.0
 */
public abstract class BoolComponent extends AbstractLayoutComponent {

    @Override
    public JsonProviderMetaType toSchema() {

        JsonProviderMetaType type = new JsonProviderMetaType();
        type.setId(getId());
        type.setType(TypeDict.Bool.getName());
        type.setFields(new JsonProviderMetaType[]{generateField()});

        return type;
    }

}
