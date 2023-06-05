package com.learning.layoutsdk.component.impl;

import com.learning.layoutsdk.component.EnumComponent;
import com.learning.layoutsdk.component.tool.LayoutComponentTool;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SingleSelector
 * @Description TODO
 * @Author hufei
 * @Date 2023/2/17 10:36
 * @Version 1.0
 */
public class SingleSelector extends EnumComponent {

    private Map<String, Object[]> paramMap;
    private LayoutCommonConfig config;

    public SingleSelector() {
        config = LayoutCommonConfig.init();
        paramMap = new HashMap<>();
    }

    @Override
    public SingleSelector withId(String id) {
        config.setId(id);
        if (null == config.getName()) {
            config.setName(id);
        }
        paramMap.put("withId", new Object[]{id});
        return this;
    }

    @Override
    public SingleSelector withName(String name) {
        config.setName(name);
        paramMap.put("withName", new Object[]{name});
        return this;
    }

    @Override
    public SingleSelector withTitleColor(String titleColor) {
        config.setTitleColor(titleColor);
        paramMap.put("withTitleColor", new Object[]{titleColor});
        return this;
    }

    @Override
    public SingleSelector withMulti(int multi) {
        config.setMulti(multi);
        paramMap.put("withMulti", new Object[]{multi});
        return this;
    }

    @Override
    public SingleSelector withHelp(String help) {
        config.setHelp(help);
        paramMap.put("withHelp", new Object[]{help});
        return this;
    }

    @Override
    public SingleSelector withTooltip(String tooltip) {
        config.setTooltip(tooltip);
        paramMap.put("withTooltip", new Object[]{tooltip});
        return this;
    }

    @Override
    public SingleSelector withRequire() {
        config.setRequire(true);
        paramMap.put("withRequire", new Object[0]);
        return this;
    }

    @Override
    public SingleSelector withLineFeed() {
        config.setBr(true);
        paramMap.put("withLineFeed", new Object[0]);
        return this;
    }

    public SingleSelector withDisabled() {
        config.setDisabled(true);
        paramMap.put("withDisabled", new Object[0]);
        return this;
    }

    public SingleSelector withHidden() {
        config.setHidden(true);
        paramMap.put("withHidden", new Object[0]);
        return this;
    }

    @Override
    public SingleSelector withNotRequireCondition(String notRequireCondition) {
        config.setNotRequireCondition(notRequireCondition);
        paramMap.put("withNotRequireCondition", new Object[]{notRequireCondition});
        return this;
    }

    @Override
    public SingleSelector withShowCondition(String showCondition) {
        config.setShowCondition(showCondition);
        paramMap.put("withShowCondition", new Object[]{showCondition});
        return this;
    }

    @Override
    public SingleSelector withHiddenCondition(String hiddenCondition) {
        config.setHiddenCondition(hiddenCondition);
        paramMap.put("withHiddenCondition", new Object[]{hiddenCondition});
        return this;
    }

    @Override
    public SingleSelector withDisableCondition(String disableCondition) {
        config.setDisableCondition(disableCondition);
        paramMap.put("withDisableCondition", new Object[]{disableCondition});
        return this;
    }

    public SingleSelector withEnumItem(String... val) {
        super.appendEnumItem(val);
        return this;
    }

    public SingleSelector withKVEnumItem(String name, Object val) {
        super.appendKVEnumItem(name, val);
        return this;
    }

    public SingleSelector withBatchEnumItem(String val) {
        super.batchAppendEnumItem(val);
        return this;
    }

    public SingleSelector withDefaultEnumItem(String... val) {
        super.appendDefaultEnumItem(val);
        return this;
    }

    public SingleSelector withBatchDefaultEnumItem(String val) {
        super.batchAppendDefaultEnumItem(val);
        return this;
    }

    public SingleSelector withEnumItemOutsideWithGet(String url, String keyName, String valueName) {
        super.appendEnumItemOutsideWithGet(url, keyName, valueName, false, false, false);
        return this;
    }

    public SingleSelector withEnumItemOutsideWithPost(String url, Object body, String keyName, String valueName, String[] watch) {
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
        return false;
    }

    @Override
    public SingleSelector copy() {
        SingleSelector result = new SingleSelector();
        LayoutComponentTool.resetParam(result, paramMap, super.getParamMap());
        return result;
    }
}
