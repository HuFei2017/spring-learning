package com.learning.layoutsdk.component;

import com.learning.layoutsdk.component.definition.JsonProviderMetaType;
import com.learning.layoutsdk.enums.TypeDict;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @InterfaceName NumberComponent
 * @Description TODO
 * @Author hufei
 * @Date 2023/2/17 10:41
 * @Version 1.0
 */
public abstract class ObjectComponent extends AbstractLayoutComponent {

    public abstract JsonProviderMetaType getObjectType();

    @Override
    public List<JsonProviderMetaType> obtainExtraTypeList() {
        JsonProviderMetaType objType = getObjectType();
        return generateExtraTypeList(objType);
    }

    @Override
    public JsonProviderMetaType toSchema() {

        JsonProviderMetaType objType = getObjectType();
        Assert.notNull(objType, "object type can not be null");

        JsonProviderMetaType type = new JsonProviderMetaType();
        type.setId(getId());
        type.setType(TypeDict.Ref.getName());
        type.setRefId(objType.getId());
        type.setFields(new JsonProviderMetaType[]{generateField()});

        return type;
    }
}
