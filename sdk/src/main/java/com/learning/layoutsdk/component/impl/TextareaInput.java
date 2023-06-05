package com.learning.layoutsdk.component.impl;

import com.learning.layoutsdk.component.TextComponent;
import com.learning.layoutsdk.component.tool.LayoutComponentTool;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName TextareaInput
 * @Description TODO
 * @Author hufei
 * @Date 2023/2/17 10:34
 * @Version 1.0
 */
public class TextareaInput extends TextComponent {

    private Map<String, Object[]> paramMap;
    private LayoutCommonConfig config;
    private int height = -1;

    public TextareaInput() {
        config = LayoutCommonConfig.init();
        paramMap = new HashMap<>();
    }

    @Override
    public TextareaInput withId(String id) {
        config.setId(id);
        if (null == config.getName()) {
            config.setName(id);
        }
        paramMap.put("withId", new Object[]{id});
        return this;
    }

    @Override
    public TextareaInput withName(String name) {
        config.setName(name);
        paramMap.put("withName", new Object[]{name});
        return this;
    }

    @Override
    public TextareaInput withTitleColor(String titleColor) {
        config.setTitleColor(titleColor);
        paramMap.put("withTitleColor", new Object[]{titleColor});
        return this;
    }

    @Override
    public TextareaInput withMulti(int multi) {
        config.setMulti(multi);
        paramMap.put("withMulti", new Object[]{multi});
        return this;
    }

    public TextareaInput withDefaultValue(String value) {
        config.setDefaultValue(value);
        paramMap.put("withDefaultValue", new Object[]{value});
        return this;
    }

    @Override
    public TextareaInput withHelp(String help) {
        config.setHelp(help);
        paramMap.put("withHelp", new Object[]{help});
        return this;
    }

    @Override
    public TextareaInput withTooltip(String tooltip) {
        config.setTooltip(tooltip);
        paramMap.put("withTooltip", new Object[]{tooltip});
        return this;
    }

    public TextareaInput withPlaceholder(String placeholder) {
        config.setPlaceholder(placeholder);
        paramMap.put("withPlaceholder", new Object[]{placeholder});
        return this;
    }

    @Override
    public TextareaInput withRequire() {
        config.setRequire(true);
        paramMap.put("withRequire", new Object[0]);
        return this;
    }

    @Override
    public TextareaInput withLineFeed() {
        config.setBr(true);
        paramMap.put("withLineFeed", new Object[0]);
        return this;
    }

    public TextareaInput withDisabled() {
        config.setDisabled(true);
        paramMap.put("withDisabled", new Object[0]);
        return this;
    }

    public TextareaInput withHidden() {
        config.setHidden(true);
        paramMap.put("withHidden", new Object[0]);
        return this;
    }

    @Override
    public TextareaInput withNotRequireCondition(String notRequireCondition) {
        config.setNotRequireCondition(notRequireCondition);
        paramMap.put("withNotRequireCondition", new Object[]{notRequireCondition});
        return this;
    }

    @Override
    public TextareaInput withShowCondition(String showCondition) {
        config.setShowCondition(showCondition);
        paramMap.put("withShowCondition", new Object[]{showCondition});
        return this;
    }

    @Override
    public TextareaInput withHiddenCondition(String hiddenCondition) {
        config.setHiddenCondition(hiddenCondition);
        paramMap.put("withHiddenCondition", new Object[]{hiddenCondition});
        return this;
    }

    @Override
    public TextareaInput withDisableCondition(String disableCondition) {
        config.setDisableCondition(disableCondition);
        paramMap.put("withDisableCondition", new Object[]{disableCondition});
        return this;
    }

    public TextareaInput withHeight(int height) {
        Assert.isTrue(height > 0, "please set valid height value");
        this.height = height;
        paramMap.put("withHeight", new Object[]{height});
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
        Map<String, Object> textVal = new HashMap<>();
        textVal.put("type", "textarea");
        if (height > 0) {
            textVal.put("textareaRows", height);
        }
        Map<String, Object> val = new HashMap<>();
        val.put("common", config);
        val.put("text", textVal);
        return val;
    }

    @Override
    public TextareaInput copy() {
        TextareaInput result = new TextareaInput();
        LayoutComponentTool.resetParam(result, paramMap, new HashMap<>());
        return result;
    }
}
