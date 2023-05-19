package com.learning.layoutsdk.component;

import com.learning.layoutsdk.definition.JsonProviderMetaType;
import com.learning.layoutsdk.enums.RuleDict;
import com.learning.layoutsdk.enums.TypeDict;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @InterfaceName NumberComponent
 * @Description TODO
 * @Author hufei
 * @Date 2023/2/17 10:41
 * @Version 1.0
 */
public abstract class ObjectComponent implements LayoutComponent {

    public abstract JsonProviderMetaType getObjectType();

    @Override
    public List<JsonProviderMetaType> obtainExtraTypeList() {
        JsonProviderMetaType objType = getObjectType();
        if (null != objType && TypeDict.Struct.getName().equals(objType.getType())) {
            List<JsonProviderMetaType> types = new ArrayList<>();
            JsonProviderMetaType[] currentTypes = objType.getTypes();
            if (null != currentTypes) {
                objType.setTypes(null);
                types.addAll(new ArrayList<>(Arrays.asList(currentTypes)));
            }
            types.add(objType);
            return types;
        }
        return null;
    }

    @Override
    public JsonProviderMetaType toSchema() {

        JsonProviderMetaType objType = getObjectType();
        Assert.notNull(objType, "object type can not be null");

        JsonProviderMetaType field = new JsonProviderMetaType();
        field.setId(RuleDict.Form.getId());
        field.setType(TypeDict.Ref.getName());
        field.setRefId(RuleDict.Form.getName());
        field.setValue(toConfigSchema());

        JsonProviderMetaType type = new JsonProviderMetaType();
        type.setId(getId());
        type.setType(TypeDict.Ref.getName());
        type.setRefId(objType.getId());
        type.setFields(new JsonProviderMetaType[]{field});

        return type;
    }
}
