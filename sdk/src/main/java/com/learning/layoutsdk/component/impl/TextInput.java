package com.learning.layoutsdk.component.impl;

import com.learning.layoutsdk.component.TextComponent;
import com.learning.layoutsdk.component.tool.LayoutComponentTool;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @ClassName TextInput
 * @Description TODO
 * @Author hufei
 * @Date 2023/2/17 10:34
 * @Version 1.0
 */
public class TextInput extends TextComponent {

    private Map<String, Object[]> paramMap;
    private LayoutCommonConfig config;
    private String[] units = null;
    private String defaultUnit = null;

    public TextInput() {
        config = LayoutCommonConfig.init();
        paramMap = new HashMap<>();
    }

    @Override
    public TextInput withId(String id) {
        config.setId(id);
        if (null == config.getName()) {
            config.setName(id);
        }
        paramMap.put("withId", new Object[]{id});
        return this;
    }

    @Override
    public TextInput withName(String name) {
        config.setName(name);
        paramMap.put("withName", new Object[]{name});
        return this;
    }

    @Override
    public TextInput withTitleColor(String titleColor) {
        config.setTitleColor(titleColor);
        paramMap.put("withTitleColor", new Object[]{titleColor});
        return this;
    }

    @Override
    public TextInput withMulti(int multi) {
        config.setMulti(multi);
        paramMap.put("withMulti", new Object[]{multi});
        return this;
    }

    public TextInput withDefaultValue(String value) {
        config.setDefaultValue(value);
        paramMap.put("withDefaultValue", new Object[]{value});
        return this;
    }

    @Override
    public TextInput withHelp(String help) {
        config.setHelp(help);
        paramMap.put("withHelp", new Object[]{help});
        return this;
    }

    @Override
    public TextInput withTooltip(String tooltip) {
        config.setTooltip(tooltip);
        paramMap.put("withTooltip", new Object[]{tooltip});
        return this;
    }

    public TextInput withPlaceholder(String placeholder) {
        config.setPlaceholder(placeholder);
        paramMap.put("withPlaceholder", new Object[]{placeholder});
        return this;
    }

    @Override
    public TextInput withRequire() {
        config.setRequire(true);
        paramMap.put("withRequire", new Object[0]);
        return this;
    }

    @Override
    public TextInput withLineFeed() {
        config.setBr(true);
        paramMap.put("withLineFeed", new Object[0]);
        return this;
    }

    public TextInput withDisabled() {
        config.setDisabled(true);
        paramMap.put("withDisabled", new Object[0]);
        return this;
    }

    public TextInput withHidden() {
        config.setHidden(true);
        paramMap.put("withHidden", new Object[0]);
        return this;
    }

    @Override
    public TextInput withNotRequireCondition(String notRequireCondition) {
        config.setNotRequireCondition(notRequireCondition);
        paramMap.put("withNotRequireCondition", new Object[]{notRequireCondition});
        return this;
    }

    @Override
    public TextInput withShowCondition(String showCondition) {
        config.setShowCondition(showCondition);
        paramMap.put("withShowCondition", new Object[]{showCondition});
        return this;
    }

    @Override
    public TextInput withHiddenCondition(String hiddenCondition) {
        config.setHiddenCondition(hiddenCondition);
        paramMap.put("withHiddenCondition", new Object[]{hiddenCondition});
        return this;
    }

    public TextInput withExpScript(String expScript) {
        super.setExpScript(expScript);
        return this;
    }

    public TextInput withExpMsg(String expMsg) {
        super.setExpMsg(expMsg);
        return this;
    }

    public TextInput withUnit(String... units) {
        this.units = units.length == 0 ? null : units;
        paramMap.put("withUnit", new Object[]{units});
        return this;
    }

    public TextInput withDefaultUnit(String unit) {
        boolean found = false;
        if (null != units) {
            for (String item : units) {
                if (item.equals(unit)) {
                    this.defaultUnit = unit;
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            this.defaultUnit = null;
        }
        paramMap.put("withDefaultUnit", new Object[]{unit});
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
        if (null != units) {
            Map<String, Object> textUnit = new HashMap<>();
            textUnit.put("defaultValue", Optional.ofNullable(defaultUnit).orElse(units[0]));
            textUnit.put("value", units);
            textUnit.put("key", "unit");
            textVal.put("addonAfter", textUnit);
        }
        Map<String, Object> val = new HashMap<>();
        val.put("common", config);
        if (!textVal.isEmpty()) {
            val.put("text", textVal);
        }
        return val;
    }

    @Override
    public TextInput copy() {
        TextInput result = new TextInput();
        LayoutComponentTool.resetParam(result, paramMap, super.getParamMap());
        return result;
    }
}
