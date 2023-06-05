package com.learning.layoutsdk.component;

import com.learning.layoutsdk.component.annotation.LayoutMethodTag;
import com.learning.layoutsdk.component.definition.JsonProviderMetaType;
import com.learning.layoutsdk.enums.TypeDict;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @InterfaceName NumberComponent
 * @Description TODO
 * @Author hufei
 * @Date 2023/2/17 10:41
 * @Version 1.0
 */
public abstract class ArrayComponent extends AbstractLayoutComponent {

    private JsonProviderMetaType subType = null;
    private Map<String, Object[]> paramMap = new HashMap<>();

    @Deprecated
    public void resetNormalSubType(JsonProviderMetaType type) {
        for (TypeDict item : TypeDict.values()) {
            if (item.getName().equals(type.getType())) {
                Assert.isTrue(item != TypeDict.Ref && item != TypeDict.Array && item != TypeDict.Struct, "it is not a valid normal type");
                subType = new JsonProviderMetaType();
                subType.setType(type.getType());
                subType.setFormat(type.getFormat());
                break;
            }
        }
    }

    @LayoutMethodTag
    public void resetNormalSubType(LayoutComponent component) {
        Assert.isTrue(!(component instanceof ArrayComponent || component instanceof ObjectComponent), "it is not a valid normal component");
        subType = component.toSchema();
        paramMap.put("resetNormalSubType", new Object[]{component});
    }

    @Deprecated
    public void resetStructSubType(JsonProviderMetaType type) {
        Assert.isTrue(TypeDict.Struct.getName().equals(type.getType()), "it is not a valid object type");
        subType = type;
    }

    @LayoutMethodTag
    public void resetStructSubType(LayoutComponent component) {
        Assert.isTrue(component instanceof ObjectComponent, "it is not a valid object component");
        List<JsonProviderMetaType> types = Optional.ofNullable(component.obtainExtraTypeList()).orElse(new ArrayList<>());
        int size = types.size();
        if (size > 0) {
            JsonProviderMetaType targetType = types.get(size - 1);
            targetType.setTypes(types.subList(0, size - 1).toArray(new JsonProviderMetaType[0]));
            subType = targetType;
        }
        paramMap.put("resetStructSubType", new Object[]{component});
    }

    public Map<String, Object[]> getParamMap() {
        return paramMap;
    }

    @Override
    public List<JsonProviderMetaType> obtainExtraTypeList() {
        return generateExtraTypeList(subType);
    }

    @Override
    public JsonProviderMetaType toSchema() {

        JsonProviderMetaType items = new JsonProviderMetaType();
        Assert.notNull(subType, "array item type can not be null");
        Assert.isTrue(!TypeDict.Ref.getName().equals(subType.getType()) && !TypeDict.Array.getName().equals(subType.getType()), "it is not a valid array item type");
        if (TypeDict.Struct.getName().equals(subType.getType())) {
            items.setType(TypeDict.Ref.getName());
            items.setRefId(subType.getId());
        } else {
            items.setType(subType.getType());
            items.setFormat(subType.getFormat());
            items.setRefId(subType.getRefId());
            items.setEnums(subType.getEnums());
        }

        JsonProviderMetaType type = new JsonProviderMetaType();
        type.setId(getId());
        type.setType(TypeDict.Array.getName());
        type.setItems(items);
        type.setFields(new JsonProviderMetaType[]{generateField()});

        return type;
    }
}
