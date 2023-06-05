package com.learning.layoutsdk.component;

import com.learning.layoutsdk.component.annotation.LayoutMethodTag;
import com.learning.layoutsdk.component.definition.JsonProviderMetaType;
import com.learning.layoutsdk.enums.TypeDict;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @InterfaceName NumberComponent
 * @Description TODO
 * @Author hufei
 * @Date 2023/2/17 10:41
 * @Version 1.0
 */
public abstract class TextComponent extends AbstractLayoutComponent {

    @Data
    private static class ExpConfig {
        private String expScript;
        private String expMsg;
    }

    private ExpConfig expConfig = new ExpConfig();
    private Map<String, Object[]> paramMap = new HashMap<>();

    @LayoutMethodTag
    public void setExpScript(String expScript) {
        expConfig.setExpScript(expScript);
        paramMap.put("setExpScript", new Object[]{expScript});
    }

    @LayoutMethodTag
    public void setExpMsg(String expMsg) {
        expConfig.setExpMsg(expMsg);
        paramMap.put("setExpMsg", new Object[]{expMsg});
    }

    public Map<String, Object[]> getParamMap() {
        return paramMap;
    }

    protected Map<String, Object> parseFieldUnitValue(String[] units, String defaultUnit) {
        Map<String, Object> val = new HashMap<>();
        if (null != units) {
            val.put("defaultValue", Optional.ofNullable(defaultUnit).orElse(units[0]));
            val.put("value", units);
            val.put("key", "unit");
        }
        return val;
    }

    public Map<String, Object> parseFieldValue() {
        return generateRegexRuleList(expConfig.getExpScript(), expConfig.getExpMsg());
    }

    @Override
    public JsonProviderMetaType toSchema() {

        JsonProviderMetaType type = new JsonProviderMetaType();
        type.setId(getId());
        type.setType(TypeDict.Text.getName());
        type.setFields(new JsonProviderMetaType[]{generateField()});

        return type;
    }
}
