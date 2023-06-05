package com.learning.layoutsdk.component.impl;

import com.learning.layoutsdk.component.EnumComponent;
import com.learning.layoutsdk.component.tool.LayoutComponentTool;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName MultiSelector
 * @Description TODO
 * @Author hufei
 * @Date 2023/2/17 10:36
 * @Version 1.0
 */
public class MultiSelector extends EnumComponent {

    private Map<String, Object[]> paramMap;
    private LayoutCommonConfig config;

    public MultiSelector() {
        config = LayoutCommonConfig.init();
        paramMap = new HashMap<>();
    }

    @Override
    public MultiSelector withId(String id) {
        config.setId(id);
        if (null == config.getName()) {
            config.setName(id);
        }
        paramMap.put("withId", new Object[]{id});
        return this;
    }

    @Override
    public MultiSelector withName(String name) {
        config.setName(name);
        paramMap.put("withName", new Object[]{name});
        return this;
    }

    @Override
    public MultiSelector withTitleColor(String titleColor) {
        config.setTitleColor(titleColor);
        paramMap.put("withTitleColor", new Object[]{titleColor});
        return this;
    }

    @Override
    public MultiSelector withMulti(int multi) {
        config.setMulti(multi);
        paramMap.put("withMulti", new Object[]{multi});
        return this;
    }

    @Override
    public MultiSelector withHelp(String help) {
        config.setHelp(help);
        paramMap.put("withHelp", new Object[]{help});
        return this;
    }

    @Override
    public MultiSelector withTooltip(String tooltip) {
        config.setTooltip(tooltip);
        paramMap.put("withTooltip", new Object[]{tooltip});
        return this;
    }

    @Override
    public MultiSelector withRequire() {
        config.setRequire(true);
        paramMap.put("withRequire", new Object[0]);
        return this;
    }

    @Override
    public MultiSelector withLineFeed() {
        config.setBr(true);
        paramMap.put("withLineFeed", new Object[0]);
        return this;
    }

    public MultiSelector withDisabled() {
        config.setDisabled(true);
        paramMap.put("withDisabled", new Object[0]);
        return this;
    }

    public MultiSelector withHidden() {
        config.setHidden(true);
        paramMap.put("withHidden", new Object[0]);
        return this;
    }

    @Override
    public MultiSelector withNotRequireCondition(String notRequireCondition) {
        config.setNotRequireCondition(notRequireCondition);
        paramMap.put("withNotRequireCondition", new Object[]{notRequireCondition});
        return this;
    }

    @Override
    public MultiSelector withShowCondition(String showCondition) {
        config.setShowCondition(showCondition);
        paramMap.put("withShowCondition", new Object[]{showCondition});
        return this;
    }

    @Override
    public MultiSelector withHiddenCondition(String hiddenCondition) {
        config.setHiddenCondition(hiddenCondition);
        paramMap.put("withHiddenCondition", new Object[]{hiddenCondition});
        return this;
    }

    @Override
    public MultiSelector withDisableCondition(String disableCondition) {
        config.setDisableCondition(disableCondition);
        paramMap.put("withDisableCondition", new Object[]{disableCondition});
        return this;
    }

    public MultiSelector withMaxSelectCount(int max) {
        super.setMaxSelectCount(max);
        return this;
    }

    public MultiSelector withMaxTagCount(int max) {
        super.setMaxTagCount(max);
        return this;
    }

    public MultiSelector withMaxTagTextLength(int max) {
        super.setMaxTagTextLength(max);
        return this;
    }

    public MultiSelector withEnumItem(String... val) {
        super.appendEnumItem(val);
        return this;
    }

    public MultiSelector withKVEnumItem(String name, Object val) {
        super.appendKVEnumItem(name, val);
        return this;
    }

    public MultiSelector withBatchEnumItem(String val) {
        super.batchAppendEnumItem(val);
        return this;
    }

    public MultiSelector withDefaultEnumItem(String... val) {
        super.appendDefaultEnumItem(val);
        return this;
    }

    public MultiSelector withBatchDefaultEnumItem(String val) {
        super.batchAppendDefaultEnumItem(val);
        return this;
    }

    public MultiSelector withEnumItemOutsideWithGet(String url, String keyName, String valueName) {
        super.appendEnumItemOutsideWithGet(url, keyName, valueName, false, false, false);
        return this;
    }

    public MultiSelector withEnumItemOutsideWithPost(String url, Object body, String keyName, String valueName, String[] watch) {
        super.appendEnumItemOutsideWithPost(url, body, keyName, valueName, watch, false, false, false);
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
        return null;
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
    public MultiSelector copy() {
        MultiSelector result = new MultiSelector();
        LayoutComponentTool.resetParam(result, paramMap, super.getParamMap());
        return result;
    }
}
