package com.learning.layoutsdk.component;

import com.learning.layoutsdk.definition.JsonProviderMetaType;
import com.learning.layoutsdk.enums.RuleDict;
import com.learning.layoutsdk.enums.TypeDict;

/**
 * @InterfaceName NumberComponent
 * @Description TODO
 * @Author hufei
 * @Date 2023/2/17 10:41
 * @Version 1.0
 */
public abstract class BoolComponent implements LayoutComponent {

    @Override
    public JsonProviderMetaType toSchema() {

        JsonProviderMetaType field = new JsonProviderMetaType();
        field.setId(RuleDict.Form.getId());
        field.setType(TypeDict.Ref.getName());
        field.setRefId(RuleDict.Form.getName());
        field.setValue(toConfigSchema());

        JsonProviderMetaType type = new JsonProviderMetaType();
        type.setId(getId());
        type.setType(TypeDict.Bool.getName());
        type.setFields(new JsonProviderMetaType[]{field});

        return type;
    }

}
