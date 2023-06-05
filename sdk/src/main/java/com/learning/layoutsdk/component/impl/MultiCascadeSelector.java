package com.learning.layoutsdk.component.impl;

import com.learning.layoutsdk.component.EnumComponent;
import com.learning.layoutsdk.component.tool.LayoutComponentTool;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName MultiCascadeSelector
 * @Description TODO
 * @Author hufei
 * @Date 2023/2/17 10:37
 * @Version 1.0
 */
public class MultiCascadeSelector extends EnumComponent {

    private Map<String, Object[]> paramMap;
    private LayoutCommonConfig config;

    public MultiCascadeSelector() {
        config = LayoutCommonConfig.init();
        paramMap = new HashMap<>();
    }

    @Override
    public MultiCascadeSelector withId(String id) {
        config.setId(id);
        if (null == config.getName()) {
            config.setName(id);
        }
        paramMap.put("withId", new Object[]{id});
        return this;
    }

    @Override
    public MultiCascadeSelector withName(String name) {
        config.setName(name);
        paramMap.put("withName", new Object[]{name});
        return this;
    }

    @Override
    public MultiCascadeSelector withTitleColor(String titleColor) {
        config.setTitleColor(titleColor);
        paramMap.put("withTitleColor", new Object[]{titleColor});
        return this;
    }

    @Override
    public MultiCascadeSelector withMulti(int multi) {
        config.setMulti(multi);
        paramMap.put("withMulti", new Object[]{multi});
        return this;
    }

    @Override
    public MultiCascadeSelector withHelp(String help) {
        config.setHelp(help);
        paramMap.put("withHelp", new Object[]{help});
        return this;
    }

    @Override
    public MultiCascadeSelector withTooltip(String tooltip) {
        config.setTooltip(tooltip);
        paramMap.put("withTooltip", new Object[]{tooltip});
        return this;
    }

    @Override
    public MultiCascadeSelector withRequire() {
        config.setRequire(true);
        paramMap.put("withRequire", new Object[0]);
        return this;
    }

    @Override
    public MultiCascadeSelector withLineFeed() {
        config.setBr(true);
        paramMap.put("withLineFeed", new Object[0]);
        return this;
    }

    public MultiCascadeSelector withDisabled() {
        config.setDisabled(true);
        paramMap.put("withDisabled", new Object[0]);
        return this;
    }

    public MultiCascadeSelector withHidden() {
        config.setHidden(true);
        paramMap.put("withHidden", new Object[0]);
        return this;
    }

    @Override
    public MultiCascadeSelector withNotRequireCondition(String notRequireCondition) {
        config.setNotRequireCondition(notRequireCondition);
        paramMap.put("withNotRequireCondition", new Object[]{notRequireCondition});
        return this;
    }

    @Override
    public MultiCascadeSelector withShowCondition(String showCondition) {
        config.setShowCondition(showCondition);
        paramMap.put("withShowCondition", new Object[]{showCondition});
        return this;
    }

    @Override
    public MultiCascadeSelector withHiddenCondition(String hiddenCondition) {
        config.setHiddenCondition(hiddenCondition);
        paramMap.put("withHiddenCondition", new Object[]{hiddenCondition});
        return this;
    }

    @Override
    public MultiCascadeSelector withDisableCondition(String disableCondition) {
        config.setDisableCondition(disableCondition);
        paramMap.put("withDisableCondition", new Object[]{disableCondition});
        return this;
    }

    public MultiCascadeSelector withMaxSelectCount(int max) {
        super.setMaxSelectCount(max);
        return this;
    }

    public MultiCascadeSelector withMaxTagCount(int max) {
        super.setMaxTagCount(max);
        return this;
    }

    public MultiCascadeSelector withMaxTagTextLength(int max) {
        super.setMaxTagTextLength(max);
        return this;
    }

    public MultiCascadeSelector withEnumItemOutsideWithGet(String url, String keyName, String valueName) {
        super.appendEnumItemOutsideWithGet(url, keyName, valueName, true, false, false);
        return this;
    }

    public MultiCascadeSelector withEnumItemOutsideWithPost(String url, Object body, String keyName, String valueName, String[] watch) {
        super.appendEnumItemOutsideWithPost(url, body, keyName, valueName, watch, true, false, false);
        return this;
    }

    @Override
    public LayoutCommonConfig getCommonConfig() {
        return config;
    }

    @Override
    public boolean isMultiple() {
        return true;
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
        return null;
    }

    @Override
    public MultiCascadeSelector copy() {
        MultiCascadeSelector result = new MultiCascadeSelector();
        LayoutComponentTool.resetParam(result, paramMap, super.getParamMap());
        return result;
    }
}
