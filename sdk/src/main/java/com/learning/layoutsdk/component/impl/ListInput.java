package com.learning.layoutsdk.component.impl;

import com.learning.layoutsdk.component.ArrayComponent;
import com.learning.layoutsdk.component.LayoutComponent;
import com.learning.layoutsdk.component.definition.JsonProviderMetaType;
import com.learning.layoutsdk.component.tool.LayoutComponentTool;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ListInput
 * @Description TODO
 * @Author hufei
 * @Date 2023/2/17 10:37
 * @Version 1.0
 */
public class ListInput extends ArrayComponent {

    private Map<String, Object[]> paramMap;
    private LayoutCommonConfig config;
    private boolean hiddenTitle = false;

    public ListInput() {
        config = LayoutCommonConfig.init();
        paramMap = new HashMap<>();
    }

    @Override
    public ListInput withId(String id) {
        config.setId(id);
        if (null == config.getName()) {
            config.setName(id);
        }
        paramMap.put("withId", new Object[]{id});
        return this;
    }

    @Override
    public ListInput withName(String name) {
        config.setName(name);
        paramMap.put("withName", new Object[]{name});
        return this;
    }

    @Override
    public ListInput withTitleColor(String titleColor) {
        config.setTitleColor(titleColor);
        paramMap.put("withTitleColor", new Object[]{titleColor});
        return this;
    }

    @Override
    public ListInput withMulti(int multi) {
        config.setMulti(multi);
        paramMap.put("withMulti", new Object[]{multi});
        return this;
    }

    @Override
    public ListInput withHelp(String help) {
        config.setHelp(help);
        paramMap.put("withHelp", new Object[]{help});
        return this;
    }

    @Override
    public ListInput withTooltip(String tooltip) {
        config.setTooltip(tooltip);
        paramMap.put("withTooltip", new Object[]{tooltip});
        return this;
    }

    @Override
    public ListInput withRequire() {
        config.setRequire(true);
        paramMap.put("withRequire", new Object[0]);
        return this;
    }

    @Override
    public ListInput withLineFeed() {
        config.setBr(true);
        paramMap.put("withLineFeed", new Object[0]);
        return this;
    }

    public ListInput withDisabled() {
        config.setDisabled(true);
        paramMap.put("withDisabled", new Object[0]);
        return this;
    }

    public ListInput withHidden() {
        config.setHidden(true);
        paramMap.put("withHidden", new Object[0]);
        return this;
    }

    @Override
    public ListInput withNotRequireCondition(String notRequireCondition) {
        config.setNotRequireCondition(notRequireCondition);
        paramMap.put("withNotRequireCondition", new Object[]{notRequireCondition});
        return this;
    }

    @Override
    public ListInput withShowCondition(String showCondition) {
        config.setShowCondition(showCondition);
        paramMap.put("withShowCondition", new Object[]{showCondition});
        return this;
    }

    @Override
    public ListInput withHiddenCondition(String hiddenCondition) {
        config.setHiddenCondition(hiddenCondition);
        paramMap.put("withHiddenCondition", new Object[]{hiddenCondition});
        return this;
    }

    public ListInput withTitleHidden() {
        this.hiddenTitle = true;
        paramMap.put("withTitleHidden", new Object[0]);
        return this;
    }

    @Deprecated
    public ListInput withNormalSubType(JsonProviderMetaType type) {
        super.resetNormalSubType(type);
        return this;
    }

    public ListInput withNormalSubType(LayoutComponent component) {
        super.resetNormalSubType(component);
        return this;
    }

    @Deprecated
    public ListInput withStructSubType(JsonProviderMetaType type) {
        super.resetStructSubType(type);
        return this;
    }

    public ListInput withStructSubType(LayoutComponent component) {
        super.resetStructSubType(component);
        return this;
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
        Map<String, Object> arrayVal = new HashMap<>();
        arrayVal.put("layout", "default");
        arrayVal.put("titleHidden", hiddenTitle);
        Map<String, Object> val = new HashMap<>();
        val.put("common", config);
        val.put("array", arrayVal);
        return val;
    }

    @Override
    public ListInput copy() {
        ListInput result = new ListInput();
        LayoutComponentTool.resetParam(result, paramMap, super.getParamMap());
        return result;
    }
}
