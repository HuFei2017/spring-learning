package com.learning.layoutsdk.component.impl;

import com.learning.layoutsdk.component.EnumComponent;
import com.learning.layoutsdk.component.tool.LayoutComponentTool;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName MultiCheckbox
 * @Description TODO
 * @Author hufei
 * @Date 2023/2/17 10:37
 * @Version 1.0
 */
public class MultiCheckbox extends EnumComponent {

    private Map<String, Object[]> paramMap;
    private LayoutCommonConfig config;
    private int colSpan = -1;

    public MultiCheckbox() {
        config = LayoutCommonConfig.init();
        paramMap = new HashMap<>();
    }

    @Override
    public MultiCheckbox withId(String id) {
        config.setId(id);
        if (null == config.getName()) {
            config.setName(id);
        }
        paramMap.put("withId", new Object[]{id});
        return this;
    }

    @Override
    public MultiCheckbox withName(String name) {
        config.setName(name);
        paramMap.put("withName", new Object[]{name});
        return this;
    }

    @Override
    public MultiCheckbox withTitleColor(String titleColor) {
        config.setTitleColor(titleColor);
        paramMap.put("withTitleColor", new Object[]{titleColor});
        return this;
    }

    @Override
    public MultiCheckbox withMulti(int multi) {
        config.setMulti(multi);
        paramMap.put("withMulti", new Object[]{multi});
        return this;
    }

    @Override
    public MultiCheckbox withHelp(String help) {
        config.setHelp(help);
        paramMap.put("withHelp", new Object[]{help});
        return this;
    }

    @Override
    public MultiCheckbox withTooltip(String tooltip) {
        config.setTooltip(tooltip);
        paramMap.put("withTooltip", new Object[]{tooltip});
        return this;
    }

    @Override
    public MultiCheckbox withRequire() {
        config.setRequire(true);
        paramMap.put("withRequire", new Object[0]);
        return this;
    }

    @Override
    public MultiCheckbox withLineFeed() {
        config.setBr(true);
        paramMap.put("withLineFeed", new Object[0]);
        return this;
    }

    public MultiCheckbox withDisabled() {
        config.setDisabled(true);
        paramMap.put("withDisabled", new Object[0]);
        return this;
    }

    public MultiCheckbox withHidden() {
        config.setHidden(true);
        paramMap.put("withHidden", new Object[0]);
        return this;
    }

    @Override
    public MultiCheckbox withNotRequireCondition(String notRequireCondition) {
        config.setNotRequireCondition(notRequireCondition);
        paramMap.put("withNotRequireCondition", new Object[]{notRequireCondition});
        return this;
    }

    @Override
    public MultiCheckbox withShowCondition(String showCondition) {
        config.setShowCondition(showCondition);
        paramMap.put("withShowCondition", new Object[]{showCondition});
        return this;
    }

    @Override
    public MultiCheckbox withHiddenCondition(String hiddenCondition) {
        config.setHiddenCondition(hiddenCondition);
        paramMap.put("withHiddenCondition", new Object[]{hiddenCondition});
        return this;
    }

    @Override
    public MultiCheckbox withDisableCondition(String disableCondition) {
        config.setDisableCondition(disableCondition);
        paramMap.put("withDisableCondition", new Object[]{disableCondition});
        return this;
    }

    public MultiCheckbox withMaxSelectCount(int max) {
        super.setMaxSelectCount(max);
        return this;
    }

    public MultiCheckbox withMaxTagCount(int max) {
        super.setMaxTagCount(max);
        return this;
    }

    public MultiCheckbox withMaxTagTextLength(int max) {
        super.setMaxTagTextLength(max);
        return this;
    }

    public MultiCheckbox withEnumItem(String... val) {
        super.appendEnumItem(val);
        return this;
    }

    public MultiCheckbox withKVEnumItem(String name, Object val) {
        super.appendKVEnumItem(name, val);
        return this;
    }

    public MultiCheckbox withBatchEnumItem(String val) {
        super.batchAppendEnumItem(val);
        return this;
    }

    public MultiCheckbox withDefaultEnumItem(String... val) {
        super.appendDefaultEnumItem(val);
        return this;
    }

    public MultiCheckbox withBatchDefaultEnumItem(String val) {
        super.batchAppendDefaultEnumItem(val);
        return this;
    }

    public MultiCheckbox withEnumItemOutsideWithGet(String url, String keyName, String valueName) {
        super.appendEnumItemOutsideWithGet(url, keyName, valueName, false, false, false);
        return this;
    }

    public MultiCheckbox withEnumItemOutsideWithPost(String url, Object body, String keyName, String valueName, String[] watch) {
        super.appendEnumItemOutsideWithPost(url, body, keyName, valueName, watch, false, false, false);
        return this;
    }

    public MultiCheckbox withColSpan(int colSpan) {
        this.colSpan = colSpan;
        paramMap.put("withColSpan", new Object[]{colSpan});
        return this;
    }

    @Override
    public String getId() {
        return config.getId();
    }

    @Override
    public Map toConfigSchema() {
        Map<String, Object> spanConfig = new HashMap<>();
        if (colSpan > 0) {
            spanConfig.put("colSpan", colSpan);
        }
        Map<String, Object> radioConfig = new HashMap<>();
        if (!spanConfig.isEmpty()) {
            radioConfig.put("checkbox", spanConfig);
        }
        return radioConfig;
    }

    @Override
    public LayoutCommonConfig getCommonConfig() {
        return config;
    }

    @Override
    public String getName() {
        return config.getName();
    }

    @Override
    public boolean isMultiple() {
        return true;
    }

    @Override
    public boolean isCheckbox() {
        return true;
    }

    @Override
    public MultiCheckbox copy() {
        MultiCheckbox result = new MultiCheckbox();
        LayoutComponentTool.resetParam(result, paramMap, super.getParamMap());
        return result;
    }
}
