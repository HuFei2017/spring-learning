package com.learning.layoutsdk.component.impl;

import com.learning.layoutsdk.component.BoolComponent;
import com.learning.layoutsdk.component.tool.LayoutComponentTool;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SwitchInput
 * @Description TODO
 * @Author hufei
 * @Date 2023/2/17 10:35
 * @Version 1.0
 */
public class SwitchInput extends BoolComponent {

    private Map<String, Object[]> paramMap;
    private LayoutCommonConfig config;

    public SwitchInput() {
        config = LayoutCommonConfig.init();
        config.setDefaultValue(false);
        paramMap = new HashMap<>();
    }

    @Override
    public SwitchInput withId(String id) {
        config.setId(id);
        if (null == config.getName()) {
            config.setName(id);
        }
        paramMap.put("withId", new Object[]{id});
        return this;
    }

    @Override
    public SwitchInput withName(String name) {
        config.setName(name);
        paramMap.put("withName", new Object[]{name});
        return this;
    }

    @Override
    public SwitchInput withTitleColor(String titleColor) {
        config.setTitleColor(titleColor);
        paramMap.put("withTitleColor", new Object[]{titleColor});
        return this;
    }

    @Override
    public SwitchInput withMulti(int multi) {
        config.setMulti(multi);
        paramMap.put("withMulti", new Object[]{multi});
        return this;
    }

    public SwitchInput withDefaultValue(boolean value) {
        config.setDefaultValue(value);
        paramMap.put("withDefaultValue", new Object[]{value});
        return this;
    }

    @Override
    public SwitchInput withHelp(String help) {
        config.setHelp(help);
        paramMap.put("withHelp", new Object[]{help});
        return this;
    }

    @Override
    public SwitchInput withTooltip(String tooltip) {
        config.setTooltip(tooltip);
        paramMap.put("withTooltip", new Object[]{tooltip});
        return this;
    }

    @Override
    public SwitchInput withRequire() {
        config.setRequire(true);
        paramMap.put("withRequire", new Object[0]);
        return this;
    }

    @Override
    public SwitchInput withLineFeed() {
        config.setBr(true);
        paramMap.put("withLineFeed", new Object[0]);
        return this;
    }

    public SwitchInput withDisabled() {
        config.setDisabled(true);
        paramMap.put("withDisabled", new Object[0]);
        return this;
    }

    public SwitchInput withHidden() {
        config.setHidden(true);
        paramMap.put("withHidden", new Object[0]);
        return this;
    }

    @Override
    public SwitchInput withNotRequireCondition(String notRequireCondition) {
        config.setNotRequireCondition(notRequireCondition);
        paramMap.put("withNotRequireCondition", new Object[]{notRequireCondition});
        return this;
    }

    @Override
    public SwitchInput withShowCondition(String showCondition) {
        config.setShowCondition(showCondition);
        paramMap.put("withShowCondition", new Object[]{showCondition});
        return this;
    }

    @Override
    public SwitchInput withHiddenCondition(String hiddenCondition) {
        config.setHiddenCondition(hiddenCondition);
        paramMap.put("withHiddenCondition", new Object[]{hiddenCondition});
        return this;
    }

    @Override
    public SwitchInput withDisableCondition(String disableCondition) {
        config.setDisableCondition(disableCondition);
        paramMap.put("withDisableCondition", new Object[]{disableCondition});
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
        Map<String, Object> val = new HashMap<>();
        val.put("common", config);
        return val;
    }

    @Override
    public SwitchInput copy() {
        SwitchInput result = new SwitchInput();
        LayoutComponentTool.resetParam(result, paramMap, new HashMap<>());
        return result;
    }
}
