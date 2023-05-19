package com.learning.layoutsdk.component.impl;

import com.learning.layoutsdk.component.EnumComponent;
import com.learning.layoutsdk.component.tool.LayoutComponentTool;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SingleRatio
 * @Description TODO
 * @Author hufei
 * @Date 2023/2/17 10:37
 * @Version 1.0
 */
public class SingleRadio extends EnumComponent {

    private Map<String, Object[]> paramMap;
    private LayoutCommonConfig config;
    private int colSpan = -1;

    public SingleRadio() {
        config = LayoutCommonConfig.init();
        paramMap = new HashMap<>();
    }

    @Override
    public SingleRadio withId(String id) {
        config.setId(id);
        if (null == config.getName()) {
            config.setName(id);
        }
        paramMap.put("withId", new Object[]{id});
        return this;
    }

    @Override
    public SingleRadio withName(String name) {
        config.setName(name);
        paramMap.put("withName", new Object[]{name});
        return this;
    }

    @Override
    public SingleRadio withTitleColor(String titleColor) {
        config.setTitleColor(titleColor);
        paramMap.put("withTitleColor", new Object[]{titleColor});
        return this;
    }

    @Override
    public SingleRadio withMulti(int multi) {
        config.setMulti(multi);
        paramMap.put("withMulti", new Object[]{multi});
        return this;
    }

    @Override
    public SingleRadio withHelp(String help) {
        config.setHelp(help);
        paramMap.put("withHelp", new Object[]{help});
        return this;
    }

    @Override
    public SingleRadio withTooltip(String tooltip) {
        config.setTooltip(tooltip);
        paramMap.put("withTooltip", new Object[]{tooltip});
        return this;
    }

    @Override
    public SingleRadio withRequire() {
        config.setRequire(true);
        paramMap.put("withRequire", new Object[0]);
        return this;
    }

    @Override
    public SingleRadio withLineFeed() {
        config.setBr(true);
        paramMap.put("withLineFeed", new Object[0]);
        return this;
    }

    public SingleRadio withDisabled() {
        config.setDisabled(true);
        paramMap.put("withDisabled", new Object[0]);
        return this;
    }

    public SingleRadio withHidden() {
        config.setHidden(true);
        paramMap.put("withHidden", new Object[0]);
        return this;
    }

    @Override
    public SingleRadio withNotRequireCondition(String notRequireCondition) {
        config.setNotRequireCondition(notRequireCondition);
        paramMap.put("withNotRequireCondition", new Object[]{notRequireCondition});
        return this;
    }

    @Override
    public SingleRadio withShowCondition(String showCondition) {
        config.setShowCondition(showCondition);
        paramMap.put("withShowCondition", new Object[]{showCondition});
        return this;
    }

    @Override
    public SingleRadio withHiddenCondition(String hiddenCondition) {
        config.setHiddenCondition(hiddenCondition);
        paramMap.put("withHiddenCondition", new Object[]{hiddenCondition});
        return this;
    }

    public SingleRadio withEnumItem(String... val) {
        super.appendEnumItem(val);
        return this;
    }

    public SingleRadio withKVEnumItem(String name, Object val) {
        super.appendKVEnumItem(name, val);
        return this;
    }

    public SingleRadio withBatchEnumItem(String val) {
        super.batchAppendEnumItem(val);
        return this;
    }

    /**
     * 接口要求返回类型为 {@link com.ronds.phm.business.deviceModel.dto.ComponentTree}
     */
    public SingleRadio withEnumItemOutsideWithGet(String url, String keyName, String valueName) {
        super.appendEnumItemOutsideWithGet(url, keyName, valueName, false, false, false);
        return this;
    }

    /**
     * 接口要求返回类型为 {@link com.ronds.phm.business.deviceModel.dto.ComponentTree}
     */
    public SingleRadio withEnumItemOutsideWithPost(String url, Object body, String keyName, String valueName, String[] watch) {
        super.appendEnumItemOutsideWithPost(url, body, keyName, valueName, watch, false, false, false);
        return this;
    }

    public SingleRadio withColSpan(int colSpan) {
        this.colSpan = colSpan;
        paramMap.put("withColSpan", new Object[]{colSpan});
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
    public boolean isMultiple() {
        return false;
    }

    @Override
    public boolean isCheckbox() {
        return true;
    }

    @Override
    public SingleRadio copy() {
        SingleRadio result = new SingleRadio();
        LayoutComponentTool.resetParam(result, paramMap, super.getParamMap());
        return result;
    }
}
