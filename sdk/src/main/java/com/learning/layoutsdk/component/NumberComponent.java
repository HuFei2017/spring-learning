package com.learning.layoutsdk.component;

import com.learning.layoutsdk.component.annotation.LayoutMethodTag;
import com.learning.layoutsdk.definition.JsonProviderMetaType;
import com.learning.layoutsdk.enums.RuleDict;
import com.learning.layoutsdk.enums.TypeDict;
import lombok.Data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @InterfaceName NumberComponent
 * @Description TODO
 * @Author hufei
 * @Date 2023/2/17 10:41
 * @Version 1.0
 */
public abstract class NumberComponent implements LayoutComponent {

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

    public Map<String, Object> parseFieldValue() {
        Map<String, Object> val = new HashMap<>();
        if (null != expConfig.getExpScript() && null != expConfig.getExpMsg()) {
            Map<String, String> exp = new HashMap<>();
            exp.put("regExp", expConfig.getExpScript());
            exp.put("message", expConfig.getExpMsg());
            val.put("rules", Collections.singletonList(exp));
        }
        return val;
    }

    public abstract String getNumberType();

    @Override
    public JsonProviderMetaType toSchema() {

        JsonProviderMetaType field = new JsonProviderMetaType();
        field.setId(RuleDict.Form.getId());
        field.setType(TypeDict.Ref.getName());
        field.setRefId(RuleDict.Form.getName());
        field.setValue(toConfigSchema());

        JsonProviderMetaType type = new JsonProviderMetaType();
        type.setId(getId());
        type.setType(TypeDict.Number.getName());
        type.setFormat(getNumberType());
        type.setFields(new JsonProviderMetaType[]{field});

        return type;
    }
}
