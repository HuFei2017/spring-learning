package com.learning.layoutsdk.component.impl;

import com.learning.layoutsdk.component.NumberComponent;
import com.learning.layoutsdk.component.tool.LayoutComponentTool;
import com.learning.layoutsdk.enums.NumberDict;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName NumberInput
 * @Description TODO
 * @Author hufei
 * @Date 2023/2/17 10:34
 * @Version 1.0
 */
public class DoubleInput extends NumberComponent {

    private Map<String, Object[]> paramMap;
    private LayoutCommonConfig config;

    public DoubleInput() {
        config = LayoutCommonConfig.init();
        paramMap = new HashMap<>();
    }

    @Override
    public DoubleInput withId(String id) {
        config.setId(id);
        if (null == config.getName()) {
            config.setName(id);
        }
        paramMap.put("withId", new Object[]{id});
        return this;
    }

    @Override
    public DoubleInput withName(String name) {
        config.setName(name);
        paramMap.put("withName", new Object[]{name});
        return this;
    }

    @Override
    public DoubleInput withTitleColor(String titleColor) {
        config.setTitleColor(titleColor);
        paramMap.put("withTitleColor", new Object[]{titleColor});
        return this;
    }

    @Override
    public DoubleInput withMulti(int multi) {
        config.setMulti(multi);
        paramMap.put("withMulti", new Object[]{multi});
        return this;
    }

    public DoubleInput withDefaultValue(double value) {
        config.setDefaultValue(value);
        paramMap.put("withDefaultValue", new Object[]{value});
        return this;
    }

    @Override
    public DoubleInput withHelp(String help) {
        config.setHelp(help);
        paramMap.put("withHelp", new Object[]{help});
        return this;
    }

    @Override
    public DoubleInput withTooltip(String tooltip) {
        config.setTooltip(tooltip);
        paramMap.put("withTooltip", new Object[]{tooltip});
        return this;
    }

    public DoubleInput withPlaceholder(String placeholder) {
        config.setPlaceholder(placeholder);
        paramMap.put("withPlaceholder", new Object[]{placeholder});
        return this;
    }

    @Override
    public DoubleInput withRequire() {
        config.setRequire(true);
        paramMap.put("withRequire", new Object[0]);
        return this;
    }

    @Override
    public DoubleInput withLineFeed() {
        config.setBr(true);
        paramMap.put("withLineFeed", new Object[0]);
        return this;
    }

    public DoubleInput withDisabled() {
        config.setDisabled(true);
        paramMap.put("withDisabled", new Object[0]);
        return this;
    }

    public DoubleInput withHidden() {
        config.setHidden(true);
        paramMap.put("withHidden", new Object[0]);
        return this;
    }

    @Override
    public DoubleInput withNotRequireCondition(String notRequireCondition) {
        config.setNotRequireCondition(notRequireCondition);
        paramMap.put("withNotRequireCondition", new Object[]{notRequireCondition});
        return this;
    }

    @Override
    public DoubleInput withShowCondition(String showCondition) {
        config.setShowCondition(showCondition);
        paramMap.put("withShowCondition", new Object[]{showCondition});
        return this;
    }

    @Override
    public DoubleInput withHiddenCondition(String hiddenCondition) {
        config.setHiddenCondition(hiddenCondition);
        paramMap.put("withHiddenCondition", new Object[]{hiddenCondition});
        return this;
    }

    @Override
    public DoubleInput withDisableCondition(String disableCondition) {
        config.setDisableCondition(disableCondition);
        paramMap.put("withDisableCondition", new Object[]{disableCondition});
        return this;
    }

    public DoubleInput withExpScript(String expScript) {
        super.setExpScript(expScript);
        return this;
    }

    public DoubleInput withExpMsg(String expMsg) {
        super.setExpMsg(expMsg);
        return this;
    }

    @Override
    public String getNumberType() {
        return NumberDict.Double.getName();
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
        Map<String, Object> textVal = super.parseFieldValue();
        Map<String, Object> val = new HashMap<>();
        val.put("common", config);
        if (!textVal.isEmpty()) {
            val.put("text", textVal);
        }
        return val;
    }

    @Override
    public DoubleInput copy() {
        DoubleInput result = new DoubleInput();
        LayoutComponentTool.resetParam(result, paramMap, super.getParamMap());
        return result;
    }
}
