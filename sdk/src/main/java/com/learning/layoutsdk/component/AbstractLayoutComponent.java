package com.learning.layoutsdk.component;

import com.learning.layoutsdk.component.definition.JsonProviderMetaType;
import com.learning.layoutsdk.enums.RuleDict;
import com.learning.layoutsdk.enums.TypeDict;

import java.util.*;

/**
 * @ClassName AbstractLayoutComponent
 * @Description TODO
 * @Author hufei
 * @Date 2023/6/5 15:43
 * @Version 1.0
 */
abstract class AbstractLayoutComponent implements LayoutComponent {
    List<JsonProviderMetaType> generateExtraTypeList(JsonProviderMetaType objType) {
        if (null != objType && TypeDict.Struct.getName().equals(objType.getType())) {
            List<JsonProviderMetaType> types = new ArrayList<>();
            JsonProviderMetaType[] currentTypes = objType.getTypes();
            if (null != currentTypes) {
                objType.setTypes(null);
                types.addAll(new ArrayList<>(Arrays.asList(currentTypes)));
            }
            types.add(objType);
            return types;
        }
        return null;
    }

    JsonProviderMetaType generateField() {
        JsonProviderMetaType field = new JsonProviderMetaType();
        field.setId(RuleDict.Form.getId());
        field.setType(TypeDict.Ref.getName());
        field.setRefId(RuleDict.Form.getName());
        field.setValue(toConfigSchema());
        return field;
    }

    Map<String, Object> generateRegexRuleList(String expScript, String expMsg) {
        Map<String, Object> val = new HashMap<>();
        if (null != expScript && null != expMsg) {
            Map<String, String> exp = new HashMap<>();
            exp.put("regExp", expScript);
            exp.put("message", expMsg);
            val.put("rules", Collections.singletonList(exp));
        }
        return val;
    }
}
