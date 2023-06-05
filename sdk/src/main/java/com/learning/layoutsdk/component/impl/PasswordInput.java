package com.learning.layoutsdk.component.impl;

import com.learning.layoutsdk.component.TextComponent;
import com.learning.layoutsdk.component.tool.LayoutComponentTool;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName PasswordInput
 * @Description TODO
 * @Author hufei
 * @Date 2023/2/17 10:35
 * @Version 1.0
 */
public class PasswordInput extends TextComponent {

    private Map<String, Object[]> paramMap;
    private LayoutCommonConfig config;

    public PasswordInput() {
        config = LayoutCommonConfig.init();
        paramMap = new HashMap<>();
    }

    @Override
    public PasswordInput withId(String id) {
        config.setId(id);
        if (null == config.getName()) {
            config.setName(id);
        }
        paramMap.put("withId", new Object[]{id});
        return this;
    }

    @Override
    public PasswordInput withName(String name) {
        config.setName(name);
        paramMap.put("withName", new Object[]{name});
        return this;
    }

    @Override
    public PasswordInput withTitleColor(String titleColor) {
        config.setTitleColor(titleColor);
        paramMap.put("withTitleColor", new Object[]{titleColor});
        return this;
    }

    @Override
    public PasswordInput withMulti(int multi) {
        config.setMulti(multi);
        paramMap.put("withMulti", new Object[]{multi});
        return this;
    }

    @Override
    public PasswordInput withHelp(String help) {
        config.setHelp(help);
        paramMap.put("withHelp", new Object[]{help});
        return this;
    }

    @Override
    public PasswordInput withTooltip(String tooltip) {
        config.setTooltip(tooltip);
        paramMap.put("withTooltip", new Object[]{tooltip});
        return this;
    }

    public PasswordInput withPlaceholder(String placeholder) {
        config.setPlaceholder(placeholder);
        paramMap.put("withPlaceholder", new Object[]{placeholder});
        return this;
    }

    @Override
    public PasswordInput withRequire() {
        config.setRequire(true);
        paramMap.put("withRequire", new Object[0]);
        return this;
    }

    @Override
    public PasswordInput withLineFeed() {
        config.setBr(true);
        paramMap.put("withLineFeed", new Object[0]);
        return this;
    }

    public PasswordInput withDisabled() {
        config.setDisabled(true);
        paramMap.put("withDisabled", new Object[0]);
        return this;
    }

    public PasswordInput withHidden() {
        config.setHidden(true);
        paramMap.put("withHidden", new Object[0]);
        return this;
    }

    @Override
    public PasswordInput withNotRequireCondition(String notRequireCondition) {
        config.setNotRequireCondition(notRequireCondition);
        paramMap.put("withNotRequireCondition", new Object[]{notRequireCondition});
        return this;
    }

    @Override
    public PasswordInput withShowCondition(String showCondition) {
        config.setShowCondition(showCondition);
        paramMap.put("withShowCondition", new Object[]{showCondition});
        return this;
    }

    @Override
    public PasswordInput withHiddenCondition(String hiddenCondition) {
        config.setHiddenCondition(hiddenCondition);
        paramMap.put("withHiddenCondition", new Object[]{hiddenCondition});
        return this;
    }

    @Override
    public PasswordInput withDisableCondition(String disableCondition) {
        config.setDisableCondition(disableCondition);
        paramMap.put("withDisableCondition", new Object[]{disableCondition});
        return this;
    }

    public PasswordInput withExpScript(String expScript) {
        super.setExpScript(expScript);
        return this;
    }

    public PasswordInput withExpMsg(String expMsg) {
        super.setExpMsg(expMsg);
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
        textVal.put("type", "password");
        Map<String, Object> val = new HashMap<>();
        val.put("common", config);
        val.put("text", textVal);
        return val;
    }

    @Override
    public PasswordInput copy() {
        PasswordInput result = new PasswordInput();
        LayoutComponentTool.resetParam(result, paramMap, super.getParamMap());
        return result;
    }
}
