package com.learning.layoutsdk.component.impl;

import com.learning.layoutsdk.component.ArrayComponent;
import com.learning.layoutsdk.component.LayoutComponent;
import com.learning.layoutsdk.component.definition.JsonProviderMetaType;
import com.learning.layoutsdk.component.tool.LayoutComponentTool;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName TableInput
 * @Description TODO
 * @Author hufei
 * @Date 2023/2/17 10:38
 * @Version 1.0
 */
public class TableInput extends ArrayComponent {

    private Map<String, Object[]> paramMap;
    private LayoutCommonConfig config;
    private boolean hiddenTitle = false;
    private boolean hiddenAddButton = false;
    private int lineNum = -1;

    public TableInput() {
        config = LayoutCommonConfig.init();
        paramMap = new HashMap<>();
    }

    @Override
    public TableInput withId(String id) {
        config.setId(id);
        if (null == config.getName()) {
            config.setName(id);
        }
        paramMap.put("withId", new Object[]{id});
        return this;
    }

    @Override
    public TableInput withName(String name) {
        config.setName(name);
        paramMap.put("withName", new Object[]{name});
        return this;
    }

    @Override
    public TableInput withTitleColor(String titleColor) {
        config.setTitleColor(titleColor);
        paramMap.put("withTitleColor", new Object[]{titleColor});
        return this;
    }

    @Override
    public TableInput withMulti(int multi) {
        config.setMulti(multi);
        paramMap.put("withMulti", new Object[]{multi});
        return this;
    }

    @Override
    public TableInput withHelp(String help) {
        config.setHelp(help);
        paramMap.put("withHelp", new Object[]{help});
        return this;
    }

    @Override
    public TableInput withTooltip(String tooltip) {
        config.setTooltip(tooltip);
        paramMap.put("withTooltip", new Object[]{tooltip});
        return this;
    }

    @Override
    public TableInput withRequire() {
        config.setRequire(true);
        paramMap.put("withRequire", new Object[0]);
        return this;
    }

    @Override
    public TableInput withLineFeed() {
        config.setBr(true);
        paramMap.put("withLineFeed", new Object[0]);
        return this;
    }

    public TableInput withDisabled() {
        config.setDisabled(true);
        paramMap.put("withDisabled", new Object[0]);
        return this;
    }

    public TableInput withHidden() {
        config.setHidden(true);
        paramMap.put("withHidden", new Object[0]);
        return this;
    }

    @Override
    public TableInput withNotRequireCondition(String notRequireCondition) {
        config.setNotRequireCondition(notRequireCondition);
        paramMap.put("withNotRequireCondition", new Object[]{notRequireCondition});
        return this;
    }

    @Override
    public TableInput withShowCondition(String showCondition) {
        config.setShowCondition(showCondition);
        paramMap.put("withShowCondition", new Object[]{showCondition});
        return this;
    }

    @Override
    public TableInput withHiddenCondition(String hiddenCondition) {
        config.setHiddenCondition(hiddenCondition);
        paramMap.put("withHiddenCondition", new Object[]{hiddenCondition});
        return this;
    }

    public TableInput withTitleHidden() {
        this.hiddenTitle = true;
        paramMap.put("withTitleHidden", new Object[0]);
        return this;
    }

    public TableInput withAddButtonHidden() {
        this.hiddenAddButton = true;
        paramMap.put("withAddButtonHidden", new Object[0]);
        return this;
    }

    public TableInput withDefaultLineNumber(int number) {
        this.lineNum = number;
        paramMap.put("withDefaultLineNumber", new Object[]{number});
        return this;
    }

    @Deprecated
    public TableInput withSubType(JsonProviderMetaType type) {
        super.resetStructSubType(type);
        return this;
    }

    public TableInput withSubType(LayoutComponent component) {
        super.resetStructSubType(component);
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
        Map<String, Object> arrayVal = new HashMap<>();
        arrayVal.put("layout", "table");
        arrayVal.put("titleHidden", hiddenTitle);
        arrayVal.put("addButtonHidden", hiddenAddButton);
        if (lineNum > 0) {
            arrayVal.put("rowNumber", lineNum);
        }
        Map<String, Object> val = new HashMap<>();
        val.put("common", config);
        val.put("array", arrayVal);
        return val;
    }

    @Override
    public TableInput copy() {
        TableInput result = new TableInput();
        LayoutComponentTool.resetParam(result, paramMap, super.getParamMap());
        return result;
    }
}
