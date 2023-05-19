package com.learning.layoutsdk.component.impl;

import com.learning.layoutsdk.component.TextComponent;
import com.learning.layoutsdk.component.tool.LayoutComponentTool;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName DateInput
 * @Description TODO
 * @Author hufei
 * @Date 2023/2/17 10:35
 * @Version 1.0
 */
public class DateInput extends TextComponent {

    private Map<String, Object[]> paramMap;
    private LayoutCommonConfig config;
    private String format = "YYYY-MM-DD HH:mm:ss";

    public DateInput() {
        config = LayoutCommonConfig.init();
        paramMap = new HashMap<>();
    }

    @Override
    public DateInput withId(String id) {
        config.setId(id);
        if (null == config.getName()) {
            config.setName(id);
        }
        paramMap.put("withId", new Object[]{id});
        return this;
    }

    @Override
    public DateInput withName(String name) {
        config.setName(name);
        paramMap.put("withName", new Object[]{name});
        return this;
    }

    @Override
    public DateInput withTitleColor(String titleColor) {
        config.setTitleColor(titleColor);
        paramMap.put("withTitleColor", new Object[]{titleColor});
        return this;
    }

    @Override
    public DateInput withMulti(int multi) {
        config.setMulti(multi);
        paramMap.put("withMulti", new Object[]{multi});
        return this;
    }

    public DateInput withDefaultValue(String value) {
        config.setDefaultValue(value);
        paramMap.put("withDefaultValue", new Object[]{value});
        return this;
    }

    @Override
    public DateInput withHelp(String help) {
        config.setHelp(help);
        paramMap.put("withHelp", new Object[]{help});
        return this;
    }

    @Override
    public DateInput withTooltip(String tooltip) {
        config.setTooltip(tooltip);
        paramMap.put("withTooltip", new Object[]{tooltip});
        return this;
    }

    public DateInput withPlaceholder(String placeholder) {
        config.setPlaceholder(placeholder);
        paramMap.put("withPlaceholder", new Object[]{placeholder});
        return this;
    }

    @Override
    public DateInput withRequire() {
        config.setRequire(true);
        paramMap.put("withRequire", new Object[0]);
        return this;
    }

    @Override
    public DateInput withLineFeed() {
        config.setBr(true);
        paramMap.put("withLineFeed", new Object[0]);
        return this;
    }

    public DateInput withDisabled() {
        config.setDisabled(true);
        paramMap.put("withDisabled", new Object[0]);
        return this;
    }

    public DateInput withHidden() {
        config.setHidden(true);
        paramMap.put("withHidden", new Object[0]);
        return this;
    }

    @Override
    public DateInput withNotRequireCondition(String notRequireCondition) {
        config.setNotRequireCondition(notRequireCondition);
        paramMap.put("withNotRequireCondition", new Object[]{notRequireCondition});
        return this;
    }

    @Override
    public DateInput withShowCondition(String showCondition) {
        config.setShowCondition(showCondition);
        paramMap.put("withShowCondition", new Object[]{showCondition});
        return this;
    }

    @Override
    public DateInput withHiddenCondition(String hiddenCondition) {
        config.setHiddenCondition(hiddenCondition);
        paramMap.put("withHiddenCondition", new Object[]{hiddenCondition});
        return this;
    }

    public DateInput withExpScript(String expScript) {
        super.setExpScript(expScript);
        return this;
    }

    public DateInput withExpMsg(String expMsg) {
        super.setExpMsg(expMsg);
        return this;
    }

    public DateInput withFormatter(String format) {
        this.format = format;
        paramMap.put("withFormatter", new Object[]{format});
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
        Map<String, Object> textVal = super.parseFieldValue();
        textVal.put("type", "datePicker");
        if (null != format) {
            textVal.put("format", format.replace("yyyy", "YYYY").replace("dd", "DD"));
        }
        Map<String, Object> val = new HashMap<>();
        val.put("common", config);
        val.put("text", textVal);
        return val;
    }

    @Override
    public DateInput copy() {
        DateInput result = new DateInput();
        LayoutComponentTool.resetParam(result, paramMap, super.getParamMap());
        return result;
    }
}
