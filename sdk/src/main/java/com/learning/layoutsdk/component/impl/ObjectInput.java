package com.learning.layoutsdk.component.impl;

import com.learning.layoutsdk.component.LayoutComponent;
import com.learning.layoutsdk.component.ObjectComponent;
import com.learning.layoutsdk.component.definition.JsonProviderMetaType;
import com.learning.layoutsdk.component.tool.LayoutComponentTool;
import com.learning.layoutsdk.enums.TypeDict;

import java.util.*;

/**
 * @ClassName ObjectInput
 * @Description TODO
 * @Author hufei
 * @Date 2023/2/17 10:38
 * @Version 1.0
 */
public class ObjectInput extends ObjectComponent {

    private Map<String, Object[]> paramMap;
    private LayoutCommonConfig config;
    private JsonProviderMetaType objType = null;
    private List<JsonProviderMetaType> properties = null;
    private List<JsonProviderMetaType> types = null;

    public ObjectInput() {
        config = LayoutCommonConfig.init();
        paramMap = new HashMap<>();
    }

    @Override
    public ObjectInput withId(String id) {
        config.setId(id);
        if (null == config.getName()) {
            config.setName(id);
        }
        paramMap.put("withId", new Object[]{id});
        return this;
    }

    @Override
    public ObjectInput withName(String name) {
        config.setName(name);
        paramMap.put("withName", new Object[]{name});
        return this;
    }

    @Override
    public ObjectInput withTitleColor(String titleColor) {
        config.setTitleColor(titleColor);
        paramMap.put("withTitleColor", new Object[]{titleColor});
        return this;
    }

    @Override
    public ObjectInput withMulti(int multi) {
        config.setMulti(multi);
        paramMap.put("withMulti", new Object[]{multi});
        return this;
    }

    @Override
    public ObjectInput withHelp(String help) {
        config.setHelp(help);
        paramMap.put("withHelp", new Object[]{help});
        return this;
    }

    @Override
    public ObjectInput withTooltip(String tooltip) {
        config.setTooltip(tooltip);
        paramMap.put("withTooltip", new Object[]{tooltip});
        return this;
    }

    @Override
    public ObjectInput withRequire() {
        config.setRequire(true);
        paramMap.put("withRequire", new Object[0]);
        return this;
    }

    @Override
    public ObjectInput withLineFeed() {
        config.setBr(true);
        paramMap.put("withLineFeed", new Object[0]);
        return this;
    }

    public ObjectInput withDisabled() {
        config.setDisabled(true);
        paramMap.put("withDisabled", new Object[0]);
        return this;
    }

    public ObjectInput withHidden() {
        config.setHidden(true);
        paramMap.put("withHidden", new Object[0]);
        return this;
    }

    @Override
    public ObjectInput withNotRequireCondition(String notRequireCondition) {
        config.setNotRequireCondition(notRequireCondition);
        paramMap.put("withNotRequireCondition", new Object[]{notRequireCondition});
        return this;
    }

    @Override
    public ObjectInput withShowCondition(String showCondition) {
        config.setShowCondition(showCondition);
        paramMap.put("withShowCondition", new Object[]{showCondition});
        return this;
    }

    @Override
    public ObjectInput withHiddenCondition(String hiddenCondition) {
        config.setHiddenCondition(hiddenCondition);
        paramMap.put("withHiddenCondition", new Object[]{hiddenCondition});
        return this;
    }

    @Override
    public ObjectInput withDisableCondition(String disableCondition) {
        config.setDisableCondition(disableCondition);
        paramMap.put("withDisableCondition", new Object[]{disableCondition});
        return this;
    }

    @Deprecated
    public ObjectInput withObjectType(JsonProviderMetaType type) {
        this.objType = type;
        return this;
    }

    public ObjectInput withProperty(LayoutComponent component) {

        if (null == properties) {
            properties = new ArrayList<>();
        }
        properties.add(component.toSchema());

        List<JsonProviderMetaType> extraTypeList = component.obtainExtraTypeList();
        if (null != extraTypeList) {
            if (null == types) {
                types = new ArrayList<>();
            }
            types.addAll(extraTypeList);
        }

        paramMap.put("withProperty", new Object[]{component});
        return this;
    }

    @Override
    public JsonProviderMetaType getObjectType() {
        if (null != objType) {
            return objType;
        }
        JsonProviderMetaType targetType = new JsonProviderMetaType();
        targetType.setId(config.getId());
        targetType.setType(TypeDict.Struct.getName());
        targetType.setProperties(Optional.ofNullable(properties).orElse(new ArrayList<>()).toArray(new JsonProviderMetaType[0]));
        if (null != types) {
            targetType.setTypes(types.toArray(new JsonProviderMetaType[0]));
        }
        return targetType;
    }

    @Override
    public String getId() {
        return config.getId();
    }

    @Override
    public String getName() {
        return config.getName();
    }

    @Override
    public Map toConfigSchema() {
        Map<String, Object> val = new HashMap<>();
        val.put("common", config);
        return val;
    }

    @Override
    public ObjectInput copy() {
        ObjectInput result = new ObjectInput();
        LayoutComponentTool.resetParam(result, paramMap, new HashMap<>());
        return result;
    }
}
