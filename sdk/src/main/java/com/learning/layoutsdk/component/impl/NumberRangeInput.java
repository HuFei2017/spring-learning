package com.learning.layoutsdk.component.impl;

import com.learning.layoutsdk.component.TextComponent;
import com.learning.layoutsdk.component.tool.LayoutComponentTool;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName TextInput
 * @Description TODO
 * @Author hufei
 * @Date 2023/2/17 10:34
 * @Version 1.0
 */
public class NumberRangeInput extends TextComponent {

    private Map<String, Object[]> paramMap;
    private LayoutCommonConfig config;
    private String[] units = null;
    private String defaultUnit = null;
    private final String defaultMinKey = "min";
    private final String defaultMaxKey = "max";
    private String minKey = defaultMinKey;
    private String maxKey = defaultMaxKey;

    public NumberRangeInput() {
        config = LayoutCommonConfig.init();
        paramMap = new HashMap<>();
    }

    @Override
    public NumberRangeInput withId(String id) {
        config.setId(id);
        if (null == config.getName()) {
            config.setName(id);
        }
        paramMap.put("withId", new Object[]{id});
        return this;
    }

    @Override
    public NumberRangeInput withName(String name) {
        config.setName(name);
        paramMap.put("withName", new Object[]{name});
        return this;
    }

    @Override
    public NumberRangeInput withTitleColor(String titleColor) {
        config.setTitleColor(titleColor);
        paramMap.put("withTitleColor", new Object[]{titleColor});
        return this;
    }

    @Override
    public NumberRangeInput withMulti(int multi) {
        config.setMulti(multi);
        paramMap.put("withMulti", new Object[]{multi});
        return this;
    }

    public NumberRangeInput withDefaultValue(Integer min, Integer max) {
        Map<String, Integer> val = new HashMap<>();
        val.put(minKey, min);
        val.put(maxKey, max);
        config.setDefaultValue(val);
        paramMap.put("withDefaultValue", new Object[]{min, max});
        return this;
    }

    @Override
    public NumberRangeInput withHelp(String help) {
        config.setHelp(help);
        paramMap.put("withHelp", new Object[]{help});
        return this;
    }

    @Override
    public NumberRangeInput withTooltip(String tooltip) {
        config.setTooltip(tooltip);
        paramMap.put("withTooltip", new Object[]{tooltip});
        return this;
    }

    @Override
    public NumberRangeInput withRequire() {
        config.setRequire(true);
        paramMap.put("withRequire", new Object[0]);
        return this;
    }

    @Override
    public NumberRangeInput withLineFeed() {
        config.setBr(true);
        paramMap.put("withLineFeed", new Object[0]);
        return this;
    }

    public NumberRangeInput withDisabled() {
        config.setDisabled(true);
        paramMap.put("withDisabled", new Object[0]);
        return this;
    }

    public NumberRangeInput withHidden() {
        config.setHidden(true);
        paramMap.put("withHidden", new Object[0]);
        return this;
    }

    @Override
    public NumberRangeInput withNotRequireCondition(String notRequireCondition) {
        config.setNotRequireCondition(notRequireCondition);
        paramMap.put("withNotRequireCondition", new Object[]{notRequireCondition});
        return this;
    }

    @Override
    public NumberRangeInput withShowCondition(String showCondition) {
        config.setShowCondition(showCondition);
        paramMap.put("withShowCondition", new Object[]{showCondition});
        return this;
    }

    @Override
    public NumberRangeInput withHiddenCondition(String hiddenCondition) {
        config.setHiddenCondition(hiddenCondition);
        paramMap.put("withHiddenCondition", new Object[]{hiddenCondition});
        return this;
    }

    @Override
    public NumberRangeInput withDisableCondition(String disableCondition) {
        config.setDisableCondition(disableCondition);
        paramMap.put("withDisableCondition", new Object[]{disableCondition});
        return this;
    }

    public NumberRangeInput withUnit(String... units) {
        this.units = units.length == 0 ? null : units;
        paramMap.put("withUnit", new Object[]{units});
        return this;
    }

    public NumberRangeInput withDefaultUnit(String unit) {
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

    public NumberRangeInput withMinKeyName(String name) {
        resetDefaultValueKey(minKey, name);
        this.minKey = name;
        paramMap.put("withMinKeyName", new Object[]{name});
        return this;
    }

    public NumberRangeInput withMaxKeyName(String name) {
        resetDefaultValueKey(maxKey, name);
        this.maxKey = name;
        paramMap.put("withMaxKeyName", new Object[]{name});
        return this;
    }

    @SuppressWarnings(value = "unchecked")
    private void resetDefaultValueKey(String oldKey, String newKey) {
        Object curDefaultValue = config.getDefaultValue();
        if (curDefaultValue instanceof Map) {
            Map curDefaultVal = (Map) curDefaultValue;
            Object keyVal = curDefaultVal.get(oldKey);
            curDefaultVal.remove(oldKey);
            curDefaultVal.put(newKey, keyVal);
            config.setDefaultValue(curDefaultVal);
        }
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
        Map<String, Object> numberRangeVal = new HashMap<>();
        if (!minKey.equals(defaultMinKey)) {
            numberRangeVal.put("startKey", minKey);
        }
        if (!maxKey.equals(defaultMaxKey)) {
            numberRangeVal.put("endKey", maxKey);
        }
        if (null != units) {
            numberRangeVal.put("addonAfter", super.parseFieldUnitValue(units, defaultUnit));
        }
        Map<String, Object> textVal = super.parseFieldValue();
        textVal.put("type", "numberRange");
        if (!numberRangeVal.isEmpty()) {
            textVal.put("numberRange", numberRangeVal);
        }
        Map<String, Object> val = new HashMap<>();
        val.put("common", config);
        val.put("text", textVal);
        return val;
    }

    @Override
    public NumberRangeInput copy() {
        NumberRangeInput result = new NumberRangeInput();
        LayoutComponentTool.resetParam(result, paramMap, super.getParamMap());
        return result;
    }
}
