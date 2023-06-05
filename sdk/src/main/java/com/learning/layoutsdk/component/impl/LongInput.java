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
public class LongInput extends NumberComponent {

    private Map<String, Object[]> paramMap;
    private LayoutCommonConfig config;

    public LongInput() {
        config = LayoutCommonConfig.init();
        paramMap = new HashMap<>();
    }

    @Override
    public LongInput withId(String id) {
        config.setId(id);
        if (null == config.getName()) {
            config.setName(id);
        }
        paramMap.put("withId", new Object[]{id});
        return this;
    }

    @Override
    public LongInput withName(String name) {
        config.setName(name);
        paramMap.put("withName", new Object[]{name});
        return this;
    }

    @Override
    public LongInput withTitleColor(String titleColor) {
        config.setTitleColor(titleColor);
        paramMap.put("withTitleColor", new Object[]{titleColor});
        return this;
    }

    @Override
    public LongInput withMulti(int multi) {
        config.setMulti(multi);
        paramMap.put("withMulti", new Object[]{multi});
        return this;
    }

    public LongInput withDefaultValue(long value) {
        config.setDefaultValue(value);
        paramMap.put("withDefaultValue", new Object[]{value});
        return this;
    }

    @Override
    public LongInput withHelp(String help) {
        config.setHelp(help);
        paramMap.put("withHelp", new Object[]{help});
        return this;
    }

    @Override
    public LongInput withTooltip(String tooltip) {
        config.setTooltip(tooltip);
        paramMap.put("withTooltip", new Object[]{tooltip});
        return this;
    }

    public LongInput withPlaceholder(String placeholder) {
        config.setPlaceholder(placeholder);
        paramMap.put("withPlaceholder", new Object[]{placeholder});
        return this;
    }

    @Override
    public LongInput withRequire() {
        config.setRequire(true);
        paramMap.put("withRequire", new Object[0]);
        return this;
    }

    @Override
    public LongInput withLineFeed() {
        config.setBr(true);
        paramMap.put("withLineFeed", new Object[0]);
        return this;
    }

    public LongInput withDisabled() {
        config.setDisabled(true);
        paramMap.put("withDisabled", new Object[0]);
        return this;
    }

    public LongInput withHidden() {
        config.setHidden(true);
        paramMap.put("withHidden", new Object[0]);
        return this;
    }

    @Override
    public LongInput withNotRequireCondition(String notRequireCondition) {
        config.setNotRequireCondition(notRequireCondition);
        paramMap.put("withNotRequireCondition", new Object[]{notRequireCondition});
        return this;
    }

    @Override
    public LongInput withShowCondition(String showCondition) {
        config.setShowCondition(showCondition);
        paramMap.put("withShowCondition", new Object[]{showCondition});
        return this;
    }

    @Override
    public LongInput withHiddenCondition(String hiddenCondition) {
        config.setHiddenCondition(hiddenCondition);
        paramMap.put("withHiddenCondition", new Object[]{hiddenCondition});
        return this;
    }

    @Override
    public LongInput withDisableCondition(String disableCondition) {
        config.setDisableCondition(disableCondition);
        paramMap.put("withDisableCondition", new Object[]{disableCondition});
        return this;
    }

    public LongInput withExpScript(String expScript) {
        super.setExpScript(expScript);
        return this;
    }

    public LongInput withExpMsg(String expMsg) {
        super.setExpMsg(expMsg);
        return this;
    }

    @Override
    public String getNumberType() {
        return NumberDict.Integer.getName();
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
    public LongInput copy() {
        LongInput result = new LongInput();
        LayoutComponentTool.resetParam(result, paramMap, super.getParamMap());
        return result;
    }
}
